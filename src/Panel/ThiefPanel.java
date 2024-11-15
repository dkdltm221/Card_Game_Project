package Panel;

import Card.Card;
import Card.ThiefCardFactory;
import Game.Thief;
import Main.MainApp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThiefPanel extends JPanel {
    private JTextArea displayArea;
    private Thief thief;
    private List<Card> computerCards;
    private List<Card> userCards;
    private List<JButton> userButtons;

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
            JButton button = new JButton(" ");
            computerPanel.add(button);
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
        for (Card card : thief.getSelectedCards()) {
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
}
