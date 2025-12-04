package trabalho.tp2_poo;

/**
 * Classe responsável pela lógica principal do jogo "Corre Cabrito". Representa
 * o estado do tabuleiro, controla os turnos e valida as regras de movimentação.
 *
 * @author Fade Hassan Husein Kanaan
 * @author Rodrigo Thoma da Silva
 */
public class Jogo {

    // Constantes para mapear visualmente os índices do array às posições no pentágono
    public static final int TOPO = 0;
    public static final int ESQ_SUP = 1;
    public static final int DIR_SUP = 2;
    public static final int ESQ_INF = 3;
    public static final int DIR_INF = 4;
    public static final int CENTRO = 5;

    private int posicaoCabrito;
    private int posicaoCarcara;
    private boolean turnoCabrito; // true = vez do Cabrito, false = vez do Carcará
    private boolean superPuloDisponivel; // O cabrito tem só 1 super pulo no jogo inteirio
    private boolean jogoAcabou;
    private int contadorJogadas;

    /**
     * Matriz de Adjacência. O índice da linha representa a posição de origem.
     * Os valores internos representam as posições de destino possíveis
     * (vizinhos conectados). Exemplo: adjacencias[0] = {1, 2, 5} significa que
     * do TOPO (0) pode-se ir para 1, 2 ou 5.
     */
    private final int[][] adjacencias = {
        {1, 2, 5}, // 0 (TOPO) conecta com: 1, 2, 5
        {0, 3}, // 1 (ESQ_SUP) conecta com: 0, 3
        {0, 4}, // 2 (DIR_SUP) conecta com: 0, 4
        {1, 4, 5}, // 3 (ESQ_INF) conecta com: 1, 4, 5
        {2, 3, 5}, // 4 (DIR_INF) conecta com: 2, 3, 5
        {0, 3, 4} // 5 (CENTRO) conecta com: 0, 3, 4
    };

    /**
     * Construtor: Inicializa o jogo com as configurações padrão descritas no
     * enunciado.
     */
    public Jogo() {
        this.posicaoCabrito = TOPO; // Cabrito começa no topo
        this.posicaoCarcara = ESQ_SUP; // Carcará começa na esquerda superior
        this.turnoCabrito = true;  // Cabrito sempre começa jogando
        this.superPuloDisponivel = true;
        this.jogoAcabou = false;
        this.contadorJogadas = 0;
    }

    // Getters para consultar o estado
    public int getPosicaoCabrito() {
        return posicaoCabrito;
    }

    public int getPosicaoCarcara() {
        return posicaoCarcara;
    }

    public boolean isVezDoCabrito() {
        return turnoCabrito;
    }

    public boolean isJogoAcabou() {
        return jogoAcabou;
    }

    /**
     * Retorna uma mensagem formatada com o resultado final do jogo. Exibe quem
     * venceu e o total de jogadas realizadas.
     */
    public String getVencedor() {
        if (!jogoAcabou) {
            return "Jogo em andamento";
        }
        // Se o turno era do Cabrito e o jogo acabou, significa que o Carcará jogou antes e capturou.
        // A lógica de vitória do Cabrito é "sobreviver", então tecnicamente o jogo só acaba com vitória do Carcará
        String resultado = !turnoCabrito ? "Carcará capturou o coitado!" : "Cabrito nunca sobreviveu...";

        return resultado + "\nTotal de jogadas: " + contadorJogadas;
    }

    /* Valida se um movimento é permitido pelas regras do jogo, lançando exceção se não for.
     * Não altera o estado do jogo, apenas verifica.
     */
    public void validarMovimento(int origem, int destino) throws MovimentoInvalidoException {
        if (jogoAcabou) {
            throw new MovimentoInvalidoException("O jogo acabou!");
        }

        if (origem == destino) {
            throw new MovimentoInvalidoException("Mova para uma casa diferente!!!");
        }

        // Verifica se o jogador está tentando mover a peça que lhe pertence no turno atual
        if (turnoCabrito && origem != posicaoCabrito) {
            throw new MovimentoInvalidoException("É a vez do Cabrito! Mova a peça certa.");
        }
        if (!turnoCabrito && origem != posicaoCarcara) {
            throw new MovimentoInvalidoException("É a vez do Carcará! Mova a peça certa.");
        }

        // Regras Específicas do CABRITO 
        if (turnoCabrito) {
            // Cabrito nao pode ir pra onde o Carcará está
            if (destino == posicaoCarcara) {
                throw new MovimentoInvalidoException("Você não pode se matar :)");
            }
            // Verifica se as casas são conectadas por uma linha (adjacentes)
            if (!saoVizinhos(origem, destino)) {
                // Se não são vizinhas, a única chance é usar o Super Pulo
                if (!superPuloDisponivel) {
                    throw new MovimentoInvalidoException("Movimento inválido! Apenas casas vizinhas!");
                }
                // Se chegou aqui e tem super pulo, o movimento é válido (será descontado no realizarJogada)
            }
        } // Regras Específicas do CARCARÁ
        else {
            // Carcará não tem super pulo, então deve mover apenas para vizinhos
            if (!saoVizinhos(origem, destino)) {
                throw new MovimentoInvalidoException("O Carcará só voa para casas vizinhas!");
            }
            // destino == posicaoCabrito (isso é a captura)
        }
    }

    // Verifica se dois pontos são conectados por uma linha direta
    public boolean saoVizinhos(int origem, int destino) {
        for (int vizinho : adjacencias[origem]) {
            if (vizinho == destino) {
                return true;
            }
        }
        return false;
    }

    public boolean isSuperPuloDisponivel() {
        return superPuloDisponivel;
    }

    /**
     * Executa a jogada, atualizando as posições e trocando o turno. Este método
     * deve ser chamado apenas após validarMovimento().
     */
    public void realizarJogada(int origem, int destino) throws MovimentoInvalidoException {
        // 1. Garante que as regras são respeitadas antes de mover
        validarMovimento(origem, destino);

        if (turnoCabrito) {
            boolean ehVizinho = saoVizinhos(origem, destino);
            // Se moveu para longe (não vizinho), consome o poder especial
            if (!ehVizinho) {

                superPuloDisponivel = false;
            }

            // Atualiza posição do Cabrito
            posicaoCabrito = destino;
        } else {
            // Se o Carcará moveu para onde o Cabrito está, jogo acaba!
            if (destino == posicaoCabrito) {
                jogoAcabou = true;
            }

            // Atualiza posição do Carcará
            posicaoCarcara = destino;
        }

        contadorJogadas++; // Incrementa o contador total de jogadas

        if (!jogoAcabou) {
            turnoCabrito = !turnoCabrito; // Troca a vez (inverte o boolean)
        }
    }

    /**
     * Restaura o jogo para o estado inicial para permitir uma nova partida.
     */
    public void reiniciar() {
        this.posicaoCabrito = TOPO;
        this.posicaoCarcara = ESQ_SUP;
        this.turnoCabrito = true;
        this.superPuloDisponivel = true;
        this.jogoAcabou = false;
        this.contadorJogadas = 0;
    }
}
