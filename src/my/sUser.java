package my;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class sUser extends JFrame {
    Gameboard userBoard;
    Set<Integer> usedNumbersForUser;

    public sUser(BingoPanel bingoPanel) {
        setTitle("User's Bingo Game");
        setLayout(new BorderLayout());

        // User 클래스에서는 고유 숫자를 관리할 Set 생성
        usedNumbersForUser = new HashSet<>();

        // "User" 플레이어용 Gameboard 생성
        userBoard = new Gameboard(bingoPanel, "User");

        add(userBoard, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 400));
        pack();
        setVisible(true);
    }
}