import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.github.ghmk5.info.NovelList;
import com.github.ghmk5.info.NovelMeta;
import com.github.ghmk5.info.Properties;
import com.github.ghmk5.swing.DialogConverterSettings;
import com.github.hmdev.converter.AozoraEpub3Converter;
import com.github.hmdev.info.ProfileInfo;
import com.github.hmdev.util.CharUtils;
import com.github.hmdev.util.LogAppender;
import com.github.hmdev.web.WebAozoraConverter;
import com.github.hmdev.writer.Epub3ImageWriter;
import com.github.hmdev.writer.Epub3Writer;
import com.opencsv.CSVWriter;

/**
 * @author mk5
 *
 */
public class ListWindow {

  /** 青空→ePub3変換クラス */
  AozoraEpub3Converter aozoraConverter;

  /** Web小説青空変換クラス */
  WebAozoraConverter webConverter;

  /** ePub3出力クラス */
  Epub3Writer epub3Writer;

  /** ePub3画像出力クラス */
  Epub3ImageWriter epub3ImageWriter;

  /** 変換をキャンセルした場合true */
  boolean convertCanceled = false;
  /** 変換実行中 */
  boolean running = false;

  Process kindleProcess;

  /** 設定ファイル */
  public Properties props;

  /** 設定ファイル名 */
  String propFileName = "nucJ.ini";

  /** jarファイルのあるパス文字列 "/"含む */
  String jarPath = null;

  /** 前回の出力パス */
  File currentPath = null;
  /** キャッシュ保存パス */
  File cachePath = null;
  /** Web小説取得情報格納パス */
  File webConfigPath = null;

  /** 選択されているプロファイル */
  ProfileInfo selectedProfile;
  /** プロファイル格納パス */
  File profilePath;
  // ここまでAozoraEpub3関連の変数

  // 以下ListWindow関連の変数
  JFrame frame;
  JLabel lastChkLbl;
  JTable table;

  DefaultTableModel defaultTableModel;
  String urlString;
  private JTextField textField;
  NovelList novelList;
  File csvPath;
  String csvFileName = "NovelList.csv";
  RowSorter<TableModel> sorter;
  String dateLastChecked; // 最後に更新チェックした日時 Propertyとして保存するのでString
  String datePrevChecked; // その前の更新チェックの日時 同上
  static HashMap<String, String> lafMap;
  ButtonGroup mntmLafSubGroup;

  private final Action actionAddEntry = new ActionAddEntry();
  private final Action actionChkUpdt = new ActionChkUpdt();

  Font mplus2mMediumFont;
  Font mplus2mBoldFont;

  // イニシャライザ
  {
  }

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          ListWindow window = new ListWindow();

          // iniファイルに保存してあったウィンドウ位置情報の復元
          try {
            int x = (int) Float.parseFloat(window.props.getProperty("PosX"));
            int y = (int) Float.parseFloat(window.props.getProperty("PosY"));
            window.frame.setLocation(x, y);
          } catch (Exception e1) {
            LogAppender.println("ウィンドウ位置の復元に失敗した");
          }

          // iniファイルに保存してあったウィンドウサイズの復元
          try {
            int w = (int) Float.parseFloat(window.props.getProperty("SizeW"));
            int h = (int) Float.parseFloat(window.props.getProperty("SizeH"));
            window.frame.setSize(w, h);
          } catch (Exception e2) {
            LogAppender.println("ウィンドウサイズの復元に失敗した");
          }
          // window.frame.setSize(600, 655);

          // テーブルカラム幅の復元
          try {
            DefaultTableColumnModel defaultTableColumnModel = (DefaultTableColumnModel) window.table.getColumnModel();
            int numCulmns = defaultTableColumnModel.getColumnCount();
            TableColumn column = null;
            int savedwidth;
            for (int idx = 0; idx < numCulmns; idx++) {
              if (window.props.getProperty("WidthColumn" + idx) != null) {
                savedwidth = Integer.parseInt(window.props.getProperty("WidthColumn" + idx));
                column = defaultTableColumnModel.getColumn(idx);
                column.setPreferredWidth(savedwidth);
              }
            }
          } catch (Exception e3) {
            LogAppender.println("テーブルカラム幅の復元に失敗した");
          }

