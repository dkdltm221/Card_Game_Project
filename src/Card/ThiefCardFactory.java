package Card;

import Card.Card;
import Card.CardFactory;

import java.util.ArrayList;
import java.util.List;

public class ThiefCardFactory implements CardFactory {
    @Override
    public List<Card> createCards() {
        List<Card> cards = new ArrayList<>();
        String[] suits = { "♤", "♡", "◇", "♧"};
        String[] names = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        Card joker=new PlayCard("joker",0);

        for (String suit : suits) {
            for (int i = 0; i < names.length; i++) {
                cards.add(new PlayCard(  suit+names[i], values[i]));
            }
        }
        cards.add(joker);
        return cards;
    }

}