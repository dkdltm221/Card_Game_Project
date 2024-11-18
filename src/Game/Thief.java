package Game;

import Panel.ThiefPanel;
import Main.MainApp;

import java.util.ArrayList;
import java.util.List;
import Card.*;

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
        // 추가 로직 구현
    }

    // 유저 카드 클릭 처리
    public void userCardClicked(Card card) {
        panel.addText("유저 카드 " + card.getName() + " 클릭됨.");
//        if (card == null) {
//            // 선택 해제
//            if (!selectedCards.isEmpty()) {
//                Card removedCard = selectedCards.remove(selectedCards.size() - 1); // 마지막 선택 카드 제거
//                panel.addText(removedCard.getName() + " 선택이 해제되었습니다."); // 이름 출력
//            }
//        } else {
//            // 최대 2개의 카드만 선택할 수 있음
//            if (selectedCards.size() < 2) {
//                if (!selectedCards.contains(card)) {
//                    selectedCards.add(card);
//                    panel.addText("유저 카드 " + card.getName() + " 클릭됨.");
//                }
//            } else {
//                panel.addText("두 장의 카드만 선택할 수 있습니다.");
//            }
//        }
    }

    // 턴 넘기기
    public void passTurn() {
        panel.addText("턴을 넘겼습니다.");
        panel.takeCardFromUser();
    }

    // 상대방 카드 가져오기
    public void takeCard() {
        panel.addText("상대방의 카드를 가져왔습니다.");
        panel.takeCardFromCom();
    }

    // 같은 카드 2장 제거
    public void removePair() {
        panel.addText("유저 카드를 삭제합니다.");
        panel.removeUserCardAll();
//        if (selectedCards.size() == 2) {
//            Card card1 = selectedCards.get(0);
//            Card card2 = selectedCards.get(1);
//
//            if (card1.getValue() == card2.getValue()) {
//                panel.removePairButtons();
//                panel.addText("같은 카드 두 장을 제거했습니다: " + card1.getName() + "와 " + card2.getName());
//            } else {
//                panel.addText("선택된 카드가 서로 다릅니다.");
//            }
//        } else {
//            panel.addText("카드를 두 장 선택해야 제거할 수 있습니다.");
//        }
    }
    public List<Card> getSelectedCards() {
        return selectedCards;
    }
    // 선택된 카드 초기화
    public void clearSelectedCards() {
        selectedCards.clear();
    }


    public void removeCom() {
        panel.addText("컴퓨터 동일 카드 삭제!");
        panel.removeCardAll();
    }
}