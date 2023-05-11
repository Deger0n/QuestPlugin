package me.degeron.questplugin.menu;

import org.bukkit.Material;

public class QuestItem {
    private String name;
    private Material material;
    private String difficulty;
    private String reward;
    private String description;
    private int minutes;
    private int progress;
    private int full;
    private String link;

    public QuestItem(String name, Material material, String difficulty, String reward, String description, int minutes, int progress, int full, String link) {
        this.name = name;
        this.material = material;
        this.difficulty = difficulty;
        this.reward = reward;
        this.description = description;
        this.minutes = minutes;
        this.progress = progress;
        this.full = full;
        this.link = link;
    }

    public int getFull() {
        return full;
    }

    public void setFull(int full) {
        this.full = full;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
