package com.github.ghmk5.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.github.ghmk5.info.Properties;

public class DialogCopyFile extends JDialog {

  Properties props;
  public String str;
  public JCheckBox cbAoTxt;
  public JCheckBox cbEpubTxt;
  JLabel lblAoDst;
  JLabel lblEpubDst;
  public String copyDstAoTxt;
  public String copyDstEpub3;
  boolean selectingAoDst;
  public boolean accepted;

  public DialogCopyFile(Frame owner, String title, boolean modal, Properties props) {
    super(owner, title, modal);
    this.props = props;
    copyDstAoTxt = props.getProperty("CopyDstAoTxt");
    copyDstEpub3 = props.getProperty("CopyDstEpub3");
    accepted = false;

    getContentPane().setLayout(new BorderLayout());

    // 注意書きパネル
    JPanel labelPanel = new JPanel();

    // 注意書きパネル内 注意書きラベル
    JLabel label = new JLabel();
    label.setText("※ 既存の同名ファイルは上書きされます");
    labelPanel.add(label);
    getContentPane().add(labelPanel, BorderLayout.NORTH);

    // メインパネル
    JPanel mainPanel = new JPanel();
    GridBagLayout gbLayout = new GridBagLayout();
    mainPanel.setLayout(gbLayout);
    GridBagConstraints gbConstraints = new GridBagConstraints();
    gbConstraints.anchor = GridBagConstraints.WEST;

    cbAoTxt = new JCheckBox("青空文庫Txtファイルをコピーする");
    JButton btnSelAoDst = new JButton("コピー先選択");
    btnSelAoDst.addActionListener(new DirChooserListner(this, true));
    if (Objects.nonNull(copyDstAoTxt)) {
      lblAoDst = new JLabel(copyDstAoTxt);
    } else {
      lblAoDst = new JLabel("(青空文庫Txtファイルコピー先)");
    }

    cbEpubTxt = new JCheckBox("epub3ファイルをコピーする");
    JButton btnSelEpubDst = new JButton("コピー先選択");
    btnSelEpubDst.addActionListener(new DirChooserListner(this, false));
    if (Objects.nonNull(copyDstEpub3)) {
      lblEpubDst = new JLabel(copyDstEpub3);
    } else {
      lblEpubDst = new JLabel("(epub3ファイルコピー先)");
    }

    gbConstraints.insets = new Insets(5, 5, 0, 5);

    gbConstraints.gridx = 0;
    gbConstraints.gridy = 0;
    gbLayout.setConstraints(cbAoTxt, gbConstraints);
    mainPanel.add(cbAoTxt);

    gbConstraints.gridx = 1;
    gbConstraints.gridy = 0;
    gbLayout.setConstraints(btnSelAoDst, gbConstraints);
    mainPanel.add(btnSelAoDst);

    gbConstraints.gridx = 2;
    gbConstraints.gridy = 0;
    gbLayout.setConstraints(lblAoDst, gbConstraints);
    mainPanel.add(lblAoDst);

    gbConstraints.insets = new Insets(5, 5, 5, 5);

    gbConstraints.gridx = 0;
    gbConstraints.gridy = 1;
    gbLayout.setConstraints(cbEpubTxt, gbConstraints);
    mainPanel.add(cbEpubTxt);

    gbConstraints.gridx = 1;
    gbConstraints.gridy = 1;
    gbLayout.setConstraints(btnSelEpubDst, gbConstraints);
    mainPanel.add(btnSelEpubDst);

    gbConstraints.gridx = 2;
    gbConstraints.gridy = 1;
    gbLayout.setConstraints(lblEpubDst, gbConstraints);
    mainPanel.add(lblEpubDst);

    // getContentPane().add(labelPanel, BorderLayout.NORTH);
    getContentPane().add(mainPanel, BorderLayout.CENTER);

    JPanel lowerPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    lowerPanel.add(cancelButton);
    JButton okButton = new JButton("OK");
    okButton.addActionListener(new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {
        accepted = true;
        File dstDir;
        boolean filled = false;
        if (cbAoTxt.isSelected()) {
          dstDir = new File(lblAoDst.getText());
          if (dstDir.canWrite() && dstDir.isDirectory()) {
            filled = true;
          }
        }
        if (cbEpubTxt.isSelected()) {
          dstDir = new File(lblEpubDst.getText());
          if (dstDir.canWrite() && dstDir.isDirectory()) {
            filled = true;
          } else {
            filled = false;
          }
        }
        if (!filled) {
          JOptionPane.showMessageDialog(mainPanel, "書込み可能なコピー先ディレクトリを指定してください", "エラー",
              JOptionPane.ERROR_MESSAGE);
        } else {
          dispose();
        }

      }
    });
    lowerPanel.add(okButton);
    getContentPane().add(lowerPanel, BorderLayout.SOUTH);

    pack();
    setLocationRelativeTo(owner);
  }

  class DirChooserListner implements ActionListener {
    DialogCopyFile d;
    boolean selectingAoDst;

    @Override
    public void actionPerformed(ActionEvent e) {
      String pathString;
      if (selectingAoDst) {
        pathString = d.copyDstAoTxt;
      } else {
        pathString = d.copyDstEpub3;
      }
      JFileChooser fileChooser = new JFileChooser(pathString);
      fileChooser.setDialogTitle("コピー先を選択");
      fileChooser.setApproveButtonText("選択");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int state = fileChooser.showOpenDialog(d);
      if (state == JFileChooser.APPROVE_OPTION) {
        pathString = fileChooser.getSelectedFile().getAbsolutePath();
        if (selectingAoDst) {
          d.copyDstAoTxt = pathString;
          d.lblAoDst.setText(pathString);
          // System.out.println("青空文庫Txtのコピー先: " + d.copyDstAoTxt);
        } else {
          d.copyDstEpub3 = pathString;
          d.lblEpubDst.setText(pathString);
          // System.out.println("epub3ファイルのコピー先: " + d.copyDstEpub3);
        }
      }
    }

    public DirChooserListner(DialogCopyFile d, boolean selectingAoDst) {
      super();
      this.d = d;
      this.selectingAoDst = selectingAoDst;
    }

  }

  public static void main(String[] args) {
    // TODO 自動生成されたメソッド・スタブ
    Properties props = new Properties();
    DialogCopyFile dialogCopyFile = new DialogCopyFile(null, "hoge", true, props);
    dialogCopyFile.setVisible(true);

  }

}
