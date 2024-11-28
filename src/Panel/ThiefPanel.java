package Panel;

import Card.Card;
import Card.ThiefCardFactory;
import Game.Thief;
import Main.MainApp;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import Panel.GameSelectionPanel;

public class ThiefPanel extends JPanel {
    private Thief thief;
    private List<Card> computerCards;
    private List<Card> userCards;
    private List<JButton> computerButtons; // 컴퓨터 카드 버튼들
    private List<JButton> userButtons; // 유저 카드 버튼들
    private JPanel userPanel;
    private JPanel computerPanel;
    private MainApp mainApp;
    private JPanel centerPanel;
    JButton passTurnButton;
    JButton takeCardButton;
    GameSelectionPanel gameSelectionPanel= GameSelectionPanel.getInstance();

    // 배경 이미지를 표시하는 사용자 정의 JPanel 클래스
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public ThiefPanel(MainApp mainApp) {
        setLayout(new BorderLayout());
        this.mainApp = mainApp;
        //배경사진
        BackgroundPanel backgroundPanel = new BackgroundPanel("img/BlackJackBackground.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);
        centerPanel = new JPanel();
        centerPanel.setLayout(null);
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0,0,0,0));
        centerPanel.setPreferredSize(new Dimension(600,190));

        backgroundPanel.add(centerPanel, BorderLayout.CENTER);
        // 도둑잡기 게임 클래스 초기화
        thief = new Thief(this, mainApp);

        // 카드 덱 초기화
        ThiefCardFactory cardFactory = new ThiefCardFactory();
        List<Card> deck = cardFactory.createCards();
        Collections.shuffle(deck);

        // 컴퓨터와 유저에게 카드를 분배
        computerCards = new ArrayList<>();
        userCards = new ArrayList<>();
        computerButtons = new ArrayList<>();
        userButtons = new ArrayList<>();

        for (int i = 0; i < 27; i++) {
            computerCards.add(deck.remove(0));
        }
        for (int i = 0; i < 26; i++) {
            userCards.add(deck.remove(0));
        }

        // 컴퓨터 카드 패널
        computerPanel = new JPanel(null);
        computerPanel.setOpaque(false);
        computerPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        computerPanel.setPreferredSize(new Dimension(800, 220));
        backgroundPanel.add(computerPanel, BorderLayout.NORTH);

        // 유저 카드 패널
        userPanel = new JPanel(null);
        userPanel.setOpaque(false);
        userPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        userPanel.setPreferredSize(new Dimension(800, 220));
        backgroundPanel.add(userPanel, BorderLayout.SOUTH);

        // 오른쪽 버튼 패널
        JPanel rightPanel = new JPanel(null); // 절대 레이아웃 사용
        rightPanel.setPreferredSize(new Dimension(150, 220)); // 패널 크기 설정

// 버튼 크기
        int buttonWidth = 104;
        int buttonHeight = 42;
        int gap = 20; // 버튼 사이 간격

// 첫 번째 버튼 (Pass Turn)
        ImageIcon originalDoneIcon = new ImageIcon("img/pass.png");
        Image scaledDoneImage = originalDoneIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledDoneIcon = new ImageIcon(scaledDoneImage);
        passTurnButton = new JButton(scaledDoneIcon);

// 두 번째 버튼 (Take Card)
        ImageIcon originalPlayIcon = new ImageIcon("img/draw.png");
        Image scaledPlayImage = originalPlayIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledPlayIcon = new ImageIcon(scaledPlayImage);
        takeCardButton = new JButton(scaledPlayIcon);

// 오른쪽 패널 크기 가져오기
        int panelWidth = 150; // rightPanel의 너비
        int panelHeight = 220; // rightPanel의 높이

// 중앙 정렬 계산
        int centerX = (panelWidth - buttonWidth) / 2; // 수평 중앙
        int totalHeight = (2 * buttonHeight) + gap; // 버튼 전체 높이 (간격 포함)
        int startY = (panelHeight - totalHeight) / 2; // 수직 중앙 시작 위치

// 버튼 위치 설정
        passTurnButton.setBounds(centerX, startY, buttonWidth, buttonHeight);
        takeCardButton.setBounds(centerX, startY + buttonHeight + gap, buttonWidth, buttonHeight);

