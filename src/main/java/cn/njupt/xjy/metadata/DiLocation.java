package cn.njupt.xjy.metadata;

public class DiLocation extends DiNode {
    private String line;
    private String scope;

    public DiLocation(String identity, String line, String scope) {
        super(identity, DiType.DiLocation);
        this.line = line;
        this.scope = scope;
    }

    public String getLine() {
        return this.line;
    }

    public String getScope() {
        return this.scope;
    }
}