package Card;

import Deck.*;
import system.*;

public class Start {
    All all = new All();
    Throw th = new Throw();
    Select select = new Select();

    public void GameSet(Player com, Player player){
        all.devide(com,player);

        th.check(com);
        th.check(player);
    }
}
