package leitordetextosimples;

/* Projeto feito com conceitos aprendidos e citados em aula,
tanto em CARPRC1 e CARWEB2, e estudos a parte.
*/

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class LeitorDeTextoSimples {
    // Variáveis globais para controle da leitura
    private static BufferedReader leitorBuffer;
    private static int numeroLinha = 1;
    private static boolean primeiraLinha = true;
    private static int escolhaVelocidade = 1;
    private static String linhaAtual = null;
    private static JDialog dialogoPrincipal = null;
    private static JPanel painelPrincipal = null;
    private static boolean leituraEmAndamento = false;
    private static Process processoFala = null;
    
    public static void main(String[] args) {
        try {
            String mensagemBoasVindas = "Bem-vindo ao Leitor de Texto Acessível!\n\n" +
                "Este programa simula a leitura de texto para\n" +
                "pessoas com deficiência visual.\n\n" +
                " Use a BARRA DE ESPAÇO para ler a linha atual e avançar\n" + 
                " Use a tecla ESC para finalizar a leitura";
            
            mostrarDialogoEFalar(mensagemBoasVindas, "Leitor de Texto Acessível", JOptionPane.INFORMATION_MESSAGE);
            
            JFileChooser seletorArquivo = new JFileChooser();
            seletorArquivo.setDialogTitle("Selecione um arquivo de texto");
            
            int resultado = seletorArquivo.showOpenDialog(null);
            
            if (resultado != JFileChooser.APPROVE_OPTION) {
                String msgCancelamento = "Nenhum arquivo selecionado.\nO programa será encerrado.";
                mostrarDialogoEFalar(msgCancelamento, "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            File arquivoSelecionado = seletorArquivo.getSelectedFile();
            
            String msgVelocidade = "Escolha a velocidade de leitura:";
            
            new Thread(() -> falarTexto(msgVelocidade, 1)).start();
            
            String[] opcoesVelocidade = {"Lenta", "Normal", "Rápida"};
            escolhaVelocidade = JOptionPane.showOptionDialog(
                null,
                msgVelocidade,
                "Configuração de Leitura",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoesVelocidade,
                opcoesVelocidade[1]);
            
            pararFala();
            
            if (escolhaVelocidade == JOptionPane.CLOSED_OPTION) {
                escolhaVelocidade = 1;
            }
            
            FileReader leitorArquivo = new FileReader(arquivoSelecionado);
            leitorBuffer = new BufferedReader(leitorArquivo);
            
            iniciarInterfaceComTeclas();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Ocorreu um erro: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Exibe um diálogo e inicia automaticamente a leitura do texto em voz alta
    private static void mostrarDialogoEFalar(String mensagem, String titulo, int tipo) {
        final JOptionPane pane = new JOptionPane(mensagem, tipo, JOptionPane.DEFAULT_OPTION);
        final JDialog dialog = pane.createDialog(titulo);
        
        for (Component comp : pane.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component button : panel.getComponents()) {
                    if (button instanceof JButton) {
                        ((JButton) button).addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                pararFala();
                            }
                        });
                    }
                }
            }
        }
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    falarTexto(mensagem.replace("\n", " "), 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        dialog.setVisible(true);
        pararFala();
    }
    
    // Cria a interface principal com suporte para teclas de atalho
    private static void iniciarInterfaceComTeclas() {
        try {
            linhaAtual = leitorBuffer.readLine();
            
            if (linhaAtual == null) {
                String msgVazio = "O arquivo está vazio.";
                mostrarDialogoEFalar(msgVazio, "Aviso", JOptionPane.WARNING_MESSAGE);
                leitorBuffer.close();
                return;
            }
            
            dialogoPrincipal = new JDialog();
            dialogoPrincipal.setTitle("Leitor de Texto Acessível");
            dialogoPrincipal.setSize(400, 200);
            dialogoPrincipal.setLocationRelativeTo(null);
            dialogoPrincipal.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            painelPrincipal = new JPanel();
            painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
            
            atualizarConteudoPainel(false);
            
            dialogoPrincipal.add(painelPrincipal, BorderLayout.CENTER);
            
            dialogoPrincipal.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "lerEAvancar");
            dialogoPrincipal.getRootPane().getActionMap().put("lerEAvancar", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (!leituraEmAndamento) {
                        pararFala();
                        lerLinhaAtualEAvancar();
                    }
                }
            });
            
            dialogoPrincipal.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "encerrar");
            dialogoPrincipal.getRootPane().getActionMap().put("encerrar", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    pararFala();
                    encerrarLeitura();
                }
            });
            
            dialogoPrincipal.setVisible(true);
            
            lerLinhaAtualEAvancar();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erro ao iniciar a interface: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Atualiza o conteúdo do painel principal com a linha atual
    private static void atualizarConteudoPainel(boolean lendo) {
        painelPrincipal.removeAll();
        
        JLabel tituloLabel = new JLabel(lendo ? "LENDO LINHA " + numeroLinha : "LINHA " + numeroLinha);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tituloLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        painelPrincipal.add(tituloLabel);
        
        painelPrincipal.add(new JLabel(" "));
        
        JLabel conteudoLabel = new JLabel(linhaAtual);
        conteudoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        painelPrincipal.add(conteudoLabel);
        
        painelPrincipal.add(new JLabel(" "));
        
        JLabel instrucao1 = new JLabel(
            lendo ? "Aguarde a leitura terminar..." : "Pressione ESPAÇO para ler esta linha e avançar");
        instrucao1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        instrucao1.setFont(new Font("Arial", Font.PLAIN, 12));
        painelPrincipal.add(instrucao1);
        
        if (!lendo) {
            JLabel instrucao2 = new JLabel("Pressione ESC para encerrar");
            instrucao2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            instrucao2.setFont(new Font("Arial", Font.PLAIN, 12));
            painelPrincipal.add(instrucao2);
        }
        
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }
    
    // Lê a linha atual em voz alta e avança para a próxima linha
    private static void lerLinhaAtualEAvancar() {
        if (linhaAtual != null && !leituraEmAndamento) {
            leituraEmAndamento = true;
            
            atualizarConteudoPainel(true);
            
            if (primeiraLinha) {
                falarTexto("iniciando leitura", escolhaVelocidade);
                primeiraLinha = false;
            }
            
            falarTexto(linhaAtual, escolhaVelocidade);
            
            try {
                String proximaLinha = leitorBuffer.readLine();
                
                if (proximaLinha != null) {
                    numeroLinha++;
                    linhaAtual = proximaLinha;
                    
                    atualizarConteudoPainel(false);
                } else {
                    encerrarLeitura();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Erro ao avançar para a próxima linha: " + e.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
            leituraEmAndamento = false;
        }
    }
    
    // Interrompe qualquer leitura em voz alta que esteja em andamento
    private static void pararFala() {
        if (processoFala != null) {
            processoFala.destroyForcibly();
            processoFala = null;
        }
    }
    
    // Finaliza o programa e exibe uma mensagem de encerramento
    private static void encerrarLeitura() {
        try {
            if (leitorBuffer != null) {
                leitorBuffer.close();
            }
            
            if (dialogoPrincipal != null) {
                dialogoPrincipal.dispose();
            }
            
            final String mensagemFinal = "Leitura concluída!\nObrigado por usar o Leitor de Texto Acessível.";
            
            final JOptionPane pane = new JOptionPane(mensagemFinal, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            final JDialog dialog = pane.createDialog("Leitura Finalizada");
            
            for (Component comp : pane.getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component panelComp : ((JPanel) comp).getComponents()) {
                        if (panelComp instanceof JButton) {
                            JButton button = (JButton) panelComp;
                            button.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    pararFala();
                                    dialog.dispose();
                                    System.exit(0);
                                }
                            });
                        }
                    }
                }
            }
            
            dialog.setModal(false);
            dialog.setVisible(true);
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        
                        falarTexto(mensagemFinal.replace("\n", " "), escolhaVelocidade);
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dispose();
                                System.exit(0);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erro ao encerrar a leitura: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Utiliza o sintetizador de voz do Windows para ler um texto
    public static void falarTexto(String texto, int escolhaVelocidade) {
        try {
            double velocidade;
            int tempoEspera;
            
            switch (escolhaVelocidade) {
                case 0:
                    velocidade = -2;
                    tempoEspera = 150 * texto.length();
                    break;
                case 2:
                    velocidade = 3;
                    tempoEspera = 60 * texto.length();
                    break;
                default:
                    velocidade = 0;
                    tempoEspera = 100 * texto.length();
                    break;
            }
            
            String comando = "powershell -command \"Add-Type -AssemblyName System.Speech; " +
                          "$sintese = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                          "$sintese.Rate = " + velocidade + "; " +
                          "$sintese.Speak('" + texto.replace("'", "''") + "');\"";
            
            tempoEspera = Math.max(1500, tempoEspera);
            
            processoFala = Runtime.getRuntime().exec(comando);
            
            Thread.sleep(tempoEspera);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}