package cn.njupt.xjy.analyzers;

import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonGenerator;

public class Detection extends Statistics {
    private Integer true_positive;
    private Integer true_negative;
    private Integer false_positive;
    private Integer false_negative;
    private HashSet<String> good;
    private HashSet<String> bad;
    private HashSet<String> detected;

    private Boolean isGood(String subprogram) {
        Pattern pattern = Pattern.compile("good");
        Matcher matcher = pattern.matcher(subprogram);
        return matcher.find();
    }

    private Boolean isBad(String subprogram) {
        Pattern pattern = Pattern.compile("bad");
        Matcher matcher = pattern.matcher(subprogram);
        return matcher.find();
    }

    public Detection() {
        super();
        this.true_positive = 0;
        this.true_negative = 0;
        this.false_positive = 0;
        this.false_negative = 0;
        this.good = new HashSet<String>();
        this.bad = new HashSet<String>();
        this.detected = new HashSet<String>();
    }

    public void initialize() {
        super.initialize();
        this.true_positive = 0;
        this.true_negative = 0;
        this.false_positive = 0;
        this.false_negative = 0;
        this.good.clear();
        this.bad.clear();
        this.detected.clear();
    }

    public void buildBenchmark() {
        super.buildBenchmark();
        for (String subprogram : LLVMIR.metadata.getSubprogram()) {
            if (this.isGood(subprogram)) {
                this.good.add(subprogram);
            } else if (this.isBad(subprogram)) {
                this.bad.add(subprogram);
            } else {
                continue;
            }
        }
        for (String good : this.good) {
            if (this.detected.contains(good)) {
                this.false_positive += 1;
            } else {
                this.true_negative += 1;
            }
        }
        for (String bad : this.bad) {
            if (this.detected.contains(bad)) {
                this.true_positive += 1;
            } else {
                this.false_negative += 1;
            }
        }
    }

    public void resetDetection() {
        this.good.clear();
        this.bad.clear();
        this.detected.clear();
    }

    public void setDetection(String subprogram) {
        this.detected.add(subprogram);
    }

    public void show(Integer size) {
        super.show(size);
        showTitle("Detection Report", size);
        Integer TP = this.true_positive;
        Integer TN = this.true_negative;
        Integer FP = this.false_positive;
        Integer FN = this.false_negative;
        Float accuracy = Float.valueOf(TP + TN) / (TP + TN + FP + FN);
        Float precision = Float.valueOf(TP) / (TP + FP);
        Float recall = Float.valueOf(TP) / (TP + FN);
        Float f1 = 2 * precision * recall / (precision + recall);
        System.out.printf("True Positive: %d, True Negative: %d, False Positive: %d, False Negative: %d\n",
                this.true_positive, this.true_negative, this.false_positive, this.false_negative);
        System.out.printf("Accuracy: %s, Precision: %s, Recall: %s, F1-Score: %s\n",
                getPercent(accuracy), getPercent(precision), getPercent(recall), getPercent(f1));
    }

    public void save(JsonGenerator builder) throws IOException {
        super.save(builder);
        builder.writeNumberField("true_positive", this.true_positive);
        builder.writeNumberField("true_negative", this.true_negative);
        builder.writeNumberField("false_positive", this.false_positive);
        builder.writeNumberField("false_negative", this.false_negative);
    }
}