package cn.njupt.xjy.analyzers;

import java.io.File;
import java.util.Objects;

public class Debug {
    private String identity;
    private String line;
    private String filename;
    private String subprogram;

    public Debug(String identity) {
        this.identity = identity;
        this.line = "";
        this.filename = "";
        this.subprogram = "";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Debug other = (Debug) obj;
        return Objects.equals(this.identity, other.identity);
    }

    public String getIdentity() {
        return this.identity;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getLine() {
        return this.line;
    }

    public void setFilename(String filename) {
        File file = new File(filename);
        this.filename = file.getName();
    }

    public String getFilename() {
        return this.filename;
    }

    public void setSubprogram(String subprogram) {
        this.subprogram = subprogram;
    }

    public String getSubprogram() {
        return this.subprogram;
    }

    public void show(String prefix) {
        if (this.line.isEmpty()) {
            return;
        }
        System.out.printf("!--%s--! (File: \"%s\", Subprogram: \"%s\", Line: \"%s\")\n",
                prefix, this.filename, this.subprogram, this.line);
    }
}