package org.minefortress.fight;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.minefortress.entity.Colonist;
import org.minefortress.entity.WarriorPawn;
import org.minefortress.fortress.FortressClientManager;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ClientFightSelectionManager {

    private final Supplier<FortressClientManager> fortressClientManagerSupplier;

    private MousePos selectionStartPos;
    private Vec3d selectionStartBlock;
    private MousePos selectionCurPos;
    private Vec3d selectionCurBlock;

    private List<Colonist> selectedColonists = Collections.emptyList();

    private Vec3d cachedBlockPos;

    public ClientFightSelectionManager(Supplier<FortressClientManager> fortressClientManagerSupplier) {
        this.fortressClientManagerSupplier = fortressClientManagerSupplier;
    }

    public void startSelection(double x, double y, Vec3d startBlock) {
        this.resetSelection();
        this.selectionStartPos = new MousePos(x, y);
        this.selectionStartBlock = startBlock;
    }

    public void endSelection() {
        this.selectionStartBlock = null;
        this.selectionStartPos = null;
        this.selectionCurBlock = null;
        this.selectionCurPos = null;
    }

    public boolean hasSelected() {
        return !this.selectedColonists.isEmpty();
    }

    public void updateSelection(double x, double y, Vec3d endBlock) {
        if(!isSelectionStarted()) return;
        this.selectionCurPos = new MousePos(x, y);
        this.selectionCurBlock = endBlock;

        if(!this.selectionCurBlock.equals(this.cachedBlockPos)) {
            final EntityType<?> colonistType = EntityType.get("minefortress:colonist").orElseThrow();
            final var selectionBox = new Box(selectionStartBlock.getX(), -64, selectionStartBlock.getZ(), selectionCurBlock.getX(), 256, selectionCurBlock.getZ());
            final var world = MinecraftClient.getInstance().world;
            if(world != null) {
                selectedColonists = world
                        .getEntitiesByType(colonistType, selectionBox, it ->{
                            final var colonist = (Colonist) it;
                            final var clientFortressId = fortressClientManagerSupplier.get().getId();
                            final var colonistFortressId = colonist.getFortressId();
                            return colonistFortressId.isPresent() && colonistFortressId.equals(clientFortressId);
                        })
                        .stream()
                        .map(it -> (Colonist)it)
                        .toList();
            }
            this.cachedBlockPos = selectionCurBlock;
        }
    }

    public void resetSelection() {
        this.selectionStartPos = null;
        this.selectionStartBlock = null;
        this.selectionCurPos = null;
        this.selectionCurBlock = null;
        this.selectedColonists = Collections.emptyList();
    }

    public boolean isSelecting() {
        return this.selectionStartPos != null && this.selectionStartBlock != null && this.selectionCurPos != null && this.selectionCurBlock != null;
    }

    public boolean isSelectionStarted() {
        return this.selectionStartPos != null && this.selectionStartBlock != null;
    }

    public MousePos getSelectionStartPos() {
        return selectionStartPos;
    }

    public MousePos getSelectionCurPos() {
        return selectionCurPos;
    }

    public boolean isSelected(WarriorPawn colonist) {
        return this.selectedColonists != null && this.selectedColonists.contains(colonist);
    }

    public record MousePos(double x, double y) {
        public int getX() {
            return (int) x;
        }

        public int getY() {
            return (int) y;
        }
    }


}
