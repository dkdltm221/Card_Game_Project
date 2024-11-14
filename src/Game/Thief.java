package Game;

import Card.Start;
import Deck.Player;
import system.Select;
import system.Throw;
import Card.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


class MainBackgroundImg extends JComponent{
    @Override
    public void paintComponents(Graphics g) {
        super.repaint();
        super.paintComponents(g);
        g.drawImage(new ImageIcon("img/game_bg.jpg").getImage(),0,0,this);
    }
}
public class Thief extends JFrame {
    int val1 = -1;
    int val2 = -1;
    int val3 = -1;
    int check;

    Player com = new Player();
    Player player = new Player();

    Card[] com_array;
    Card[] player_array;
    Card[] trash;

    Select comturn = new Select();

    int throw_card[] = new int[2];
    int index = 0;

    public Thief() {
        setTitle("도둑잡기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new MainBackgroundImg());

        setLayout(null);

        Start game = new Start();
        game.GameSet(com, player);

        if (com.getSize() > player.getSize()) {
            check = 0;
        } else {
            comturn.select_com(com, player);
            check = 0;
        }

        ArrayDesktop();

        setSize(1200, 750);
        setVisible(true);
    }

    public void ArrayDesktop() {
        if (com.getSize() == 0) {
            JOptionPane.showMessageDialog(null, "패배", "결과", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        } else if (player.getSize() == 0) {
            JOptionPane.showMessageDialog(null, "승리", "결과", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }
        JButton bring = new JButton("가져오기");
        bring.setBorderPainted(false);
        bring.setLocation(1075,215);
        bring.setSize(100,140);

        add(bring);
        bring.addMouseListener(new MouseBring());
        int num = com.getSize()-1;
        int buttonset = (1200-(100+(50*num)))/2;

        if(com_array != null){
            for(int i = 0; i< com_array.length;i++){
                com_array[i].setVisible(false);
            }
        }

        com_array = new Card[com.getSize()];

        for(int i = 0; i<com.getSize();i++){
            com_array[i] = new Card(new ImageIcon("img/CardDown.png"));
            com_array[i].setValue(com.getCard(i));
            com_array[i].setLocation(buttonset+(i*50),90);
            com_array[i].setSize(100,140);
            add(com_array[i]);
            com_array[i].addMouseListener(new ComSelectCard());
        }

        if(trash != null){
            for(int i = 0; i< trash.length;i++){
                trash[i].setVisible(false);
            }
        }

        for(int i = 0; i<2;i++){
            throw_card[i] = Throw.trash.get(Throw.trash.size()-(2-i));
        }

        trash = new Card[2];

        for(int i=0; i<2; i++){
            trash[i] = new Card(new ImageIcon("img/"+throw_card[i]+".png"));
            trash[i].setLocation(500+(i*110),285);
            trash[i].setSize(100,140);
            add(trash[i]);
        }

        JButton next = new JButton(new ImageIcon("img/NextBtn.gif"));
        next.setBorderPainted(false);
        next.setLocation(1075,346);
        next.setSize(60,30);
        add(next);
        next.addMouseListener(new MouseNext());

        num = player.getSize()-1;
        buttonset = (1200-(100+(50*num)))/2;

        if(player_array != null){
            for(int i = 0; i< player_array.length;i++){
                player_array[i].setVisible(false);
            }
        }
        player_array = new Card[player.getSize()];

        for(int i = 0; i< player.getSize(); i++){
            player_array[i] = new Card(new ImageIcon("img/"+player.getCard(i)+".png"));
            player_array[i].setValue(player.getCard(i));
            player_array[i].setLocation(buttonset+(i*50),480);
            player_array[i].setSize(100,40);
            add(player_array[i]);
            player_array[i].addMouseListener(new MouseSelectCard());
        }

        JButton delete = new JButton();
        delete.setBorderPainted(false);
        delete.setLocation(1075,480);
        delete.setSize(100,40);
        add(delete);
        delete.addMouseListener(new MouseDelete());

        this.repaint();
    }

    class MouseBring extends MouseAdapter{

        public void mousePressed(MouseEvent e){
            if(check == 0){
                if(val3 != -1){
                    for(int i = 0; i<com.getSize(); i++){
                        if(com.getCard(i) == val3){
                            player.addCard(com.getCard(i));
                            com.throwCard(i);
                            val3 = -1;
                            i--;
                            check = 1;
                            break;
                        }
                    }
                }else{
                    val3 = -1;
                }
            }
            ArrayDesktop();
        }
    }
    class ComSelectCard extends MouseAdapter{

        public void mousePressed(MouseEvent e){

            Card seletedBtn = (Card) e.getSource();

            if(val3 != -1){

                if(seletedBtn.isSelect()){
                    seletedBtn.toggleSelect();
                    seletedBtn.setLocation(seletedBtn.getX(),seletedBtn.getY()-20);
                    val3 = -1;
                }
            }else{
                if(!seletedBtn.isSelect()){
                    seletedBtn.toggleSelect();
                    seletedBtn.setLocation(seletedBtn.getX(),seletedBtn.getY()+20);
                    val3 = seletedBtn.getValue();
                }
            }
        }
    }
    class MouseSelectCard extends MouseAdapter{

        public void mousePressed(MouseEvent e){
            Card seletedBtn = (Card) e.getSource();

            if(val1 != -1 && val2 != -1){

                if(seletedBtn.isSelect()){
                    seletedBtn.toggleSelect();
                    seletedBtn.setLocation(seletedBtn.getX(),seletedBtn.getY()+20);

                    if(seletedBtn.getValue() == val1){
                        val1 = val2;
                        val2 = -1;
                    } else {
                        val2 = -1;
                    }
                }
            } else {

                if(seletedBtn.isSelect()){
                    seletedBtn.toggleSelect();
                    seletedBtn.setLocation(seletedBtn.getX(),seletedBtn.getY()+20);

                    if(seletedBtn.getValue() == val1){
                        val1 = val2;
                        val2 = -1;
                    } else {
                        val2 = -1;
                    }
                } else {
                    seletedBtn.toggleSelect();
                    seletedBtn.setLocation(seletedBtn.getX(),seletedBtn.getY()-20);

                    if(val1 == -1){
                        val1 = seletedBtn.getValue();
                    } else{
                        val2 = seletedBtn.getValue();
                    }
                }
            }
        }
    }
    class MouseDelete extends MouseAdapter{

        public void mousePressed(MouseEvent e){
            if(val1 != -1 && val2 != -1){
                if((val1 % 13)==(val2 % 13)){
                    int playersize = player.getSize();
                    for(int i = 0; i<playersize;i++){
                        if(player.getCard(i)==val1){
                            Throw.trash.add((player.getCard(i)));
                            player.throwCard(i);
                            val1 = -1;

                            break;
                        }
                    }
                    for(int i=0; i<playersize-1;i++){
                        if(player.getCard(i)==val2){
                            Throw.trash.add((player.getCard(i)));
                            player.throwCard(i);
                            val2 = -1;

                            break;
                        }
                    }
                }
                else{
                    val1 = -1;
                    val2 = -1;
                }
            }
            ArrayDesktop();
        }
    }

    class MouseNext extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            if(check == 1){
                comturn.select_com(com,player);
                check = 0;
            }
            ArrayDesktop();
        }
    }
    public static void main(String[] args){
        new Thief();
    }
}
