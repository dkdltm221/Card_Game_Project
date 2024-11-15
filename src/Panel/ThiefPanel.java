package Panel;

import Card.Card;
import Card.ThiefCardFactory;
import Game.Thief;
import Main.MainApp;

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
    private List<JButton> userButtons;      //버튼 클릭 시 담아줄 리스트
    private List<JButton> computerButtons = new ArrayList<>(); //컴퓨터 버튼들

    public ThiefPanel(MainApp mainApp) {
        // 레이아웃 설정
        setLayout(new BorderLayout());

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
        userButtons = new ArrayList<>();
        for (int i = 0; i < 27; i++) {
            computerCards.add(deck.remove(0)); //덱에 담긴 카드 하나씩 삭제해서 넣어줌 ㅇㅅㅇa
        }
        for (int i = 0; i < 26; i++) {
            userCards.add(deck.remove(0));    // 유저 카드는 26장
        }

        // 컴퓨터 카드 패널
        JPanel computerPanel = new JPanel(new GridLayout(3, 9, 5, 5));
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
        JPanel userPanel = new JPanel(new GridLayout(3, 9, 5, 5));
        for (int i = 0; i < userCards.size(); i++) {
            int index = i;
            JButton button = new JButton(userCards.get(index).getName()); // 카드 이름 표시
            userPanel.add(button);
            button.addActionListener(e -> {
                if (button.getBackground() == Color.YELLOW) {
                    // 이미 선택된 카드인 경우
                    button.setBackground(null);
                    thief.userCardClicked(null);
                } else {
                    // 선택되지 않은 카드인 경우 선택
                    button.setBackground(Color.YELLOW);
                    userButtons.add(button);
                    thief.userCardClicked(userCards.get(index));
                }
            });
        }
        add(userPanel, BorderLayout.SOUTH);

        // 오른쪽 버튼 패널
        JPanel rightPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton passTurnButton = new JButton("턴 넘기기");
        JButton takeCardButton = new JButton("카드 가져오기");
        JButton removePairButton = new JButton("같은 카드 제거");

        passTurnButton.addActionListener(e -> thief.passTurn());
        takeCardButton.addActionListener(e -> thief.takeCard());
        removePairButton.addActionListener(e -> thief.removePair());

        rightPanel.add(passTurnButton);
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

    public void removePairButtons() {
        List<String> selectedCardNames = new ArrayList<>();
        for (Card card : thief.getSelectedCards()) {    //선택된 카드를 selectedCardNames에 넣어주는 과정
            selectedCardNames.add(card.getName());
        }

        //일치하는 버튼 제거
        for (int i = 0; i < userButtons.size(); i++) {
            JButton button = userButtons.get(i);
            if (button == null) continue; // 널 방어..?
            if (selectedCardNames.contains(button.getText())) {
                userButtons.remove(i);
                button.getParent().remove(button);
                i--;
            }
        }
        this.revalidate();
        this.repaint();

        // 선택된 카드 초기화
        thief.clearSelectedCards();
    }

    //같은 카드 자동으로 삭제
    public void removeCardAll() {
        List<Card> cardsToRemove = new ArrayList<>();

        // 컴퓨터 덱에서 value 값이 같은 두 장의 카드 찾기
        for (int i = 0; i < computerCards.size(); i++) {
            for (int j = i + 1; j < computerCards.size(); j++) {
                if (computerCards.get(i).getValue() == computerCards.get(j).getValue()) {
                    // 동일한 value 값을 가진 카드 두 장을 리스트에 추가
                    cardsToRemove.add(computerCards.get(i));
                    cardsToRemove.add(computerCards.get(j));
                    break; // 일치하는 쌍을 찾으면 더 이상 내부 루프를 돌 필요 없음
                }
            }
        }

        // 찾은 카드 제거
        for (Card card : cardsToRemove) {
            computerCards.remove(card);
            removeComputerButton(card);
        }

        // 메시지 추가
        if (!cardsToRemove.isEmpty()) {
            addText("컴퓨터 덱에서 카드 제거: " +
                    cardsToRemove.stream().map(Card::getName).reduce((a, b) -> a + ", " + b).orElse(""));
        } else {
            addText("컴퓨터 덱에 동일한 값을 가진 카드 두 장이 없습니다.");
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
}

