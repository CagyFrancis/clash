package cn.njupt.xjy.metadata;

public class DiFile extends DiNode {
    String filename;

    public DiFile(String identity, String filename) {
        super(identity, DiType.DiFile);
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }
}