package cn.njupt.xjy.analyzers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Objects;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import cn.njupt.xjy.grammars.LLVMIRLexer;
import cn.njupt.xjy.grammars.LLVMIRParser;

public class Analyzer extends Base {
    private static Integer window = 75;
    private static LLVMIR code = new LLVMIR();
    private static String benchname = "";
    private static Path benchmark = Paths.get("");
    private static Defect benchtype = Defect.MemoryLeak;
    private static Config benchconf = Config.Off;

    private static void chain(Path input_path, Path output_path) {
        if (!Files.isRegularFile(input_path)) {
            return;
        }
        String filename = input_path.getFileName().toString();
        String name = filename.substring(0, filename.lastIndexOf("."));
        if (!filename.endsWith(".ll")) {
            return;
        }
        LLVMIR.evaluation.initialize();
        analysis(filename);
        try {
            File jsonFile = output_path.resolve(name + ".json").toFile();
            LocalDateTime now = LocalDateTime.now();
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            JsonGenerator builder = mapper.createGenerator(jsonFile, JsonEncoding.UTF8);
            builder.writeStartObject();
            builder.writeStringField("benchmark", filename);
            builder.writeStringField("benchtype", benchtype.toString());
            builder.writeStringField("benchconf", benchconf.toString());
            builder.writeStringField("time_stamp", now.toString());
            LLVMIR.evaluation.save(builder);
            builder.writeEndObject();
            builder.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void auto(Path path) {
        if (!Files.isRegularFile(path)) {
            return;
        }
        String filename = path.getFileName().toString();
        if (!filename.endsWith(".ll")) {
            return;
        }
        analysis(filename);
    }

    public static void analysis(String filename) {
        try {
            // Suffix checker
            if (!filename.endsWith(".ll")) {
                throw new Exception("Source file is not a LLVM IR!");
            }
            // Java IO
            System.out.printf("Analyzing \"%s\"\n", filename);
            File file = benchmark.resolve(filename).toFile();
            InputStream fileStream = new FileInputStream(file);
            // Antlr4 IO
            Long start = System.currentTimeMillis();
            CharStream charStream = CharStreams.fromStream(fileStream);
            LLVMIRLexer lexer = new LLVMIRLexer(charStream);
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            LLVMIRParser parser = new LLVMIRParser(tokenStream);
            LLVMIRParser.CompilationUnitContext parseTree = parser.compilationUnit();
            LLVMIR.evaluation.setParseTime(System.currentTimeMillis() - start);
            // Initialize
            LLVMIR.initialize();
            // Traverse Parse Tree
            start = System.currentTimeMillis();
            ParseTreeWalker.DEFAULT.walk(code, parseTree);
            LLVMIR.evaluation.setGraphTime(System.currentTimeMillis() - start);
            // Debug Information Generation
            LLVMIR.memoryGraph.buildDebug();
            // Pointer Analysis
            if (Clash.intra_procedural == true) {
                // Memory Graph Generation
                start = System.currentTimeMillis();
                LLVMIR.memoryGraph.buildGraph();
                LLVMIR.evaluation.setPointerTime(System.currentTimeMillis() - start);
                // Call Graph Generation
                LLVMIR.callGraph.buildGraph();
                // Check Memory Defect
                start = System.currentTimeMillis();
                LLVMIR.memoryGraph.checkMemoryDefect(benchtype);
                LLVMIR.evaluation.setQueryTime(System.currentTimeMillis() - start);
            }
            // Type Graph Generation
            start = System.currentTimeMillis();
            LLVMIR.typeGraph.buildGraph();
            LLVMIR.evaluation.setTypeTime(System.currentTimeMillis() - start);
            // Benchmark Generation
            LLVMIR.evaluation.buildBenchmark();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void autoAnalysis() {
        try {
            Path results = Paths.get("results");
            Files.createDirectories(results);
            File jsonFile = results.resolve(benchname + ".json").toFile();
            LocalDateTime now = LocalDateTime.now();
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            JsonGenerator builder = mapper.createGenerator(jsonFile, JsonEncoding.UTF8);
            builder.writeStartObject();
            builder.writeStringField("benchmark", benchmark.toString());
            builder.writeStringField("benchtype", benchtype.toString());
            builder.writeStringField("benchconf", benchconf.toString());
            builder.writeStringField("time_stamp", now.toString());
            Files.list(benchmark).forEach(Analyzer::auto);
            LLVMIR.evaluation.save(builder);
            builder.writeEndObject();
            builder.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void chainAnalysis() {
        try {
            Path results = Paths.get("results", benchname);
            Files.createDirectories(results);
            Files.list(benchmark).forEach(x -> chain(x, results));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showConfigure() {
        showTitle("Configure", window);
        System.out.printf("Display Window Width: %d\n", window);
        System.out.printf("Benchmark Name: %s\n", benchname);
        System.out.printf("Benchmark Path: %s\n", benchmark.toString());
        System.out.printf("Benchmark Type: %s\n", benchtype.toString());
        System.out.printf("Benchmark Config: %s\n", benchconf.toString());
        System.out.printf("Intra-procedural Analysis: %s\n", Clash.intra_procedural.toString());
        System.out.printf("Inter-procedural Analysis: %s\n", Clash.inter_procedural.toString());
        System.out.printf("Type-based Edge Build: %s\n", Clash.type_based.toString());
        System.out.printf("Pointer-based Edge Build: %s\n", Clash.pointer_based.toString());
        System.out.printf("Combination Edge Build: %s\n", Clash.combined.toString());
        System.out.printf("Call Store Constraint: %s\n", Clash.CSC_enabled.toString());
        System.out.printf("Call Return Constraint: %s\n", Clash.CRC_enabled.toString());
        System.out.printf("Call Back Constraint: %s\n", Clash.CBC_enabled.toString());
        System.out.printf("Mutex Inherit: %s\n", Clash.mutex_inherit.toString());
        System.out.printf("Context Sensitive: %s\n", Clash.context_sensitive.toString());
        showTitle("", window);
    }

    public static void setWindow(Integer size) {
        if (size <= 0) {
            return;
        }
        window = size;
    }

    public static void setBenchmark(String path, Defect type, Config config) {
        benchmark = Paths.get(path);
        benchtype = type;
        benchconf = config;
        LocalDateTime now = LocalDateTime.now();
        benchname = String.valueOf(Objects.hash(path, type, config, now));
        LLVMIR.evaluation.initialize();
        switch (config) {
            case Config.Off:
                Clash.OffMode();
                break;
            case Config.IntraOnly:
                Clash.IntraOnlyMode();
                break;
            case Config.DCallOnly:
                Clash.DCallOnlyMode();
                break;
            case Config.ICallType:
                Clash.ICallTypeMode();
                break;
            case Config.ICallPointer:
                Clash.ICallPointerMode();
                break;
            case Config.ICallCombined:
                Clash.ICallCombinedMode();
                break;
            case Config.CSC:
                Clash.CSCMode();
                break;
            case Config.CRC:
                Clash.CRCMode();
                break;
            case Config.CBC:
                Clash.CBCMode();
                break;
            case Config.CSCMI:
                Clash.CSCMIMode();
                break;
            case Config.CRCMI:
                Clash.CRCMIMode();
                break;
            case Config.CBCMI:
                Clash.CBCMIMode();
                break;
            case Config.Clash:
                Clash.ClashMode();
                break;
            case Config.ClashType:
                Clash.ClashTypeMode();
                break;
            case Config.ClashCombined:
                Clash.ClashCombinedMode();
                break;
        }
    }

    public static void resetBenchmark() {
        LLVMIR.evaluation.initialize();
    }

    public static void showMemoryGraph() {
        LLVMIR.memoryGraph.show();
    }

    public static void showCallGraph() {
        LLVMIR.callGraph.show();
    }

    public static void showTypeGraph() {
        LLVMIR.typeGraph.show();
    }

    public static void showBenchmark() {
        LLVMIR.evaluation.show(window);
    }

    public static void saveBenchmark(String filename) {
        try {
            if (!filename.endsWith(".json")) {
                throw new Exception("Benchmark Info can only be saved in JSON file!");
            }
            Path results = Paths.get("results");
            Files.createDirectories(results);
            File jsonFile = results.resolve(filename).toFile();
            if (jsonFile.exists()) {
                throw new Exception("A same JSON file has existed!");
            }
            LocalDateTime now = LocalDateTime.now();
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            JsonGenerator builder = mapper.createGenerator(jsonFile, JsonEncoding.UTF8);
            builder.writeStartObject();
            builder.writeStringField("benchmark", benchmark.toString());
            builder.writeStringField("benchtype", benchtype.toString());
            builder.writeStringField("benchconf", benchconf.toString());
            builder.writeStringField("time_stamp", now.toString());
            LLVMIR.evaluation.save(builder);
            builder.writeEndObject();
            builder.close();
            System.out.println("Benchmark Info has been saved in File: " + jsonFile.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}