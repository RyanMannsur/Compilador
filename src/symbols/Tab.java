package symbols;

import compilador.*;
import java.util.*;

/* Informações sobre a semantica:

 *** O operador "+", quando aplicado a dado do tipo string, representa
concatenação.
*** Os demais operadores aritméticos são aplicáveis somente aos tipos numéricos.
*** O resultado da divisão entre dois números inteiros é um número real.
*** Somente tipos iguais são compatíveis nesta linguagem.
*** As operações de comparação resultam em valor lógico (verdadeiro ou falso)
*** Nos testes (dos comandos condicionais e de repetição) a expressão a ser
validada deve ser um valor lógico.
*** A semântica dos demais comandos e expressões é a tradicional de linguagens
como Java e C.
*/

public class Tab {

    private Hashtable table; //tabela de símbolos do ambiente
    protected Tab prev; //ambiente imediatamente superior

    public Tab(Tab n) {
        table = new Hashtable(); //cria a TS para o ambiente
        prev = n; //associa o ambiente atual ao anterior
    }

    public void put(Token w) {
        table.put(w, w.tag);
    }
    
    public void put(Token w, int tipo) {
        table.put(w, tipo);
    }

    public Token get(Token w) {
        Enumeration keys = this.table.keys();
        for (Tab e = this; e != null; e = e.prev) {
            while (keys.hasMoreElements()) {
                Token t = (Token) keys.nextElement();
                //System.out.println(t.toString()+" e igual? "+w.toString());
                if(t.toString().equals(w.toString())){
                    return t;
                }
            }
        }
        return null;
    }
    
    public int getTipo(Token w) {
        Enumeration keys = this.table.keys();
        for (Tab e = this; e != null; e = e.prev) {
            while (keys.hasMoreElements()) {
                Token t = (Token) keys.nextElement();
                //System.out.println(t.toString()+" e igual? "+w.toString());
                if(t.toString().equals(w.toString())){
                    return (Integer)this.table.get(t);
                }
            }
        }
        return -1;
    }
    
    public void printTabela() {
        System.out.println("TABELA DE SIMBOLOS");
        Enumeration keys = this.table.keys();
        for (Tab e = this; e != null; e = e.prev) {
            while (keys.hasMoreElements()) {
                Token w = (Token) keys.nextElement();
                Integer t =  (Integer) this.table.get(w);
                String tipo = "";
                switch(t){
                    case Tipo.INT: tipo = "int"; break;
                    case Tipo.STRING: tipo = "string"; break;
                    case Tipo.FLOAT: tipo =  "float"; break;
                    case Tipo.BOOL: tipo =  "bool"; break;
                    case Tipo.RESERV: tipo =  "reservada"; break;
                    case Tipo.CLASS: tipo =  "classe"; break;
                }
                System.out.println("Chave: " + w.toString() + ", Tipo: " + tipo);
            }
        }
    }
    
        /*Este método retorna as informações (Id) referentes a determinado Token */
 /*O Token é pesquisado do ambiente atual para os anteriores */
    /*public Token get(Token w) {
        for (Tab e = this; e != null; e = e.prev) {
            Token found = (Token) e.table.get(w);
            if (found != null) //se Token existir em uma das TS
            {
                return found;
            }
        }
        return null; //caso Token não exista em uma das TS
    }*/
}
