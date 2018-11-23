package list;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

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
   * Launch the application.
   */
  public static void main(String[] args) {
    try {
      DialogConverterSettings dialog = new DialogConverterSettings();
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setVisible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Create the dialog.
   */
  public DialogConverterSettings() {
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
        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
      }
      {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
      }
    }

    JTabbedPane tabbedpane = new JTabbedPane();

    JPanel tabPanel1 = new JPanel();
    tabPanel1.add(new JButton("button1"));

    JPanel tabPanel2 = new JPanel();
    tabPanel2.add(new JLabel("Name:"));
    tabPanel2.add(new JTextField("", 10));

    tabbedpane.addTab("tab1", tabPanel1);
    tabbedpane.addTab("tab2", tabPanel2);

    contentPanel.add(tabbedpane);

  }

}
