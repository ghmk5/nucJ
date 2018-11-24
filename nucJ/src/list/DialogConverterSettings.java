package list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.github.hmdev.info.BookInfo;

public class DialogConverterSettings extends JDialog {

  // GUIオブジェクトのレイアウトに関する変数
  Border padding0 = BorderFactory.createEmptyBorder(0, 0, 0, 0);
  Border padding1 = BorderFactory.createEmptyBorder(1, 1, 1, 1);
  Border padding2 = BorderFactory.createEmptyBorder(2, 2, 2, 2);
  Border padding3 = BorderFactory.createEmptyBorder(3, 3, 3, 3);
  Border padding5H3V = BorderFactory.createEmptyBorder(3, 5, 3, 5);
  Border padding2H = BorderFactory.createEmptyBorder(0, 2, 0, 2);
  Border padding4H = BorderFactory.createEmptyBorder(0, 4, 0, 4);
  Border padding4H2V = BorderFactory.createEmptyBorder(2, 4, 2, 4);
  Border padding3B = BorderFactory.createEmptyBorder(0, 0, 3, 0);
  Border padding4B = BorderFactory.createEmptyBorder(0, 0, 4, 0);
  Border iconPadding = BorderFactory.createEmptyBorder(0, 14, 0, 0);

  Dimension panelSize = new Dimension(1920, 26);
  Dimension panelSize28 = new Dimension(1920, 28);
  Dimension panelVMaxSize = new Dimension(640, 22);

  JTextField text = new JTextField(); // ひょっとして、fontmetricsを取得するために使用しないインスタンスを生成してる？
  FontMetrics fm = this.getFontMetrics(text.getFont());
  Insets is = text.getInsets();
  Dimension text3 = new Dimension(fm.stringWidth("000") + is.left + is.right + 2, 20);
  Dimension text4 = new Dimension(fm.stringWidth("0000") + is.left + is.right + 2, 20);
  Dimension text5 = new Dimension(fm.stringWidth("00000") + is.left + is.right + 2, 20);
  Dimension text300 = new Dimension(300, 20);
  Dimension combo3 = new Dimension(text3.width + 20, 20);

  // 設定パラメータを保持するGUIオブジェクト
  /** 表題 */
  JComboBox jComboTitle;
  JCheckBox jCheckPubFirst;
  JCheckBox jCheckUseFileName;
  JCheckBox jCheckAutoFileName;
  JCheckBox jCheckTitlePage;
  JRadioButton jRadioTitleNormal;
  JRadioButton jRadioTitleMiddle;
  JRadioButton jRadioTitleHorizontal;

  JCheckBox jCheckConfirm;

