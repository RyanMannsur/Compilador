package compilador;

import java.io.IOException;
import static java.lang.System.exit;
import symbols.Tab;

public class Parser {

    Lexer l;
    Token t = null;
    Tab words = null;

    public Parser(Lexer l, Tab words) throws IOException {
        this.l = l;
        this.words = words;
        t = l.nextToken();
    }

    public void advance() throws IOException {
        t = l.nextToken(); //lê próximo token
    }

    void eat(int tok) throws IOException {
        if (tok == t.tag) {
            //System.out.println("Lexema lido: "+t.toString());
            advance();
        } else {
            System.out.println("Erro sintatico linha: " + Lexer.line);
            System.out.println("Error, esperava tag: " + tok + " recebeu token: " + "" + t.toString());
            exit(0);
        }
    }

    public void SINTATICO() throws IOException {
        switch (t.tag) {
            case Tag.CLAS:
                PROGRAM();
                System.out.println("Analise Sintatica e Semantica feita com Sucesso.");
                break;
            default:
                System.out.println("Erro sintatico na linha " + Lexer.line);
                System.out.println("Esperava class, recebeu: " + t.toString());
                exit(0);
        }
    }

    private void PROGRAM() throws IOException {
        switch (t.tag) {
            case Tag.CLAS:
                eat(Tag.CLAS);
                words.put(t, Tipo.CLASS); //Adiciona o id na tabela de simbolos
                eat(Tag.ID);
                DECL_LIST();
                BODY();
                break;
            default:
                System.out.println("Erro sintatico na linha " + Lexer.line);
                System.out.println("Esperava class, recebeu: " + t.toString());
                exit(0);
        }
    }

    private void DECL_LIST() throws IOException {
        switch (t.tag) {
            case Tag.ACH:
                break;
            case Tag.INT:
            case Tag.STR:
            case Tag.FLOAT:
                DECL();
                eat(Tag.PVI);
                DECL_LIST();
                break;
            default:
                System.out.println("Erro sintatico na linha " + Lexer.line);
                System.out.println("Esperava int, string, float ou '{', recebeu: " + t.toString());
                exit(0);
        }
    }

    private void DECL() throws IOException {
        int tipo;
        switch (t.tag) {
            case Tag.INT:
            case Tag.STR:
            case Tag.FLOAT:
                tipo = TYPE();
                IDENT_LIST(tipo);
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava int, float ou string, recebeu: " + t.toString());
                exit(0);
        }
    }

    private void IDENT_LIST(int tipo) throws IOException {
        switch (t.tag) {
            case Tag.ID:
                if (words.get(t) == null) {
                    words.put(t, tipo);
                } else {
                    System.out.println("Erro sematico linha " + Lexer.line + ". Variavel \"" + t.toString() + "\" ja declarada.");
                    exit(0);
                }
                eat(Tag.ID);
                IDENT_LIST1(tipo);
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava ID, recebeu: " + t.toString());
                exit(0);
        }
    }

    private void IDENT_LIST1(int tipo) throws IOException {
        switch (t.tag) {
            case Tag.ACH:
            case Tag.PVI:
                break;
            case Tag.VIR:
                eat(Tag.VIR);
                if (words.get(t) == null) {
                    words.put(t, tipo);
                } else {
                    System.out.println("Erro sematico linha " + Lexer.line + ". Variavel \"" + t.toString() + "\" ja declarada.");
                    exit(0);
                }
                eat(Tag.ID);
                IDENT_LIST1(tipo);
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Erro, esperava ',' ou ';', recebeu: " + t.toString());
                exit(0);
        }
    }

    private int TYPE() throws IOException {
        switch (t.tag) {
            case Tag.INT:
                eat(Tag.INT);
                return Tipo.INT;
            case Tag.STR:
                eat(Tag.STR);
                return Tipo.STRING;
            case Tag.FLOAT:
                eat(Tag.FLOAT);
                return Tipo.FLOAT;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava int, string ou float, recebeu: " + t.toString());
                exit(0);
        }
        return -1; //tipo erro
    }

