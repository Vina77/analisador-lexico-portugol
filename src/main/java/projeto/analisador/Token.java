package projeto.analisador;

public class Token {
    private int codigo;
    private String lexema;
    private int posicao;

    public Token(int codigo, String lexema, int posicao) {
        this.codigo = codigo;
        this.lexema = lexema;
        this.posicao = posicao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getPosicao() {
        return posicao;
    }
}
