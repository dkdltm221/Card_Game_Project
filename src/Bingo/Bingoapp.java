package Bingo;

import Main.MainApp;
import Panel.GameSelectionPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bingoapp extends JPanel {
    static int score = 0;
    static JLabel scoreLabel = new JLabel("Score: 0");
    static JLabel bingoCountLabel = new JLabel("Bingo Count: 0");
    static JLabel turnLabel = new JLabel("현재턴 : 사용자");
    Gameboard board;
    private MainApp mainApp;

    // 싱글톤 GameSelectionPanel 인스턴스
    private static final GameSelectionPanel gameSelectionPanel = GameSelectionPanel.getInstance();

    public Bingoapp(MainApp mainApp) {
        this.mainApp = mainApp;
        setupMainPanel();
    }

    public void setupMainPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 정보 패널
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        infoPanel.setBackground(Color.WHITE);
        scoreLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        bingoCountLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        turnLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        infoPanel.add(scoreLabel);
        infoPanel.add(bingoCountLabel);
        infoPanel.add(turnLabel);

        board = new Gameboard(this);
        add(infoPanel, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);
        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit Game");
        startButton.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        exitButton.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(200, 60));
        exitButton.setPreferredSize(new Dimension(200, 60));

        startButton.addActionListener(e -> resetGame());
        exitButton.addActionListener(e -> mainApp.showScreen("GameSelection"));
        controlPanel.add(startButton);
        controlPanel.add(exitButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public void resetGame() {
        bingoCountLabel.setText("Bingo Count: 0");
        board.bingoCount = 0;
        board.generateUniqueRandomNumbers();
        board.initializeBoard();
        board.original();
        setTurnLabel("사용자");
        gameSelectionPanel.appendToGameLog("게임 초기화됨");
    }

    public void gameOver() {
        String log = "게임 종료: 사용자 승리!";
        gameSelectionPanel.appendToGameLog(log);
        JOptionPane.showMessageDialog(this, "게임 종료! 빙고가 3개 이상 완성되었습니다.");
    }

    public void gameOverComputer() {
        String log = "게임 종료: 컴퓨터 승리!";
        gameSelectionPanel.appendToGameLog(log);
        JOptionPane.showMessageDialog(this, "상대방 승리! 빙고가 3개 이상 완성되었습니다.");
    }

    public void updateBingoCount(int bingoCount) {
        bingoCountLabel.setText("Bingo Count: " + bingoCount);
    }

    public void incrementScore(int additionalScore) {
        score += additionalScore;
        scoreLabel.setText("Score: " + score);
    }

    public void setTurnLabel(String turn) {
        turnLabel.setText("" + turn);
    }
}
