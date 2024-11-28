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
    int playnum=1;

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
        exitButton.addActionListener(e -> exitGame());
        controlPanel.add(startButton);
        controlPanel.add(exitButton);
        add(controlPanel, BorderLayout.SOUTH);
    }


    public void resetGame() {
        playnum++;
        bingoCountLabel.setText("Bingo Count: 0");
        board.bingoCount = 0;
        board.c_bingoCount=0;
        board.generateUniqueRandomNumbers();
        board.initializeBoard();
        board.original();
        board.generateUniqueRandomNumbers();
        setTurnLabel("사용자");
    }

    public void exitGame() {
        gameSelectionPanel.appendToGameLog("빙고에서 획득한 점수: "+score+"\n");
        mainApp.showScreen("GameSelection");
    }

    public void gameOver(String result) {
        if(playnum==1)
            gameSelectionPanel.appendToGameLog("----빙고----");

        gameSelectionPanel.appendToGameLog("게임 횟수: "+playnum+"회");
        if(result.equals("win")) {
            String log = "플레이어 win! - 플레이어 포인트 +100";
            gameSelectionPanel.appendToGameLog(log);
            JOptionPane.showMessageDialog(this, "게임 종료! 빙고가 3개 이상 완성되었습니다.");
        }
        else if(result.equals("lose")) {
            String log = "컴퓨터 win! - 플레이어 포인트 +0";
            gameSelectionPanel.appendToGameLog(log);
            JOptionPane.showMessageDialog(this, "컴퓨터 승리! 빙고가 3개 이상 완성되었습니다.");
        }
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