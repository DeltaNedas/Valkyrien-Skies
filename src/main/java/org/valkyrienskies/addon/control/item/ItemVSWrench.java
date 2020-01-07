package org.valkyrienskies.addon.control.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.valkyrienskies.addon.control.block.multiblocks.GiantPropellerMultiblockSchematic;
import org.valkyrienskies.addon.control.block.multiblocks.IMultiblockSchematic;
import org.valkyrienskies.addon.control.block.multiblocks.ITileEntityMultiblock;
import org.valkyrienskies.addon.control.block.multiblocks.RudderMultiblockSchematic;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityGiantPropeller;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityRudder;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityValkyriumCompressor;
import org.valkyrienskies.addon.control.block.multiblocks.TileEntityValkyriumEngine;
import org.valkyrienskies.addon.control.tileentity.TileEntityGearbox;
import org.valkyrienskies.addon.control.util.BaseItem;
import org.valkyrienskies.mod.common.config.VSConfig;

public class ItemVSWrench extends BaseItem {
    private EnumWrenchMode mode = EnumWrenchMode.CONSTRUCT;

    public ItemVSWrench() {
		super("vs_wrench", true);
        this.setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player,
        List<String> itemInformation,
        ITooltipFlag advanced) {
        if (VSConfig.wrenchModeless) {
            itemInformation.add(TextFormatting.BLUE + I18n.format("tooltip.vs_control.wrench.toggle"));
        } else {
            itemInformation.add(TextFormatting.BLUE + I18n.format("tooltip.vs_control.wrench." + this.mode.toString()));
            itemInformation.add(TextFormatting.GREEN + "" + TextFormatting.ITALIC + I18n.format("tooltip.vs_control.wrench.modes"));
        }
    }

    // Construct potential multiblock if set to construct mode.
    // Otherwise, try to deconstruct a multiblock.
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos,
        EnumHand hand,
        EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }

        if (player.isSneaking() && !VSConfig.wrenchModeless) {
            this.mode = EnumWrenchMode.values()[(this.mode.ordinal() + 1) % EnumWrenchMode.values().length]; // Switch to the next mode
            player.sendMessage(new TextComponentString(
                TextFormatting.BLUE + I18n.format("tooltip.vs_control.wrench.switched." + this.mode.toString()))); // Say in chat
            return EnumActionResult.SUCCESS;
        }

        TileEntity blockTile = worldIn.getTileEntity(pos);
        boolean shouldConstruct = this.mode == EnumWrenchMode.CONSTRUCT || VSConfig.wrenchModeless;
        boolean shouldDeconstruct = this.mode == EnumWrenchMode.DECONSTRUCT || VSConfig.wrenchModeless;
        if (blockTile instanceof ITileEntityMultiblock) {
            ITileEntityMultiblock part = (ITileEntityMultiblock) blockTile;
            shouldConstruct = shouldConstruct && !part.isPartOfAssembledMultiblock();
            shouldDeconstruct = shouldDeconstruct && part.isPartOfAssembledMultiblock();
        } else if (blockTile instanceof TileEntityGearbox) {
            shouldConstruct = true;
        } else {
            return EnumActionResult.PASS;
        }
        if (shouldConstruct) {
            if (blockTile instanceof ITileEntityMultiblock) {
                if (((ITileEntityMultiblock) blockTile).attemptToAssembleMultiblock(worldIn, pos, facing)) {
                    return EnumActionResult.SUCCESS;
                }
            } else if (blockTile instanceof TileEntityGearbox) {
                ((TileEntityGearbox) blockTile).setInputFacing(
                    player.isSneaking() ? facing.getOpposite() : facing);
            }
        } else if (shouldDeconstruct) {
            ((ITileEntityMultiblock) blockTile).disassembleMultiblock();
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    public EnumWrenchMode getMode() {
        return this.mode;
    }
}
