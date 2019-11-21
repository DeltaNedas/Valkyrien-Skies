package org.valkyrienskies.addon.control.block.crates;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
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
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityGiantPropeller;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityRudder;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityValkyriumCompressor;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityValkyriumEngine;
import org.valkyrienskies.addon.control.util.BaseBlock;
import org.valkyrienskies.mod.common.block.IBlockForceProvider;
import org.valkyrienskies.mod.common.config.VSConfig;
import org.valkyrienskies.mod.common.math.Vector;
import org.valkyrienskies.mod.common.physics.management.physo.PhysicsObject;

public class BaseCrate extends BaseBlock implements ITileEntityProvider, IBlockForceProvider {
	protected static final PropertyBool CONSTRUCTED = PropertyBool.create("constructed");
	public EnumMultiblockType multiblockType = EnumMultiblockType.NONE;

	protected BaseCrate(String name, float hardness) {
		super(name, Material.WOOD, 0, true);
		this.setHardness(hardness);
	}

	protected BaseCrate(String name, Material mat, float hardness) {
		super(name, mat, 0, true);
		this.setHardness(hardness);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if (this.multiblockType == EnumMultiblockType.COMPRESSOR) {
			return new TileEntityValkyriumCompressor(VSConfig.compressorMaxThrust);
		} else if (this.multiblockType == EnumMultiblockType.ENGINE) {
			return new TileEntityValkyriumEngine();
		} else if (this.multiblockType == EnumMultiblockType.PROPELLER) {
			return new TileEntityGiantPropeller();
		} else if (this.multiblockType == EnumMultiblockType.RUDDER) {
			return new TileEntityRudder();
		}
		return null;
	}

	@Override
    public boolean shouldLocalForceBeRotated(World world, BlockPos pos, IBlockState state,
        double secondsToApply) {
		if (this.multiblockType == EnumMultiblockType.NONE
			|| this.multiblockType == EnumMultiblockType.COMPRESSOR) {
			return false;
		}
        return true;
    }

    @Nullable
    @Override
    public Vector getBlockForceInShipSpace(World world, BlockPos pos, IBlockState state,
        PhysicsObject physicsObject, double secondsToApply) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityValkyriumCompressor) {
			return ((TileEntityValkyriumCompressor) tileEntity)
				.getForceOutputUnoriented(secondsToApply, physicsObject);
		} else if (tileEntity instanceof TileEntityGiantPropeller) {
			return ((TileEntityGiantPropeller) tileEntity)
				.getForceOutputUnoriented(secondsToApply, physicsObject);
		} else if (tileEntity instanceof TileEntityRudder) {
			Vector forceBeforeTimeScale = ((TileEntityRudder) tileEntity)
				.calculateForceFromVelocity(physicsObject);
			if (forceBeforeTimeScale != null && forceBeforeTimeScale.lengthSq() > 1) {
				return forceBeforeTimeScale.getProduct(secondsToApply);
			}
		}
        return null;
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

	// Blockstate stuff
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(CONSTRUCTED, this.multiblockType != EnumMultiblockType.NONE);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {CONSTRUCTED});
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(CONSTRUCTED, meta == 1);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(CONSTRUCTED) ? 1 : 0;
	}
}