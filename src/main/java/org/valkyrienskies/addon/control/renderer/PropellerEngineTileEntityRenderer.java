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

package org.valkyrienskies.addon.control.renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;
import org.valkyrienskies.addon.control.block.engine.BlockAirshipEngine;
import org.valkyrienskies.addon.control.tileentity.TileEntityLegacyEngine;
import org.valkyrienskies.mod.client.render.FastBlockModelRenderer;

public class PropellerEngineTileEntityRenderer extends
    TileEntitySpecialRenderer<TileEntityLegacyEngine> {

    @Override
    public void render(TileEntityLegacyEngine tileentity, double x, double y, double z,
        float partialTick,
        int destroyStage, float alpha) {
        IBlockState state = tileentity.getWorld().getBlockState(tileentity.getPos());
        if (state.getBlock() instanceof BlockAirshipEngine) {
            EnumFacing facing = state.getValue(BlockAirshipEngine.FACING);

            IBlockState engineRenderState = getRenderState(state);
            IBlockState propellerRenderState = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.shipWheel
                .getStateFromMeta(14);

            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder BufferBuilder = tessellator.getBuffer();

            double oldX = BufferBuilder.xOffset;
            double oldY = BufferBuilder.yOffset;
            double oldZ = BufferBuilder.zOffset;

            BufferBuilder.setTranslation(0, 0, 0);
            GL11.glTranslated(x, y, z);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();

            int brightness = tileentity.getWorld().getCombinedLight(tileentity.getPos(), 0);

            // GL11.glScaled(1.2D, 1.2D, 1.2D);

            GL11.glTranslated(0.5D, 0.5D, 0.5D);

            switch (facing) {
                case UP:
                    GL11.glRotated(-90, 1, 0, 0);
                    break;
                case DOWN:
                    GL11.glRotated(90, 1, 0, 0);
                    break;
                case NORTH:
                    GL11.glRotated(180, 0, 1, 0);
                    break;
                case EAST:
                    GL11.glRotated(90, 0, 1, 0);
                    break;
                case SOUTH:
                    GL11.glRotated(0, 0, 1, 0);
                    break;
                case WEST:
                    GL11.glRotated(270, 0, 1, 0);
                    break;

            }

            GL11.glTranslated(-0.5D, -0.5D, -0.5D);

            FastBlockModelRenderer.renderBlockModel(tessellator, tileentity.getWorld(),
                engineRenderState, brightness);

            GL11.glPushMatrix();

            GL11.glTranslated(0.5D, 0.214D, 0.5D);
            GL11.glRotated(tileentity.getPropellerAngle(partialTick), 0, 0, 1);
            GL11.glScaled(1.5D, 1.5D, 1);
            GL11.glTranslated(-0.5D, -0.214D, -0.5D);

            FastBlockModelRenderer.renderBlockModel(tessellator, tileentity.getWorld(),
                propellerRenderState, brightness);

            GL11.glPopMatrix();

            GL11.glPopMatrix();

            BufferBuilder.setTranslation(oldX, oldY, oldZ);
        }
    }

    private IBlockState getRenderState(IBlockState inWorldState) {
        if (inWorldState.getBlock()
            == ValkyrienSkiesControl.INSTANCE.vsControlBlocks.ultimateEngine) {
            return ValkyrienSkiesControl.INSTANCE.vsControlBlocks.shipWheel.getStateFromMeta(9);
        }
        if (inWorldState.getBlock()
            == ValkyrienSkiesControl.INSTANCE.vsControlBlocks.redstoneEngine) {
            return ValkyrienSkiesControl.INSTANCE.vsControlBlocks.shipWheel.getStateFromMeta(10);
        }
        if (inWorldState.getBlock() == ValkyrienSkiesControl.INSTANCE.vsControlBlocks.eliteEngine) {
            return ValkyrienSkiesControl.INSTANCE.vsControlBlocks.shipWheel.getStateFromMeta(11);
        }
        if (inWorldState.getBlock() == ValkyrienSkiesControl.INSTANCE.vsControlBlocks.basicEngine) {
            return ValkyrienSkiesControl.INSTANCE.vsControlBlocks.shipWheel.getStateFromMeta(12);
        }
        if (inWorldState.getBlock()
            == ValkyrienSkiesControl.INSTANCE.vsControlBlocks.advancedEngine) {
            return ValkyrienSkiesControl.INSTANCE.vsControlBlocks.shipWheel.getStateFromMeta(13);
        }

        return ValkyrienSkiesControl.INSTANCE.vsControlBlocks.shipWheel.getStateFromMeta(9);
    }
}
