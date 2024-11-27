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
    static String[] images = { // 씨앗 이미지 배열
            "seed1.png","seed2.png","seed3.png","seed4.png", "seed5.png",
            "seed6.png","seed7.png","seed8.png","seed9.png", "seed10.png",
            "seed11.png","seed12.png","seed13.png","seed14.png", "seed15.png",
            "seed16.png","seed17.png","seed18.png","seed19.png", "seed20.png",
            "seed21.png","seed22.png","seed23.png","seed24.png", "seed25.png"
    };
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
        Cursor c=customcursor();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                btnOX[i][j] = false;
                cellPanel[i][j] = new MyButton(i, j, bingoNum[i][j]);
                cellPanel[i][j].addActionListener(this);
                cellPanel[i][j].setFont(cellPanel[i][j].getFont().deriveFont(28.0f));
                add(cellPanel[i][j]);
                cellPanel[i][j].setCursor(c);
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
            btn.setIcon(changeImage("sprout.png"));
            btnOX[btn.x][btn.y] = true;
            bingoapp.setTurnLabel("현재 턴: 컴퓨터"); // 턴을 컴퓨터로 변경
            checkBingo();
            isComputerTurn = true; // 컴퓨터 턴으로 변경

            if(bingoCount<3) {
                // 1초 후에 컴퓨터 턴으로 전환
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        computerTurn(); // 컴퓨터의 차례 실행
                    }
                });
                timer.setRepeats(false); // 한 번만 실행
                timer.start();
            }


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
                        cellPanel[x][y].setIcon(changeImage("sprout.png")); // 버튼 클릭 시 버튼 이미지를 새싹 이미지로 설정
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
                    cellPanel[i][n].setIcon(changeImage("flower.png")); // 빙고 한 줄 완성 시 버튼 이미지를 꽃 이미지로 설정
                }
            }
            if (seroCount == colSize && !colBingo[i]) {
                colBingo[i] = true;
                newBingoCount++;
                for (int n = 0; n < colSize; n++) {
                    cellPanel[n][i].setIcon(changeImage("flower.png"));
                }
            }
            if (btnOX[i][i]) rdcrossCount++;
            if (btnOX[i][rowSize - 1 - i]) rucrossCount++;
        }

        if (rdcrossCount == rowSize && !rdcrossBingo) {
            rdcrossBingo = true;
            newBingoCount++;
            for (int n = 0; n < rowSize; n++) {
                cellPanel[n][n].setIcon(changeImage("flower.png"));
            }
        }

        if (rucrossCount == rowSize && !rucrossBingo) {
            rucrossBingo = true;
            newBingoCount++;
            for (int n = 0; n < rowSize; n++) {
                cellPanel[n][rowSize - 1 - n].setIcon(changeImage("flower.png"));
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
            setIcon(changeImage(images[num-1])); // 버튼 초기 이미지를 번호에 맞는 씨앗 이미지로 설정
            setOpaque(true);
        }
    }

    static ImageIcon changeImage(String filename) { // 이미지 변환 메소드
        ImageIcon icon = new ImageIcon("img/image/" + filename); // 파일 경로 지정
        Image originImage = icon.getImage();
        Image changedImage = originImage.getScaledInstance(160, 120, Image.SCALE_SMOOTH); // 이미지 크기 설정
        ImageIcon icon_new = new ImageIcon(changedImage);
        return icon_new;
    }

    public Cursor customcursor() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image cursorImage = tk.getImage("img/image/cursor.png"); // 파일 경로 지정
        Image scaledImage = cursorImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // 커서 크기 설정
        Point point = new Point(40,40); // 커서 포인트 위치 설정
        Cursor cursor = tk.createCustomCursor(scaledImage, point, "");
        return cursor;
    }
}