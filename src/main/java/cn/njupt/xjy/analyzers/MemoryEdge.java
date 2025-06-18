package cn.njupt.xjy.analyzers;

import java.util.HashSet;

public class MemoryEdge {
    private Boolean changed;
    private String identity;
    private HashSet<String> source;
    private HashSet<String> sink;
    private HashSet<Transformer> transformers;

    public MemoryEdge(String identity) {
        this.changed = false;
        this.identity = identity;
        this.source = new HashSet<String>();
        this.sink = new HashSet<String>();
        this.transformers = new HashSet<Transformer>();
    }

    public Integer size() {
        return this.transformers.size();
    }

    public Boolean isChanged() {
        return this.changed;
    }

    public void resetChanged() {
        this.changed = false;
    }

    public String getIdentity() {
        return this.identity;
    }

    public HashSet<String> getNeighbour() {
        HashSet<String> neighbour = new HashSet<String>();
        neighbour.addAll(this.source);
        neighbour.addAll(this.sink);
        return neighbour;
    }

    public HashSet<Transformer> getTransformers() {
        return this.transformers;
    }

    public void addSource(String identity) {
        this.source.add(identity);
    }

    public void addSink(String identity) {
        this.sink.add(identity);
    }

    public void storeRule(MemoryNode source, MemoryNode sink) {
        this.sink.add(sink.getIdentity());
        Mutex mutex = new Mutex("null", null);
        Transformer transformer = new Transformer(Opcode.STORE, source, sink, mutex, null);
        this.changed = this.transformers.add(transformer) || this.changed;
    }

    public void loadRule(MemoryNode source, MemoryNode sink) {
        this.sink.add(sink.getIdentity());
        Mutex mutex = new Mutex("null", null);
        Transformer transformer = new Transformer(Opcode.LOAD, source, sink, mutex, null);
        this.changed = this.transformers.add(transformer) || this.changed;
    }

    public void callRule(MemoryNode source, MemoryNode sink, Mutex mutex, Memory function) {
        this.sink.add(sink.getIdentity());
        Transformer transformer = new Transformer(Opcode.CALL, source, sink, mutex, function);
        this.changed = this.transformers.add(transformer) || this.changed;
    }

    public void returnRule(MemoryNode source, MemoryNode sink, Mutex mutex) {
        this.sink.add(sink.getIdentity());
        Transformer transformer = new Transformer(Opcode.RETURN, source, sink, mutex, null);
        this.changed = this.transformers.add(transformer) || this.changed;
    }

    public void elementRule(MemoryNode source, MemoryNode sink) {
        this.sink.add(sink.getIdentity());
        Mutex mutex = new Mutex("null", null);
        Transformer transformer = new Transformer(Opcode.ELEMENT, source, sink, mutex, null);
        this.changed = this.transformers.add(transformer) || this.changed;
    }

    public void selectRule(MemoryNode source, MemoryNode sink) {
        this.sink.add(sink.getIdentity());
        Mutex mutex = new Mutex("null", null);
        Transformer transformer = new Transformer(Opcode.SELECT, source, sink, mutex, null);
        this.changed = this.transformers.add(transformer) || this.changed;
    }

    public void phiRule(MemoryNode source, MemoryNode sink) {
        this.sink.add(sink.getIdentity());
        Mutex mutex = new Mutex("null", null);
        Transformer transformer = new Transformer(Opcode.PHI, source, sink, mutex, null);
        this.changed = this.transformers.add(transformer) || this.changed;
    }
}