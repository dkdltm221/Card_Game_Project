package Panel;

public class Carddd
{
    private String mSuit;
    private int mRank;

    //A카드를 11로 취급할지, 1로 취급할지 결정하는 boolean 함수
    //Rank(실제 값)을 변경하는것은 위험할 수 있다고 판단하여 boolean으로 기본 값은 건드리지 않기로 결정
    private boolean mIsACalc11;

    //기본적으로 A는 11로 취급한다.
    public Carddd(String suit, int rank)
    {
        this.mSuit = suit;
        this.mRank = rank;

        this.mIsACalc11 =true;
    }

    public String GetSuit()
    {
        return mSuit;
    }

    //GetRank를 호출하면 10을 초과하는 값은 10으로 리턴하고
    //A카드는 mIsACalc11의 참거짓에 따라 11, 1로 리턴한다.
    //그 외는 Rank에 맞게 리턴
    public int GetRank()
    {
        if(mRank >10)
        {
            return 10;
        }

        if(mRank ==1)
        {
            if(mIsACalc11)
            {
                return 11;
            }

            else
            {
                return 1;
            }
        }

        return mRank;
    }

    public void SetACalcTo11(boolean flag)
    {
        mIsACalc11 = flag;
    }

    public String DisplayCard()
    {
        String tempRank =null;

        switch(mRank)
        {
            case 1:
            {
                tempRank ="A";
                break;
            }

            case 11:
            {
                tempRank ="J";
                break;
            }

            case 12:
            {
                tempRank ="Q";
                break;
            }

            case 13:
            {
                tempRank ="K";
                break;
            }

            default:
            {
                tempRank = Integer.toString(mRank);
            }
        }

        return tempRank +"("+ mSuit +")  ";
    }
}