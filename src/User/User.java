package User;

import Card.Card;
import Deck.Deck;


public class User {
    private String name;
    private Deck deck;
    private int score;

    public User(String name) {
        this.name = name;
        this.score = 0;
    }

    public String getName() {
        return name;
    }


    public int getScore() {
        return this.score;
    }


    public void addScore(int points) {
        this.score += points;
    }


    public Card drawCard() {
        if (deck != null) {
            return deck.drawCard();
        } else {
            throw new IllegalStateException("덱이 설정되지 않았습니다.");
        }
    }


    public void resetScore() {
        this.score = 0;
    }
}