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
        Card joker=new PlayCard("joker",0);

        for (String suit : suits) {
            for (int i = 0; i < names.length; i++) {
                cards.add(new PlayCard(  suit+names[i], values[i]));
            }
            cards.add(joker);
        return cards;
    }

}