package cn.njupt.xjy.analyzers;

import org.antlr.v4.runtime.tree.TerminalNode;

import cn.njupt.xjy.grammars.LLVMIRBaseListener;
import cn.njupt.xjy.grammars.LLVMIRParser;

public class LLVMIR extends LLVMIRBaseListener {
    protected static Constant constant = new Constant();
    protected static Database database = new Database();
    protected static Metadata metadata = new Metadata();
    protected static TypeGraph typeGraph = new TypeGraph();
    protected static CallGraph callGraph = new CallGraph();
    protected static MemoryGraph memoryGraph = new MemoryGraph();
    protected static Evaluation evaluation = new Evaluation();

    private static String strip(String content) {
        if (content.startsWith("\"")) {
            content = content.substring(1);
        }
        if (content.endsWith("\"")) {
            content = content.substring(0, content.length() - 1);
        }
        return content;
    }

    private String getConstant(LLVMIRParser.ConstantContext ctx) {
        LLVMIRParser.ConstantExprContext expression = ctx.constantExpr();
        if (expression == null) {
            return ctx.getText();
        }
        LLVMIRParser.GetElementPtrExprContext instruction = expression.getElementPtrExpr();
        if (instruction == null) {
            return ctx.getText();
        }
        return this.getConstant(instruction.typeConst().constant());
    }

    private String getValue(LLVMIRParser.ValueContext ctx) {
        LLVMIRParser.ConstantContext constant = ctx.constant();
        if (constant == null) {
            return ctx.getText();
        } else {
            return this.getConstant(constant);
        }
    }

    private String getType(LLVMIRParser.ArgContext ctx) {
        LLVMIRParser.ConcreteTypeContext concrete = ctx.concreteType();
        LLVMIRParser.MetadataTypeContext metadata = ctx.metadataType();
        if (concrete != null) {
            return concrete.getText();
        } else if (metadata != null) {
            return metadata.getText();
        } else {
            return "";
        }
    }

    public static void initialize() {
        database.initialize();
        metadata.initialize();
        typeGraph.initialize();
        callGraph.initialize();
        memoryGraph.initialize();
    }

    @Override
    public void enterMetadataAttachment(LLVMIRParser.MetadataAttachmentContext ctx) {
        String metadata = ctx.MetadataName().getText();
        if (metadata.equals("!dbg")) {
            database.setDebug(ctx.mdNode().getText());
        }
    }

    @Override
    public void exitGlobalDef(LLVMIRParser.GlobalDefContext ctx) {
        database.setIdentity(ctx.GlobalIdent().getText());
        metadata.debugRule();
        memoryGraph.globalRule();
    }

    @Override
    public void exitGlobalDecl(LLVMIRParser.GlobalDeclContext ctx) {
        database.setIdentity(ctx.GlobalIdent().getText());
        metadata.debugRule();
        memoryGraph.globalRule();
    }

    @Override
    public void enterFuncDef(LLVMIRParser.FuncDefContext ctx) {
        LLVMIRParser.FuncHeaderContext header = ctx.funcHeader();
        database.resetParameter();
        database.resetSignature();
        database.addSignature(header.type().getText());
        database.setCaller(header.GlobalIdent().getText());
    }

    @Override
    public void enterFuncBody(LLVMIRParser.FuncBodyContext ctx) {
        database.setIdentity(database.getCaller());
        metadata.debugRule();
        callGraph.functionRule();
        memoryGraph.globalRule();
        typeGraph.signatureRule();
    }

    @Override
    public void enterFuncDecl(LLVMIRParser.FuncDeclContext ctx) {
        LLVMIRParser.FuncHeaderContext header = ctx.funcHeader();
        database.resetParameter();
        database.resetSignature();
        database.addSignature(header.type().getText());
        database.setCaller(header.GlobalIdent().getText());
    }

    @Override
    public void exitFuncDecl(LLVMIRParser.FuncDeclContext ctx) {
        database.setIdentity(database.getCaller());
        metadata.debugRule();
        callGraph.libraryRule();
        memoryGraph.globalRule();
        typeGraph.signatureRule();
    }

    @Override
    public void enterParam(LLVMIRParser.ParamContext ctx) {
        database.pushParameter();
        database.addSignature(ctx.type().getText());
        TerminalNode identity = ctx.LocalIdent();
        if (identity == null) {
            return;
        }
        database.setIdentity(identity.getText());
        memoryGraph.parameterRule();
    }

    @Override
    public void enterBasicBlock(LLVMIRParser.BasicBlockContext ctx) {
        LLVMIR.evaluation.pushBasicBlock();
    }

	@Override
    public void enterInstruction(LLVMIRParser.InstructionContext ctx) {
        LLVMIR.evaluation.pushInstruction();
    }

    @Override
    public void enterLocalDefInst(LLVMIRParser.LocalDefInstContext ctx) {
        String identity = ctx.LocalIdent().getText();
        database.setIdentity(identity);
        if (ctx.valueInstruction().callInst() != null) {
            database.setReceiver(identity);
        }
    }

    @Override
    public void exitAllocaInst(LLVMIRParser.AllocaInstContext ctx) {
        metadata.debugRule();
        memoryGraph.allocaRule();
    }

    @Override
    public void exitLoadInst(LLVMIRParser.LoadInstContext ctx) {
        LLVMIRParser.TypeValueContext typeValue = ctx.typeValue();
        database.resetValue();
        database.addValue(this.getValue(typeValue.value()));
        database.setType(typeValue.firstClassType().getText());
        metadata.debugRule();
        memoryGraph.loadRule();
    }

