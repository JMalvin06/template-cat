package oreo.fabricmod.blocks;

import net.minecraft.block.*;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;


public class CatBed extends HorizontalFacingBlock {

    private static CatEntity assignedCat;

    public CatBed(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    private static final VoxelShape SHAPE = Block.createCuboidShape(0,0, 0, 16, 6, 16);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        return SHAPE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

    public void setUser(CatEntity user){
        assignedCat = user;
    }

    public void clearUser(){
        assignedCat = null;
    }

    public CatEntity getUser(){
        return assignedCat;
    }
}
