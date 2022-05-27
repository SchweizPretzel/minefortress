package org.minefortress.fortress;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public abstract class AbstractFortressManager {

    public BlockState getStateForCampCenter() {
        return Blocks.CAMPFIRE.getDefaultState();
    }
    public abstract boolean hasRequiredBuilding(String requirementId, int minCount);
    public abstract boolean hasRequiredBlock(Block block, boolean blueprint, int minCount);

    public abstract int getTotalColonistsCount();

    abstract void setGamemode(FortressGamemode gamemode);
    public abstract boolean isCreative();

}
