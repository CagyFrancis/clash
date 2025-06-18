package cn.njupt.xjy.analyzers;

import java.util.HashSet;

public class Clash {
    protected static Boolean intra_procedural = false;
    protected static Boolean inter_procedural = false;
    protected static Boolean combined = false;
    protected static Boolean type_based = false;
    protected static Boolean pointer_based = false;
    protected static Boolean CSC_enabled = false;
    protected static Boolean CRC_enabled = false;
    protected static Boolean CBC_enabled = false;
    protected static Boolean mutex_inherit = false;
    protected static Boolean context_sensitive = false;

    protected Boolean hasClash(Memory memory, Mutex clash) {
        HashSet<Mutex> dstMutex = clash.getMutex();
        for (Mutex mutex : memory.getMutex()) {
            HashSet<Mutex> srcMutex = mutex.getMutex();
            if (mutex.equals(clash)) {
                return false;
            } else if (srcMutex == dstMutex) {
                return true;
            }
        }
        return false;
    }

    protected Boolean hasClash(Memory source, Memory sink) {
        if (sink == null) {
            return false;
        }
        for (Mutex srcMutex : source.getMutex()) {
            for (Mutex dstMutex : sink.getMutex()) {
                if (srcMutex.equals(dstMutex)) {
                    break;
                } else if (srcMutex.getMutex() == dstMutex.getMutex()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void setClash(Memory memory, Mutex clash) {
        if (context_sensitive == false) {
            return;
        }
        memory.setMutex(clash);
    }

    protected void setClash(Memory source, Memory sink) {
        if (mutex_inherit == false) {
            return;
        }
        sink.setAllMutex(source.getMutex());
    }

    protected static void OffMode() {
        Clash.intra_procedural = false;
        Clash.inter_procedural = false;
        Clash.type_based = false;
        Clash.pointer_based = false;
        Clash.combined = false;
        Clash.CSC_enabled = false;
        Clash.CRC_enabled = false;
        Clash.CBC_enabled = false;
        Clash.mutex_inherit = false;
        Clash.context_sensitive = false;
    }

    protected static void IntraOnlyMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = false;
        Clash.type_based = false;
        Clash.pointer_based = false;
        Clash.combined = false;
        Clash.CSC_enabled = false;
        Clash.CRC_enabled = false;
        Clash.CBC_enabled = false;
        Clash.mutex_inherit = false;
        Clash.context_sensitive = false;
    }

    protected static void DCallOnlyMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = false;
        Clash.pointer_based = false;
        Clash.combined = false;
        Clash.CSC_enabled = false;
        Clash.CRC_enabled = false;
        Clash.CBC_enabled = false;
        Clash.mutex_inherit = false;
        Clash.context_sensitive = false;
    }

    protected static void ICallTypeMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = true;
        Clash.pointer_based = false;
        Clash.combined = false;
        Clash.CSC_enabled = false;
        Clash.CRC_enabled = false;
        Clash.CBC_enabled = false;
        Clash.mutex_inherit = false;
        Clash.context_sensitive = false;
    }

    protected static void ICallPointerMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = false;
        Clash.pointer_based = true;
        Clash.combined = false;
        Clash.CSC_enabled = false;
        Clash.CRC_enabled = false;
        Clash.CBC_enabled = false;
        Clash.mutex_inherit = false;
        Clash.context_sensitive = false;
    }

    protected static void ICallCombinedMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = false;
        Clash.pointer_based = false;
        Clash.combined = true;
        Clash.CSC_enabled = false;
        Clash.CRC_enabled = false;
        Clash.CBC_enabled = false;
        Clash.mutex_inherit = false;
        Clash.context_sensitive = false;
    }

    protected static void CSCMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = false;
        Clash.pointer_based = true;
        Clash.combined = false;
        Clash.CSC_enabled = true;
        Clash.CRC_enabled = false;
        Clash.CBC_enabled = false;
        Clash.mutex_inherit = false;
        Clash.context_sensitive = true;
    }

    protected static void CRCMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = false;
        Clash.pointer_based = true;
        Clash.combined = false;
        Clash.CSC_enabled = false;
        Clash.CRC_enabled = true;
        Clash.CBC_enabled = false;
        Clash.mutex_inherit = false;
        Clash.context_sensitive = true;
    }

    protected static void CBCMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = false;
        Clash.pointer_based = true;
        Clash.combined = false;
        Clash.CSC_enabled = false;
        Clash.CRC_enabled = false;
        Clash.CBC_enabled = true;
        Clash.mutex_inherit = false;
        Clash.context_sensitive = true;
    }

    protected static void CSCMIMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = false;
        Clash.pointer_based = true;
        Clash.combined = false;
        Clash.CSC_enabled = true;
        Clash.CRC_enabled = false;
        Clash.CBC_enabled = false;
        Clash.mutex_inherit = true;
        Clash.context_sensitive = true;
    }

    protected static void CRCMIMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = false;
        Clash.pointer_based = true;
        Clash.combined = false;
        Clash.CSC_enabled = false;
        Clash.CRC_enabled = true;
        Clash.CBC_enabled = false;
        Clash.mutex_inherit = true;
        Clash.context_sensitive = true;
    }

    protected static void CBCMIMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = false;
        Clash.pointer_based = true;
        Clash.combined = false;
        Clash.CSC_enabled = false;
        Clash.CRC_enabled = false;
        Clash.CBC_enabled = true;
        Clash.mutex_inherit = true;
        Clash.context_sensitive = true;
    }

    protected static void ClashMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = false;
        Clash.pointer_based = true;
        Clash.combined = false;
        Clash.CSC_enabled = true;
        Clash.CRC_enabled = true;
        Clash.CBC_enabled = true;
        Clash.mutex_inherit = true;
        Clash.context_sensitive = true;
    }

    protected static void ClashTypeMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = true;
        Clash.pointer_based = false;
        Clash.combined = false;
        Clash.CSC_enabled = true;
        Clash.CRC_enabled = true;
        Clash.CBC_enabled = true;
        Clash.mutex_inherit = true;
        Clash.context_sensitive = true;
    }

    protected static void ClashCombinedMode() {
        Clash.intra_procedural = true;
        Clash.inter_procedural = true;
        Clash.type_based = false;
        Clash.pointer_based = false;
        Clash.combined = true;
        Clash.CSC_enabled = true;
        Clash.CRC_enabled = true;
        Clash.CBC_enabled = true;
        Clash.mutex_inherit = true;
        Clash.context_sensitive = true;
    }
}