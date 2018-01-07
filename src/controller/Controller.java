package controller;

public abstract class Controller {
    public abstract void keyUp();
    public abstract void keyDown();
    public abstract void keyLeft();
    public abstract void keyRight();
    public abstract void keySpace();
    public abstract void keyPause();
    public abstract void keyResume();
    public abstract boolean isRunning();
}
