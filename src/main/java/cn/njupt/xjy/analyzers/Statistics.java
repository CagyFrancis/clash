package cn.njupt.xjy.analyzers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

public class Statistics extends Base {
    private Integer samples;
    private Integer nodes;
    private Integer edges;
    private Integer heaps;
    private Integer stacks;
    private Integer globals;
    private Integer metadata;
    private Integer functions;
    private Integer libraries;
    private Integer transfers;
    private Integer basic_blocks;
    private Integer instructions;
    private Integer static_calls;
    private Integer direct_calls;
    private Integer indirect_calls;
    private Integer parameter_calls;
    private Integer structure_calls;
    private Long parse_time;
    private Long graph_time;
    private Long pointer_time;
    private Long query_time;
    private Long type_time;

    public Statistics() {
        this.samples = 0;
        this.nodes = 0;
        this.edges = 0;
        this.heaps = 0;
        this.stacks = 0;
        this.globals = 0;
        this.metadata = 0;
        this.functions = 0;
        this.libraries = 0;
        this.transfers = 0;
        this.basic_blocks = 0;
        this.instructions = 0;
        this.static_calls = 0;
        this.direct_calls = 0;
        this.indirect_calls = 0;
        this.parameter_calls = 0;
        this.structure_calls = 0;
        this.parse_time = 0L;
        this.graph_time = 0L;
        this.pointer_time = 0L;
        this.query_time = 0L;
        this.type_time = 0L;
    }

    public void initialize() {
        this.samples = 0;
        this.nodes = 0;
        this.edges = 0;
        this.heaps = 0;
        this.stacks = 0;
        this.globals = 0;
        this.metadata = 0;
        this.functions = 0;
        this.libraries = 0;
        this.transfers = 0;
        this.basic_blocks = 0;
        this.instructions = 0;
        this.static_calls = 0;
        this.direct_calls = 0;
        this.indirect_calls = 0;
        this.parameter_calls = 0;
        this.structure_calls = 0;
        this.parse_time = 0L;
        this.graph_time = 0L;
        this.pointer_time = 0L;
        this.query_time = 0L;
        this.type_time = 0L;
    }

    public void pushBasicBlock() {
        this.basic_blocks += 1;
    }

    public void pushInstruction() {
        this.instructions += 1;
    }

    public void pushTransfer() {
        this.transfers += 1;
    }

    public void pushMetadata() {
        this.metadata += 1;
    }

    public void setParseTime(Long time) {
        this.parse_time += time;
    }

    public void setGraphTime(Long time) {
        this.graph_time += time;
    }

    public void setTypeTime(Long time) {
        this.type_time += time;
    }

    public void setPointerTime(Long time) {
        this.pointer_time += time;
    }

    public void setQueryTime(Long time) {
        this.query_time += time;
    }

    public Integer getTargetCount() {
        return this.functions + this.libraries;
    }

    public void buildBenchmark() {
        this.samples += 1;
        this.nodes += LLVMIR.memoryGraph.getNodeCount();
        this.edges += LLVMIR.memoryGraph.getEdgeCount();
        this.heaps += LLVMIR.memoryGraph.getHeapCount();
        this.stacks += LLVMIR.memoryGraph.getStackCount();
        this.globals += LLVMIR.memoryGraph.getGlobalCount();
        this.functions += LLVMIR.callGraph.getFunctionCount();
        this.libraries += LLVMIR.callGraph.getLibraryCount();
        this.static_calls += LLVMIR.callGraph.getSCallCount();
        this.direct_calls += LLVMIR.callGraph.getDCallCount();
        this.indirect_calls += LLVMIR.callGraph.getICallCount();
        this.parameter_calls += LLVMIR.callGraph.getPCallCount();
        this.structure_calls += LLVMIR.callGraph.getTCallCount();
    }

    public void show(Integer size) {
        showTitle("Basic Information", size);
        Integer memory = this.heaps + this.stacks + this.globals;
        System.out.printf("Samples: %d\n", this.samples);
        System.out.printf("Memory: %d\n", memory);
        System.out.printf("Memory Node: %d\n", this.nodes);
        System.out.printf("Memory Edge: %d\n", this.edges);
        System.out.printf("Metadata: %d\n", this.metadata);
        System.out.printf("Functions: %d\n", this.functions);
        System.out.printf("Libraries: %d\n", this.libraries);
        System.out.printf("Basic Blocks: %d\n", this.basic_blocks);
        System.out.printf("Instructions: %d\n", this.instructions);
        System.out.printf("Static Calls: %d\n", this.static_calls);
        System.out.printf("Direct Calls: %d\n", this.direct_calls);
        System.out.printf("Indirect Calls: %d\n", this.indirect_calls);
        System.out.printf("Parameter Calls: %d\n", this.parameter_calls);
        System.out.printf("Structure Calls: %d\n", this.structure_calls);
        System.out.printf("Transfer Counts: %d\n", this.transfers);
        System.out.printf("Parse LLVM-IR Time: %s\n", getTimeString(this.parse_time));
        System.out.printf("Graph Generation Time: %s\n", getTimeString(this.graph_time));
        System.out.printf("Type-based Anlysis Time: %s\n", getTimeString(this.type_time));
        System.out.printf("Pointer-based Analysis Time: %s\n", getTimeString(this.pointer_time));
        System.out.printf("Defect Query Time: %s\n", getTimeString(this.query_time));
    }

    public void save(JsonGenerator builder) throws IOException {
        builder.writeNumberField("samples", this.samples);
        builder.writeNumberField("nodes", this.nodes);
        builder.writeNumberField("edges", this.edges);
        builder.writeNumberField("heaps", this.heaps);
        builder.writeNumberField("stacks", this.stacks);
        builder.writeNumberField("globals", this.globals);
        builder.writeNumberField("metadata", this.metadata);
        builder.writeNumberField("functions", this.functions);
        builder.writeNumberField("libraries", this.libraries);
        builder.writeNumberField("transfers", this.transfers);
        builder.writeNumberField("basic_blocks", this.basic_blocks);
        builder.writeNumberField("instructions", this.instructions);
        builder.writeNumberField("static_calls", this.static_calls);
        builder.writeNumberField("direct_calls", this.direct_calls);
        builder.writeNumberField("indirect_calls", this.indirect_calls);
        builder.writeNumberField("parameter_calls", this.parameter_calls);
        builder.writeNumberField("structure_calls", this.structure_calls);
        builder.writeNumberField("parse_time", this.parse_time);
        builder.writeNumberField("graph_time", this.graph_time);
        builder.writeNumberField("type_time", this.type_time);
        builder.writeNumberField("pointer_time", this.pointer_time);
        builder.writeNumberField("query_time", this.query_time);
    }
}