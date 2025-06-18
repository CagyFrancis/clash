package cn.njupt.xjy.metadata;

public class DiNode {
    private String identity;
    private DiType type;

    public DiNode(String identity, DiType type) {
        this.identity = identity;
        this.type = type;
    }

    public String getIdentity() {
        return this.identity;
    }

    public DiType getType() {
        return this.type;
    }
}