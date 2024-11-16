package system;

import Deck.Player;

import java.util.ArrayList;

public class Throw {
    public static ArrayList<Integer> trash = new ArrayList<>();
    public void check(Player p){
        int csize = p.getSize();
        for(int i = 0; i<csize-1; i++) {
            for (int j = i + 1; j < csize; j++) {
                if (p.getCard(i) == 0 || p.getCard(j) == 0)
                    continue;
                if ((p.getCard(i) % 13) == (p.getCard(j) % 13)) {
                    trash.add(p.getCard(i));
                    trash.add(p.getCard(j));
                    p.throwCard(i);
                    p.throwCard(j - 1);

                    csize = csize - 2;

                    if (i != 0)
                        i = i - 1;
                    else
                        i = 0;
                    continue;
                }
            }
        }
    }
    public void printTrash(){
        for(int i = 0; i<trash.size();i++){
            System.out.print(trash.get(i)+" ");
        }
        System.out.println();
    }
    public int last(){
        return trash.size();
    }
}

