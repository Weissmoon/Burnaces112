package weissmoon.metalfurnace.lib;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

/**
 * Created by Weissmoon on 10/4/19.
 */
public enum FurnaceType implements IStringSerializable {

    IRON("ingotiron", 160, MapColor.IRON),
    GOLD("ingotgold", 133, MapColor.GOLD),
    DIAMOND("gemdiamond", 100, MapColor.DIAMOND),
    EMERALD("gememerald", 80, MapColor.EMERALD),
    NETHER("blockbricknether", 50, MapColor.NETHERRACK, Material.ROCK, SoundType.STONE),
    OBSIDIAN("obsidian", 100, MapColor.OBSIDIAN, Material.ROCK, SoundType.STONE);

    private String ingot;
    private int cookTime;
    private MapColor mapColor;
    private Material material;
    private SoundType soundType;

    FurnaceType(String ingot, int cookTime, MapColor mapColor){
        this(ingot, cookTime, mapColor, Material.IRON, SoundType.METAL);
    }

    FurnaceType(String ingot, int cookTime, MapColor mapColor, Material material, SoundType sound){
        this.ingot = ingot;
        this.cookTime = cookTime;
        this.mapColor = mapColor;
        this.material = material;
        this.soundType = sound;
    }

    @Override
    public String getName() {
        return this.ingot;
    }

    public int getCookTime(){
        return this.cookTime;
    }

    public MapColor getMapColor(){
        return this.mapColor;
    }

    public Material getMaterial(){
        return this.material;
    }

    public SoundType getSoundType(){
        return this.soundType;
    }

    @Nonnull
    public static FurnaceType getFromOrdinal(int ord){
        for(FurnaceType t:FurnaceType.values()) {
            if (t.ordinal() == ord) return t;
        }
        return IRON;
    }
}
