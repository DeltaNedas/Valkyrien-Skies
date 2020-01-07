package org.valkyrienskies.addon.control.block.engine;

import org.valkyrienskies.addon.control.tileentity.TileEntityLegacyEngine;
import org.valkyrienskies.addon.control.util.BaseBlock;
import org.valkyrienskies.mod.common.block.IBlockForceProvider;
import org.valkyrienskies.mod.common.math.Vector;
import org.valkyrienskies.mod.common.physics.management.physo.PhysicsObject;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * All engines should extend this class, that way other kinds of engines can be made without making
 * tons of new classes for them. Only engines that add new functionality should have their own
 * class.
 */
public abstract class BlockAirshipEngine extends BaseBlock implements IBlockForceProvider,
    ITileEntityProvider {

    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    protected double enginePower;

    public BlockAirshipEngine(String name, Material mat, double enginePower, float hardness) {
        super(name + "_engine", mat, 0.0F, true);
        this.setEnginePower(enginePower);
        this.setHardness(hardness);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing,
        float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState()
            .withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        // &7 to remove any higher bits
        return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = state.getValue(FACING).getIndex();
        return i;
    }

    @Override
    public Vector getBlockForceInShipSpace(World world, BlockPos pos, IBlockState state,
        PhysicsObject physicsObject, double secondsToApply) {
        Vector acting = new Vector(0, 0, 0);
        if (!world.isBlockPowered(pos)) {
            return acting;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityLegacyEngine) {
            //Just set the Thrust to be the maximum
            ((TileEntityLegacyEngine) tileEntity)
                .setThrustMultiplierGoal(this.getEnginePower(world, pos, state, physicsObject));
            ((TileEntityLegacyEngine) tileEntity).updateTicksSinceLastRecievedSignal();
            ((TileEntityLegacyEngine) tileEntity).setThrustMultiplierGoal(1D);
            return ((TileEntityLegacyEngine) tileEntity)
                .getForceOutputUnoriented(secondsToApply, physicsObject);
        }

        return acting;
    }

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
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos,
        EnumFacing face) {
        return true;
    }

    /**
     * Used for calculating force applied to the airship by an engine. Override this in your
     * subclasses to make engines that are more dynamic than simply being faster engines.
     */
    public double getEnginePower(World world, BlockPos pos, IBlockState state,
        PhysicsObject physicsObject) {
        return this.enginePower;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        IBlockState state = getStateFromMeta(meta);
        return new TileEntityLegacyEngine(new Vector(state.getValue(FACING)), true, enginePower);
    }

    public void setEnginePower(double power) {
        this.enginePower = power;
    }

    /**
     * The spinning rotor engines must obey the rules of airflow, otherwise idiots would stick all
     * their engines inside leaving nothing exposed.
     */
    @Override
    public boolean doesForceSpawnParticles() {
        return true;
    }
}
