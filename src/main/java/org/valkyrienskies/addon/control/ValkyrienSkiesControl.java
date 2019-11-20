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

package org.valkyrienskies.addon.control;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.GameData;
import org.valkyrienskies.addon.control.block.multiblocks.GiantPropellerMultiblockSchematic;
import org.valkyrienskies.addon.control.block.multiblocks.RudderMultiblockSchematic;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityGiantPropeller;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityRudder;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityValkyriumCompressor;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityValkyriumEngine;
import org.valkyrienskies.addon.control.block.multiblocks.ValkyriumCompressorMultiblockSchematic;
import org.valkyrienskies.addon.control.block.multiblocks.ValkyriumEngineMultiblockSchematic;
import org.valkyrienskies.addon.control.block.torque.TileEntityRotationAxle;
import org.valkyrienskies.addon.control.capability.ICapabilityLastRelay;
import org.valkyrienskies.addon.control.capability.ImplCapabilityLastRelay;
import org.valkyrienskies.addon.control.capability.StorageLastRelay;
import org.valkyrienskies.addon.control.item.ItemBaseWire;
import org.valkyrienskies.addon.control.item.ItemIronGear;
import org.valkyrienskies.addon.control.item.ItemSailFabric;
import org.valkyrienskies.addon.control.item.ItemVSWrench;
import org.valkyrienskies.addon.control.network.MessagePlayerStoppedPiloting;
import org.valkyrienskies.addon.control.network.MessagePlayerStoppedPilotingHandler;
import org.valkyrienskies.addon.control.network.MessageStartPiloting;
import org.valkyrienskies.addon.control.network.MessageStartPilotingHandler;
import org.valkyrienskies.addon.control.network.MessageStopPiloting;
import org.valkyrienskies.addon.control.network.MessageStopPilotingHandler;
import org.valkyrienskies.addon.control.piloting.PilotControlsMessage;
import org.valkyrienskies.addon.control.piloting.PilotControlsMessageHandler;
import org.valkyrienskies.addon.control.proxy.CommonProxyControl;
import org.valkyrienskies.addon.control.tileentity.TileEntityCaptainsChair;
import org.valkyrienskies.addon.control.tileentity.TileEntityGearbox;
import org.valkyrienskies.addon.control.tileentity.TileEntityGyroscopeDampener;
import org.valkyrienskies.addon.control.tileentity.TileEntityGyroscopeStabilizer;
import org.valkyrienskies.addon.control.tileentity.TileEntityLiftLever;
import org.valkyrienskies.addon.control.tileentity.TileEntityLiftValve;
import org.valkyrienskies.addon.control.tileentity.TileEntityNetworkDisplay;
import org.valkyrienskies.addon.control.tileentity.TileEntityNetworkRelay;
import org.valkyrienskies.addon.control.tileentity.TileEntityPassengerChair;
import org.valkyrienskies.addon.control.tileentity.TileEntityLegacyEngine;
import org.valkyrienskies.addon.control.tileentity.TileEntityShipHelm;
import org.valkyrienskies.addon.control.tileentity.TileEntitySpeedTelegraph;
import org.valkyrienskies.addon.world.ValkyrienSkiesWorld;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;

@Mod(
    name = ValkyrienSkiesControl.MOD_NAME,
    modid = ValkyrienSkiesControl.MOD_ID,
    version = ValkyrienSkiesControl.MOD_VERSION,
    dependencies = "required-after:" + ValkyrienSkiesWorld.MOD_ID
)
@Mod.EventBusSubscriber(modid = ValkyrienSkiesControl.MOD_ID)
public class ValkyrienSkiesControl {
    // Used for registering stuff
    public static final List<Block> BLOCKS = new ArrayList<Block>();
    public static final List<Item> ITEMS = new ArrayList<Item>();

    // MOD INFO CONSTANTS
    public static final String MOD_ID = "vs_control";
    public static final String MOD_NAME = "Valkyrien Skies Control";
    public static final String MOD_VERSION = ValkyrienSkiesMod.MOD_VERSION;

    // MOD INSTANCE
    @Instance(MOD_ID)
    public static ValkyrienSkiesControl INSTANCE;

