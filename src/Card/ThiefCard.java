package Card;

public class ThiefCard implements Card{
    private String mSuit;
    private int mRank;

    public ThiefCard(String suit, int rank)
    {
        this.mSuit = suit;
        this.mRank = rank;
    }


    @Override
    public String getName() {
        return mSuit;
    }

    @Override
    public int getValue() {
        return mRank;
    }
}