    private void BODY() throws IOException {
        switch (t.tag) {
            case Tag.ACH:
                eat(Tag.ACH);
                STMT_LIST();
                eat(Tag.FCH);
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava '}', recebeu: " + t.toString());
                exit(0);
        }
    }

    private void STMT_LIST() throws IOException {
        switch (t.tag) {
            case Tag.ID:
            case Tag.IF:
            case Tag.DO:
            case Tag.READ:
            case Tag.WRI:
                STMT();
                eat(Tag.PVI);
                STMT_LIST1();
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava id, if, do, else, read ou write, recebeu: " + t.toString());
                exit(0);
        }
    }

    private void STMT_LIST1() throws IOException {
        switch (t.tag) {
            case Tag.FCH:
                break;
            case Tag.ID:
            case Tag.IF:
            case Tag.DO:
            case Tag.READ:
            case Tag.WRI:
                STMT();
                eat(Tag.PVI);
                STMT_LIST1();
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava id, if, do, else, read, write ou '}', recebeu: " + t.toString());
                exit(0);
        }
    }

    private void STMT() throws IOException {
        switch (t.tag) {
            case Tag.ID:
                ASSIGN_STMT();
                break;
            case Tag.IF:
                IF_STMT();
                break;
            case Tag.DO:
                DO_STMT();
                break;
            case Tag.READ:
                READ_STMT();
                break;
            case Tag.WRI:
                WRITE_STMT();
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava id, if, do, else, read ou write, recebeu: " + t.toString());
                exit(0);
        }
    }

