package weissmoon.metalfurnace.block;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import weissmoon.core.block.WeissBlock;
import weissmoon.metalfurnace.lib.FurnaceType;

/**
 * Created by Weissmoon on 10/4/19.
 */
public class BlockMetalFurnace extends WeissBlock{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool IS_ON = PropertyBool.create("ison");
    public final FurnaceType variant;

    public BlockMetalFurnace(FurnaceType variant) {
        super("metalfurnace" + variant.toString().toLowerCase(), variant.getMaterial(), variant.getMapColor());
        this.variant = variant;
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(IS_ON, false));
        this.setHardness(3F);
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, IS_ON);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileMetalFurnace)
            {
                playerIn.displayGUIChest((TileMetalFurnace)tileentity);
                playerIn.addStat(StatList.FURNACE_INTERACTION);
            }

            return true;
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityFurnace)
            {
                ((TileEntityFurnace)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;
        if(state.getValue(IS_ON)){
            meta = 1;
        }
        meta |= (state.getValue(FACING).getHorizontalIndex() << 1);
        return meta;
//        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int e){
        //IBlockState state = this.getDefaultState();
        boolean on;
        EnumFacing facing;
        if (((e & 1)) == 1){
            //state.withProperty(IS_ON, true);
            on = true;
        }else{
            //state.withProperty(IS_ON, false);
            on = false;
        }
        facing = EnumFacing.getHorizontal((e >> 1) & 3);
        return this.getDefaultState().withProperty(IS_ON, on).withProperty(FACING, facing);
    }

    public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        //keepInventory = true;

        if (active)
        {
            worldIn.setBlockState(pos, iblockstate.withProperty(IS_ON, true), 3);
            worldIn.setBlockState(pos, iblockstate.withProperty(IS_ON, true), 3);
        }
        else
        {
            worldIn.setBlockState(pos, iblockstate.withProperty(IS_ON, false), 3);
            worldIn.setBlockState(pos, iblockstate.withProperty(IS_ON, false), 3);
        }

        //keepInventory = false;

        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileMetalFurnace(this.variant);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return variant.getMapColor();
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) {
        return variant.getSoundType();
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        IBlockState state = world.getBlockState(pos);
        if (this.variant == FurnaceType.OBSIDIAN) {
            return 10000F;
        }
        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.getValue(IS_ON) ? 13 : 0;
    }

    public FurnaceType getVariant(){
        return this.variant;
    }
}
