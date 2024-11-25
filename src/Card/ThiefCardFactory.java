package Card;

import Card.Card;
import Card.CardFactory;

import java.util.ArrayList;
import java.util.List;

public class ThiefCardFactory implements CardFactory {
    @Override
    public List<Card> createCards() {
        List<Card> cards = new ArrayList<>();
        String[] suits = { "Spades", "Hearts", "Diamonds", "Clubs"};
        String[] names = {"Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};
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