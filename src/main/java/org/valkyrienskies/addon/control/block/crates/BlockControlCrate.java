package org.valkyrienskies.addon.control.block.crates;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.valkyrienskies.addon.control.block.multiblocks.EnumMultiblockType;

public class BlockControlCrate extends BaseCrate {
	public BlockControlCrate() {
		super("control_crate", 6.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if (this.multiblockType == EnumMultiblockType.ENGINE) {
			return new TileEntityValkyriumEngine();
		}
		System.out.println(">>>>>>>>>>>>>> inb4 NPE");
		return null;
	}
}