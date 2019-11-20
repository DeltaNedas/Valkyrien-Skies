/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2015-2019 the Valkyrien Skies team
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income unless it is to be used as a part of a larger project (IE: "modpacks"), nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from the Valkyrien Skies team.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: The Valkyrien Skies team), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package org.valkyrienskies.addon.control.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import org.valkyrienskies.addon.control.nodenetwork.BasicForceNodeTileEntity;
import org.valkyrienskies.mod.common.math.Vector;

public class TileEntityLegacyEngine extends BasicForceNodeTileEntity {

    private double propellerAngle;
    private double prevPropellerAngle;
    private boolean isPowered;
    private double propellerAngularVelocity;

    public TileEntityLegacyEngine(Vector normalVeclocityUnoriented,
        boolean isForceOutputOriented, double maxThrust) {
        super(normalVeclocityUnoriented, isForceOutputOriented, maxThrust);
        this.isPowered = false;
        this.propellerAngle = Math.random() * 90D;
        this.prevPropellerAngle = propellerAngle;
        this.propellerAngularVelocity = 0;
    }

    public TileEntityLegacyEngine() {
        super();
        this.propellerAngle = Math.random() * 90D;
        this.prevPropellerAngle = propellerAngle;
    }

    public double getPropellerAngle(double partialTicks) {
        double delta = propellerAngle - prevPropellerAngle;
        if (Math.abs(delta) > 180D) {
            delta %= 180D;
            delta += 180D;
        }
        return prevPropellerAngle + delta * partialTicks;
    }

    @Override
    public void update() {
        super.update();
        isPowered = world.isBlockPowered(this.getPos());
        if (isPowered) {
            propellerAngularVelocity++;
        } else {
            propellerAngularVelocity *= Math.max(Math.random(), .9) * 1.05;
            propellerAngularVelocity -= .75 * Math.random() * Math.random();
        }
        propellerAngularVelocity = Math.max(0, Math.min(propellerAngularVelocity, 50));
        prevPropellerAngle = propellerAngle;
        propellerAngle += propellerAngularVelocity;
        propellerAngle %= 360D;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        propellerAngularVelocity = compound.getDouble("propellerAngularVelocity");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setDouble("propellerAngularVelocity", propellerAngularVelocity);
        return super.writeToNBT(compound);
    }
}
