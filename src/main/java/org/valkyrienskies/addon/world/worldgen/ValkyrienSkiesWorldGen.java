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

package org.valkyrienskies.addon.world.worldgen;

import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.valkyrienskies.addon.world.ValkyrienSkiesWorld;

/**
 * Created by joeyr on 4/18/2017.
 */
public class ValkyrienSkiesWorldGen implements IWorldGenerator {

    public WorldGenMinable genValkyriumOre = null;

    public ValkyrienSkiesWorldGen() {
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
        IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (ValkyrienSkiesWorld.OREGEN_ENABLED) {
            if (this.genValkyriumOre == null) {
                this.genValkyriumOre = new WorldGenMinable(
                    ValkyrienSkiesWorld.INSTANCE.valkyriumOre.getDefaultState(), 8);
            }
            switch (world.provider.getDimension()) {
                case 0: //Overworld
                    this.runValkyriumGenerator(this.genValkyriumOre, world, random, chunkX, chunkZ, 2,
                        0, 25);
                    // runDungeonGenerator(world, random, chunkX, chunkZ, 1);
                    break;
                case -1: //Nvalkyrium
                    break;
                case 1: //End
                    break;
            }
        }
    }

    private void runValkyriumGenerator(WorldGenerator generator, World world, Random rand,
        int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight) {
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");
        }

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chancesToSpawn; i++) {
            int x = chunk_X * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunk_Z * 16 + rand.nextInt(16);
            generator.generate(world, rand, new BlockPos(x, y, z));
        }
    }

}
