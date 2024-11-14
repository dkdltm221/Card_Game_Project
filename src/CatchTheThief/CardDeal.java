package CatchTheThief;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class CardDeal {
    void run() {
        String[] suit = {"♤", "♡", "◇", "♧"};
        String[] rank = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String joker = "joker";

        ArrayList<String> deck = new ArrayList<>();

        // 카드 덱 생성
        for (String s : suit) {
            for (String r : rank) {
                deck.add(s + r);
            }
        }
        // 조커 카드 추가
        deck.add(joker);

        // 카드 섞기
        Collections.shuffle(deck);

        // 컴퓨터와 사용자 덱 나누기
        ArrayList<String> deckForCom = new ArrayList<>(deck.subList(0, 27));
        ArrayList<String> deckForMe = new ArrayList<>(deck.subList(27, 53));

        System.out.println("컴퓨터의 초기 카드: " + deckForCom);
        System.out.println("사용자의 초기 카드: " + deckForMe);

        // 각 덱에서 중복된 숫자 삭제
        removeDuplicateRanks(deckForCom);
        removeDuplicateRanks(deckForMe);

        System.out.println("중복 제거 후 컴퓨터의 카드: " + deckForCom);
        System.out.println("중복 제거 후 사용자의 카드: " + deckForMe);
    }

    // 중복된 랭크를 제거하는 함수
    public static void removeDuplicateRanks(ArrayList<String> deck) {
        HashSet<String> seenRanks = new HashSet<>();
        HashSet<String> duplicateRanks = new HashSet<>();

        // 중복된 랭크 찾기
        for (String card : deck) {
            String rank = getRank(card);
            if (!seenRanks.add(rank)) {
                duplicateRanks.add(rank);  // 중복 랭크 기록
            }
        }

        // 중복된 랭크 카드 제거
        deck.removeIf(card -> duplicateRanks.contains(getRank(card)));
    }

    // 카드의 랭크를 반환하는 함수
    public static String getRank(String card) {
        if (card.equals("joker")) {
            return "joker";
        } else {
            return card.substring(1); // 첫 글자는 무늬이므로 제거하고 숫자만 반환
        }
    }
    public static void main(String[] args){
        CardDeal cardDeal = new CardDeal();
        cardDeal.run();
    }
}



