package compilador;

import java.io.*;
import symbols.*;

public class Lexer {

    private boolean decimal = false;
    public static int line = 1; //contador de linhas
    private char ch = ' '; //caractere lido do arquivo
    private FileReader file;
    //private Tab words = new Tab(null);
    private Tab words = null;
    
    /* Método para inserir palavras reservadas na HashTable */
    private void reserve(Word w) {
        words.put(w, Tipo.RESERV);
    }

    /* Método construtor */
    public Lexer(String fileName, Tab words) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }
        this.words = words;
        //adicionar as palavras reservadas
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else", Tag.ELS));
        reserve(new Word("read", Tag.READ));
        reserve(new Word("write", Tag.WRI));
        reserve(new Word("while", Tag.WHI));
        reserve(new Word("int", Tag.INT));
        reserve(new Word("float", Tag.FLOAT));
        reserve(new Word("string", Tag.STR));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("class", Tag.CLAS));
    }

    /*Lê o próximo caractere do arquivo*/
    private void readch() throws IOException {
        ch = (char) file.read();
    }

    /* Lê o próximo caractere do arquivo e verifica se é igual a c*/
    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c) {
            return false;
        }
        ch = ' ';
        return true;
    }
    
    private Token comparar(String s){
        if(s.equals("class")){
            return Word.Class;
        } else
            if(s.equals("if")){
                return Word.If;
            }else
            if(s.equals("else")){
                return Word.Else;
            }else
            if(s.equals("do")){
                return Word.Do;
            }else
            if(s.equals("int")){
                return Word.Int;
            }else
            if(s.equals("float")){
                return Word.Float;
            }else
            if(s.equals("string")){
                return Word.String;
            }else
            if(s.equals("read")){
                return Word.Read;
            }else
            if(s.equals("write")){
                return Word.Write;
            }else
            if(s.equals("while")){
                return Word.While;
            }else
                return null;
    }

    public Token scan() throws IOException {
        //Desconsidera delimitadores na entrada
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') {
                continue;
            } else if (ch == '\n') {
                line++; //conta linhas
            } else {
                break;
            }
        }
        if(ch == '/'){
            readch();
            if(ch == '*'){
                boolean i = false;
                for(;;readch()){
                    if(ch == '\n')
                        line++;
                    if(ch == -1 || ch == 65535){
                        break;
                    }else if(ch == '*'){
                        i = true;
                        continue;
                        }else if(ch == '/' && i){
                            break;
                    }
                    i = false;
                }
            }else{ 
                if(ch == '/'){  
                    for(;;readch()){
                        if(ch == '\n'){
                            //line++;
                            break;
                        }else if(ch == -1 || ch == 65535){
                            break;
                        }
                    }
                }
                else{
                    return Word.div;
                }
            }
        }
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b' || ch == '/') {
                continue;
            } else if (ch == '\n') {
                line++; //conta linhas
            } else {
                break;
            }
        }
        switch (ch) {
            //Operadores
            case '&':
                if (readch('&')) {
                    return Word.and;
                } else {
                    return new Token('&');
                }
            case '|':
                if (readch('|')) {
                    return Word.or;
                } else {
                    return new Token('|');
                }
            case '=':
                if (readch('=')) {
                    return Word.eq;
                } else {
                    return Word.atr;
                }
            case '<':
                if (readch('=')) {
                    return Word.le;
                } else {
                    return Word.men;
                }
            case '>':
                if (readch('=')) {
                    return Word.ge;
                } else {
                    return Word.mai;
                }
            case '+':
                if(readch((char) -1)){
                    return Word.add;
                }else
                return Word.add;
            case '-':
                if(readch((char) -1)){
                    return Word.sub;
                }else
                return Word.sub;
            case '*':
                if(readch((char) -1)){
                    return Word.mul;
                }else
                return Word.mul;
            case '/':
                if(readch((char) -1)){
                    return Word.div;
                }else
                return Word.div;
            case '!':
                if(readch((char) -1)){
                    return Word.not;
                }else
                return Word.not;
            case '{':
                if(readch((char) -1)){
                    return Word.ach;
                }else
                return Word.ach;
            case '}':
                if(readch((char) -1)){
                    return Word.fch;
                }else
                    return Word.fch;
            case '(':
                if(readch((char) -1)){
                    return Word.apa;
                }else
                return Word.apa;
            case ')':
                if(readch((char) -1)){
                    return Word.fpa;
                }else
                return Word.fpa;
            case ',':
                if(readch((char) -1)){
                    return Word.vir;
                }else
                return Word.vir;
            case ';':
                if(readch((char) -1)){
                    return Word.pvi;
                }else
                return Word.pvi;
        }
        //Números
        if (Character.isDigit(ch)) {
            String value = "";
            do {
                value = value + ch;
                if (value.equals('.')) {
                    decimal = true;
                }
                readch();
            } while (Character.isDigit(ch) || (ch == '.' && (!decimal)));
            if (ch == -1) {
                return null;
            }
            if (decimal) {
                System.out.println(value);
                return new NumReal(Double.parseDouble(value));
            } else {
                decimal = false;
                return new Num(Integer.parseInt(value));
            }
        }
        //Identificadores
        if (Character.isLetter(ch)) {
            StringBuffer sb = new StringBuffer();
            do {
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch) || ch == '_');
            String s = sb.toString();
            Token t = comparar(s);
            Word w = new Word(s, Tag.ID);
            if(t == null){ //palavra não é reservada
                //w = (Word) words.get(w);
                //if (w == null) { //palavra não exites
                    //w = new Word(s, Tag.ID);
                    //words.put(w);
                    //return w;
                //}else
                return w; //palavra já existe na HashTable
            }
            return t; //palavra reservada
            //return w; //retorna a palavra lida, reservada ou não
        }

        if (ch == '"') { //Literal
            readch(); //pulo as "
            StringBuffer sb = new StringBuffer();
            do {
                sb.append(ch);
                readch();
            } while (ch != '\n' && ch != '"');
            if (ch == '"') {
                readch(); // pulo as "
            }else if(ch == '\n'){
                    String s = sb.toString();
                    Token t = new Token(0); //tag 0 será de um erro
                    return t;
                }
            String s = sb.toString();
            Word w = new Word(s, Tag.LIT);
            return w;
        }

        if (ch == -1 || ch == 65535) {
            return new Token(ch);
        }
        //Caracteres não especificados
        Token t = new Token(ch);
        ch = ' ';
        return t;
    }

    public void printLexer() throws IOException { //Imprime a analise sintatica
        while (ch != -1) {
            Token t = scan();
            if (ch == 65535) {
                break;
            }
            System.out.println("Lexema: "+t.toString()+" - Tag: "+t.tag+ " Linha: "+line);
        }
        System.out.println();
        //words.printTabela();
    }
    
    public Token nextToken() throws IOException{ //Envia o proximo token
        Token t = null;
        if (ch != -1 || ch != 65535) {
            t = scan();
        }
        return t;
    }
}