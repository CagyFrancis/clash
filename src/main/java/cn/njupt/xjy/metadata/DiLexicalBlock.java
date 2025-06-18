package cn.njupt.xjy.metadata;

public class DiLexicalBlock extends DiNode {
    private String scope;
    private String file;
    private String line;

    public DiLexicalBlock(String identity, String scope, String file, String line) {
        super(identity, DiType.DiLexicalBlock);
        this.scope = scope;
        this.file = file;
        this.line = line;
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