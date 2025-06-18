package cn.njupt.xjy.analyzers;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class Summary {
    private String identity;
    private HashMap<Integer, String> parameters;
    private HashMap<Integer, HashSet<Context>> arguments;
    private HashSet<String> returns;
    private HashSet<Context> receivers;
    private HashSet<Mutex> mutex;

    public Summary(String identity) {
        this.identity = identity;
        this.parameters = new HashMap<Integer, String>();
        this.arguments = new HashMap<Integer, HashSet<Context>>();
        this.returns = new HashSet<String>();
        this.receivers = new HashSet<Context>();
        this.mutex = new HashSet<Mutex>();
    }

    public String getIdentity() {
        return this.identity;
    }

    public Set<Integer> getIndices() {
        return this.arguments.keySet();
    }

    public void addParameters(Integer index, String identity) {
        this.parameters.put(index, identity);
    }

    public String getParameters(Integer index) {
        return this.parameters.get(index);
    }

    public void addArguments(Integer index, Context context) {
        HashSet<Context> arguments = this.arguments.get(index);
        if (arguments == null) {
            arguments = new HashSet<Context>();
            this.arguments.put(index, arguments);
        }
        arguments.add(context);
        this.mutex.add(new Mutex(context.getCallSite(), this.mutex));
    }

    public HashSet<Context> getArguments(Integer index) {
        return this.arguments.get(index);
    }

    public void addReturns(String identity) {
        this.returns.add(identity);
    }

    public HashSet<String> getReturns() {
        return this.returns;
    }

    public void addReceivers(Context context) {
        this.receivers.add(context);
        this.mutex.add(new Mutex(context.getCallSite(), this.mutex));
    }

    public HashSet<Context> getReceivers() {
        return this.receivers;
    }

    public HashSet<Mutex> getMutex() {
        return this.mutex;
    }
}