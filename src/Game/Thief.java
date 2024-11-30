package Game;

import Panel.ThiefPanel;
import Main.MainApp;


public class Thief {
    private ThiefPanel panel;
    private MainApp mainApp;

    public Thief(ThiefPanel panel, MainApp mainApp) {
        this.panel = panel;
        this.mainApp = mainApp;
    }
    // 턴 넘기기
    public void passTurn() {
        panel.takeCardFromUser();
        panel.removeComputerCardAll();
        panel.checkUserCardsAndAlertVictory();
    }

    // 상대방 카드 가져오기
    public void takeCard() {
        panel.takeSelectedComputerCard();
        panel.checkComputerCardsAndAlertVictory();
    }

}