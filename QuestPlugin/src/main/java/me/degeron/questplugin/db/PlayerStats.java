package me.degeron.questplugin.db;

public class PlayerStats {
    private String UUID;
    private int completed;
    private int canceled;
    private String selectedQuest;
    private int progressInQuest;

    public PlayerStats(String UUID, int completed, int canceled, String selectedQuest, int progressInQuest) {
        this.UUID = UUID;
        this.completed = completed;
        this.canceled = canceled;
        this.selectedQuest = selectedQuest;
        this.progressInQuest = progressInQuest;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getCanceled() {
        return canceled;
    }

    public void setCanceled(int canceled) {
        this.canceled = canceled;
    }

    public String getSelectedQuest() {
        return selectedQuest;
    }

    public void setSelectedQuest(String selectedQuest) {
        this.selectedQuest = selectedQuest;
    }

    public int getProgressInQuest() {
        return progressInQuest;
    }

    public void setProgressInQuest(int progressInQuest) {
        this.progressInQuest = progressInQuest;
    }
}
