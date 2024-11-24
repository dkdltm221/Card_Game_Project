package Login;

import Main.MainApp;
import Main.InputUsers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    static JFrame mainFrame = new JFrame("Login System");
    private Register register = new Register();
    private InputUsers inputUsers = new InputUsers();

    public Login() {
        register.readAll(); // 파일에서 사용자 데이터 읽기
        inputUsers.readAll(); // 포인트 데이터 읽기
    }

    public void startGUI() {
        javax.swing.SwingUtilities.invokeLater(this::createAndShowLoginPanel);
    }

    private void createAndShowLoginPanel() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(800, 600)); // 창 크기 설정
        mainFrame.setContentPane(createBackgroundPanel()); // 배경 설정
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private JPanel createBackgroundPanel() {
        // JFrame 크기 가져오기
        Dimension screenSize = new Dimension(800, 600); // 창 크기와 동일하게 설정
        int frameWidth = screenSize.width;
        int frameHeight = screenSize.height;

        // 배경 이미지 로드 및 크기 조정
        ImageIcon backgroundIcon = new ImageIcon("img/cards/LoginBackground.png.jpg");
        Image backgroundImage = backgroundIcon.getImage();
        Image scaledImage = backgroundImage.getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH); // 창 크기에 맞게 조정
        ImageIcon scaledBackgroundIcon = new ImageIcon(scaledImage);

        // JLabel로 설정
        JLabel backgroundLabel = new JLabel(scaledBackgroundIcon);
        backgroundLabel.setLayout(new GridBagLayout()); // 중앙 배치를 위한 레이아웃 설정

        // 로그인 박스 추가
        JPanel loginBox = setupLoginBox();
        loginBox.setOpaque(false); // 패널 배경을 투명하게 설정

        // 로그인 박스를 배경 아래쪽에 배치
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // 가로 위치
        gbc.gridy = 1; // 세로 위치 (아래로 이동시키는 핵심)
        gbc.anchor = GridBagConstraints.PAGE_END; // 하단 배치
        gbc.insets = new Insets(50, 0, 50, 0); // 여백 (위, 왼쪽, 아래, 오른쪽)
        backgroundLabel.add(loginBox, gbc);

        // 전체 패널 반환
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(backgroundLabel, BorderLayout.CENTER);

        return mainPanel;
    }



    private JPanel setupLoginBox() {
        // 로그인 박스 패널 생성
        JPanel loginBox = new JPanel();
        loginBox.setPreferredSize(new Dimension(350, 200));
        loginBox.setBackground(new Color(0, 0, 0)); // 반투명 검정 배경


        // 내부 로그인 패널 생성
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setOpaque(false); // 내부 패널 투명화

        JLabel userLabel = new JLabel("아이디:");
        JLabel passLabel = new JLabel("비밀번호:");
        userLabel.setForeground(Color.WHITE); // 텍스트 흰색
        passLabel.setForeground(Color.WHITE);

        JTextField usernameField = new JTextField();
        JPasswordField pwField = new JPasswordField();
        JButton loginButton = new JButton("로그인");
        JButton signupButton = new JButton("회원가입");

        // 버튼 스타일 설정
        loginButton.setBackground(new Color(70, 130, 180)); // 스틸 블루
        loginButton.setForeground(Color.BLACK); // 텍스트 흰색
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));

        signupButton.setBackground(new Color(34, 139, 34)); // 초록색
        signupButton.setForeground(Color.BLACK);
        signupButton.setFont(new Font("Arial", Font.BOLD, 12));

        // 로그인 버튼 액션 (기존 로직 유지)
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String pw = new String(pwField.getPassword());
                if (register.matches(username) && register.matches(pw)) { // 아이디와 비밀번호 일치 확인
                    JOptionPane.showMessageDialog(mainFrame, "로그인 성공!");
                    mainFrame.dispose();
                    SwingUtilities.invokeLater(() -> new MainApp(username));
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "로그인 실패!");
                }
            }
        });

        // 회원가입 버튼 액션 (기존 로직 유지)
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String pw = new String(pwField.getPassword());
                if (!register.matches(username)) { // 중복 아이디 확인
                    register.addUserToFile(username, pw);
                    inputUsers.addUserToFile(username, 100); // 회원가입 시 초기 포인트
                    JOptionPane.showMessageDialog(mainFrame, "회원가입 성공!!");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "중복된 아이디");
                }
            }
        });

        // 로그인 패널에 컴포넌트 추가
        loginPanel.add(userLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passLabel);
        loginPanel.add(pwField);
        loginPanel.add(loginButton);
        loginPanel.add(signupButton);

        // 박스에 로그인 패널 추가
        loginBox.setLayout(new GridBagLayout());
        loginBox.add(loginPanel);

        return loginBox;
    }


    public static void main(String[] args) {
        Login login = new Login();
        login.startGUI();
    }
}