  /**
   * Create the dialog.
   */
  public DialogConverterSettings(Frame owner) {
    super(owner);
    setTitle("設定");
    setBounds(100, 100, 600, 300);

    getContentPane().setLayout(new BorderLayout());

    JTabbedPane tabbedpane = new JTabbedPane();
    getContentPane().add(tabbedpane, BorderLayout.CENTER);

    JPanel buttonPane = new JPanel();
    buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
    getContentPane().add(buttonPane, BorderLayout.SOUTH);
    JButton cancelButton = new JButton("Cancel");
    cancelButton.setActionCommand("Cancel");
    cancelButton.setAction(new ActionCancel());
    buttonPane.add(cancelButton);
    JButton okButton = new JButton("OK");
    okButton.setActionCommand("OK");
    okButton.setAction(new ActionApplyChanges());
    buttonPane.add(okButton);
    getRootPane().setDefaultButton(okButton);

    // "変換"タブ
    JPanel tab1RootPanel = new JPanel();
    tab1RootPanel.setLayout(new BoxLayout(tab1RootPanel, BoxLayout.Y_AXIS));
    tabbedpane.addTab("変換", tab1RootPanel);

    // "変換"タブ内 "表題"グループ
    JPanel tab1InnerPanel1 = new JPanel();
    tab1InnerPanel1 = new JPanel();
    tab1InnerPanel1.setLayout(new BoxLayout(tab1InnerPanel1, BoxLayout.X_AXIS));
    tab1InnerPanel1.setMinimumSize(panelSize);
    tab1InnerPanel1.setMaximumSize(panelSize);
    tab1InnerPanel1.setPreferredSize(panelSize);
    tab1InnerPanel1.setBorder(padding4H2V);
    tab1RootPanel.add(tab1InnerPanel1);
    JLabel label = new JLabel("表題: ");
    tab1InnerPanel1.add(label);
    label = new JLabel("本文内");
    label.setBorder(padding2H);
    tab1InnerPanel1.add(label);
    jComboTitle = new JComboBox(BookInfo.TitleType.titleTypeNames);
    jComboTitle.setFocusable(false);
    jComboTitle.setMaximumSize(new Dimension(200, 22));
    jComboTitle.setBorder(padding0);
    ((JLabel) jComboTitle.getRenderer()).setBorder(padding2H);
    tab1InnerPanel1.add(jComboTitle);
    // 入力ファイル名優先
    jCheckPubFirst = new JCheckBox("先頭が発行者");
    jCheckPubFirst.setFocusPainted(false);
    tab1InnerPanel1.add(jCheckPubFirst);
    // 入力ファイル名優先
    tab1InnerPanel1.add(new JLabel(" "));
    jCheckUseFileName = new JCheckBox("ファイル名優先 ");
    jCheckUseFileName.setFocusPainted(false);
    tab1InnerPanel1.add(jCheckUseFileName);

    // "変換"タブ内 "表紙"グループ
    JPanel tab1InnerPanel2 = new JPanel();
    tab1InnerPanel2 = new JPanel();
    tab1InnerPanel2.setLayout(new BoxLayout(tab1InnerPanel2, BoxLayout.X_AXIS));
    tab1InnerPanel2.setMinimumSize(panelSize);
    tab1InnerPanel2.setMaximumSize(panelSize);
    tab1InnerPanel2.setPreferredSize(panelSize);
    tab1InnerPanel2.setBorder(padding4H2V);
    tab1RootPanel.add(tab1InnerPanel2);
    JLabel labelTab1InnerPanel2 = new JLabel("表紙: ");
    tab1InnerPanel2.add(labelTab1InnerPanel2);

    JTextField jTextMaxCoverLine = new JTextField("10");
    jTextMaxCoverLine.setToolTipText("先頭の挿絵に利用する画像注記を取得する最大行数 0なら制限なし");
    jTextMaxCoverLine.setHorizontalAlignment(JTextField.RIGHT);
    jTextMaxCoverLine.setMinimumSize(text4);
    jTextMaxCoverLine.setMaximumSize(text4);
    jTextMaxCoverLine.setPreferredSize(text4);
    jTextMaxCoverLine.addFocusListener(new TextSelectFocusListener(jTextMaxCoverLine));
    jTextMaxCoverLine.setInputVerifier(new IntegerInputVerifier(10, 0, 9999));
    tab1InnerPanel2.add(jTextMaxCoverLine);
    JLabel jLabelMaxCoverLine = new JLabel("行目までの");
    jLabelMaxCoverLine.setToolTipText(jTextMaxCoverLine.getToolTipText());
    jLabelMaxCoverLine.setBorder(padding2H);
    tab1InnerPanel2.add(jLabelMaxCoverLine);

    JComboBox jComboCover = new JComboBox(new String[] { "[先頭の挿絵]", "[入力ファイル名と同じ画像(png,jpg)]", "[表紙無し]", "http://" });
    jComboCover.setMinimumSize(new Dimension(54, 27));
    jComboCover.setEditable(true);
    // jComboCover.setPreferredSize(new Dimension(80, 24));
    jComboCover.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        boolean visible = jComboCover.getSelectedIndex() == 0;
        jTextMaxCoverLine.setVisible(visible);
        jLabelMaxCoverLine.setVisible(visible);
      }
    });
    tab1InnerPanel2.add(jComboCover);
    // new DropTarget(jComboCover.getEditor().getEditorComponent(),
    // DnDConstants.ACTION_COPY_OR_MOVE,
    // new DropCoverListener(), true);
    boolean visible = jComboCover.getSelectedIndex() == 0;
    jTextMaxCoverLine.setVisible(visible);
    jLabelMaxCoverLine.setVisible(visible);

    JButton jButtonCover = new JButton("選択");
    jButtonCover.setBorder(padding3);
    jButtonCover.setIcon(new ImageIcon(DialogConverterSettings.class.getResource("/images/cover.png")));
    jButtonCover.setFocusPainted(false);
    // jButtonCover.addActionListener(new CoverChooserListener(this));
    tab1InnerPanel2.add(jButtonCover);
    JCheckBox jCheckCoverHistory = new JCheckBox("表紙履歴利用", true);
    jCheckCoverHistory.setToolTipText("前回の変換(またはスキップ)で設定した表紙を利用します ※履歴は再起動時に初期化されます");
    jCheckCoverHistory.setFocusPainted(false);
    tab1InnerPanel2.add(jCheckCoverHistory);

    // "変換"タブ内 "ページ出力"グループ

    JPanel tab1InnerPanel3 = new JPanel();
    tab1InnerPanel3.setLayout(new BoxLayout(tab1InnerPanel3, BoxLayout.X_AXIS));
    tab1InnerPanel3.setMinimumSize(panelSize);
    tab1InnerPanel3.setMaximumSize(panelSize);
    tab1InnerPanel3.setPreferredSize(panelSize);
    tab1InnerPanel3.setBorder(padding4H2V);
    tab1RootPanel.add(tab1InnerPanel3);
    // ページ出力
    JLabel labelTab1InnerPanel3 = new JLabel("ページ出力:");
    tab1InnerPanel3.add(labelTab1InnerPanel3);
    JCheckBox jCheckCoverPage = new JCheckBox("表紙画像 ", true);
    jCheckCoverPage.setFocusPainted(false);
    tab1InnerPanel3.add(jCheckCoverPage);
    // 左右中央
    JCheckBox jCheckTitlePage = new JCheckBox("表題", true);
    jCheckTitlePage.setToolTipText("表題を単独のページで出力します。チェック無し時は表題等は出力されません");
    jCheckTitlePage.setFocusPainted(false);
    tab1InnerPanel3.add(jCheckTitlePage);
    JLabel labelParenLeft = new JLabel("(");
    tab1InnerPanel3.add(labelParenLeft);
    ButtonGroup buttonGroupTitle = new ButtonGroup();
    JRadioButton jRadioTitleNormal = new JRadioButton("本文内 ");
    jRadioTitleNormal.setToolTipText("別ページ処理せずに本文中に表題等を出力します。 目次は表題前に出力されます");
    jRadioTitleNormal.setBorder(padding0);
    jRadioTitleNormal.setIconTextGap(1);
    tab1InnerPanel3.add(jRadioTitleNormal);
    buttonGroupTitle.add(jRadioTitleNormal);
    JRadioButton jRadioTitleMiddle = new JRadioButton("中央 ", true);
    jRadioTitleMiddle.setToolTipText("中央寄せの表題ページを出力します");
    jRadioTitleMiddle.setBorder(padding0);
    jRadioTitleMiddle.setIconTextGap(1);
    tab1InnerPanel3.add(jRadioTitleMiddle);
    buttonGroupTitle.add(jRadioTitleMiddle);
    jRadioTitleHorizontal = new JRadioButton("横書き");
    jRadioTitleHorizontal.setToolTipText("横書きの表題ページを出力します");
    jRadioTitleHorizontal.setBorder(padding0);
    jRadioTitleHorizontal.setIconTextGap(1);
    tab1InnerPanel3.add(jRadioTitleHorizontal);
    buttonGroupTitle.add(jRadioTitleHorizontal);
    JLabel labelParenRight = new JLabel(") ");
    tab1InnerPanel3.add(labelParenRight);
    JCheckBox jCheckTocPage = new JCheckBox("目次");
    jCheckTocPage.setToolTipText("目次ページを表題ページの次に追加します");
    jCheckTocPage.setFocusPainted(false);
    tab1InnerPanel3.add(jCheckTocPage);
    JLabel labelParenLeft2 = new JLabel("(");
    tab1InnerPanel3.add(labelParenLeft2);
    ButtonGroup buttonGroupIndex = new ButtonGroup();
    JRadioButton jRadioTocV = new JRadioButton("縦 ", true);
    jRadioTocV.setFocusPainted(false);
    jRadioTocV.setIconTextGap(2);
    jRadioTocV.setBorder(padding0);
    tab1InnerPanel3.add(jRadioTocV);
    buttonGroupIndex.add(jRadioTocV);
    JRadioButton jRadioTocH = new JRadioButton("横");
    jRadioTocH.setFocusPainted(false);
    jRadioTocH.setIconTextGap(2);
    jRadioTocH.setBorder(padding0);
    tab1InnerPanel3.add(jRadioTocH);
    buttonGroupIndex.add(jRadioTocH);
    JLabel labelParenRight2 = new JLabel(")");
    tab1InnerPanel3.add(labelParenRight2);

    // JPanel tabPanel2 = new JPanel();
    // tabPanel2.add(new JLabel("Name:"));
    // tabPanel2.add(new JTextField("", 10));
    //
    // JPanel tabPanel3 = new JPanel();
    //
    // JPanel tabPanel4 = new JPanel();
    //
    // JPanel tabPanel5 = new JPanel();
    //
    // JPanel tabPanel6 = new JPanel();
    //
    // JPanel tabPanel7 = new JPanel();

    // tabbedpane.addTab("画像1", tabPanel2);
    // tabbedpane.addTab("画像2", tabPanel3);
    // tabbedpane.addTab("詳細設定", tabPanel4);
    // tabbedpane.addTab("目次", tabPanel5);
    // tabbedpane.addTab("スタイル", tabPanel6);
    // tabbedpane.addTab("Web", tabPanel7);

  }

  ////////////////////////////////////////////////////////////////
  class TextSelectFocusListener implements FocusListener {
    JTextField jTextField;

    TextSelectFocusListener(JTextField jTextField) {
      this.jTextField = jTextField;
    }

    public void focusLost(FocusEvent e) {
    }

    public void focusGained(FocusEvent e) {
      this.jTextField.setSelectionStart(0);
      this.jTextField.setSelectionEnd(10);
    }
  }

  class IntegerInputVerifier extends InputVerifier {
    /** 基準値 */
    int def = 0;
    /** 最小値 */
    int min = Integer.MIN_VALUE;
    /** 最大値 */
    int max = Integer.MAX_VALUE;

    IntegerInputVerifier(int def, int min) {
      this.def = Math.max(def, min);
      this.min = min;
    }

    IntegerInputVerifier(int def, int min, int max) {
      this.def = Math.min(Math.max(def, min), max);
      this.min = min;
      this.max = max;
    }

    @Override
    public boolean verify(JComponent c) {
      JTextField textField = (JTextField) c;
      try {
        int i = (int) Double.parseDouble(textField.getText());
        if (i >= this.min && i <= this.max) {
          textField.setText(Integer.toString(i));
          return true;
        }
        if (this.max != Integer.MAX_VALUE && i > this.max) {
          textField.setText(Integer.toString(this.max));
          return true;
        } else if (i < this.min) {
          textField.setText(Integer.toString(this.min));
          return true;
        }
      } catch (NumberFormatException e) {
        // UIManager.getLookAndFeel().provideErrorFeedback(c);
      }
      textField.setText(Integer.toString(this.def));
      return true;
    }
  }

  class FloatInputVerifier extends InputVerifier {
    /** 基準値 */
    float def = 0;
    /** 最小値 */
    float min = Float.MIN_VALUE;
    /** 最大値 */
    float max = Float.MAX_VALUE;

    // NumberFormat format = NumberFormat.getNumberInstance();

    FloatInputVerifier(float def, float min) {
      this.def = Math.max(def, min);
      this.min = min;
    }

    FloatInputVerifier(float def, float min, float max) {
      this.def = Math.min(Math.max(def, min), max);
      this.min = min;
      this.max = max;
    }

    @Override
    public boolean verify(JComponent c) {
      JTextField textField = (JTextField) c;
      try {
        float f = (float) Double.parseDouble(textField.getText());
        if (f >= this.min && f <= this.max) {
          textField.setText(Float.toString(f));
          return true;
        }
        if (this.max != Float.MAX_VALUE && f > this.max) {
          textField.setText(Float.toString(this.max));
          return true;
        } else if (f < this.min) {
          textField.setText(Float.toString(this.min));
          return true;
        }
      } catch (NumberFormatException e) {
      }
      textField.setText(Float.toString(this.def));
      return true;
    }
  }

  /** 小数点以下が0なら表示しない */
  class NumberVerifier extends InputVerifier {
    /** 基準値 */
    float def = 0;
    /** 最小値 */
    float min = Float.MIN_VALUE;
    /** 最大値 */
    float max = Float.MAX_VALUE;

    NumberFormat format = NumberFormat.getNumberInstance();

    NumberVerifier(float def, float min) {
      this.def = Math.max(def, min);
      this.min = min;
    }

    NumberVerifier(float def, float min, float max) {
      this.def = Math.min(Math.max(def, min), max);
      this.min = min;
      this.max = max;
    }

    @Override
    public boolean verify(JComponent c) {
      JTextField textField = (JTextField) c;
      try {
        float f = (float) Double.parseDouble(textField.getText());
        if (f >= this.min && f <= this.max) {
          textField.setText(this.format.format(f));
          return true;
        }
        if (this.max != Float.MAX_VALUE && f > this.max) {
          textField.setText(this.format.format(this.max));
          return true;
        } else if (f < this.min) {
          textField.setText(this.format.format(this.min));
          return true;
        }
      } catch (NumberFormatException e) {
      }
      textField.setText(this.format.format(this.def));
      return true;
    }
  }

  // GUIオブジェクトの状態をプロファイルに保存してダイアログを閉じる
  private class ActionApplyChanges extends AbstractAction {
    public ActionApplyChanges() {
      putValue(NAME, "Apply Changes");
      putValue(SHORT_DESCRIPTION, "Apply Changes and Close Dialog");
    }

    public void actionPerformed(ActionEvent e) {
      // TODO GUIオブジェクトの状態をプロパティに書き出す処理
      System.out.println("Changes Saved to Props File");
      dispose();
    }
  }

  // 変更を破棄してダイアログを閉じる
  private class ActionCancel extends AbstractAction {
    public ActionCancel() {
      putValue(NAME, "Cancel");
      putValue(SHORT_DESCRIPTION, "Discard Changes and Close Dialog");
    }

    public void actionPerformed(ActionEvent e) {
      System.out.println("Changes Discarded");
      dispose();
    }
  }

}