    private void ASSIGN_STMT() throws IOException {
        int tipo, tipoID;
        switch (t.tag) {
            case Tag.ID:
                if (words.get(t) == null) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Variavel nao declarada");
                    exit(0);
                }
                tipoID = words.getTipo(t);
                eat(Tag.ID);
                eat(Tag.ATR);
                tipo = SIMPLE_EXPR();
                if(tipo != tipoID){
                    //System.out.println(words.getTipo(t));
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Tipos diferentes");
                    exit(0);
                }
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperav id, recebeu: " + t.toString());
                exit(0);
        }
    }

    private void IF_STMT() throws IOException {
        int tipo;
        switch (t.tag) {
            case Tag.IF:
                eat(Tag.IF);
                eat(Tag.APA);
                tipo = CODITION();
                if(tipo != Tipo.BOOL){
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
                eat(Tag.FPA);
                eat(Tag.ACH);
                STMT_LIST();
                eat(Tag.FCH);
                ELSE_STMT();
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperav if, recebeu: " + t.toString());
                exit(0);
        }
    }

    private void ELSE_STMT() throws IOException {
        switch (t.tag) {
            case Tag.PVI:
                break;
            case Tag.ELS:
                eat(Tag.ELS);
                eat(Tag.ACH);
                STMT_LIST();
                eat(Tag.FCH);
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava else ou ';', recebeu: " + t.toString());
                exit(0);
        }
    }

    private int CODITION() throws IOException {
        int tipo = -1;
        switch (t.tag) {
            case Tag.MEN:
            case Tag.ID:
                if (words.get(t) == null) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Variavel nao declarada");
                    exit(0);
                }
            case Tag.NUM:
            case Tag.LIT:
            case Tag.REAL:
            case Tag.APA:
            case Tag.NOT:
                tipo = EXPRESSION();
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava id, const, '-', '!' ou '(', recebeu: " + t.toString());
                exit(0);
        }
        return tipo;
    }

    private void DO_STMT() throws IOException {
        switch (t.tag) {
            case Tag.DO:
                eat(Tag.DO);
                eat(Tag.ACH);
                STMT_LIST();
                eat(Tag.FCH);
                DO_SUFFIX();
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperada 'do', recebeu: " + t.toString());
                exit(0);
        }
    }

    private void DO_SUFFIX() throws IOException {
        int tipo;
        switch (t.tag) {
            case Tag.WHI:
                eat(Tag.WHI);
                eat(Tag.APA);
                tipo = CODITION();
                if(tipo != Tipo.BOOL){
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
                eat(Tag.FPA);
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava 'while', recebeu: " + t.toString());
                exit(0);
        }
    }

    private void READ_STMT() throws IOException {
        switch (t.tag) {
            case Tag.READ:
                eat(Tag.READ);
                eat(Tag.APA);
                if (words.get(t) == null) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Variavel nao declarada");
                    exit(0);
                }
                eat(Tag.ID);
                eat(Tag.FPA);
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava 'read', recebeu: " + t.toString());
                exit(0);
        }
    }

    private void WRITE_STMT() throws IOException {
        switch (t.tag) {
            case Tag.WRI:
                eat(Tag.WRI);
                eat(Tag.APA);
                WRITABLE();
                eat(Tag.FPA);
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava 'write', recebeu: " + t.toString());
                exit(0);
        }
    }

    private void WRITABLE() throws IOException {
        switch (t.tag) {
            case Tag.SUB:
            case Tag.ID:
                if (words.get(t) == null) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Variavel nao declarada");
                    exit(0);
                }
            case Tag.NUM:
            case Tag.LIT:
            case Tag.REAL:
            case Tag.APA:
            case Tag.NOT:
                SIMPLE_EXPR();
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava '-', id, const, '(' ou '!', recebeu: " + t.toString());
                exit(0);
        }
    }

    private int EXPRESSION() throws IOException {
        int tipo = -1, tipoSimplEx, tipoExpr1;
        switch (t.tag) {
            case Tag.SUB:
                tipoSimplEx = SIMPLE_EXPR();
                tipoExpr1 = EXPRESSION1();
                if ((tipoExpr1 == tipoSimplEx) || (tipoExpr1 == Tipo.VAZIO && tipoSimplEx != Tipo.STRING)){
                    tipo = tipoSimplEx;
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
                break;
            case Tag.ID:
                if (words.get(t) == null) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Variavel nao declarada");
                    exit(0);
                }
            case Tag.NUM:
            case Tag.LIT:
            case Tag.REAL:
            case Tag.APA:
                tipoSimplEx = SIMPLE_EXPR();
                tipoExpr1 = EXPRESSION1();
                if ((tipoExpr1 == tipoSimplEx) || (tipoExpr1 == Tipo.VAZIO)){
                    tipo = tipoSimplEx;
                    break;
                } else {
                    if(tipoExpr1 != Tipo.VAZIO)
                        tipo = Tipo.BOOL;
                    else{
                        System.out.println("Erro semantico linha: " + Lexer.line);
                        System.out.println("Operacao invalida");
                        exit(0);
                    }
                }
                break;
            case Tag.NOT:
                tipoSimplEx = SIMPLE_EXPR();
                tipoExpr1 = EXPRESSION1();
                if ((tipoExpr1 == tipoSimplEx) || (tipoExpr1 == Tipo.VAZIO && tipoSimplEx == Tipo.BOOL)){
                    tipo = tipoSimplEx;
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava '-', id, const, '(' ou '!', recebeu: " + t.toString());
                exit(0);
        }
        return tipo;
    }

    private int EXPRESSION1() throws IOException {
        int tipo = -1;
        switch (t.tag) {
            case Tag.FPA:
                tipo = Tipo.VAZIO;
                break;
            case Tag.MAI:
            case Tag.MEN:
            case Tag.GE:
            case Tag.LE:
                RELOP();
                tipo = SIMPLE_EXPR();
                if (tipo == Tipo.STRING) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
                tipo = Tipo.BOOL;
                break;
            case Tag.NE:
            case Tag.EQ:
                RELOP();
                SIMPLE_EXPR();
                tipo = Tipo.BOOL;
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava ')', '<', '<=', '>', '>=', '==' ou '!=', recebeu: " + t.toString());
                exit(0);
        }
        return tipo;
    }

    private int SIMPLE_EXPR() throws IOException {
        int tipo = -1, tipoTerm, tipoSimplEx1;
        switch (t.tag) {
            case Tag.SUB:
            case Tag.ID:
                if (words.get(t) == null) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Variavel nao declarada");
                    exit(0);
                }
            case Tag.NUM:
            case Tag.APA:
                tipoTerm = TERM();
                tipoSimplEx1 = SIMPLE_EXPR1();
                //System.out.println(tipoTerm+" e igual a "+tipoSimplEx1);
                if ((tipoTerm == tipoSimplEx1 || tipoSimplEx1 == Tipo.VAZIO || tipoTerm == Tipo.FLOAT || tipoSimplEx1 == Tipo.FLOAT)) {
                    if (tipoTerm == Tipo.FLOAT || tipoSimplEx1 == Tipo.FLOAT) {
                        tipo = Tipo.FLOAT;
                    } else {
                        tipo = tipoTerm;
                    }
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
            case Tag.LIT:
                tipoTerm = TERM();
                tipoSimplEx1 = SIMPLE_EXPR1();
                if ((tipoTerm == tipoSimplEx1 || tipoSimplEx1 == Tipo.VAZIO || tipoTerm == Tipo.STRING)) {
                    if (tipoTerm == Tipo.STRING) {
                        tipo = Tipo.STRING;
                    } else {
                        tipo = tipoTerm;
                    }
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
            case Tag.REAL:
                tipoTerm = TERM();
                tipoSimplEx1 = SIMPLE_EXPR1();
                if ((tipoTerm == tipoSimplEx1 || tipoSimplEx1 == Tipo.VAZIO || tipoTerm == Tipo.FLOAT) && (tipoTerm != Tipo.STRING && tipoSimplEx1 != Tipo.STRING)) {
                    if (tipoTerm == Tipo.FLOAT) {
                        tipo = Tipo.FLOAT;
                    } else {
                        tipo = tipoTerm;
                    }
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
                break;
            case Tag.NOT:
                tipoTerm = TERM();
                tipoSimplEx1 = SIMPLE_EXPR1();
                if ((tipoTerm == tipoSimplEx1) || (tipoSimplEx1 == Tipo.VAZIO && tipoTerm == Tipo.BOOL)) {
                    tipo = Tipo.BOOL;
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava '-', id, const, '(' ou '!', recebeu: " + t.toString());
                exit(0);
        }
        return tipo;
    }

    private int SIMPLE_EXPR1() throws IOException {
        int tipo = -1, tipoTerm, tipoSimplEx1;
        switch (t.tag) {
            case Tag.PVI:
            case Tag.FPA:
            case Tag.MAI:
            case Tag.MEN:
            case Tag.EQ:
            case Tag.GE:
            case Tag.LE:
            case Tag.NE:
                tipo = Tipo.VAZIO;
                break;
            case Tag.SUB: //não pode string
                ADDOP();
                tipoTerm = TERM();
                tipoSimplEx1 = SIMPLE_EXPR1();
                if ((tipoTerm == tipoSimplEx1 || tipoSimplEx1 == Tipo.VAZIO || tipoTerm == Tipo.FLOAT) && (tipoTerm != Tipo.STRING && tipoSimplEx1 != Tipo.STRING)) {
                    if (tipoTerm == Tipo.FLOAT) {
                        tipo = Tipo.FLOAT;
                    } else {
                        tipo = tipoTerm;
                    }
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
            case Tag.ADD:
                ADDOP();
                tipoTerm = TERM();
                tipoSimplEx1 = SIMPLE_EXPR1();
                if ((tipoTerm == tipoSimplEx1 || tipoSimplEx1 == Tipo.VAZIO || tipoTerm == Tipo.FLOAT)) {
                    if (tipoTerm == Tipo.FLOAT) {
                        tipo = Tipo.FLOAT;
                    } else {
                        tipo = tipoTerm;
                    }
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
            case Tag.OR: //tem que ser bool 
                ADDOP();
                tipoTerm = TERM();
                tipoSimplEx1 = SIMPLE_EXPR1();
                if ((tipoTerm == tipoSimplEx1) || (tipoSimplEx1 == Tipo.VAZIO && tipoTerm == Tipo.BOOL)) {
                    tipo = Tipo.BOOL;
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava ')', '+', '-', '||', '<', '<=', '>', '>=', '==' ou '!=', recebeu: " + t.toString());
                exit(0);
        }
        return tipo;
    }

    private int TERM() throws IOException {
        int tipoTerm1, tipoFactor, tipo = -1;
        switch (t.tag) {
            case Tag.SUB:
            case Tag.ID:
                if (words.get(t) == null) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Variavel nao declarada");
                    exit(0);
                }
                tipoFactor = FACTOR_A();
                tipoTerm1 = TERM1();
                if (tipoTerm1 == tipoFactor || tipoTerm1 == Tipo.VAZIO || (tipoTerm1 == Tipo.FLOAT)) {
                    if(tipoTerm1 == Tipo.FLOAT)
                        tipo = Tipo.FLOAT;
                    else
                        tipo = tipoFactor;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
                break;
            case Tag.NUM:
                tipoFactor = FACTOR_A();
                tipoTerm1 = TERM1();
                if (tipoTerm1 == tipoFactor || (tipoTerm1 == Tipo.VAZIO || tipoTerm1 == Tipo.FLOAT)) {
                    if (tipoTerm1 == Tipo.FLOAT) {
                        tipo = Tipo.FLOAT;
                    } else {
                        tipo = tipoFactor;
                    }
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
                break;
            case Tag.LIT:
                tipoFactor = FACTOR_A();
                tipoTerm1 = TERM1();
                if (tipoTerm1 == tipoFactor || (tipoTerm1 == Tipo.VAZIO)) {
                    tipo = tipoFactor;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
                break;
            case Tag.REAL:
            case Tag.APA:
                tipoFactor = FACTOR_A();
                tipoTerm1 = TERM1();
                if (tipoTerm1 == tipoTerm1 || tipoTerm1 == Tipo.VAZIO) {
                    tipo = tipoFactor;
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
            case Tag.NOT:
                tipoFactor = FACTOR_A();
                tipoTerm1 = TERM1();
                if ((tipoTerm1 == tipoFactor) || (tipoTerm1 == Tipo.VAZIO && tipoFactor == Tipo.BOOL)) {
                    tipo = Tipo.BOOL;
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava '-', id, const, '(' ou '!', recebeu: " + t.toString());
                exit(0);
        }
        return tipo;
    }

    private int TERM1() throws IOException {
        int tipoTerm1, tipoFactor, tipo = -1;
        switch (t.tag) {
            case Tag.MAI:
            case Tag.MEN:
            case Tag.EQ:
            case Tag.GE:
            case Tag.LE:
            case Tag.NE:
            case Tag.PVI:
            case Tag.FPA:
            case Tag.SUB:
            case Tag.ADD:
            case Tag.OR:
                tipo = Tipo.VAZIO;
                break;
            case Tag.DIV:
                MULOP();
                tipoFactor = FACTOR_A();
                tipoTerm1 = TERM1();
                if ((tipoFactor == Tipo.STRING) || (tipoTerm1 == Tipo.STRING)) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
                tipo = Tipo.FLOAT; //Se existe uma operação de divisão, passo a aceitar qualquer tipo na operação, menos string
                break;
            case Tag.AND:
                MULOP();
                tipoFactor = FACTOR_A();
                tipoTerm1 = TERM1();
                if ((tipoTerm1 == tipoFactor) || (tipoFactor != Tipo.STRING && tipoTerm1 == Tipo.VAZIO)) {
                    tipo = Tipo.BOOL;
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
            case Tag.MUL:
                MULOP();
                tipoFactor = FACTOR_A();
                tipoTerm1 = TERM1();
                //System.out.println(tipoFactor+" e igua a "+tipoTerm1);
                if ((tipoTerm1 == tipoFactor) || (tipoFactor != Tipo.STRING && tipoTerm1 == Tipo.VAZIO) || tipoTerm1 == Tipo.FLOAT) {
                    if(tipoTerm1 == Tipo.FLOAT)
                        tipo = Tipo.FLOAT;
                    else
                        tipo = tipoFactor;
                    break;
                } else {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida");
                    exit(0);
                }
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava '<', '<=', '>', '>=', '==', '!=', '/', '*', '&&', '+', '-' ou '||', recebeu: " + t.toString());
                exit(0);
        }
        return tipo;
    }

    private int FACTOR_A() throws IOException {
        int tipo = -1;
        int tipoID;
        switch (t.tag) {
            case Tag.SUB:
                eat(Tag.SUB);
                tipo = FACTOR();
                if (tipo == Tipo.STRING) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida, operador '-' incompativel");
                    exit(0);
                }
                break;
            case Tag.NOT:
                eat(Tag.NOT);
                tipo = FACTOR();
                if (tipo != Tipo.BOOL) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida, operador '!' incopativel");
                    exit(0);
                }
                break;
            case Tag.ID:
                if (words.get(t) == null) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Variavel nao declarada");
                    exit(0);
                }
                tipoID = words.getTipo(t);
                tipo = FACTOR();
                if (tipo != tipoID) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida, tipos diferentes");
                    exit(0);
                }
                break;
            case Tag.NUM:
                tipo = FACTOR();
                if (tipo != Tipo.INT) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida, esperava inteiro");
                    exit(0);
                }
                break;
            case Tag.LIT:
                tipo = FACTOR();
                if (tipo != Tipo.STRING) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida, esperava string");
                    exit(0);
                }
                break;
            case Tag.REAL:
                tipo = FACTOR();
                if (tipo != Tipo.FLOAT) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Operacao invalida, esperava float");
                    exit(0);
                }
                break;
            case Tag.APA:
                tipo = FACTOR();
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava '-', id, const, '(' ou '!', recebeu: " + t.toString());
                exit(0);
        }
        return tipo;
    }

    private int FACTOR() throws IOException {
        int tipo = -1;
        switch (t.tag) {
            case Tag.ID:
                if (words.get(t) == null) {
                    System.out.println("Erro semantico linha: " + Lexer.line);
                    System.out.println("Variavel nao declarada");
                    exit(0);
                }
                tipo = words.getTipo(t);
                eat(Tag.ID);
                break;
            case Tag.NUM:
                tipo = Tipo.INT;
                eat(Tag.NUM);
                break;
            case Tag.LIT:
                tipo = Tipo.STRING;
                eat(Tag.LIT);
                break;
            case Tag.REAL:
                tipo = Tipo.FLOAT;
                eat(Tag.REAL);
                break;
            case Tag.APA:
                eat(Tag.APA);
                tipo = EXPRESSION(); //tem que me retornar um tipo
                eat(Tag.FPA);
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava id, const ou '(', recebeu: " + t.toString());
                exit(0);
        }
        return tipo;
    }

    private void RELOP() throws IOException {
        switch (t.tag) {
            case Tag.MAI:
                eat(Tag.MAI);
                break;
            case Tag.MEN:
                eat(Tag.MEN);
                break;
            case Tag.EQ:
                eat(Tag.EQ);
                break;
            case Tag.GE:
                eat(Tag.GE);
                break;
            case Tag.LE:
                eat(Tag.LE);
                break;
            case Tag.NE:
                eat(Tag.NE);
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava '<', '<=', '>', '>=', '==' ou '!=', recebeu: " + t.toString());
                exit(0);
        }
    }

    private void ADDOP() throws IOException {
        switch (t.tag) {
            case Tag.SUB:
                eat(Tag.SUB);
                break;
            case Tag.ADD:
                eat(Tag.ADD);
                break;
            case Tag.OR:
                eat(Tag.OR);
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava  '+', '-' ou '||', recebeu: " + t.toString());
                exit(0);
        }
    }

    private void MULOP() throws IOException {
        switch (t.tag) {
            case Tag.DIV:
                eat(Tag.DIV);
                break;
            case Tag.AND:
                eat(Tag.AND);
                break;
            case Tag.MUL:
                eat(Tag.MUL);
                break;
            default:
                System.out.println("Erro sintatico linha: " + Lexer.line);
                System.out.println("Esperava '/', '*' ou '&&', recebeu: " + t.toString());
                exit(0);
        }
    }
}
