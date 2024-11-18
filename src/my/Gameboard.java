package my;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Gameboard extends JPanel implements ActionListener {
    static final int rowSize = 5;
    static final int colSize = 5;
    Random rand = new Random();
    MyButton[][] cellPanel = new MyButton[rowSize][colSize];
    int[][] userBingoNum = new int[rowSize][colSize]; // 유저의 숫자 배열
    int[][] computerBingoNum = new int[rowSize][colSize]; // 컴퓨터의 숫자 배열
    boolean[][] btnOX = new boolean[rowSize][colSize]; // 선택 여부 배열
    HashMap<Integer, Point> numberPositionMap = new HashMap<>(); // 숫자 위치 맵핑
    int bingoCount = 0;
    BingoPanel bingoPanel;
    String playerType; // "User" 또는 "Computer"
    Set<Integer> usedNumbers = new HashSet<>(); // 숫자 중복 방지
    Point lastMove; // 마지막 컴퓨터의 움직임을 저장

    public Gameboard(BingoPanel bingoPanel, String playerType) {
        if (bingoPanel == null) {
            throw new IllegalArgumentException("MainApp cannot be null");
        }
        this.bingoPanel = bingoPanel;
        this.playerType = playerType;
        setLayout(new GridLayout(rowSize, colSize));
        generateUniqueRandomNumbers(); // 고유한 숫자 배열 생성
        initializeBoard();
    }

    void generateUniqueRandomNumbers() {
        usedNumbers.clear(); // 숫자 중복 방지 맵 초기화
        // 유저와 컴퓨터의 숫자 배열을 고유한 숫자로 생성
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                int num;
                do {
                    num = rand.nextInt(25) + 1; // 1부터 25까지의 숫자
                } while (usedNumbers.contains(num)); // 이미 사용된 숫자는 제외
                usedNumbers.add(num); // 새로 사용된 숫자를 추가
                userBingoNum[i][j] = num; // 유저의 각 위치에 숫자 할당
                computerBingoNum[i][j] = num; // 컴퓨터의 각 위치에 숫자 할당
                // 숫자와 위치를 맵핑하여 저장
                numberPositionMap.put(num, new Point(i, j));
            }
        }
    }

    public void initializeBoard() {
        removeAll();  // 기존의 모든 컴포넌트를 제거
        generateUniqueRandomNumbers();  // 고유한 숫자 배열 생성

        // 버튼과 보드 초기화
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                btnOX[i][j] = false;  // 버튼 선택 여부 초기화
                int numToDisplay = "User".equals(playerType) ? userBingoNum[i][j] : 0; // 유저 보드에만 숫자 표시
                cellPanel[i][j] = new MyButton(i, j, numToDisplay);  // 버튼 초기화
                cellPanel[i][j].addActionListener(this);  // 클릭 리스너 추가
                cellPanel[i][j].setFont(cellPanel[i][j].getFont().deriveFont(28.0f));  // 글자 크기 설정
                add(cellPanel[i][j]);  // 버튼을 패널에 추가
            }
        }
        revalidate();  // UI 갱신
        repaint();  // 화면 갱신
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        MyButton btn = (MyButton) e.getSource();

        if (playerType.equals("User")) {
            if (!btnOX[btn.x][btn.y]) {
                btnOX[btn.x][btn.y] = true;
                btn.setBackground(Color.BLUE); // 유저 클릭은 파란색
                bingoPanel.reflectUserMoveOnComputer(btn.x, btn.y); // 유저 선택을 컴퓨터 화면에 반영
                if (checkBingo()) {
                    bingoPanel.updateBingoCount(bingoCount, playerType);
                }
                bingoPanel.switchToComputerTurn();
            }
        }
    }

    // 컴퓨터의 턴 처리 (유저 화면에 반영)
    public void computerMakeMove() {
        boolean moveMade = false;

        while (!moveMade) {
            int num = rand.nextInt(25) + 1; // 1부터 25까지의 숫자 중 랜덤 선택
            Point position = numberPositionMap.get(num); // 컴퓨터의 고유 숫자 배열에서 위치를 얻음

            // 해당 칸이 아직 선택되지 않았다면
            if (!btnOX[position.x][position.y]) {
                MyButton btn = cellPanel[position.x][position.y];
                btnOX[position.x][position.y] = true;
                btn.setBackground(Color.RED); // 컴퓨터 선택은 빨간색
                lastMove = position; // 마지막 움직임 저장
                if (checkBingo()) {
                    bingoPanel.updateBingoCount(bingoCount, playerType);
                }
                bingoPanel.reflectUserMoveOnComputer(position.x, position.y); // 컴퓨터 선택을 유저 화면에 반영
                moveMade = true;
            }
        }
    }

    public Point getLastMove() {
        return lastMove;
    }

    public void updateMove(int x, int y, Color color) {
        btnOX[x][y] = true;
        cellPanel[x][y].setBackground(color);
    }

    boolean checkBingo() {
        int newBingoCount = 0;
        int rdcrossCount = 0, rucrossCount = 0;

        // 가로, 세로, 대각선 체크
        for (int i = 0; i < rowSize; i++) {
            int garoCount = 0, seroCount = 0;
            for (int j = 0; j < colSize; j++) {
                if (btnOX[i][j]) garoCount++;
                if (btnOX[j][i]) seroCount++;
            }
            if (garoCount == rowSize) newBingoCount++;
            if (seroCount == colSize) newBingoCount++;
            if (btnOX[i][i]) rdcrossCount++;
            if (btnOX[i][rowSize - 1 - i]) rucrossCount++;
        }

        if (rdcrossCount == rowSize) newBingoCount++;
        if (rucrossCount == rowSize) newBingoCount++;

        bingoCount += newBingoCount;

        // 3줄 빙고가 완성되면 게임 종료
        if (bingoCount >= 3) {
            endGame();
            return true;
        }

        return newBingoCount > 0;
    }

    public void endGame() {
        // 게임 종료 시 호출되는 메소드
        JOptionPane.showMessageDialog(this, "3줄 빙고 완성! 게임 종료!");

        // 게임을 멈추기 위해 보드와 버튼 비활성화
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                cellPanel[i][j].setEnabled(false); // 모든 버튼 비활성화
            }
        }

        // Start 버튼을 활성화하여 게임이 재시작될 수 있도록 함
        bingoPanel.getStartButton().setEnabled(true);
    }




    class MyButton extends JButton {
        int x, y, num;

        MyButton(int x, int y, int num) {
            this.x = x;
            this.y = y;
            this.num = num;
            setBackground(Color.WHITE);
            setOpaque(true);

            // 유저와 컴퓨터에 따라 다르게 숫자 표시
            if ("User".equals(playerType)) {
                setText(String.valueOf(num));  // 유저는 숫자 보여줌
            } else {
                setText("");  // 컴퓨터는 숫자 숨김
            }
        }
    }
}
