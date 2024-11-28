package Panel;

import Main.MainApp;

import javax.swing.*;
import java.awt.*;

public class GameSelectionPanel extends JPanel {
    private static GameSelectionPanel instance;
    private MainApp mainApp;
    private JLabel infoLabel;
    private JTextArea gameLog = new JTextArea();

    public static final String IMAGE_DIR = "img/cards/";
    private GameSelectionPanel(){}
    public static GameSelectionPanel getInstance(){
        if(instance==null) instance = new GameSelectionPanel();
        return instance;
    }

    public JPanel createGameSelectionPanel(MainApp app) {
        this.mainApp = app;

        // 전체 레이아웃 설정
        setLayout(new BorderLayout());

        // 배경 패널 생성
        BackgroundPanel backgroundPanel = new BackgroundPanel("img/background.png");
        backgroundPanel.setLayout(new BorderLayout());

        // 중앙의 이름과 점수 패널
        JPanel infoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 반투명 배경
                g2.setColor(new Color(0, 0, 0, 150));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // 금빛 테두리
                g2.setColor(new Color(212, 175, 55));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new GridBagLayout()); // 중앙 정렬

        infoLabel = new JLabel("이름: " + MainApp.getUserName() + " 점수: " + MainApp.getUserScore()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                // 텍스트만 표시 (그림자 효과 제거)
                g2.setColor(getForeground());
                g2.drawString(getText(), 0, getHeight() - g.getFontMetrics().getDescent());
            }
        };
        infoLabel.setFont(new Font("Serif", Font.BOLD, 20));
        infoLabel.setForeground(new Color(255, 215, 0)); // 황금빛 텍스트

        infoPanel.add(infoLabel);

        Timer timer = new Timer(1000, e -> updateInfoLabel());
        timer.start();

        // Center Panel 생성 (이미지와 버튼을 중앙 배치)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 50, 20, 50); // 버튼 간격 확대
        gbc.fill = GridBagConstraints.CENTER;

// 블랙잭 게임
        gbc.gridx = 0;
        gbc.gridy = 0;
        JButton button = setButton("블랙잭");
        centerPanel.add(createGameComponent(IMAGE_DIR + "BlackJack_Card", button, "BlackjackPanel"), gbc);

// 빙고 게임
        gbc.gridx = 1;
        JButton bingoButton = setButton("빙고");
        centerPanel.add(createGameComponent(IMAGE_DIR + "Bingo_Card", bingoButton, "BingoApp"), gbc);

// 도둑잡기 게임
        gbc.gridx = 2;
        JButton ThiefButton = setButton("도둑잡기");
        centerPanel.add(createGameComponent(IMAGE_DIR + "Thief_Card", ThiefButton, "ThiefPanel"), gbc);

// 점수판 패널 생성
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BorderLayout());
        scorePanel.setPreferredSize(new Dimension(300, 400)); // 점수판 크기 확대
        scorePanel.setOpaque(false);


// 점수판 버튼 추가
        JButton scoreboardButton = new JButton("점수판");
        scoreboardButton.setPreferredSize(new Dimension(300, 50));
        scoreboardButton.addActionListener(e -> mainApp.showScreen("Scoreboard"));
        scorePanel.add(scoreboardButton, BorderLayout.NORTH);

// 점수판에 들어갈 JTextArea 생성
        gameLog.setLineWrap(true);
        gameLog.setWrapStyleWord(true);
        gameLog.setEditable(false);

// 반투명 배경 설정 (흰색, 투명도 150)
        gameLog.setBackground(new Color(30, 30, 30, 150));
        gameLog.setForeground(Color.WHITE); // 글자 색상
        gameLog.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        gameLog.setOpaque(true); // JTextArea의 기본 배경을 유지하면서 반투명 설정
        gameLog.setBorder(BorderFactory.createLineBorder(Color.BLACK));

// JTextArea 크기 설정
        gameLog.setPreferredSize(new Dimension(300, 350));

// 점수판 패널에 JTextArea 직접 추가
        scorePanel.add(gameLog, BorderLayout.CENTER);

// 배경 패널에 컴포넌트 추가
        backgroundPanel.add(infoPanel, BorderLayout.NORTH);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);
        backgroundPanel.add(scorePanel, BorderLayout.EAST);

        // 최종적으로 GameSelectionPanel에 배경 패널 추가
        add(backgroundPanel);
        return this;
    }

    private JPanel createGameComponent(String imagePath, JButton button,String text) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // 이미지 설정
        ImageIcon endIcon = new ImageIcon(new ImageIcon(imagePath+"_END.jpg").getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH));
        ImageIcon icon = new ImageIcon(new ImageIcon(imagePath+".jpg").getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(icon); // 초기 이미지는 icon
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 컴포넌트 추가
        panel.add(imageLabel);
        panel.add(Box.createVerticalStrut(10)); // 이미지와 버튼 사이 간격
        panel.add(button);

        // 버튼 클릭 시 이미지 변경 로직 추가
        button.addActionListener(e -> {
            imageLabel.setIcon(endIcon);
            button.setVisible(false);
            panel.revalidate();
            panel.repaint();
            mainApp.showScreen(text);
        });

        return panel;
    }

    private JButton setButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 배경 색상 설정
                if (getModel().isPressed()) {
                    g2.setColor(new Color(50, 50, 50)); // 클릭 시 어두운 회색
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(70, 70, 70)); // 마우스 오버 시 중간 회색
                } else {
                    g2.setColor(new Color(0, 0, 0)); // 기본 색상 (검정)
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // 테두리 색상 설정
                g2.setColor(new Color(212, 175, 55)); // 흰색 테두리
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                // 텍스트 페인팅은 부모 메서드에서 처리
                super.paintComponent(g);
            }

            @Override
            public void setContentAreaFilled(boolean b) {
                // 기본 배경 비활성화
            }

            @Override
            public void setBorderPainted(boolean b) {
                // 테두리 페인팅 비활성화
            }
        };

        // 버튼 속성 설정
        button.setOpaque(false); // 기존 배경 제거
        button.setBackground(new Color(20, 20, 20));
        button.setFocusPainted(false); // 선택 테두리 제거
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Helvetica Neue", Font.BOLD, 16)); // MacOS 스타일 폰트
        button.setForeground(Color.WHITE); // 텍스트 색상
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 손가락 커서

        return button;
    }




    private void updateInfoLabel() {
        infoLabel.setText("이름: " + MainApp.getUserName() + " 점수: " + MainApp.getUserScore());
        infoLabel.repaint(); // 즉각적인 텍스트 업데이트
        mainApp.updateScoreBord();
    }

    // 배경 이미지를 표시하는 사용자 정의 JPanel 클래스
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    public void appendToGameLog(String message) {
        SwingUtilities.invokeLater(() -> {
            gameLog.append(message + "\n");
            gameLog.setCaretPosition(gameLog.getDocument().getLength()); // 자동 스크롤
        });
    }
}
