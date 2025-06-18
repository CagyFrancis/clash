package cn.njupt.xjy.analyzers;

import java.util.HashMap;
import java.util.HashSet;

public class Constant {
    private HashSet<String> entry;
    private HashSet<String> declare;
    private HashMap<String, Manager> constructor;
    private HashMap<String, Manager> destructor;

    public Constant() {
        this.entry = new HashSet<String>();
        this.declare = new HashSet<String>();
        this.constructor = new HashMap<String, Manager>();
        this.destructor = new HashMap<String, Manager>();
        this.entry.add("@main");
        this.declare.add("@llvm.dbg.declare");
        this.constructor.put("@malloc", Manager.Free);
        this.constructor.put("@calloc", Manager.Free);
        this.constructor.put("@realloc", Manager.Free);
        this.constructor.put("@strdup", Manager.Free);
        this.constructor.put("@wcsdup", Manager.Free);
        this.constructor.put("@fopen", Manager.Close);
        this.constructor.put("@_Znwy", Manager.Delete);
        this.constructor.put("@_Znwm", Manager.Delete);
        this.constructor.put("@_Znay", Manager.DeleteArray);
        this.constructor.put("@_Znam", Manager.DeleteArray);
        this.destructor.put("@free", Manager.Free);
        this.destructor.put("@_ZdlPv", Manager.Delete);
        this.destructor.put("@_ZdlPvm", Manager.Delete);
        this.destructor.put("@_ZdaPv", Manager.DeleteArray);
        this.destructor.put("@_ZdaPvm", Manager.DeleteArray);
        this.destructor.put("@fclose", Manager.Close);
    }

    public HashSet<String> getEntry() {
        return this.entry;
    }

    public Manager getManager(String callee) {
        Manager constructor = this.constructor.get(callee);
        if (constructor != null) {
            return constructor;
        }
        Manager destructor = this.destructor.get(callee);
        if (destructor != null) {
            return destructor;
        }
        return Manager.None;
    }

    public Boolean isLLVMDeclare(String declare) {
        return this.declare.contains(declare);
    }

    public Boolean isConstructor(String constructor) {
        return this.constructor.keySet().contains(constructor);
    }

    public Boolean isDestructor(String destructor) {
        return this.destructor.keySet().contains(destructor);
    }
}