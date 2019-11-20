package org.valkyrienskies.addon.control.block.crates;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.valkyrienskies.addon.control.block.multiblocks.EnumMultiblockType;
import org.valkyrienskies.addon.control.block.multiblocks.ITileEntityMultiblock;
import org.valkyrienskies.addon.control.util.BaseBlock;
import org.valkyrienskies.mod.common.block.IBlockForceProvider;

public abstract class BaseCrate extends BaseBlock implements ITileEntityProvider, IBlockForceProvider {
	protected static final PropertyBool CONSTRUCTED = PropertyBool.create("constructed");
	public EnumMultiblockType multiblockType = EnumMultiblockType.NONE;

	public BaseCrate(String name, float hardness) {
		super(name, Material.WOOD, 0, true);
		this.setHardness(hardness);
	}

	public BaseCrate(String name, Material mat, float hardness) {
		super(name, mat, 0, true);
		this.setHardness(hardness);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof ITileEntityMultiblock) {
			((ITileEntityMultiblock) tile).disassembleMultiblock();
		}
		super.breakBlock(worldIn, pos, state);
	}

    @Override
    public void addInformation(ItemStack stack, @Nullable World player,
        List<String> itemInformation, ITooltipFlag advanced) {
        itemInformation.add(TextFormatting.BLUE + I18n.format("tooltip.vs_control." + this.getRegistryName().getPath()));
    }

    // Lighting stuff
    @Override
    public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(CONSTRUCTED, this.multiblockType != EnumMultiblockType.NONE);
	}
}