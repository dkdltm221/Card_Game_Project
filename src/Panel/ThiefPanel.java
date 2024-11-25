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

public class ThiefPanel extends JPanel {
    private JTextArea displayArea;
    private Thief thief;
    private List<Card> computerCards;
    private List<Card> userCards;
    private List<JButton> computerButtons; // 컴퓨터 카드 버튼들
    private List<JButton> userButtons; // 유저 카드 버튼들
    private JPanel userPanel;
    private JPanel computerPanel;
    private MainApp mainApp;


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
        BackgroundPanel backgroundPanel = new BackgroundPanel("img/BackGround.png");
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);
        // 게임 메시지 출력 영역
        displayArea = new JTextArea();
        displayArea.setOpaque(false);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        //add(new JScrollPane(displayArea), BorderLayout.CENTER);
        // 게임 메시지 출력 영역을 배경 패널 위에 추가
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setOpaque(false); // 스크롤 투명 처리
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false); // 뷰포트도 투명 처리
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);
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
        computerPanel.setPreferredSize(new Dimension(800, 240));
        backgroundPanel.add(computerPanel, BorderLayout.NORTH);

        // 유저 카드 패널
        userPanel = new JPanel(null);
        userPanel.setOpaque(false);
        userPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        userPanel.setPreferredSize(new Dimension(800, 240));
        backgroundPanel.add(userPanel, BorderLayout.SOUTH);

        // 오른쪽 버튼 패널
        JPanel rightPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        JButton passTurnButton = new JButton("턴 넘기기");
        JButton removeComButton = new JButton("컴퓨터 카드제거");
        JButton takeCardButton = new JButton("카드 가져오기");
        JButton removePairButton = new JButton("같은 카드 제거");

        passTurnButton.addActionListener(e -> thief.passTurn());
        removeComButton.addActionListener(e -> thief.removeCom());
        takeCardButton.addActionListener(e -> thief.takeCard());
        removePairButton.addActionListener(e -> thief.removePair());

        rightPanel.add(passTurnButton);
        rightPanel.add(removeComButton);
        rightPanel.add(takeCardButton);
        rightPanel.add(removePairButton);
        rightPanel.setOpaque(false); // 투명 처리
        backgroundPanel.add(rightPanel, BorderLayout.EAST);

        // 카드 배분 애니메이션 실행
        dealCardsAnimation();
    }

    // 메시지 출력 메서드
    public void setText(String text) {
        displayArea.setText(text);
    }

    public void addText(String text) {
        displayArea.append(text + "\n");
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    // 카드 배분 애니메이션
    private void dealCardsAnimation() {
        Timer timer = new Timer(100, null); // 100ms 간격으로 실행
        final int[] index = {0}; // 현재 카드 인덱스
        final int cardHeight = 180;
        //뒷면사진 크기에 맞게 적용
        ImageIcon originalIcon = new ImageIcon("img/cards/CardDown.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(126, 180, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        timer.addActionListener(e -> {
            if (index[0] < 27) { // 컴퓨터 카드 배분 (27장)
                JButton button = new JButton(scaledIcon);
                button.setBounds(index[0] * 40, -cardHeight, 126, cardHeight); // 시작 위치: 패널 밖
                computerPanel.add(button);
                computerButtons.add(button); // 버튼 리스트에 추가
                button.setActionCommand(computerCards.get(index[0]).getName()); // 카드 이름을 ActionCommand에 설정, 버튼 지울때 사용

                Card currentCard = computerCards.get(index[0]);
                button.addActionListener(cardEvent -> handleComputerCardSelection(currentCard, button));


                // 애니메이션: 카드가 내려오는 효과
                Timer fallTimer = new Timer(30, null);
                fallTimer.addActionListener(fallEvent -> {
                    if (button.getY() < 30) {
                        button.setBounds(button.getX(), button.getY() + 10, 126, cardHeight);
                    } else {
                        fallTimer.stop(); // 떨어짐 완료
                    }
                });
                fallTimer.start();
            }
            if (index[0] < 26) { // 유저 카드 배분 (26장)
                ImageIcon userIcon = new ImageIcon("img/cards/"+userCards.get(index[0]).getName()+".png");
                Image userImage = userIcon.getImage().getScaledInstance(126, 180, Image.SCALE_SMOOTH);
                ImageIcon userdIcon = new ImageIcon(userImage);
                JButton button = new JButton(userdIcon); //userCards.get(index[0].getName())
                button.setBounds(index[0] * 40, -cardHeight, 126, cardHeight); // 시작 위치: 패널 밖
                userPanel.add(button);
                userButtons.add(button); // 버튼 리스트에 추가

                // 버튼에 클릭 이벤트 추가
                Card currentCard = userCards.get(index[0]);
                button.addActionListener(ev -> handleUserCardClick(currentCard, button)); // 변수 이름 'ev'로 변경

                // 애니메이션: 카드가 내려오는 효과
                Timer fallTimer = new Timer(30, null);
                fallTimer.addActionListener(fallEvent -> { // 변수 이름을 'fallEvent'로 변경
                    if (button.getY() < 30) {
                        button.setBounds(button.getX(), button.getY() + 10, 126, cardHeight);
                    } else {
                        fallTimer.stop(); // 떨어짐 완료
                    }
                });
                fallTimer.start();
            }

            index[0]++;
            computerPanel.revalidate();
            computerPanel.repaint();
            userPanel.revalidate();
            userPanel.repaint();

            if (index[0] >= 27) {
                timer.stop(); // 애니메이션 종료
            }
        });
        timer.start();
    }

    // 컴퓨터 덱에서 동일한 값의 카드 두 장을 제거
