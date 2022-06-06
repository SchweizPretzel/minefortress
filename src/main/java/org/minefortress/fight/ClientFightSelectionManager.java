package org.minefortress.fight;

import com.google.common.base.Predicates;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.minefortress.entity.Colonist;
import org.minefortress.entity.ai.controls.FightControl;
import org.minefortress.network.ServerboundSelectColonistsPacket;
import org.minefortress.network.helpers.FortressChannelNames;
import org.minefortress.network.helpers.FortressClientNetworkHelper;
import org.minefortress.professions.ProfessionManager;

import java.util.Collections;
import java.util.List;

public class ClientFightSelectionManager {

    private MousePos selectionStartPos;
    private Vec3d selectionStartBlock;
    private MousePos selectionCurPos;
    private Vec3d selectionCurBlock;

    private List<Colonist> selectedColonists = Collections.emptyList();

    private Vec3d cachedBlockPos;

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
        this.updateSelectionOnServer();
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
                        .getEntitiesByType(colonistType, selectionBox, it -> FightControl.isDefender((Colonist)it))
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
        this.updateSelectionOnServer();
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

    public boolean isSelected(Colonist colonist) {
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

    private void updateSelectionOnServer() {
        if(this.selectedColonists == null) return;
        final var ids = this.selectedColonists.stream().map(Colonist::getId).toList();
        final var packet = new ServerboundSelectColonistsPacket(ids);
        FortressClientNetworkHelper.send(FortressChannelNames.FORTRESS_SELECT_COLONISTS, packet);
    }
}