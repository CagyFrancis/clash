package cn.njupt.xjy.metadata;

public class DiSubprogram extends DiNode {
    private String name;
    private String scope;
    private String file;
    private String line;

    public DiSubprogram(String identity, String name, String scope, String file, String line) {
        super(identity, DiType.DiSubprogram);
        this.name = name;
        this.scope = scope;
        this.file = file;
        this.line = line;
    }

    public String getName() {
        return this.name;
    }

    public String getScope() {
        return this.scope;
    }

    public String getFile() {
        return this.file;
    }

    public String getLine() {
        return this.line;
    }
}