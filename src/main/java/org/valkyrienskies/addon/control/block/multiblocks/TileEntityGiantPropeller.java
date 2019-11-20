package org.valkyrienskies.addon.control.block.multiblocks;

import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.valkyrienskies.addon.control.MultiblockRegistry;
import org.valkyrienskies.addon.control.block.torque.IRotationNode;
import org.valkyrienskies.addon.control.block.torque.IRotationNodeProvider;
import org.valkyrienskies.addon.control.block.torque.IRotationNodeWorld;
import org.valkyrienskies.addon.control.block.torque.IRotationNodeWorldProvider;
import org.valkyrienskies.addon.control.block.torque.ImplRotationNode;
import org.valkyrienskies.fixes.VSNetwork;
import org.valkyrienskies.mod.common.coordinates.VectorImmutable;
import org.valkyrienskies.mod.common.math.Vector;
import org.valkyrienskies.mod.common.physics.management.physo.PhysicsObject;
import org.valkyrienskies.mod.common.util.ValkyrienUtils;

public class TileEntityGiantPropeller extends
    TileEntityMultiblockForce<GiantPropellerMultiblockSchematic, TileEntityGiantPropeller> implements
    IRotationNodeProvider<TileEntityGiantPropeller> {

    public static final int GIANT_PROPELLER_SORTING_PRIORITY = 50;
    private final IRotationNode rotationNode;
    private double prevPropellerAngle;
    private double propellerAngle;
    private double nextPropellerAngle;
    private boolean firstUpdate;

    public TileEntityGiantPropeller() {
        super();
        this.rotationNode = new ImplRotationNode<>(this, 5, GIANT_PROPELLER_SORTING_PRIORITY);
        this.firstUpdate = true;
    }

    @Override
    public double getMaxThrust() {
        return 999999;
    }

    @Override
    public VectorImmutable getForceOutputNormal(double secondsToApply,
        PhysicsObject physicsObject) {
        if (!this.isPartOfAssembledMultiblock()) {
            return null;
        } else {
            if (!this.isMaster()) {
                TileEntityGiantPropeller master = this.getMaster();
                if (master != null) {
                    return master.getForceOutputNormal(secondsToApply, physicsObject);
                } else {
                    return null;
                }
            } else {
                if (!this.getRotationNode().isPresent()) {
                    return null;
                } else if (this.getRotationNode().get().getAngularVelocity() == 0) {
                    return null;
                }
                Vector facingDir = new Vector(this.getPropellerFacing().getDirectionVec());
                final double angularVelocity = this.getRotationNode().get().getAngularVelocity();
                if (angularVelocity != 0) {
                    // We don't want the propeller animation and force to be backwards.
                    facingDir.multiply(-Math.signum(angularVelocity));
                }
                return new VectorImmutable(facingDir);
            }
        }
    }

    @Override
    public double getThrustMagnitude() {
        if (!this.isPartOfAssembledMultiblock()) {
            return 0;
        } else {
            if (!this.isMaster()) {
                TileEntityGiantPropeller master = this.getMaster();
                if (master != null) {
                    return master.getThrustMagnitude();
                } else {
                    return 0;
                }
            } else {
                if (!this.getRotationNode().isPresent()) {
                    return 0;
                }
                double angularVel = this.getRotationNode().get().getAngularVelocity();
                // Temporary simple thrust function.
                return 500D * angularVel * angularVel;
            }
        }
    }

    @Override
    public void update() {
        if (!this.getWorld().isRemote) {
            if (firstUpdate) {
                this.rotationNode.markInitialized();
                this.rotationNode.queueTask(() -> this.rotationNode.setAngularVelocityRatio(
                    this.getMultiBlockSchematic().getPropellerFacing().getOpposite(),
                    Optional.of(-1D)));
                firstUpdate = false;
            }

            if (this.isPartOfAssembledMultiblock()) {
                Optional<PhysicsObject> physicsObjectOptional = ValkyrienUtils
                    .getPhysoManagingBlock(getWorld(), getPos());
                if (this.isMaster()) {
                    if (!rotationNode.hasBeenPlacedIntoNodeWorld()) {
                        IRotationNodeWorld nodeWorld;
                        if (physicsObjectOptional.isPresent()) {
                            nodeWorld = physicsObjectOptional.get()
                                .getPhysicsCalculations().getPhysicsRotationNodeWorld();
                        } else {
                            IRotationNodeWorldProvider provider = (IRotationNodeWorldProvider) getWorld();
                            nodeWorld = provider.getPhysicsRotationNodeWorld();
                        }

                        nodeWorld.enqueueTaskOntoWorld(
                            () -> nodeWorld.setNodeFromPos(getPos(), rotationNode));

                        final int propellerRadius = this.getMultiBlockSchematic()
                            .getPropellerRadius();
                        this.rotationNode.queueTask(() -> this.rotationNode
                            .setRotationalInertia(propellerRadius * propellerRadius));
                    }
                    this.prevPropellerAngle = this.propellerAngle;
                    // May need to convert to degrees from radians.
                    this.propellerAngle = Math
                        .toDegrees(rotationNode.getAngularRotationUnsynchronized());
                }
                VSNetwork.sendTileToAllNearby(this);
            }
        } else {
            this.prevPropellerAngle = this.propellerAngle;
            double increment = nextPropellerAngle - propellerAngle;
            if (increment < 0) {
                increment = MathHelper.wrapDegrees(increment);
            }
            this.propellerAngle = this.propellerAngle + increment * .75;
        }
    }

    @Override
    public boolean attemptToAssembleMultiblock(World worldIn, BlockPos pos, EnumFacing facing) {
		List<IMultiblockSchematic> schematics = MultiblockRegistry.getSchematicsWithPrefix(EnumMultiblockType.PROPELLER.toString());
        for (IMultiblockSchematic schematic : schematics) {
            GiantPropellerMultiblockSchematic propSchem = (GiantPropellerMultiblockSchematic) schematic;
            if (propSchem.getPropellerFacing() == facing && schematic.attemptToCreateMultiblock(worldIn, pos, EnumMultiblockType.PROPELLER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void disassembleMultiblockLocal() {
        super.disassembleMultiblockLocal();

        Optional<PhysicsObject> object = ValkyrienUtils.getPhysoManagingBlock(getWorld(), getPos());
        object.ifPresent(obj -> this.rotationNode.queueTask(rotationNode::resetNodeData));
    }

    @Override
    public Optional<IRotationNode> getRotationNode() {
        if (rotationNode.isInitialized()) {
            return Optional.of(rotationNode);
        } else {
            return Optional.empty();
        }
    }

    public EnumFacing getPropellerFacing() {
        if (!this.isPartOfAssembledMultiblock()) {
            return null;
        }
        return getMultiBlockSchematic().getPropellerFacing();
    }

    public int getPropellerRadius() {
        if (!this.isPartOfAssembledMultiblock()) {
            return 1;
        }
        return getMultiBlockSchematic().getPropellerRadius();
    }

    public float getPropellerAngle(float partialTick) {
        return (float) (prevPropellerAngle + (propellerAngle - prevPropellerAngle) * partialTick);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        rotationNode.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        rotationNode.writeToNBT(compound);
        return compound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound toSend = super.writeToNBT(new NBTTagCompound());
        toSend.setDouble("propeller_angle", propellerAngle);
        // Use super.writeToNBT to avoid sending the rotation node over nbt.
        return new SPacketUpdateTileEntity(pos, 0, toSend);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.nextPropellerAngle = pkt.getNbtCompound().getDouble("propeller_angle");
    }
}