    @Override
    public void enterCallInst(LLVMIRParser.CallInstContext ctx) {
        String callee = this.getValue(ctx.value());
        database.pushCallSite();
        database.resetArgument();
        database.resetSignature();
        database.setCallee(callee);
        database.addSignature(ctx.type().getText());
        callGraph.callRule();
    }

    @Override
    public void exitCallInst(LLVMIRParser.CallInstContext ctx) {
        typeGraph.callSiteRule();
        String callee = database.getCallee();
        if (constant.isLLVMDeclare(callee)) {
            LLVMIRParser.TypeValueContext typevalue = ctx.args().arg(0).metadata().typeValue();
            if (typevalue == null) {
                return;
            }
            database.setIdentity(typevalue.value().getText());
            database.setMetadata(ctx.args().arg(1).metadata().MetadataId().getText());
            metadata.llvmDeclareRule();
        } else if (!database.getReceiver().isEmpty()) {
            database.setIdentity(database.getReceiver());
            if (constant.isConstructor(callee)) {
                database.setManager(constant.getManager(callee));
                memoryGraph.constructRule();
            } else {
                memoryGraph.receiverRule();
            }
            metadata.debugRule();
        }
        database.resetReceiver();
    }

    @Override
    public void enterArg(LLVMIRParser.ArgContext ctx) {
        database.pushArgument();
        database.addSignature(this.getType(ctx));
        String callee = database.getCallee();
        LLVMIRParser.ValueContext value = ctx.value();
        if (value == null) {
            return;
        }
        database.setIdentity(this.getValue(value));
        if (constant.isDestructor(callee)) {
            database.setManager(constant.getManager(callee));
            memoryGraph.destructRule();
        } else {
            memoryGraph.argumentRule();
        }
    }

    @Override
    public void exitStoreInst(LLVMIRParser.StoreInstContext ctx) {
        database.resetValue();
        ctx.typeValue().forEach(x -> database.addValue(this.getValue(x.value())));
        memoryGraph.storeRule();
    }

    @Override
    public void exitGetElementPtrInst(LLVMIRParser.GetElementPtrInstContext ctx) {
        database.resetValue();
        ctx.typeValue().forEach(x -> database.addValue(this.getValue(x.value())));
        metadata.debugRule();
        memoryGraph.elementRule();
    }

    @Override
    public void exitSelectInst(LLVMIRParser.SelectInstContext ctx) {
        database.resetValue();
        ctx.typeValue().forEach(x -> database.addValue(this.getValue(x.value())));
        metadata.debugRule();
        memoryGraph.selectRule();
    }

    @Override
    public void exitPhiInst(LLVMIRParser.PhiInstContext ctx) {
        database.resetValue();
        ctx.inc().forEach(x -> database.addValue(this.getValue(x.value())));
        metadata.debugRule();
        memoryGraph.phiRule();
    }

    @Override
    public void exitRetTerm(LLVMIRParser.RetTermContext ctx) {
        LLVMIRParser.ValueContext value = ctx.value();
        if (value == null) {
            return;
        }
        database.setIdentity(this.getValue(value));
        memoryGraph.returnRule();
    }

    @Override
    public void enterMetadataDef(LLVMIRParser.MetadataDefContext ctx) {
        evaluation.pushMetadata();
        database.setMetadata(ctx.MetadataId().getText());
    }

    @Override
    public void enterLineField(LLVMIRParser.LineFieldContext ctx) {
        database.setLine(ctx.IntLit().getText());
    }

    @Override
    public void enterScopeField(LLVMIRParser.ScopeFieldContext ctx) {
        database.setScope(ctx.mdField().getText());
    }

    @Override
    public void enterNameField(LLVMIRParser.NameFieldContext ctx) {
        database.setName(strip(ctx.StringLit().getText()));
    }

    @Override
    public void enterFileField(LLVMIRParser.FileFieldContext ctx) {
        database.setFile(ctx.mdField().getText());
    }

    @Override
    public void enterVarField(LLVMIRParser.VarFieldContext ctx) {
        database.setVariable(ctx.mdField().getText());
    }

    @Override
    public void enterFilenameField(LLVMIRParser.FilenameFieldContext ctx) {
        database.setFilename(strip(ctx.StringLit().getText()));
    }

    @Override
    public void exitDiCompileUnit(LLVMIRParser.DiCompileUnitContext ctx) {
        metadata.diCompileUnitRule();
    }

    @Override
    public void exitDiGlobalVariableExpression(LLVMIRParser.DiGlobalVariableExpressionContext ctx) {
        metadata.diExpressionRule();
    }

    @Override
    public void exitDiFile(LLVMIRParser.DiFileContext ctx) {
        metadata.diFileRule();
    }

    @Override
    public void exitDiLexicalBlock(LLVMIRParser.DiLexicalBlockContext ctx) {
        metadata.diLexicalBlockRule();
    }

    @Override
    public void exitDiLocation(LLVMIRParser.DiLocationContext ctx) {
        metadata.diLocationRule();
    }

    @Override
    public void exitDiSubprogram(LLVMIRParser.DiSubprogramContext ctx) {
        metadata.diSubprogramRule();
    }

    @Override
    public void exitDiGlobalVariable(LLVMIRParser.DiGlobalVariableContext ctx) {
        metadata.diVariableRule();
    }

    @Override
    public void exitDiLocalVariable(LLVMIRParser.DiLocalVariableContext ctx) {
        metadata.diVariableRule();
    }
}