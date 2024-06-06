//package br.edu.cesarschool.cc.poo.ac.passagem.gui;
//
//import br.edu.cesarschool.cc.poo.ac.passagem.BilheteMediator;
//import br.edu.cesarschool.cc.poo.ac.passagem.ResultadoGeracaoBilhete;
//import br.edu.cesarschool.cc.poo.ac.passagem.Voo;
//import br.edu.cesarschool.cc.poo.ac.passagem.VooMediator;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//
//public class TelaGuiBilhete {
//    private BilheteMediator bilheteMediator = BilheteMediator.obterInstancia();
//    private VooMediator vooMediator = VooMediator.obterInstancia();
//
//    private JFrame frame;
//    private JTextField cpfField;
//    private JComboBox<String> companhiaAereaComboBox;
//    private JComboBox<Integer> numeroVooComboBox;
//    private JTextField precoField;
//    private JTextField pagamentoPontosField;
//    private JTextField dataHoraField;
//    private JRadioButton normalRadioButton;
//    private JRadioButton vipRadioButton;
//    private JTextField bonusPontuacaoField;
//    private ButtonGroup radioGroup;
//
//    public TelaGuiBilhete() {
//        initialize();
//    }
//
//    private void initialize() {
//        frame = new JFrame("Tela de Bilhete");
//        frame.setBounds(100, 100, 450, 300);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().setLayout(new GridLayout(10, 2));
//
//        frame.getContentPane().add(new JLabel("CPF:"));
//        cpfField = new JTextField();
//        frame.getContentPane().add(cpfField);
//
//        frame.getContentPane().add(new JLabel("Companhia Aérea:"));
//        companhiaAereaComboBox = new JComboBox<>();
//        populateCompanhiaAereaComboBox();
//        frame.getContentPane().add(companhiaAereaComboBox);
//
//        frame.getContentPane().add(new JLabel("Número do Voo:"));
//        numeroVooComboBox = new JComboBox<>();
//        populateNumeroVooComboBox();
//        frame.getContentPane().add(numeroVooComboBox);
//
//        frame.getContentPane().add(new JLabel("Preço:"));
//        precoField = new JTextField();
//        frame.getContentPane().add(precoField);
//
//        frame.getContentPane().add(new JLabel("Pagamento em pontos:"));
//        pagamentoPontosField = new JTextField();
//        frame.getContentPane().add(pagamentoPontosField);
//
//        frame.getContentPane().add(new JLabel("Data e Hora:"));
//        dataHoraField = new JTextField();
//        frame.getContentPane().add(dataHoraField);
//
//        normalRadioButton = new JRadioButton("Bilhete Normal");
//        vipRadioButton = new JRadioButton("Bilhete Vip");
//        radioGroup = new ButtonGroup();
//        radioGroup.add(normalRadioButton);
//        radioGroup.add(vipRadioButton);
//        normalRadioButton.setSelected(true);
//
//        normalRadioButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                bonusPontuacaoField.setEnabled(false);
//                bonusPontuacaoField.setText("");
//            }
//        });
//
//        vipRadioButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                bonusPontuacaoField.setEnabled(true);
//            }
//        });
//
//        frame.getContentPane().add(normalRadioButton);
//        frame.getContentPane().add(vipRadioButton);
//
//        frame.getContentPane().add(new JLabel("Bônus Pontuação:"));
//        bonusPontuacaoField = new JTextField();
//        bonusPontuacaoField.setEnabled(false);
//        frame.getContentPane().add(bonusPontuacaoField);
//
//        JButton gerarBilheteButton = new JButton("Gerar Bilhete");
//        gerarBilheteButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                gerarBilhete();
//            }
//        });
//        frame.getContentPane().add(gerarBilheteButton);
//
//        JButton limparButton = new JButton("Limpar");
//        limparButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                limparCampos();
//            }
//        });
//        frame.getContentPane().add(limparButton);
//
//        frame.setVisible(true);
//        limparCampos();
//    }
//
//    private void populateCompanhiaAereaComboBox() {
//        Voo[] voos = vooMediator.buscarTodos();
//        if (voos != null) {
//            for (Voo voo : voos) {
//                if (((DefaultComboBoxModel<String>) companhiaAereaComboBox.getModel()).getIndexOf(voo.getCompanhiaAerea()) == -1) {
//                    companhiaAereaComboBox.addItem(voo.getCompanhiaAerea());
//                }
//            }
//        }
//    }
//
//    private void populateNumeroVooComboBox() {
//        Voo[] voos = vooMediator.buscarTodos();
//        if (voos != null) {
//            for (Voo voo : voos) {
//                if (((DefaultComboBoxModel<Integer>) numeroVooComboBox.getModel()).getIndexOf(voo.getNumeroVoo()) == -1) {
//                    numeroVooComboBox.addItem(voo.getNumeroVoo());
//                }
//            }
//        }
//    }
//
//    private void gerarBilhete() {
//        String dataHoraString = dataHoraField.getText();
//        LocalDateTime dataHora = null;
//
//        if (!dataHoraString.isEmpty()) {
//            try {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//                dataHora = LocalDateTime.parse(dataHoraString, formatter);
//            } catch (DateTimeParseException e) {
//                System.err.println("Erro ao converter data e hora: " + e.getMessage());
//            }
//        }
//
//        String cpf = cpfField.getText();
//        String companhiaAerea = (String) companhiaAereaComboBox.getSelectedItem();
//        int numeroVoo = (int) numeroVooComboBox.getSelectedItem();
//        double preco = Double.parseDouble(precoField.getText());
//        double pagamentoPontos = Double.parseDouble(pagamentoPontosField.getText());
//        boolean isVip = vipRadioButton.isSelected();
//        double bonusPontuacao = 0.0;
//        String mensagem = "";
//
//        ResultadoGeracaoBilhete resultadoGeracaoBilhete;
//        if (isVip) {
//            bonusPontuacao = Double.parseDouble(bonusPontuacaoField.getText());
//            resultadoGeracaoBilhete = bilheteMediator.gerarBilheteVip(cpf, companhiaAerea, numeroVoo, preco, pagamentoPontos, dataHora, bonusPontuacao);
//        } else {
//            resultadoGeracaoBilhete = bilheteMediator.gerarBilhete(cpf, companhiaAerea, numeroVoo, preco, pagamentoPontos, dataHora);
//        }
//
//        if (resultadoGeracaoBilhete.getBilhete() != null) {
//            JOptionPane.showMessageDialog(frame, "Bilhete gerado com sucesso!");
//        } else {
//            JOptionPane.showMessageDialog(frame, "Erro ao gerar bilhete: " + resultadoGeracaoBilhete.getMensagemErro());
//        }
//    }
//
//    private void limparCampos() {
//        cpfField.setText("");
//        companhiaAereaComboBox.setSelectedIndex(0);
//        numeroVooComboBox.setSelectedIndex(0);
//        precoField.setText("");
//        pagamentoPontosField.setText("");
//        dataHoraField.setText("");
//        normalRadioButton.setSelected(true);
//        bonusPontuacaoField.setText("");
//        bonusPontuacaoField.setEnabled(false);
//        cpfField.requestFocus();
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    TelaGuiBilhete window = new TelaGuiBilhete();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//}