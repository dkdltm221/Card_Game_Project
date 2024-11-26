package blackjack;

import Main.MainApp;

import javax.swing.*;
import java.awt.*;
import Deck.BlackJackDeck;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import Panel.GameSelectionPanel;
/**
 * Contains all Game logic
 */
public class Game extends JPanel {

    // 상수
    public static final int CARD_WIDTH = 150;  // 카드의 너비
    public static final int CARD_HEIGHT = 245; // 카드의 높이
    public static final String IMAGE_DIR = "img/cards/"; // 카드 이미지 디렉토리

    //객체 선언
    GameSelectionPanel gameSelectionPanel = GameSelectionPanel.getInstance();
    // Game 클래스에서 필요한 인스턴스 변수 선언
    private int score =0;
    private boolean oneGame = true;
    private BlackJackDeck deck, discarded; // 카드 덱과 버려진 카드 덱
    private Dealer dealer; // 딜러 객체
    private Player player; // 플레이어 객체
    private int wins, losses, pushes; // 승리, 패배, 무승부 횟수
    private MainApp mainApp = null;
    private JTextArea gameLog = new JTextArea();
    // "Hit", "Stand", "Next Round" 버튼
    private JButton btnHit, btnStand, btnNext,btnEnd;

    // 딜러와 플레이어의 카드 이미지를 표시하기 위한 라벨 배열
    private JLabel[] lblDealerCards, lblPlayerCards;
    // 점수판과 메시지를 표시하기 위한 라벨
    private JLabel lblScore, lblPlayerHandVal, lblDealerHandVal, lblGameMessage;

    /**
     * Game 클래스 생성자
     * 변수를 초기화하고 게임을 시작함
     */
    public Game(MainApp mainApp) {
        this.mainApp = mainApp;
        // 52장의 카드로 새로운 덱 생성
        deck = new BlackJackDeck(true);
        // 빈 덱 생성
        discarded = new BlackJackDeck();

        // 딜러와 플레이어 객체 생성
        dealer = new Dealer();
        player = new Player();

        // 덱 셔플 후 첫 라운드 시작
        deck.shuffle();

        setupGUI();
        startRound();
    }

    /**
     * 게임의 GUI를 설정
     * 버튼과 라벨을 JPanel에 추가
     */
    private void setupGUI() {
        // JPanel 크기 설정
        this.setSize(800, 500);

        // "Hit", "Stand", "Next Round" 버튼 생성 및 위치 지정
        btnHit = new JButton("Hit");
        btnHit.setBounds(20, 20, 100, 40);
        btnStand = new JButton("Stand");
        btnStand.setBounds(140, 20, 200, 40);
        btnNext = new JButton("Next Round");
        btnNext.setBounds(360, 20, 280, 40);
        btnNext.setVisible(false);
        btnEnd = new JButton("Leave the game");
        btnEnd.setBounds(20,20,280,40);
        btnEnd.setVisible(false);

        // 절대 위치를 사용하기 위해 레이아웃 설정
        this.setLayout(null);

        // 버튼을 JPanel에 추가
        this.add(btnHit);
        this.add(btnStand);
        this.add(btnNext);
        this.add(btnEnd);
        // 딜러와 플레이어 카드 이미지를 저장할 배열 초기화
        lblDealerCards = new JLabel[11];
        lblPlayerCards = new JLabel[11];

        // 첫 번째 카드의 초기 위치 설정
        int initialCardX = 310, initialCardY = 170;

        // 11장의 카드에 대해 반복
        for (int i = 0; i < lblDealerCards.length; i++) {
            // 카드 뒷면 이미지를 라벨로 설정
            lblDealerCards[i] = new JLabel(new ImageIcon(new ImageIcon(IMAGE_DIR + "CardDown.png").getImage()
                    .getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH)));
            lblPlayerCards[i] = new JLabel(new ImageIcon(new ImageIcon(IMAGE_DIR + "CardDown.png").getImage()
                    .getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH)));

