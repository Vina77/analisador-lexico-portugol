package dev.ceub.analisador.lexico.portugol;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Nenhum arquivo foi informado");
            return;
        }

        String nomeArquivo = args[0];
        String codigo;
        try {
            codigo = Files.readString(Paths.get(nomeArquivo), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            return;
        }

        // Fim da linha do código
        codigo = codigo + "\0";
        AnalisadorLexico lexico = new AnalisadorLexico(codigo);
        Token token;

        System.out.printf("%-10s %-25s %-10s\n", "Código", "Lexema", "Posição");
        System.out.println("----------------------------------------");

        do {
            token = lexico.proximoToken();
            if (token != null) {
                System.out.printf("%-10d %-15s %-10d\n", token.getCodigo(), token.getLexema(), token.getPosicao());
            }
        } while (token == null || token.getCodigo() != 99);
    }
}
