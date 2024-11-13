package Main;

import Deck.BlackjackDeck;
import Deck.Deck;
import User.User;

import javax.swing.*;
import java.awt.*;
import Panel.GameSelectionPanel;
import Panel.ScoreboardPanel;
import Panel.BlackjackPanel;
public class MainApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private static User user = null;
    static InputUsers inputUsers = new InputUsers();
    public MainApp() {
        setTitle("Mini Game App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        Deck deck = null;
        deck = new BlackjackDeck();

        inputUsers.readAll();
        user = setUserName("Kim"); //이건 추후에 변경예정

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(new GameSelectionPanel(this), "GameSelection");
        mainPanel.add(new ScoreboardPanel(this), "Scoreboard");
        mainPanel.add(new BlackjackPanel(this),"BlackjackPanel");
        cardLayout.show(mainPanel, "GameSelection"); // Show login screen initially


        add(mainPanel);
        setVisible(true);
    }
    public User setUserName(String id){
        return user = inputUsers.getUser(id);
    }
    public static String getUserName(){
        return user.getName();
    }
    public static int getUserScore(){
        return user.getScore();
    }

    public static void updateScore(int point){
        user.addScore(point);
        InputUsers.userUpdate(user.getName(),point);
    }
    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}