            // 카드 위치 및 크기 설정
            lblDealerCards[i].setBounds(initialCardX, initialCardY, CARD_WIDTH, CARD_HEIGHT);
            lblPlayerCards[i].setBounds(initialCardX, initialCardY + 350, CARD_WIDTH, CARD_HEIGHT);

            // JPanel에 라벨 추가
            this.add(lblDealerCards[i]);
            this.add(lblPlayerCards[i]);

            // 다음 카드 위치 조정
            initialCardX += 50;
            initialCardY -= 18;
        }

        // 점수판 라벨 설정
        lblScore = new JLabel("[Wins: 0]   [Losses: 0]   [Pushes: 0]  [플레이어 점수: 0]");
        lblScore.setBounds(10, 10, getWidth() - 20, 50); // 위치 조정
        lblScore.setHorizontalAlignment(SwingConstants.RIGHT); // 텍스트를 오른쪽 정렬
        lblScore.setFont(new Font("Serif", Font.BOLD, 20));       // 폰트 설정: Arial, Bold, 20 크기
        this.add(lblScore);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // 패널 크기 변경 시 lblScore 위치 업데이트
                lblScore.setBounds(10, 10, getWidth() - 20, 50);
            }
        });



        // 메시지 라벨 설정
        lblGameMessage = new JLabel("라운드 시작! Hit 또는 Stand를 선택하세요.");
        lblGameMessage.setBounds(800, 200, 550, 140);
        lblGameMessage.setFont(new Font("Serif", Font.BOLD, 25));
        this.add(lblGameMessage);

        // JTextArea 추가 설정 (게임 기록 출력용)
        gameLog.setBounds(lblGameMessage.getX(), lblGameMessage.getY() + lblGameMessage.getHeight(), 550, 400);
        gameLog.setBackground(new Color(0, 0, 0, 150)); // 반투명 배경 (흰색, 알파값 150)
        gameLog.setForeground(Color.WHITE); // 글자 색상
        gameLog.setFont(new Font("Serif", Font.PLAIN, 20)); // Arial 폰트, 크기 20
        gameLog.setLineWrap(true); // 텍스트 자동 줄바꿈
        gameLog.setWrapStyleWord(true); // 단어 단위로 줄바꿈
        gameLog.setEditable(false); // 사용자가 직접 수정하지 못하게 설정
        gameLog.setOpaque(true); // 반투명 효과
        this.add(gameLog);

        // 딜러와 플레이어의 핸드 값 표시 라벨 설정
        lblDealerHandVal = new JLabel("딜러 핸드 값:");
        lblPlayerHandVal = new JLabel("플레이어 핸드 값:");
        lblDealerHandVal.setBounds(320, 400, 300, 50);
        lblPlayerHandVal.setBounds(320, 750, 300, 50);
        this.add(lblDealerHandVal);
        this.add(lblPlayerHandVal);

        // 모든 라벨의 텍스트 색상을 흰색으로 설정
        lblGameMessage.setForeground(Color.WHITE);
        lblDealerHandVal.setForeground(Color.WHITE);
        lblPlayerHandVal.setForeground(Color.WHITE);
        lblScore.setForeground(Color.WHITE);

        // 버튼 클릭 이벤트 리스너 설정
        btnHit.addActionListener(e -> {
            // 플레이어가 Hit를 선택하면 카드를 추가
            player.hit(deck, discarded);
            // 화면 업데이트
            updateScreen();
            checkBusts();
            checkPlayer21();
        });

        btnStand.addActionListener(e -> {
            // 플레이어가 Stand를 선택하면 딜러의 턴 진행
            dealersTurn();
            // 승패 확인
            checkWins();
            // 화면 업데이트 및 딜러 카드 공개
            updateScreen();
            dealer.printHand(lblDealerCards);
            // 다음 라운드 버튼 활성화
            btnHit.setVisible(false);
            btnStand.setVisible(false);
            btnEnd.setVisible(true);
            btnNext.setVisible(true);
        });

        btnNext.addActionListener(e -> {
            // 다음 라운드 시작
            btnNext.setVisible(false);
            btnEnd.setVisible(false);
            btnHit.setVisible(true);
            btnStand.setVisible(true);
            startRound();
        });
        btnEnd.addActionListener(e -> {
            if(oneGame){
                MainApp.updateScore(score);
                gameSelectionPanel.appendToGameLog("----블랙잭----\n"+gameLog.getText());
                oneGame = false;
            }
            mainApp.showScreen("GameSelection");

        });
    }

    /**
     * This is called when player hits "Hit" button to see if they busted
     */
    private void checkBusts() {
        // Check if they busted
        if (player.getHand().calculatedValue() > 21) {
            // show message
            lblGameMessage.setText("You BUST - Over 21");
            gameLog.append("You BUST - Over 21\n");
            // update score
            score-=100;
            losses++;
            // make next round button only visible button
            btnHit.setVisible(false);
            btnStand.setVisible(false);
            btnEnd.setVisible(true);
            btnNext.setVisible(true);
        }
    }

    /**
     * At the end of each round, this method is called to see who won
     */
    private void checkWins() {
        lblDealerHandVal.setText("딜러 핸드 값: " + dealer.getHand().calculatedValue());

        if (dealer.getHand().calculatedValue() > 21) {
            lblGameMessage.setText("딜러 Bust! 당신이 승리했습니다.");
            gameLog.append("딜러 Bust! 당신이 승리했습니다.\n");
            score+=100;
            wins++;
        } else if (dealer.getHand().calculatedValue() > player.getHand().calculatedValue()) {
            lblGameMessage.setText("딜러 승리 - 더 높은 값");
            gameLog.append("딜러 승리 - 더 높은 값\n");
            score-=100;
            losses++;
        } else if (player.getHand().calculatedValue() > dealer.getHand().calculatedValue()) {
            lblGameMessage.setText("플레이어 승리 - 더 높은 값");
            gameLog.append("플레이어 승리 - 더 높은 값\"\n");
            score+=100;
            wins++;
        } else {
            lblGameMessage.setText("무승부");
            gameLog.append("무승부\n");
            pushes++;
        }
    }

    /**
     * 플레이어가 21인지 확인
     */
    private void checkPlayer21() {
        if (player.getHand().calculatedValue() == 21) {
            lblGameMessage.setText("21에 도달했습니다! 플레이어 win!");
            gameLog.append("21에 도달했습니다! 플레이어 win!\n");
            score +=100;
            wins++;
            btnHit.setVisible(false);
            btnStand.setVisible(false);
            btnEnd.setVisible(true);
            btnNext.setVisible(true);
        }
    }

    /**
     * 딜러가 17 이상의 값을 가질 때까지 카드 드로우
     */
    private void dealersTurn() {
        while (dealer.getHand().calculatedValue() < 17) {
            dealer.hit(deck, discarded);
            updateScreen();
        }
    }

    /**
     * 화면 배경을 카드 테이블처럼 녹색으로 설정
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 이미지를 로드
        Image backgroundImage = new ImageIcon("img/BlackJackBackground.jpg").getImage();

        // 배경 이미지를 패널 크기에 맞게 그리기
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);

        // 기존 초록색 배경을 그리는 코드를 삭제 (필요시 조정)
        // g.setColor(Color.decode("#18320e"));
        // g.fillRect(0, 0, 1000, 1000);
    }


    /**
     * 화면 업데이트
     * 카드, 값, 점수 등 갱신
     */
    private void updateScreen() {
        lblPlayerHandVal.setText("플레이어 핸드 값: " + player.getHand().calculatedValue());
        player.printHand(lblPlayerCards);
        lblScore.setText("[Wins: " + wins + "]   [Losses: " + losses + "]   [Pushes: " + pushes + "]  [플레이어 점수: "+score+"]");
    }

    /**
     * 새로운 라운드를 시작하고 점수를 표시한 뒤 카드를 분배하고 블랙잭 여부를 확인.
     * 플레이어에게 다음 행동을 물어봅니다.
     */
    private void startRound() {
        /*
         * wins = 0; losses = 0; pushes = 1;
         * Card testCard = new Card(Suit.CLUB, Rank.NINE);
         * Card testCard2 = new Card(Suit.CLUB, Rank.TEN);
         */

        // 첫 라운드가 아니라면 점수를 표시하고 이전 카드를 덱으로 되돌립니다.
        if (wins > 0 || losses > 0 || pushes > 0) {
            System.out.println();
            System.out.println("다음 라운드 시작... 승리: " + wins + " 패배: " + losses + " 무승부: " + pushes);
            dealer.getHand().discardHandToDeck(discarded);
            player.getHand().discardHandToDeck(discarded);
        }

        // 덱에 최소한 4장의 카드가 남아있는지 확인합니다.
        if (deck.cardsLeft() < 4) {
            deck.reloadDeckFromDiscard(discarded);
        }

        // 딜러에게 두 장의 카드를 나눠줍니다.
        dealer.getHand().takeCardFromDeck(deck);
        dealer.getHand().takeCardFromDeck(deck);

        // 플레이어에게 두 장의 카드를 나눠줍니다.
        player.getHand().takeCardFromDeck(deck);
        player.getHand().takeCardFromDeck(deck);

        // 플레이어와 딜러에게 카드를 나눠준 후 화면을 업데이트합니다.
        updateScreen();
        lblDealerHandVal.setText("딜러 핸드 값: " + dealer.getHand().getCard(0).getValue() + " + ?");
        lblGameMessage.setText("라운드 시작! Hit 또는 Stand를 선택하세요.");

        // 딜러의 핸드를 한 장은 뒷면으로 보여줍니다.
        dealer.printHand(lblDealerCards);
        lblDealerCards[1].setIcon(new ImageIcon(new ImageIcon(IMAGE_DIR + "CardDown.png").getImage()
                .getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_DEFAULT)));

        // 플레이어의 핸드를 보여줍니다.
        player.printHand(lblPlayerCards);

        // 딜러가 블랙잭인지 확인합니다.
        if (dealer.hasBlackjack()) {
            // 딜러가 블랙잭일 경우 딜러의 핸드를 보여줍니다.
            dealer.printHand(lblDealerCards);

            // 플레이어도 블랙잭인지 확인합니다.
            if (player.hasBlackjack()) {
                // 무승부로 라운드를 종료합니다.
                lblGameMessage.setText("둘 다 블랙잭 - 무승부");
                pushes++;
                // 다음 라운드 버튼을 활성화합니다.
                btnHit.setVisible(false);
                btnStand.setVisible(false);
                btnNext.setVisible(true);
            } else {
                lblGameMessage.setText("딜러가 블랙잭입니다!");
                dealer.printHand(lblDealerCards);
                losses++;
                // 플레이어 패배, 새로운 라운드 시작
                btnHit.setVisible(false);
                btnStand.setVisible(false);
                btnNext.setVisible(true);
            }
        }

        // 플레이어가 블랙잭인지 확인합니다.
        // 이 시점에서는 딜러가 블랙잭이 아님을 이미 알고 있습니다.
        if (player.hasBlackjack()) {
            // 플레이어가 블랙잭일 경우 메시지를 표시합니다.
            lblGameMessage.setText("블랙잭입니다!");
            // 점수를 업데이트합니다.
            wins++;
            // 다음 라운드 버튼만 활성화합니다.
            btnHit.setVisible(false);
            btnStand.setVisible(false);
            btnNext.setVisible(true);
        }
    }
}
