package Game;

import Panel.ThiefPanel;
import Main.MainApp;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import Card.*;

import javax.swing.*;

public class Thief {
    private ThiefPanel panel;
    private MainApp mainApp;
    private List<Card> selectedCards;  // 선택된 카드 리스트

    public Thief(ThiefPanel panel, MainApp mainApp) {
        this.panel = panel;
        this.mainApp = mainApp;
        this.selectedCards = new ArrayList<>();
        initializeGame();
    }

    // 게임 초기화
    private void initializeGame() {
        panel.setText("도둑잡기 게임이 시작되었습니다!\n");
    }

    // 컴퓨터 카드 클릭 처리
    public void computerCardClicked(int index) {
        panel.addText("컴퓨터 카드 " + (index + 1) + " 클릭됨.");
    }

    // 유저 카드 클릭 처리
    public void userCardClicked(Card card) {
        if (card == null) {
            // 선택 해제
            if (!selectedCards.isEmpty()) {
                Card removedCard = selectedCards.remove(selectedCards.size() - 1); // 마지막 선택 카드 제거
                panel.addText(removedCard.getName() + " 선택이 해제되었습니다."); // 이름 출력
            }
        } else {
            // 최대 2개의 카드만 선택할 수 있음
            if (selectedCards.size() < 2) {
                if (!selectedCards.contains(card)) {
                    selectedCards.add(card);
                    panel.addText("유저 카드 " + card.getName() + " 클릭됨.");
                }
            } else {
                panel.addText("두 장의 카드만 선택할 수 있습니다.");
            }
        }
    }

    // 턴 넘기기
    public void passTurnToComputer() {
        panel.addText("컴퓨터에게 턴을 넘겼습니다.");
        panel.addText("컴퓨터 pair 카드 삭제");
        panel.removeCardAll();
        panel.addText("당신의 카드를 뽑겠습니다.");
        takeCardFromUser();
        panel.addText("이제 당신의 턴입니다.");
    }

    // 상대방 카드 가져오기
    public void takeCardFromUser() {
            if (!panel.getUserCards().isEmpty()) {
                // 랜덤으로 유저 카드 선택
                int randomIndex = (int) (Math.random() * panel.getUserCards().size());
                Card takenCard = panel.getUserCards().remove(randomIndex);
                panel.getComputerCards().add(takenCard);

                // UI 갱신
                panel.removeUserButton(randomIndex); // 유저 버튼 제거
                panel.addComputerButton(takenCard); // 컴퓨터 버튼 추가

                panel.addText("컴퓨터가 유저 카드 " + takenCard.getName() + "를 가져갔습니다.");
            } else {
                panel.addText("유저에게 가져올 카드가 없습니다.");
            }
    }
    public void takeCardFromCom() {
        if (!panel.getComputerCards().isEmpty()) {
            // 랜덤으로 유저 카드 선택
            int comIndex = (int) (Math.random() * panel.getComputerCards().size());
            Card takenCard = panel.getUserCards().remove(comIndex);
            panel.getUserCards().add(takenCard);

            // UI 갱신
            panel.removeUserButton(comIndex); // 유저 버튼 제거
            panel.addUserButton(takenCard); // 컴퓨터 버튼 추가

            panel.addText("유저가 컴퓨터 카드 " + takenCard.getName() + "를 가져갔습니다.");
        } else {
            panel.addText("컴퓨터에게 가져올 카드가 없습니다.");
        }
    }

    // 같은 카드 2장 제거
    public void removePair() {
        if (selectedCards.size() == 2) {
            Card card1 = selectedCards.get(0);
            Card card2 = selectedCards.get(1);

            if (card1.getValue() == card2.getValue()) {
                panel.removePairButtons();
                panel.addText("같은 카드 두 장을 제거했습니다: " + card1.getName() + "와 " + card2.getName());
            } else {
                panel.addText("선택된 카드가 서로 다릅니다.");
            }
        } else {
            panel.addText("카드를 두 장 선택해야 제거할 수 있습니다.");
        }
    }
    public List<Card> getSelectedCards() {
        return selectedCards;
    }
    // 선택된 카드 초기화
    public void clearSelectedCards() {
        selectedCards.clear();
    }

}
