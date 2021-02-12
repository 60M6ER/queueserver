package com.baikalsr.queueserver.jsonView.tablo;

public class TabloSettingsPOJO {
    private boolean active;
    private long id;
    private int landscape;
    private int countLines;
    private int versionTabloPage;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLandscape() {
        return landscape;
    }

    public void setLandscape(int landscape) {
        this.landscape = landscape;
    }

    public int getCountLines() {
        return countLines;
    }

    public void setCountLines(int countLines) {
        this.countLines = countLines;
    }

    public int getVersionTabloPage() {
        return versionTabloPage;
    }

    public void setVersionTabloPage(int versionTabloPage) {
        this.versionTabloPage = versionTabloPage;
    }
}
