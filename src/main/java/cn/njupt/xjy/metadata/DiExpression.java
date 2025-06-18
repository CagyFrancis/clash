package cn.njupt.xjy.metadata;

public class DiExpression extends DiNode {
    private String variable;

    public DiExpression(String identity, String variable) {
        super(identity, DiType.DiExpression);
        this.variable = variable;
    }

    public String getVariable() {
        return this.variable;
    }
}