import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CadastroAlunosSwingAvancado extends JFrame {

    private DefaultListModel<Aluno> modelAlunos = new DefaultListModel<>();
    private JList<Aluno> listaAlunos = new JList<>(modelAlunos);
    private DefaultListModel<String> modelDisciplinas = new DefaultListModel<>();
    private JList<String> listaDisciplinas = new JList<>(modelDisciplinas);

    public CadastroAlunosSwingAvancado() {
        super("Cadastro de Alunos - Versão Pro");

        // Look and Feel Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus Look and Feel não disponível.");
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760, 460);
        setLocationRelativeTo(null);

        // Painel esquerdo - lista alunos com borda e sombra
        JPanel painelEsq = new JPanel(new BorderLayout(5, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 87, 153), 0, h, new Color(125, 185, 232));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, w, h, 30, 30);
            }
        };
        painelEsq.setBorder(new EmptyBorder(15, 15, 15, 15));
        painelEsq.setOpaque(false);

        JLabel lblAlunos = new JLabel("Alunos");
        lblAlunos.setFont(new Font("Montserrat", Font.BOLD, 20));
        lblAlunos.setForeground(Color.WHITE);
        lblAlunos.setHorizontalAlignment(SwingConstants.CENTER);

        listaAlunos.setFont(new Font("Segoe UI", Font.BOLD, 15));
        listaAlunos.setSelectionBackground(new Color(255, 140, 0));
        listaAlunos.setSelectionForeground(Color.WHITE);
        listaAlunos.setOpaque(false);
        listaAlunos.setBackground(new Color(0,0,0,0));

        painelEsq.add(lblAlunos, BorderLayout.NORTH);
        painelEsq.add(new JScrollPane(listaAlunos) {{
            setOpaque(false);
            getViewport().setOpaque(false);
        }}, BorderLayout.CENTER);

        // Painel direito - lista disciplinas com borda leve
        JPanel painelDir = new JPanel(new BorderLayout(5, 5));
        painelDir.setBorder(new CompoundBorder(
                new EmptyBorder(15, 15, 15, 15),
                BorderFactory.createLineBorder(new Color(30, 144, 255), 2, true)
        ));
        painelDir.setBackground(Color.WHITE);

        JLabel lblDisciplinas = new JLabel("Disciplinas");
        lblDisciplinas.setFont(new Font("Montserrat", Font.BOLD, 20));
        lblDisciplinas.setHorizontalAlignment(SwingConstants.CENTER);

        listaDisciplinas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaDisciplinas.setForeground(new Color(50, 50, 50));

        painelDir.add(lblDisciplinas, BorderLayout.NORTH);
        painelDir.add(new JScrollPane(listaDisciplinas), BorderLayout.CENTER);

        // Painel botoes com botões customizados
        JPanel painelBotoes = new JPanel(new GridLayout(4, 1, 15, 15));
        painelBotoes.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelBotoes.setBackground(new Color(240, 240, 240));

        JButton btnAddAluno = criarBotao("➕ Adicionar Aluno", new Color(34, 139, 34));
        JButton btnRemoverAluno = criarBotao("🗑 Remover Aluno", new Color(220, 20, 60));
        JButton btnAddDisciplina = criarBotao("📚 Adicionar Matéria", new Color(30, 144, 255));
        JButton btnCalcularMedia = criarBotao("🧮 Calcular Média", new Color(255, 140, 0));

        painelBotoes.add(btnAddAluno);
        painelBotoes.add(btnRemoverAluno);
        painelBotoes.add(btnAddDisciplina);
        painelBotoes.add(btnCalcularMedia);

        // Layout principal
        getContentPane().setLayout(new BorderLayout(20, 20));
        getContentPane().add(painelEsq, BorderLayout.WEST);
        getContentPane().add(painelDir, BorderLayout.CENTER);
        getContentPane().add(painelBotoes, BorderLayout.EAST);
        ((JComponent)getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // Eventos
        listaAlunos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) atualizarDisciplinas();
        });

        btnAddAluno.addActionListener(e -> adicionarAluno());
        btnRemoverAluno.addActionListener(e -> removerAluno());
        btnAddDisciplina.addActionListener(e -> adicionarDisciplina());
        btnCalcularMedia.addActionListener(e -> calcularMedia());
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setBorder(new RoundedBorder(15));
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
            }
        });
        return botao;
    }

    private void adicionarAluno() {
        String nome = JOptionPane.showInputDialog(this, "Nome do aluno:");
        if (nome != null && !nome.trim().isEmpty()) {
            modelAlunos.addElement(new Aluno(nome.trim()));
        }
    }

    private void removerAluno() {
        Aluno selecionado = listaAlunos.getSelectedValue();
        if (selecionado != null) {
            modelAlunos.removeElement(selecionado);
            modelDisciplinas.clear();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para remover.");
        }
    }

    private void adicionarDisciplina() {
        Aluno selecionado = listaAlunos.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para adicionar matéria.");
            return;
        }

        String nomeMat = JOptionPane.showInputDialog(this, "Nome da matéria:");
        if (nomeMat == null || nomeMat.trim().isEmpty()) return;

        String notaStr = JOptionPane.showInputDialog(this, "Nota da matéria:");
        if (notaStr == null) return;

        try {
            double nota = Double.parseDouble(notaStr);
            selecionado.adicionarDisciplina(new Disciplina(nomeMat.trim(), nota));
            atualizarDisciplinas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nota inválida!");
        }
    }

    private void atualizarDisciplinas() {
        modelDisciplinas.clear();
        Aluno selecionado = listaAlunos.getSelectedValue();
        if (selecionado != null) {
            for (Disciplina d : selecionado.getDisciplinas()) {
                modelDisciplinas.addElement(d.getNome() + " - Nota: " + d.getNota());
            }
        }
    }

    private void calcularMedia() {
        Aluno selecionado = listaAlunos.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para calcular a média.");
            return;
        }
        double media = selecionado.calcularMedia();
        JOptionPane.showMessageDialog(this, "Média do aluno " + selecionado.getNome() + ": " + String.format("%.2f", media));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CadastroAlunosSwingAvancado().setVisible(true);
        });
    }
}

// Classes auxiliares

class Aluno {
    private String nome;
    private ArrayList<Disciplina> disciplinas = new ArrayList<>();

    public Aluno(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void adicionarDisciplina(Disciplina d) {
        disciplinas.add(d);
    }

    public ArrayList<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public double calcularMedia() {
        if (disciplinas.isEmpty()) return 0;
        double soma = 0;
        for (Disciplina d : disciplinas) {
            soma += d.getNota();
        }
        return soma / disciplinas.size();
    }

    @Override
    public String toString() {
        return nome;
    }
}

class Disciplina {
    private String nome;
    private double nota;

    public Disciplina(String nome, double nota) {
        this.nome = nome;
        this.nota = nota;
    }

    public String getNome() {
        return nome;
    }

    public double getNota() {
        return nota;
    }
}

// Borda arredondada customizada
class RoundedBorder extends AbstractBorder {
    private int radius;
    public RoundedBorder(int radius) {
        this.radius = radius;
    }
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.GRAY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawRoundRect(x, y, width - 1, height -1, radius, radius);
        g2d.dispose();
    }
}
