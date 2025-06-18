package cn.njupt.xjy.analyzers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CallGraph extends Base {
    private Integer static_call;
    private Integer direct_call;
    private Integer indirect_call;
    private Integer parameter_call;
    private Integer structure_call;
    private HashSet<String> library;
    private HashSet<String> function;
    private HashMap<String, HashSet<String>> nodes;
    private HashMap<String, HashSet<String>> edges;

    private void dfs(String node, ArrayList<String> visited) {
        visited.add(node);
        HashSet<String> children = this.nodes.get(node);
        if (children == null) {
            return;
        }
        for (String child : children) {
            if (visited.contains(child)) {
                continue;
            }
            dfs(child, visited);
        }
    }

    private HashSet<String> getNode(String identity) {
        HashSet<String> node = this.nodes.get(identity);
        if (node == null) {
            node = new HashSet<String>();
            this.nodes.put(identity, node);
        }
        return node;
    }

    private HashSet<String> getEdge(String identity) {
        HashSet<String> edge = this.edges.get(identity);
        if (edge == null) {
            edge = new HashSet<String>();
            this.edges.put(identity, edge);
        }
        return edge;
    }

    public CallGraph() {
        this.static_call = 0;
        this.direct_call = 0;
        this.indirect_call = 0;
        this.parameter_call = 0;
        this.structure_call = 0;
        this.library = new HashSet<String>();
        this.function = new HashSet<String>();
        this.nodes = new HashMap<String, HashSet<String>>();
        this.edges = new HashMap<String, HashSet<String>>();
    }

    public void initialize() {
        this.static_call = 0;
        this.direct_call = 0;
        this.indirect_call = 0;
        this.parameter_call = 0;
        this.structure_call = 0;
        this.nodes.clear();
        this.edges.clear();
        this.library.clear();
        this.function.clear();
    }

    public Integer getFunctionCount() {
        return this.function.size();
    }

    public Integer getLibraryCount() {
        return this.library.size();
    }

    public Integer getSCallCount() {
        return this.static_call;
    }

    public Integer getICallCount() {
        return this.indirect_call;
    }

    public Integer getDCallCount() {
        return this.direct_call;
    }

    public Integer getPCallCount() {
        return this.parameter_call;
    }

    public Integer getTCallCount() {
        return this.structure_call;
    }

    public void functionRule() {
        String caller = LLVMIR.database.getCaller();
        this.function.add(caller);
    }

    public void libraryRule() {
        String caller = LLVMIR.database.getCaller();
        this.library.add(caller);
    }

    public void callRule() {
        String caller = LLVMIR.database.getCaller();
        String callee = LLVMIR.database.getCallee();
        HashSet<String> node = this.getNode(caller);
        String identity = getIdentity(caller, callee);
        if (isIdentity(callee)) {
            node.add(callee);
            this.direct_call += 1;
        } else {
            this.edges.put(identity, node);
            this.indirect_call += 1;
        }
    }

    public void buildGraph() {
        for (String callsite : this.edges.keySet()) {
            HashSet<String> edge = this.getEdge(callsite);
            MemoryNode node = LLVMIR.memoryGraph.getNode(callsite);
            for (Memory memory : node.getSymbols()) {
                String identity = memory.getIdentity();
                if (!isIdentity(identity)) {
                    continue;
                }
                if (memory.hasMutex()) {
                    this.parameter_call += 1;
                }
                if (memory.isStructured()) {
                    this.structure_call += 1;
                }
                edge.add(identity);
            }
            if (node.size() == 1) {
                this.static_call += 1;
            }
        }
    }

    public ArrayList<String> topology() {
        ArrayList<String> visited = new ArrayList<String>();
        for (String node : LLVMIR.constant.getEntry()) {
            if (visited.contains(node)) {
                continue;
            }
            dfs(node, visited);
        }
        return visited;
    }

    public void show() {
        for (String caller : this.nodes.keySet()) {
            for (String callee : this.nodes.get(caller)) {
                System.out.printf("Caller: %s, Callee: %s\n", caller, callee);
            }
        }
    }
}