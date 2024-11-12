package Deck;

import Card.BlackjackCardFactory;

public class BlackjackDeck extends Deck {
    public BlackjackDeck() {
        super(new BlackjackCardFactory());
    }
}
