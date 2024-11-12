package Panel;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class TablePanel extends JPanel implements ListSelectionListener {

    static private TablePanel tablePanel = null;

    static public TablePanel GetInstance() {
        if (tablePanel == null)
            tablePanel = new TablePanel();
        return tablePanel;
    }

    private TablePanel() {
        super(new BorderLayout());
    }

    JTable table;
    DefaultTableModel tableModel;
    int selectedIndex = -1;



    void addComponentsToPane() {
        initTablePane("user.txt");
        JScrollPane center = new JScrollPane(table);
        add(center, BorderLayout.CENTER);


    }

    String columnNames[] = { "등수", "이름", "점수"};

    void initTablePane(String filename) {
        tableModel = new DefaultTableModel(columnNames, 0);
        loadData(filename);

        table = new JTable(tableModel);
        ListSelectionModel rowSM = table.getSelectionModel();
        rowSM.addListSelectionListener(this);

        table.setFont(new Font("굴림", Font.PLAIN, 15));
        table.setRowHeight(22);
        table.setPreferredScrollableViewportSize(new Dimension(500, 220));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // 엔진에서 검색된 객체들을 테이블에 보여준다. kwd가 ""면 모두 출력
    void loadData(String filename) {
        Scanner scan = openFile(filename);
        tableModel.setRowCount(0);
        int s=1;
        while (scan.hasNext()) {
            String line = s+" "+scan.nextLine();
            tableModel.addRow(line.split(" "));
            s++;
        }
    }

    void addRow(String[] editTexts) {
        tableModel.addRow(editTexts);
    }

    // 현재 선택된 행을 엔진에서 삭제하고 테이블에서 삭제
    void removeRow() {
        if (selectedIndex < 0)
            return;
        String key = (String) tableModel.getValueAt(selectedIndex, 0);
        tableModel.removeRow(selectedIndex);
    }

    // 현재 선택된 행을 편집창의 내용으로 수정
    void updateRow(String[] editTexts) {
        if (selectedIndex < 0)
            return;
        for (int i = 0; i < editTexts.length; i++) {
            tableModel.setValueAt(editTexts[i], selectedIndex, i);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }

    Scanner openFile(String filename) {
        Scanner filein = null;
        try {
            filein = new Scanner(new File(filename));
        } catch (Exception e) {
            System.out.println(filename + ": 파일 없음");
            System.exit(0);
        }
        return filein;
    }
}