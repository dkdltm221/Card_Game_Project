package Game;

import Card.BlackjackCard;
import Main.MainApp;
import Panel.BlackjackPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

enum STATE {
    LOADGAME, BET, INGAME, FINISH, PLYAER_SQUANDERD, PLYAER_NOT_SQUANDER,
}
public class BlackJack implements KeyListener {
    static int count=0;
    private BlackjackPanel mFrame;
    FinishCode mCode;
    STATE mState;
    static public Integer MAX_CARD_COUNT =52;
    static public Integer MAX_CARD_TYPE =4;
    // 게임 중 사용할 카드를 집어넣는 큐
    private LinkedList<BlackjackCard> mCards;
    // 게임 중 각 참가자의 카드를 저장하는 벡터
    private Vector<BlackjackCard> mPlayerCard;
    private Vector<BlackjackCard> mDealerCard;
    private int mPlayerCost;
    private int mDealerCost;
    private int mCurrentBetCost;
    private int mFlowCost;
    static Random Rand =new Random();
    private MainApp mainApp;
    int addPoint;
    // 생성자
    public BlackJack(BlackjackPanel blackjackPanel, MainApp mainApp) {
        this.mainApp = mainApp;
        mFrame = blackjackPanel;
        mFrame.addKeyListener(this);  // BlackJack 인스턴스를 KeyListener로 등록
        this.mPlayerCost = MainApp.getUserScore();
        this.mDealerCost = 100;
        this.mFlowCost = mPlayerCost;
        this.addPoint = 0;
        mState = STATE.LOADGAME;
        SelectData();
    }


