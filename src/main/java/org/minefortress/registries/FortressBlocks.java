package org.minefortress.registries;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FortressBlocks {

    public static final Block SCAFFOLD_OAK_PLANKS = new Block(
            FabricBlockSettings
                    .of(Material.WOOD)
                    .strength(2.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
    );

    public static void register() {
        Registry.register(Registry.BLOCK, new Identifier("minefortress", "scaffold_oak_planks"), FortressBlocks.SCAFFOLD_OAK_PLANKS);
        FlammableBlockRegistry.getInstance(Blocks.FIRE).add(FortressBlocks.SCAFFOLD_OAK_PLANKS, 5, 20);
    }
}
