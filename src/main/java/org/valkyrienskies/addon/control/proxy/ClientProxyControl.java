package org.valkyrienskies.addon.control.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import org.valkyrienskies.addon.control.ControlEventsClient;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityGiantPropeller;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityRudder;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityValkyriumCompressor;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityValkyriumEngine;
import org.valkyrienskies.addon.control.block.torque.TileEntityRotationAxle;
import org.valkyrienskies.addon.control.renderer.BasicNodeTileEntityRenderer;
import org.valkyrienskies.addon.control.renderer.GearboxTileEntityRenderer;
import org.valkyrienskies.addon.control.renderer.GiantPropellerTileEntityRenderer;
import org.valkyrienskies.addon.control.renderer.LiftLeverTileEntityRenderer;
import org.valkyrienskies.addon.control.renderer.PropellerEngineTileEntityRenderer;
import org.valkyrienskies.addon.control.renderer.RotationAxleTileEntityRenderer;
import org.valkyrienskies.addon.control.renderer.RudderTileEntityRenderer;
import org.valkyrienskies.addon.control.renderer.ShipHelmTileEntityRenderer;
import org.valkyrienskies.addon.control.renderer.SpeedTelegraphTileEntityRenderer;
import org.valkyrienskies.addon.control.renderer.ValkyriumCompressorTileEntityRenderer;
import org.valkyrienskies.addon.control.renderer.ValkyriumEngineTileEntityRenderer;
import org.valkyrienskies.addon.control.tileentity.TileEntityGearbox;
import org.valkyrienskies.addon.control.tileentity.TileEntityLiftLever;
import org.valkyrienskies.addon.control.tileentity.TileEntityNetworkRelay;
import org.valkyrienskies.addon.control.tileentity.TileEntityLegacyEngine;
import org.valkyrienskies.addon.control.tileentity.TileEntityShipHelm;
import org.valkyrienskies.addon.control.tileentity.TileEntitySpeedTelegraph;
import org.valkyrienskies.mod.client.render.GibsAnimationRegistry;
import org.valkyrienskies.mod.client.render.GibsModelRegistry;

@SuppressWarnings("unused")
public class ClientProxyControl extends CommonProxyControl {
    private static void registerTileEntityRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNetworkRelay.class,
            new BasicNodeTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShipHelm.class,
            new ShipHelmTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpeedTelegraph.class,
            new SpeedTelegraphTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLegacyEngine.class,
            new PropellerEngineTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityValkyriumEngine.class,
            new ValkyriumEngineTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGearbox.class,
            new GearboxTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiftLever.class,
            new LiftLeverTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityValkyriumCompressor.class,
            new ValkyriumCompressorTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRudder.class,
            new RudderTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGiantPropeller.class,
            new GiantPropellerTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRotationAxle.class,
            new RotationAxleTileEntityRenderer());
    }

    @Override
    public void preInit(FMLStateEvent event) {
        super.preInit(event);

        // Register events
        MinecraftForge.EVENT_BUS.register(new ControlEventsClient());
        // Register gibs
        OBJLoader.INSTANCE.addDomain(ValkyrienSkiesControl.MOD_ID.toLowerCase());

        registerControlGibs("chadburn_dial_simplevoxel_geo");
        registerControlGibs("chadburn_glass_simplevoxel_geo");
        registerControlGibs("chadburn_handles_simplevoxel_geo");
        registerControlGibs("chadburn_speed_telegraph_simplevoxel_geo");

        registerControlGibs("ship_helm_base");
        registerControlGibs("ship_helm_dial");
        registerControlGibs("ship_helm_dial_glass");
        registerControlGibs("ship_helm_wheel");

        registerRudderGibs("rudder_geo");
        registerRudderGibs("rudder_axle_geo");

        registerGearboxGibs("gearbox_back_geo");
        registerGearboxGibs("gearbox_bottom_geo");
        registerGearboxGibs("gearbox_front_geo");
        registerGearboxGibs("gearbox_left_geo");
        registerGearboxGibs("gearbox_right_geo");
        registerGearboxGibs("gearbox_top_geo");

        GibsAnimationRegistry.registerAnimation("valkyrium_compressor",
            new ResourceLocation(ValkyrienSkiesControl.MOD_ID,
                "models/block/valkyrium_compressor/compressor_animations.atom"));

        GibsAnimationRegistry.registerAnimation("valkyrium_engine",
            new ResourceLocation(ValkyrienSkiesControl.MOD_ID,
                "models/block/valkyrium_engine/valkyrium_engine.atom"));

        GibsAnimationRegistry.registerAnimation("lift_lever",
            new ResourceLocation(ValkyrienSkiesControl.MOD_ID,
                "models/block/controls/lift_lever_keyframes.atom"));

        GibsAnimationRegistry.registerAnimation("gearbox",
            new ResourceLocation(ValkyrienSkiesControl.MOD_ID,
                "models/block/gearbox/gearbox.atom"));

        GibsAnimationRegistry.registerAnimation("pocketwatch_body",
            new ResourceLocation(ValkyrienSkiesControl.MOD_ID,
                "models/block/pocketwatch/pocketwatch_keyframes.atom"));

        GibsAnimationRegistry.registerAnimation("pocketwatch_lid",
            new ResourceLocation(ValkyrienSkiesControl.MOD_ID,
                "models/block/pocketwatch/pocketwatch_lid_keyframes.atom"));

        GibsAnimationRegistry.registerAnimation("telescope",
            new ResourceLocation(ValkyrienSkiesControl.MOD_ID,
                "models/block/telescope/telescope_keyframes.atom"));

        GibsAnimationRegistry.registerAnimation("rudder",
            new ResourceLocation(ValkyrienSkiesControl.MOD_ID,
                "models/block/rudder/rudder_animation.atom"));

        GibsAnimationRegistry.registerAnimation("rotation_axle",
            new ResourceLocation(ValkyrienSkiesControl.MOD_ID,
                "models/block/rotation_axle/rotation_axle.atom"));

        GibsAnimationRegistry.registerAnimation("giant_propeller",
            new ResourceLocation(ValkyrienSkiesControl.MOD_ID,
                "models/block/giant_propeller/giant_propeller.atom"));
    }

    private void registerGearboxGibs(String name) {
        GibsModelRegistry.registerGibsModel(name,
            new ResourceLocation(ValkyrienSkiesControl.MOD_ID, "block/gearbox/" + name + ".obj"));
    }

    private void registerControlGibs(String name) {
        GibsModelRegistry.registerGibsModel(name,
            new ResourceLocation(ValkyrienSkiesControl.MOD_ID, "block/controls/" + name + ".obj"));
    }

    private void registerRudderGibs(String name) {
        GibsModelRegistry.registerGibsModel(name, new ResourceLocation(ValkyrienSkiesControl.MOD_ID,
            "block/rudder/" + name + ".obj"));
    }

    @Override
    public void init(FMLStateEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLStateEvent event) {
        super.postInit(event);
        registerTileEntityRenderers();
    }

}