    private void SelectData() {
        mFrame.SetText(null);
        mFrame.AddText("\n\n");
        mFrame.AddText("   --------------- BlackJack Game ---------------\n\n");
        mFrame.AddText("   1. START NEW GAME\n\n");

        mFrame.AddText("   2. CONTINUE GAME");
        mFrame.AddText("\n\n");

        mFrame.AddText("   -------------------------------------------------\n\n");
        mFrame.AddText("   Select : ");
    }
    // 사용할 멤버변수들을 초기화한다
    private void ContinueGame() {
        mCurrentBetCost = (mPlayerCost /2);
        mCurrentBetCost += mCurrentBetCost % 10;
        mState = STATE.BET;
        mCode = FinishCode.NONE;
        mCards =new LinkedList<>();
        mPlayerCard =new Vector<>();
        mDealerCard =new Vector<>();
        String tempSuit ="?";
        for (int i =1; i <= MAX_CARD_COUNT / MAX_CARD_TYPE; ++i) {
            for (int j =0; j < MAX_CARD_TYPE; ++j) {
                switch (j) {
                    case 0: {
                        tempSuit ="◆";
                        break;
                    }
                    case 1: {
                        tempSuit ="♣";
                        break;
                    }
                    case 2: {
                        tempSuit ="♥";
                        break;
                    }
                    case 3: {
                        tempSuit ="♠";
                        break;
                    }
                    default: {
                        System.out.println("Index Error");
                        System.exit(0);
                    }
                }
                mCards.add(new BlackjackCard(tempSuit, i));
            }
        }
        int randPos;
        for (int i =0; i <2; ++i) {
            randPos = Rand.nextInt(mCards.size());
            mPlayerCard.add(mCards.get(randPos));
            mCards.remove(randPos);
            randPos = Rand.nextInt(mCards.size());
            mDealerCard.add(mCards.get(randPos));
            mCards.remove(randPos);
        }
        DealMoney();
    }
    private void DealMoney() {
        if (mDealerCost <= 0) {
            mFrame.AddText("\n\n   Dealer has run out of money. Game over.");
            mState = STATE.FINISH;
            mFrame.AddText(" If you want to leave, please press the E ");
            return;
        }
        mFrame.SetText(null);
        mFrame.AddText("\n\n");
        mFrame.AddText("   --------------- BlackJack Game ---------------\n\n");
        mFrame.AddText("    # Dealer: ($"+ mDealerCost +")\n");
        mFrame.AddText("    # Player: ($"+ mPlayerCost +")\n");
        mFrame.AddText("   -------------------------------------------------\n\n");
        mFrame.AddText("   How much money do you want to bet?\n\n");
        mFrame.AddText("   "+ mCurrentBetCost +"     (Up/Down/Enter)");
    }
    private void DisplayInfo() {
        CalcBet();
        CheckPlayerCost();

        BlackjackCard tempCarddd;
        mFrame.SetText(null);
        mFrame.AddText("\n\n   --------------- BlackJack Game ---------------\n");
        mFrame.AddText("    # Betting : $"+ mCurrentBetCost +"\n\n");
        mFrame.AddText("    # Dealer: ($"+ mDealerCost +") \t");
        // 0은 A로, 11이상은 각 J Q K로 출력한다 그 외는 정수 그대로
        for (int i = 0; i < mDealerCard.size() -1; ++i) {
            tempCarddd = mDealerCard.get(i);
            mFrame.AddText(tempCarddd.DisplayCard());
        }
        if (mCode == FinishCode.NONE) {
            mFrame.AddText("XX");
        }
        else {
            tempCarddd = mDealerCard.get(mDealerCard.size() -1);
            mFrame.AddText(tempCarddd.DisplayCard());
        }
        mFrame.AddText("\n");
        mFrame.AddText("    # Player:  ($"+ mPlayerCost +") \t");
        for (int i = 0; i < mPlayerCard.size(); ++i) {
            tempCarddd = mPlayerCard.get(i);
            mFrame.AddText(tempCarddd.DisplayCard());
        }
        mFrame.AddText("\n");
        mFrame.AddText("   -------------------------------------------------");
        DisplayCurrentState();
    }
    private void Hit() {
        int randPos = Rand.nextInt(mCards.size());
        mPlayerCard.add(mCards.get(randPos));
        mCards.remove(randPos);
        // 만약 추가했을 때 총 값이 21을 초과하면 플레이어는 BUSTED한다.
        if (CalcPrice(mPlayerCard) >21) {
            mCode = FinishCode.PLAYERBUSTED;
        }
    }
    // Stand을 하면 딜러의 카드벡터에 전체카드 큐를 17이상이 될때까지 전체카드큐에서 poll한다.
    private void Stand() {
        while (CalcPrice(mDealerCard) <17) {
            int randPos = Rand.nextInt(mCards.size());
            mDealerCard.add(mCards.get(randPos));
            mCards.remove(randPos);
        }
        // 만약에 21보다 커지면 딜러는 BUSTED한다.
        if (CalcPrice(mDealerCard) >21) {
            mCode = FinishCode.DEALERBUSTED;
        }
        // 그것이 아니라면 플레이어와 딜러의 값을 비교할 필요가 있다.
        else {
            mCode = FinishCode.NEEDCALC;
        }
    }
    private int CalcPrice(Vector<BlackjackCard> carddds) {
        int sumRes =0;
        BlackjackCard tempCarddd =null;
        for (int i = 0; i < carddds.size(); ++i) {
            tempCarddd = carddds.get(i);
            sumRes += tempCarddd.getValue();
        }
        // 21을 초과한 시점에서
        // A 카드를 가지고 있고, 그 카드가 11로 계산되고 있을경우
        if (sumRes >21) {
            for (int i = 0; i < carddds.size(); ++i) {
                // 랭크가 11이다? -> Calcto11이 True인경우?
                if (carddds.get(i).getValue() ==11) {
                    // 해당 A카드의 11여부를 false로 하고
                    carddds.get(i).SetACalcTo11(false);
                    // 11 -> 1로 했기에 10을 빼서 리턴한다.
                    sumRes -=10;
                    // 디버그용 출력
                    System.out.println("A 카드가 11에서 1로 값 변경됨");
                }
            }
        }
        return sumRes;
    }
    // 게임이 끝나면 누가 이겼는지 계산한다.
    private void CalcWinner() {
        int playerVal = CalcPrice(mPlayerCard);
        int dealerVal = CalcPrice(mDealerCard);
        if (playerVal >21 && dealerVal >21) {
            mCode = FinishCode.DRAW;
        }
        else if (playerVal == dealerVal) {
            mCode = FinishCode.DRAW;
        }
        else if (playerVal > dealerVal) {
            mCode = FinishCode.PLAYER;
        }
        else {
            mCode = FinishCode.DEALER;
        }
    }
    // enum타입의 FinishCode에 따라 알맞은 결과를 출력한다.
    @SuppressWarnings("incomplete-switch")
    private void DisplayCurrentState() {
        if (mCode == FinishCode.NEEDCALC) {
            CalcWinner();
            DisplayInfo();
            return;
        }
        mFrame.AddText("\n\n   ");
        if (mState == STATE.PLYAER_SQUANDERD) {
            mFrame.AddText("\n\n   You lost EVERYTHING!! Quit Game !");
            mState = STATE.FINISH;
            mFrame.AddText(" If you want to leave, please press the E ");
            return;
        }
        switch (mCode) {
            case NONE: {
                mFrame.AddText("Hit or Stand? (H/S): ");
                break;
            }
            case DRAW: {
                mFrame.AddText("Equal points...");
                break;
            }
            case PLAYER: {
                mFrame.AddText("Player Wins...");
                break;
            }
            case PLAYERBUSTED: {
                mFrame.AddText("Player Busted...");
                break;
            }
            case DEALER: {
                mFrame.AddText("Dealer Wins...");
                break;
            }
            case DEALERBUSTED: {
                mFrame.AddText("Dealer Busted...");
                break;
            }
        }
        if (mState == STATE.PLYAER_NOT_SQUANDER) {
            mFrame.AddText("\n\n   Play Again? (Y/N)");
        }
    }

