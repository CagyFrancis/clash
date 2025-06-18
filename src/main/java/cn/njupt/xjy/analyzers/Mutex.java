package cn.njupt.xjy.analyzers;

import java.util.HashSet;
import java.util.Objects;

public class Mutex {
    private String identity;
    private HashSet<Mutex> mutex;

    public Mutex(String identity, HashSet<Mutex> mutex) {
        this.identity = identity;
        this.mutex = mutex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Mutex other = (Mutex) obj;
        return Objects.equals(this.identity, other.identity);
    }

    public String getIdentity() {
        return this.identity;
    }

    public HashSet<Mutex> getMutex() {
        return this.mutex;
    }
}