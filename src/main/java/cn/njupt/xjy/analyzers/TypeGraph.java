package cn.njupt.xjy.analyzers;

import java.util.HashMap;
import java.util.HashSet;

public class TypeGraph extends Base {
    private Integer type_number;
    private Integer escape_number;
    private Integer failure_number;
    private Integer pointer_number;
    private Integer combined_number;
    private HashSet<String> type_target;
    private HashSet<String> escape_target;
    private HashSet<String> pointer_target;
    private HashSet<String> combined_target;
    private HashMap<String, HashSet<String>> callsite;
    private HashMap<String, HashSet<String>> signature;

    private HashSet<String> getFunction(String signature) {
        HashSet<String> function = this.signature.get(signature);
        if (function == null) {
            function = new HashSet<String>();
            this.signature.put(signature, function);
        }
        return function;
    }

    public TypeGraph() {
        this.type_number = 0;
        this.escape_number = 0;
        this.pointer_number = 0;
        this.failure_number = 0;
        this.combined_number = 0;
        this.type_target = new HashSet<String>();
        this.escape_target = new HashSet<String>();
        this.pointer_target = new HashSet<String>();
        this.combined_target = new HashSet<String>();
        this.callsite = new HashMap<String, HashSet<String>>();
        this.signature = new HashMap<String, HashSet<String>>();
    }

    public void initialize() {
        this.type_number = 0;
        this.escape_number = 0;
        this.pointer_number = 0;
        this.failure_number = 0;
        this.combined_number = 0;
        this.type_target.clear();
        this.escape_target.clear();
        this.pointer_target.clear();
        this.combined_target.clear();
        this.callsite.clear();
        this.signature.clear();
    }

    public HashSet<String> getCallSite(String caller) {
        HashSet<String> callee = this.callsite.get(caller);
        if (callee == null) {
            callee = new HashSet<String>();
            this.callsite.put(caller, callee);
        }
        return callee;
    }

    public Integer getTypeCount() {
        return this.type_target.size();
    }

    public Integer getEscapeCount() {
        return this.escape_target.size();
    }

    public Integer getPointerCount() {
        return this.pointer_target.size();
    }

    public Integer getCombinedCount() {
        return this.combined_target.size();
    }

    public Integer getTypeNumber() {
        return this.type_number;
    }

    public Integer getEscapeNumber() {
        return this.escape_number;
    }

    public Integer getPointerNumber() {
        return this.pointer_number;
    }

    public Integer getFailureNumber() {
        return this.failure_number;
    }

    public Integer getCombinedNumber() {
        return this.combined_number;
    }

    public void callSiteRule() {
        String caller = LLVMIR.database.getCaller();
        String callee = LLVMIR.database.getCallee();
        String signature = LLVMIR.database.getSignature();
        if (isIdentity(callee)) {
            return;
        }
        HashSet<String> function = this.getFunction(signature);
        String identity = getIdentity(caller, callee);
        this.callsite.put(identity, function);
    }

    public void signatureRule() {
        String caller = LLVMIR.database.getCaller();
        String signature = LLVMIR.database.getSignature();
        HashSet<String> function = this.getFunction(signature);
        function.add(caller);
    }

    public void buildGraph() {
        for (String caller : this.callsite.keySet()) {
            HashSet<String> callee = this.callsite.get(caller);
            MemoryNode node = LLVMIR.memoryGraph.getNode(caller);
            for (Memory memory : node.getSymbols()) {
                String identity = memory.getIdentity();
                if (!isIdentity(identity)) {
                    this.failure_number += 1;
                    continue;
                }
                this.pointer_number += 1;
                this.pointer_target.add(identity);
                if (callee.contains(identity)) {
                    this.combined_number += 1;
                    this.combined_target.add(identity);
                } else {
                    this.escape_number += 1;
                    this.escape_target.add(identity);
                }
            }
            this.type_number += callee.size();
            this.type_target.addAll(callee);
        }
    }

    public void show() {
        for (String identity : this.callsite.keySet()) {
            HashSet<String> function = this.callsite.get(identity);
            System.out.printf("Indirect Call: %s, Targets: %s\n", identity, function.toString());
        }
    }
}