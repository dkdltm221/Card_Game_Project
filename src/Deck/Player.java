package Deck;

import java.util.ArrayList;

public class Player {
    public ArrayList<Integer> player = new ArrayList<Integer>();

    public Player(){}

    public int getSize(){
        return player.size();
    }
    public void addCard(int card){
        player.add(card);
    }
    public int getCard(int card){
        return player.get(card);
    }
    public void throwCard(int card){
        player.remove(card);
    }
    public void setCard(int now, int newCard){
        player.set(now,newCard);
    }
    public void printDeck(){
        for(int i=0; i<player.size();i++){
            System.out.print(player.get(i)+" ");
        }
        System.out.println();
    }
    public void secretPrint(){
        for(int i=0; i< player.size(); i++){
            System.out.print((i+1)+" ");
        }
        System.out.println();
    }
}
