package com.blueteam.observable;


import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArrayList;

public class CurvedLevel extends Observable {

    private static final int DEFAULT_LEVEL = 1;

    private static CurvedLevel curvedLevel = new CurvedLevel(DEFAULT_LEVEL);

    private int level;
    private boolean changed;
    CopyOnWriteArrayList<Observer> obs;

    private CurvedLevel(int level) {
        this.level = level;
        changed = false;
        obs = new CopyOnWriteArrayList<>();
    }

    public synchronized void addObserver(Observer o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.add(o);
        }
    }

    public synchronized void deleteObserver(Observer o) {
        obs.remove(o);
    }

    public void notifyObservers() {
        notifyObservers(null);
    }

    public void notifyObservers(Object arg) {
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        Object[] arrLocal;

        synchronized (this) {
            if (!hasChanged())
                return;
            arrLocal = obs.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length - 1; i >= 0; i--)
            ((Observer) arrLocal[i]).update(this, arg);
    }

    public synchronized int getLevel() {
        return level;
    }

    public synchronized void setLevel(int level) {
        this.level = level;
    }

    protected synchronized void setChanged() {
        changed = true;
    }

    protected synchronized void clearChanged() {
        changed = false;
    }

    public synchronized boolean hasChanged() {
        return changed;
    }

    public static CurvedLevel getInstance() {
        return curvedLevel;
    }

}
