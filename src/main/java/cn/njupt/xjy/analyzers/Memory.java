package cn.njupt.xjy.analyzers;

import java.util.HashSet;

public class Memory extends Debug {
    private Boolean changed;
    private Manager manager;
    private Integer sanitizer;
    private Boolean structured;
    private HashSet<Mutex> mutex;
    private HashSet<Memory> values;

    public Memory(String identity, Manager manager) {
        super(identity);
        this.changed = false;
        this.manager = manager;
        this.sanitizer = 0;
        this.structured = false;
        this.mutex = new HashSet<Mutex>();
        this.values = new HashSet<Memory>();
    }

    public Boolean isStructured() {
        return this.structured;
    }

    public void setStructured() {
        this.structured = true;
    }

    public Integer size() {
        return this.values.size();
    }

    public Manager getManager() {
        return this.manager;
    }

    public void pushSanitizer() {
        this.sanitizer++;
    }

    public Boolean hasMutex() {
        return this.mutex.size() != 0;
    }

    public Boolean isMemoryLeak() {
        if (this.sanitizer == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isDoubleFree() {
        if (this.sanitizer >= 2) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isStackFree() {
        if (this.sanitizer == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean isChanged() {
        return this.changed;
    }

    public void resetChanged() {
        this.changed = false;
    }

    public HashSet<Memory> getValues() {
        return this.values;
    }

    public void addValues(Memory value) {
        this.changed = this.values.add(value) || this.changed;
    }

    public void addAllValues(HashSet<Memory> values) {
        this.changed = this.values.addAll(values) || this.changed;
    }

    public HashSet<Mutex> getMutex() {
        return this.mutex;
    }

    public void setMutex(Mutex clash) {
        this.mutex.add(clash);
    }

    public void setAllMutex(HashSet<Mutex> clash) {
        this.mutex.addAll(clash);
    }
}