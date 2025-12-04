package trabalho.tp2_poo;

/*
 * Exceção personalizada para tratar violações das regras do jogo.
 * @author Fade Hassan Husein Kanaan
 * @author Rodrigo Thoma da Silva
 */
public class MovimentoInvalidoException extends Exception {

    public MovimentoInvalidoException(String mensagem) {
        super(mensagem);
    }

}
