package cn.njupt.xjy.analyzers;

import java.util.HashMap;
import java.util.HashSet;

import cn.njupt.xjy.metadata.*;

public class Metadata extends Base {
    private HashSet<String> subprogram;
    private HashMap<String, String> debug;
    private HashMap<String, DiNode> metadata;

    private String getLine(String identity) {
        DiNode metadata = this.metadata.get(identity);
        if (metadata == null) {
            return "";
        } else if (metadata.getClass() == DiCompileUnit.class) {
            return "";
        } else if (metadata.getClass() == DiExpression.class) {
            DiExpression diExpression = (DiExpression)metadata;
            return this.getLine(diExpression.getVariable());
        } else if (metadata.getClass() == DiFile.class) {
            return "";
        } else if (metadata.getClass() == DiLexicalBlock.class) {
            DiLexicalBlock diLexicalBlock = (DiLexicalBlock)metadata;
            return diLexicalBlock.getLine();
        } else if (metadata.getClass() == DiLocation.class) {
            DiLocation diLocation = (DiLocation)metadata;
            return diLocation.getLine();
        } else if (metadata.getClass() == DiSubprogram.class) {
            DiSubprogram diSubprogram = (DiSubprogram)metadata;
            return diSubprogram.getLine();
        } else if (metadata.getClass() == DiVariable.class) {
            DiVariable diVariable = (DiVariable)metadata;
            return diVariable.getLine();
        } else {
            return "";
        }
    }

    private String getSubprogram(String identity) {
        DiNode metadata = this.metadata.get(identity);
        if (metadata == null) {
            return "";
        } else if (metadata.getClass() == DiCompileUnit.class) {
            return "Global Variable";
        } else if (metadata.getClass() == DiExpression.class) {
            return "Global Variable";
        } else if (metadata.getClass() == DiFile.class) {
            return "Global Variable";
        } else if (metadata.getClass() == DiLexicalBlock.class) {
            DiLexicalBlock diLexicalBlock = (DiLexicalBlock)metadata;
            return this.getSubprogram(diLexicalBlock.getScope());
        } else if (metadata.getClass() == DiLocation.class) {
            DiLocation diLocation = (DiLocation)metadata;
            return this.getSubprogram(diLocation.getScope());
        } else if (metadata.getClass() == DiSubprogram.class) {
            DiSubprogram diSubprogram = (DiSubprogram)metadata;
            return diSubprogram.getName();
        } else if (metadata.getClass() == DiVariable.class) {
            DiVariable diVariable = (DiVariable)metadata;
            return this.getSubprogram(diVariable.getScope());
        } else {
            return "";
        }
    }

    private String getFilename(String identity) {
        DiNode metadata = this.metadata.get(identity);
        if (metadata == null) {
            return "";
        } else if (metadata.getClass() == DiCompileUnit.class) {
            DiCompileUnit diCompileUnit = (DiCompileUnit)metadata;
            return this.getFilename(diCompileUnit.getFile());
        } else if (metadata.getClass() == DiExpression.class) {
            DiExpression diExpression = (DiExpression)metadata;
            return this.getFilename(diExpression.getVariable());
        } else if (metadata.getClass() == DiFile.class) {
            DiFile diFile = (DiFile)metadata;
            return diFile.getFilename();
        } else if (metadata.getClass() == DiLexicalBlock.class) {
            DiLexicalBlock diLexicalBlock = (DiLexicalBlock)metadata;
            return this.getFilename(diLexicalBlock.getFile());
        } else if (metadata.getClass() == DiLocation.class) {
            DiLocation diLocation = (DiLocation)metadata;
            return this.getFilename(diLocation.getScope());
        } else if (metadata.getClass() == DiSubprogram.class) {
            DiSubprogram diSubprogram = (DiSubprogram)metadata;
            return this.getFilename(diSubprogram.getFile());
        } else if (metadata.getClass() == DiVariable.class) {
            DiVariable diVariable = (DiVariable)metadata;
            return this.getFilename(diVariable.getFile());
        } else {
            return "";
        }
    }

    public Metadata() {
        this.subprogram = new HashSet<String>();
        this.debug = new HashMap<String, String>();
        this.metadata = new HashMap<String, DiNode>();
    }

    public void initialize() {
        this.subprogram.clear();
        this.debug.clear();
        this.metadata.clear();
    }

    public HashSet<String> getSubprogram() {
        return this.subprogram;
    }

    public void buildDebug(Debug debug) {
        String metadata = this.debug.get(debug.getIdentity());
        debug.setLine(this.getLine(metadata));
        debug.setSubprogram(this.getSubprogram(metadata));
        debug.setFilename(this.getFilename(metadata));
    }

    public void debugRule() {
        String debug = LLVMIR.database.getDebug();
        String caller = LLVMIR.database.getCaller();
        String identity = getIdentity(caller, LLVMIR.database.getIdentity());
        this.debug.put(identity, debug);
    }

    public void llvmDeclareRule() {
        String caller = LLVMIR.database.getCaller();
        String metadata = LLVMIR.database.getMetadata();
        String identity = getIdentity(caller, LLVMIR.database.getIdentity());
        this.debug.put(identity, metadata);
    }

    public void diCompileUnitRule() {
        String file = LLVMIR.database.getFile();
        String metadata = LLVMIR.database.getMetadata();
        DiCompileUnit diCompileUnit = new DiCompileUnit(metadata, file);
        this.metadata.put(metadata, diCompileUnit);
    }

    public void diExpressionRule() {
        String variable = LLVMIR.database.getVariable();
        String metadata = LLVMIR.database.getMetadata();
        DiExpression diExpression = new DiExpression(metadata, variable);
        this.metadata.put(metadata, diExpression);
    }

    public void diFileRule() {
        String filename = LLVMIR.database.getFilename();
        String metadata = LLVMIR.database.getMetadata();
        DiFile diFile = new DiFile(metadata, filename);
        this.metadata.put(metadata, diFile);
    }

    public void diLexicalBlockRule() {
        String line = LLVMIR.database.getLine();
        String file = LLVMIR.database.getFile();
        String scope = LLVMIR.database.getScope();
        String metadata = LLVMIR.database.getMetadata();
        DiLexicalBlock diLexicalBlock = new DiLexicalBlock(metadata, scope, file, line);
        this.metadata.put(metadata, diLexicalBlock);
    }

    public void diLocationRule() {
        String line = LLVMIR.database.getLine();
        String scope = LLVMIR.database.getScope();
        String metadata = LLVMIR.database.getMetadata();
        DiLocation diLocation = new DiLocation(metadata, line, scope);
        this.metadata.put(metadata, diLocation);
    }

    public void diSubprogramRule() {
        String name = LLVMIR.database.getName();
        String line = LLVMIR.database.getLine();
        String file = LLVMIR.database.getFile();
        String scope = LLVMIR.database.getScope();
        String metadata = LLVMIR.database.getMetadata();
        DiSubprogram diSubprogram = new DiSubprogram(metadata, name, scope, file, line);
        this.metadata.put(metadata, diSubprogram);
        this.subprogram.add(name);
    }

    public void diVariableRule() {
        String name = LLVMIR.database.getName();
        String line = LLVMIR.database.getLine();
        String file = LLVMIR.database.getFile();
        String scope = LLVMIR.database.getScope();
        String metadata = LLVMIR.database.getMetadata();
        DiVariable diVariable = new DiVariable(metadata, name, scope, file, line);
        this.metadata.put(metadata, diVariable);
    }
}