    private void CalcBet()
    {
        switch (mCode) {
            case DRAW: {
                mPlayerCost += mCurrentBetCost;
                break;
            }
            case PLAYER: {
                mDealerCost -= mCurrentBetCost;
                mPlayerCost += mCurrentBetCost *2;
                addPoint += mCurrentBetCost;
                break;
            }
            case PLAYERBUSTED: {
                mDealerCost += mCurrentBetCost;
                addPoint-= mCurrentBetCost;
                break;
            }
            case DEALER: {
                mDealerCost += mCurrentBetCost;
                addPoint-= mCurrentBetCost;
                break;
            }
            case DEALERBUSTED: {
                mDealerCost -= mCurrentBetCost;
                mPlayerCost += mCurrentBetCost *2;
                addPoint += mCurrentBetCost;
                break;
            }
            case NEEDCALC:
                break;
            case NONE:
                break;
            default:
                break;
        }

    }
    private void CheckPlayerCost() {
        if (mCode == FinishCode.NONE)
            return;
        if (mPlayerCost <=0) {

            if(mCode == FinishCode.DEALERBUSTED || mCode == FinishCode.PLAYER || mCode == FinishCode.DRAW)
            {
                mState = STATE.PLYAER_NOT_SQUANDER;
                return;
            }

            mState = STATE.PLYAER_SQUANDERD;

        } else {
            mState = STATE.PLYAER_NOT_SQUANDER;
        }
    }
    private void DisplayResult() {

        int flow = mPlayerCost - mFlowCost;

        mFrame.SetText(null);
        mFrame.AddText("\n\n   --------------- BlackJack Game ---------------\n\n");
        if(flow >0)
        {
            mFrame.AddText("   You earned $"+ flow);
        }
        else if (flow <0)
        {
            mFrame.AddText("   You lost $"+ (-flow));
        }
        else
        {
            mFrame.AddText("   No $ changed");
        }
        mFrame.AddText("\n\n");
        mFrame.AddText("   -------------------------------------------------\n");
        mState = STATE.FINISH;
        mFrame.AddText(" If you want to leave, please press the E ");

    }
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }
    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        switch (e.getKeyCode()) {
            case KeyEvent.VK_H: {
                if (mState != STATE.INGAME)
                    break;
                Hit();
                DisplayInfo();
                break;
            }
            case KeyEvent.VK_S: {
                if (mState != STATE.INGAME)
                    break;
                Stand();
                DisplayInfo();
            }
            case KeyEvent.VK_UP: {
                if (mState != STATE.BET)
                    break;

                if(mCurrentBetCost +10 > mPlayerCost) break;

                mCurrentBetCost +=10;

                DealMoney();
                break;
            }
            case KeyEvent.VK_DOWN: {
                if (mState != STATE.BET)
                    break;
                if(mCurrentBetCost -10 <10) break;

                mCurrentBetCost -=10;
                DealMoney();
                break;
            }
            case KeyEvent.VK_ENTER: {
                if (mState != STATE.BET)
                    break;
                mPlayerCost -= mCurrentBetCost;
                mState = STATE.INGAME;
                DisplayInfo();
                break;
            }
            case KeyEvent.VK_Y: {
                if (mState != STATE.PLYAER_NOT_SQUANDER)
                    break;

                ContinueGame();
                break;
            }
            case KeyEvent.VK_N: {
                if (mState != STATE.PLYAER_NOT_SQUANDER)
                    break;

                DisplayResult();
                break;
            }
            case KeyEvent.VK_1: {
                if (mState != STATE.LOADGAME)
                    break;

                mState = STATE.BET;
                ContinueGame();
                break;
            }
            case KeyEvent.VK_2: {
                if (mState != STATE.LOADGAME )
                    break;
                mState = STATE.FINISH;
                DisplayResult();
                break;
            }
            case KeyEvent.VK_E:{
                if(mState!=STATE.FINISH)
                    break;
                if(count==0) {
                    MainApp.updateScore(addPoint);
                }
                count++;
                mainApp.showScreen("GameSelection");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }
}