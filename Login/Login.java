package Login;

import GameUser.User;

import javax.imageio.spi.RegisterableService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Login {
    static JFrame mainFrame = new JFrame("Login System");
    private Register register = new Register();
    private InputUsers inputUsers=new InputUsers();
    public Login() {
        register.readAll(); // 파일에서 사용자 데이터 읽기
        inputUsers.readAll();
    }

    public void startGUI() {
        javax.swing.SwingUtilities.invokeLater(this::createAndShowLoginPanel);
    }

    private void createAndShowLoginPanel() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(400, 300));
        mainFrame.getContentPane().add(setupLoginPanel());
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    JPanel setupLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel userLabel = new JLabel("아이디:");
        JLabel passLabel = new JLabel("비밀번호:");
        JTextField usernameField = new JTextField();
        JPasswordField pwField = new JPasswordField();
        JButton loginButton = new JButton("로그인");
        JButton signupButton = new JButton("회원가입");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String pw=new String(pwField.getPassword());
                if (register.matches(username)&&register.matches(pw)) {
                    JOptionPane.showMessageDialog(mainFrame, "로그인 성공!");
                    //패널 변경 할거 넣기
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "로그인 실패!");
                }
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String pw=new String(pwField.getPassword());
                int point;
                if (!register.matches(username)) {
                    register.addUserToFile(username, pw);
                    inputUsers.addUserToFile(username,10);
                    JOptionPane.showMessageDialog(mainFrame, "회원가입 성공!!");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "중복된 아이디");
                }
            }
        });

        loginPanel.add(userLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passLabel);
        loginPanel.add(pwField);
        loginPanel.add(loginButton);
        loginPanel.add(signupButton);

        return loginPanel;
    }

    public static void main(String[] args) {
        Login main = new Login();
        main.startGUI();
    }
}

