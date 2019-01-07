package com.github.ghmk5.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * 与えられたファイルの各行をリストとして表示し、ダブルクリックされた行の内容をpublicな変数に入れて外からアクセス可能にするだけのダイアログ
 * 
 * @author mk5
 *
 */
public class DialogListFilesToOpen extends JDialog {

  public String selectedLine;

  public DialogListFilesToOpen(Frame owner, File listFile) throws IOException {
    super(owner);

    getContentPane().setLayout(new BorderLayout());
    JPanel panel = new JPanel();
    getContentPane().add(panel, BorderLayout.CENTER);

    DefaultListModel<String> listModel = new DefaultListModel<>();

    FileInputStream fis = new FileInputStream(listFile);
    InputStreamReader iReader = new InputStreamReader(fis, "UTF-8");
    BufferedReader br = new BufferedReader(iReader);
    String line = "";
    while (line != null) {
      line = br.readLine();
      listModel.addElement(line);
    }
    br.close();

    JList list = new JList(listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    list.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2) {
          selectedLine = (String) list.getSelectedValue();
          dispose();
        }
      }
    });

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.getViewport().setView(list);

    panel.add(scrollPane, BorderLayout.CENTER);

    pack();

  }

}
