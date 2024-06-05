package projeto.analisador;

public class Main {
    public static void main(String[] args) {
        String codigo = """
                inicio
                    inteiro:b;
                    inteiro:c;
                    imprima("digite um valor para c:");
                    leia(c);
                    para b=0 até c passo 2
                        imprima(b);
                    fim_para
                fim
                """;

        AnalisadorLexico lexico = new AnalisadorLexico(codigo);
        Token token;

        System.out.printf("%-10s %-15s %-10s %-20s\n", "Código", "Lexema", "Posição", "Descrição");
        System.out.println("-----------------------------------------------------------");

        do {
            token = lexico.proximoToken();
            if (token != null) {
                String descricao = Descricao.getDescricao(token);
                System.out.printf("%-10d %-15s %-10d %-20s\n", token.getCodigo(), token.getLexema(), token.getPosicao(), descricao);
            }
        } while (token == null || token.getCodigo() != 99);
    }
}
