package Deck;

import Card.BlackjackCardFactory;
import Card.Card;
public class BlackjackDeck extends Deck {
    public BlackjackDeck() {
        super(new BlackjackCardFactory());
    }

}
