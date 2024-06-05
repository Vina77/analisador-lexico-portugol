package projeto.analisador;

public class Descricao {

    // Método que retorna a descrição do lexema
    public static String getDescricao(Token token) {
        String lexema = token.getLexema();

        // Verifica se é um operador
        if ("+-*/".contains(lexema)) {
            return "Operador";
        }

        // Verifica se é numérico
        if (lexema.matches("\\d+(\\.\\d+)?")) {
            return "Numérico";
        }

        // Verifica se é um literal (string)
        if (token.getCodigo() == 16) {
            return "Literal";
        }

        // Verifica se é um delimitador
        if (".,;:".contains(lexema)) {
            return "Delimitador";
        }

        // Verifica se é uma palavra reservada
        if (AnalisadorLexico.palavrasReservadas.containsKey(lexema)) {
            return "Palavra Reservada";
        }

        // Se não for nenhum dos acima, assume que é uma variável
        return "Variável";
    }
}
