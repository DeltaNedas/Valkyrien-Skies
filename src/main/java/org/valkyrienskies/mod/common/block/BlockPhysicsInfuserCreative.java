package org.valkyrienskies.mod.common.block;

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.valkyrienskies.mod.common.physmanagement.relocation.DetectorManager;

@ParametersAreNonnullByDefault
public class BlockPhysicsInfuserCreative extends BlockPhysicsInfuser {

<<<<<<< HEAD
    public BlockPhysicsInfuserCreative() {
        super("physics_infuser_creative");
        shipSpawnDetectorID = DetectorManager.DetectorIDs.BlockPosFinder.ordinal();
=======
    public BlockPhysicsInfuserCreative(Material materialIn) {
        super(materialIn);
>>>>>>> 3c2c237c7b502b80d217eef470b66a7924cc0afc
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip,
        ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.valkyrienskies.physics_infuser_creative_1"));
        tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("tooltip.valkyrienskies.physics_infuser_creative_2"));
    }

    @Override
    public DetectorManager.DetectorIDs getShipSpawnDetectorID() {
        return DetectorManager.DetectorIDs.BlockPosFinder;
    }

}