package list;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.Properties;

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
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.hmdev.info.BookInfo;
import com.github.hmdev.swing.NarrowTitledBorder;

public class DialogConverterSettings extends JDialog {

  // 設定
  public Properties props;

  // テスト用
  public String testString;

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

  JTextField jTextMaxCoverLine;
  JComboBox jComboCover;
  JCheckBox jCheckCoverHistory;

  JCheckBox jCheckCoverPage;
  JCheckBox jCheckTitlePage;
  JRadioButton jRadioTitleNormal;
  JRadioButton jRadioTitleMiddle;
  JRadioButton jRadioTitleHorizontal;
  JCheckBox jCheckTocPage;
  JRadioButton jRadioTocV;
  JRadioButton jRadioTocH;

  JComboBox jComboExt;
  JCheckBox jCheckAutoFileName;
  JCheckBox jCheckOverWrite;

  JCheckBox jCheckSamePath;
  JComboBox jComboDstPath;

  JComboBox jComboEncType;

  JRadioButton jRadioVertical;
  JRadioButton jRadioHorizontal;

  JCheckBox jCheckNoIllust;

  JCheckBox jCheckConfirm;

  JTextField jTextDispW;
  JTextField jTextDispH;
  JTextField jTextCoverW;
  JTextField jTextCoverH;

  JCheckBox jCheckImageScale;
  JTextField jTextImageScale;
  JCheckBox jCheckImageFloat;
  JTextField jTextImageFloatW;
  JTextField jTextImageFloatH;
  JComboBox jComboImageFloatType;

  JTextField jTextSinglePageSizeW;
  JTextField jTextSinglePageSizeH;
  JTextField jTextSinglePageWidth;
  JRadioButton jRadioImageSizeType1;
  JRadioButton jRadioImageSizeType3;
  JCheckBox jCheckFitImage;

  JCheckBox jCheckImageFloatPage;
  JCheckBox jCheckImageFloatBlock;

