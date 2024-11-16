package system;
import Deck.*;
public class Select extends Throw{
    public void select_com(Player com, Player player){
        int sel_com = (int) (Math.random()* player.getSize());

        com.addCard(player.getCard(sel_com));
        player.throwCard(sel_com);

        check(com);
        shuffle(com);
    }
    public void shuffle(Player p){
        int rand;
        int temp;
        for(int i = 0; i<p.getSize();i++){
            rand = (int) (Math.random()*(p.getSize()-1));
            temp = p.getCard(i);
            p.setCard(i,p.getCard(rand));
            p.setCard(rand,temp);
        }
    }
    public void Deckprint(Player com, Player player){
        System.out.print("com : ");
        com.secretPrint();
        System.out.print("player : ");
        player.printDeck();
        System.out.print("throw : ");
        System.out.println();
    }
}
