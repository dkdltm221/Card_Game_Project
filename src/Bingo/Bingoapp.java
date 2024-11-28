package Bingo;

import Main.MainApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bingoapp extends JPanel {
    static int score = 0;
    static JLabel scoreLabel = new JLabel("Score: 0");
    static JLabel bingoCountLabel = new JLabel("Bingo Count: 0");
    static JLabel turnLabel = new JLabel("현재 턴: 사용자"); // 현재 턴 표시 라벨 수정
    Gameboard board;
    private MainApp mainApp;

    public Bingoapp(MainApp mainApp) {
        this.mainApp = mainApp;
        setupMainPanel();
    }

    public void setupMainPanel() {
        setLayout(new BorderLayout());

        // 상단 정보 패널 (가로로 표시)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10)); // 가로 간격 30, 세로 간격 10

        // 라벨 글씨 크기 설정
        scoreLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        bingoCountLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        turnLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));

        // 라벨 추가
        infoPanel.add(scoreLabel);
        infoPanel.add(bingoCountLabel);
        infoPanel.add(turnLabel);

        board = new Gameboard(this);
        add(infoPanel, BorderLayout.NORTH); // 정보 패널 상단 배치
        add(board, BorderLayout.CENTER); // 게임 보드 가운데 배치

        // 하단 버튼 패널
        JPanel controlPanel = new JPanel();
        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit Game");

        // 버튼 글씨 크기와 크기 조정
        startButton.setFont(new Font("맑은 고딕", Font.BOLD, 24));  // 버튼 글씨 크기
        exitButton.setFont(new Font("맑은 고딕", Font.BOLD, 24));   // 버튼 글씨 크기
        startButton.setPreferredSize(new Dimension(200, 60));  // 버튼 크기 조정
        exitButton.setPreferredSize(new Dimension(200, 60));   // 버튼 크기 조정

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.showScreen("GameSelection");
            }
        });

        controlPanel.add(startButton);
        controlPanel.add(exitButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public void resetGame() {
        bingoCountLabel.setText("Bingo Count: 0");
        board.bingoCount = 0;
        board.c_bingoCount = 0;
        board.generateUniqueRandomNumbers();
        board.initializeBoard();
        board.original();
        board.generateUniqueRandomNumbers();
        setTurnLabel("사용자"); // 게임 초기화 시 사용자 턴으로 설정
    }

    public void gameOver() {
        JOptionPane.showMessageDialog(this, "게임 종료! 빙고가 3개 이상 완성되었습니다.");
    }

    public void gameOverComputer() {
        JOptionPane.showMessageDialog(this, "상대방 승리! 빙고가 3개 이상 완성되었습니다.");
        // 점수를 추가하지 않음
    }

    public void updateBingoCount(int bingoCount) {
        bingoCountLabel.setText("Bingo Count: " + bingoCount);
    }

    public void incrementScore(int additionalScore) {
        score += additionalScore;
        scoreLabel.setText("Score: " + score);
    }

    public void setTurnLabel(String turn) {
        turnLabel.setText("현재 턴: " + turn); // 중복 방지
    }
}
