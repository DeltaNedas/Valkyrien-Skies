package org.valkyrienskies.addon.control.block.multiblocks;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.valkyrienskies.addon.control.MultiblockRegistry;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;

public class GiantPropellerMultiblockSchematic implements IMultiblockSchematic {

    private final List<BlockPosBlockPair> structureRelativeToCenter;
    private String schematicID;
    private int propellerRadius;
    private EnumFacing propellerFacing;

    public GiantPropellerMultiblockSchematic() {
        this.structureRelativeToCenter = new ArrayList<BlockPosBlockPair>();
        this.schematicID = MultiblockRegistry.EMPTY_SCHEMATIC_ID;
        this.propellerFacing = EnumFacing.NORTH;
    }

    @Override
    public void initializeMultiblockSchematic(String schematicID) {
        Block gearsCrate = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.gearsCrate;
		Block sailCrate = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.sailCrate;
		Block frame = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.frame;

        Vec3i perpAxisOne = null;
        Vec3i perpAxisTwo = null;
        switch (propellerFacing.getAxis()) {
            case X:
                perpAxisOne = new Vec3i(0, 1, 0);
                perpAxisTwo = new Vec3i(0, 0, 1);
                break;
            case Y:
                perpAxisOne = new Vec3i(1, 0, 0);
                perpAxisTwo = new Vec3i(0, 0, 1);
                break;
            case Z:
                perpAxisOne = new Vec3i(1, 0, 0);
                perpAxisTwo = new Vec3i(0, 1, 0);
                break;
        }

        for (int x = -propellerRadius; x <= propellerRadius; x++) {
			int horizX = perpAxisOne.getX() * x;
			int horizY = perpAxisOne.getY() * x;
			int horizZ = perpAxisOne.getZ() * x;
            for (int y = -propellerRadius; y <= propellerRadius; y++) {
				int relativeX = horizX + perpAxisTwo.getX() * y;
				int relativeY = horizY + perpAxisTwo.getY() * y;
				int relativeZ = horizZ + perpAxisTwo.getZ() * y;

				// Create cross shape of frames with gears crate centre
				// Was much simpler than I imagined...
				Block select = sailCrate;
				if (x == 0 && y == 0) {
					select = gearsCrate;
				} else if (x == 0 || y == 0) {
					select = frame;
				}
                structureRelativeToCenter.add(new BlockPosBlockPair(
						new BlockPos(relativeX, relativeY, relativeZ),
                        select));
            }
        }
        this.schematicID = schematicID;
    }

    @Override
    public List<BlockPosBlockPair> getStructureRelativeToCenter() {
        return structureRelativeToCenter;
    }

    @Override
    public String getSchematicPrefix() {
		return EnumMultiblockType.PROPELLER.toString();
    }

    @Override
    public String getSchematicID() {
        return this.schematicID;
    }

    @Override
    public void applyMultiblockCreation(World world, BlockPos tilePos, BlockPos relativePos) {
        TileEntity tileEntity = world.getTileEntity(tilePos);
        if (!(tileEntity instanceof TileEntityGiantPropeller)) {
            throw new IllegalStateException();
        }
        TileEntityGiantPropeller enginePart = (TileEntityGiantPropeller) tileEntity;
        enginePart.assembleMultiblock(this, relativePos);
    }

    @Override
    public List<IMultiblockSchematic> generateAllVariants() {
        List<IMultiblockSchematic> variants = new ArrayList<IMultiblockSchematic>();

        for (EnumFacing variantPropellerFacing : EnumFacing.values()) {
            for (int radius = 3; radius >= 1; radius--) {
                GiantPropellerMultiblockSchematic variant = new GiantPropellerMultiblockSchematic();

                variant.propellerRadius = radius;
                variant.propellerFacing = variantPropellerFacing;
                variant.initializeMultiblockSchematic(
                    getSchematicPrefix() + ":facing:" + variantPropellerFacing.toString()
                        + ":radius:" + radius);

                variants.add(variant);
            }
        }
        return variants;
    }

    public EnumFacing getPropellerFacing() {
        return propellerFacing;
    }

    public int getPropellerRadius() {
        return propellerRadius;
    }
}
