package org.valkyrienskies.addon.control.block.multiblocks;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITileEntityMultiblock<E extends IMultiblockSchematic, F extends ITileEntityMultiblock> {
    boolean isPartOfAssembledMultiblock();
    boolean isMaster();
    F getMaster();
    BlockPos getMultiblockOrigin();
    BlockPos getRelativePos();
    void disassembleMultiblock();
    void disassembleMultiblockLocal();
    void assembleMultiblock(E schematic, BlockPos relativePos);
    boolean attemptToAssembleMultiblock(World worldIn, BlockPos pos, EnumFacing facing);
    E getMultiBlockSchematic();
}
