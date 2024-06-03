package dev.ceub.analisador.lexico.portugol;

import java.util.*;

public class AnalisadorLexico {

    // Definição de tokens
    private static final int T_INICIO = 1;
    private static final int T_FIM = 2;
    private static final int T_INTEIRO = 3;
    private static final int T_IMPRIMA = 4;
    private static final int T_LEIA = 5;
    private static final int T_SE = 6;
    private static final int T_ENTAO = 7;
    private static final int T_SENAO = 8;
    private static final int T_FIM_SE = 9;
    private static final int T_PARA = 10;
    private static final int T_ATE = 11;
    private static final int T_PASSO = 12;
    private static final int T_FIM_PARA = 13;
    private static final int T_IDENTIFICADOR = 14;
    private static final int T_NUMERO = 15;
    private static final int T_STRING = 16;
    private static final int T_OPERADOR = 17;
    private static final int T_ATRIBUICAO = 18;
    private static final int T_DEFINICAO = 19;
    private static final int T_DELIMITADOR = 20;
    private static final int T_FIM_ARQUIVO = 99;

    // Tabela de palavras reservadas
    static final Map<String, Integer> palavrasReservadas = new HashMap<>();
    static {
        palavrasReservadas.put("inicio", T_INICIO);
        palavrasReservadas.put("fim_se", T_FIM_SE);
        palavrasReservadas.put("fim_para", T_FIM_PARA);
        palavrasReservadas.put("fim", T_FIM);
        palavrasReservadas.put("inteiro", T_INTEIRO);
        palavrasReservadas.put("imprima", T_IMPRIMA);
        palavrasReservadas.put("leia", T_LEIA);
        palavrasReservadas.put("se", T_SE);
        palavrasReservadas.put("então", T_ENTAO);
        palavrasReservadas.put("senão", T_SENAO);
        palavrasReservadas.put("para", T_PARA);
        palavrasReservadas.put("até", T_ATE);
        palavrasReservadas.put("passo", T_PASSO);
    }

    // Tabela de símbolos
    private static final Map<String, Integer> tabelaSimbolos = new HashMap<>();
    private static int proximaPosicao = 0;

    // Código fonte
    private static String codigoFonte;
    private static int posicaoAtual;

    public AnalisadorLexico(String codigo) {
        codigoFonte = codigo;
        posicaoAtual = 0;
    }

    private char proxChar() {
        if (posicaoAtual < codigoFonte.length()) {
            return codigoFonte.charAt(posicaoAtual++);
        }
        return '\0'; // EOF
    }

    private void retroceder() {
        if (posicaoAtual > 0) posicaoAtual--;
    }

    private boolean isLetra(char c) {
        return Character.isLetter(c);
    }

    private boolean isDigito(char c) {
        return Character.isDigit(c);
    }

    private boolean isEspaco(char c) {
        return Character.isWhitespace(c);
    }

    private boolean isIdentificadorChar(char c) {
        return isLetra(c) || isDigito(c) || c == '_';
    }

    private int buscarPalavraReservada(String palavra) {
        return palavrasReservadas.getOrDefault(palavra, -1);
    }

    public Token proximoToken() {
        StringBuilder lexema = new StringBuilder();
        char c;

        // Ignorar espaços em branco
        do {
            c = proxChar();
        } while (isEspaco(c));

        // Fim do arquivo
        if (c == '\0') {
            return new Token(T_FIM_ARQUIVO, "EOF", -1);
        }

        // Strings
        if (c == '\"') {
            c = proxChar();
            while (c != '\"') {
                lexema.append(c);
                c = proxChar();
            }
            return new Token(T_STRING, lexema.toString(), -1);
        }

        // Identificadores e palavras reservadas
        if (isLetra(c)) {
            while (isIdentificadorChar(c)) {
                lexema.append(c);
                c = proxChar();
            }
            retroceder();
            String palavra = lexema.toString();
            int codigo = buscarPalavraReservada(palavra);
            if (codigo == -1) {
                if (!tabelaSimbolos.containsKey(palavra)) {
                    tabelaSimbolos.put(palavra, proximaPosicao++);
                }
                return new Token(T_IDENTIFICADOR, palavra, tabelaSimbolos.get(palavra));
            }
            return new Token(codigo, palavra, -1);
        }

        // Números
        if (isDigito(c)) {
            while (isDigito(c)) {
                lexema.append(c);
                c = proxChar();
            }
            retroceder();
            return new Token(T_NUMERO, lexema.toString(), -1);
        }

        // Operadores, definição e atribuição
        if ("+-/*()<>:;=".indexOf(c) != -1) {
            lexema.append(c);
            if (c == '<' && proxChar() == '-') {
                lexema.append('-');
                return new Token(T_ATRIBUICAO, lexema.toString(), -1);
            } else if (c == '<' || c == '>') {
                c = proxChar();
                if (c == '=') {
                    lexema.append(c);
                } else {
                    retroceder();
                }
            } else if (c == ':') {
                return new Token(T_DEFINICAO, lexema.toString(), -1);
            } else if (c == ';') {
                return new Token(T_DELIMITADOR, lexema.toString(), -1);
            }
            return new Token(T_OPERADOR, lexema.toString(), -1);
        }

        // Caractere desconhecido
        return null;
    }
}
