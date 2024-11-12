package Card;

public class PlayCard implements Card {
    private String name;
    private int value;

    public PlayCard(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getValue() {
        return value;
    }
}
