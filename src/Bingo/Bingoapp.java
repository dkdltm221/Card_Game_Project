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
    static JLabel turnLabel = new JLabel("현재 턴: 사용자"); // 현재 턴 표시 라벨 추가
    Gameboard board;
    private MainApp mainApp;

    public Bingoapp(MainApp mainApp) {
        this.mainApp = mainApp;
        setupMainPanel();
    }

    public void setupMainPanel() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        infoPanel.add(scoreLabel);
        infoPanel.add(bingoCountLabel);
        infoPanel.add(turnLabel); // 턴 라벨 추가

        board = new Gameboard(this);
        add(infoPanel, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit Game");

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
        board.generateUniqueRandomNumbers();
        board.initializeBoard();
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
        turnLabel.setText("현재 턴: " + turn); // 턴 업데이트 메서드 추가
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16)); // 강조 효과 추가
        // 사용자와 컴퓨터에 따라 색상 변경
        if (turn.equals("사용자")) {
            turnLabel.setForeground(Color.BLUE); // 사용자 차례일 때 파란색
        } else if (turn.equals("컴퓨터")) {
            turnLabel.setForeground(Color.RED); // 컴퓨터 차례일 때 빨간색
        }
    }


}
