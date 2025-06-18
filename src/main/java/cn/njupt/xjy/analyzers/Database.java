package cn.njupt.xjy.analyzers;

import java.util.List;
import java.util.ArrayList;

public class Database {
    private String caller;
    private String callee;
    private Integer parameter;
    private Integer argument;
    private Integer callsite;
    private String type;
    private String receiver;
    private Manager manager;
    private String identity;
    private String debug;
    private String metadata;
    private String line;
    private String scope;
    private String name;
    private String file;
    private String filename;
    private String variable;
    private List<String> value;
    private List<String> signature;

    public Database() {
        this.caller = "";
        this.callee = "";
        this.parameter = 0;
        this.argument = 0;
        this.callsite = 0;
        this.type = "";
        this.receiver = "";
        this.manager = Manager.None;
        this.identity = "";
        this.debug = "";
        this.metadata = "";
        this.line = "";
        this.scope = "";
        this.name = "";
        this.file = "";
        this.filename = "";
        this.variable = "";
        this.value = new ArrayList<String>();
        this.signature = new ArrayList<String>();
    }

    public void initialize() {
        this.caller = "";
        this.callee = "";
        this.parameter = 0;
        this.argument = 0;
        this.callsite = 0;
        this.type = "";
        this.receiver = "";
        this.manager = Manager.None;
        this.identity = "";
        this.debug = "";
        this.metadata = "";
        this.line = "";
        this.scope = "";
        this.name = "";
        this.file = "";
        this.filename = "";
        this.variable = "";
        this.value.clear();
        this.signature.clear();
    }

    public String getCaller() {
        return this.caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCallee() {
        return this.callee;
    }

    public void setCallee(String callee) {
        this.callee = callee;
    }

    public Integer getParameter() {
        return this.parameter;
    }

    public void resetParameter() {
        this.parameter = 0;
    }

    public void pushParameter() {
        this.parameter++;
    }

    public Integer getArgument() {
        return this.argument;
    }

    public void resetArgument() {
        this.argument = 0;
    }

    public void pushArgument() {
        this.argument++;
    }

    public String getCallSite() {
        return String.valueOf(this.callsite);
    }

    public void resetCallSite() {
        this.callsite = 0;
    }

    public void pushCallSite() {
        this.callsite++;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void resetReceiver() {
        this.receiver = "";
    }

    public String getIdentity() {
        return this.identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Manager getManager() {
        return this.manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public List<String> getValue() {
        return this.value;
    }

    public String getValue(Integer index) {
        return this.value.get(index);
    }

    public void addValue(String value) {
        this.value.add(value);
    }

    public void resetValue() {
        this.value.clear();
    }

    public String getDebug() {
        return this.debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getLine() {
        return this.line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getVariable() {
        return this.variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getSignature() {
        return this.signature.toString();
    }

    public void addSignature(String signature) {
        this.signature.add(signature);
    }

    public void resetSignature() {
        this.signature.clear();
    }
}