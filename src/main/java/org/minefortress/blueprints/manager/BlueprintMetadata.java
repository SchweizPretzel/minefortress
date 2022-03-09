package org.minefortress.blueprints.manager;

import net.minecraft.util.BlockRotation;

public class BlueprintMetadata {

    private final String name;
    private final String file;
    private final boolean premium;
    private int floorLevel;

    private BlockRotation rotation = BlockRotation.NONE;

    public BlueprintMetadata(String name, String file) {
        this.name = name;
        this.file = file;
        this.floorLevel = 0;
        this.premium = false;
    }

    public BlueprintMetadata(String name, String file, boolean premium) {
        this.name = name;
        this.file = file;
        this.floorLevel = 0;
        this.premium = premium;
    }

    public BlueprintMetadata(String name, String file, int floorLevel) {
        this.name = name;
        this.file = file;
        this.floorLevel = floorLevel;
        this.premium = false;
    }

    public BlueprintMetadata(String name, String file, int floorLevel, boolean premium) {
        this.name = name;
        this.file = file;
        this.floorLevel = floorLevel;
        this.premium = premium;
    }

    public String getName() {
        return name;
    }

    public String getFile() {
        return file;
    }

    public String getId() {
        return file +"-"+rotation.name();
    }

    public int getFloorLevel() {
        return floorLevel;
    }

    public void setFloorLevel(int floorLevel) {
        this.floorLevel = floorLevel;
    }

    public void rotateRight() {
        rotation = rotation.rotate(BlockRotation.CLOCKWISE_90);
    }

    public void rotateLeft() {
        rotation = rotation.rotate(BlockRotation.COUNTERCLOCKWISE_90);
    }

    public BlockRotation getRotation() {
        return rotation;
    }

    public boolean isPremium() {
        return premium;
    }
}