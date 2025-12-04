package trabalho.tp2_poo;


/* começo de codigo gerado por IA */
/**
 * Classe utilitária para desenhar bordas circulares personalizadas nos botões.
 */
class BordaRedonda implements javax.swing.border.Border {

    private int espessura;
    private java.awt.Color cor;

    public BordaRedonda(java.awt.Color cor, int espessura) {
        this.cor = cor;
        this.espessura = espessura;
    }

    @Override
    public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        // Ativa o anti-aliasing para o círculo não ficar serrilhado
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(cor);
        g2.setStroke(new java.awt.BasicStroke(espessura));

        // CÁLCULO DE TAMANHO MÁXIMO:
        // Pega o menor lado do botão e desconta a espessura para a borda caber inteira dentro
        int diametro = Math.min(width, height) - espessura;

        // Centraliza perfeitamente
        int xCentro = x + (width - diametro) / 2;
        int yCentro = y + (height - diametro) / 2;

        g2.drawOval(xCentro, yCentro, diametro, diametro);
    }

    @Override
    public java.awt.Insets getBorderInsets(java.awt.Component c) {
        // RETORNAR 0 AQUI É O SEGREDO:
        // Isso diz ao botão: "Não diminua a imagem do cabrito, eu vou desenhar a borda por cima/em volta"
        return new java.awt.Insets(0, 0, 0, 0);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}

    /* fim de codigo gerado por IA */
