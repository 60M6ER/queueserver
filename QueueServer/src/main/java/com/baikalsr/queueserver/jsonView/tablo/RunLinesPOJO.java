package com.baikalsr.queueserver.jsonView.tablo;


public class RunLinesPOJO {
    private boolean active;
    private String[] runLines;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String[] getRunLines() {
        return runLines;
    }

    public void setRunLines(String[] runLines) {
        this.runLines = runLines;
    }
}
