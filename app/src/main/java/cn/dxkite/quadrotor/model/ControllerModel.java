package cn.dxkite.quadrotor.model;

public class ControllerModel {
    protected int id;
    protected String name;
    protected int max;
    protected int progress;

    public ControllerModel(int id, String name, int max, int progress) {
        this.id = id;
        this.name = name;
        this.max = max;
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
