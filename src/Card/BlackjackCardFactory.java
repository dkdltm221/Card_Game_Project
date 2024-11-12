package Card;

import java.util.ArrayList;
import java.util.List;

public class BlackjackCardFactory implements CardFactory {
    @Override
    public List<Card> createCards() {
        List<Card> cards = new ArrayList<>();
        String[] suits = { "♤", "♡", "◇", "♧"};
        String[] names = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};

        for (String suit : suits) {
            for (int i = 0; i < names.length; i++) {
                cards.add(new PlayCard(  suit+names[i], values[i]));
            }
        }
        return cards;
    }

}
