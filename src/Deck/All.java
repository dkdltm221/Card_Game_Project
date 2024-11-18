package Deck;

public class All {
    public int all[] = new int[53];

    public All(){}

    public void devide(Player p1, Player p2){
        for(int i=0; i<all.length; i++){
            all[i] = i;
        }

        int check = 0;

        while(check<53){
            int select = (int)(Math.random());
            int card = (int)(Math.random()*53);

            if(check==53)
                break;
            if(all[card]==-1)
                continue;

            if(p1.getSize()==27||p2.getSize()==27){
                if(p1.getSize()==27){
                    p2.addCard(all[card]);
                    all[card]=-1;
                    check++;
                }
                else{
                    p1.addCard(all[card]);
                    all[card]=-1;
                    check++;
                }
            }
            else {
                if(select==0){
                    p1.addCard(all[card]);
                    all[card]=-1;
                    check++;
                }
                else{
                    p2.addCard(all[card]);
                    all[card]=-1;
                    check++;
                }
            }
        }
    }
}
