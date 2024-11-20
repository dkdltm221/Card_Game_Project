package Panel;

import Main.MainApp;

import javax.swing.*;
import java.awt.*;

public class GameSelectionPanel extends JPanel {
    private MainApp mainApp;
    private JLabel infoLabel;

    public static final String IMAGE_DIR = "img/cards/";

    public GameSelectionPanel(MainApp app) {
        this.mainApp = app;

        // 전체 레이아웃 설정
        setLayout(new BorderLayout());

        // 배경 패널 생성
        BackgroundPanel backgroundPanel = new BackgroundPanel(IMAGE_DIR + "background.jpg");
        backgroundPanel.setLayout(new BorderLayout());

        // 중앙의 이름과 점수 패널
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

                // 텍스트 그림자 효과
                g2.setColor(Color.BLACK);
                g2.drawString(getText(), 3, 3); // 그림자 오프셋
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
        gbc.insets = new Insets(50, 30, 50, 50); // 간격 설정
        gbc.fill = GridBagConstraints.CENTER;

        // 블랙잭 게임
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(createGameComponent("블랙잭", IMAGE_DIR + "BlackJack.jpg", e -> mainApp.showScreen("BlackjackPanel")), gbc);

        // 빙고 게임
        gbc.gridx = 1;
        centerPanel.add(createGameComponent("빙고", IMAGE_DIR + "Bingo.jpg", e -> mainApp.showScreen("BingoApp")), gbc);

        // 도둑잡기 게임
        gbc.gridx = 2;
        centerPanel.add(createGameComponent("도둑잡기", IMAGE_DIR + "Thief.jpg", e -> mainApp.showScreen("ThiefPanel")), gbc);

        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        JButton scoreboardButton = new JButton("점수판");
        scoreboardButton.setPreferredSize(new Dimension(100,20));
        southPanel.add(scoreboardButton);
        scoreboardButton.addActionListener(e -> mainApp.showScreen("Scoreboard"));

        // 배경 패널에 컴포넌트 추가
        backgroundPanel.add(infoPanel, BorderLayout.NORTH);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);
        backgroundPanel.add(southPanel, BorderLayout.SOUTH);

        // 최종적으로 GameSelectionPanel에 배경 패널 추가
        add(backgroundPanel);
    }

    // 게임 컴포넌트를 생성하는 메서드 (이미지 + 버튼)
    private JPanel createGameComponent(String text, String imagePath, java.awt.event.ActionListener actionListener) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // 이미지 설정
        ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 커스텀 버튼 생성
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(255, 215, 0)); // 클릭 시 황금 강조
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 223, 80)); // 마우스 오버 시 밝은 황금색
                } else {
                    g2.setColor(new Color(44, 62, 80)); // 기본 딥 블루
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2.setColor(new Color(212, 175, 55)); // 테두리 색상
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                super.paintComponent(g);
            }

            @Override
            public void setContentAreaFilled(boolean b) {
                // 기본 구현 비활성화
            }
        };
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("돋움", Font.BOLD, 18)); // 세련된 폰트
        button.setForeground(Color.WHITE); // 텍스트 색상
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addActionListener(actionListener);

        // 컴포넌트 추가
        panel.add(imageLabel);
        panel.add(Box.createVerticalStrut(10)); // 이미지와 버튼 사이 간격
        panel.add(button);

        return panel;
    }


    private void updateInfoLabel() {
        infoLabel.setText("이름: " + MainApp.getUserName() + " 점수: " + MainApp.getUserScore());
        infoLabel.repaint(); // 즉각적인 텍스트 업데이트
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
}