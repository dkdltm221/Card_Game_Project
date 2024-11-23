package Main;


import Deck.Deck;
import Login.Login;
import User.User;

import javax.swing.*;
import java.awt.*;
import Panel.GameSelectionPanel;
import Panel.ScoreboardPanel;

import Panel.ThiefPanel;
import blackjack.Game;


public class MainApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private static User user = null;
    static InputUsers inputUsers = new InputUsers();
    private ScoreboardPanel scoreboardPanel;
    public MainApp(String userName) {
        setTitle("Mini Game App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        inputUsers.readAll();
        user = setUserName(userName);
        scoreboardPanel = new ScoreboardPanel(this);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(new GameSelectionPanel(this), "GameSelection");
        mainPanel.add(scoreboardPanel, "Scoreboard");
        mainPanel.add(new Game(this),"BlackjackPanel");
        mainPanel.add(new ThiefPanel(this),"ThiefPanel");
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

        InputUsers.writeSortedToFile();
    }
    public void updateScore(){
            scoreboardPanel.updateScores();
            // 다른 패널들도 필요 시 업데이트
    }
    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }


}