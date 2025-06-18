package cn.njupt.xjy.analyzers;

import java.util.HashSet;
import java.util.Objects;

public class Transformer extends Clash {
    private Opcode opcode;
    private MemoryNode source;
    private MemoryNode sink;
    private Mutex mutex;
    private Memory function;

    private void storeRule() {
        for (Memory source : this.source.getSymbols()) {
            for (Memory sink : this.sink.getSymbols()) {
                if (Clash.CSC_enabled && this.hasClash(source, sink)) {
                    continue;
                } else {
                    sink.addValues(source);
                }
            }
        }
    }

    private void loadRule() {
        HashSet<Memory> sink = this.sink.getSymbols();
        for (Memory source : this.source.getSymbols()) {
            for (Memory memory : source.getValues()) {
                if (sink.contains(memory)) {
                    continue;
                } else {
                    this.setClash(source, memory);
                    this.sink.addSymbols(memory);
                }
            }
        }
    }

    private void callRule() {
        HashSet<Memory> sink = this.sink.getSymbols();
        for (Memory source : this.source.getSymbols()) {
            if (sink.contains(source)) {
                continue;
            } else if (Clash.CBC_enabled && this.hasClash(source, this.function)) {
                continue;
            } else {
                this.setClash(source, this.mutex);
                this.sink.addSymbols(source);
            }
        }
    }

    private void returnRule() {
        HashSet<Memory> sink = this.sink.getSymbols();
        for (Memory source : this.source.getSymbols()) {
            if (sink.contains(source)) {
                continue;
            } else if (Clash.CRC_enabled && this.hasClash(source, this.mutex)) {
                continue;
            } else {
                this.sink.addSymbols(source);
            }
        }
    }

    private void elementRule() {
        for (Memory memory : this.source.getSymbols()) {
            memory.setStructured();
        }
        this.sink.addAllSymbols(this.source.getSymbols());
    }

    private void selectRule() {
        this.sink.addAllSymbols(this.source.getSymbols());
    }

    private void phiRule() {
        this.sink.addAllSymbols(this.source.getSymbols());
    }

    public Transformer(Opcode opcode, MemoryNode source, MemoryNode sink, Mutex mutex, Memory function) {
        this.opcode = opcode;
        this.source = source;
        this.sink = sink;
        this.mutex = mutex;
        this.function = function;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.opcode, this.source, this.sink, this.mutex, this.function);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Transformer other = (Transformer) obj;
        boolean result = Objects.equals(this.opcode, other.opcode)
                && Objects.equals(this.source, other.source)
                && Objects.equals(this.sink, other.sink)
                && Objects.equals(this.mutex, other.mutex)
                && Objects.equals(this.function, other.function);
        return result;
    }

    public String getOpcode() {
        return this.opcode.toString();
    }

    public String getSource() {
        return this.source.getIdentity();
    }

    public String getSink() {
        return this.sink.getIdentity();
    }

    public String getMutex() {
        return this.mutex.getIdentity();
    }

    public void transfer() {
        switch (this.opcode) {
            case Opcode.STORE:
                this.storeRule();
                break;
            case Opcode.LOAD:
                this.loadRule();
                break;
            case Opcode.CALL:
                this.callRule();
                break;
            case Opcode.RETURN:
                this.returnRule();
                break;
            case Opcode.ELEMENT:
                this.elementRule();
                break;
            case Opcode.SELECT:
                this.selectRule();
                break;
            case Opcode.PHI:
                this.phiRule();
                break;
        }
        LLVMIR.evaluation.pushTransfer();
    }
}