        // 초기 상태: passTurn 버튼 비활성화
        passTurnButton.setEnabled(false);

        takeCardButton.addActionListener(e -> handleTakeCardAction()); // takeCard 처리
        passTurnButton.addActionListener(e -> handlePassTurnAction()); // passTurn 처리


        rightPanel.add(passTurnButton);
        rightPanel.add(takeCardButton);
        rightPanel.setOpaque(false); // 투명 처리
        backgroundPanel.add(rightPanel, BorderLayout.EAST);

        // 카드 배분 애니메이션 실행
        dealCardsAnimation();
    }

    // 카드 배분 애니메이션
    private void dealCardsAnimation() {
        Timer timer = new Timer(100, null); // 100ms 간격으로 실행
        final int[] index = {0}; // 현재 카드 인덱스
        final int cardHeight = 180;

        Collections.shuffle(userCards);    //카드 섞어주기
        Collections.shuffle(computerCards);

        ImageIcon originalIcon = new ImageIcon("img/cards/CardDown.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(126, 180, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        timer.addActionListener(e -> {
            // 컴퓨터 카드 애니메이션
            if (index[0] < computerCards.size()) {
                Card currentCard = computerCards.get(index[0]);
                JButton button = new JButton(scaledIcon);
                button.setBounds(index[0] * 40, -cardHeight, 126, cardHeight);
                button.setActionCommand(currentCard.getName());
                button.addActionListener(cardEvent -> handleComputerCardSelection(currentCard, button));
                computerPanel.add(button);
                computerButtons.add(button);

                // 애니메이션 효과
                Timer fallTimer = new Timer(30, null);
                fallTimer.addActionListener(fallEvent -> {
                    if (button.getY() < 30) {
                        button.setBounds(button.getX(), button.getY() + 10, 126, cardHeight);
                    } else {
                        fallTimer.stop();
                    }
                });
                fallTimer.start();
            }

            // 유저 카드 애니메이션
            if (index[0] < userCards.size()) {
                Card currentCard = userCards.get(index[0]);
                ImageIcon userIcon = new ImageIcon("img/cards/" + currentCard.getName() + ".png");
                Image userImage = userIcon.getImage().getScaledInstance(126, 180, Image.SCALE_SMOOTH);
                ImageIcon userdIcon = new ImageIcon(userImage);
                JButton button = new JButton(userdIcon);
                button.setBounds(index[0] * 40, -cardHeight, 126, cardHeight);
                button.addActionListener(ev -> handleUserCardClick(currentCard, button));
                userPanel.add(button);
                userButtons.add(button);

                // 애니메이션 효과
                Timer fallTimer = new Timer(30, null);
                fallTimer.addActionListener(fallEvent -> {
                    if (button.getY() < 30) {
                        button.setBounds(button.getX(), button.getY() + 10, 126, cardHeight);
                    } else {
                        fallTimer.stop();
                    }
                });
                fallTimer.start();
            }

            index[0]++;
            computerPanel.revalidate();
            computerPanel.repaint();
            userPanel.revalidate();
            userPanel.repaint();

            if (index[0] >= Math.max(computerCards.size(), userCards.size())) {
                timer.stop(); // 애니메이션 종료
            }
        });
        timer.start();
    }


    //컴퓨터 버튼 삭제
    private void removeComputerButton(Card card) {
        for (int i = 0; i < computerButtons.size(); i++) {
            JButton button = computerButtons.get(i);

            // 버튼의 이름과 카드 이름이 일치하는지 확인
            if (button.getActionCommand().equals(card.getName())) {
                // 버튼을 패널에서 제거하고, 버튼 리스트에서도 제거
                computerButtons.remove(i); // 리스트에서 버튼 제거
                computerPanel.remove(button); // 패널에서 버튼 제거
                break; // 일치하는 버튼을 찾으면 탈출
            }
        }

        // UI 갱신
        this.revalidate();
        this.repaint();
    }

    // 유저 덱에서 컴퓨터 덱으로 카드와 버튼 이동
    public void takeCardFromUser() {
        // 유저 카드 중 랜덤으로 하나를 선택
        int randomIndex = (int) (Math.random() * userCards.size());
        Card selectedCard = userCards.remove(randomIndex);

        // 선택한 카드를 컴퓨터 카드 덱에 추가
        computerCards.add(selectedCard);

        //  기존 레이아웃 초기화
        resetGameLayout();

        //  카드를 새로 분배
        dealCardsAnimation();
    }
    public void checkUserCardsAndAlertVictory() {
        if (userCards.isEmpty()) {
            // 알림 창 생성
            int response = JOptionPane.showOptionDialog(
                    this,
                    "승리!",
                    "알림",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"확인"},
                    "확인"
            );
            if (response == JOptionPane.OK_OPTION || response == JOptionPane.CLOSED_OPTION) {
                if (mainApp == null) {
                    System.err.println("mainApp이 null입니다. 제대로 초기화되었는지 확인하세요.");
                    return; // null일 경우 실행 중단
                }
                MainApp.updateScore(100);
                gameSelectionPanel.appendToGameLog("----도둑잡기----\n 승리 ! 도둑잡기에서 획득한 점수: +100점\n");
                mainApp.showScreen("GameSelection");
            }
        }
    }

    public void checkComputerCardsAndAlertVictory() {
        if (computerCards.isEmpty()) {
            // 알림 창 생성
            int response = JOptionPane.showOptionDialog(
                    this,
                    "패배!",
                    "알림",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"확인"},
                    "확인"
            );
            if (response == JOptionPane.OK_OPTION || response == JOptionPane.CLOSED_OPTION) {
                if (mainApp == null) {
                    System.err.println("mainApp이 null입니다. 제대로 초기화되었는지 확인하세요.");
                    return; // null일 경우 실행 중단
                }
                MainApp.updateScore(-100);
                gameSelectionPanel.appendToGameLog("----도둑잡기----\n 패배! 도둑잡기에서 획득한 점수: -100점\n");
                mainApp.showScreen("GameSelection");
            }
        }
    }

    public void removeComputerCardAll() {
        // 컴퓨터 덱에서 value 값이 같은 두 장의 카드 찾기
        HashMap<Integer, List<Card>> groupedCards = new HashMap<>();
        for (Card card : computerCards) {
            groupedCards.computeIfAbsent(card.getValue(), k -> new ArrayList<>()).add(card); //같은 value를 가진 카드끼리 그룹화
            //omputeIfAbsent는 지정된 키(card.getValue())가 맵에 없는 경우 실행, 새 리스트 생성
        }

        List<Card> cardsToRemove = new ArrayList<>();
        // 그룹별로 처리
        for (List<Card> group : groupedCards.values()) {
            if (group.size() == 2 || group.size() == 4) {
                // 그룹 크기가 2나 4이면 모두 제거
                cardsToRemove.addAll(group);
            } else if (group.size() == 3) {
                // 그룹 크기가 3이면 2개만 제거
                cardsToRemove.add(group.get(0));
                cardsToRemove.add(group.get(1));
            }
        }
        // cardsToRemove 리스트를 centerPanel에 표시
        if (!cardsToRemove.isEmpty()) {
            showCardsInCenterPanel(cardsToRemove.get(0), cardsToRemove.get(1));
        }
        // 제거 대상 카드 처리
        for (Card card : cardsToRemove) {
            computerCards.remove(card);
            removeComputerButton(card);
        }
        // UI 갱신
        this.revalidate();
        this.repaint();

        if(computerCards.isEmpty()){
            checkComputerCardsAndAlertVictory();
        }
    }
    // CenterPanel에 카드 버튼 표시
    private void showCardsInCenterPanel(Card card1, Card card2) {
        centerPanel.removeAll(); // 기존 컴포넌트 제거
        centerPanel.setLayout(null);

        // 버튼 크기
        int buttonWidth = 126;
        int buttonHeight = 180;

        // 첫 번째 카드 버튼 생성
        JButton cardButton1 = createCardButton(card1);
        cardButton1.setSize(buttonWidth, buttonHeight);

        // 두 번째 카드 버튼 생성
        JButton cardButton2 = createCardButton(card2);
        cardButton2.setSize(buttonWidth, buttonHeight);

        // 패널 크기 가져오기
        int panelWidth = centerPanel.getWidth();
        int panelHeight = centerPanel.getHeight();

        // 버튼 간 간격 (예: 20px)
        int gap = 20;

        // 첫 번째 버튼 위치 계산 (중앙 기준 좌측)
        int x1 = (panelWidth / 2) - buttonWidth - (gap / 2);
        int y1 = (panelHeight / 2) - (buttonHeight / 2);

        // 두 번째 버튼 위치 계산 (중앙 기준 우측)
        int x2 = (panelWidth / 2) + (gap / 2);
        int y2 = y1;

        // 버튼 위치 설정
        cardButton1.setLocation(x1, y1);
        cardButton2.setLocation(x2, y2);

        // 버튼을 패널에 추가
        centerPanel.add(cardButton1);
        centerPanel.add(cardButton2);

        // 패널 갱신
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    private JButton createCardButton(Card card) {
        ImageIcon cardIcon = new ImageIcon("img/cards/" + card.getName() + ".png");
        Image scaledImage = cardIcon.getImage().getScaledInstance(126, 180, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JButton cardButton = new JButton(scaledIcon);
        cardButton.setPreferredSize(new Dimension(126, 180)); // 버튼 크기 설정
        return cardButton;
    }

    private List<Card> selectedUserCards = new ArrayList<>();
    private List<JButton> selectedUserButtons = new ArrayList<>();

    // 유저 카드 클릭 핸들러
    private void handleUserCardClick(Card card, JButton button) {
        // 이미 선택된 카드인지 확인
        if (selectedUserCards == null || selectedUserButtons == null) {
            selectedUserCards = new ArrayList<>(); // 리스트가 null인 경우 초기화
            selectedUserButtons = new ArrayList<>();
        }
        if (selectedUserCards.contains(card)) {
            resetButtonPositions();
            clearSelectedUserCards();
            return; // 중복 선택 방지
        }

        // 선택된 카드와 버튼 추가
        selectedUserCards.add(card);
        selectedUserButtons.add(button);

        // 버튼 클릭 애니메이션 실행
        animateButtonClick(button);

        // 두 개의 카드를 선택했는지 확인
        if (selectedUserCards.size() == 2) {
            Card firstCard = selectedUserCards.get(0);
            Card secondCard = selectedUserCards.get(1);

            // 같은 value인지 비교
            if (firstCard.getValue() == secondCard.getValue()) {
                // 같은 카드일 경우 제거
                removeSelectedUserCards();
                if(userCards.isEmpty()){
                    checkUserCardsAndAlertVictory();
                }
            } else {
                // 다른 카드일 경우 선택 초기화
                resetButtonPositions();
                clearSelectedUserCards();
            }
        }
    }


    // 선택된 유저 카드 제거
    private void removeSelectedUserCards() {
        List<Card> cardsToRemove = new ArrayList<>();

        for (int i = 0; i < selectedUserCards.size(); i++) {
            Card card = selectedUserCards.get(i);
            JButton button = selectedUserButtons.get(i);

            // 카드와 버튼을 삭제
            cardsToRemove.add(card);
            userCards.remove(card);
            userButtons.remove(button);
            userPanel.remove(button);
        }

        // cardsToRemove 리스트를 centerPanel에 표시
        if (!cardsToRemove.isEmpty()) {
            showCardsInCenterPanel(cardsToRemove.get(0), cardsToRemove.get(1));
        }
        // 선택 초기화
        clearSelectedUserCards();

        // UI 갱신
        this.revalidate();
        this.repaint();


    }

    // 선택 초기화
    private void clearSelectedUserCards() {
        selectedUserCards.clear();
        selectedUserButtons.clear();
    }

    private Map<JButton, Boolean> buttonSelected = new HashMap<>(); // 버튼 상태 추적

    private void animateButtonClick(JButton button) {
        int originalY = button.getY(); // 버튼의 원래 Y 좌표
        int targetY = originalY - 30; // 올라갈 Y 좌표 (30px 위로)
        boolean isSelected = buttonSelected.getOrDefault(button, false); // 선택 여부 확인

        if (!isSelected) {
            // 올라가는 애니메이션
            Timer upTimer = new Timer(15, null); // 15ms 간격으로 실행
            upTimer.addActionListener(e -> {
                if (button.getY() > targetY) {
                    button.setBounds(button.getX(), button.getY() - 2, button.getWidth(), button.getHeight());
                } else {
                    upTimer.stop(); // 목표 위치에 도달하면 중지
                    buttonSelected.put(button, true); // 상태 업데이트 (선택됨)
                }
            });
            upTimer.start(); // 올라가기 시작
        }
    }

    private void resetButtonPositions() {
        for (int i = 0; i < selectedUserButtons.size(); i++) {
            JButton button = selectedUserButtons.get(i);

            // 애니메이션: 버튼을 원래 위치로 되돌리기
            final int originalY = button.getY(); // 버튼의 원래 Y 좌표
            final int targetY = originalY + 30; // 목표 위치 (30px 아래로)

            Timer downTimer = new Timer(15, null);
            downTimer.addActionListener(e -> {
                // 버튼을 천천히 원래 위치로 되돌리기
                if (button.getY() < targetY) {
                    button.setLocation(button.getX(), button.getY() + 2); // 위치 조정
                } else {
                    downTimer.stop(); // 목표 위치에 도달하면 중지
                    // 애니메이션이 끝났을 때 상태 초기화
                    buttonSelected.put(button, false); // 상태 초기화
                }
            });
            downTimer.start();
        }
    }
    private Card selectedComputerCard = null;
    private JButton selectedComputerButton = null;

    private void handleComputerCardSelection(Card card, JButton button) {
        if (selectedComputerButton != null) {
            // 이전에 선택된 버튼이 있다면 기본 상태로 복구
            selectedComputerButton.setBorder(null);
        }

        // 선택한 카드와 버튼 저장
        selectedComputerCard = card;
        selectedComputerButton = button;

        // 선택된 버튼 강조 표시
        button.setBorder(BorderFactory.createLineBorder(Color.RED, 3));

    }

    public void takeSelectedComputerCard() {
        if (selectedComputerCard == null || selectedComputerButton == null) {
            return;
        }

        // 1. 선택한 카드를 컴퓨터 덱에서 제거
        computerCards.remove(selectedComputerCard);  // 컴퓨터 덱에서 카드 제거

        // 2. 선택한 카드를 유저 덱에 추가
        userCards.add(selectedComputerCard); // 유저 덱에 카드 추가

        // 4. 선택 초기화
        selectedComputerCard = null;
        selectedComputerButton = null;

        // 5. 기존 레이아웃 초기화
        resetGameLayout();

        // 6. 카드를 새로 분배
        dealCardsAnimation();
    }

    private void resetGameLayout() {
        // 패널에서 모든 버튼 제거
        computerPanel.removeAll();
        userPanel.removeAll();
        //UI 갱신
        computerPanel.revalidate();
        computerPanel.repaint();
        userPanel.revalidate();
        userPanel.repaint();
    }
    private boolean takeCardClicked = false; // takeCard 버튼 클릭 상태를 추적
    private void handleTakeCardAction() {
        if (selectedComputerCard == null) {
            JOptionPane.showMessageDialog(this, "선택된 카드가 없습니다.", "경고", JOptionPane.WARNING_MESSAGE);
            return; // 선택된 카드가 없으면 아무 동작도 하지 않음
        }
        // Thief 클래스의 takeCard 호출
        thief.takeCard();

        // 버튼 상태 변경
        takeCardClicked = true; // takeCard가 클릭됨
        passTurnButton.setEnabled(true); // passTurn 활성화
        takeCardButton.setEnabled(false); // takeCard 비활성화
    }
    private void handlePassTurnAction() {
        // Thief 클래스의 passTurn 호출
        thief.passTurn();

        // 버튼 상태 변경
        takeCardClicked = false; // takeCard 상태 초기화
        passTurnButton.setEnabled(false); // passTurn 비활성화
        takeCardButton.setEnabled(true); // takeCard 활성화
    }

}
