package cn.njupt.xjy.analyzers;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class MemoryGraph extends Base {
    private HashSet<Memory> heaps;
    private HashSet<Memory> stacks;
    private HashSet<Memory> globals;
    private HashSet<String> pointers;
    private HashMap<String, Summary> summary;
    private HashMap<String, MemoryNode> nodes;
    private HashMap<String, MemoryEdge> edges;
    private HashMap<String, Manager> sanitizers;

    private void buildCallEdge(Summary source, Summary sink, Memory function) {
        for (Integer index : source.getIndices()) {
            String dstKey = sink.getParameters(index);
            if (dstKey == null) {
                continue;
            }
            MemoryNode dstNode = this.getNode(dstKey);
            MemoryEdge dstEdge = this.getEdge(dstKey);
            for (Context context : source.getArguments(index)) {
                String srcKey = context.getIdentity();
                String callsite = context.getCallSite();
                Mutex mutex = new Mutex(callsite, source.getMutex());
                MemoryNode srcNode = this.getNode(srcKey);
                MemoryEdge srcEdge = this.getEdge(srcKey);
                srcEdge.callRule(srcNode, dstNode, mutex, function);
                dstEdge.addSource(srcKey);
            }
        }
    }

    private void buildReturnEdge(Summary source, Summary sink) {
        for (String srcKey : source.getReturns()) {
            MemoryNode srcNode = this.getNode(srcKey);
            MemoryEdge srcEdge = this.getEdge(srcKey);
            for (Context context : sink.getReceivers()) {
                String dstKey = context.getIdentity();
                String callsite = context.getCallSite();
                Mutex mutex = new Mutex(callsite, sink.getMutex());
                MemoryNode dstNode = this.getNode(dstKey);
                MemoryEdge dstEdge = this.getEdge(dstKey);
                srcEdge.returnRule(srcNode, dstNode, mutex);
                dstEdge.addSource(srcKey);
            }
        }
    }

    private void directEdgeBuild(String task) {
        Summary source = this.summary.get(task);
        this.buildCallEdge(source, source, null);
        this.buildReturnEdge(source, source);
    }

    private void typeEdgeBuild(String task) {
        Summary source = this.summary.get(task);
        for (String identity : LLVMIR.typeGraph.getCallSite(task)) {
            Summary sink = this.summary.get(identity);
            if (sink == null) {
                continue;
            }
            this.buildCallEdge(source, sink, null);
            this.buildReturnEdge(sink, source);
        }
    }

    private void pointerEdgeBuild(String task) {
        Summary source = this.summary.get(task);
        MemoryNode node = this.getNode(task);
        for (Memory memory : node.getSymbols()) {
            String identity = memory.getIdentity();
            Summary sink = this.summary.get(identity);
            if (sink == null) {
                continue;
            }
            this.buildCallEdge(source, sink, memory);
            this.buildReturnEdge(sink, source);
        }
    }

    private void combinedEdgeBuild(String task) {
        Summary source = this.summary.get(task);
        MemoryNode node = this.getNode(task);
        HashSet<String> callsite = LLVMIR.typeGraph.getCallSite(task);
        for (Memory memory : node.getSymbols()) {
            String identity = memory.getIdentity();
            if (!callsite.contains(identity)) {
                continue;
            }
            Summary sink = this.summary.get(identity);
            if (sink == null) {
                continue;
            }
            this.buildCallEdge(source, sink, memory);
            this.buildReturnEdge(sink, source);
        }
    }

    private void buildEdge(HashSet<String> worklist) {
        for (String task : worklist) {
            if (isIdentity(task)) {
                this.directEdgeBuild(task);
            } else if (Clash.type_based == true) {
                this.typeEdgeBuild(task);
            } else if (Clash.pointer_based == true) {
                this.pointerEdgeBuild(task);
            } else if (Clash.combined == true) {
                this.combinedEdgeBuild(task);
            } else {
                continue;
            }
        }
    }

    private void buildNode(HashSet<String> worklist) {
        for (String task : worklist) {
            MemoryEdge edge = this.getEdge(task);
            for (Transformer transformer : edge.getTransformers()) {
                transformer.transfer();
            }
        }
    }

    private HashSet<String> getChanged(Set<String> subgraph) {
        HashSet<String> worklist = new HashSet<String>();
        for (String key : subgraph) {
            MemoryNode node = this.getNode(key);
            MemoryEdge edge = this.getEdge(key);
            if (!node.isChanged() && !edge.isChanged()) {
                continue;
            }
            worklist.add(key);
        }
        return worklist;
    }

    private void resetChanged() {
        for (MemoryNode node : this.nodes.values()) {
            node.resetChanged();
        }
        for (MemoryEdge edge : this.edges.values()) {
            edge.resetChanged();
        }
    }

    public MemoryGraph() {
        this.heaps = new HashSet<Memory>();
        this.stacks = new HashSet<Memory>();
        this.globals = new HashSet<Memory>();
        this.pointers = new HashSet<String>();
        this.summary = new HashMap<String, Summary>();
        this.nodes = new HashMap<String, MemoryNode>();
        this.edges = new HashMap<String, MemoryEdge>();
        this.sanitizers = new HashMap<String, Manager>();
    }

    public void initialize() {
        this.heaps.clear();
        this.stacks.clear();
        this.globals.clear();
        this.pointers.clear();
        this.summary.clear();
        this.nodes.clear();
        this.edges.clear();
        this.sanitizers.clear();
    }

    public MemoryNode getNode(String identity) {
        MemoryNode node = this.nodes.get(identity);
        if (node == null) {
            node = new MemoryNode(identity);
            this.nodes.put(identity, node);
        }
        return node;
    }

    public MemoryEdge getEdge(String identity) {
        MemoryEdge edge = this.edges.get(identity);
        if (edge == null) {
            edge = new MemoryEdge(identity);
            this.edges.put(identity, edge);
        }
        return edge;
    }

    public Summary getSummary(String identity) {
        Summary summary = this.summary.get(identity);
        if (summary == null) {
            summary = new Summary(identity);
            this.summary.put(identity, summary);
        }
        return summary;
    }

    public Integer getNodeCount() {
        return this.nodes.size();
    }

    public Integer getEdgeCount() {
        Integer counter = 0;
        for (MemoryEdge edge : this.edges.values()) {
            counter += edge.getTransformers().size();
        }
        return counter;
    }

    public Integer getHeapCount() {
        return this.heaps.size();
    }

    public Integer getStackCount() {
        return this.stacks.size();
    }

    public Integer getGlobalCount() {
        return this.globals.size();
    }

    public void globalRule() {
        String identity = LLVMIR.database.getIdentity();
        Memory memory = new Memory(identity, Manager.Auto);
        MemoryNode node = this.getNode(identity);
        node.addSymbols(memory);
        this.globals.add(memory);
    }

    public void parameterRule() {
        String caller = LLVMIR.database.getCaller();
        Integer index = LLVMIR.database.getParameter();
        String identity = getIdentity(caller, LLVMIR.database.getIdentity());
        Summary summary = this.getSummary(caller);
        summary.addParameters(index, identity);
    }

    public void allocaRule() {
        String caller = LLVMIR.database.getCaller();
        String identity = getIdentity(caller, LLVMIR.database.getIdentity());
        Memory memory = new Memory(identity, Manager.Auto);
        MemoryNode node = this.getNode(identity);
        node.addSymbols(memory);
        this.stacks.add(memory);
    }

    public void storeRule() {
        String caller = LLVMIR.database.getCaller();
        String source = getIdentity(caller, LLVMIR.database.getValue(0));
        String sink = getIdentity(caller, LLVMIR.database.getValue(1));
        MemoryNode srcNode = this.getNode(source);
        MemoryNode dstNode = this.getNode(sink);
        MemoryEdge srcEdge = this.getEdge(source);
        MemoryEdge dstEdge = this.getEdge(sink);
        srcEdge.storeRule(srcNode, dstNode);
        dstEdge.addSource(source);
    }

    public void loadRule() {
        String caller = LLVMIR.database.getCaller();
        String source = getIdentity(caller, LLVMIR.database.getValue(0));
        String sink = getIdentity(caller, LLVMIR.database.getIdentity());
        if (isPointerType(LLVMIR.database.getType())) {
            this.pointers.add(sink);
        }
        MemoryNode srcNode = this.getNode(source);
        MemoryNode dstNode = this.getNode(sink);
        MemoryEdge srcEdge = this.getEdge(source);
        MemoryEdge dstEdge = this.getEdge(sink);
        srcEdge.loadRule(srcNode, dstNode);
        dstEdge.addSource(source);
    }

    public void receiverRule() {
        String caller = LLVMIR.database.getCaller();
        String callsite = LLVMIR.database.getCallSite();
        String callee = getIdentity(caller, LLVMIR.database.getCallee());
        String identity = getIdentity(caller, LLVMIR.database.getIdentity());
        Summary summary = this.getSummary(callee);
        summary.addReceivers(new Context(identity, callsite));
    }

    public void argumentRule() {
        String caller = LLVMIR.database.getCaller();
        Integer index = LLVMIR.database.getArgument();
        String callsite = LLVMIR.database.getCallSite();
        String callee = getIdentity(caller, LLVMIR.database.getCallee());
        String identity = getIdentity(caller, LLVMIR.database.getIdentity());
        Summary summary = this.getSummary(callee);
        summary.addArguments(index, new Context(identity, callsite));
    }

    public void returnRule() {
        String caller = LLVMIR.database.getCaller();
        String identity = getIdentity(caller, LLVMIR.database.getIdentity());
        Summary summary = this.getSummary(caller);
        summary.addReturns(identity);
    }

    public void constructRule() {
        String caller = LLVMIR.database.getCaller();
        Manager manager = LLVMIR.database.getManager();
        String identity = getIdentity(caller, LLVMIR.database.getIdentity());
        Memory memory = new Memory(identity, manager);
        MemoryNode node = this.getNode(identity);
        node.addSymbols(memory);
        this.heaps.add(memory);
    }

    public void destructRule() {
        String caller = LLVMIR.database.getCaller();
        Manager manager = LLVMIR.database.getManager();
        String identity = getIdentity(caller, LLVMIR.database.getIdentity());
        this.sanitizers.put(identity, manager);
    }

    public void elementRule() {
        String caller = LLVMIR.database.getCaller();
        String source = getIdentity(caller, LLVMIR.database.getValue(0));
        String sink = getIdentity(caller, LLVMIR.database.getIdentity());
        MemoryNode srcNode = this.getNode(source);
        MemoryNode dstNode = this.getNode(sink);
        MemoryEdge srcEdge = this.getEdge(source);
        MemoryEdge dstEdge = this.getEdge(sink);
        srcEdge.elementRule(srcNode, dstNode);
        dstEdge.addSource(source);
    }

    public void selectRule() {
        String caller = LLVMIR.database.getCaller();
        String sink = getIdentity(caller, LLVMIR.database.getIdentity());
        MemoryNode dstNode = this.getNode(sink);
        MemoryEdge dstEdge = this.getEdge(sink);
        for (String identity : LLVMIR.database.getValue()) {
            String source = getIdentity(caller, identity);
            MemoryNode srcNode = this.getNode(source);
            MemoryEdge srcEdge = this.getEdge(source);
            srcEdge.selectRule(srcNode, dstNode);
            dstEdge.addSource(source);
        }
    }

    public void phiRule() {
        String caller = LLVMIR.database.getCaller();
        String sink = getIdentity(caller, LLVMIR.database.getIdentity());
        MemoryNode dstNode = this.getNode(sink);
        MemoryEdge dstEdge = this.getEdge(sink);
        for (String identity : LLVMIR.database.getValue()) {
            String source = getIdentity(caller, identity);
            MemoryNode srcNode = this.getNode(source);
            MemoryEdge srcEdge = this.getEdge(source);
            srcEdge.phiRule(srcNode, dstNode);
            dstEdge.addSource(source);
        }
    }

    public void buildDebug() {
        for (Memory memory : this.globals) {
            LLVMIR.metadata.buildDebug(memory);
        }
        for (Memory memory : this.stacks) {
            LLVMIR.metadata.buildDebug(memory);
        }
        for (Memory memory : this.heaps) {
            LLVMIR.metadata.buildDebug(memory);
        }
        for (MemoryNode node : this.nodes.values()) {
            LLVMIR.metadata.buildDebug(node);
        }
    }

    public void buildGraph() {
        HashSet<String> worklist = null;
        do {
            if (Clash.inter_procedural == true) {
                worklist = this.getChanged(this.summary.keySet());
                this.buildEdge(worklist);
            }
            worklist = this.getChanged(this.nodes.keySet());
            this.resetChanged();
            this.buildNode(worklist);
        } while (worklist != null && !worklist.isEmpty());
        for (String identity : this.sanitizers.keySet()) {
            MemoryNode node = this.getNode(identity);
            for (Memory memory : node.getSymbols()) {
                memory.pushSanitizer();
            }
        }
    }

    public void checkMemoryDefect(Defect benchType) {
        switch (benchType) {
            case Defect.None:
                break;
            case Defect.MemoryLeak:
                this.checkMemoryLeak();
                break;
            case Defect.DoubleFree:
                this.checkDoubleFree();
                break;
            case Defect.StackFree:
                this.checkStackFree();
                break;
            case Defect.Mismatch:
                this.checkMismatch();
                break;
        }
    }

    public void checkMemoryLeak() {
        LLVMIR.evaluation.resetDetection();
        for (Memory memory : this.heaps) {
            if (!memory.isMemoryLeak()) {
                continue;
            }
            memory.show("Memory Leak Detected");
            LLVMIR.evaluation.setDetection(memory.getSubprogram());
        }
    }

    public void checkDoubleFree() {
        LLVMIR.evaluation.resetDetection();
        for (Memory memory : this.heaps) {
            if (!memory.isDoubleFree()) {
                continue;
            }
            memory.show("Double Free Detected");
            LLVMIR.evaluation.setDetection(memory.getSubprogram());
        }
    }

    public void checkStackFree() {
        LLVMIR.evaluation.resetDetection();
        for (Memory memory : this.stacks) {
            if (!memory.isStackFree()) {
                continue;
            }
            memory.show("Free Memory Not on Heap Detected");
            LLVMIR.evaluation.setDetection(memory.getSubprogram());
        }
    }

    public void checkMismatch() {
        LLVMIR.evaluation.resetDetection();
        for (String identity : this.sanitizers.keySet()) {
            MemoryNode node = this.getNode(identity);
            Manager manager = this.sanitizers.get(identity);
            for (Memory memory : node.getSymbols()) {
                if (memory.getManager() == manager) {
                    continue;
                }
                memory.show("Mismatched Memory Management Detected");
                LLVMIR.evaluation.setDetection(memory.getSubprogram());
            }
        }
    }

    public void show() {
        for (String key : this.nodes.keySet()) {
            MemoryNode node = this.nodes.get(key);
            for (Memory symbol : node.getSymbols()) {
                System.out.printf("Node: %s, Symbol: %s\n", key, symbol.getIdentity());
                for (Memory value : symbol.getValues()) {
                    System.out.printf("Node: %s, Value: %s\n", key, value.getIdentity());
                }
            }
        }
        for (String key : this.edges.keySet()) {
            MemoryEdge edge = this.edges.get(key);
            for (Transformer transformer : edge.getTransformers()) {
                System.out.printf("Edge: %s -> %s, Opcode: %s, CallSite: %s\n",
                        transformer.getSource(), transformer.getSink(), transformer.getOpcode(),
                        transformer.getMutex());
            }
        }
    }
}