//    public void removePairButtons() {
//        // 선택된 카드 이름들 저장
//        List<String> selectedCardNames = new ArrayList<>();
//        for (Card card : thief.getSelectedCards()) {
//            selectedCardNames.add(card.getName());
//        }
//
//        // 선택된 버튼 삭제
//        for (int i = 0; i < userSelectButtons.size(); i++) {
//            JButton selectedButton = userSelectButtons.get(i);
//            if (selectedButton == null) continue;
//
//            // 선택된 버튼의 텍스트와 userButtons의 버튼 텍스트 비교
//            for (int j = 0; j < userButtons.size(); j++) {
//                JButton userButton = userButtons.get(j);
//
//                if (userButton != null && selectedButton.getText().equals(userButton.getText())) {
//                    // 일치하는 버튼을 리스트와 패널에서 제거
//                    userButtons.remove(j);
//                    userPanel.remove(userButton);
//                    break; // 한 번 찾으면 내부 루프 탈출
//                }
//            }
//        }
//
//        // UI 갱신
//        this.revalidate();
//        this.repaint();
//
//        // 선택된 카드 초기화
//        thief.clearSelectedCards();
//        userSelectButtons.clear(); // 선택된 버튼 리스트 초기화
//    }

    //같은 카드 자동으로 삭제
    public void removeCardAll() {
        // 컴퓨터 덱에서 value 값이 같은 두 장의 카드 찾기
        for (int i = 0; i < computerCards.size(); i++) {
            boolean pairFound = false; // 한 쌍을 찾았는지 여부
            for (int j = i + 1; j < computerCards.size(); j++) {
                if (computerCards.get(i).getValue() == computerCards.get(j).getValue()) {
                    // 동일한 value 값을 가진 카드 두 장을 즉시 삭제
                    Card card1 = computerCards.get(i);
                    Card card2 = computerCards.get(j);

                    // 버튼 및 덱에서 카드 삭제
                    removeComputerButton(card1);
                    removeComputerButton(card2);
                    computerCards.remove(j); // 주의: j 먼저 제거
                    computerCards.remove(i); // i 제거 (j가 먼저 제거되었으므로 안전)

                    addText("컴퓨터 덱에서 카드 제거: " + card1.getName() + ", " + card2.getName());

                    pairFound = true;
                    break; // 내부 루프 탈출
                }
            }
            if (pairFound) {
                break; // 외부 루프 탈출 후 다시 시작
            }
        }

        // UI 갱신
        this.revalidate();
        this.repaint();

        // 더 이상 동일한 카드가 없을 경우 메시지 추가
        if (computerCards.isEmpty()) {
            addText("컴퓨터 덱에 동일한 값을 가진 카드 두 장이 없습니다.");
            addText("당신 차례!");
        }
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
        if (userCards == null || userCards.isEmpty()) {
            addText("유저 카드가 없습니다!");
            checkUserCardsAndAlertVictory();
        }

        // 유저 카드 중 랜덤으로 하나를 선택
        int randomIndex = (int) (Math.random() * userCards.size());
        Card selectedCard = userCards.remove(randomIndex);

        // 선택한 카드를 컴퓨터 카드 덱에 추가
        computerCards.add(selectedCard);

        // 관련된 유저 버튼 삭제
        JButton buttonToRemove = userButtons.remove(randomIndex);
        if (buttonToRemove != null) {
            userPanel.remove(buttonToRemove);
        }

        // 컴퓨터 버튼 추가
        JButton newComputerButton = new JButton(" "); // 컴퓨터 카드는 내용 숨김
        newComputerButton.setActionCommand(selectedCard.getName());
        newComputerButton.addActionListener(e -> thief.computerCardClicked(computerButtons.size()));
        computerPanel.add(newComputerButton);
        computerButtons.add(newComputerButton);

        // UI 갱신
        this.revalidate();
        this.repaint();

        // 메시지 출력
        addText("유저 카드 '" + selectedCard.getName() + "'을(를) 컴퓨터가 가져갔습니다.");
    }

    //컴터 덱에서 유저 덱으로 카드와 버튼 이동
    public void takeCardFromCom() {
        if (computerCards == null || computerCards.isEmpty()) {
            addText("컴퓨터 카드가 없습니다!");
            checkComputerCardsAndAlertVictory(); // 유저 카드가 없는 경우 종료
        }
        if (computerButtons == null || computerButtons.isEmpty()) { //오류 찾기
            addText("컴퓨터 버튼 초기화되지 않았습니다!");
            return;
        }
        if (computerPanel == null) {    //오류 찾기
            addText("컴퓨터 패널이 오류!");
            return;
        }

        // 컴터 카드 중 랜덤으로 하나를 선택
        int randomIndex = (int) (Math.random() * computerCards.size());
        Card selectedCard = computerCards.remove(randomIndex);

        // 선택한 카드를 유저 카드 덱에 추가
        userCards.add(selectedCard);

        // 관련된 유저 버튼 삭제
        JButton buttonToRemove = computerButtons.remove(randomIndex);
        if (buttonToRemove != null) {
            computerPanel.remove(buttonToRemove);
        }

        // 유저 버튼 추가
        JButton newUserButton = new JButton(selectedCard.getName()); // 유저카드 버튼 추가
        newUserButton.addActionListener(e -> thief.userCardClicked(selectedCard));
        userPanel.add(newUserButton);
        userButtons.add(newUserButton);

        // UI 갱신
        this.revalidate();
        this.repaint();

        // 메시지 출력
        addText("컴퓨터 카드 '" + selectedCard.getName() + "'을(를) 유저가 가져갔습니다.");
    }

    public void removeUserCardAll() {
        for (int i = 0; i < userCards.size(); i++) {
            boolean pairFound = false; // 한 쌍을 찾았는지 여부
            for (int j = i + 1; j < userCards.size(); j++) {
                if (userCards.get(i).getValue() == userCards.get(j).getValue()) {
                    // 동일한 value 값을 가진 카드 두 장을 즉시 삭제
                    Card card1 = userCards.get(i);
                    Card card2 = userCards.get(j);

                    // 버튼 및 덱에서 카드 삭제
                    removeUserButton(card1);
                    removeUserButton(card2);
                    userCards.remove(j); // 주의: j 먼저 제거
                    userCards.remove(i); // i 제거 (j가 먼저 제거되었으므로 안전)

                    addText("유저 덱에서 카드 제거: " + card1.getName() + ", " + card2.getName());

                    pairFound = true;
                    break; // 내부 루프 탈출
                }
            }
            if (pairFound) {
                break; // 외부 루프 탈출 후 다시 시작
            }
        }

        // UI 갱신
        this.revalidate();
        this.repaint();

        // 더 이상 동일한 카드가 없을 경우 메시지 추가
        if (userCards.isEmpty()) {
            addText("유저 덱에 동일한 값을 가진 카드 두 장이 없습니다.");
            addText("턴넘기기! ");
        }
    }

    private void removeUserButton(Card card) {
        for (int i = 0; i < userButtons.size(); i++) {
            JButton button = userButtons.get(i);

            // 버튼의 텍스트가 카드의 이름과 일치하는지 확인
            if (button.getText().equals(card.getName())) {
                // 버튼을 패널에서 제거하고, 버튼 리스트에서도 제거
                userButtons.remove(i);
                userPanel.remove(button);
                break; // 한 번 찾으면 반복문을 종료
            }
        }

        // UI 갱신
        this.revalidate();
        this.repaint();
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

            // 확인 버튼 클릭 시 동작
            /*if (response == JOptionPane.OK_OPTION || response == JOptionPane.CLOSED_OPTION) {
                MainApp.updateScore(100);
                // mainApp.showScreen 호출
                mainApp.showScreen("GameSelection");
            }*/
            if (response == JOptionPane.OK_OPTION || response == JOptionPane.CLOSED_OPTION) {
                if (mainApp == null) {
                    System.err.println("mainApp이 null입니다. 제대로 초기화되었는지 확인하세요.");
                    return; // null일 경우 실행 중단
                }
                MainApp.updateScore(15);
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

            // 확인 버튼 클릭 시 동작
            //if (response == JOptionPane.OK_OPTION || response == JOptionPane.CLOSED_OPTION) {
            // mainApp.showScreen 호출
            //    mainApp.showScreen("GameSelection");
            //}
            if (response == JOptionPane.OK_OPTION || response == JOptionPane.CLOSED_OPTION) {
                if (mainApp == null) {
                    System.err.println("mainApp이 null입니다. 제대로 초기화되었는지 확인하세요.");
                    return; // null일 경우 실행 중단
                }
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

        // 제거 대상 카드 처리
        for (Card card : cardsToRemove) {
            computerCards.remove(card);
            removeComputerButton(card);
        }

        // 메시지 추가
        if (!cardsToRemove.isEmpty()) {
            addText("컴퓨터 덱에서 제거된 카드: " +
                    cardsToRemove.stream().map(Card::getName).reduce((a, b) -> a + ", " + b).orElse(""));
        } else {
            addText("컴퓨터 덱에 제거할 카드가 없습니다.");
        }

        // UI 갱신
        this.revalidate();
        this.repaint();
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
            addText("같은 카드를 두 번 선택할 수 없습니다. 다른 카드를 선택하세요.");
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
                addText("유저 카드 제거: " + firstCard.getName() + ", " + secondCard.getName());
            } else {
                // 다른 카드일 경우 선택 초기화
                addText("선택된 카드가 다릅니다. 다시 선택해주세요.");
                resetButtonPositions();
                clearSelectedUserCards();
            }
        }
    }


    // 선택된 유저 카드 제거
    private void removeSelectedUserCards() {
        for (int i = 0; i < selectedUserCards.size(); i++) {
            Card card = selectedUserCards.get(i);
            JButton button = selectedUserButtons.get(i);

            // 카드와 버튼을 삭제
            userCards.remove(card);
            userButtons.remove(button);
            userPanel.remove(button);
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
        int targetY = originalY - 10; // 올라갈 Y 좌표 (10px 위로)
        boolean isSelected = buttonSelected.getOrDefault(button, false); // 선택 여부 확인

        if (!isSelected) {
            // 올라가는 애니메이션
            Timer upTimer = new Timer(100, null); // 15ms 간격으로 실행
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
            final int targetY = originalY + 10; // 목표 위치 (10px 아래로)

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
            addText("먼저 컴퓨터 카드를 선택하세요.");
            return;
        }

        // 1. 선택한 카드를 컴퓨터 덱에서 제거
        computerCards.remove(selectedComputerCard);  // 컴퓨터 덱에서 카드 제거
        computerButtons.remove(selectedComputerButton); // 컴퓨터 버튼 리스트에서 제거
        computerPanel.remove(selectedComputerButton);  // 컴퓨터 패널에서 버튼 제거

        // 2. 선택한 카드를 유저 덱에 추가
        userCards.add(selectedComputerCard); // 유저 덱에 카드 추가

        // 3. 유저 패널에 버튼 추가
        ImageIcon userIcon = new ImageIcon("img/cards/"+selectedComputerCard.getName()+".png");
        Image userImage = userIcon.getImage().getScaledInstance(126, 180, Image.SCALE_SMOOTH);
        ImageIcon userdIcon = new ImageIcon(userImage);
        JButton userButton = new JButton(userdIcon);
        int xPosition = userButtons.size() * 40; // 기존 버튼 개수에 따라 위치 계산
        int yPosition = 30; // Y 좌표 고정
        userButton.setBounds(xPosition, yPosition, 126, 180); // 버튼 위치 설정
        userButton.addActionListener(e -> handleUserCardClick(selectedComputerCard, userButton)); // 클릭 이벤트 추가
        userButtons.add(userButton); // 유저 버튼 리스트에 추가
        userPanel.add(userButton);   // 유저 패널에 버튼 추가

        // 4. 선택 초기화
        selectedComputerCard = null;
        selectedComputerButton = null;

        // 5. UI 갱신
        userPanel.revalidate(); // 유저 패널 레이아웃 재배치
        userPanel.repaint();    // 유저 패널 다시 그리기
        computerPanel.revalidate(); // 컴퓨터 패널 레이아웃 재배치
        computerPanel.repaint();    // 컴퓨터 패널 다시 그리기

        // 6. 메시지 출력
        addText("컴퓨터 카드가 유저 덱으로 이동되었습니다.");
    }



}