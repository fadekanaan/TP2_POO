package trabalho.tp2_poo;

/**
 *
 * @author Fade Hassan Husein Kanaan
 * @author Rodrigo Thoma da Silva
 */
public class Jogo {

    public static final int TOPO = 0;
    public static final int ESQ_SUP = 1;
    public static final int DIR_SUP = 2;
    public static final int ESQ_INF = 3;
    public static final int DIR_INF = 4;
    public static final int CENTRO = 5;
    
    private int posicaoCabrito;
    private int posicaoCarcara;
    private boolean turnoCabrito; // true = vez do Cabrito, false = vez do Carcará
    private boolean superPuloDisponivel; // O cabrito tem 1 super pulo
    private boolean jogoAcabou;
    private int contadorJogadas;
    
    private final int[][] adjacencias = {
        {1, 2, 5},    // 0 (TOPO) conecta com: 1, 2, 5
        {0, 3},       // 1 (ESQ_SUP) conecta com: 0, 3
        {0, 4},       // 2 (DIR_SUP) conecta com: 0, 4
        {1, 4, 5},    // 3 (ESQ_INF) conecta com: 1, 4, 5
        {2, 3, 5},    // 4 (DIR_INF) conecta com: 2, 3, 5
        {0, 3, 4}     // 5 (CENTRO) conecta com: 0, 3, 4
    };
    
    public Jogo() {
        // Cabrito começa, Posições iniciais fixas, Super Pulo ativo.
        this.posicaoCabrito = TOPO;
        this.posicaoCarcara = ESQ_SUP; // Conforme imagem 1 do PDF
        this.turnoCabrito = true; 
        this.superPuloDisponivel = true;
        this.jogoAcabou = false;
        this.contadorJogadas = 0;
    }
    
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
    
    public String getVencedor() { 
        if (!jogoAcabou) {
            return "Jogo em andamento";
        }
        return turnoCabrito ? "Carcará (Capturou!)" : "Cabrito (Sobreviveu)"; 
    }
    
    public void validarMovimento(int origem, int destino) throws MovimentoInvalidoException {
        // --- Regras Gerais ---
        if (jogoAcabou) {
            throw new MovimentoInvalidoException("O jogo acabou!");
        }
        
        // Verifica se a origem bate com a peça da vez
        if (turnoCabrito && origem != posicaoCabrito) {
            throw new MovimentoInvalidoException("É a vez do Cabrito! Mova a peça certa.");
        }
        if (!turnoCabrito && origem != posicaoCarcara) {
            throw new MovimentoInvalidoException("É a vez do Carcará! Mova a peça certa.");
        }

        // --- Regras Específicas do CABRITO ---
        if (turnoCabrito) {
            // Cabrito NÃO pode ir para onde o Carcará está (suicídio não permitido)
            if (destino == posicaoCarcara) {
                throw new MovimentoInvalidoException("Você não pode ir para onde o Carcará está!");
            }
            
            // Verifica conexões
            if (!saoVizinhos(origem, destino)) {
                // Se não é vizinho, só pode se for Super Pulo
                if (!superPuloDisponivel) {
                    throw new MovimentoInvalidoException("Movimento inválido! Casas não conectadas.");
                }
                // Se chegou aqui, é um Super Pulo válido (destino vazio e tem poder)
            }
        } 
        
        // --- Regras Específicas do CARCARÁ ---
        else {
            // Carcará DEVE ser vizinho (não tem super pulo)
            if (!saoVizinhos(origem, destino)) {
                throw new MovimentoInvalidoException("O Carcará só voa para casas conectadas!");
            }
            // Nota: Carcará PODE ir para o destino == posicaoCabrito (Isso é a captura)
        }
    }
    
    // Verifica se dois pontos são conectados por uma linha direta
    private boolean saoVizinhos(int origem, int destino) {
        for (int vizinho : adjacencias[origem]) {
            if (vizinho == destino) {
                return true;
            }
        }
        return false;
    }
    
    public void realizarJogada(int origem, int destino) throws MovimentoInvalidoException {
        // 1. Primeiro, garantimos que o movimento é legal
        validarMovimento(origem, destino);

        // 2. Lógica se for a vez do CABRITO
        if (turnoCabrito) {
            // Verifica se foi um movimento normal ou Super Pulo
            boolean ehVizinho = saoVizinhos(origem, destino);
            
            if (!ehVizinho) {
                // Se não é vizinho e passou da validação, é Super Pulo!
                // Então descontamos o poder especial.
                superPuloDisponivel = false;
            }
            
            // Move o cabrito
            posicaoCabrito = destino;
        } 
        
        // 3. Lógica se for a vez do CARCARÁ
        else {
            // Se o Carcará moveu para onde o Cabrito está -> CAPTURA!
            if (destino == posicaoCabrito) {
                jogoAcabou = true;
            }
            
            // Move o carcará
            posicaoCarcara = destino;
        }

        // 4. Finalização do Turno
        contadorJogadas++; // Conta a jogada
        
        if (!jogoAcabou) {
            turnoCabrito = !turnoCabrito; // Troca a vez (Inverte o booleano)
        }
    }
    
    
    
    
    
}
