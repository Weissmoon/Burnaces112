package weissmoon.metalfurnace.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import weissmoon.metalfurnace.ModData;

/**
 * Created by Weissmoon on 11/15/19.
 */
@JEIPlugin
public class JEICompat implements IModPlugin {
    public void register(IModRegistry registry) {
        for(Block furnace:ModData.metalFurnace.clone()) {
            ItemStack furnaceStack = new ItemStack(furnace);
            registry.addRecipeCatalyst(furnaceStack, VanillaRecipeCategoryUid.SMELTING);
        }
    }
}
