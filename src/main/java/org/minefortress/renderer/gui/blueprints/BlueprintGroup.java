package org.minefortress.renderer.gui.blueprints;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public enum BlueprintGroup {
    LIVING_HOUSES(true, Items.OAK_PLANKS, "Houses"),
    DECORATION(true, Items.ROSE_BUSH, "Decoration");

    private final boolean topRow;
    private final ItemStack icon;
    private final Text nameText;

    BlueprintGroup(boolean topRow, Item item, String name) {
        this.topRow = topRow;
        this.icon = new ItemStack(item);
        this.nameText = new LiteralText(name);
    }

    public boolean isTopRow() {
        return topRow;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public Text getNameText() {
        return nameText;
    }
}