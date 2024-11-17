package Panel;

import Card.Card;
import Card.ThiefCardFactory;
import Game.Thief;
import Main.MainApp;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ThiefPanel extends JPanel {
    private JTextArea displayArea;
    private Thief thief;
    private List<Card> computerCards;    //컴퓨터 카드덱
    private List<Card> userCards;       //유저 카드덱
    private List<JButton> userSelectButtons;//버튼 클릭 시 담아줄 리스트
    private List<JButton> userButtons;//유저 버튼들
    private List<JButton> computerButtons = new ArrayList<>(); //컴퓨터 버튼들
    private JPanel userPanel; // 유저 패널
    private JPanel computerPanel; // 컴퓨터 패널
    private MainApp mainApp;

    public ThiefPanel(MainApp mainApp) {
        // 레이아웃 설정
        setLayout(new BorderLayout());
        this.mainApp = mainApp;
        // 게임 메시지 출력 영역
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        // 도둑잡기 게임 클래스 초기화
        thief = new Thief(this, mainApp);

        // 카드 덱 초기화
        ThiefCardFactory cardFactory = new ThiefCardFactory();
        List<Card> deck = cardFactory.createCards();
        Collections.shuffle(deck); // 카드 섞기

        // 컴퓨터와 유저에게 카드를 분배
        computerCards = new ArrayList<>();
        userCards = new ArrayList<>();
        userButtons=new ArrayList<>();
        userSelectButtons = new ArrayList<>();
        for (int i = 0; i < 27; i++) {
            computerCards.add(deck.remove(0)); //덱에 담긴 카드 하나씩 삭제해서 넣어줌 ㅇㅅㅇa
        }
        for (int i = 0; i < 26; i++) {
            userCards.add(deck.remove(0));    // 유저 카드는 26장
        }

        // 컴퓨터 카드 패널
        computerPanel = new JPanel(new GridLayout(3, 9, 5, 5));
        for (int i = 0; i < computerCards.size(); i++) {
            int index = i;
            JButton button = new JButton(" ");  //컴퓨터는 카드값 안보여주게 설정
            button.setActionCommand(computerCards.get(index).getName()); // 카드 이름을 ActionCommand에 설정, 버튼 지울때 사용
            computerPanel.add(button);
            computerButtons.add(button);
            button.addActionListener(e -> thief.computerCardClicked(index)); // 컴터 카드는 뭔지 안나오게 설정
        }
        add(computerPanel, BorderLayout.NORTH);

        // 유저 카드 패널
        userPanel = new JPanel(new GridLayout(3, 9, 5, 5));
        for (int i = 0; i < userCards.size(); i++) {
            int index = i;
            JButton button = new JButton(userCards.get(index).getName()); // 카드 이름 표시
            userPanel.add(button);
            userButtons.add(button);
            button.addActionListener(e -> { thief.userCardClicked(userCards.get(index));
//                if (button.getBackground() == Color.YELLOW) {
//                    // 이미 선택된 카드인 경우
//                    button.setBackground(null);
//                    thief.userCardClicked(null);
//                } else {
//                    // 선택되지 않은 카드인 경우 선택
//                    button.setBackground(Color.YELLOW);
//                    userSelectButtons.add(button);
//                    thief.userCardClicked(userCards.get(index));
//                }
            });

        }
        add(userPanel, BorderLayout.SOUTH);

        // 오른쪽 버튼 패널
        JPanel rightPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        JButton passTurnButton = new JButton("턴 넘기기");
        JButton removeComButton = new JButton("컴퓨터 카드제거");
        JButton takeCardButton = new JButton("카드 가져오기");
        JButton removePairButton = new JButton("같은 카드 제거");

        passTurnButton.addActionListener(e -> thief.passTurn());
        removeComButton.addActionListener(e->thief.removeCom());
        takeCardButton.addActionListener(e -> thief.takeCard());
        removePairButton.addActionListener(e -> thief.removePair());

        rightPanel.add(passTurnButton);
        rightPanel.add(removeComButton);
        rightPanel.add(takeCardButton);
        rightPanel.add(removePairButton);
        add(rightPanel, BorderLayout.EAST);
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

    public void removePairButtons() {
        // 선택된 카드 이름들 저장
        List<String> selectedCardNames = new ArrayList<>();
        for (Card card : thief.getSelectedCards()) {
            selectedCardNames.add(card.getName());
        }

        // 선택된 버튼 삭제
        for (int i = 0; i < userSelectButtons.size(); i++) {
            JButton selectedButton = userSelectButtons.get(i);
            if (selectedButton == null) continue;

            // 선택된 버튼의 텍스트와 userButtons의 버튼 텍스트 비교
            for (int j = 0; j < userButtons.size(); j++) {
                JButton userButton = userButtons.get(j);

                if (userButton != null && selectedButton.getText().equals(userButton.getText())) {
                    // 일치하는 버튼을 리스트와 패널에서 제거
                    userButtons.remove(j);
                    userPanel.remove(userButton);
                    break; // 한 번 찾으면 내부 루프 탈출
                }
            }
        }

        // UI 갱신
        this.revalidate();
        this.repaint();

        // 선택된 카드 초기화
        thief.clearSelectedCards();
        userSelectButtons.clear(); // 선택된 버튼 리스트 초기화
    }

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

            // ActionCommand와 카드 이름을 비교
            if (button.getActionCommand().equals(card.getName())) {
                computerButtons.remove(i); // 리스트에서 버튼 제거
                button.getParent().remove(button); // 버튼 제거
                break;
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
                MainApp.updateScore(100);
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
}

