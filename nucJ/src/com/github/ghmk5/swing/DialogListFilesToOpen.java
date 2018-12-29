package com.github.ghmk5.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class DialogListFilesToOpen extends JDialog {

  public File fileToOpen;

  public DialogListFilesToOpen(Frame owner, ArrayList<File> listFilesToOpen) {
    super(owner);

    getContentPane().setLayout(new BorderLayout());
    JPanel panel = new JPanel();
    getContentPane().add(panel, BorderLayout.CENTER);

    DefaultListModel<String> listModel = new DefaultListModel<>();

    HashMap<String, File> mapNameToFile = new HashMap<>();
    for (int i = 0; i < listFilesToOpen.size(); i++) {
      File file = listFilesToOpen.get(i);
      String fileName = file.toPath().getFileName().toString();
      mapNameToFile.put(fileName, file);
      listModel.addElement(fileName);
    }

    JList list = new JList(listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    list.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2) {
          String fileName = (String) list.getSelectedValue();
          fileToOpen = mapNameToFile.get(fileName);
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
