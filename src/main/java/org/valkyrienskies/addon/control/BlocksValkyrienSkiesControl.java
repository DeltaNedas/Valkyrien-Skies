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
import org.valkyrienskies.addon.control.block.BlockShipHelm;
import org.valkyrienskies.addon.control.block.BlockShipWheel;
import org.valkyrienskies.addon.control.block.BlockSpeedTelegraph;
import org.valkyrienskies.addon.control.block.crates.BlockCrate;
import org.valkyrienskies.addon.control.block.crates.BlockFrame;
import org.valkyrienskies.addon.control.block.crates.BlockBoilerCrate;
import org.valkyrienskies.addon.control.block.crates.BlockControlCrate;
import org.valkyrienskies.addon.control.block.crates.BlockGearsCrate;
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
	public final Block pistonCrate;
	public final Block sailCrate;

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
		pistonCrate = new BlockPistonCrate();
		sailCrate = new BlockSailCrate();
    }
}
