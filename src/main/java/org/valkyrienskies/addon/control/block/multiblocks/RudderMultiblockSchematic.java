package org.valkyrienskies.addon.control.block.multiblocks;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.valkyrienskies.addon.control.MultiblockRegistry;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;
import org.valkyrienskies.mod.common.config.VSConfig;

public class RudderMultiblockSchematic implements IMultiblockSchematic {

    private final List<BlockPosBlockPair> structureRelativeToCenter;
    private String schematicID;
    private int axleLength;
    private EnumFacing axleAxis;
    private EnumFacing axleFacing;

    public RudderMultiblockSchematic() {
        this.structureRelativeToCenter = new ArrayList<BlockPosBlockPair>();
        this.schematicID = MultiblockRegistry.EMPTY_SCHEMATIC_ID;
        this.axleLength = -1;
        this.axleAxis = EnumFacing.UP;
        this.axleFacing = EnumFacing.UP;
    }

    @Override
    public void initializeMultiblockSchematic(String schematicID) {
        this.schematicID = schematicID;
    }

    @Override
    public List<BlockPosBlockPair> getStructureRelativeToCenter() {
        return structureRelativeToCenter;
    }

    @Override
    public String getSchematicPrefix() {
		return EnumMultiblockType.RUDDER.toString();
    }

    @Override
    public String getSchematicID() {
        return schematicID;
    }

    @Override
    public void applyMultiblockCreation(World world, BlockPos tilePos, BlockPos relativePos) {
        TileEntity tileEntity = world.getTileEntity(tilePos);
        if (!(tileEntity instanceof TileEntityRudder)) {
            throw new IllegalStateException();
        }
        TileEntityRudder enginePart = (TileEntityRudder) tileEntity;
        enginePart.assembleMultiblock(this, relativePos);
    }

    @Override
    public List<IMultiblockSchematic> generateAllVariants() {
        Block axleBlock = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.sailCrate;
		Block baseBlock = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.controlCrate;
        // Order matters here
        List<IMultiblockSchematic> variants = new ArrayList<IMultiblockSchematic>();
        for (int length = VSConfig.maxRudderLength; length >= 2; length--) {
            for (EnumFacing possibleAxleAxisDirection : EnumFacing.VALUES) {
                for (EnumFacing possibleAxleFacingDirection : EnumFacing.VALUES) {
                    if (possibleAxleAxisDirection.getAxis() != possibleAxleFacingDirection
                        .getAxis()) {
                        BlockPos originPos = new BlockPos(0, 0, 0);
                        RudderMultiblockSchematic schematicVariant = new RudderMultiblockSchematic();
                        schematicVariant.initializeMultiblockSchematic(
                            getSchematicPrefix() + "axle_axis_direction:"
                                + possibleAxleAxisDirection.toString() + ":axle_facing:"
                                + possibleAxleFacingDirection.toString() + ":length:" + length);
                        schematicVariant.axleAxis = possibleAxleAxisDirection;
                        schematicVariant.axleFacing = possibleAxleFacingDirection;
                        schematicVariant.axleLength = length;
                            schematicVariant.structureRelativeToCenter
                                .add(new BlockPosBlockPair(
                                    BlockPos.ORIGIN.offset(possibleAxleAxisDirection, 0),
                                    baseBlock));
                        for (int i = 1; i < length; i++) {
                            schematicVariant.structureRelativeToCenter
                                .add(new BlockPosBlockPair(
                                    BlockPos.ORIGIN.offset(possibleAxleAxisDirection, i),
                                    axleBlock));
                        }
                        variants.add(schematicVariant);
                    }
                }
            }
        }
        return variants;
    }

    public int getAxleLength() {
        return axleLength;
    }

    public EnumFacing getAxleAxisDirection() {
        return axleAxis;
    }

    public EnumFacing getAxleFacingDirection() {
        return axleFacing;
    }

}
