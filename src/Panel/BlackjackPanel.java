package Panel;


import Game.BlackJack;
import Main.MainApp;

import javax.swing.*;
import java.awt.*;

public class BlackjackPanel extends JPanel {
    private JTextArea displayArea;

    public BlackjackPanel(MainApp mainApp) {
        setLayout(new BorderLayout());
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.addKeyListener(new BlackJack(this,mainApp));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        setFocusable(true);
        requestFocusInWindow();

    }

    public void SetText(String text) {
        displayArea.setText(text);
    }

    public void AddText(String text) {
        displayArea.append(text);
    }
}

