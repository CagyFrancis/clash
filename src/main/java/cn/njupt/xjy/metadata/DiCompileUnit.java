package cn.njupt.xjy.metadata;

public class DiCompileUnit extends DiNode {
    private String file;

    public DiCompileUnit(String identity, String file) {
        super(identity, DiType.DiCompileUnit);
        this.file = file;
    }

    public String getFile() {
        return this.file;
    }
}