    @SidedProxy(
        clientSide = "org.valkyrienskies.addon.control.proxy.ClientProxyControl",
        serverSide = "org.valkyrienskies.addon.control.proxy.CommonProxyControl")
    private static CommonProxyControl proxy;

    @CapabilityInject(ICapabilityLastRelay.class)
    public static final Capability<ICapabilityLastRelay> lastRelayCapability = null;

    // MOD CLASS MEMBERS
    public static SimpleNetworkWrapper controlNetwork;
    public BlocksValkyrienSkiesControl vsControlBlocks;
    public Item relayWire;
    public Item vanishingWire;
    public Item vsWrench;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
        Block[] blockArray = BLOCKS.toArray(new Block[0]);
        event.getRegistry().registerAll(blockArray);
	}

	public void addBlocks() {
		INSTANCE.vsControlBlocks = new BlocksValkyrienSkiesControl();
	}

	public void registerMultiblocks() {
        MultiblockRegistry
        .registerAllPossibleSchematicVariants(ValkyriumEngineMultiblockSchematic.class);
        MultiblockRegistry
        .registerAllPossibleSchematicVariants(ValkyriumCompressorMultiblockSchematic.class);
        MultiblockRegistry
        .registerAllPossibleSchematicVariants(RudderMultiblockSchematic.class);
        MultiblockRegistry
        .registerAllPossibleSchematicVariants(GiantPropellerMultiblockSchematic.class);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ITEMS.toArray(new Item[0]));
    }

    public void addItems() {
		INSTANCE.relayWire = new ItemBaseWire(EnumWireType.RELAY);
		INSTANCE.vanishingWire = new ItemBaseWire(EnumWireType.VANISHING);
		INSTANCE.vsWrench = new ItemVSWrench();
		INSTANCE.sailFabric = new ItemSailFabric();
		INSTANCE.ironGear = new ItemIronGear();
	}

    public void registerRecipes() {
		addShapedRecipe(INSTANCE.vsControlBlocks.captainsChair, 1,
            "SLS",
            "VWV",
            " S ",
            'S', Items.STICK,
            'L', Items.LEATHER,
            'W', Item.getItemFromBlock(Blocks.LOG),
            'V', ValkyrienSkiesWorld.INSTANCE.valkyriumCrystal);
		addShapedRecipe(INSTANCE.vsControlBlocks.passengerChair, 1,
            "SLS",
            "PWP",
            " S ",
            'S', Items.STICK,
            'L', Items.LEATHER,
            'W', Item.getItemFromBlock(Blocks.LOG),
            'P', Item.getItemFromBlock(Blocks.PLANKS));

		addEngineRecipe(INSTANCE.vsControlBlocks.basicEngine, Blocks.PLANKS);
		addEngineRecipe(INSTANCE.vsControlBlocks.advancedEngine, Blocks.STONE);
		addEngineRecipe(INSTANCE.vsControlBlocks.advancedEngine, Blocks.COBBLESTONE);
		addEngineRecipe(INSTANCE.vsControlBlocks.eliteEngine, Items.IRON_INGOT);
		addEngineRecipe(INSTANCE.vsControlBlocks.ultimateEngine, Blocks.OBSIDIAN);
		addEngineRecipe(INSTANCE.vsControlBlocks.redstoneEngine, Blocks.REDSTONE_BLOCK);
        Item relayWireIngot = Items.IRON_INGOT;
        // TODO: Code to check for copper and set relayWireIngot

        addShapedRecipe(INSTANCE.relayWire, 4, // 1 per copper/iron ingot
            " IS",
            "ISI",
            "SI ",
            'I', relayWireIngot,
            'S', Items.STICK);
        addShapedRecipe(INSTANCE.relayWire, 4, // flipped
            "SI ",
            "ISI",
            " IS",
            'I', relayWireIngot,
            'S', Items.STICK);
		addShapedRecipe(INSTANCE.vanishingWire, 8,
            "WWW",
            "WVW",
            "WWW",
            'W', INSTANCE.relayWire,
            'V', ValkyrienSkiesWorld.INSTANCE.valkyriumCrystal);
        addShapedRecipe(INSTANCE.sailFabric, 4,
            "SWS",
            "SWS",
            "SWS",
            'S', Items.STRING,
            'W', Item.getItemFromBlock(Blocks.WOOL));
        addShapedRecipe(INSTANCE.ironGear, 1,
            " I ",
            "ISI",
            " I ",
            'S', Items.STICK,
            'I', Items.IRON_INGOT);

		addShapedRecipe(INSTANCE.vsWrench, 1,
			" I ",
			" VI",
			"I  ",
			'I', Items.IRON_INGOT,
			'V', ValkyrienSkiesWorld.INSTANCE.valkyriumCrystal);
        addShapedRecipe(INSTANCE.vsControlBlocks.compactedValkyrium, 1,
            "VVV",
            "VVV",
            "VVV",
            'V', ValkyrienSkiesWorld.INSTANCE.valkyriumCrystal);

		addShapedRecipe(INSTANCE.vsControlBlocks.frame,
            "sSs",
            "s s",
            "sSs",
            'S', Item.getItemFromBlock(Blocks.WOODEN_SLAB),
            's', Items.STICK);

        addShapedRecipe(INSTANCE.vsControlBlocks.crate,
            "SSS",
            "FfF",
            "SSS",
            'S', Item.getItemFromBlock(Blocks.WOODEN_SLAB),
            'F', INSTANCE.sailFabric,
            'f', Item.getItemFromBlock(INSTANCE.vsControlBlocks.frame));

        addShapedRecipe(INSTANCE.vsControlBlocks.boilerCrate,
            "IEI",
            "FCF",
            "BBB",
            'C', Item.getItemFromBlock(INSTANCE.vsControlBlocks.crate),
            'E', Items.EMPTY_BUCKET,
            'B', Item.getItemFromBlock(Blocks.BRICKS),
            'F', Item.getItemFromBlock(Blocks.FURNACE),
            'I', Items.IRON_INGOT);

        addShapedRecipe(INSTANCE.vsControlBlocks.controlCrate,
            "IDI",
            "DRD",
            "SFS",
            'F', Item.getItemFromBlock(INSTANCE.vsControlBlocks.frame),
            'R', Items.REDSTONE_DUST,
            'S', Items.STICK,
            'I', Items.IRON_INGOT);

        addShapedRecipe(INSTANCE.vsControlBlocks.pistonCrate,
            "PPP",
            "ICI",
            "IRI",
            'C', Item.getItemFromBlock(INSTANCE.vsControlBlocks.crate),
            'P', Item.getItemFromBlock(Blocks.PISTON),
            'I', Items.IRON_INGOT,
            'R', Items.REDSTONE_DUST);

        addShapedRecipe(INSTANCE.vsControlBlocks.sailCrate,
            "FPF",
            "FCF",
            " P ",
            'C', Item.getItemFromBlock(INSTANCE.vsControlBlocks.crate),
            'P', Item.getItemFromBlock(Blocks.PLANKS),
            'F', INSTANCE.sailFabric);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		addItems();
		addBlocks();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
		registerRecipes();
		registerMultiblocks();
        registerTileEntities();
        registerNetworks();
        registerCapabilities();
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    private void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityCaptainsChair.class,
            new ResourceLocation(MOD_ID, "tile_captains_chair"));
        GameRegistry.registerTileEntity(TileEntityPassengerChair.class,
            new ResourceLocation(MOD_ID, "tile_passenger_chair"));

        GameRegistry.registerTileEntity(TileEntityNetworkRelay.class,
            new ResourceLocation(MOD_ID, "tile_network_relay"));
        GameRegistry.registerTileEntity(TileEntityNetworkDisplay.class,
            new ResourceLocation(MOD_ID, "tile_network_display"));

        GameRegistry.registerTileEntity(TileEntityShipHelm.class,
            new ResourceLocation(MOD_ID, "tile_ship_helm"));
        GameRegistry.registerTileEntity(TileEntitySpeedTelegraph.class,
            new ResourceLocation(MOD_ID, "tile_speed_telegraph"));
        GameRegistry.registerTileEntity(TileEntityGyroscopeStabilizer.class,
            new ResourceLocation(MOD_ID, "tile_gyroscope_stabilizer"));
        GameRegistry.registerTileEntity(TileEntityGyroscopeDampener.class,
            new ResourceLocation(MOD_ID, "tile_gyroscope_dampener"));
        GameRegistry.registerTileEntity(TileEntityLiftValve.class,
            new ResourceLocation(MOD_ID, "tile_lift_valve"));
        GameRegistry.registerTileEntity(TileEntityLiftLever.class,
            new ResourceLocation(MOD_ID, "tile_lift_lever"));

        GameRegistry.registerTileEntity(TileEntityLegacyEngine.class,
            new ResourceLocation(MOD_ID, "tile_legacy_engine"));
        GameRegistry.registerTileEntity(TileEntityValkyriumEngine.class,
            new ResourceLocation(MOD_ID, "tile_valkyrium_engine"));
        GameRegistry.registerTileEntity(TileEntityValkyriumCompressor.class,
            new ResourceLocation(MOD_ID, "tile_valkyrium_compressor"));
        GameRegistry.registerTileEntity(TileEntityRudder.class,
            new ResourceLocation(MOD_ID, "tile_rudder"));
        GameRegistry.registerTileEntity(TileEntityGiantPropeller.class,
            new ResourceLocation(MOD_ID, "tile_giant_propeller"));
        GameRegistry.registerTileEntity(TileEntityGearbox.class,
            new ResourceLocation(MOD_ID, "tile_gearbox"));
        GameRegistry.registerTileEntity(TileEntityRotationAxle.class,
            new ResourceLocation(MOD_ID, "tile_rotation_axle"));

    }

    private void registerNetworks() {
        controlNetwork = NetworkRegistry.INSTANCE.newSimpleChannel("control_network");
        controlNetwork
            .registerMessage(PilotControlsMessageHandler.class, PilotControlsMessage.class, 2,
                Side.SERVER);
        controlNetwork
            .registerMessage(MessageStartPilotingHandler.class, MessageStartPiloting.class, 3,
                Side.CLIENT);
        controlNetwork
            .registerMessage(MessageStopPilotingHandler.class, MessageStopPiloting.class, 4,
                Side.CLIENT);
        controlNetwork.registerMessage(MessagePlayerStoppedPilotingHandler.class,
            MessagePlayerStoppedPiloting.class, 5, Side.SERVER);
    }

    private void registerCapabilities() {
        CapabilityManager.INSTANCE.register(ICapabilityLastRelay.class, new StorageLastRelay(),
            ImplCapabilityLastRelay::new);
    }

    public void addShapedRecipe(ItemStack output, Object... params) {
		ResourceLocation location = getNameForRecipe(output);
		CraftingHelper.ShapedPrimer primer = CraftingHelper.parseShaped(params);
		ShapedRecipes recipe = new ShapedRecipes(
			output.getItem().getRegistryName().toString(),
			primer.width, primer.height, primer.input, output);
		recipe.setRegistryName(location);
		GameData.register_impl(recipe);
	}

	public void addShapedRecipe(Item output, int outputCount, Object... params) {
		addShapedRecipe(new ItemStack(output, outputCount), params);
	}
	public void addShapedRecipe(Block output, int outputCount, Object... params) {
		addShapedRecipe(new ItemStack(output, outputCount), params);
	}

	// Engine recipe helpers
	public void addEngineRecipe(Block output, Item type) {
		addShapedRecipe(output, 4,
			"I##",
			"IPP",
			"I##",
			'#', type,
			'P', Item.getItemFromBlock(Blocks.PISTON),
			'I', Items.IRON_INGOT);
	}

	public void addEngineRecipe(Block output, Block type) {
		addEngineRecipe(output, Item.getItemFromBlock(type));
	}

	// If a recipe already exists, increment number
	/* eg:
	  vs_control:item_0
	  vs_control:item_1
	*/
	private static ResourceLocation getNameForRecipe(ItemStack output) {
		ResourceLocation baseLoc = new ResourceLocation(MOD_ID, output.getItem().getRegistryName().getPath());
		ResourceLocation recipeLoc = baseLoc;
		int index = 0;
		while (CraftingManager.REGISTRY.containsKey(recipeLoc)) {
			index++;
			recipeLoc = new ResourceLocation(MOD_ID, baseLoc.getPath() + "_" + index);
		}
		return recipeLoc;
	}
}
