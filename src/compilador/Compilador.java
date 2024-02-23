
package compilador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import symbols.Tab;


public class Compilador {


    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome do Arquivo: ");
        String file = scanner.nextLine();
        scanner.close();
        Tab words = new Tab(null);
        Lexer lexer = new Lexer(file, words);
        Parser parser = new Parser(lexer, words);
        //lexer.printLexer();
        parser.SINTATICO();
        //words.printTabela();
    }
    
}
