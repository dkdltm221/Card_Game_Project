package CatchTheThief;

import java.util.*;

public class CardDeal {
    Scanner scan = new Scanner(System.in);
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
        ArrayList<String> deckForMe = new ArrayList<>(deck.subList(26, 53));

        System.out.println("컴퓨터의 초기 카드: " + deckForCom);
        System.out.println("사용자의 초기 카드: " + deckForMe);

        // 각 덱에서 중복된 숫자 삭제
        removeDuplicatesBasedOnCount(deckForCom);
        removeDuplicatesBasedOnCount(deckForMe);

        System.out.println("중복 제거 후 컴퓨터의 카드: " + deckForCom);
        System.out.println("중복 제거 후 사용자의 카드: " + deckForMe);

        int go = scan.nextInt();
        while(true){

            transferRandomCard(deckForCom, deckForMe);
            System.out.println("뽑았다!");
            //removeDuplicatesBasedOnCount(deckForMe);

            checkAndEndGame(deckForCom,deckForMe);
            System.out.println("중복 제거 후 컴퓨터의 카드: " + deckForCom);
            System.out.println("중복 제거 후 사용자의 카드: " + deckForMe);
            go = scan.nextInt();
            if(go==0){
                break;
            }

            transferRandomCard(deckForMe, deckForCom);
            System.out.println("뽑았다!");
            removeDuplicatesBasedOnCount(deckForCom);
            checkAndEndGame(deckForCom,deckForMe);
            System.out.println("중복 제거 후 컴퓨터의 카드: " + deckForCom);
            System.out.println("중복 제거 후 사용자의 카드: " + deckForMe);
            go = scan.nextInt();
            if(go==0){
                break;
            }
        }

    }


    // 중복된 랭크를 제거하는 함수
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
    public static void transferRandomCard(ArrayList<String> fromDeck, ArrayList<String> toDeck) {
        if (!fromDeck.isEmpty()) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(fromDeck.size());
            String card = fromDeck.remove(randomIndex); // fromDeck에서 카드 제거
            toDeck.add(card); // toDeck에 카드 추가
            removeDuplicatesBasedOnCount(toDeck);
            if(!card.equals("joker"))  toDeck.remove(card);
        }
    }
    public static void checkAndEndGame(ArrayList<String> deckForCom, ArrayList<String> deckForMe) {
        if (isOnlyJokerLeft(deckForCom)) {
            System.out.println("컴퓨터 덱에 joker만 남았습니다. 게임 종료.");
            System.exit(0);
        } else if (isOnlyJokerLeft(deckForMe)) {
            System.out.println("사용자 덱에 joker만 남았습니다. 게임 종료.");
            System.exit(0);
        }
    }
    public static boolean isOnlyJokerLeft(ArrayList<String> deck) {
        return deck.size() == 1 && deck.get(0).equals("joker");
    }
    public static void main(String[] args){
        CardDeal cardDeal = new CardDeal();
        cardDeal.run();
    }
}