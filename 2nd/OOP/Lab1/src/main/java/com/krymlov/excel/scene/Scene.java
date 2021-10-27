package com.krymlov.excel.scene;

import com.krymlov.excel.calculator.Calculator;
import com.krymlov.excel.calculator.LexemeBuffer;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;
import javax.swing.filechooser.FileNameExtensionFilter;

import static com.krymlov.excel.calculator.Calculator.*;

public class Scene extends JFrame {
    private String[] columns = new String[]{};
    private Object[][] data = new Object[][]{{}};
    private final int DEFAULTRAWS = 10;
    private DefaultTableModel model;
    private JTable table;
    private JButton jbtAddRow;
    private JButton jbtAddColumn;
    private JButton jbtDeleteRow;
    private JButton jbtDeleteColumn;
    private JButton jbtCalculate;
    private JButton jbtHelp;
    private JTextField jTextField;
    private JMenuBar jMenuBar;
    private JMenu jMenu;
    private JMenuItem jMenuSetDefaultTable;
    private JMenuItem jMenuItemSave;
    private JFileChooser fileChooser;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;

    public Scene() {
        this.setTitle("Excel");
        this.setSize(800, 700);
        this.setLocationRelativeTo((Component) null);
        this.setDefaultCloseOperation(3);
        this.jTextField = new JTextField();
        setMainIcon();
        initButtons();
        initMainTable();
        initMenuBar();
        initFileChooser();
        initJPanels();
        this.add(jPanel1, "North");
        this.add(jPanel3, "South");
        this.jbtAddRow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Scene.this.table.getSelectedRow() >= 0) {
                    Scene.this.model.insertRow(Scene.this.table.getSelectedRow(), new Vector());
                } else {
                    Scene.this.model.addRow(new Vector());
                }
                prepareToExport(table);
            }
        });
        this.jbtAddColumn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Введіть назву стовпчика:");
                Scene.this.model.addColumn(name);
                prepareToExport(table);
            }
        });
        this.jbtDeleteRow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Scene.this.table.getSelectedRow() >= 0) {
                    Scene.this.model.removeRow(Scene.this.table.getSelectedRow());
                }

            }
        });
        this.jbtDeleteColumn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Scene.this.table.getSelectedColumn() >= 0) {
                    TableColumnModel columnModel = Scene.this.table.getColumnModel();
                    TableColumn column = columnModel.getColumn(Scene.this.table.getSelectedColumn());
                    columnModel.removeColumn(column);
                }

            }
        });
        this.jbtHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File("e:\\Users\\Danil\\Desktop\\KNU2020\\OOP\\Lab1\\src\\main\\resources\\help.txt");
                try {
                    Scanner in = new Scanner(new FileReader(file));
                    StringBuilder sb = new StringBuilder();
                    while (in.hasNextLine()) {
                        sb.append(in.nextLine());
                        sb.append('\n');
                    }
                    in.close();
                    String finalText = sb.toString();
                    JOptionPane.showMessageDialog(table, finalText);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });

        this.jMenuItemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showSaveDialog(Scene.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    prepareToExport(table);
                    exportToExcel(table, fileChooser.getSelectedFile());
                    JOptionPane.showMessageDialog(Scene.this,
                            "Файл '" + fileChooser.getSelectedFile() +
                                    " ) збережено!");
                }
            }
        });
        this.jbtCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jTextField.getText().equals("")) {
                    throw new RuntimeException("Empty textfield!");
                } else {
                    if (jTextField.getText().charAt(0) == '=') {
                        Calculator.setTable(table);
                        String expression = jTextField.getText().substring(1, jTextField.getText().length());
                        List<Lexeme> lexemes = lexAnalyze(expression);
                        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
                        double result = expr(lexemeBuffer);
                        int row = table.getSelectedRow();
                        int column = table.getSelectedColumn();
                        table.setValueAt(result, row, column);
                    }
                }
            }
        });
        this.jMenuSetDefaultTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Scene.this.model.addColumn("A");
                Scene.this.model.addColumn("B");
                Scene.this.model.addColumn("C");
                Scene.this.model.addColumn("D");
                Scene.this.model.addColumn("E");
                for (int i = 0; i < 10; i++) {
                    Scene.this.model.addRow(new Vector());
                }
                prepareToExport(table);
            }
        });
        this.setResizable(true);
        this.setVisible(true);
    }

    private void initFileChooser() {
        this.fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Текстовий документ", "*.TXT");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Сохранение файла");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    private void initMenuBar() {
        this.jMenuBar = new JMenuBar();
        this.jMenu = new JMenu("Файл");
        this.jMenuItemSave = new JMenuItem("Зберегти");
        this.jMenuSetDefaultTable = new JMenuItem("Початкова табл.");
        this.jMenu.add(jMenuSetDefaultTable);
        this.jMenu.add(jMenuItemSave);
        this.jMenuBar.add(jMenu);
        setJMenuBar(jMenuBar);
    }

    private void initMainTable() {
        this.model = new DefaultTableModel(this.data, this.columns);
        this.table = new JTable(this.model);
        this.table.setShowGrid(true);
        this.table.setGridColor(Color.BLACK);
        this.table.setAutoCreateRowSorter(true);
        this.add(new JScrollPane(this.table), "Center");
        this.table.setColumnSelectionAllowed(true);
        this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        for (int i = 0; i < DEFAULTRAWS; i++) {
            Scene.this.model.addRow(new Vector());
        }
    }

    private void initButtons() {
        Icon iconCalc = new ImageIcon("e:\\Users\\Danil\\Desktop\\KNU2020\\OOP\\Lab1\\src\\main\\resources\\calc.png");
        Icon iconAdd = new ImageIcon("e:\\Users\\Danil\\Desktop\\KNU2020\\OOP\\Lab1\\src\\main\\resources\\plus.png");
        Icon iconDel = new ImageIcon("e:\\Users\\Danil\\Desktop\\KNU2020\\OOP\\Lab1\\src\\main\\resources\\minus.png");
        Icon iconHelp = new ImageIcon("e:\\Users\\Danil\\Desktop\\KNU2020\\OOP\\Lab1\\src\\main\\resources\\help.png");
        this.jbtAddRow = new JButton("Новий рядок");
        this.jbtAddColumn = new JButton("Новий стовпчик");
        this.jbtDeleteRow = new JButton("Видалити рядок");
        this.jbtDeleteColumn = new JButton("Видалити стовпчик");
        this.jbtCalculate = new JButton("Розрахувати");
        this.jbtHelp = new JButton("Допомога");
        setButtonStyle(iconCalc, jbtCalculate,"#e1e1e1", "Arial", 16);
        setButtonStyle(iconHelp, jbtHelp,"#e1e1e1", "Arial", 16);
        setButtonStyle(iconAdd, jbtAddRow,"#e1e1e1", "Arial", 16);
        setButtonStyle(iconAdd, jbtAddColumn,"#e1e1e1", "Arial", 16);
        setButtonStyle(iconDel, jbtDeleteRow,"#e1e1e1", "Arial", 16);
        setButtonStyle(iconDel, jbtDeleteColumn,"#e1e1e1", "Arial", 16);
    }

    private void initJPanels() {
        jPanel1 = new JPanel(new GridLayout(1, 2));
        jPanel1.setPreferredSize(new Dimension(800, 30));
        jPanel1.add(this.jTextField);
        jPanel1.add(this.jbtCalculate);
        jPanel1.add(this.jbtHelp);
        jPanel2 = new JPanel(new GridLayout(2, 2));
        jPanel2.setPreferredSize(new Dimension(800, 100));
        jPanel2.add(this.jbtAddRow);
        jPanel2.add(this.jbtAddColumn);
        jPanel2.add(this.jbtDeleteRow);
        jPanel2.add(this.jbtDeleteColumn);
        jPanel3 = new JPanel(new BorderLayout());
        jPanel3.add(jPanel2, "South");
    }

    private void exportToExcel(JTable table, File file) {
        try {
            TableModel model = table.getModel();
            Writer excel = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

            for (int i = 0; i < model.getColumnCount(); i++) {
                excel.write(model.getColumnName(i) + "\t");
            }

            excel.write("\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    excel.write(model.getValueAt(i, j).toString() + "\t");
                }
                excel.write("\n");
            }

            excel.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void prepareToExport(JTable table) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            for (int j = 0; j < table.getRowCount(); j++) {
                if (table.getValueAt(j, i) == null) {
                    table.setValueAt("", j, i);
                }
            }
        }
    }

    private void setButtonStyle(Icon icon, JButton button, String hashcolor, String font, int fontSize) {
        button.setBackground(Color.decode(hashcolor));
        button.setIcon(icon);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font(font, Font.PLAIN, fontSize));
    }

    private void setMainIcon() {
        Image icon = Toolkit.getDefaultToolkit().getImage("e:\\Users\\Danil\\Desktop\\KNU2020\\OOP\\Lab1\\src\\main\\resources\\excel.png");
        this.setIconImage(icon);
    }
}
