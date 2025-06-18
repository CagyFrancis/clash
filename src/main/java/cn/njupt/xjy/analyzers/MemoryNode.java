package cn.njupt.xjy.analyzers;

import java.util.HashSet;

public class MemoryNode extends Debug {
    private Boolean changed;
    private HashSet<Memory> symbols;

    public MemoryNode(String identity) {
        super(identity);
        this.changed = false;
        this.symbols = new HashSet<Memory>();
    }

    public Integer size() {
        return this.symbols.size();
    }

    public Boolean isEmpty() {
        return this.symbols.isEmpty();
    }

    public Boolean isChanged() {
        Boolean result = this.changed;
        for (Memory memory : this.symbols) {
            result = result || memory.isChanged();
        }
        return result;
    }

    public void resetChanged() {
        this.changed = false;
        for (Memory memory : this.symbols) {
            memory.resetChanged();
        }
    }

    public HashSet<Memory> getSymbols() {
        return this.symbols;
    }

    public void addSymbols(Memory memory) {
        this.changed = this.symbols.add(memory) || this.changed;
    }

    public void addAllSymbols(HashSet<Memory> memory) {
        this.changed = this.symbols.addAll(memory) || this.changed;
    }
}