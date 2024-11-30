package Panel;

import my.Gameboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BingoPanel extends JPanel {
    Gameboard userBoard;
    Gameboard computerBoard;
    JLabel userScoreLabel;
    JLabel computerScoreLabel;
    int userScore = 0;
    int computerScore = 0;
    JButton startButton;  // startButton 변수 추가

    public BingoPanel() {
        setLayout(new BorderLayout());

        // 유저와 컴퓨터의 빙고판 생성
        userBoard = new Gameboard(this, "User");
        computerBoard = new Gameboard(this, "Computer");

        // 유저 점수 라벨과 컴퓨터 점수 라벨 설정
        JPanel userPanel = new JPanel(new BorderLayout());
        userScoreLabel = new JLabel("User Score: 0");
        userPanel.add(userScoreLabel, BorderLayout.NORTH);
        userPanel.add(userBoard, BorderLayout.CENTER);

        JPanel computerPanel = new JPanel(new BorderLayout());
        computerScoreLabel = new JLabel("Computer Score: 0");
        computerPanel.add(computerScoreLabel, BorderLayout.NORTH);
        computerPanel.add(computerBoard, BorderLayout.CENTER);

        add(userPanel, BorderLayout.WEST);
        add(computerPanel, BorderLayout.EAST);

        // 버튼 패널 생성
        JPanel buttonPanel = new JPanel(new FlowLayout());
        startButton = new JButton("Start");  // startButton 초기화
        JButton restartButton = new JButton("Restart");
        JButton exitButton = new JButton("Exit");

        // 시작 버튼 리스너
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // 다시 시작 버튼 리스너
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        // 종료 버튼 리스너
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // 프로그램 종료
            }
        });

        buttonPanel.add(startButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void startGame() {
        // 점수 초기화
        userScore = 0;
        computerScore = 0;
        userScoreLabel.setText("User Score: 0");
        computerScoreLabel.setText("Computer Score: 0");

        // 유저와 컴퓨터 보드 초기화
        userBoard.initializeBoard();
        computerBoard.initializeBoard();

        // 게임 시작 화면을 보여줍니다.
        switchToUserTurn();

        // Start 버튼 비활성화 (게임 중에는 Start 버튼을 사용할 수 없도록 설정)
        startButton.setEnabled(false);  // 오류 수정: bingoPanel.getStartButton() 대신 startButton 사용
    }

    public void updateBingoCount(int bingoCount, String playerType) {
        if (bingoCount >= 3) {
            if (playerType.equals("User")) {
                userScore++;
                userScoreLabel.setText("User Score: " + userScore);
            } else if (playerType.equals("Computer")) {
                computerScore++;
                computerScoreLabel.setText("Computer Score: " + computerScore);
            }
        }
    }

    public void reflectUserMoveOnComputer(int x, int y) {
        computerBoard.updateMove(x, y, Color.BLUE);
    }

    public void switchToComputerTurn() {
        userBoard.setEnabled(false);
        computerBoard.setEnabled(true);
        computerBoard.computerMakeMove(); // 컴퓨터가 무작위로 칸 선택

        // 컴퓨터의 선택이 유저 화면에도 반영되도록 전달
        Point computerMove = computerBoard.getLastMove();
        userBoard.updateMove(computerMove.x, computerMove.y, Color.RED);
        switchToUserTurn();
    }

    public void switchToUserTurn() {
        userBoard.setEnabled(true);
        computerBoard.setEnabled(false);
    }

    public void restartGame() {
        userScore = 0;
        computerScore = 0;
        userScoreLabel.setText("User Score: 0");
        computerScoreLabel.setText("Computer Score: 0");

        userBoard.initializeBoard(); // 유저 보드 초기화
        computerBoard.initializeBoard(); // 컴퓨터 보드 초기화

        // 게임 시작 화면을 다시 보여줍니다.
        switchToUserTurn();
    }

    public JButton getStartButton() {
        return startButton;  // startButton 반환
    }

    public static void main(String[] args) {
        // MainApp을 JFrame에 추가하여 표시
        JFrame frame = new JFrame("Bingo Game");
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BingoPanel());  // MainApp을 JFrame에 추가
        frame.setVisible(true);
    }
}
