package list;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class DialogConverterSettings extends JDialog {

  private final JPanel contentPanel = new JPanel();

  /**
   * Create the dialog.
   */
  public DialogConverterSettings(Frame owner) {
    super(owner);
    setTitle("設定");
    setBounds(100, 100, 450, 300);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    {
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
      }
      {
        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
      }
    }

    JTabbedPane tabbedpane = new JTabbedPane();

    JPanel tabPanel1 = new JPanel();
    tabPanel1.add(new JButton("button1"));

    JPanel tabPanel2 = new JPanel();
    tabPanel2.add(new JLabel("Name:"));
    tabPanel2.add(new JTextField("", 10));

    JPanel tabPanel3 = new JPanel();

    JPanel tabPanel4 = new JPanel();

    JPanel tabPanel5 = new JPanel();

    JPanel tabPanel6 = new JPanel();

    JPanel tabPanel7 = new JPanel();

    tabbedpane.addTab("変換", tabPanel1);
    tabbedpane.addTab("画像1", tabPanel2);
    tabbedpane.addTab("画像2", tabPanel3);
    tabbedpane.addTab("詳細設定", tabPanel4);
    tabbedpane.addTab("目次", tabPanel5);
    tabbedpane.addTab("スタイル", tabPanel6);
    tabbedpane.addTab("Web", tabPanel7);

    contentPanel.add(tabbedpane);

  }

}
