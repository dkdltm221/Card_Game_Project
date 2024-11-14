package CatchTheThief;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CardDeal {
    public static void main(String[] args) {
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
        // 조커 카드 추가s
        deck.add(joker);

        // 카드 섞기
        Collections.shuffle(deck);

        // 컴퓨터와 사용자 덱 나누기
        ArrayList<String> deckForCom = new ArrayList<>(deck.subList(0, 27));
        ArrayList<String> deckForMe = new ArrayList<>(deck.subList(26, 53));

        System.out.println("컴퓨터의 초기 카드: " + deckForCom);
        System.out.println("사용자의 초기 카드: " + deckForMe);

        // 각 덱에서 중복된 숫자에 따라 카드 제거
        removeDuplicatesBasedOnCount(deckForCom);
        removeDuplicatesBasedOnCount(deckForMe);

        System.out.println("중복 삭제 후 컴퓨터의 카드: " + deckForCom);
        System.out.println("중복 삭제 후 사용자의 카드: " + deckForMe);
    }

    // 중복된 랭크의 카드들을 조건에 따라 삭제하는 함수
    public static void removeDuplicatesBasedOnCount(ArrayList<String> deck) {
        HashMap<String, Integer> rankCount = new HashMap<>();

        // 각 랭크의 개수 세기
        for (String card : deck) {
            String rank = getRank(card);
            rankCount.put(rank, rankCount.getOrDefault(rank, 0) + 1);
        }

        // 카드 삭제 목록 생성
        ArrayList<String> cardsToRemove = new ArrayList<>();
        for (String card : deck) {
            String rank = getRank(card);
            int count = rankCount.get(rank);

            if (count == 2) {  // 같은 랭크 카드가 2장일 경우 둘 다 삭제
                cardsToRemove.add(card);
                rankCount.put(rank, count - 1);
            } else if (count == 3) {  // 같은 랭크 카드가 3장일 경우 하나만 남김
                cardsToRemove.add(card);
                rankCount.put(rank, count - 1);
                if (rankCount.get(rank) == 1) {
                    break;
                }
            } else if (count == 4) {  // 같은 랭크 카드가 4장일 경우 모두 삭제
                cardsToRemove.add(card);
                rankCount.put(rank, count - 1);
            }
        }

        // 조건에 맞게 선택된 카드들 제거
        deck.removeAll(cardsToRemove);
    }

    // 카드의 랭크를 반환하는 함수
    public static String getRank(String card) {
        if (card.equals("joker")) {
            return "joker";
        } else {
            return card.substring(1); // 첫 글자는 무늬이므로 제거하고 숫자만 반환
        }
    }
}
