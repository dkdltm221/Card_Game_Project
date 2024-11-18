package my;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Computer extends JFrame {
    Gameboard computerBoard;
    Set<Integer> usedNumbersForComputer;

    public Computer(BingoPanel bingoPanel) {
        setTitle("Computer's Bingo Game");
        setLayout(new BorderLayout());

        // Computer 클래스에서는 유저의 숫자와 겹치지 않는 고유 숫자를 사용
        usedNumbersForComputer = new HashSet<>();

        // "Computer" 플레이어용 Gameboard 생성
        computerBoard = new Gameboard(bingoPanel, "Computer");

        add(computerBoard, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 400));
        pack();
        setVisible(true);
    }
}
