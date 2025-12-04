package trabalho.tp2_poo;

/**
 *
 * @author Fade Hassan Husein Kanaan
 * @author Rodrigo Thoma da Silva
 */
public class CorreCabritoGUI extends javax.swing.JFrame {

    private Jogo jogo;
    private boolean modoSuperPuloAtivo = false;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CorreCabritoGUI.class.getName());

    /**
     * Creates new form CorreCabritoGUI
     */
    public CorreCabritoGUI() {
        initComponents();

        configurarMenus();

        jPanel2.remove(jLabel3);

        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 530, 800, 40));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.setComponentZOrder(jLabel3, 0);

        jogo = new Jogo();

        atualizarTela();
    }

    /**
     * Atualiza a interface gráfica com base no estado atual do objeto Jogo.
     * Este método redesenha os ícones nas posições corretas, atualiza as
     * mensagens de turno e verifica se houve condição de vitória.
     */
    private void atualizarTela() {
        // Limpa ícones e bordas de todos os botões
        javax.swing.JButton[] todosBotoes = {btnTopo, btnEsquerdaSuperior, btnDireitaSuperior, btnEsquerdaInferior, btnDireitaInferior, btnCentro};

        for (javax.swing.JButton btn : todosBotoes) {
            btn.setIcon(null);
            btn.setBorder(null);         // Remove borda antiga
            btn.setBorderPainted(false); // Garante que não pinte borda padrão quadrada
        }

        // Carrega e define os ícones das peças
        javax.swing.Icon iconCabrito = new javax.swing.ImageIcon(getClass().getResource("/images/Cabrito2Icon.png"));
        javax.swing.Icon iconCarcara = new javax.swing.ImageIcon(getClass().getResource("/images/CarcaraIcon.png"));

        int posCabrito = jogo.getPosicaoCabrito();
        int posCarcara = jogo.getPosicaoCarcara();

        javax.swing.JButton btnCabrito = getBotaoPorId(posCabrito);
        javax.swing.JButton btnCarcara = getBotaoPorId(posCarcara);

        btnCabrito.setIcon(iconCabrito);
        btnCarcara.setIcon(iconCarcara);

        jLabel3.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // Centraliza texto

        if (jogo.isVezDoCabrito()) {
            jLabel3.setText("Vez do Cabrito");
            jLabel3.setForeground(new java.awt.Color(34, 139, 34)); // Verde Floresta
        } else {
            jLabel3.setText("Vez do Carcará");
            jLabel3.setForeground(java.awt.Color.RED); // Vermelho
        }

        // Aplica o Destaque Amarelo (Borda Redonda) na peça da vez
        javax.swing.border.Border destaque = new BordaRedonda(java.awt.Color.YELLOW, 5);

        if (jogo.isVezDoCabrito()) {
            btnCabrito.setBorder(destaque);
            btnCabrito.setBorderPainted(true);
        } else {
            btnCarcara.setBorder(destaque);
            btnCarcara.setBorderPainted(true);
        }

        // Lógica do Botão Super Pulo
        boolean turnoCabrito = jogo.isVezDoCabrito();
        boolean temPoder = jogo.isSuperPuloDisponivel();

        btnSuperPulo.setEnabled(turnoCabrito && temPoder);

        if (modoSuperPuloAtivo && turnoCabrito && temPoder) {
            // Visual do botao ativado
            btnSuperPulo.setBackground(java.awt.Color.GREEN);
            btnSuperPulo.setText("ESCOLHA O DESTINO");
        } else {
            // Visual do botao ja usado
            btnSuperPulo.setBackground(new java.awt.Color(255, 255, 102)); // Seu amarelo original
            btnSuperPulo.setText(temPoder ? "Super Pulo" : "JÁ USADO");

            // Se perdeu o poder ou passou a vez, desativa o modo automaticamente
            if (!turnoCabrito || !temPoder) {
                modoSuperPuloAtivo = false;
            }
        }

        // Fim de Jogo
        if (jogo.isJogoAcabou()) {

            javax.swing.Icon iconeFim = new javax.swing.ImageIcon(getClass().getResource("/images/vitoria.png"));
            Object[] opcoes = {"Reiniciar", "Sair"};

            int escolha = javax.swing.JOptionPane.showOptionDialog(
                    this,
                    jogo.getVencedor() + "\n\nO que deseja fazer?",
                    "Fim de Jogo",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.PLAIN_MESSAGE, //  PLAIN para o ícone personalizado aparecer bem       
                    iconeFim,
                    opcoes,
                    opcoes[0]
            );

            if (escolha == 0) {
                reiniciarJogo();
            } else {
                System.exit(0);
            }
        }
    }

    private void configurarMenus() {
        jMenuBar1.removeAll();

        jMenu1.removeAll();
        jMenu2.removeAll();

        // Configurando Menu Jogo jMenu1
        jMenu1.setText("Jogo");

        // Item Reiniciar
        javax.swing.JMenuItem itemReiniciar = new javax.swing.JMenuItem("Reiniciar");
        itemReiniciar.addActionListener(evt -> reiniciarJogo());
        jMenu1.add(itemReiniciar);

        // Item Regras (dentro do menu Jogo)
        javax.swing.JMenuItem itemRegras = new javax.swing.JMenuItem("Regras");
        itemRegras.addActionListener(evt -> {
            String regras = "REGRAS DO CORRE CABRITO:\n\n"
                    + "1. O Cabrito foge e o Carcará caça.\n"
                    + "2. As peças movem-se pelas linhas pretas.\n"
                    + "3. O Cabrito tem 1 Super Pulo para qualquer casa vazia.\n"
                    + "4. O Carcará vence se ocupar a mesma casa do Cabrito.\n"
                    + "5. O Cabrito vence se sobreviver infinitamente.\n"
                    + "(Ou seja, não vence kkkkkkkkkkkkk)\n"
                    + "\nBom jogo!";
            javax.swing.JOptionPane.showMessageDialog(this, regras, "Regras", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        });
        jMenu1.add(itemRegras);

        jMenu1.addSeparator(); // Linha separadora

        // Item Sair
        javax.swing.JMenuItem itemSair = new javax.swing.JMenuItem("Sair");
        itemSair.addActionListener(evt -> System.exit(0));
        jMenu1.add(itemSair);

        // Configurando Menu Autoria jMenu2
        jMenu2.setText("Autoria");

        javax.swing.JMenuItem itemNomes = new javax.swing.JMenuItem("Ver nomes");
        itemNomes.addActionListener(evt -> {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Autores do Trabalho:\n\nFade Hassan Husein Kanaan\nRodrigo Thoma da Silva");
        });
        jMenu2.add(itemNomes);

        jMenuBar1.add(jMenu1);
        jMenuBar1.add(jMenu2);

        jMenuBar1.revalidate();
        jMenuBar1.repaint();
    }

    // Converte ID (0, 1, 2...) no Botão real do jogo
    private javax.swing.JButton getBotaoPorId(int id) {
        switch (id) {
            case Jogo.TOPO:
                return btnTopo;
            case Jogo.ESQ_SUP:
                return btnEsquerdaSuperior;
            case Jogo.DIR_SUP:
                return btnDireitaSuperior;
            case Jogo.ESQ_INF:
                return btnEsquerdaInferior;
            case Jogo.DIR_INF:
                return btnDireitaInferior;
            case Jogo.CENTRO:
                return btnCentro;
            default:
                return null;
        }
    }

    /**
     * Método centralizador que recebe a ação de clique de qualquer botão do
     * tabuleiro. Ele conecta a interação do usuário (Front-end) com a lógica de
     * validação (Back-end).
     */
    private void tentarMover(int destino) {
        try {
            // Determina automaticamente a origem com base em de quem é a vez
            int origem;
            if (jogo.isVezDoCabrito()) {
                origem = jogo.getPosicaoCabrito();
            } else {
                origem = jogo.getPosicaoCarcara();
            }

            if (jogo.isVezDoCabrito()) {
                boolean ehVizinho = jogo.saoVizinhos(origem, destino);

                // Se for a própria casa, ele pula esse IF e vai pro backend dar o erro certo.
                if (!ehVizinho && !modoSuperPuloAtivo && origem != destino) {

                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Para pular para longe, clique no botão 'SUPER PULO' primeiro!",
                            "Atenção", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return; // Cancela e não chama o backend
                }
            }
            // ----------------------------------------

            // Tenta realizar a jogada no backend
            jogo.realizarJogada(origem, destino);

            // Se a jogada deu certo, reseta o botão (para não ficar ligado pra sempre)
            modoSuperPuloAtivo = false;

            atualizarTela();

        } catch (MovimentoInvalidoException e) {
            javax.swing.JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void reiniciarJogo() {
        // Reseta a lógica
        jogo.reiniciar();
        // Reseta a visualização
        atualizarTela();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        btnSuperPulo = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnTopo = new javax.swing.JButton();
        btnEsquerdaSuperior = new javax.swing.JButton();
        btnEsquerdaInferior = new javax.swing.JButton();
        btnCentro = new javax.swing.JButton();
        btnDireitaSuperior = new javax.swing.JButton();
        btnDireitaInferior = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CorreCabrito");
        setIconImages(null);
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 600));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSuperPulo.setBackground(new java.awt.Color(255, 255, 102));
        btnSuperPulo.setText("Super Pulo");
        btnSuperPulo.addActionListener(this::btnSuperPuloActionPerformed);
        jPanel2.add(btnSuperPulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 60, -1, -1));

        jLabel2.setText("Clique para ativar (1 USO)");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 40, -1, -1));

        jLabel3.setText("Vez do Cabrito");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 540, -1, -1));

        btnTopo.setForeground(new java.awt.Color(60, 63, 65));
        btnTopo.setToolTipText("");
        btnTopo.setBorderPainted(false);
        btnTopo.setContentAreaFilled(false);
        btnTopo.setPreferredSize(new java.awt.Dimension(90, 90));
        btnTopo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTopoMouseClicked(evt);
            }
        });
        btnTopo.addActionListener(this::btnTopoActionPerformed);
        jPanel2.add(btnTopo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, 110, 110));

        btnEsquerdaSuperior.setToolTipText("");
        btnEsquerdaSuperior.setBorderPainted(false);
        btnEsquerdaSuperior.setContentAreaFilled(false);
        btnEsquerdaSuperior.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEsquerdaSuperiorMouseClicked(evt);
            }
        });
        btnEsquerdaSuperior.addActionListener(this::btnEsquerdaSuperiorActionPerformed);
        jPanel2.add(btnEsquerdaSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 110, 110));

        btnEsquerdaInferior.setForeground(new java.awt.Color(60, 63, 65));
        btnEsquerdaInferior.setBorder(null);
        btnEsquerdaInferior.setBorderPainted(false);
        btnEsquerdaInferior.setContentAreaFilled(false);
        btnEsquerdaInferior.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEsquerdaInferiorMouseClicked(evt);
            }
        });
        btnEsquerdaInferior.addActionListener(this::btnEsquerdaInferiorActionPerformed);
        jPanel2.add(btnEsquerdaInferior, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 410, 110, 120));

        btnCentro.setForeground(new java.awt.Color(255, 255, 255));
        btnCentro.setBorder(null);
        btnCentro.setBorderPainted(false);
        btnCentro.setContentAreaFilled(false);
        btnCentro.setPreferredSize(new java.awt.Dimension(90, 90));
        btnCentro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCentroMouseClicked(evt);
            }
        });
        btnCentro.addActionListener(this::btnCentroActionPerformed);
        jPanel2.add(btnCentro, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 250, 110, 100));

        btnDireitaSuperior.setForeground(new java.awt.Color(60, 63, 65));
        btnDireitaSuperior.setBorder(null);
        btnDireitaSuperior.setBorderPainted(false);
        btnDireitaSuperior.setContentAreaFilled(false);
        btnDireitaSuperior.setPreferredSize(new java.awt.Dimension(90, 90));
        btnDireitaSuperior.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDireitaSuperiorMouseClicked(evt);
            }
        });
        btnDireitaSuperior.addActionListener(this::btnDireitaSuperiorActionPerformed);
        jPanel2.add(btnDireitaSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 200, 120, 110));

        btnDireitaInferior.setForeground(new java.awt.Color(60, 63, 65));
        btnDireitaInferior.setBorder(null);
        btnDireitaInferior.setBorderPainted(false);
        btnDireitaInferior.setContentAreaFilled(false);
        btnDireitaInferior.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDireitaInferiorMouseClicked(evt);
            }
        });
        btnDireitaInferior.addActionListener(this::btnDireitaInferiorActionPerformed);
        jPanel2.add(btnDireitaInferior, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 420, 110, 110));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/FundoIcon.png"))); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 600));

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Jogo");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Autoria");
        jMenuBar1.add(jMenu2);

        jMenu4.setText("Regras");
        jMenuBar1.add(jMenu4);

        jMenu3.setText("Sair");
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTopoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTopoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTopoActionPerformed

    private void btnEsquerdaSuperiorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEsquerdaSuperiorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEsquerdaSuperiorActionPerformed

    private void btnEsquerdaInferiorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEsquerdaInferiorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEsquerdaInferiorActionPerformed

    private void btnCentroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCentroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCentroActionPerformed

    private void btnDireitaSuperiorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDireitaSuperiorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDireitaSuperiorActionPerformed

    private void btnDireitaInferiorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDireitaInferiorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDireitaInferiorActionPerformed

    private void btnCentroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCentroMouseClicked
        tentarMover(Jogo.CENTRO);
    }//GEN-LAST:event_btnCentroMouseClicked

    private void btnTopoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTopoMouseClicked
        tentarMover(Jogo.TOPO);
    }//GEN-LAST:event_btnTopoMouseClicked

    private void btnEsquerdaSuperiorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEsquerdaSuperiorMouseClicked
        tentarMover(Jogo.ESQ_SUP);
    }//GEN-LAST:event_btnEsquerdaSuperiorMouseClicked

    private void btnDireitaSuperiorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDireitaSuperiorMouseClicked
        tentarMover(Jogo.DIR_SUP);
    }//GEN-LAST:event_btnDireitaSuperiorMouseClicked

    private void btnEsquerdaInferiorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEsquerdaInferiorMouseClicked
        tentarMover(Jogo.ESQ_INF);
    }//GEN-LAST:event_btnEsquerdaInferiorMouseClicked

    private void btnDireitaInferiorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDireitaInferiorMouseClicked
        tentarMover(Jogo.DIR_INF);
    }//GEN-LAST:event_btnDireitaInferiorMouseClicked

    private void btnSuperPuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuperPuloActionPerformed
        if (jogo.isVezDoCabrito() && jogo.isSuperPuloDisponivel()) {
            modoSuperPuloAtivo = !modoSuperPuloAtivo; // Toggle do botao
            atualizarTela();
        }    }//GEN-LAST:event_btnSuperPuloActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new CorreCabritoGUI().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCentro;
    private javax.swing.JButton btnDireitaInferior;
    private javax.swing.JButton btnDireitaSuperior;
    private javax.swing.JButton btnEsquerdaInferior;
    private javax.swing.JButton btnEsquerdaSuperior;
    private javax.swing.JButton btnSuperPulo;
    private javax.swing.JButton btnTopo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
