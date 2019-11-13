package weissmoon.metalfurnace.block;

import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import weissmoon.core.helper.RNGHelper;
import weissmoon.metalfurnace.lib.FurnaceType;

import java.util.Map;

/**
 * Created by Weissmoon on 10/4/19.
 */
public class TileMetalFurnace extends TileEntityFurnace{

    protected FurnaceType furnaceType;

    @GameRegistry.ItemStackHolder(value = "minecraft:sponge", meta = 1)
    public static final ItemStack WET_SPONGE = ItemStack.EMPTY;

    protected ItemStack recipeKey = ItemStack.EMPTY;
    protected ItemStack recipeOutput = ItemStack.EMPTY;
    protected ItemStack failedMatch = ItemStack.EMPTY;

    public TileMetalFurnace(FurnaceType variant) {
        super();
        this.furnaceType = variant;
        //this.inventory = this.initInventory();
    }

    public TileMetalFurnace() {
        this(FurnaceType.IRON);
    }

    public void update()
    {
        final boolean wasBurning = this.isBurning();

        if(wasBurning)
            --this.furnaceBurnTime;

        if (this.world.isRemote)
            return;

        final boolean canSmelt = canSmelt();

        ItemStack fuelStack = this.furnaceItemStacks.get(1);

        if (!this.isBurning() && !fuelStack.isEmpty()) {
            if (canSmelt)
                burnFuel(fuelStack, false);
        }

        //boolean wasBurning = isBurning();

        if (this.isBurning()) {
            //furnaceBurnTime--;
            if (canSmelt)
                smelt();
            else
                cookTime = 0;
        }

        if (!this.isBurning()) {
            if (!(fuelStack = furnaceItemStacks.get(1)).isEmpty()) {
                if (canSmelt())
                    burnFuel(fuelStack, wasBurning);
            } else if (cookTime > 0) {
                cookTime = MathHelper.clamp(cookTime - 2, 0, totalCookTime);
            }
        }

        if (wasBurning && !isBurning()) {
            this.markDirty();
            BlockMetalFurnace.setState(false, world, pos);
        }
    }

    private boolean canSmelt()
    {
        ItemStack imputStack = furnaceItemStacks.get(0);
        ItemStack outputStack = furnaceItemStacks.get(2);

        if (imputStack.isEmpty() || imputStack == failedMatch){
            return false;
        }

        if (recipeKey.isEmpty() || !OreDictionary.itemMatches(recipeKey, imputStack, false)) {
            boolean matched = false;
            for (Map.Entry<ItemStack, ItemStack> e : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
                if (OreDictionary.itemMatches(e.getKey(), imputStack, false)) {
                    recipeKey = e.getKey();
                    recipeOutput = e.getValue();
                    matched = true;
                    failedMatch = ItemStack.EMPTY;
                    break;
                }
            }
            if (!matched) {
                recipeKey = ItemStack.EMPTY;
                recipeOutput = ItemStack.EMPTY;
                failedMatch = imputStack;
                return false;
            }
        }

        return !recipeOutput.isEmpty() &&
                (outputStack.isEmpty() ||
                        (itemsMatch(recipeOutput, outputStack) && (recipeOutput.getCount() + outputStack.getCount() <= outputStack.getMaxStackSize())));
    }

    public int getCookTime(ItemStack stack)
    {
        return this.furnaceType.getCookTime();
//        return 200;
    }


    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.furnaceItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);
        this.furnaceBurnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks.get(1));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", (short)this.furnaceBurnTime);
        compound.setInteger("CookTime", (short)this.cookTime);
        compound.setInteger("CookTimeTotal", (short)this.totalCookTime);
        ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);

        return compound;
    }

    public void smeltItem()
    {
        ItemStack inputStack = this.furnaceItemStacks.get(0);
        ItemStack recipeOutput = FurnaceRecipes.instance().getSmeltingList().get(recipeKey);
        ItemStack outputStack = this.furnaceItemStacks.get(2);

        if (outputStack.isEmpty()){
            this.furnaceItemStacks.set(2, recipeOutput.copy());
        }else if (itemsMatch(recipeOutput, outputStack)){
            outputStack.grow(recipeOutput.getCount());
        }
        if (this.furnaceType == FurnaceType.OBSIDIAN){
            if(RNGHelper.getRNGFloat()<.25){
                outputStack.grow(recipeOutput.getCount());
            }
        }

        if (inputStack.isItemEqual(WET_SPONGE) && this.furnaceItemStacks.get(1).getItem() == Items.BUCKET){
            this.furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
        }

        inputStack.shrink(1);
    }

    boolean itemsMatch(ItemStack a, ItemStack b) {
        return ItemHandlerHelper.canItemStacksStack(a, b);
    }

    protected void smelt() {
        cookTime++;
        if (this.cookTime == this.totalCookTime) {
            this.cookTime = 0;
            this.totalCookTime = this.getCookTime(this.furnaceItemStacks.get(0));
            this.smeltItem();
        }
    }

    protected void burnFuel(ItemStack fuel, boolean burnedThisTick) {
        currentItemBurnTime = (furnaceBurnTime = getItemBurnTime(fuel));
        if (this.isBurning()) {
            Item item = fuel.getItem();
            fuel.shrink(1);
            if (fuel.isEmpty()) furnaceItemStacks.set(1, item.getContainerItem(fuel));
            if (!burnedThisTick) BlockMetalFurnace.setState(true, world, pos);
        }
    }
}
