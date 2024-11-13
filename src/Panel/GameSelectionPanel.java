package Panel;

import Main.MainApp;

import javax.swing.*;
import java.awt.*;
public class GameSelectionPanel extends JPanel {
    private MainApp mainApp;
    private JLabel jLabel2;
    public GameSelectionPanel(MainApp app) {
        this.mainApp = app;
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1,2));
        JLabel jLabel1 = new JLabel();
        jLabel2 = new JLabel();

        jLabel1.setText(MainApp.getUserName());
        jLabel1.setFont(new Font("돋움",Font.BOLD,40));
        jLabel2.setText(" "+ MainApp.getUserScore());
        jLabel2.setFont(new Font("돋움",Font.BOLD,40));
        northPanel.add(jLabel1);
        northPanel.add(jLabel2);

        Timer timer = new Timer(1000, e -> updateScoreLabel());
        timer.start();

        JPanel centerPanel = new JPanel(new GridLayout(1, 3));
        JButton blackjackButton = new JButton("블랙잭");
        JButton bingoButton = new JButton("빙고");
        JButton thiefGameButton = new JButton("도둑잡기");

        blackjackButton.addActionListener(e -> {
            mainApp.showScreen("BlackjackPanel");
        });
        bingoButton.addActionListener(e -> showNotImplemented());
        thiefGameButton.addActionListener(e -> showNotImplemented());

        centerPanel.add(blackjackButton);
        centerPanel.add(bingoButton);
        centerPanel.add(thiefGameButton);

        JPanel southPanel = new JPanel();
        JButton scoreboardButton = new JButton("점수판");
        southPanel.add(scoreboardButton);

        scoreboardButton.addActionListener(e -> mainApp.showScreen("Scoreboard"));

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void updateScoreLabel() {
        jLabel2.setText(" " + MainApp.getUserScore());
    }
    private void showNotImplemented() {
        JOptionPane.showMessageDialog(this, "아직 게임이 개발되지 않았음");
    }
}
