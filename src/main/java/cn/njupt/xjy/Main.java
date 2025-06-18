package cn.njupt.xjy;

import cn.njupt.xjy.analyzers.Analyzer;
import cn.njupt.xjy.analyzers.Config;
import cn.njupt.xjy.analyzers.Defect;

public class Main {
    public static void main(String[] args) {
        String benchmark = "examples";
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.IntraOnly);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.DCallOnly);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.ICallType);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.ICallPointer);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.ICallCombined);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.CSC);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.CBC);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.CRC);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.CSCMI);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.CBCMI);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.CRCMI);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.Clash);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.ClashType);
        Analyzer.autoAnalysis();
        Analyzer.setBenchmark(benchmark, Defect.MemoryLeak, Config.ClashCombined);
        Analyzer.autoAnalysis();
    }
}