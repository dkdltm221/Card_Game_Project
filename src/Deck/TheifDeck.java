package Deck;

import Card.ThiefCardFactory;
import Card.CardFactory;

public class TheifDeck extends Deck{
    public TheifDeck() {
        super(new ThiefCardFactory());
    }
}
