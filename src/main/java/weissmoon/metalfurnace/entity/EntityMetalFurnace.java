package weissmoon.metalfurnace.entity;

import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.world.World;

/**
 * Created by Weissmoon on 10/26/19.
 */
public class EntityMetalFurnace extends EntityMinecartFurnace {
    public EntityMetalFurnace(World worldIn) {
        super(worldIn);
    }

    public EntityMetalFurnace(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
}