          // Look & Feelの復元
          String lafName = lafMap.get(window.props.getProperty("LastLook&Feel"));
          try {
            UIManager.setLookAndFeel(lafName);
            SwingUtilities.updateComponentTreeUI(window.frame);
          } catch (Exception e) {
            // TODO: handle exception
            LogAppender.println("Look & Feelを " + lafName + " に設定できなかった");
          }

          // 現在選択されているLook & Feelに対応するメニュー内ボタンモデルを選択する処理
          String className = UIManager.getLookAndFeel().getName();
          for (Enumeration<AbstractButton> e = window.mntmLafSubGroup.getElements(); e.hasMoreElements();) {
            AbstractButton button = e.nextElement();
            if (button.getText().equals(className)) {
              button.setSelected(true);
            }
          }

          window.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
              try {
                window.finalize();
              } catch (Throwable e) {
                e.printStackTrace();
              }
              System.exit(0);
            }
          });
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
    });
  }

  /**
   * Create the application.
   */
  public ListWindow() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {

    // 文字表示のアンチエイリアス有効化-Windowsでのみ必要
    System.setProperty("awt.useSystemAAFontSettings", "on");
    System.setProperty("swing.aatext", "true");

    this.jarPath = "";

    this.cachePath = new File(this.jarPath + "cache");
    this.webConfigPath = new File(this.jarPath + "web");
    this.profilePath = new File(this.jarPath + "profiles");
    this.profilePath.mkdir();
    this.csvPath = new File(this.jarPath + "csv");

    // DefaultTableModelを定義
    // このまま使用されるのはiniファイルから読み込めず、かつデフォルトiniからも読み込めなかった場合のみ
    DefaultTableModel defaultTableModel;
    String[] columnNames = { "novel ID", "author", "title", "chapters", "last updated" };
    defaultTableModel = new DefaultTableModel(columnNames, 0);

    // 使用可能なLook & Feelを取得
    LookAndFeelInfo[] lafis = UIManager.getInstalledLookAndFeels();
    String[] sLafis = new String[lafis.length];
    lafMap = new HashMap<>();
    for (int i = 0; i < lafis.length; i++) {
      sLafis[i] = lafis[i].getName();
      lafMap.put(sLafis[i], lafis[i].getClassName());
    }

    // 設定ファイル読み込み
    props = new Properties();

    try {
      FileInputStream fos = new FileInputStream(this.jarPath + this.propFileName);
      props.load(fos);
      fos.close();
    } catch (Exception e) {
      InputStream iStream = this.getClass().getResourceAsStream("defaults/nucJ_default.ini");
      try {
        props.load(iStream);
        iStream.close();
      } catch (IOException e1) {
        LogAppender.println("デフォルト設定ファイル defaults/nucJ_default.ini のロードに失敗した");
        e1.printStackTrace();
      }
    }

    String path = props.getProperty("LastDir");
    if (path != null && path.length() > 0)
      this.currentPath = new File(path);

    // テーブル表示につかうフォントをjar同梱のttfファイルから生成
    mplus2mMediumFont = null;
    mplus2mBoldFont = null;
    try {
      mplus2mMediumFont = Font.createFont(Font.TRUETYPE_FONT,
          this.getClass().getResourceAsStream("fonts/mplus-2m-medium.ttf"));
      mplus2mBoldFont = Font.createFont(Font.TRUETYPE_FONT,
          this.getClass().getResourceAsStream("fonts/mplus-2m-bold.ttf"));
    } catch (FontFormatException e2) {
      // TODO 自動生成された catch ブロック
      e2.printStackTrace();
    } catch (IOException e2) {
      // TODO 自動生成された catch ブロック
      e2.printStackTrace();
    }

    frame = new JFrame("Narrow Update checker J");
    frame.setMinimumSize(new Dimension(450, 600));
    // frame.setBounds(100, 100, 450, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JMenuBar menuBar = new JMenuBar();
    frame.setJMenuBar(menuBar);

    JMenu mnNewMenu = new JMenu("File");
    menuBar.add(mnNewMenu);

    // JMenuItem mntm1 = new JMenuItem("load CSV...");
    // mntm1.addActionListener(new ActionListener() {
    // // CSVファイルからDefaultTableModelを作ってtableに突っ込む処理
    // public void actionPerformed(ActionEvent e) {
    // JFileChooser filechooser = new JFileChooser("./csv");
    // int selected = filechooser.showOpenDialog(frame);
    // if (selected == JFileChooser.APPROVE_OPTION) {
    // File file = filechooser.getSelectedFile();
    // try {
    // novelList = new NovelList(file);
    // table.setModel(novelList.getTableModel());
    // sorter = new TableRowSorter<>(table.getModel());
    // table.setRowSorter(sorter);
    // } catch (IOException e1) {
    // // TODO 自動生成された catch ブロック
    // e1.printStackTrace();
    // }
    // }
    //
    // }
    // });
    // mnNewMenu.add(mntm1);

    // JMenuItem mntm2 = new JMenuItem("Switch table");
    // mntm2.addActionListener(new ActionListener() {
    // public void actionPerformed(ActionEvent e) {
    // // てきとうにDefaultTableModelを作ってtableに突っ込む処理2
    // // これで動的にテーブルの中身が切り替えられるか確かめるテスト
    // String[] columnNames = { "hoge", "fuga", "piyo" };
    // String[][] data = { { "hoge", "fuga", "piyo" }, { "hoge", "fuga", "piyo"
    // }, { "hoge", "fuga", "piyo" } };
    // table.setModel(new DefaultTableModel(data, columnNames));
    // sorter = new TableRowSorter<>(table.getModel());
    // table.setRowSorter(sorter);
    // }
    // });
    // mnNewMenu.add(mntm2);

    // JMenuItem mntm3 = new JMenuItem("Test Command");
    // mntm3.addActionListener(new ActionListener() {
    // public void actionPerformed(ActionEvent e) {
    // LogAppender.println("RowSelection: " + table.getRowSelectionAllowed());
    // LogAppender.println("ColumnSelection: " +
    // table.getColumnSelectionAllowed());
    // LogAppender.println("CellSelection: " + table.getCellSelectionEnabled());
    // }
    // });
    // mnNewMenu.add(mntm3);

    // 設定ダイアログを開くメニューアイテム
    JMenuItem menuOpenPrefDialog = new JMenuItem("設定...");
    menuOpenPrefDialog.addActionListener(new ActionListener() {
      // JFrame owner;

      public void actionPerformed(ActionEvent e) {
        DialogConverterSettings dialogConverterSettings = new DialogConverterSettings(frame, props);
        dialogConverterSettings.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosed(WindowEvent e) {
            LogAppender.println(dialogConverterSettings.testString);
            if (dialogConverterSettings.approved)
              props = dialogConverterSettings.props;
          }
        });
        dialogConverterSettings.setLocationRelativeTo(frame);
        dialogConverterSettings.setModal(true);
        dialogConverterSettings.setVisible(true);
        try {
          // 設定ファイル更新
          FileOutputStream fos = new FileOutputStream(jarPath + propFileName);
          props.store(fos, "nucJ Parameters");
          fos.close();
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    mnNewMenu.add(menuOpenPrefDialog);

    // Look & Feel切り替えメニューアイテム
    JMenuItem mntmLaF = new JMenu("Look & Feel");

    mntmLafSubGroup = new ButtonGroup();
    JRadioButtonMenuItem[] mntmLafSubs = new JRadioButtonMenuItem[sLafis.length];
    for (int i = 0; i < sLafis.length; i++) {

      mntmLafSubs[i] = new JRadioButtonMenuItem(sLafis[i]);

      int j = i;
      mntmLafSubs[i].addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          String className = lafMap.get(sLafis[j]);
          try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(frame);
            // LogAppender.println("Look & Feelを " + className + " に設定した");
            props.setProperty("LastLook&Feel", className);
          } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
              | UnsupportedLookAndFeelException e1) {
            // LogAppender.println("Look & Feelを " + className + " に設定できなかった");
            e1.printStackTrace();
          }
        }
      });
      mntmLafSubGroup.add(mntmLafSubs[i]);

      mntmLaF.add(mntmLafSubs[i]);
    }

    mnNewMenu.add(mntmLaF);

    // 全巻更新日時表示ラベル & 更新チェック実行ボタン
    JPanel rootPanel = new JPanel();
    rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
    frame.getContentPane().add(rootPanel, BorderLayout.CENTER);

    JPanel panel1 = new JPanel();
    panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    lastChkLbl = new JLabel("Last Checked: YYYY-MM-DD HH:MM:SS");
    dateLastChecked = props.getProperty("DateLastChecked");
    if (dateLastChecked != "" && dateLastChecked != null)
      lastChkLbl.setText("Last Checked: " + dateLastChecked);
    panel1.add(lastChkLbl);
    JButton btnChkUpdt = new JButton("Check Update");
    btnChkUpdt.setActionCommand("check");
    panel1.add(btnChkUpdt);
    btnChkUpdt.setAction(actionChkUpdt);
    rootPanel.add(panel1);

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BorderLayout(0, 0));
    // table = new JTable(defaultTableModel);
    table = new JTable(defaultTableModel) {
      // カラムヘッダクリックでソートできるcolumnModelをoverride
      @Override
      protected JTableHeader createDefaultTableHeader() {
        return new SampleHeader(super.columnModel);
      }

      // check flagがfalseの行は文字色を変える
      @Override
      public Component prepareRenderer(TableCellRenderer tcr, int row, int column) {
        Component c = super.prepareRenderer(tcr, row, column);
        String novelID = (String) getValueAt(row, 0);
        NovelMeta novelMeta = novelList.novelMetaMap.get(novelID);
        if (novelMeta != null && novelMeta.checkFlag != null) {
          if (novelMeta.checkFlag == false) {
            c.setForeground(Color.GRAY);
          } else {
            c.setForeground(getForeground());

            // 対象コンポーネントの表示フォントをボールドにする
            // if (mplus2mBoldFont != null) {
            // mplus2mBoldFont = mplus2mBoldFont.deriveFont(0, 13.0f);
            // c.setFont(mplus2mBoldFont);
            // } else {
            // Font font = table.getFont().deriveFont(Font.BOLD);
            // c.setFont(font);
            // }

          }
        }
        return c;
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    if (mplus2mMediumFont != null) {
      mplus2mMediumFont = mplus2mMediumFont.deriveFont(0, 13.0f);
      table.setFont(mplus2mMediumFont);
    }
    table.setSelectionForeground(Color.WHITE);
    table.setSelectionBackground(UIManager.getColor("EditorPane.selectionBackground"));

    // テーブルのコンテキストメニュー
    JPopupMenu tableContextMenu = new JPopupMenu();
    JMenuItem tglChkFlg = new JMenuItem("toggle update check flag");
    tglChkFlg.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int[] selection = table.getSelectedRows();
        for (int i = 0; i < selection.length; i++) {
          String novelID = (String) table.getValueAt(selection[i], 0);
          NovelMeta novelMeta = novelList.novelMetaMap.get(novelID);
          if (novelMeta.checkFlag) {
            novelMeta.checkFlag = false;
          } else {
            novelMeta.checkFlag = true;
          }
          novelList.novelMetaMap.put(novelID, novelMeta);
        }
      }
    });
    tableContextMenu.add(tglChkFlg);

    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.setComponentPopupMenu(tableContextMenu);
    sorter = new TableRowSorter<>(table.getModel());
    table.setRowSorter(sorter);
    table.setPreferredScrollableViewportSize(new Dimension(450, 250));
    table.setShowVerticalLines(false);
    table.setShowHorizontalLines(false);
    JScrollPane tableScrollPane = new JScrollPane(table);
    panel2.add(tableScrollPane);
    rootPanel.add(panel2);

    JPanel panel3 = new JPanel();
    panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
    panel3.add(Box.createRigidArea(new Dimension(10, 10)));
    textField = new JTextField();
    textField.setMaximumSize(new Dimension(2147483647, 26));
    panel3.add(textField);
    textField.setColumns(24);
    JButton btnAddEntry = new JButton("Add Entry");
    btnAddEntry.setActionCommand("add");
    panel3.add(btnAddEntry);
    btnAddEntry.setAction(actionAddEntry);
    rootPanel.add(panel3);

    JPanel panel4 = new JPanel();
    panel4.setLayout(new BorderLayout(0, 0));
    JTextArea jTextArea = new JTextArea();
    jTextArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(jTextArea);
    scrollPane.setPreferredSize(new Dimension(4, 100));
    panel4.add(scrollPane);
    rootPanel.add(panel4);

    // ログ出力先を設定
    LogAppender.setTextArea(jTextArea);

    // csvをロード
    if (!csvPath.isDirectory()) {
      csvPath.mkdirs();
      LogAppender.println("小説リストCSVファイルの保存ディレクトリを生成しました");
    } else {
      try {
        File csvFile = new File(csvPath.getCanonicalPath(), csvFileName);
        if (csvFile.isFile()) {
          novelList = new NovelList(csvFile);
          table.setModel(novelList.getTableModel());

          // 更新日時(カラムID=4)降順でソート実行
          sorter = new TableRowSorter<>(table.getModel());
          ArrayList sortKeys = new ArrayList<>();
          sortKeys.add(new RowSorter.SortKey(4, SortOrder.DESCENDING));
          sorter.setSortKeys(sortKeys);
          table.setRowSorter(sorter);

        } else {
          LogAppender.println("保存済み小説リストCSVファイルが見つかりません: " + csvFile.getCanonicalPath());
          LogAppender.println("小説リストCSVファイル保存パス: " + csvPath.getCanonicalPath());
        }
      } catch (IOException e1) {
        // TODO 自動生成された catch ブロック
        e1.printStackTrace();
      }

    }

  }

  /**
   * アプレット終了時の処理 設定ファイルを保存
   * Macではアプリケーションウィンドウのクローズボックスのクリックではなく、cmd-QでJavaVMを終了させると 実行されないので注意 -
   * 重要な情報の保存など、確実に実行させたい処理はここに書くべきではない
   */
  @Override
  protected void finalize() throws Throwable {
    this.convertCanceled = true;

    // Windowの位置とサイズをpropsに設定
    Point location = this.frame.getLocation();
    Dimension size = this.frame.getSize();
    this.props.setProperty("PosX", "" + location.getX());
    this.props.setProperty("PosY", "" + location.getY());
    this.props.setProperty("SizeW", "" + size.getWidth());
    this.props.setProperty("SizeH", "" + size.getHeight());

    // テーブルの列幅を取得してpropsに設定
    DefaultTableColumnModel defaultTableColumnModel = (DefaultTableColumnModel) this.table.getColumnModel();
    int numColumns = defaultTableColumnModel.getColumnCount();
    String propName;
    for (int idx = 0; idx < numColumns; idx++) {
      TableColumn tableColumn = defaultTableColumnModel.getColumn(idx);
      int width = tableColumn.getWidth();
      propName = "WidthColumn" + idx;
      this.props.setProperty(propName, "" + width);
    }

    // 使用中のLook & Feelを取得してpropsに設定
    String className = UIManager.getLookAndFeel().getName();
    props.setProperty("LastLook&Feel", className);

    // 設定ファイル更新
    FileOutputStream fos = new FileOutputStream(this.jarPath + this.propFileName);
    this.props.store(fos, "nucJ Parameters");
    fos.close();

    File csvFile = new File(csvPath.getCanonicalPath(), csvFileName);
    writeCSV(csvFile, novelList);

    super.finalize();
  }

  private Runnable execConvert() {
    // String[] args = { ".cache/ncode.syosetu.com/n9160bq/converted.txt" };
    // AozoraEpub3Applet.main(args);
    // AozoraEpub3.main(args);

    urlString = textField.getText();
    if (urlString.length() <= 0) {
      // LogAppender.println("textFieldが空です");
      return null;
    }

    try {
      webConverter = WebAozoraConverter.createWebAozoraConverter(urlString, webConfigPath);
      // LogAppender
      // .println("urlString: " + urlString + ", webConfigPath: " +
      // webConfigPath + " を引数としてwebConverterを生成した");
      if (webConverter == null) {
        LogAppender.append(urlString);
        LogAppender.println(" は変換できませんでした");
      }

      int interval = 500;
      int beforeChapter = 0;
      float modifiedExpire = 0;
      // キャッシュパス
      if (!cachePath.isDirectory()) {
        cachePath.mkdirs();
        LogAppender.println("キャッシュパスを作成します : " + cachePath.getCanonicalPath());
      }
      if (!cachePath.isDirectory()) {
        LogAppender.println("キャッシュパスが作成できませんでした");
        return null;
      }

      // File srcFile = webConverter.convertToAozoraText(urlString, cachePath,
      // interval, modifiedExpire,
      // this.jCheckWebConvertUpdated.isSelected(),
      // this.jCheckWebModifiedOnly.isSelected(),
      // jCheckWebModifiedTail.isSelected(), beforeChapter);
      File srcFile = webConverter.convertToAozoraText(urlString, cachePath, interval, modifiedExpire, false, false,
          false, beforeChapter);

      if (srcFile == null) {
        LogAppender.append(urlString);
        if (webConverter.isCanceled())
          LogAppender.println(" の変換をキャンセルしました");
        else
          LogAppender.println(" は変換できませんでした");
      } else {
        if (webConverter.isUpdated()) {

          String urlFilePath = CharUtils.escapeUrlToFile(urlString.substring(urlString.indexOf("//") + 2));
          String urlParentPath = urlFilePath;
          boolean isPath = false;
          if (urlFilePath.endsWith("/")) {
            isPath = true;
            urlFilePath += "index.html";
          } else
            urlParentPath = urlFilePath.substring(0, urlFilePath.lastIndexOf('/') + 1);

          // 変換結果
          String dstPath = cachePath.getAbsolutePath() + "/";
          if (isPath)
            dstPath += urlParentPath;
          else
            dstPath += urlFilePath + "_converted/";

          LogAppender.append(dstPath);
          LogAppender.println(" に キャッシュしました");
        }
      }
    } catch (IOException e1) {
      // TODO 自動生成された catch ブロック
      e1.printStackTrace();
    }
    return null;
  }

  /** 別スレッド実行用SwingWorker */
  class WebConvertWorker extends SwingWorker<Object, Object> {

    public WebConvertWorker() {

    }

    @Override
    protected Object doInBackground() throws Exception {
      execConvert();
      return null;
    }

  }

  // TextFieldに入力されたURLをリストに追加するアクション
  private class ActionAddEntry extends AbstractAction {
    public ActionAddEntry() {
      putValue(NAME, "Add Entry");
      // putValue(SHORT_DESCRIPTION, "check update for URL in textField");
    }

    public void actionPerformed(ActionEvent e) {
      // TODO 現状はコンバータに渡してるだけ リストのデータ周りが実装できたら書く
      WebConvertWorker webConvertWorker = new WebConvertWorker();
      webConvertWorker.execute();
    }
  }

  // テーブルに表示されたリストの内のチェック有効になってるエントリのアップデートを確認するアクション
  private class ActionChkUpdt extends AbstractAction {
    public ActionChkUpdt() {
      putValue(NAME, "Check Update");
      putValue(SHORT_DESCRIPTION, "check update for enabled URLs in List");
    }

    public void actionPerformed(ActionEvent e) {
      // TODO 現状は現在日時を取得してラベルを書き換えてるだけ リストのデータ周りが実装できたら書く

      for (String novelID : novelList.novelMetaMap.keySet()) {
        NovelMeta novelMeta = novelList.novelMetaMap.get(novelID);
        String urlString = novelMeta.url;
        LogAppender.println(novelID + ": " + urlString);
      }

      datePrevChecked = lastChkLbl.getText().substring(14);
      props.setProperty("DatePrevChecked", datePrevChecked);
      DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime ldtNow = LocalDateTime.now();
      dateLastChecked = dateTimeformatter.format(ldtNow);
      lastChkLbl.setText("Last Checked: " + dateLastChecked);
      props.setProperty("DateLastChecked", dateLastChecked);

    }
  }

  // ダブルクリックで列幅調節機能＋αを盛り込んだ改変版TableHeader
  // http://www.ne.jp/asahi/hishidama/home/tech/java/swing/JTable.html
  class SampleHeader extends JTableHeader {

    /** コンストラクター */
    public SampleHeader(TableColumnModel columnModel) {
      super(columnModel);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
      if (e.getID() == MouseEvent.MOUSE_CLICKED // クリックイベント
          && SwingUtilities.isLeftMouseButton(e)) { // 左クリック
        Cursor cur = super.getCursor();
        if (cur.getType() == Cursor.E_RESIZE_CURSOR) { // 矢印カーソル
          int cc = e.getClickCount();
          if (cc % 2 == 1) {
            // シングルクリック
            // ここでリターンしない場合、ソート機能が働いてしまう
            return;
          } else {
            // ダブルクリック
            Point pt = new Point(e.getX() - 3, e.getY()); // 列幅変更の場合、3ピクセルずらされて考慮されている
            int vc = super.columnAtPoint(pt);
            if (vc >= 0) {
              sizeWidthToFitData(vc);
              e.consume();
              return;
            }
          }
        }
      }
      super.processMouseEvent(e);
    }

    /**
     * データの幅に合わせる.
     *
     * @param vc
     *          表示列番号
     */
    public void sizeWidthToFitData(int vc) {
      JTable table = super.getTable();
      TableColumn tc = table.getColumnModel().getColumn(vc);

      int max = 0;

      int vrows = table.getRowCount(); // 表示行数
      for (int i = 0; i < vrows; i++) {
        TableCellRenderer r = table.getCellRenderer(i, vc); // レンダラー
        Object value = table.getValueAt(i, vc); // データ
        Component c = r.getTableCellRendererComponent(table, value, false, false, i, vc);
        int w = c.getPreferredSize().width; // データ毎の幅
        if (max < w) {
          max = w;
        }
      }

      tc.setPreferredWidth(max + 1); // +1してやらないと、省略表示になってしまう
    }
  }

  // NovelListの内容をCSVファイルに書き出す
  private void writeCSV(File csvFile, NovelList novelList) throws IOException {
    // MacOSなら平気なのだがWindowsではopenCSVのCSVWriterをそのまま使うと文字化けが起きるので、
    // java.io.OutputStreamWriterを挟み、ここで文字コードを指定することで文字化けを回避している
    CSVWriter csvWriter;
    FileOutputStream fileOutputStream = new FileOutputStream(csvFile);
    Writer writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
    csvWriter = new CSVWriter(writer, ',', '"', "\n");
    List<String[]> outStrList = new ArrayList<>();
    String[] header = { "novel ID", "author", "title", "chapters", "last updated", "check flag", "URL", "author ID" };
    outStrList.add(header);
    for (String novelID : novelList.novelMetaMap.keySet()) {
      NovelMeta novelMeta = novelList.novelMetaMap.get(novelID);
      String[] row = { novelMeta.novelID, novelMeta.author, novelMeta.title, novelMeta.numSections.toString(),
          novelMeta.lastUpdate, novelMeta.checkFlag.toString(), novelMeta.url, novelMeta.authorID };
      outStrList.add(row);
    }
    csvWriter.writeAll(outStrList);
    csvWriter.close();
  }

}
