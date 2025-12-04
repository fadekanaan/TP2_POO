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
    private boolean superPuloDisponivel; // O cabrito tem só 1 super pulo no jogo inteirio
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
        // Cabrito começa, posicoes fixas
        this.posicaoCabrito = TOPO;
        this.posicaoCarcara = ESQ_SUP; 
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
    String resultado = !turnoCabrito ? "Carcará capturou o coitado!" : "Cabrito nunca sobreviveu...";
    
    return resultado + "\nTotal de jogadas: " + contadorJogadas;
    }
    
    public void validarMovimento(int origem, int destino) throws MovimentoInvalidoException {
        if (jogoAcabou) {
            throw new MovimentoInvalidoException("O jogo acabou!");
        }
      
        if (origem == destino) {
            throw new MovimentoInvalidoException("Mova para uma casa diferente!!!");
        }
        
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
            
            if (!saoVizinhos(origem, destino)) {
                // Só pode Super pulo daí, se nao for vizinho
                if (!superPuloDisponivel) {
                    throw new MovimentoInvalidoException("Movimento inválido! Apenas casas vizinhas!");
                }
            }
        } 
        
        // Regras Específicas do CARCARÁ
        else {
            // Carcará deve ser vizinho (não tem super pulo)
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
    
    public void realizarJogada(int origem, int destino) throws MovimentoInvalidoException {
      
        validarMovimento(origem, destino);

        if (turnoCabrito) {  
            boolean ehVizinho = saoVizinhos(origem, destino);     
            if (!ehVizinho) {
              
                superPuloDisponivel = false;
            }
            
            // Move o cabrito
            posicaoCabrito = destino;
        } 
        
       
        else {
            // Se o Carcará moveu para onde o Cabrito está, jogo acaba!
            if (destino == posicaoCabrito) {
                jogoAcabou = true;
            }
            
            // Move o carcará
            posicaoCarcara = destino;
        }

        contadorJogadas++; // Conta a jogada
        
        if (!jogoAcabou) {
            turnoCabrito = !turnoCabrito; // Troca a vez (inverte o boolean)
        }   
    }
    
    public void reiniciar() {
    this.posicaoCabrito = TOPO;
    this.posicaoCarcara = ESQ_SUP;
    this.turnoCabrito = true;
    this.superPuloDisponivel = true;
    this.jogoAcabou = false;
    this.contadorJogadas = 0;
    }
}
