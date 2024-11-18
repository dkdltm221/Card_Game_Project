package Bingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Gameboard extends JPanel implements ActionListener {
    static final int rowSize = 5; // 빙고 크기
    static final int colSize = 5; // 빙고 크기
    Random rand = new Random();
    MyButton[][] cellPanel = new MyButton[rowSize][colSize];
    int[][] bingoNum = new int[rowSize][colSize];
    boolean[][] btnOX = new boolean[rowSize][colSize];
    boolean[] rowBingo = new boolean[rowSize];
    boolean[] colBingo = new boolean[colSize];
    boolean rdcrossBingo = false;
    boolean rucrossBingo = false;
    int bingoCount = 0;
    Bingoapp bingoapp;

    boolean isComputerTurn = false; // 컴퓨터 턴을 나타내는 변수

    Gameboard(Bingoapp bingoapp) {
        this.bingoapp = bingoapp;
        setLayout(new GridLayout(rowSize, colSize));
        generateUniqueRandomNumbers();
        initializeBoard();
    }

    void generateUniqueRandomNumbers() {
        Set<Integer> usedNumbers = new HashSet<>();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                int num;
                do {
                    num = rand.nextInt(25) + 1;
                } while (usedNumbers.contains(num));
                usedNumbers.add(num);
                bingoNum[i][j] = num;
            }
        }
    }

    public void initializeBoard() {
        removeAll();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                btnOX[i][j] = false;
                cellPanel[i][j] = new MyButton(i, j, bingoNum[i][j]);
                cellPanel[i][j].addActionListener(this);
                cellPanel[i][j].setFont(cellPanel[i][j].getFont().deriveFont(28.0f));
                cellPanel[i][j].setBackground(null);
                add(cellPanel[i][j]);
            }
        }
        bingoCount = 0;
        rowBingo = new boolean[rowSize];
        colBingo = new boolean[colSize];
        rdcrossBingo = false;
        rucrossBingo = false;
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MyButton btn = (MyButton) e.getSource();

        // 사용자가 선택한 버튼 처리
        if (!btnOX[btn.x][btn.y] && !isComputerTurn) { // 이미 선택된 버튼인지 확인
            btn.setBackground(Color.BLUE);
            btnOX[btn.x][btn.y] = true;
            bingoapp.setTurnLabel("현재 턴: 컴퓨터"); // 턴을 컴퓨터로 변경
            checkBingo();
            isComputerTurn = true; // 컴퓨터 턴으로 변경

            // 1초 후에 컴퓨터 턴으로 전환
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    computerTurn(); // 컴퓨터의 차례 실행
                }
            });
            timer.setRepeats(false); // 한 번만 실행
            timer.start();
        } else {
            // 이미 선택된 칸을 클릭한 경우
            JOptionPane.showMessageDialog(this, "이미 선택된 칸입니다!");
        }
    }


    void computerTurn() {
        // 컴퓨터 턴에서 색깔을 먼저 바꾸기
        bingoapp.setTurnLabel("현재 턴: 사용자"); // 턴을 사용자로 변경

        // 1초 후에 컴퓨터가 선택하는 로직 실행
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                while (true) {
                    int x = rand.nextInt(rowSize);
                    int y = rand.nextInt(colSize);
                    if (!btnOX[x][y]) {
                        btnOX[x][y] = true;
                        cellPanel[x][y].setBackground(Color.RED);
                        break;
                    }
                }

                if (checkBingo()) {
                    // 컴퓨터가 3개 빙고를 완성하면 상대방 승리 메시지 표시
                    if (bingoCount >= 3) {
                        bingoapp.gameOverComputer(); // 상대방 승리 메시지
                    }
                }
                isComputerTurn = false; // 다시 사용자 차례로 변경
            }
        });
        timer.setRepeats(false); // 한 번만 실행
        timer.start();
    }

    boolean checkBingo() {
        int newBingoCount = 0;
        int rdcrossCount = 0, rucrossCount = 0;

        for (int i = 0; i < rowSize; i++) {
            int garoCount = 0, seroCount = 0;
            for (int j = 0; j < colSize; j++) {
                if (btnOX[i][j]) garoCount++;
                if (btnOX[j][i]) seroCount++;
            }
            if (garoCount == rowSize && !rowBingo[i]) {
                rowBingo[i] = true;
                newBingoCount++;
                for (int n = 0; n < rowSize; n++) {
                    cellPanel[i][n].setBackground(Color.YELLOW);
                }
            }
            if (seroCount == colSize && !colBingo[i]) {
                colBingo[i] = true;
                newBingoCount++;
                for (int n = 0; n < colSize; n++) {
                    cellPanel[n][i].setBackground(Color.YELLOW);
                }
            }
            if (btnOX[i][i]) rdcrossCount++;
            if (btnOX[i][rowSize - 1 - i]) rucrossCount++;
        }

        if (rdcrossCount == rowSize && !rdcrossBingo) {
            rdcrossBingo = true;
            newBingoCount++;
            for (int n = 0; n < rowSize; n++) {
                cellPanel[n][n].setBackground(Color.YELLOW);
            }
        }

        if (rucrossCount == rowSize && !rucrossBingo) {
            rucrossBingo = true;
            newBingoCount++;
            for (int n = 0; n < rowSize; n++) {
                cellPanel[n][rowSize - 1 - n].setBackground(Color.YELLOW);
            }
        }

        if (newBingoCount > 0) {
            bingoCount += newBingoCount;
            bingoapp.updateBingoCount(bingoCount);

            // 사용자가 3개 이상의 빙고를 만들면 점수를 추가하고 게임 종료
            if (bingoCount >= 3) {
                bingoapp.incrementScore(1);
                bingoapp.gameOver();
                return true;
            }

            // 컴퓨터가 승리할 경우 점수 추가 없음
            return true;
        }
        return false;
    }

    class MyButton extends JButton {
        int x, y, num;

        MyButton(int x, int y, int num) {
            this.x = x;
            this.y = y;
            this.num = num;
            setBackground(Color.WHITE);
            setOpaque(true);
            setText("<html>" + num + "</html>");
        }
    }
}
