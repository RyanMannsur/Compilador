package compilador;

public class Tag {

    public final static int //Palavras reservadas
            CLAS = 256, //class
            IF = 257, //if
            ELS = 258, //else
            DO = 259, //do
            INT = 260, //int
            FLOAT = 261, //float
            STR = 262, //string
            READ = 263, //read
            WRI = 264, //write
            WHI = 265, //while
            //Operadores e pontuação
            EQ = 294, //==
            GE = 293, //>=
            LE = 292, //<=
            NE = 291, //!=
            AND = 290, //&&
            OR = 289, //||
            ADD = 288, //+
            SUB = 287, //-
            MUL = 286, //*
            DIV = 285, ///
            NOT = 284, //!
            ACH = 283, //{
            FCH = 282, //}
            APA = 281, //(
            FPA = 280, //)
            VIR = 279, //,
            PVI = 278, //;
            ATR = 277, //=
            MEN = 276, //<
            MAI = 275, //>
            //Outros tokens
            NUM = 271, //numeros
            REAL = 272, //float
            ID = 273, //id 
            LIT = 274; //literal, definido por "
}
