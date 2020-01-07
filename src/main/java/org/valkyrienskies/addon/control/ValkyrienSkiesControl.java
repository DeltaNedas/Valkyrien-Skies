package org.valkyrienskies.addon.control;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
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
import org.valkyrienskies.addon.control.item.ItemSandyBrick;
import org.valkyrienskies.addon.control.item.ItemSandyClay;
import org.valkyrienskies.addon.control.item.ItemVSWrench;
import org.valkyrienskies.addon.control.network.MessagePlayerStoppedPiloting;
import org.valkyrienskies.addon.control.network.MessagePlayerStoppedPilotingHandler;
import org.valkyrienskies.addon.control.network.MessageStartPiloting;
import org.valkyrienskies.addon.control.network.MessageStartPilotingHandler;
import org.valkyrienskies.addon.control.network.MessageStopPiloting;
import org.valkyrienskies.addon.control.network.MessageStopPilotingHandler;
import org.valkyrienskies.addon.control.nodenetwork.EnumWireType;
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
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final List<Item> ITEMS = new ArrayList<>();

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
	public Item ironGear;
	public Item sailFabric;
	public Item sandyClay;
	public Item sandyBrick;

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
		INSTANCE.sandyClay = new ItemSandyClay();
		INSTANCE.sandyBrick = new ItemSandyBrick();
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

		addShapelessRecipe(INSTANCE.sandyClay, 3,
			new ItemStack(Blocks.SAND, 1),
			new ItemStack(Items.CLAY_BALL, 3));
		addSmelting(INSTANCE.sandyBrick, 1, INSTANCE.sandyClay, 1, 0.2F);
		addShapedRecipe(INSTANCE.vsControlBlocks.sandyBricks, 1,
            "BB",
            "BB",
            'B', INSTANCE.sandyBrick);

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

		addShapedRecipe(INSTANCE.vsControlBlocks.frame, 1,
            "sSs",
            "s s",
            "sSs",
            'S', Item.getItemFromBlock(Blocks.WOODEN_SLAB),
            's', Items.STICK);
		addShapedRecipe(INSTANCE.vsControlBlocks.crate, 1,
            "SSS",
            "FfF",
            "SSS",
            'S', Item.getItemFromBlock(Blocks.WOODEN_SLAB),
            'F', INSTANCE.sailFabric,
            'f', Item.getItemFromBlock(INSTANCE.vsControlBlocks.frame));
		addShapedRecipe(INSTANCE.vsControlBlocks.boilerCrate, 1,
            "IEI",
            "FHF",
            "BBB",
            'H', Item.getItemFromBlock(INSTANCE.vsControlBlocks.heavyDutyCrate),
            'E', Items.BUCKET,
            'B', Item.getItemFromBlock(Blocks.BRICK_BLOCK),
            'F', Item.getItemFromBlock(Blocks.FURNACE),
            'I', Items.IRON_INGOT);
		addShapedRecipe(INSTANCE.vsControlBlocks.controlCrate, 1,
            "IDI",
            "DRD",
            "SFS",
            'F', Item.getItemFromBlock(INSTANCE.vsControlBlocks.frame),
            'R', Item.getItemFromBlock(INSTANCE.vsControlBlocks.networkRelay),
            'D', Items.REDSTONE,
            'S', Items.STICK,
            'I', Items.IRON_INGOT);
		addShapedRecipe(INSTANCE.vsControlBlocks.gearsCrate, 1,
            "SGS",
            "GCG",
            "SGS",
            'C', Item.getItemFromBlock(INSTANCE.vsControlBlocks.crate),
            'S', Items.STICK,
            'G', INSTANCE.ironGear);
		addShapedRecipe(INSTANCE.vsControlBlocks.heavyDutyCrate, 1,
            "SSS",
            "ICI",
            "sss",
            'C', Item.getItemFromBlock(INSTANCE.vsControlBlocks.crate),
            'I', Items.IRON_INGOT,
            'S', Item.getItemFromBlock(INSTANCE.vsControlBlocks.sandyBricks),
            's', Item.getItemFromBlock(Blocks.WOODEN_SLAB));
        addShapedRecipe(INSTANCE.vsControlBlocks.pistonCrate, 1,
            "PPP",
            "IHI",
            "IRI",
            'H', Item.getItemFromBlock(INSTANCE.vsControlBlocks.heavyDutyCrate),
            'P', Item.getItemFromBlock(Blocks.PISTON),
            'I', Items.IRON_INGOT,
            'R', Items.REDSTONE);
        addShapedRecipe(INSTANCE.vsControlBlocks.sailCrate, 1,
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

    // Recipe functions

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

	public void addShapelessRecipe(ItemStack output, Object... input) {
		ResourceLocation location = getNameForRecipe(output);
		ShapelessRecipes recipe = new ShapelessRecipes(location.getNamespace(), output, buildInput(input));
		recipe.setRegistryName(location);
		GameData.register_impl(recipe);
	}
	public void addShapelessRecipe(Item output, int outputCount, Object... input) {
		addShapelessRecipe(new ItemStack(output, outputCount), input);
	}

	public void addSmelting(ItemStack input, ItemStack output, float xp) {
		FurnaceRecipes.instance().addSmeltingRecipe(input, output, xp);
	}
	public void addSmelting(Item input, int inputCount, Item output, int outputCount, float xp) {
		addSmelting(new ItemStack(input, inputCount), new ItemStack(output, outputCount), xp);
	}

	public void addSmelting(Block input, int inputCount, Item output, int outputCount, float xp) {
		addSmelting(new ItemStack(input, inputCount), new ItemStack(output, outputCount), xp);
	}

	// Utilities used by ^

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

	private static NonNullList<Ingredient> buildInput(Object[] input) {
		NonNullList<Ingredient> list = NonNullList.create();
		for (Object obj : input) {
			if (obj instanceof Ingredient) {
				list.add((Ingredient) obj);
			} else {
				Ingredient ingredient = CraftingHelper.getIngredient(obj);
				if (ingredient == null) {
					ingredient = Ingredient.EMPTY;
				}
				list.add(ingredient);
			}
		}
		return list;
	}
}
