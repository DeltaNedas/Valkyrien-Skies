package org.valkyrienskies.addon.control.block.crates;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityGiantPropeller;
import org.valkyrienskies.addon.control.block.multiblocks.EnumMultiblockType;
import org.valkyrienskies.mod.common.math.Vector;
import org.valkyrienskies.mod.common.physics.management.physo.PhysicsObject;

public class BlockGearsCrate extends BaseCrate {
	public BlockGearsCrate() {
		super("gears_crate", 6.0F);
	}

    @Nullable
    @Override
    public Vector getBlockForceInShipSpace(World world, BlockPos pos, IBlockState state,
        PhysicsObject physicsObject, double secondsToApply) {
        TileEntity tileEntity = world.getTileEntity(pos);
		if (this.multiblockType == EnumMultiblockType.PROPELLER &&
			tileEntity instanceof TileEntityGiantPropeller) {
            return ((TileEntityGiantPropeller) tileEntity).getForceOutputUnoriented(secondsToApply, physicsObject);
        }
        return null;
    }

    @Override
    public boolean shouldLocalForceBeRotated(World world, BlockPos pos, IBlockState state,
        double secondsToApply) {
		if (this.multiblockType == EnumMultiblockType.PROPELLER) {
			return true;
		}
		return false;
    }
}