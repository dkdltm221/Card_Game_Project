package CatchTheThief;

import java.util.*;

public class CardDeal {
    List<List<String>> myList = new ArrayList<>();
    List<List<String>> comList = new ArrayList<>();
    void run() {
        int cardsForCom = 27;
        int cardsForMe = 26;

        String[] suit = new String[]{"♤", "♡", "◇", "♧"};
        String[] rank = new String[]{"A", "2", "3", "4", "5",
                "6", "7", "8", "9", "10", "J", "Q", "K"};
        String joker = "joker";
        List<String> deck = new ArrayList<String>();
        for (int i = 0; i < suit.length; i++)
            for (int j = 0; j < rank.length; j++)
                deck.add(suit[i] + rank[j]);
        deck.add(joker);
        Collections.shuffle(deck);


        List<String> mydeck = dealHand(deck, cardsForMe);
        List<String> comdeck = dealHand(deck, cardsForCom);
        System.out.println(mydeck);
        System.out.println(comdeck);
        myList.add(mydeck);
        comList.add(comdeck);

        //비교 후 삭제

    }

    void match(){

    }

    public <E> List<E> dealHand(List<E> deck, int n) {
        int deckSize = deck.size(); // 끝에서 n개 서브리스트
        List<E> handView = deck.subList(deckSize - n, deckSize);
        List<E> hand = new ArrayList<E>(handView); // 복제 생성
        handView.clear(); // 서브리스트 삭제, 원래 리스트에서도 삭제됨
        return hand;
    }
    public static void main(String[] args){
        CardDeal main = new CardDeal();
        main.run();
    }
}


