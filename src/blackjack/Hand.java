package blackjack;

import Card.BlackJackCard;
import Deck.BlackJackDeck;
import java.util.ArrayList;

/**
 * A hand of cards to play with
 */
public class Hand {

    private ArrayList<BlackJackCard> hand;

    public Hand(){
        hand = new ArrayList<BlackJackCard>();
    }

    /**
     * Take a single card from the top of this deck and add it to the hand, removing it from the previous deck
     * @param deck The deck of cards we're taking from
     */
    public void takeCardFromDeck(BlackJackDeck deck){
        hand.add(deck.takeCard());
    }

    /**
     * Add a single card to this hand
     * @param c The card being added
     */
    /**
     *
     * @param discardDeck The deck we're discarding this hand to
     */
    public void discardHandToDeck(BlackJackDeck discardDeck){

        //copy cards from hand to discardDeck
        discardDeck.addCards(hand);

        //clear the hand
        hand.clear();

    }

    /**
     *
     * @return The hand with all its cards in a single line String
     */
    public String toString(){
        String output = "";
        for(BlackJackCard card: hand){
            output += card + " - ";
        }
        return output;
    }


    /**
     *
     * @return The calculated numerical value of the hand as an integer
     */
    public int calculatedValue(){

        //variable to count number of aces, and current total value
        int value = 0;
        int aceCount = 0;

        //For each card in this hand
        for(BlackJackCard card: hand){
            //Add the card value to the hand
            value += card.getValue();
            //Count how many aces have been added
            if (card.getValue() == 11){
                aceCount ++;
            }
        }
        //if we have a scenario where we have multiple aces, as may be the case of drawing 10, followed by two or more aces, (10+11+1 > 21)
        //go back and set each ace to 1 until get back under 21, if possible
        if (value > 21 && aceCount > 0){
            while(aceCount > 0 && value > 21){
                aceCount --;
                value -= 10;
            }
        }
        return value;

    }


    /**
     *
     * @param idx the index of the card we're getting
     * @return the card we got
     */
    public BlackJackCard getCard(int idx){
        return hand.get(idx);
    }

    /**
     * Get the number of cards in this hand
     * @return
     */
    public int getHandSize(){
        return hand.size();
    }

}
