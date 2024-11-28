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
    int[][] OrigNum = new int[rowSize][colSize]; // 원래 빙고
    static String[] images = { // 버튼 이미지 배열
            "num1.png","num2.png","num3.png","num4.png", "num5.png",
            "num6.png","num7.png","num8.png","num9.png", "num10.png",
            "num11.png","num12.png","num13.png","num14.png", "num15.png",
            "num16.png","num17.png","num18.png","num19.png", "num20.png",
            "num21.png","num22.png","num23.png","num24.png", "num25.png"
    };
    // 사용자 빙고판 확인
    boolean[] rowBingo = new boolean[rowSize];
    boolean[] colBingo = new boolean[colSize];
    boolean rdcrossBingo = false;
    boolean rucrossBingo = false;
    boolean[][] btnOX = new boolean[rowSize][colSize]; // 사용자 클릭 확인
    int bingoCount = 0; // 사용자 빙고 카운트
    // 컴퓨터 빙고판 확인
    boolean[] c_rowBingo = new boolean[rowSize];
    boolean[] c_colBingo = new boolean[colSize];
    boolean c_rdcrossBingo = false;
    boolean c_rucrossBingo = false;
    boolean[][] computerOX=new boolean[rowSize][colSize]; // 컴퓨터 클릭 확인
    int c_bingoCount = 0; // 컴퓨터 빙고 카운트

    boolean isComputerTurn = false; // 컴퓨터 턴을 나타내는 변수
    Bingoapp bingoapp;

    Gameboard(Bingoapp bingoapp) { // 생성자
        this.bingoapp = bingoapp;
        setLayout(new GridLayout(rowSize, colSize));
        generateUniqueRandomNumbers();
        initializeBoard();
        original();
        generateUniqueRandomNumbers(); // 컴퓨터 빙고판 세팅(bingoNum 배열을 컴퓨터의 빙고판으로 사용)
    }

    void generateUniqueRandomNumbers() { // 숫자 랜덤 생성 메소드
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

    public void initializeBoard() { // 사용자 빙고판 세팅 메소드
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

    void original() { // 원래 빙고 저장 메소드
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                OrigNum[i][j] = bingoNum[i][j];
                computerOX[i][j] = false;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MyButton btn = (MyButton) e.getSource();
        // 사용자가 선택한 버튼 처리
        if (!btnOX[btn.x][btn.y] && !isComputerTurn) { // 이미 선택된 버튼인지 확인
            btn.setIcon(changeImage("u_click.png")); // 버튼 클릭 시 버튼 이미지를 변경
            btnOX[btn.x][btn.y] = true;
            bingoapp.setTurnLabel("현재 턴: 컴퓨터"); // 컴퓨터 턴으로 변경
            updateComputerBoard(btn.num); //컴퓨터 판에도 적용

            if(!checkBingo()) { // 게임이 종료되지 않았을 때만
                isComputerTurn = true; // 컴퓨터 턴으로 변경
                // 1초 후에 컴퓨터 턴으로 전환
                Timer timer = new Timer(1000, evt -> computerTurn());
                timer.setRepeats(false); // 한 번만 실행
                timer.start();
            }
        }
        else if(isComputerTurn){
            JOptionPane.showMessageDialog(this, "컴퓨터 차례입니다!");
        }
        else{ // 이미 선택된 칸을 클릭한 경우
            JOptionPane.showMessageDialog(this, "이미 선택된 칸입니다!");
        }
    }

    void computerTurn() {
        bingoapp.setTurnLabel("현재 턴: 사용자"); // 사용자 턴으로 변경
        // 1초 후에 컴퓨터가 선택하는 로직 실행
        Timer timer = new Timer(1000, evt -> {
            while (true) {
                int x = rand.nextInt(rowSize);
                int y = rand.nextInt(colSize);
                // 컴퓨터가 선택한 버튼 처리
                if (!btnOX[x][y]) {
                    btnOX[x][y] = true;
                    cellPanel[x][y].setIcon(changeImage("c_click.png")); // 버튼 클릭 시 버튼 이미지를 변경
                    updateComputerBoard(OrigNum[x][y]); // 컴퓨터 판에도 적용
                    break;
                }
            }
            checkBingo();
            isComputerTurn = false; // 사용자 턴으로 변경
        });
        timer.setRepeats(false); // 한 번만 실행
        timer.start();
    }

    void updateComputerBoard(int num) {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (bingoNum[i][j] == num) computerOX[i][j] = true;
            }
        }
    }

    boolean checkBingo() {
        checkPlayerBingo(); // 사용자 빙고 카운트
        checkComputerBingo(); // 컴퓨터 빙고 카운트
        // 게임 종료 확인
        if (bingoCount >= 3) { // 사용자가 3개 이상의 빙고를 만들면 점수를 추가하고 게임 종료
            for(int i=0; i<rowSize; i++) { // 컴퓨터 빙고판 확인
                for(int j=0; j<colSize; j++) {
                    if(computerOX[i][j])
                        System.out.print("●\t");
                    else System.out.print(bingoNum[i][j]+"\t");
                }
                System.out.println();
            }
            System.out.println("Bingo Count: "+c_bingoCount);
            bingoapp.incrementScore(100);
            bingoapp.gameOver();
            return true;
        } else if (c_bingoCount >= 3) { // 컴퓨터가 승리할 경우 점수 추가 없이 게임 종료
            for(int i=0; i<rowSize; i++) { // 컴퓨터 빙고판 확인
                for(int j=0; j<colSize; j++) {
                    if(computerOX[i][j])
                        System.out.print("●\t");
                    else System.out.print(bingoNum[i][j]+"\t");
                }
                System.out.println();
            }
            System.out.println("Bingo Count: "+c_bingoCount);
            bingoapp.gameOverComputer();
            return true;
        }
        return false;
    }

    void checkPlayerBingo() { // 사용자 빙고 검사 메소드
        int count = 0;

        // 가로 및 세로 빙고 확인
        for (int i = 0; i < rowSize; i++) {
            if (!rowBingo[i] && isRowBingo(btnOX, i)) {
                rowBingo[i] = true;
                count++;
                for (int n = 0; n < rowSize; n++) {
                    cellPanel[i][n].setIcon(changeImage("line.png")); // 빙고 한 줄 완성 시 버튼 이미지를 변경
                }
            }
            if (!colBingo[i] && isColBingo(btnOX, i)) {
                colBingo[i] = true;
                count++;
                for (int n = 0; n < rowSize; n++) {
                    cellPanel[n][i].setIcon(changeImage("line.png")); // 빙고 한 줄 완성 시 버튼 이미지를 변경
                }
            }
        }
        // 대각선 빙고 확인
        if (!rdcrossBingo && isDiagonalBingo(btnOX, true)) {
            rdcrossBingo = true;
            count++;
            for (int n = 0; n < rowSize; n++) {
                cellPanel[n][n].setIcon(changeImage("line.png")); // 빙고 한 줄 완성 시 버튼 이미지를 변경
            }
        }
        if (!rucrossBingo && isDiagonalBingo(btnOX, false)) {
            rucrossBingo = true;
            count++;
            for (int n = 0; n < rowSize; n++) {
                cellPanel[n][rowSize - 1 - n].setIcon(changeImage("line.png")); // 빙고 한 줄 완성 시 버튼 이미지를 변경
            }
        }

        if (count > 0) { // 빙고 카운트 업데이트
            bingoCount += count;
            bingoapp.updateBingoCount(bingoCount);
        }
    }

    void checkComputerBingo() { // 컴퓨터 빙고 검사 메소드
        int count = 0;

        // 가로 및 세로 빙고 확인
        for (int i = 0; i < rowSize; i++) {
            if (!c_rowBingo[i] && isRowBingo(computerOX, i)) {
                c_rowBingo[i] = true;
                count++;
            }
            if (!c_colBingo[i] && isColBingo(computerOX, i)) {
                c_colBingo[i] = true;
                count++;
            }
        }
        // 대각선 빙고 확인
        if (!c_rdcrossBingo && isDiagonalBingo(computerOX, true)) {
            c_rdcrossBingo = true;
            count++;
        }
        if (!c_rucrossBingo && isDiagonalBingo(computerOX, false)) {
            c_rucrossBingo = true;
            count++;
        }

        if (count > 0) { // 빙고 카운트 업데이트
            c_bingoCount += count;
        }
    }

    boolean isRowBingo(boolean[][] oxArray, int row) { // 가로 빙고 검사 메소드
        for (int col = 0; col < colSize; col++) {
            if (!oxArray[row][col]) {
                return false;
            }
        }
        return true;
    }

    boolean isColBingo(boolean[][] oxArray, int col) { // 세로 빙고 검사 메소드
        for (int row = 0; row < rowSize; row++) {
            if (!oxArray[row][col]) {
                return false;
            }
        }
        return true;
    }

    boolean isDiagonalBingo(boolean[][] oxArray, boolean isMainDiagonal) { // 대각선 빙고 검사 메소드
        for (int i = 0; i < rowSize; i++) {
            if (isMainDiagonal && !oxArray[i][i]) { // rd
                return false;
            }
            if (!isMainDiagonal && !oxArray[i][rowSize - 1 - i]) { // ru
                return false;
            }
        }
        return true;
    }

    class MyButton extends JButton {
        int x, y, num;
        MyButton(int x, int y, int num) {
            this.x = x;
            this.y = y;
            this.num = num;
            setIcon(changeImage(images[num-1])); // 버튼 초기 이미지를 각 번호에 맞는 이미지로 설정
            setOpaque(true);
        }
    }

    static ImageIcon changeImage(String filename) { // 버튼 이미지 변환 메소드
        ImageIcon icon = new ImageIcon("img/bingoimg/" + filename); // 파일 경로 지정
        Image originImage = icon.getImage();
        Image changedImage = originImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH); // 이미지 크기 설정
        ImageIcon icon_new = new ImageIcon(changedImage);
        return icon_new;
    }

    public Cursor customcursor() { // 커서 이미지 변환 메소드
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image cursorImage = tk.getImage("img/bingoimg/cursor.png"); // 파일 경로 지정
        Image scaledImage = cursorImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // 커서 크기 설정
        Point point = new Point(40,40); // 커서 포인트 위치 설정
        Cursor cursor = tk.createCustomCursor(scaledImage, point, "");
        return cursor;
    }
}