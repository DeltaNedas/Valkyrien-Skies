package org.valkyrienskies.addon.control;

import org.valkyrienskies.addon.control.block.BlockCaptainsChair;
import org.valkyrienskies.addon.control.block.BlockCompactedValkyrium;
import org.valkyrienskies.addon.control.block.BlockDummyTelegraph;
import org.valkyrienskies.addon.control.block.BlockGearbox;
import org.valkyrienskies.addon.control.block.BlockGyroscopeDampener;
import org.valkyrienskies.addon.control.block.BlockGyroscopeStabilizer;
import org.valkyrienskies.addon.control.block.BlockLiftLever;
import org.valkyrienskies.addon.control.block.BlockLiftValve;
import org.valkyrienskies.addon.control.block.BlockNetworkDisplay;
import org.valkyrienskies.addon.control.block.BlockNetworkRelay;
import org.valkyrienskies.addon.control.block.BlockPassengerChair;
import org.valkyrienskies.addon.control.block.BlockRotationAxle;
import org.valkyrienskies.addon.control.block.BlockSandyBricks;
import org.valkyrienskies.addon.control.block.BlockShipHelm;
import org.valkyrienskies.addon.control.block.BlockShipWheel;
import org.valkyrienskies.addon.control.block.BlockSpeedTelegraph;
import org.valkyrienskies.addon.control.block.crates.BlockCrate;
import org.valkyrienskies.addon.control.block.crates.BlockFrame;
import org.valkyrienskies.addon.control.block.crates.BlockBoilerCrate;
import org.valkyrienskies.addon.control.block.crates.BlockControlCrate;
import org.valkyrienskies.addon.control.block.crates.BlockGearsCrate;
import org.valkyrienskies.addon.control.block.crates.BlockHeavyDutyCrate;
import org.valkyrienskies.addon.control.block.crates.BlockPistonCrate;
import org.valkyrienskies.addon.control.block.crates.BlockSailCrate;
import org.valkyrienskies.addon.control.block.engine.BlockNormalEngine;
import org.valkyrienskies.addon.control.block.engine.BlockRedstoneEngine;
import org.valkyrienskies.mod.common.config.VSConfig;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlocksValkyrienSkiesControl {

    public final BlockNormalEngine basicEngine;
    public final BlockNormalEngine advancedEngine;
    public final BlockNormalEngine eliteEngine;
    public final BlockNormalEngine ultimateEngine;
    public final BlockRedstoneEngine redstoneEngine;
    public final Block compactedValkyrium;
    public final Block captainsChair;
    public final Block passengerChair;
    public final Block shipHelm;
    public final Block shipWheel;
    public final Block speedTelegraph;
    public final Block dummyTelegraph;
	public final Block networkRelay;
	public final Block networkDisplay;
	public final Block gyroscopeStabilizer;
	public final Block gyroscopeDampener;
    public final Block liftValve;
    public final Block liftLever;
    public final Block gearbox;
    public final Block rotationAxle;

	public final Block crate;
	public final Block frame;
	public final Block boilerCrate;
	public final Block controlCrate;
	public final Block gearsCrate;
	public final Block heavyDutyCrate;
	public final Block pistonCrate;
	public final Block sailCrate;

	public final Block sandyBricks;

    public BlocksValkyrienSkiesControl() {
        basicEngine = new BlockNormalEngine("basic", Material.WOOD,
            VSConfig.ENGINE_POWER.basicEnginePower, 5.0F);
        advancedEngine = new BlockNormalEngine("advanced", Material.ROCK,
            VSConfig.ENGINE_POWER.basicEnginePower, 6.0F);
        eliteEngine = new BlockNormalEngine("elite", Material.IRON,
            VSConfig.ENGINE_POWER.basicEnginePower, 8.0F);
        ultimateEngine = new BlockNormalEngine("ultimate", Material.GROUND,
            VSConfig.ENGINE_POWER.basicEnginePower, 10.0F);
        redstoneEngine = new BlockRedstoneEngine();

        compactedValkyrium = new BlockCompactedValkyrium();
        captainsChair = new BlockCaptainsChair();
        passengerChair = new BlockPassengerChair();
        shipHelm = new BlockShipHelm();
        shipWheel = new BlockShipWheel();
        speedTelegraph = new BlockSpeedTelegraph();
        dummyTelegraph = new BlockDummyTelegraph();

        networkRelay = new BlockNetworkRelay();
        networkDisplay = new BlockNetworkDisplay();

        gyroscopeStabilizer = new BlockGyroscopeStabilizer();
        gyroscopeDampener = new BlockGyroscopeDampener();

        liftValve = new BlockLiftValve();
        liftLever = new BlockLiftLever();

        rotationAxle = new BlockRotationAxle();
        gearbox = new BlockGearbox();

		crate = new BlockCrate();
		frame = new BlockFrame();
		boilerCrate = new BlockBoilerCrate();
		controlCrate = new BlockControlCrate();
		gearsCrate = new BlockGearsCrate();
		heavyDutyCrate = new BlockHeavyDutyCrate();
		pistonCrate = new BlockPistonCrate();
		sailCrate = new BlockSailCrate();

		sandyBricks = new BlockSandyBricks();
    }
}
