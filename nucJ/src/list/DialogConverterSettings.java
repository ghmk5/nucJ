package list;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
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
import java.io.IOException;
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

  JCheckBox jCheckSvgImage;

  JTextField jTextJpegQuality;
  JCheckBox jCheckGamma;
  JTextField jTextGammaValue;
  JCheckBox jCheckResizeH;
  JTextField jTextResizeNumH;

  JCheckBox jCheckResizeW;
  JTextField jTextResizeNumW;
  JComboBox jComboRotateImage;

  JCheckBox jCheckAutoMargin;
  JTextField jTextAutoMarginLimitH;
  JTextField jTextAutoMarginLimitV;
  JTextField jTextAutoMarginWhiteLevel;
  JTextField jTextAutoMarginPadding;
  JTextField jTextAutoMarginNombreSize;
  JComboBox jComboAutoMarginNombre;

  JRadioButton jRadioSpaceHyp0;
  JRadioButton jRadioSpaceHyp1;
  JRadioButton jRadioSpaceHyp2;

  JRadioButton jRadioChukiRuby0;
  JRadioButton jRadioChukiRuby1;
  JRadioButton jRadioChukiRuby2;

  JCheckBox jCheckAutoYoko;
  JCheckBox jCheckAutoYokoNum1;
  JCheckBox jCheckAutoYokoNum3;
  JCheckBox jCheckAutoEQ1;

  JCheckBox jCheckCommentPrint;
  JCheckBox jCheckCommentConvert;

  JCheckBox jCheckMarkId;

  JComboBox jComboxRemoveEmptyLine;
  JComboBox jComboxMaxEmptyLine;

  JCheckBox jCheckForceIndent;

  JCheckBox jCheckPageBreak;
  JTextField jTextPageBreakSize;
  JTextField jTextPageBreakEmptySize;
  JTextField jTextPageBreakChapterSize;
  JCheckBox jCheckPageBreakEmpty;
  JComboBox jComboxPageBreakEmptyLine;
  JCheckBox jCheckPageBreakChapter;

  JTextField jTextMaxChapterNameLength;
  JCheckBox jCheckCoverPageToc;
  JCheckBox jCheckTitleToc;
  JCheckBox jCheckChapterUseNextLine;
  JCheckBox jCheckChapterExclude;
  JCheckBox jCheckNavNest;
  JCheckBox jCheckNcxNest;

  JCheckBox jCheckChapterH;
  JCheckBox jCheckChapterH1;
  JCheckBox jCheckChapterH2;
  JCheckBox jCheckChapterH3;
  JCheckBox jCheckSameLineChapter;
  JCheckBox jCheckChapterSection;
  JCheckBox jCheckChapterName;
  JCheckBox jCheckChapterNumOnly;
  JCheckBox jCheckChapterNumTitle;
  JCheckBox jCheckChapterNumParen;
  JCheckBox jCheckChapterNumParenTitle;
  JCheckBox jCheckChapterPattern;
  JComboBox jComboChapterPattern;

  JComboBox jComboLineHeight;

  JComboBox jComboFontSize;

  JCheckBox jCheckBoldUseGothic;
  JCheckBox jCheckGothicUseBold;

  JTextField[] jTextPageMargins;
  JRadioButton jRadioPageMarginUnit0;
  JRadioButton jRadioPageMarginUnit1;
  JTextField[] jTextBodyMargins;
  JRadioButton jRadioBodyMarginUnit0;
  JRadioButton jRadioBodyMarginUnit1;

  JRadioButton jRadioDakutenType0;
  JRadioButton jRadioDakutenType1;
  JRadioButton jRadioDakutenType2;

  JCheckBox jCheckIvsBMP;
  JCheckBox jCheckIvsSSP;

  JTextField jTextWebInterval;
  JTextField jTextCachePath;

  JTextField jTextWebModifiedExpire;
  JCheckBox jCheckWebConvertUpdated;

  JCheckBox jCheckWebBeforeChapter;
  JTextField jTextWebBeforeChapterCount;
  JCheckBox jCheckWebModifiedOnly;
  JCheckBox jCheckWebModifiedTail;

  JLabel label;// 使い回し用ラベル

  /**
   * Create the dialog.
   */
  public DialogConverterSettings(Frame owner, Properties props) {
    super(owner);

    this.props = props;

    setResizable(false);
    setMinimumSize(new Dimension(640, 290));
    setTitle("設定");

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
     * btnGrpNotice.add(jRadioImageSizeType2);
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

    // "画像2"タブ
    JPanel tab3RootPanel = new JPanel();
    tab3RootPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
    tabbedpane.addTab("画像2", new ImageIcon(this.getClass().getResource("/images/image.png")), tab3RootPanel);

    // "画像2"タブ内 "全画面表示"グループ
    JPanel tab3InnerPanel1 = new JPanel();
    tab3InnerPanel1.setLayout(new BoxLayout(tab3InnerPanel1, BoxLayout.X_AXIS));
    tab3InnerPanel1.setBorder(new NarrowTitledBorder("全画面表示"));
    tab3RootPanel.add(tab3InnerPanel1);
    jCheckSvgImage = new JCheckBox("SVGタグ出力（画像zipのみ） ");
    jCheckSvgImage.setFocusPainted(false);
    jCheckSvgImage.setToolTipText("画像のみのzipの場合、固定レイアウト＋SVGタグで出力します");
    jCheckSvgImage.setBorder(padding2);
    tab3InnerPanel1.add(jCheckSvgImage);

    // "画像2"タブ内 "Jpeg圧縮率"グループ
    JPanel tab3InnerPanel2 = new JPanel();
    tab3InnerPanel2.setLayout(new BoxLayout(tab3InnerPanel2, BoxLayout.X_AXIS));
    tab3InnerPanel2.setBorder(new NarrowTitledBorder("Jpeg圧縮率"));
    tab3RootPanel.add(tab3InnerPanel2);
    jTextJpegQuality = new JTextField("85");
    jTextJpegQuality.setToolTipText("表紙編集、縮小、回転、余白除去時のJpeg保存時の画質(100が最高画質)");
    jTextJpegQuality.setHorizontalAlignment(JTextField.RIGHT);
    jTextJpegQuality.setInputVerifier(new IntegerInputVerifier(85, 30, 100));
    jTextJpegQuality.setMaximumSize(text3);
    jTextJpegQuality.setPreferredSize(text3);
    jTextJpegQuality.addFocusListener(new TextSelectFocusListener(jTextJpegQuality));
    tab3InnerPanel2.add(jTextJpegQuality);
    tab3InnerPanel2.add(new JLabel(" (30～100)"));

    // "画像2"タブ内 "色調整"グループ
    JPanel tab3InnerPanel3 = new JPanel();
    tab3InnerPanel3.setLayout(new BoxLayout(tab3InnerPanel3, BoxLayout.X_AXIS));
    tab3InnerPanel3.setBorder(new NarrowTitledBorder("色調整"));
    tab3RootPanel.add(tab3InnerPanel3);
    jCheckGamma = new JCheckBox("ガンマ補正");
    jCheckGamma.setToolTipText("画像の濃さを変更します (濃:0.2～1.8:淡)");
    jCheckGamma.setFocusPainted(false);
    jCheckGamma.setBorder(padding2);
    jCheckGamma.setIconTextGap(2);
    jCheckGamma.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        jTextGammaValue.setEditable(jCheckGamma.isSelected());
      }
    });
    tab3InnerPanel3.add(jCheckGamma);
    jTextGammaValue = new JTextField("1.0");
    jTextGammaValue.setToolTipText(jCheckGamma.getToolTipText());
    jTextGammaValue.setHorizontalAlignment(JTextField.RIGHT);
    jTextGammaValue.setInputVerifier(new FloatInputVerifier(1.0f, 0.2f, 1.8f));
    jTextGammaValue.setMaximumSize(text3);
    jTextGammaValue.setPreferredSize(text3);
    jTextGammaValue.setEditable(jCheckGamma.isSelected());
    jTextGammaValue.addFocusListener(new TextSelectFocusListener(jTextGammaValue));
    tab3InnerPanel3.add(jTextGammaValue);

    // "画像2"タブ内 "画像縮小回転"グループ
    JPanel tab3InnerPanel4 = new JPanel();
    tab3InnerPanel4.setLayout(new BoxLayout(tab3InnerPanel4, BoxLayout.X_AXIS));
    tab3InnerPanel4.setBorder(new NarrowTitledBorder("画像縮小回転"));
    tab3RootPanel.add(tab3InnerPanel4);
    ChangeListener resizeChangeLister = new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        setResizeTextEditable(true);
      }
    };
    jCheckResizeW = new JCheckBox("横");
    jCheckResizeW.setFocusPainted(false);
    jCheckResizeW.setBorder(padding2);
    jCheckResizeW.setIconTextGap(2);
    jCheckResizeW.addChangeListener(resizeChangeLister);
    tab3InnerPanel4.add(jCheckResizeW);
    jTextResizeNumW = new JTextField("2048");
    jTextResizeNumW.setHorizontalAlignment(JTextField.RIGHT);
    jTextResizeNumW.setInputVerifier(new IntegerInputVerifier(2048, 100, 9999));
    jTextResizeNumW.setMaximumSize(text4);
    jTextResizeNumW.setPreferredSize(text4);
    jTextResizeNumW.addFocusListener(new TextSelectFocusListener(jTextResizeNumW));
    jTextResizeNumW.setEditable(jCheckResizeW.isSelected());
    tab3InnerPanel4.add(jTextResizeNumW);
    label = new JLabel("px以下 ");
    label.setBorder(padding2H);
    tab3InnerPanel4.add(label);
    jCheckResizeH = new JCheckBox("縦");
    jCheckResizeH.setFocusPainted(false);
    jCheckResizeH.setBorder(padding2);
    jCheckResizeH.setIconTextGap(2);
    jCheckResizeH.addChangeListener(resizeChangeLister);
    tab3InnerPanel4.add(jCheckResizeH);
    jTextResizeNumH = new JTextField("2048");
    jTextResizeNumH.setHorizontalAlignment(JTextField.RIGHT);
    jTextResizeNumH.setInputVerifier(new IntegerInputVerifier(2048, 100, 9999));
    jTextResizeNumH.setMaximumSize(text4);
    jTextResizeNumH.setPreferredSize(text4);
    jTextResizeNumH.addFocusListener(new TextSelectFocusListener(jTextResizeNumH));
    tab3InnerPanel4.add(jTextResizeNumH);
    label = new JLabel("px以下");
    label.setBorder(padding2H);
    tab3InnerPanel4.add(label);
    this.setResizeTextEditable(true);
    label = new JLabel(" 自動回転");
    label.setBorder(padding2H);
    tab3InnerPanel4.add(label);
    jComboRotateImage = new JComboBox(new String[] { "なし", "右", "左" });
    jComboRotateImage.setToolTipText("単ページ時画面の縦横比に合わせて画像を回転します");
    jComboRotateImage.setFocusable(false);
    jComboRotateImage.setBorder(padding0);
    jComboRotateImage.setPreferredSize(new Dimension(84, 20));
    tab3InnerPanel4.add(jComboRotateImage);

    // "画像2"タブ内 "余白除去"グループ
    JPanel tab3InnerPanel5 = new JPanel();
    tab3InnerPanel5.setLayout(new BoxLayout(tab3InnerPanel5, BoxLayout.Y_AXIS));
    tab3InnerPanel5.setBorder(new NarrowTitledBorder("余白除去"));
    tab3RootPanel.add(tab3InnerPanel5);
    JPanel tab3panel5UpperPanel = new JPanel();
    tab3panel5UpperPanel.setLayout(new BoxLayout(tab3panel5UpperPanel, BoxLayout.X_AXIS));
    tab3panel5UpperPanel.setBorder(padding4B);
    tab3InnerPanel5.add(tab3panel5UpperPanel);
    jCheckAutoMargin = new JCheckBox("有効 ");
    jCheckAutoMargin.setFocusPainted(false);
    jCheckAutoMargin.setBorder(padding2);
    jCheckAutoMargin.setIconTextGap(2);
    jCheckAutoMargin.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        boolean selected = jCheckAutoMargin.isSelected();
        jTextAutoMarginLimitH.setEditable(selected);
        jTextAutoMarginLimitV.setEditable(selected);
        jTextAutoMarginWhiteLevel.setEditable(selected);
        jTextAutoMarginPadding.setEditable(selected);
        jTextAutoMarginNombreSize.setEditable(selected);
      }
    });
    tab3panel5UpperPanel.add(jCheckAutoMargin);
    label = new JLabel(" 横");
    label.setToolTipText("横方向の余白除去量の制限 左右の余白の合計");
    tab3panel5UpperPanel.add(label);
    jTextAutoMarginLimitH = new JTextField("15");
    jTextAutoMarginLimitH.setToolTipText(label.getToolTipText());
    jTextAutoMarginLimitH.setHorizontalAlignment(JTextField.RIGHT);
    jTextAutoMarginLimitH.setInputVerifier(new IntegerInputVerifier(15, 0, 50));
    jTextAutoMarginLimitH.setMaximumSize(text3);
    jTextAutoMarginLimitH.setPreferredSize(text3);
    jTextAutoMarginLimitH.setEditable(jCheckAutoMargin.isSelected());
    jTextAutoMarginLimitH.addFocusListener(new TextSelectFocusListener(jTextAutoMarginLimitH));
    tab3panel5UpperPanel.add(jTextAutoMarginLimitH);
    label = new JLabel("%");
    label.setBorder(padding2H);
    tab3panel5UpperPanel.add(label);
    label = new JLabel(" 縦");
    label.setToolTipText("縦方向の余白除去量の制限 上下の余白の合計");
    tab3panel5UpperPanel.add(label);
    jTextAutoMarginLimitV = new JTextField("15");
    jTextAutoMarginLimitV.setToolTipText(label.getToolTipText());
    jTextAutoMarginLimitV.setHorizontalAlignment(JTextField.RIGHT);
    jTextAutoMarginLimitV.setInputVerifier(new IntegerInputVerifier(15, 0, 50));
    jTextAutoMarginLimitV.setMaximumSize(text3);
    jTextAutoMarginLimitV.setPreferredSize(text3);
    jTextAutoMarginLimitV.setEditable(jCheckAutoMargin.isSelected());
    jTextAutoMarginLimitV.addFocusListener(new TextSelectFocusListener(jTextAutoMarginLimitV));
    tab3panel5UpperPanel.add(jTextAutoMarginLimitV);
    label = new JLabel("%");
    label.setBorder(padding2H);
    tab3panel5UpperPanel.add(label);
    tab3panel5UpperPanel.add(label);
    label = new JLabel("  余白追加");
    label.setToolTipText("余白除去後に追加する余白の量(追加部分の画像はそのまま)");
    tab3panel5UpperPanel.add(label);
    jTextAutoMarginPadding = new JTextField("1.0");
    jTextAutoMarginPadding.setToolTipText(label.getToolTipText());
    jTextAutoMarginPadding.setHorizontalAlignment(JTextField.RIGHT);
    jTextAutoMarginPadding.setInputVerifier(new FloatInputVerifier(1.0f, 0, 50));
    jTextAutoMarginPadding.setMaximumSize(text3);
    jTextAutoMarginPadding.setPreferredSize(text3);
    jTextAutoMarginPadding.setEditable(jCheckAutoMargin.isSelected());
    jTextAutoMarginPadding.addFocusListener(new TextSelectFocusListener(jTextAutoMarginPadding));
    tab3panel5UpperPanel.add(jTextAutoMarginPadding);
    label = new JLabel("%");
    label.setBorder(padding2H);
    tab3panel5UpperPanel.add(label);

    label = new JLabel("  白レベル");
    label.setToolTipText("余白部分の白い画素と判別するレベルを指定します (黒:0～白:100)");
    tab3panel5UpperPanel.add(label);
    jTextAutoMarginWhiteLevel = new JTextField("80");
    jTextAutoMarginWhiteLevel.setToolTipText(label.getToolTipText());
    jTextAutoMarginWhiteLevel.setHorizontalAlignment(JTextField.RIGHT);
    jTextAutoMarginWhiteLevel.setInputVerifier(new IntegerInputVerifier(80, 0, 100));
    jTextAutoMarginWhiteLevel.setMaximumSize(text3);
    jTextAutoMarginWhiteLevel.setPreferredSize(text3);
    jTextAutoMarginWhiteLevel.setEditable(jCheckAutoMargin.isSelected());
    jTextAutoMarginWhiteLevel.addFocusListener(new TextSelectFocusListener(jTextAutoMarginWhiteLevel));
    tab3panel5UpperPanel.add(jTextAutoMarginWhiteLevel);
    label = new JLabel("%");
    label.setBorder(padding2H);

    JPanel tab3panel5LowerPanel = new JPanel();
    tab3panel5LowerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    tab3InnerPanel5.add(tab3panel5LowerPanel);
    label = new JLabel("ノンブル除去 (位置");
    label.setBorder(padding2H);
    tab3panel5LowerPanel.add(label);
    jComboAutoMarginNombre = new JComboBox(new String[] { "なし", "上", "下", "上下" });
    jComboAutoMarginNombre.setToolTipText("ノンブルを除去します。除去した場合は除去制限が5%追加されます");
    jComboAutoMarginNombre.setFocusable(false);
    jComboAutoMarginNombre.setMaximumSize(new Dimension(text3.width + 24, 20));
    jComboAutoMarginNombre.setPreferredSize(new Dimension(84, 20));
    tab3panel5LowerPanel.add(jComboAutoMarginNombre);
    label = new JLabel(" 高さ");
    label.setBorder(padding2H);
    tab3panel5LowerPanel.add(label);
    jTextAutoMarginNombreSize = new JTextField("3.0");
    jTextAutoMarginNombreSize.setToolTipText("ノンブルの文字部分の高さを指定します。これより大きい場合はノンブル除去されません");
    jTextAutoMarginNombreSize.setHorizontalAlignment(JTextField.RIGHT);
    jTextAutoMarginNombreSize.setInputVerifier(new FloatInputVerifier(3.0f, 0.5f, 10));
    jTextAutoMarginNombreSize.setMaximumSize(text3);
    jTextAutoMarginNombreSize.setPreferredSize(text3);
    jTextAutoMarginNombreSize.setEditable(jCheckAutoMargin.isSelected());
    jTextAutoMarginNombreSize.addFocusListener(new TextSelectFocusListener(jTextAutoMarginPadding));
    tab3panel5LowerPanel.add(jTextAutoMarginNombreSize);
    label = new JLabel("% )");
    label.setBorder(padding2H);
    tab3panel5LowerPanel.add(label);

    // "詳細設定"タブ
    JPanel tab4RootPanel = new JPanel();
    tab4RootPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
    tabbedpane.addTab("詳細設定", new ImageIcon(this.getClass().getResource("/images/page_setting.png")), tab4RootPanel);

    // "詳細設定"タブ内 "文中全角スペースの処理"グループ
    JPanel tab4InnerPanel1 = new JPanel();
    tab4InnerPanel1.setLayout(new BoxLayout(tab4InnerPanel1, BoxLayout.X_AXIS));
    tab4InnerPanel1.setBorder(new NarrowTitledBorder("文中全角スペースの処理"));
    tab4RootPanel.add(tab4InnerPanel1);
    // ピクセル
    label = new JLabel("行末で非表示(");
    label.setBorder(padding2);
    tab4InnerPanel1.add(label);
    ButtonGroup btnGrp2WSpc = new ButtonGroup();
    jRadioSpaceHyp1 = new JRadioButton("Kobo・Kindle ");
    jRadioSpaceHyp1.setToolTipText("Kobo・Kindleで行末で非表示にします Readerではそのままと同じ表示になります");
    jRadioSpaceHyp1.setFocusPainted(false);
    jRadioSpaceHyp1.setIconTextGap(2);
    jRadioSpaceHyp1.setBorder(padding2);
    tab4InnerPanel1.add(jRadioSpaceHyp1);
    btnGrp2WSpc.add(jRadioSpaceHyp1);
    jRadioSpaceHyp2 = new JRadioButton("Reader ) ");
    jRadioSpaceHyp2.setToolTipText("Reader以外では次行に追い出しの禁則処理になります");
    jRadioSpaceHyp2.setFocusPainted(false);
    jRadioSpaceHyp2.setIconTextGap(2);
    jRadioSpaceHyp2.setBorder(padding2);
    tab4InnerPanel1.add(jRadioSpaceHyp2);
    btnGrp2WSpc.add(jRadioSpaceHyp2);
    jRadioSpaceHyp0 = new JRadioButton("そのまま", true);
    jRadioSpaceHyp0.setToolTipText("行の折り返し部分にある全角スペースが行頭に表示されます");
    jRadioSpaceHyp0.setFocusPainted(false);
    jRadioSpaceHyp0.setIconTextGap(2);
    jRadioSpaceHyp0.setBorder(padding2);
    tab4InnerPanel1.add(jRadioSpaceHyp0);
    btnGrp2WSpc.add(jRadioSpaceHyp0);

    // "詳細設定"タブ内 "「○○」に「××」の注記"グループ
    JPanel tab4InnerPanel2 = new JPanel();
    tab4InnerPanel2.setLayout(new BoxLayout(tab4InnerPanel2, BoxLayout.X_AXIS));
    tab4InnerPanel2.setBorder(new NarrowTitledBorder("「○○」に「××」の注記"));
    tab4RootPanel.add(tab4InnerPanel2);
    ButtonGroup btnGrpNotice = new ButtonGroup();
    jRadioChukiRuby0 = new JRadioButton("非表示", true);
    jRadioChukiRuby0.setFocusPainted(false);
    jRadioChukiRuby0.setIconTextGap(2);
    jRadioChukiRuby0.setBorder(padding2);
    tab4InnerPanel2.add(jRadioChukiRuby0);
    btnGrpNotice.add(jRadioChukiRuby0);
    jRadioChukiRuby1 = new JRadioButton("ルビ");
    jRadioChukiRuby1.setToolTipText("○○のルビとして××を表示します(「ママ」の注記は非表示)");
    jRadioChukiRuby1.setFocusPainted(false);
    jRadioChukiRuby1.setIconTextGap(2);
    jRadioChukiRuby1.setBorder(padding2);
    tab4InnerPanel2.add(jRadioChukiRuby1);
    btnGrpNotice.add(jRadioChukiRuby1);
    jRadioChukiRuby2 = new JRadioButton("小書き");
    jRadioChukiRuby2.setToolTipText("○○の後ろに××を小書きで表示します(「ママ」の注記は非表示)");
    jRadioChukiRuby2.setFocusPainted(false);
    jRadioChukiRuby2.setIconTextGap(2);
    jRadioChukiRuby2.setBorder(padding2);
    tab4InnerPanel2.add(jRadioChukiRuby2);
    btnGrpNotice.add(jRadioChukiRuby2);

    // "詳細設定"タブ内 "自動縦中横"グループ
    JPanel tab4InnerPanel3 = new JPanel();
    tab4InnerPanel3.setLayout(new BoxLayout(tab4InnerPanel3, BoxLayout.X_AXIS));
    tab4InnerPanel3.setBorder(new NarrowTitledBorder("自動縦中横"));
    tab4RootPanel.add(tab4InnerPanel3);
    // 半角2文字縦書き
    jCheckAutoYoko = new JCheckBox("有効 ", true);
    jCheckAutoYoko.setFocusPainted(false);
    jCheckAutoYoko.setToolTipText("半角の2文字の数字、2～3文字の!?を縦中横で表示します。(前後に半角が無い場合)");
    jCheckAutoYoko.setBorder(padding2);
    tab4InnerPanel3.add(jCheckAutoYoko);
    label = new JLabel("+数字(");
    label.setBorder(padding0);
    tab4InnerPanel3.add(label);
    // 半角数字1文字縦書き
    jCheckAutoYokoNum1 = new JCheckBox("1桁 ");
    jCheckAutoYokoNum1.setFocusPainted(false);
    jCheckAutoYokoNum1.setIconTextGap(1);
    jCheckAutoYokoNum1.setBorder(padding2);
    tab4InnerPanel3.add(jCheckAutoYokoNum1);
    // 半角数字3文字縦書き
    jCheckAutoYokoNum3 = new JCheckBox("3桁");
    jCheckAutoYokoNum3.setFocusPainted(false);
    jCheckAutoYokoNum3.setIconTextGap(1);
    jCheckAutoYokoNum3.setBorder(padding2);
    tab4InnerPanel3.add(jCheckAutoYokoNum3);
    label = new JLabel(") +");
    label.setBorder(padding0);
    tab4InnerPanel3.add(label);
    // !? 1文字
    jCheckAutoEQ1 = new JCheckBox("!? 1文字");
    jCheckAutoEQ1.setFocusPainted(false);
    jCheckAutoEQ1.setIconTextGap(3);
    jCheckAutoEQ1.setBorder(padding2);
    tab4InnerPanel3.add(jCheckAutoEQ1);

    // "詳細設定"タブ内 "コメント出力"グループ
    JPanel tab4InnerPanel4 = new JPanel();
    tab4InnerPanel4.setLayout(new BoxLayout(tab4InnerPanel4, BoxLayout.X_AXIS));
    tab4InnerPanel4.setBorder(new NarrowTitledBorder("コメントブロック出力"));
    tab4RootPanel.add(tab4InnerPanel4);
    // 半角2文字縦書き
    jCheckCommentPrint = new JCheckBox("コメント出力 ");
    jCheckCommentPrint.setToolTipText("コメント行の間を出力します");
    jCheckCommentPrint.setFocusPainted(false);
    jCheckCommentPrint.setBorder(padding2);
    tab4InnerPanel4.add(jCheckCommentPrint);
    // 半角2文字縦書き
    jCheckCommentConvert = new JCheckBox("コメント内注記変換");
    jCheckCommentConvert.setToolTipText("コメント内の注記を変換します");
    jCheckCommentConvert.setFocusPainted(false);
    jCheckCommentConvert.setBorder(padding2);
    tab4InnerPanel4.add(jCheckCommentConvert);

    // "詳細設定"タブ内 "栞用ID"グループ
    JPanel tab4InnerPanel5 = new JPanel();
    tab4InnerPanel5.setLayout(new BoxLayout(tab4InnerPanel5, BoxLayout.X_AXIS));
    tab4InnerPanel5.setBorder(new NarrowTitledBorder("栞用ID"));
    tab4RootPanel.add(tab4InnerPanel5);
    jCheckMarkId = new JCheckBox("各行に出力");
    jCheckMarkId.setToolTipText("Kobo向けの栞を記憶するためのIDを各行に設定します");
    jCheckMarkId.setFocusPainted(false);
    jCheckMarkId.setBorder(padding2);
    tab4InnerPanel5.add(jCheckMarkId);

    // "詳細設定"タブ内 "空行除去"グループ
    JPanel tab4InnerPanel6 = new JPanel();
    tab4InnerPanel6.setLayout(new BoxLayout(tab4InnerPanel6, BoxLayout.X_AXIS));
    tab4InnerPanel6.setBorder(new NarrowTitledBorder("空行除去"));
    tab4RootPanel.add(tab4InnerPanel6);
    jComboxRemoveEmptyLine = new JComboBox(new String[] { "0", "1", "2", "3", "4", "5" });
    jComboxRemoveEmptyLine.setToolTipText("空行の行数を減らします 見出し行の後ろ3行以内は1行残します");
    jComboxRemoveEmptyLine.setFocusable(false);
    jComboxRemoveEmptyLine.setBorder(padding0);
    jComboxRemoveEmptyLine.setMaximumSize(new Dimension(96, 20));
    ((JLabel) jComboxRemoveEmptyLine.getRenderer()).setBorder(padding2);
    tab4InnerPanel6.add(jComboxRemoveEmptyLine);
    label = new JLabel("行減らす");
    label.setBorder(padding2);
    tab4InnerPanel6.add(label);

    label = new JLabel(" 最大");
    label.setBorder(padding2);
    tab4InnerPanel6.add(label);
    jComboxMaxEmptyLine = new JComboBox(new String[] { "-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" });
    jComboxMaxEmptyLine.setToolTipText("空行の連続を指定行数以下に制限します");
    jComboxMaxEmptyLine.setFocusable(false);
    jComboxMaxEmptyLine.setBorder(padding0);
    jComboxMaxEmptyLine.setMaximumSize(new Dimension(96, 20));
    jComboxMaxEmptyLine.setPreferredSize(new Dimension(64, 20));
    tab4InnerPanel6.add(jComboxMaxEmptyLine);
    label = new JLabel("行");
    label.setBorder(padding2);
    tab4InnerPanel6.add(label);

    // "詳細設定"タブ内 "行頭字下げ"グループ
    JPanel tab4InnerPanel7 = new JPanel();
    tab4InnerPanel7.setLayout(new BoxLayout(tab4InnerPanel7, BoxLayout.X_AXIS));
    tab4InnerPanel7.setBorder(new NarrowTitledBorder("行頭字下げ"));
    tab4RootPanel.add(tab4InnerPanel7);
    jCheckForceIndent = new JCheckBox("有効     ");
    jCheckForceIndent.setToolTipText("行頭が「『―”（〈〔【と全角空白以外なら行頭に全角空白を追加します 半角空白のみは全角に置き換えます");
    jCheckForceIndent.setFocusPainted(false);
    jCheckForceIndent.setBorder(padding2);
    tab4InnerPanel7.add(jCheckForceIndent);

    // "詳細設定"タブ内 "強制改ページ"グループ
    JPanel tab4InnerPanel8 = new JPanel();
    tab4InnerPanel8.setLayout(new BoxLayout(tab4InnerPanel8, BoxLayout.X_AXIS));
    tab4InnerPanel8.setBorder(new NarrowTitledBorder("強制改ページ"));
    tab4RootPanel.add(tab4InnerPanel8);

    jCheckPageBreak = new JCheckBox("有効", true);
    jCheckPageBreak.setToolTipText("指定サイズを超えた時点で強制改ページ(ブロック注記の外側のみ)");
    jCheckPageBreak.setFocusPainted(false);
    jCheckPageBreak.setBorder(padding2);
    jCheckPageBreak.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        boolean selected = jCheckPageBreak.isSelected();
        jTextPageBreakSize.setEditable(selected);
        jTextPageBreakEmptySize.setEditable(selected);
        jTextPageBreakChapterSize.setEditable(selected);
      }
    });
    tab4InnerPanel8.add(jCheckPageBreak);

    jTextPageBreakSize = new JTextField("400");
    jTextPageBreakSize.setMaximumSize(text4);
    jTextPageBreakSize.setPreferredSize(text4);
    jTextPageBreakSize.setInputVerifier(new IntegerInputVerifier(400, 1, 9999));
    jTextPageBreakSize.setEditable(jCheckPageBreak.isSelected());
    jTextPageBreakSize.addFocusListener(new TextSelectFocusListener(jTextPageBreakSize));
    tab4InnerPanel8.add(jTextPageBreakSize);
    label = new JLabel("KB ");
    label.setBorder(padding2);
    tab4InnerPanel8.add(label);

    jCheckPageBreakEmpty = new JCheckBox("空行(");
    jCheckPageBreakEmpty.setFocusPainted(false);
    jCheckPageBreakEmpty.setBorder(padding2);
    tab4InnerPanel8.add(jCheckPageBreakEmpty);
    jComboxPageBreakEmptyLine = new JComboBox(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" });
    jComboxPageBreakEmptyLine.setSelectedIndex(1);
    jComboxPageBreakEmptyLine.setFocusable(false);
    jComboxPageBreakEmptyLine.setBorder(padding0);
    jComboxPageBreakEmptyLine.setMaximumSize(text5);
    jComboxPageBreakEmptyLine.setPreferredSize(text5);
    ((JLabel) jComboxPageBreakEmptyLine.getRenderer()).setBorder(padding2);
    tab4InnerPanel8.add(jComboxPageBreakEmptyLine);
    label = new JLabel("行以上 ");
    label.setBorder(padding2);
    tab4InnerPanel8.add(label);
    jTextPageBreakEmptySize = new JTextField("300");
    jTextPageBreakEmptySize.setMaximumSize(text4);
    jTextPageBreakEmptySize.setPreferredSize(text4);
    jTextPageBreakEmptySize.setInputVerifier(new IntegerInputVerifier(300, 1, 9999));
    jTextPageBreakEmptySize.setEditable(jCheckPageBreak.isSelected());
    jTextPageBreakEmptySize.addFocusListener(new TextSelectFocusListener(jTextPageBreakEmptySize));
    tab4InnerPanel8.add(jTextPageBreakEmptySize);
    label = new JLabel("KB) ");
    label.setBorder(padding2);
    tab4InnerPanel8.add(label);

    jCheckPageBreakChapter = new JCheckBox("見出し前(");
    jCheckPageBreakChapter.setFocusPainted(false);
    jCheckPageBreakChapter.setBorder(padding2);
    tab4InnerPanel8.add(jCheckPageBreakChapter);
    jTextPageBreakChapterSize = new JTextField("200");
    jTextPageBreakChapterSize.setMaximumSize(text4);
    jTextPageBreakChapterSize.setPreferredSize(text4);
    jTextPageBreakChapterSize.setInputVerifier(new IntegerInputVerifier(200, 1, 9999));
    jTextPageBreakChapterSize.setEditable(jCheckPageBreak.isSelected());
    jTextPageBreakChapterSize.addFocusListener(new TextSelectFocusListener(jTextPageBreakChapterSize));
    tab4InnerPanel8.add(jTextPageBreakChapterSize);
    label = new JLabel("KB) ");
    label.setBorder(padding2);
    tab4InnerPanel8.add(label);

    // "目次"タブ
    JPanel tab5RootPanel = new JPanel();
    tab5RootPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
    tabbedpane.addTab("目次", new ImageIcon(this.getClass().getResource("/images/toc.png")), tab5RootPanel);

    // "目次"タブ内 "目次設定"グループ
    JPanel tab5InnerPanel1 = new JPanel();
    tab5InnerPanel1.setLayout(new BoxLayout(tab5InnerPanel1, BoxLayout.Y_AXIS));
    tab5InnerPanel1.setBorder(new NarrowTitledBorder("目次設定"));
    tab5RootPanel.add(tab5InnerPanel1);
    JPanel tab5Inner1UpperPanel = new JPanel();
    tab5Inner1UpperPanel.setLayout(new BoxLayout(tab5Inner1UpperPanel, BoxLayout.X_AXIS));

    // 最大文字数
    label = new JLabel(" 最大文字数");
    label.setBorder(padding2);
    tab5Inner1UpperPanel.add(label);
    jTextMaxChapterNameLength = new JTextField("64");
    jTextMaxChapterNameLength.setHorizontalAlignment(JTextField.RIGHT);
    jTextMaxChapterNameLength.setInputVerifier(new IntegerInputVerifier(64, 1, 999));
    jTextMaxChapterNameLength.setMaximumSize(text3);
    jTextMaxChapterNameLength.setPreferredSize(text3);
    jTextMaxChapterNameLength.addFocusListener(new TextSelectFocusListener(jTextMaxChapterNameLength));
    tab5Inner1UpperPanel.add(jTextMaxChapterNameLength);

    label = new JLabel("  ");
    label.setBorder(padding2);
    tab5Inner1UpperPanel.add(label);

    // 表紙
    jCheckCoverPageToc = new JCheckBox("表紙 ");
    jCheckCoverPageToc.setToolTipText("表紙画像のページを目次に追加します");
    jCheckCoverPageToc.setFocusPainted(false);
    jCheckCoverPageToc.setBorder(padding2);
    tab5Inner1UpperPanel.add(jCheckCoverPageToc);

    // 表紙
    jCheckTitleToc = new JCheckBox("表題 ", true);
    jCheckTitleToc.setToolTipText("表題の行を目次に追加します");
    jCheckTitleToc.setFocusPainted(false);
    jCheckTitleToc.setBorder(padding2);
    tab5Inner1UpperPanel.add(jCheckTitleToc);

    jCheckChapterUseNextLine = new JCheckBox("次の行を繋げる ");
    jCheckChapterUseNextLine.setToolTipText("次の行が空行でなければ見出しの後ろに繋げます");
    jCheckChapterUseNextLine.setFocusPainted(false);
    jCheckChapterUseNextLine.setBorder(padding2);
    tab5Inner1UpperPanel.add(jCheckChapterUseNextLine);

    jCheckChapterExclude = new JCheckBox("連続する見出しを除外", true);
    jCheckChapterExclude.setToolTipText("3つ以上連続する自動抽出された見出しを除外します(空行1行間隔も連続扱い)");
    jCheckChapterExclude.setFocusPainted(false);
    jCheckChapterExclude.setBorder(padding2);
    tab5Inner1UpperPanel.add(jCheckChapterExclude);

    tab5InnerPanel1.add(tab5Inner1UpperPanel);

    JPanel tab5Inner1LowerPanel = new JPanel();
    tab5Inner1LowerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));

    // nav階層化
    jCheckNavNest = new JCheckBox("目次ページ階層化 ");
    jCheckNavNest.setToolTipText("目次ページ(nav.xhtml)を階層化します");
    jCheckNavNest.setFocusPainted(false);
    jCheckNavNest.setBorder(padding2);
    tab5Inner1LowerPanel.add(jCheckNavNest);

    // 表紙
    jCheckNcxNest = new JCheckBox("目次(ncx)階層化 ");
    jCheckNcxNest.setToolTipText("目次(toc.ncx)を階層化します");
    jCheckNcxNest.setFocusPainted(false);
    jCheckNcxNest.setBorder(padding2);
    tab5Inner1LowerPanel.add(jCheckNcxNest);

    tab5InnerPanel1.add(tab5Inner1LowerPanel);

    // "目次"タブ内 "目次抽出"グループ
    JPanel tab5InnerPanel2 = new JPanel();
    tab5InnerPanel2.setLayout(new BoxLayout(tab5InnerPanel2, BoxLayout.Y_AXIS));
    tab5InnerPanel2.setBorder(new NarrowTitledBorder("目次抽出"));
    tab5RootPanel.add(tab5InnerPanel2);

    JPanel tab5Inner2UpperPanel = new JPanel();
    tab5Inner2UpperPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
    tab5Inner2UpperPanel.setMaximumSize(panelVMaxSize);
    tab5Inner2UpperPanel.setBorder(padding3B);
    tab5InnerPanel2.add(tab5Inner2UpperPanel);
    // 見出し注記
    label = new JLabel("注記(");
    label.setBorder(padding2);
    tab5Inner2UpperPanel.add(label);
    jCheckChapterH = new JCheckBox("見出し ", true);
    jCheckChapterH.setFocusPainted(false);
    jCheckChapterH.setBorder(padding2);
    tab5Inner2UpperPanel.add(jCheckChapterH);
    jCheckChapterH1 = new JCheckBox("大見出し ", true);
    jCheckChapterH1.setFocusPainted(false);
    jCheckChapterH1.setBorder(padding2);
    tab5Inner2UpperPanel.add(jCheckChapterH1);
    jCheckChapterH2 = new JCheckBox("中見出し ", true);
    jCheckChapterH2.setFocusPainted(false);
    jCheckChapterH2.setBorder(padding2);
    tab5Inner2UpperPanel.add(jCheckChapterH2);
    jCheckChapterH3 = new JCheckBox("小見出し ) ", true);
    jCheckChapterH3.setFocusPainted(false);
    jCheckChapterH3.setBorder(padding2);
    tab5Inner2UpperPanel.add(jCheckChapterH3);

    jCheckSameLineChapter = new JCheckBox("同行見出し含む", false);
    jCheckSameLineChapter.setFocusPainted(false);
    jCheckSameLineChapter.setBorder(padding2);
    tab5Inner2UpperPanel.add(jCheckSameLineChapter);

    tab5Inner2UpperPanel = new JPanel();
    tab5Inner2UpperPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
    tab5Inner2UpperPanel.setMaximumSize(panelVMaxSize);
    tab5Inner2UpperPanel.setBorder(padding3B);
    tab5InnerPanel2.add(tab5Inner2UpperPanel);
    // 改ページ後を目次に追加
    jCheckChapterSection = new JCheckBox("改ページ後 ", true);
    jCheckChapterSection.setToolTipText("改ページ後の先頭行の文字を目次に出力します");
    jCheckChapterSection.setFocusPainted(false);
    jCheckChapterSection.setBorder(padding2);
    tab5Inner2UpperPanel.add(jCheckChapterSection);
    jCheckChapterName = new JCheckBox("章見出し (第～章/その～/～章/序/プロローグ 等)", true);
    jCheckChapterName.setToolTipText("第～話/第～章/第～篇/第～部/第～節/第～幕/第～編/その～/～章/プロローグ/エピローグ/モノローグ/序/序章/終章/転章/間章/幕間");
    jCheckChapterName.setFocusPainted(false);
    jCheckChapterName.setBorder(padding2);
    tab5Inner2UpperPanel.add(jCheckChapterName);

    JPanel tab5Inner2LowerPanel = new JPanel();
    tab5Inner2LowerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
    tab5Inner2LowerPanel.setMaximumSize(panelVMaxSize);
    tab5Inner2LowerPanel.setBorder(padding3B);
    tab5InnerPanel2.add(tab5Inner2LowerPanel);
    jCheckChapterNumOnly = new JCheckBox("数字のみ");
    jCheckChapterNumOnly.setFocusPainted(false);
    jCheckChapterNumOnly.setBorder(padding2);
    tab5Inner2LowerPanel.add(jCheckChapterNumOnly);
    jCheckChapterNumTitle = new JCheckBox("数字+見出し  ");
    jCheckChapterNumTitle.setFocusPainted(false);
    jCheckChapterNumTitle.setBorder(padding2);
    tab5Inner2LowerPanel.add(jCheckChapterNumTitle);
    jCheckChapterNumParen = new JCheckBox("括弧内数字のみ");
    jCheckChapterNumParen.setToolTipText("（）〈〉〔〕【】内の数字");
    jCheckChapterNumParen.setFocusPainted(false);
    jCheckChapterNumParen.setBorder(padding2);
    tab5Inner2LowerPanel.add(jCheckChapterNumParen);
    jCheckChapterNumParenTitle = new JCheckBox("括弧内数字+見出し");
    jCheckChapterNumParenTitle.setFocusPainted(false);
    jCheckChapterNumParenTitle.setBorder(padding2);
    tab5Inner2LowerPanel.add(jCheckChapterNumParenTitle);

    tab5Inner2LowerPanel = new JPanel();
    tab5Inner2LowerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
    tab5Inner2LowerPanel.setMaximumSize(panelVMaxSize);
    tab5Inner2LowerPanel.setBorder(padding0);
    tab5InnerPanel2.add(tab5Inner2LowerPanel);
    jCheckChapterPattern = new JCheckBox("その他パターン");
    jCheckChapterPattern.setToolTipText("目次抽出パターンを正規表現で指定します。前後の空白とタグを除いた文字列と比較します。");
    jCheckChapterPattern.setFocusPainted(false);
    jCheckChapterPattern.setBorder(padding2);
    jCheckChapterPattern.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        jComboChapterPattern.setEditable(jCheckChapterPattern.isSelected());
        jComboChapterPattern.repaint();
      }
    });
    tab5Inner2LowerPanel.add(jCheckChapterPattern);
    jComboChapterPattern = new JComboBox(new String[] { "^(見出し１|見出し２|見出し３)$", "^(†|【|●|▼|■)",
        "^(0-9|０-９|一|二|三|四|五|六|七|八|九|十|〇)", "^[1|2|１|２]?[0-9|０-９]月[1-3|１-３]?[0-9|０-９]日",
        "^(一|十)?(一|二|三|四|五|六|七|八|九|十|〇)月(一|十|二十?|三十?)?(一|二|三|四|五|六|七|八|九|十|〇)日" });
    jComboChapterPattern.setBorder(padding0);
    jComboChapterPattern.setMaximumSize(text300);
    jComboChapterPattern.setPreferredSize(text300);
    jComboChapterPattern.setEditable(jCheckChapterPattern.isSelected());
    tab5Inner2LowerPanel.add(jComboChapterPattern);

    // "スタイル"タブ
    JPanel tab6RootPanel = new JPanel();
    tab6RootPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
    tabbedpane.addTab("スタイル", new ImageIcon(this.getClass().getResource("/images/style.png")), tab6RootPanel);

    // "スタイル"タブ内 "行の高さ"グループ
    JPanel tab6InnerPanel1 = new JPanel();
    tab6InnerPanel1.setLayout(new BoxLayout(tab6InnerPanel1, BoxLayout.X_AXIS));
    tab6InnerPanel1.setBorder(new NarrowTitledBorder("行の高さ"));
    tab6RootPanel.add(tab6InnerPanel1);
    jComboLineHeight = new JComboBox(new String[] { "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "2.0" });
    jComboLineHeight.setBorder(padding0);
    jComboLineHeight.setMaximumSize(combo3);
    jComboLineHeight.setPreferredSize(combo3);
    jComboLineHeight.setEditable(true);
    jComboLineHeight.setInputVerifier(new FloatInputVerifier(1.8f, 1));
    jComboLineHeight.setSelectedItem("1.8");
    tab6InnerPanel1.add(jComboLineHeight);
    label = new JLabel("文字");
    label.setBorder(padding2);
    tab6InnerPanel1.add(label);

    // "スタイル"タブ内 "文字サイズ"グループ
    JPanel tab6InnerPanel2 = new JPanel();
    tab6InnerPanel2.setLayout(new BoxLayout(tab6InnerPanel2, BoxLayout.X_AXIS));
    tab6InnerPanel2.setBorder(new NarrowTitledBorder("文字サイズ"));
    tab6RootPanel.add(tab6InnerPanel2);
    jComboFontSize = new JComboBox(
        new String[] { "75", "80", "85", "90", "95", "100", "105", "110", "115", "120", "125" });
    jComboFontSize.setToolTipText("標準フォントからの倍率を設定します");
    jComboFontSize.setBorder(padding0);
    jComboFontSize.setMaximumSize(combo3);
    jComboFontSize.setPreferredSize(combo3);
    jComboFontSize.setEditable(true);
    jComboFontSize.setInputVerifier(new IntegerInputVerifier(100, 10));
    jComboFontSize.setSelectedItem("100");
    tab6InnerPanel2.add(jComboFontSize);
    label = new JLabel("%");
    label.setBorder(padding2);
    tab6InnerPanel2.add(label);

    // "スタイル"タブ内 "太字ゴシック表示"グループ
    JPanel tab6InnerPanel3 = new JPanel();
    tab6InnerPanel3.setLayout(new BoxLayout(tab6InnerPanel3, BoxLayout.X_AXIS));
    tab6InnerPanel3.setBorder(new NarrowTitledBorder("太字ゴシック表示"));
    tab6RootPanel.add(tab6InnerPanel3);
    jCheckBoldUseGothic = new JCheckBox("太字注記", false);
    jCheckBoldUseGothic.setToolTipText("太字注記を太字ゴシックで表示します");
    jCheckBoldUseGothic.setFocusPainted(false);
    jCheckBoldUseGothic.setBorder(padding2);
    tab6InnerPanel3.add(jCheckBoldUseGothic);

    jCheckGothicUseBold = new JCheckBox("ゴシック体注記", false);
    jCheckGothicUseBold.setToolTipText("ゴシック体注記を太字ゴシックで表示します");
    jCheckGothicUseBold.setFocusPainted(false);
    jCheckGothicUseBold.setBorder(padding2);
    tab6InnerPanel3.add(jCheckGothicUseBold);

    // "スタイル"タブ内 余白設定グループ パネル内に子パネル2枚を横に並べる
    // "スタイル"タブ内 余白設定グループ 左パネル
    JPanel tab6InnerPanel4 = new JPanel();
    tab6InnerPanel4.setLayout(new BoxLayout(tab6InnerPanel4, BoxLayout.X_AXIS));
    tab6RootPanel.add(tab6InnerPanel4);

    JPanel tab6Inner1LeftPanel = new JPanel();
    tab6Inner1LeftPanel.setLayout(new BoxLayout(tab6Inner1LeftPanel, BoxLayout.X_AXIS));
    tab6Inner1LeftPanel.setBorder(new NarrowTitledBorder("テキスト余白 (@page margin)"));
    tab6InnerPanel4.add(tab6Inner1LeftPanel);
    String[] marginLabels = { "上", "右", "下", "左" };
    jTextPageMargins = new JTextField[4];
    NumberVerifier numberVerifier0 = new NumberVerifier(0, 0);
    // この部分、WindowBuilderでは表示されないので注意。実行すれば問題なく生成される
    for (int i = 0; i < jTextPageMargins.length; i++) {
      label = new JLabel(marginLabels[i]);
      label.setBorder(padding2);
      tab6Inner1LeftPanel.add(label);
      JTextField jTextField = new JTextField("0.5");
      jTextPageMargins[i] = jTextField;
      jTextField.setHorizontalAlignment(JTextField.RIGHT);
      jTextField.addFocusListener(new TextSelectFocusListener(jTextField));
      jTextField.setInputVerifier(numberVerifier0);
      jTextField.setMaximumSize(text3);
      jTextField.setPreferredSize(text3);
      tab6Inner1LeftPanel.add(jTextField);
    }
    tab6Inner1LeftPanel.add(new JLabel("  "));
    ButtonGroup group = new ButtonGroup();
    jRadioPageMarginUnit0 = new JRadioButton("字 ", true);
    jRadioPageMarginUnit0.setBorder(padding0);
    jRadioPageMarginUnit0.setFocusPainted(false);
    tab6Inner1LeftPanel.add(jRadioPageMarginUnit0);
    group.add(jRadioPageMarginUnit0);
    jRadioPageMarginUnit1 = new JRadioButton("%");
    jRadioPageMarginUnit1.setBorder(padding0);
    jRadioPageMarginUnit1.setFocusPainted(false);
    tab6Inner1LeftPanel.add(jRadioPageMarginUnit1);
    group.add(jRadioPageMarginUnit1);

    // "スタイル"タブ内 余白設定グループ 右パネル
    JPanel tab6Inner1RightPanel = new JPanel();
    tab6Inner1RightPanel.setLayout(new BoxLayout(tab6Inner1RightPanel, BoxLayout.X_AXIS));
    tab6Inner1RightPanel.setBorder(new NarrowTitledBorder("テキスト余白 (html margin) Reader用"));
    tab6InnerPanel4.add(tab6Inner1RightPanel);
    jTextBodyMargins = new JTextField[4];
    // この部分、WindowBuilderでは表示されないので注意。実行すれば問題なく生成される
    for (int i = 0; i < jTextBodyMargins.length; i++) {
      label = new JLabel(marginLabels[i]);
      label.setBorder(padding2);
      tab6Inner1RightPanel.add(label);
      JTextField jTextField = new JTextField("0");
      jTextBodyMargins[i] = jTextField;
      jTextField.setHorizontalAlignment(JTextField.RIGHT);
      jTextField.addFocusListener(new TextSelectFocusListener(jTextField));
      jTextField.setInputVerifier(numberVerifier0);
      jTextField.setMaximumSize(text3);
      jTextField.setPreferredSize(text3);
      tab6Inner1RightPanel.add(jTextField);
    }
    tab6Inner1RightPanel.add(new JLabel("  "));
    group = new ButtonGroup();
    jRadioBodyMarginUnit0 = new JRadioButton("字 ", true);
    jRadioBodyMarginUnit0.setBorder(padding0);
    jRadioBodyMarginUnit0.setFocusPainted(false);
    tab6Inner1RightPanel.add(jRadioBodyMarginUnit0);
    group.add(jRadioBodyMarginUnit0);
    jRadioBodyMarginUnit1 = new JRadioButton("%");
    jRadioBodyMarginUnit1.setBorder(padding0);
    jRadioBodyMarginUnit1.setFocusPainted(false);
    tab6Inner1RightPanel.add(jRadioBodyMarginUnit1);
    group.add(jRadioBodyMarginUnit1);

    // "スタイル"タブ内 "濁点/半濁点文字"グループ
    JPanel tab6InnerPanel5 = new JPanel();
    tab6InnerPanel5.setLayout(new BoxLayout(tab6InnerPanel5, BoxLayout.X_AXIS));
    tab6InnerPanel5.setBorder(new NarrowTitledBorder("濁点/半濁点文字"));
    tab6RootPanel.add(tab6InnerPanel5);
    ButtonGroup btnGrpInscMark = new ButtonGroup();
    jRadioDakutenType0 = new JRadioButton("そのまま");
    jRadioDakutenType0.setToolTipText("結合文字は通常の文字に変換されます");
    jRadioDakutenType0.setBorder(padding2);
    jRadioDakutenType0.setFocusPainted(false);
    tab6InnerPanel5.add(jRadioDakutenType0);
    btnGrpInscMark.add(jRadioDakutenType0);
    jRadioDakutenType1 = new JRadioButton("重ねる", true);
    jRadioDakutenType1.setToolTipText("Reader,Kobo,Kindle以外はずれる場合があります。ルビ内はそのまま出力します");
    jRadioDakutenType1.setBorder(padding2);
    jRadioDakutenType1.setFocusPainted(false);
    tab6InnerPanel5.add(jRadioDakutenType1);
    btnGrpInscMark.add(jRadioDakutenType1);
    jRadioDakutenType2 = new JRadioButton("フォント", true);
    jRadioDakutenType2.setToolTipText("一文字フォントを利用します。端末によっては太字斜体表示できません");
    jRadioDakutenType2.setBorder(padding2);
    jRadioDakutenType2.setFocusPainted(false);
    tab6InnerPanel5.add(jRadioDakutenType2);
    group.add(jRadioDakutenType2);

    // "スタイル"タブ内 "IVS出力"グループ
    JPanel tab6InnerPanel6 = new JPanel();
    tab6InnerPanel6.setLayout(new BoxLayout(tab6InnerPanel6, BoxLayout.X_AXIS));
    tab6InnerPanel6.setBorder(new NarrowTitledBorder("IVS出力(Kobo,Kindle非対応)"));
    tab6RootPanel.add(tab6InnerPanel6);
    jCheckIvsBMP = new JCheckBox("英数字用(U+FE00-FE0E)", false);
    jCheckIvsBMP.setToolTipText("英数字、絵文字向けのIVSを出力します");
    jCheckIvsBMP.setFocusPainted(false);
    jCheckIvsBMP.setBorder(padding2);
    tab6InnerPanel6.add(jCheckIvsBMP);
    jCheckIvsSSP = new JCheckBox("漢字用(U+E0100-E01EF)", false);
    jCheckIvsSSP.setToolTipText("漢字用のIVSを出力します");
    jCheckIvsSSP.setFocusPainted(false);
    jCheckIvsSSP.setBorder(padding2);
    tab6InnerPanel6.add(jCheckIvsSSP);

    // "Web"タブ
    JPanel tab7RootPanel = new JPanel();
    tab7RootPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
    tabbedpane.addTab("Web", new ImageIcon(this.getClass().getResource("/images/web.png")), tab7RootPanel);

    // "Web"タブ内 "取得間隔"グループ
    JPanel tab7InnerPanel1 = new JPanel();
    tab7InnerPanel1.setLayout(new BoxLayout(tab7InnerPanel1, BoxLayout.X_AXIS));
    tab7InnerPanel1.setBorder(new NarrowTitledBorder("取得設定"));
    tab7RootPanel.add(tab7InnerPanel1);
    label = new JLabel("取得間隔");
    label.setBorder(padding2);
    label.setToolTipText("Web小説の取得間隔を設定します");
    tab7InnerPanel1.add(label);
    jTextWebInterval = new JTextField("0.5");
    jTextWebInterval.setToolTipText(label.getToolTipText());
    jTextWebInterval.setHorizontalAlignment(JTextField.RIGHT);
    jTextWebInterval.setInputVerifier(new FloatInputVerifier(0.5f, 0, 60));
    jTextWebInterval.setMaximumSize(text3);
    jTextWebInterval.setPreferredSize(text3);
    jTextWebInterval.addFocusListener(new TextSelectFocusListener(jTextWebInterval));
    tab7InnerPanel1.add(jTextWebInterval);
    label = new JLabel("秒");
    label.setBorder(padding1);
    tab7InnerPanel1.add(label);

    // "Web"タブ内 "キャッシュ保存先"グループ
    JPanel tab7InnerPanel2 = new JPanel();
    tab7InnerPanel2.setLayout(new BoxLayout(tab7InnerPanel2, BoxLayout.X_AXIS));
    tab7InnerPanel2.setBorder(new NarrowTitledBorder("キャッシュ保存パス"));
    tab7RootPanel.add(tab7InnerPanel2);
    jTextCachePath = new JTextField("cache");
    jTextCachePath.setToolTipText("キャッシュファイルを保存するパスです。フルパスまたは起動パスからの相対パスを指定します");
    jTextCachePath.setMaximumSize(text300);
    jTextCachePath.setPreferredSize(text300);
    jTextCachePath.addFocusListener(new TextSelectFocusListener(jTextCachePath));
    tab7InnerPanel2.add(jTextCachePath);
    JButton jButtonCachePath = new JButton("選択");
    jButtonCachePath.setBorder(padding2);
    jButtonCachePath.setIcon(new ImageIcon(this.getClass().getResource("/images/dst_path.png")));
    jButtonCachePath.setFocusPainted(false);
    jButtonCachePath.addActionListener(new CachePathChooserListener(jButtonCachePath));
    tab7InnerPanel2.add(jButtonCachePath);

    // "Web"タブ内 "更新判定"グループ
    JPanel tab7InnerPanel3 = new JPanel();
    tab7InnerPanel3.setLayout(new BoxLayout(tab7InnerPanel3, BoxLayout.X_AXIS));
    tab7InnerPanel3.setBorder(new NarrowTitledBorder("更新判定"));
    tab7RootPanel.add(tab7InnerPanel3);
    jTextWebModifiedExpire = new JTextField("24");
    jTextWebModifiedExpire.setToolTipText("この時間以内に取得したキャッシュを更新分として処理します");
    jTextWebModifiedExpire.setHorizontalAlignment(JTextField.RIGHT);
    jTextWebModifiedExpire.setInputVerifier(new NumberVerifier(24, 0, 9999));
    jTextWebModifiedExpire.setMaximumSize(text4);
    jTextWebModifiedExpire.setPreferredSize(text4);
    jTextWebModifiedExpire.addFocusListener(new TextSelectFocusListener(jTextWebModifiedExpire));
    tab7InnerPanel3.add(jTextWebModifiedExpire);
    label = new JLabel("時間以内");
    label.setBorder(padding1);
    label.setToolTipText(jTextWebModifiedExpire.getToolTipText());
    tab7InnerPanel3.add(label);

    // "Web"タブ内 "ePub出力設定"グループ
    JPanel tab7InnerPanel4 = new JPanel();
    tab7InnerPanel4.setLayout(new BoxLayout(tab7InnerPanel4, BoxLayout.X_AXIS));
    tab7InnerPanel4.setBorder(new NarrowTitledBorder("ePub出力設定"));
    tab7RootPanel.add(tab7InnerPanel4);
    jCheckWebConvertUpdated = new JCheckBox("更新時のみ出力");
    jCheckWebConvertUpdated.setToolTipText("新規追加または一覧ページで更新がある場合のみePubファイルを出力します");
    jCheckWebConvertUpdated.setFocusPainted(false);
    jCheckWebConvertUpdated.setBorder(padding2);
    tab7InnerPanel4.add(jCheckWebConvertUpdated);

    // "Web"タブ内 "変換対象"グループ
    JPanel tab7InnerPanel5 = new JPanel();
    tab7InnerPanel5.setLayout(new BoxLayout(tab7InnerPanel5, BoxLayout.X_AXIS));
    tab7InnerPanel5.setBorder(new NarrowTitledBorder("変換対象"));
    tab7RootPanel.add(tab7InnerPanel5);
    jCheckWebBeforeChapter = new JCheckBox("最新");
    jCheckWebBeforeChapter.setToolTipText("最新話から指定話数のみ出力します。追加更新分のみの出力がある場合はそれに追加されます");
    jCheckWebBeforeChapter.setFocusPainted(false);
    jCheckWebBeforeChapter.setBorder(padding0);
    jCheckWebBeforeChapter.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        jTextWebBeforeChapterCount.setEditable(jCheckWebBeforeChapter.isSelected());
        jTextWebBeforeChapterCount.repaint();
      }
    });
    tab7InnerPanel5.add(jCheckWebBeforeChapter);
    jTextWebBeforeChapterCount = new JTextField("1");
    jTextWebBeforeChapterCount.setToolTipText(jCheckWebBeforeChapter.getToolTipText());
    jTextWebBeforeChapterCount.setEditable(false);
    jTextWebBeforeChapterCount.setHorizontalAlignment(JTextField.RIGHT);
    jTextWebBeforeChapterCount.setInputVerifier(new IntegerInputVerifier(0, 0, 999));
    jTextWebBeforeChapterCount.setMaximumSize(text3);
    jTextWebBeforeChapterCount.setPreferredSize(text3);
    jTextWebBeforeChapterCount.addFocusListener(new TextSelectFocusListener(jTextWebBeforeChapterCount));
    tab7InnerPanel5.add(jTextWebBeforeChapterCount);
    label = new JLabel("話 +");
    label.setBorder(padding1);
    tab7InnerPanel5.add(label);
    jCheckWebModifiedOnly = new JCheckBox("更新分");
    jCheckWebModifiedOnly.setToolTipText("追加更新のあった話のみ変換します");
    jCheckWebModifiedOnly.setFocusPainted(false);
    jCheckWebModifiedOnly.setBorder(padding2);
    tab7InnerPanel5.add(jCheckWebModifiedOnly);
    tab7InnerPanel5.add(new JLabel("("));
    jCheckWebModifiedTail = new JCheckBox("連続");
    jCheckWebModifiedTail.setToolTipText("最新話から連続した更新分のみ変換します。途中話の更新は変換されません");
    jCheckWebModifiedTail.setFocusPainted(false);
    jCheckWebModifiedTail.setBorder(padding2);
    tab7InnerPanel5.add(jCheckWebModifiedTail);
    tab7InnerPanel5.add(new JLabel(")"));

  }

  ////////////////////////////////////////////////////////////////

  // TODO propsの内容をGUIオブジェクトに反映させるメソッドをここで実行
  // 定義直後にやってるのもあるのでここに移動させる方がいいかも

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

  /** コンポーネント内をすべてsetEnabled */
  private void setEnabledAll(Component c, boolean b) {
    if (c instanceof JPanel) {
      for (Component c2 : ((Container) c).getComponents())
        setEnabledAll(c2, b);
    } else {// if (!(c instanceof JLabel)) {
      c.setEnabled(b);
    }
  }

  /** 画像縮小回転可否のチェックボックスをON/OFF */
  private void setResizeTextEditable(boolean enabled) {
    if (enabled) {
      this.jTextResizeNumW.setEditable(jCheckResizeW.isSelected());
      this.jTextResizeNumH.setEditable(jCheckResizeH.isSelected());
      // this.jTextPixelW.setEditable(jCheckPixel.isSelected());
      // this.jTextPixelH.setEditable(jCheckPixel.isSelected());
    } else {
      this.jTextResizeNumW.setEditable(false);
      this.jTextResizeNumH.setEditable(false);
      // this.jTextPixelW.setEditable(false);
      // this.jTextPixelH.setEditable(false);
    }
  }

  /** キャッシュパス選択ボタンイベント */
  class CachePathChooserListener implements ActionListener {
    Component parent;

    private CachePathChooserListener(Component parent) {
      this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      File path = new File(jTextCachePath.getText());
      if (!path.isDirectory())
        path = path.getParentFile();
      if (path != null && !path.isDirectory())
        path = path.getParentFile();
      JFileChooser fileChooser = new JFileChooser(path);
      fileChooser.setDialogTitle("キャッシュ出力先を選択");
      fileChooser.setApproveButtonText("選択");
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int state = fileChooser.showOpenDialog(parent);
      switch (state) {
      case JFileChooser.APPROVE_OPTION:
        String pathString = fileChooser.getSelectedFile().getAbsolutePath();
        try {
          // パス調整
          String rootPath = new File("").getCanonicalPath();
          if (pathString.startsWith(rootPath)) {
            pathString = pathString.substring(rootPath.length() + 1);
          }
        } catch (IOException e1) {
        }
        jTextCachePath.setText(pathString);
      }
    }
  }

  /** propsの内容をGUIオブジェクトに反映させる-コンストラクタの最後かイニシャライザで最初に実行 */
  private void setPropsValues(Properties props) {
    // TODO propsの内容をGUIオブジェクトに反映させるメソッド GUIが出来たら書く
  }

  // GUIオブジェクトの状態をプロファイルに保存してダイアログを閉じる
  private class ActionApplyChanges extends AbstractAction {
    public ActionApplyChanges() {
      putValue(NAME, "Apply");
      putValue(SHORT_DESCRIPTION, "Apply Changes and Close Dialog");
    }

    public void actionPerformed(ActionEvent e) {
      // TODO GUIオブジェクトの状態をプロファイルに書き出す処理
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
