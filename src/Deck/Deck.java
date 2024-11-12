package Deck;

import Card.Card;
import Card.CardFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;

abstract public class Deck {
    protected List<Card> cards;

    public Deck(CardFactory cardFactory) {
        cards = cardFactory.createCards();
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.remove(cards.size() - 1);
    }

    public List<Card> getCards() {
        return cards;
    }

}
