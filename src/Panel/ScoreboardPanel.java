package Panel;

import javax.swing.*;
import java.awt.*;
import Main.MainApp;
public class ScoreboardPanel extends JPanel {
    private MainApp mainApp;

    public ScoreboardPanel(MainApp app) {
        this.mainApp = app;

        setLayout(new BorderLayout());
        // TablePanel 인스턴스를 생성하여 중앙에 추가
        TablePanel tablePanel = TablePanel.GetInstance();
        tablePanel.addComponentsToPane(); // 테이블 초기화
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> mainApp.showScreen("GameSelection"));

        // TablePanel을 중앙에 추가하고 버튼을 하단에 추가
        add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}
