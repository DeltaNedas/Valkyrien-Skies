package org.valkyrienskies.addon.control.block.multiblocks;

import net.minecraft.nbt.NBTTagCompound;
import org.valkyrienskies.addon.control.nodenetwork.IForceTile;

public abstract class TileEntityMultiblockForce<E extends IMultiblockSchematic, F extends TileEntityMultiblockForce> extends
    TileEntityMultiblock<E, F> implements IForceTile {

    private double thrustMultiplierGoal;
    private double maxThrust;

    public TileEntityMultiblockForce() {
        super();
        this.thrustMultiplierGoal = 0;
        this.maxThrust = 0;
    }

    @Override
    public double getMaxThrust() {
        return maxThrust;
    }

    @Override
    public void setMaxThrust(double maxThrust) {
        this.maxThrust = maxThrust;
    }

    @Override
    public double getThrustMultiplierGoal() {
        return thrustMultiplierGoal;
    }

    @Override
    public void setThrustMultiplierGoal(double thrustMultiplierGoal) {
        this.thrustMultiplierGoal = thrustMultiplierGoal;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound toReturn = super.writeToNBT(compound);
        toReturn.setDouble("thrustMultiplierGoal", thrustMultiplierGoal);
        toReturn.setDouble("maxThrust", maxThrust);
        return toReturn;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.thrustMultiplierGoal = compound.getDouble("thrustMultiplierGoal");
        this.maxThrust = compound.getDouble("maxThrust");
    }

}
