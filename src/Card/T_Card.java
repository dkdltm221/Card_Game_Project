package Card;

import javax.swing.*;

public class T_Card extends JButton{
    int value;
    boolean select;

    public T_Card(ImageIcon image){
        super(image);
    }

    public void setValue(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    //select 관련 메서드
    public boolean isSelect(){
        return select;
    }

    public void toggleSelect(){
        this.select = !select;
    }
}