  /**
   * Create the dialog.
   */
  public DialogConverterSettings(Frame owner, Properties props) {
    super(owner);

    this.props = props;

    setTitle("設定");
    setSize(580, 270);

    getContentPane().setLayout(new BorderLayout());

    // メインパネル
    JTabbedPane tabbedpane = new JTabbedPane();
    getContentPane().add(tabbedpane, BorderLayout.CENTER);

    // 下部パネル
    JPanel underPanel = new JPanel();
    underPanel.setLayout(new BoxLayout(underPanel, BoxLayout.X_AXIS));
    getContentPane().add(underPanel, BorderLayout.SOUTH);

    // 下部パネル内のチェックボックス用パネル
    JPanel checkBoxPanel = new JPanel();
    checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    underPanel.add(checkBoxPanel);
    jCheckConfirm = new JCheckBox("変換前確認", true);
    jCheckConfirm.setToolTipText("変換前にタイトルと表紙の設定が可能な確認画面を表示します");
    jCheckConfirm.setFocusPainted(false);
    jCheckConfirm.setBorder(new EmptyBorder(4, 15, 0, 0));
    checkBoxPanel.add(jCheckConfirm);

    // 下部パネル内のボタン用パネル
    JPanel buttonPane = new JPanel();
    buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
    JButton cancelButton = new JButton("Cancel");
    cancelButton.setActionCommand("Cancel");
    cancelButton.setAction(new ActionCancel());
    buttonPane.add(cancelButton);
    JButton okButton = new JButton("OK");
    okButton.setActionCommand("OK");
    okButton.setAction(new ActionApplyChanges());
    buttonPane.add(okButton);
    getRootPane().setDefaultButton(okButton);
    underPanel.add(buttonPane);

    // "変換"タブ
    JPanel tab1RootPanel = new JPanel();
    tab1RootPanel.setLayout(new BoxLayout(tab1RootPanel, BoxLayout.Y_AXIS));
    tabbedpane.addTab("変換", new ImageIcon(this.getClass().getResource("/images/epub.png")), tab1RootPanel);

    // "変換"タブ内 "表題"グループ
    JPanel tab1InnerPanel1 = new JPanel();
    tab1InnerPanel1 = new JPanel();
    tab1InnerPanel1.setLayout(new BoxLayout(tab1InnerPanel1, BoxLayout.X_AXIS));
    tab1InnerPanel1.setMinimumSize(panelSize);
    tab1InnerPanel1.setMaximumSize(panelSize);
    tab1InnerPanel1.setPreferredSize(panelSize);
    tab1InnerPanel1.setBorder(padding4H2V);
    tab1RootPanel.add(tab1InnerPanel1);
    JLabel labelTitle = new JLabel("表題: ");
    tab1InnerPanel1.add(labelTitle);
    JLabel labelInTheBody = new JLabel("本文内");
    labelInTheBody.setBorder(padding2H);
    tab1InnerPanel1.add(labelInTheBody);
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

    jTextMaxCoverLine = new JTextField("10");
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

    jComboCover = new JComboBox(new String[] { "[先頭の挿絵]", "[入力ファイル名と同じ画像(png,jpg)]", "[表紙無し]", "http://" });
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
    jCheckCoverHistory = new JCheckBox("表紙履歴利用", true);
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
    jCheckCoverPage = new JCheckBox("表紙画像 ", true);
    jCheckCoverPage.setFocusPainted(false);
    tab1InnerPanel3.add(jCheckCoverPage);
    // 左右中央
    jCheckTitlePage = new JCheckBox("表題", true);
    jCheckTitlePage.setToolTipText("表題を単独のページで出力します。チェック無し時は表題等は出力されません");
    jCheckTitlePage.setFocusPainted(false);
    tab1InnerPanel3.add(jCheckTitlePage);
    JLabel labelParenLeft = new JLabel("(");
    tab1InnerPanel3.add(labelParenLeft);
    ButtonGroup buttonGroupTitle = new ButtonGroup();
    jRadioTitleNormal = new JRadioButton("本文内 ");
    jRadioTitleNormal.setToolTipText("別ページ処理せずに本文中に表題等を出力します。 目次は表題前に出力されます");
    jRadioTitleNormal.setBorder(padding0);
    jRadioTitleNormal.setIconTextGap(1);
    tab1InnerPanel3.add(jRadioTitleNormal);
    buttonGroupTitle.add(jRadioTitleNormal);
    jRadioTitleMiddle = new JRadioButton("中央 ", true);
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
    jCheckTocPage = new JCheckBox("目次");
    jCheckTocPage.setToolTipText("目次ページを表題ページの次に追加します");
    jCheckTocPage.setFocusPainted(false);
    tab1InnerPanel3.add(jCheckTocPage);
    JLabel labelParenLeft2 = new JLabel("(");
    tab1InnerPanel3.add(labelParenLeft2);
    ButtonGroup buttonGroupIndex = new ButtonGroup();
    jRadioTocV = new JRadioButton("縦 ", true);
    jRadioTocV.setFocusPainted(false);
    jRadioTocV.setIconTextGap(2);
    jRadioTocV.setBorder(padding0);
    tab1InnerPanel3.add(jRadioTocV);
    buttonGroupIndex.add(jRadioTocV);
    jRadioTocH = new JRadioButton("横");
    jRadioTocH.setFocusPainted(false);
    jRadioTocH.setIconTextGap(2);
    jRadioTocH.setBorder(padding0);
    tab1InnerPanel3.add(jRadioTocH);
    buttonGroupIndex.add(jRadioTocH);
    JLabel labelParenRight2 = new JLabel(")");
    tab1InnerPanel3.add(labelParenRight2);

    // "変換"タブ内 "拡張子"グループ
    JPanel tab1InnerPanel4 = new JPanel();
    tab1InnerPanel4.setLayout(new BoxLayout(tab1InnerPanel4, BoxLayout.X_AXIS));
    tab1InnerPanel4.setMinimumSize(panelSize);
    tab1InnerPanel4.setMaximumSize(panelSize);
    tab1InnerPanel4.setPreferredSize(panelSize);
    tab1InnerPanel4.setBorder(padding4H2V);
    tab1RootPanel.add(tab1InnerPanel4);
    JLabel labelExt = new JLabel("拡張子: ");
    tab1InnerPanel4.add(labelExt);
    jComboExt = new JComboBox(new String[] { ".epub", ".kepub.epub", ".fxl.kepub.epub", ".mobi", ".mobi+.epub" });
    jComboExt.setToolTipText("出力するファイルの拡張子を選択します。 mobi出力時はKindlegenが必要になります");
    jComboExt.setEditable(true);
    jComboExt.setMaximumSize(new Dimension(110, 24));
    jComboExt.setPreferredSize(new Dimension(110, 24));
    tab1InnerPanel4.add(jComboExt);
    // 出力ファイル名設定
    jCheckAutoFileName = new JCheckBox("出力ファイル名に表題利用", true);
    jCheckAutoFileName.setFocusPainted(false);
    tab1InnerPanel4.add(jCheckAutoFileName);
    // ファイルの上書き許可
    jCheckOverWrite = new JCheckBox("出力ファイル上書き", true);
    jCheckOverWrite.setFocusPainted(false);
    tab1InnerPanel4.add(jCheckOverWrite);

    // "変換"タブ内 "出力先"グループ
    JPanel tab1InnerPanel5 = new JPanel();
    tab1InnerPanel5.setLayout(new BoxLayout(tab1InnerPanel5, BoxLayout.X_AXIS));
    tab1InnerPanel5.setMinimumSize(panelSize);
    tab1InnerPanel5.setMaximumSize(panelSize);
    tab1InnerPanel5.setPreferredSize(panelSize);
    tab1InnerPanel5.setBorder(padding4H2V);
    tab1RootPanel.add(tab1InnerPanel5);
    JLabel labeldstPath = new JLabel("出力先: ");
    tab1InnerPanel5.add(labeldstPath);
    jCheckSamePath = new JCheckBox("入力と同じ", true);
    jCheckSamePath.setToolTipText("入力ファイルと同じ場所に出力します");
    tab1InnerPanel5.add(jCheckSamePath);
    jCheckSamePath.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        jComboDstPath.setEditable(!jCheckSamePath.isSelected());
        jComboDstPath.setForeground(jCheckSamePath.isSelected() ? Color.gray : Color.black);
        jComboDstPath.repaint();
      }
    });
    jComboDstPath = new JComboBox();
    jComboDstPath.setToolTipText("出力先を指定します。変換時に履歴に追加されます");
    jComboDstPath.setEditable(false);
    jComboDstPath.setForeground(Color.gray);
    jComboDstPath.setPreferredSize(new Dimension(260, 24));

    // jComboDstPathにパスを追加
    // vecDstPath.add("[入力ファイルと同じ場所]");
    String propValue = props.getProperty("DstPathList");
    String dstPath = props.getProperty("DstPath");
    if (propValue != null && propValue.length() > 0) {
      for (String listPath : propValue.split(",")) {
        if (!"".equals(listPath))
          jComboDstPath.addItem(listPath);
      }
    }
    if (dstPath != null && !dstPath.equals("")) {
      jComboDstPath.setSelectedItem(dstPath);
    }
    tab1InnerPanel5.add(jComboDstPath);
    // propsの内容に応じて必要ならjCheckSamePathの選択を解除
    if ("".equals(props.getProperty("SamePath")))
      jCheckSamePath.setSelected(false);

    JButton jButtonDstPath = new JButton("選択");
    jButtonDstPath.setBorder(padding3);
    jButtonDstPath.setIcon(new ImageIcon(this.getClass().getResource("/images/dst_path.png")));
    jButtonDstPath.setFocusPainted(false);
    // jButtonDstPath.addActionListener(dstPathChooser);
    tab1InnerPanel5.add(jButtonDstPath);

    // "変換"タブ内 "入力文字コード"グループ & "組方向"グループ
    JPanel tab1InnerPanel6 = new JPanel();
    tab1InnerPanel6.setLayout(new BoxLayout(tab1InnerPanel6, BoxLayout.X_AXIS));
    tab1InnerPanel6.setMinimumSize(panelSize);
    tab1InnerPanel6.setMaximumSize(panelSize);
    tab1InnerPanel6.setPreferredSize(panelSize);
    tab1InnerPanel6.setBorder(padding4H2V);
    tab1RootPanel.add(tab1InnerPanel6);
    // "入力文字コード"グループ
    JPanel charCodePanel = new JPanel();
    charCodePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    tab1InnerPanel6.setBorder(padding0);
    tab1InnerPanel6.add(charCodePanel);
    JLabel charCodeLabel = new JLabel("入力文字コード");
    charCodeLabel.setBorder(padding0);
    charCodePanel.add(charCodeLabel);
    jComboEncType = new JComboBox(new String[] { "MS932", "UTF-8" });
    jComboEncType.setToolTipText("入力ファイルのテキストファイルの文字コード。青空文庫の標準はMS932(SJIS)です");
    jComboEncType.setFocusable(false);
    jComboEncType.setPreferredSize(new Dimension(100, 22));
    charCodePanel.add(jComboEncType);
    tab1InnerPanel6.add(charCodePanel);

    // "組方向"グループ
    JPanel typeSettingMethodPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    // typeSettingMethodPanel.setPreferredSize(panelSize);
    tab1InnerPanel6.setBorder(padding0);
    // 縦書き横書き
    ButtonGroup buttonGroupTypeSet = new ButtonGroup();
    jRadioVertical = new JRadioButton();
    jRadioVertical.setSelected(true);
    jRadioVertical.setFocusPainted(false);
    // jRadioVertical.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));
    jRadioVertical.setBorder(padding0);
    jRadioVertical.setIconTextGap(0);
    JLabel labelVertical = new JLabel("縦書き", new ImageIcon(this.getClass().getResource("/images/page_vertical.png")),
        JLabel.LEFT);
    labelVertical.setBorder(iconPadding);
    jRadioVertical.add(labelVertical);
    typeSettingMethodPanel.add(jRadioVertical);
    buttonGroupTypeSet.add(jRadioVertical);
    jRadioHorizontal = new JRadioButton();
    jRadioHorizontal.setFocusPainted(false);
    jRadioHorizontal.setBorder(padding0);
    jRadioHorizontal.setIconTextGap(0);
    JLabel labelHorizontal = new JLabel("横書き ",
        new ImageIcon(this.getClass().getResource("/images/page_horizontal.png")), JLabel.LEFT);
    labelHorizontal.setBorder(iconPadding);
    jRadioHorizontal.add(labelHorizontal);
    typeSettingMethodPanel.add(jRadioHorizontal);
    buttonGroupTypeSet.add(jRadioHorizontal);
    tab1InnerPanel6.add(typeSettingMethodPanel);

    // "画像1"タブ
    JPanel tab2RootPanel = new JPanel();
    tab2RootPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
    tabbedpane.addTab("画像1", new ImageIcon(this.getClass().getResource("/images/image.png")), tab2RootPanel);

    // "画像1"タブ内 "画像注記"グループ
    JPanel tab2InnerPanel1 = new JPanel();
    tab2InnerPanel1.setLayout(new BoxLayout(tab2InnerPanel1, BoxLayout.X_AXIS));
    tab2InnerPanel1.setBorder(new NarrowTitledBorder("画像注記"));
    tab2RootPanel.add(tab2InnerPanel1);
    jCheckNoIllust = new JCheckBox("挿絵除外 ");
    jCheckNoIllust.setFocusPainted(false);
    jCheckNoIllust.setToolTipText("テキストの画像注記は表紙と外字画像以外はePubに格納されません");
    jCheckNoIllust.setBorder(padding2);
    tab2InnerPanel1.add(jCheckNoIllust);

    // "画像1"タブ内 "画面サイズ"グループ
    JPanel tab2InnerPanel2 = new JPanel();
    tab2InnerPanel2.setLayout(new BoxLayout(tab2InnerPanel2, BoxLayout.X_AXIS));
    tab2InnerPanel2.setBorder(new NarrowTitledBorder("画面・表紙サイズ"));
    tab2RootPanel.add(tab2InnerPanel2);
    JLabel labelHView = new JLabel(" 画面: 横");
    tab2InnerPanel2.add(labelHView);
    jTextDispW = new JTextField("600");
    jTextDispW.setHorizontalAlignment(JTextField.RIGHT);
    jTextDispW.setMaximumSize(text4);
    jTextDispW.setPreferredSize(text4);
    jTextDispW.addFocusListener(new TextSelectFocusListener(jTextDispW));
    jTextDispW.setInputVerifier(new IntegerInputVerifier(600, 1, 9999));
    tab2InnerPanel2.add(jTextDispW);
    JLabel labelx = new JLabel("x");
    labelx.setBorder(padding2H);
    tab2InnerPanel2.add(labelx);
    JLabel labelV = new JLabel("縦");
    tab2InnerPanel2.add(labelV);
    jTextDispH = new JTextField("800");
    jTextDispH.setHorizontalAlignment(JTextField.RIGHT);
    jTextDispH.setMaximumSize(text4);
    jTextDispH.setPreferredSize(text4);
    jTextDispH.addFocusListener(new TextSelectFocusListener(jTextDispH));
    jTextDispH.setInputVerifier(new IntegerInputVerifier(800, 1, 9999));
    tab2InnerPanel2.add(jTextDispH);
    JLabel labelpx = new JLabel("px");
    labelpx.setBorder(padding2H);
    tab2InnerPanel2.add(labelpx);

    // "画像1"タブ内 "画面・表紙サイズ"グループ
    JLabel labelCoverW = new JLabel("  表紙: 横");
    tab2InnerPanel2.add(labelCoverW);
    jTextCoverW = new JTextField("600");
    jTextCoverW.setHorizontalAlignment(JTextField.RIGHT);
    jTextCoverW.setInputVerifier(new IntegerInputVerifier(600, 64, 4096));
    jTextCoverW.setMaximumSize(text4);
    jTextCoverW.setPreferredSize(text4);
    jTextCoverW.addFocusListener(new TextSelectFocusListener(jTextCoverW));
    tab2InnerPanel2.add(jTextCoverW);
    JLabel labelCoverX = new JLabel("x");
    labelCoverX.setBorder(padding2H);
    tab2InnerPanel2.add(labelCoverX);
    JLabel labelCoverH = new JLabel("縦");
    tab2InnerPanel2.add(labelCoverH);
    jTextCoverH = new JTextField("800");
    jTextCoverH.setHorizontalAlignment(JTextField.RIGHT);
    jTextCoverH.setInputVerifier(new IntegerInputVerifier(800, 64, 4096));
    jTextCoverH.setMaximumSize(text4);
    jTextCoverH.setPreferredSize(text4);
    jTextCoverH.addFocusListener(new TextSelectFocusListener(jTextCoverH));
    tab2InnerPanel2.add(jTextCoverH);
    JLabel labelPx = new JLabel("px");
    labelPx.setBorder(padding2H);
    tab2InnerPanel2.add(labelPx);

    // "画像1"タブ内 "画像倍率"グループ
    JPanel tab2InnerPanel3 = new JPanel();
    tab2InnerPanel3.setLayout(new BoxLayout(tab2InnerPanel3, BoxLayout.X_AXIS));
    tab2InnerPanel3.setBorder(new NarrowTitledBorder("画像表示倍率"));
    tab2RootPanel.add(tab2InnerPanel3);
    jCheckImageScale = new JCheckBox("有効 ", true);
    jCheckImageScale.setToolTipText("画面の解像度に合わせて画像の幅を％指定します。画像キャプションがはみ出る場合も指定してください");
    jCheckImageScale.setFocusPainted(false);
    jCheckImageScale.setBorder(padding2);
    jCheckImageScale.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        jTextImageScale.setEditable(jCheckImageScale.isSelected());
      }
    });
    tab2InnerPanel3.add(jCheckImageScale);
    jTextImageScale = new JTextField("1.0");
    jTextImageScale.setToolTipText("指定倍率で画像を拡大表示します。64px以下の画像は変更されません");
    jTextImageScale.setHorizontalAlignment(JTextField.RIGHT);
    jTextImageScale.setInputVerifier(new FloatInputVerifier(1, 0.01f, 30));
    jTextImageScale.setMaximumSize(text4);
    jTextImageScale.setPreferredSize(text4);
    jTextImageScale.addFocusListener(new TextSelectFocusListener(jTextImageScale));
    tab2InnerPanel3.add(jTextImageScale);
    JLabel labelPowers = new JLabel("倍");
    tab2InnerPanel3.add(labelPowers);

    // "画像1"タブ内 "画像回り込み"グループ
    JPanel tab2InnerPanel4 = new JPanel();
    tab2InnerPanel4.setLayout(new BoxLayout(tab2InnerPanel4, BoxLayout.X_AXIS));
    tab2InnerPanel4.setBorder(new NarrowTitledBorder("画像回り込み (※単ページ化より優先)"));
    tab2RootPanel.add(tab2InnerPanel4);

    jCheckImageFloat = new JCheckBox("回り込み有効 ");
    jCheckImageFloat.setToolTipText("画像の実サイズが指定サイズ以下の画像を回り込み設定します");
    jCheckImageFloat.setFocusPainted(false);
    jCheckImageFloat.setBorder(padding2);
    jCheckImageFloat.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        jTextImageFloatW.setEditable(jCheckImageFloat.isSelected());
        jTextImageFloatH.setEditable(jCheckImageFloat.isSelected());
      }
    });
    tab2InnerPanel4.add(jCheckImageFloat);
    JLabel labelImgFloatThW = new JLabel("横");
    tab2InnerPanel4.add(labelImgFloatThW);
    jTextImageFloatW = new JTextField("600");
    jTextImageFloatW.setHorizontalAlignment(JTextField.RIGHT);
    jTextImageFloatW.setInputVerifier(new IntegerInputVerifier(600, 1, 9999));
    jTextImageFloatW.setMaximumSize(text4);
    jTextImageFloatW.setPreferredSize(text4);
    jTextImageFloatW.addFocusListener(new TextSelectFocusListener(jTextImageFloatW));
    jTextImageFloatW.setEditable(jCheckImageFloat.isSelected());
    tab2InnerPanel4.add(jTextImageFloatW);
    JLabel labelx2 = new JLabel("x");
    labelx2.setBorder(padding2H);
    tab2InnerPanel4.add(labelx2);
    labelx2 = new JLabel("縦");
    tab2InnerPanel4.add(labelx2);
    jTextImageFloatH = new JTextField("400");
    jTextImageFloatH.setHorizontalAlignment(JTextField.RIGHT);
    jTextImageFloatH.setInputVerifier(new IntegerInputVerifier(400, 1, 9999));
    jTextImageFloatH.setMaximumSize(text4);
    jTextImageFloatH.setPreferredSize(text4);
    jTextImageFloatH.addFocusListener(new TextSelectFocusListener(jTextImageFloatH));
    jTextImageFloatH.setEditable(jCheckImageFloat.isSelected());
    tab2InnerPanel4.add(jTextImageFloatH);
    JLabel labelLTpx = new JLabel("px以下");
    labelLTpx.setBorder(padding2H);
    tab2InnerPanel4.add(labelLTpx);

    JLabel labelArroc = new JLabel(" 配置");
    tab2InnerPanel4.add(labelArroc);
    jComboImageFloatType = new JComboBox(new String[] { "上/左", "下/右" });
    jComboImageFloatType.setFocusable(false);
    jComboImageFloatType.setBorder(padding0);
    jComboImageFloatType.setPreferredSize(new Dimension(text4.width + 48, 20));
    tab2InnerPanel4.add(jComboImageFloatType);

    // "画像1"タブ内 "画像単ページ化"グループ
    JPanel tab2InnerPanel5 = new JPanel();
    tab2InnerPanel5.setLayout(new BoxLayout(tab2InnerPanel5, BoxLayout.Y_AXIS));
    tab2InnerPanel5.setBorder(new NarrowTitledBorder("画像単ページ化"));
    tab2RootPanel.add(tab2InnerPanel5);
    // 上段
    JPanel panelUpper = new JPanel();
    panelUpper.setLayout(new BoxLayout(panelUpper, BoxLayout.X_AXIS));
    panelUpper.setMaximumSize(panelVMaxSize);
    panelUpper.setBorder(padding4B);
    tab2InnerPanel5.add(panelUpper);
    // 横x縦
    JLabel labelW = new JLabel("横");
    panelUpper.add(labelW);
    jTextSinglePageSizeW = new JTextField("400");
    jTextSinglePageSizeW.setHorizontalAlignment(JTextField.RIGHT);
    jTextSinglePageSizeW.setInputVerifier(new IntegerInputVerifier(400, 1, 9999));
    jTextSinglePageSizeW.setMaximumSize(text4);
    jTextSinglePageSizeW.setPreferredSize(text4);
    jTextSinglePageSizeW.addFocusListener(new TextSelectFocusListener(jTextSinglePageSizeW));
    panelUpper.add(jTextSinglePageSizeW);
    JLabel labelx3 = new JLabel("x");
    labelx3.setBorder(padding2H);
    panelUpper.add(labelx3);
    JLabel labelV2 = new JLabel("縦");
    panelUpper.add(labelV2);
    jTextSinglePageSizeH = new JTextField("600");
    jTextSinglePageSizeH.setHorizontalAlignment(JTextField.RIGHT);
    jTextSinglePageSizeH.setInputVerifier(new IntegerInputVerifier(600, 1, 9999));
    jTextSinglePageSizeH.setMaximumSize(text4);
    jTextSinglePageSizeH.setPreferredSize(text4);
    jTextSinglePageSizeH.addFocusListener(new TextSelectFocusListener(jTextSinglePageSizeH));
    panelUpper.add(jTextSinglePageSizeH);
    JLabel labelOverpx = new JLabel("px以上 ");
    labelOverpx.setBorder(padding2H);
    panelUpper.add(labelOverpx);
    // 横のみ
    JLabel labelWOnly = new JLabel("横のみ");
    labelWOnly.setBorder(padding2H);
    panelUpper.add(labelWOnly);
    jTextSinglePageWidth = new JTextField("600");
    jTextSinglePageWidth.setHorizontalAlignment(JTextField.RIGHT);
    jTextSinglePageWidth.setInputVerifier(new IntegerInputVerifier(600, 1, 9999));
    jTextSinglePageWidth.setMaximumSize(text4);
    jTextSinglePageWidth.setPreferredSize(text4);
    jTextSinglePageWidth.addFocusListener(new TextSelectFocusListener(jTextSinglePageWidth));
    panelUpper.add(jTextSinglePageWidth);
    JLabel labelOverpx2 = new JLabel("px以上");
    labelOverpx2.setBorder(padding2H);
    panelUpper.add(labelOverpx2);

    // 下段
    JPanel panelLower = new JPanel();
    panelLower.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    tab2InnerPanel5.add(panelLower);

    JLabel labelDownSize = new JLabel("縮小表示 (");
    labelDownSize.setBorder(padding2H);
    panelLower.add(labelDownSize);

    ButtonGroup buttonGroupImgSize = new ButtonGroup();
    jRadioImageSizeType1 = new JRadioButton("指定無し");
    jRadioImageSizeType1.setToolTipText("画像のサイズを指定しません。 端末が自動で縮小します(Kindle, Kobo)");
    jRadioImageSizeType1.setFocusPainted(false);
    jRadioImageSizeType1.setBorder(padding2);
    jRadioImageSizeType1.setIconTextGap(1);
    panelLower.add(jRadioImageSizeType1);
    buttonGroupImgSize.add(jRadioImageSizeType1);
    /*
     * jRadioImageSizeType2 = new JRadioButton("高さ%", true);
     * jRadioImageSizeType2.setToolTipText(
     * "画面の縦横比に合せて画像の高さのみ%指定します。画面設定より縦長の端末でははみ出すか縦長に表示されます");
     * jRadioImageSizeType2.setFocusPainted(false);
     * jRadioImageSizeType2.setBorder(padding2);
     * jRadioImageSizeType2.setIconTextGap(1); panel.add(jRadioImageSizeType2);
     * buttonGroup.add(jRadioImageSizeType2);
     */
    jRadioImageSizeType3 = new JRadioButton("縦横比");
    jRadioImageSizeType3.setToolTipText("画面の縦横比に合せて幅または高さを100%指定します。画面回転で画像がはみ出す場合があります");
    jRadioImageSizeType3.setFocusPainted(false);
    jRadioImageSizeType3.setBorder(padding2);
    jRadioImageSizeType3.setIconTextGap(1);
    panelLower.add(jRadioImageSizeType3);
    buttonGroupImgSize.add(jRadioImageSizeType3);

    JLabel labelParenR = new JLabel(")  ");
    labelParenR.setBorder(padding2H);
    panelLower.add(labelParenR);

    jCheckFitImage = new JCheckBox("拡大表示", true);
    jCheckFitImage.setToolTipText("画面サイズより小さい画像を幅高さに合わせて拡大表示します。画面回転で画像がはみ出す場合があります");
    jCheckFitImage.setFocusPainted(false);
    jCheckFitImage.setBorder(padding2);
    panelLower.add(jCheckFitImage);
    buttonGroupImgSize = new ButtonGroup();

    // "画像1"タブ内 "Float指定"グループ
    JPanel tab2InnerPanel6 = new JPanel();
    tab2InnerPanel6.setLayout(new BoxLayout(tab2InnerPanel6, BoxLayout.X_AXIS));
    tab2InnerPanel6.setBorder(new NarrowTitledBorder("Float指定 (Readerのみ)"));
    tab2RootPanel.add(tab2InnerPanel6);
    jCheckImageFloatPage = new JCheckBox("単ページ画像");
    jCheckImageFloatPage.setToolTipText("単ページ対象の画像をfloat表示します。 xhtmlは分割されません");
    jCheckImageFloatPage.setFocusPainted(false);
    jCheckImageFloatPage.setBorder(padding2);
    tab2InnerPanel6.add(jCheckImageFloatPage);

    jCheckImageFloatBlock = new JCheckBox("通常画像");
    jCheckImageFloatBlock.setToolTipText("回り込み、単ページ以外の画像をfloat表示します。 64px以上の画像のみ");
    jCheckImageFloatBlock.setFocusPainted(false);
    jCheckImageFloatBlock.setBorder(padding2);
    tab2InnerPanel6.add(jCheckImageFloatBlock);

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

  // TODO propsの内容をGUIオブジェクトに反映させる処理 定義直後にやってるのもあるのでここに移動させる方がいいかも

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

  /** 出力先選択ボタンイベント */
  class DstPathChooserListener implements ActionListener {
    Component parent;

    private DstPathChooserListener(Component parent) {
      this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      File path = null;
      String dstPath = null;
      File currentPath = new File(props.getProperty("LastDir"));
      Object obj = jCheckSamePath.isSelected() ? jComboDstPath.getSelectedItem() : jComboDstPath.getEditor().getItem();
      if (obj != null)
        dstPath = obj.toString();
      // パス修正
      if (dstPath == null || "".equals(dstPath))
        path = currentPath;
      else {
        path = new File(dstPath);
        if (path != null && !path.isDirectory())
          path = path.getParentFile();
        if (path != null && !path.isDirectory())
          path = path.getParentFile();
        if (path != null && !path.isDirectory())
          path = currentPath;
      }
      JFileChooser fileChooser = new JFileChooser(path);
      fileChooser.setDialogTitle("出力先を選択");
      fileChooser.setApproveButtonText("選択");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int state = fileChooser.showOpenDialog(parent);
      switch (state) {
      case JFileChooser.APPROVE_OPTION:
        String pathString = fileChooser.getSelectedFile().getAbsolutePath();
        jCheckSamePath.setSelected(false);
        jComboDstPath.setEditable(true);
        jComboDstPath.setSelectedItem(pathString);
      }
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
      putValue(NAME, "Apply");
      putValue(SHORT_DESCRIPTION, "Apply Changes and Close Dialog");
    }

    public void actionPerformed(ActionEvent e) {
      // TODO GUIオブジェクトの状態をプロパティに書き出す処理
      testString = "dialog closed by pressing OK button";
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
      testString = "dialog closed by pressing cancel button";
      dispose();
    }
  }

}
