package cn.njupt.xjy.analyzers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

public class Evaluation extends Detection {
    private Integer type_target;
    private Integer type_number;
    private Integer escape_target;
    private Integer escape_number;
    private Integer pointer_target;
    private Integer pointer_number;
    private Integer failure_number;
    private Integer combined_target;
    private Integer combined_number;

    public Evaluation() {
        super();
        this.type_target = 0;
        this.type_number = 0;
        this.escape_target = 0;
        this.escape_number = 0;
        this.pointer_target = 0;
        this.pointer_number = 0;
        this.failure_number = 0;
        this.combined_target = 0;
        this.combined_number = 0;
    }

    public void initialize() {
        super.initialize();
        this.type_target = 0;
        this.type_number = 0;
        this.escape_target = 0;
        this.escape_number = 0;
        this.pointer_target = 0;
        this.pointer_number = 0;
        this.failure_number = 0;
        this.combined_target = 0;
        this.combined_number = 0;
    }

    public void buildBenchmark() {
        super.buildBenchmark();
        this.type_target += LLVMIR.typeGraph.getTypeCount();
        this.type_number += LLVMIR.typeGraph.getTypeNumber();
        this.escape_target += LLVMIR.typeGraph.getEscapeCount();
        this.escape_number += LLVMIR.typeGraph.getEscapeNumber();
        this.pointer_target += LLVMIR.typeGraph.getPointerCount();
        this.pointer_number += LLVMIR.typeGraph.getPointerNumber();
        this.failure_number += LLVMIR.typeGraph.getFailureNumber();
        this.combined_target += LLVMIR.typeGraph.getCombinedCount();
        this.combined_number += LLVMIR.typeGraph.getCombinedNumber();
    }

    public void show(Integer size) {
        super.show(size);
        showTitle("Evaluation", size);
        Integer total_target = this.getTargetCount();
        Float type_rate = Float.valueOf(this.type_target) / total_target;
        Float escape_rate = Float.valueOf(this.escape_target) / total_target;
        Float pointer_rate = Float.valueOf(this.pointer_target) / total_target;
        Float combined_rate = Float.valueOf(this.combined_target) / total_target;
        Float edge_rate = Float.valueOf(this.type_number - this.pointer_number) / this.type_number;
        Float scope_rate = Float.valueOf(this.type_target - this.pointer_target) / this.type_target;
        System.out.printf("Type-based Analysis: %d\n", this.type_number);
        System.out.printf("Escaped Analysis: %d\n", this.escape_number);
        System.out.printf("Pointer-based Analysis: %d\n", this.pointer_number);
        System.out.printf("Failure Analysis: %d\n", this.failure_number);
        System.out.printf("Combined Analysis: %d\n", this.combined_number);
        System.out.printf("Edge Reduction Rate: %s\n", getPercent(edge_rate));
        System.out.printf("Type-based Scope: %d(%s)\n", this.type_target, getPercent(type_rate));
        System.out.printf("Escaped Scope: %d(%s)\n", this.escape_target, getPercent(escape_rate));
        System.out.printf("Pointer-based Scope: %d(%s)\n", this.pointer_target, getPercent(pointer_rate));
        System.out.printf("Combined Scope: %d(%s)\n", this.combined_target, getPercent(combined_rate));
        System.out.printf("Scope Reduction Rate: %s\n", getPercent(scope_rate));
        showTitle("", size);
    }

    public void save(JsonGenerator builder) throws IOException {
        super.save(builder);
        builder.writeNumberField("type_target", this.type_target);
        builder.writeNumberField("type_number", this.type_number);
        builder.writeNumberField("escape_target", this.escape_target);
        builder.writeNumberField("escape_number", this.escape_number);
        builder.writeNumberField("pointer_target", this.pointer_target);
        builder.writeNumberField("pointer_number", this.pointer_number);
        builder.writeNumberField("failure_number", this.failure_number);
        builder.writeNumberField("combined_target", this.combined_target);
        builder.writeNumberField("combined_number", this.combined_number);
    }
}