package pl.dreammc.dreammcapi.api.util;

import org.jetbrains.annotations.Nullable;

public class ValueWatcher<T>{

    private T currentValue;
    private T oldValue;
    private boolean hasChanged = false;

    public ValueWatcher(T value) {
        this.currentValue = value;
        this.oldValue = value;
    }

    public void set(T value) {
        this.currentValue = value;
        this.hasChanged = !this.currentValue.equals(this.oldValue);
    }

    public T get() {
        return currentValue;
    }

    public void reset() {
        this.oldValue = this.currentValue;
        this.hasChanged = false;
    }

    @Nullable
    public T getChanged() {
        if(this.hasChanged) {
            this.oldValue = this.currentValue;
            this.hasChanged = false;
            return this.currentValue;
        }
        return null;
    }

    public boolean hasChanged() {
        return this.hasChanged;
    }

}
