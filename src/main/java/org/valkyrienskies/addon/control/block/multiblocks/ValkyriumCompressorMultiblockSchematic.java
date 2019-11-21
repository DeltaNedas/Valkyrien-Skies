package org.valkyrienskies.addon.control.block.multiblocks;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.valkyrienskies.addon.control.MultiblockRegistry;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;

public class ValkyriumCompressorMultiblockSchematic implements IMultiblockSchematic {

    private final List<BlockPosBlockPair> structureRelativeToCenter;
    private String schematicID;
    private EnumMultiblockRotation multiblockRotation;

    public ValkyriumCompressorMultiblockSchematic() {
        this.structureRelativeToCenter = new ArrayList<BlockPosBlockPair>();
        this.schematicID = MultiblockRegistry.EMPTY_SCHEMATIC_ID;
    }

    @Override
    public void initializeMultiblockSchematic(String schematicID) {
		Block boilerCrate = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.boilerCrate;
		Block controlCrate = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.controlCrate;
		Block frame = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.frame;
		Block gearsCrate = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.gearsCrate;
        Block pistonCrate = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.pistonCrate;

		// Layer 1
		structureRelativeToCenter.add(new BlockPosBlockPair(new BlockPos(0, 0, 0), controlCrate));
		structureRelativeToCenter.add(new BlockPosBlockPair(new BlockPos(1, 0, 0), frame));
		structureRelativeToCenter.add(new BlockPosBlockPair(new BlockPos(0, 0, 1), frame));
		structureRelativeToCenter.add(new BlockPosBlockPair(new BlockPos(1, 0, 1), frame));

		// Layer 2
		structureRelativeToCenter.add(new BlockPosBlockPair(new BlockPos(0, 0, 0), gearsCrate));
		structureRelativeToCenter.add(new BlockPosBlockPair(new BlockPos(1, 0, 0), boilerCrate));
		structureRelativeToCenter.add(new BlockPosBlockPair(new BlockPos(0, 0, 1), pistonCrate));
		structureRelativeToCenter.add(new BlockPosBlockPair(new BlockPos(1, 0, 1), pistonCrate));

		this.schematicID = schematicID;
    }

    @Override
    public List<BlockPosBlockPair> getStructureRelativeToCenter() {
        return structureRelativeToCenter;
    }

    @Override
    public String getSchematicID() {
        return this.schematicID;
    }

    @Override
    public void applyMultiblockCreation(World world, BlockPos tilePos, BlockPos relativePos) {
        TileEntity tileEntity = world.getTileEntity(tilePos);
        if (!(tileEntity instanceof TileEntityValkyriumCompressor)) {
            throw new IllegalStateException();
        }
        TileEntityValkyriumCompressor compressorPart = (TileEntityValkyriumCompressor) tileEntity;
		compressorPart.assembleMultiblock(this, relativePos);
    }

    @Override
    public String getSchematicPrefix() {
		return EnumMultiblockType.COMPRESSOR.toString();
    }

    @Override
    public List<IMultiblockSchematic> generateAllVariants() {
        List<IMultiblockSchematic> variants = new ArrayList<IMultiblockSchematic>();

        for (EnumMultiblockRotation potentialRotation : EnumMultiblockRotation.values()) {
            ValkyriumCompressorMultiblockSchematic variant = new ValkyriumCompressorMultiblockSchematic();

            variant.initializeMultiblockSchematic(
                getSchematicPrefix() + ":rot:" + potentialRotation.toString());

            List<BlockPosBlockPair> rotatedPairs = new ArrayList<BlockPosBlockPair>();
            for (BlockPosBlockPair unrotatedPairs : variant.structureRelativeToCenter) {
                BlockPos rotatedPos = potentialRotation.rotatePos(unrotatedPairs.getPos());
                rotatedPairs.add(new BlockPosBlockPair(rotatedPos, unrotatedPairs.getBlock()));
            }
            variant.structureRelativeToCenter.clear();
            variant.structureRelativeToCenter.addAll(rotatedPairs);
            variant.multiblockRotation = potentialRotation;
            variants.add(variant);
        }
        return variants;
    }

    @Override
    public EnumMultiblockRotation getMultiblockRotation() {
        return multiblockRotation;
    }

}
