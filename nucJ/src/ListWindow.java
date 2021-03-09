import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
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
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import com.github.ghmk5.info.NovelList;
import com.github.ghmk5.info.NovelMeta;
import com.github.ghmk5.info.Properties;
import com.github.ghmk5.swing.DialogConverterSettings;
import com.github.ghmk5.swing.DialogCopyFile;
import com.github.ghmk5.swing.DialogListFilesToOpen;
import com.github.ghmk5.txt.AozoraTxt;
import com.github.ghmk5.util.Utils;
import com.github.hmdev.converter.AozoraEpub3Converter;
import com.github.hmdev.image.ImageInfoReader;
import com.github.hmdev.info.BookInfo;
import com.github.hmdev.info.BookInfoHistory;
import com.github.hmdev.info.ProfileInfo;
import com.github.hmdev.swing.JConfirmDialog;
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

  /** 変換前確認ダイアログ */
  JConfirmDialog jConfirmDialog;

  /** 変換設定ダイアログ */
  DialogConverterSettings dialogConverterSettings;

  DialogCopyFile dialogCopyFile;

  /** アプリケーションのアイコン画像 */
  Image iconImage;

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

  /** グローバル値のプロパティセット */
  public Properties props;

  /** 作品個別値のプロパティセット */
  public Properties individualProps;

  /** グローバル値のプロパティファイル名 */
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
  JButton btnChkUpdt;
  JTable table;
  JProgressBar progressBar;

  DefaultTableModel defaultTableModel;
  String urlString;
  private JTextField urlTextField;
  NovelList novelList;
  File csvPath;
  File csvFile;
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

  FileOutputStream fos;

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
            // LogAppender.println("ウィンドウ位置の復元に失敗した");
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
            DefaultTableColumnModel defaultTableColumnModel =
                (DefaultTableColumnModel) window.table.getColumnModel();
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
            // LogAppender.println("テーブルカラム幅の復元に失敗した");
          }

          // Look & Feelの復元
          String lafName = lafMap.get(window.props.getProperty("LastLook&Feel"));
          try {
            UIManager.setLookAndFeel(lafName);
            // ここを通るとテーブル選択強調色がデフォルトに戻るので以下一行を挟んである。L&Fの選択によっては余分かもしれない
            // なお、なぜかNumbusでは効かないのみならず、一度Numbusを選ぶと他のL&Fでも効かなくなる模様
            UIManager.put("Table.selectionBackground",
                UIManager.getColor("EditorPane.selectionBackground"));
            SwingUtilities.updateComponentTreeUI(window.frame);
          } catch (Exception e) {
            // LogAppender.println("Look & Feelを " + lafName + " に設定できなかった");
          }

          // 現在選択されているLook & Feelに対応するメニュー内ボタンモデルを選択する処理
          String className = UIManager.getLookAndFeel().getName();
          for (Enumeration<AbstractButton> e = window.mntmLafSubGroup.getElements(); e
              .hasMoreElements();) {
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

    // DefaultTableModelを定義
    // このまま使用されるのはiniファイルから読み込めず、かつデフォルトiniからも読み込めなかった場合のみ
    DefaultTableModel defaultTableModel;
    String[] columnNames = {"novel ID", "author", "title", "chapters", "last updated"};
    defaultTableModel = new DefaultTableModel(columnNames, 0);

    // 使用可能なLook & Feelを取得
    LookAndFeelInfo[] lafis = UIManager.getInstalledLookAndFeels();
    String[] sLafis = new String[lafis.length];
    lafMap = new HashMap<>();
    for (int i = 0; i < lafis.length; i++) {
      sLafis[i] = lafis[i].getName();
      lafMap.put(sLafis[i], lafis[i].getClassName());
    }

    // パスを設定
    this.jarPath = "";
    this.cachePath = new File(this.jarPath + "cache");
    this.webConfigPath = new File(this.jarPath + "web");
    this.profilePath = new File(this.jarPath + "profiles");
    this.profilePath.mkdir();
    this.csvPath = new File(this.jarPath + "csv");
    // キャッシュパスだけは設定変更できるのでイニシャライザの最後で設定する

    // 設定ファイル読み込み
    props = new Properties();
    try {
      FileInputStream fis = new FileInputStream(this.jarPath + this.propFileName);
      props.load(fis);
      fis.close();
    } catch (Exception e) {
      InputStream iStream = this.getClass().getResourceAsStream("defaults/nucJ_default.ini");
      System.out.println("初期設定ファイル ./nucJ_default.ini がロードできません。デフォルト値のロードを試みます");
      try {
        props.load(iStream);
        iStream.close();
        System.out.println("デフォルト設定 ./src/defaults/nucJ_default.ini をロードしました");
      } catch (IOException e1) {
        System.out.println("デフォルト設定ファイル defaults/nucJ_default.ini のロードに失敗した");
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
      System.out.println("jar同梱フォントファイルが不正です");
      e2.printStackTrace();
    } catch (IOException e2) {
      System.out.println("jar同梱フォントファイルが読み込めません");
      e2.printStackTrace();
    }

    // 確認ダイアログ
    jConfirmDialog = new JConfirmDialog(iconImage, this.getClass().getResource("/images/icon.png")
        .toString().replaceFirst("/icon\\.png", "/"));
    if ("1".equals(props.getProperty("ReplaceCover")))
      jConfirmDialog.jCheckReplaceCover.setSelected(true);

    // 本体フレーム
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
    // LogAppender.println("csvファイルの読み込みに失敗した");
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
    menuOpenPrefDialog.addActionListener(new ActionOpenDialogSettingsGlobal());
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
            // どうやら一度Numbusを選ぶと以下二行が効かなくなるらしい。なにが起きているのか不明
            UIManager.put("Table.selectionBackground",
                UIManager.getColor("EditorPane.selectionBackground"));
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

    // 前回日時表示ラベル & 更新チェック実行ボタン
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
    btnChkUpdt = new JButton("Check Update");
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

      // getToolTipText()をオーバーライド
      // マウスポインタが上にあるセルのtooltipにそのセルの内容を設定する
      // TODO 内容がカラム幅を超えるときのみtooltip化するのは可能か？

      // ↓(http://java409.web.fc2.com/jtable_tool_tip.htm)だとソートするとダメになる
      // @Override
      // public String getToolTipText(MouseEvent e) {
      // // イベントからマウス位置を取得し、テーブル内のセルを割り出す
      // return (String) getModel().getValueAt(rowAtPoint(e.getPoint()),
      // columnAtPoint(e.getPoint()));
      // }

      // ↓https://stackoverflow.com/questions/9467093/how-to-add-a-tooltip-to-a-cell-in-a-jtable
      // 上と同じことをやってるはずだが、こちらだとうまくいってるように見える
      // 何が違うのかわからない
      @Override
      public String getToolTipText(MouseEvent e) {
        String tip = null;
        java.awt.Point p = e.getPoint();
        // int rowIndex = getRowSorter().convertRowIndexToModel(rowAtPoint(p));
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        // LogAppender.append("(" + rowIndex + ", " + colIndex + ")\n");

        try {
          tip = getValueAt(rowIndex, colIndex).toString();
        } catch (RuntimeException e1) {
          // catch null pointer exception if mouse is over an empty line
        }

        return tip;
      }

    };

    // テーブルで使用するフォントを設定
    if (mplus2mMediumFont != null) {
      mplus2mMediumFont = mplus2mMediumFont.deriveFont(0, 13.0f);
      table.setFont(mplus2mMediumFont);
    }
    table.setSelectionForeground(Color.WHITE);
    table.setSelectionBackground(UIManager.getColor("EditorPane.selectionBackground"));

    // テーブル選択行ダブルクリック時の処理
    table.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2 && table.getSelectedRows().length == 1) {
          int idx = table.getSelectedRow();
          idx = table.convertRowIndexToModel(idx);
          String novelID = (String) table.getModel().getValueAt(idx, 0);
          String urlString = novelList.novelMetaMap.get(novelID).url;
          String individualCachePath =
              Utils.getNovelWiseDstPath(urlString, cachePath.getAbsolutePath());
          String doOnDc = props.getProperty("DoOnDc");
          if (doOnDc != null) {
            switch (doOnDc) {
              case "OpenViewer":
                // TODO ビューワで開く
                break;
              case "OpenHostedBibi":
                // TODO localhostのBib/iで開く
                File listFile = Paths.get(individualCachePath, "EpubFiles.txt").toFile();
                if (!listFile.exists()) {
                  LogAppender.println("この作品のEPUBリストが見つかりません(変換されたことがない？)");
                  break;
                }
                DialogListFilesToOpen dialogListFilesToOpen;
                try {
                  dialogListFilesToOpen = new DialogListFilesToOpen(frame, listFile);
                  dialogListFilesToOpen.setModal(true);
                  dialogListFilesToOpen.setLocationRelativeTo(frame);
                  dialogListFilesToOpen.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                      String fileName = dialogListFilesToOpen.selectedLine;
                      String urlString = props.getProperty("UrlHostedBibi");
                      urlString += "?book=";
                      if (props.getPropertiesAsBoolean("UseNovelwiseDirEPUB3"))
                        urlString = urlString + novelID + "/";
                      URLCodec codec = new URLCodec("UTF-8");
                      try {
                        fileName = codec.encode(fileName);
                        fileName = fileName.replaceAll("\\+", "%20");
                        urlString += fileName;
                        URI uri;
                        uri = new URI(urlString);
                        Desktop.getDesktop().browse(uri);
                      } catch (EncoderException e1) {
                        LogAppender.append(fileName);
                        LogAppender.append(" のURLエンコードでエラーが発生しました\n");
                        e1.printStackTrace();
                      } catch (URISyntaxException e2) {
                        LogAppender.append(urlString);
                        LogAppender.append(" はURIとして解釈できません\n");
                        e2.printStackTrace();
                      } catch (IOException e3) {
                        LogAppender.append(urlString);
                        LogAppender.append(" はURIとして開けません\n");
                        e3.printStackTrace();
                      }
                    }
                  });
                  dialogListFilesToOpen.setVisible(true);
                } catch (IOException e) {
                  LogAppender.append(listFile.toString());
                  LogAppender.append(" を開くことができません/n");
                  e.printStackTrace();
                }
                break;
              case "OpenLocalBibi":
                // TODO localfileのBib/iで開く
                break;
              case "OpenFilerEPUB3": // OS標準のファイラーでEPUB保存ディレクトリを開く
                String epubSavePath = props.getProperty("EPUB3DstPath");
                if (epubSavePath != null) {
                  if (props.getPropertiesAsBoolean("UseNovelwiseDirEPUB3"))
                    epubSavePath = Paths.get(epubSavePath, novelID).toString();
                  try {
                    Desktop.getDesktop().open(new File(epubSavePath));
                  } catch (IOException e) {
                    LogAppender.append(epubSavePath);
                    LogAppender.append(" は開けません\n");
                    e.printStackTrace();
                  }
                } else {
                  LogAppender.println("EPUBファイルの保存先が記録されていません");
                }
                break;
              case "OpenFilerAozora":
                String viewerSavePath = props.getProperty("ViewerDstPath");
                if (viewerSavePath != null) {
                  if (props.getPropertiesAsBoolean("UseNovelwiseDirViewer"))
                    viewerSavePath = Paths.get(viewerSavePath, novelID).toString();
                  try {
                    Desktop.getDesktop().open(new File(viewerSavePath));
                  } catch (IOException e) {
                    LogAppender.println(viewerSavePath + " は開けません");
                    e.printStackTrace();
                  }
                } else {
                  LogAppender.println("青空文庫テキストの保存先が記録されていません");
                }
                break;
            }
          }

        }
      }
    });
    // テーブルのコンテキストメニュー
    JPopupMenu tableContextMenu = new JPopupMenu();

    JMenuItem mntmOpenWeb = new JMenuItem("作品Webページを開く");
    mntmOpenWeb.addActionListener(new ActionOpenWeb());
    tableContextMenu.add(mntmOpenWeb);

    JMenuItem mntmOpenCache = new JMenuItem("キャッシュディレクトリを開く");
    mntmOpenCache.addActionListener(new ActionOpenIndividualCache());
    tableContextMenu.add(mntmOpenCache);

    JMenuItem mntmCopyFile = new JMenuItem("EPUB/青空文庫TXTファイルをコピー");
    mntmCopyFile.addActionListener(new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {
        dialogCopyFile = new DialogCopyFile(frame, "EPUB/青空文庫TXTファイル コピー先選択", true, props);
        dialogCopyFile.setVisible(true);

        if (dialogCopyFile.accepted) {
          int[] selection = table.getSelectedRows();
          ArrayList<File> filesToCopy = new ArrayList<File>();
          String novelID;
          Path dstDirAoTxt = Path.of(dialogCopyFile.copyDstAoTxt);
          Path dstDirEpub3 = Path.of(dialogCopyFile.copyDstEpub3);
          Path epub3StoreDir = Path.of(props.getProperty("EPUB3DstPath"));
          Path aoTxtStoreDir = Path.of(props.getProperty("ViewerDstPath"));
          Path indStoreDir;
          HashMap<Path, ArrayList<File>> fileMap = new HashMap<Path, ArrayList<File>>();
          ArrayList<File> aoTxtFiles = new ArrayList<File>();
          ArrayList<File> epubFiles = new ArrayList<File>();
          for (int i : selection) {
            novelID = (String) table.getValueAt(selection[i], 0);
            if (dialogCopyFile.cbAoTxt.isSelected()) {
              if (props.getPropertiesAsBoolean("UseNovelwiseDirViewer")) {
                indStoreDir = Paths.get(aoTxtStoreDir.toString(), novelID);
              } else {
                indStoreDir = aoTxtStoreDir;
              }
              for (File file : indStoreDir.toFile().listFiles()) {
                if (file.getName().matches(".+\\.txt$")) {
                  aoTxtFiles.add(file);
                }
              }
              fileMap.put(dstDirAoTxt, aoTxtFiles);
            }
            if (dialogCopyFile.cbEpubTxt.isSelected()) {
              if (props.getPropertiesAsBoolean("UseNovelwiseDirEPUB3")) {
                indStoreDir = Paths.get(epub3StoreDir.toString(), novelID);
              } else {
                indStoreDir = epub3StoreDir;
              }
              for (File file : indStoreDir.toFile().listFiles()) {
                if (file.getName().matches(".+\\.epub$")) {
                  epubFiles.add(file);
                }
              }
              if (fileMap.keySet().contains(dstDirEpub3)) {
                aoTxtFiles = fileMap.get(dstDirEpub3);
                aoTxtFiles.addAll(epubFiles);
                fileMap.put(dstDirEpub3, aoTxtFiles);
              } else {
                fileMap.put(dstDirEpub3, epubFiles);
              }
            }
          }

          ArrayList<File> filesCouldntCopied = new ArrayList<File>();
          for (Path dstPath : fileMap.keySet()) {
            for (File file : fileMap.get(dstPath)) {
              try {
                Files.copy(file.toPath(), Paths.get(dstPath.toString(), file.getName()),
                    StandardCopyOption.REPLACE_EXISTING);
                LogAppender.println(file.getName() + " をコピーしました");
              } catch (Exception ioe) {
                ioe.printStackTrace();
                filesCouldntCopied.add(file);
              }
            }
            if (filesCouldntCopied.size() > 0) {
              LogAppender.println("以下のファイルはコピーできません");
              for (File file : filesCouldntCopied) {
                LogAppender.println("  " + file.getAbsolutePath());
              }
            } else {
              if (dialogCopyFile.cbAoTxt.isSelected()) {
                props.setProperty("CopyDstAoTxt", dstDirAoTxt.toString());
              }
              if (dialogCopyFile.cbEpubTxt.isSelected()) {
                props.setProperty("CopyDstEpub3", dstDirEpub3.toString());
              }
              try {
                // 設定ファイル更新
                fos = new FileOutputStream(jarPath + propFileName);
                props.store(fos, "nucJ Parameters");
                fos.close();
              } catch (Exception e1) {
                LogAppender.println("設定ファイル " + jarPath + propFileName + " の更新に失敗しました");
                e1.printStackTrace();
              }
            }
          }

        }
      }
    });
    tableContextMenu.add(mntmCopyFile);

    JMenuItem tglChkFlg = new JMenuItem("更新チェック可否の切り替え(トグル動作)");
    tglChkFlg.addActionListener(new ActionToggleChkUpdt());
    tableContextMenu.add(tglChkFlg);

    JMenuItem openDialogConverterSettings = new JMenuItem("作品別変換設定");
    openDialogConverterSettings.addActionListener(new ActionSetIndividualProps());
    tableContextMenu.add(openDialogConverterSettings);

    JMenuItem mntmReConvert = new JMenuItem("EPUB3/ビューワ閲覧用ファイルの再生成");
    mntmReConvert.addActionListener(new ActionReConvert());
    tableContextMenu.add(mntmReConvert);

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
    urlTextField = new JTextField();
    urlTextField.setMaximumSize(new Dimension(2147483647, 26));
    panel3.add(urlTextField);
    urlTextField.setColumns(24);
    JButton btnAddEntry = new JButton("Add Entry");
    btnAddEntry.setActionCommand("add");
    panel3.add(btnAddEntry);
    btnAddEntry.setAction(actionAddEntry);
    rootPanel.add(panel3);

    JPanel panel4 = new JPanel();
    panel4.setLayout(new BorderLayout(0, 0));
    JTextArea logTextArea = new JTextArea();
    logTextArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(logTextArea);
    scrollPane.setPreferredSize(new Dimension(4, 100));
    panel4.add(scrollPane);
    rootPanel.add(panel4);

    JPanel panel5 = new JPanel();
    rootPanel.add(panel5);
    panel5.setLayout(new BoxLayout(panel5, BoxLayout.Y_AXIS));
    progressBar = new JProgressBar();
    panel5.add(progressBar);

    // ログ出力先を設定
    LogAppender.setTextArea(logTextArea);

    // ファイルコンバーター・ライター初期化
    try {
      // ePub出力クラス初期化
      this.epub3Writer = new Epub3Writer(this.jarPath + "template/");
      // ePub画像出力クラス初期化
      this.epub3ImageWriter = new Epub3ImageWriter(this.jarPath + "template/");

      // 変換テーブルをstaticに生成
      this.aozoraConverter = new AozoraEpub3Converter(this.epub3Writer, this.jarPath);

    } catch (IOException e) {
      e.printStackTrace();
      logTextArea.append(e.getMessage());
    }

    // プロパティからのCSVパスの取り出しを試みる
    String csvPathString = props.getProperty("CSVPath");
    if (csvPathString != null && !csvPathString.equals("")) {
      csvPath = new File(csvPathString);
    } // propsから取り出せなければデフォルト値が使用される

    // csvをロード
    if (!csvPath.isDirectory()) {
      csvPath.mkdirs();
      LogAppender.println("小説リストCSVファイルの保存ディレクトリを生成しました");
    } else {
      try {
        csvFile = new File(csvPath.getCanonicalPath(), csvFileName);
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
          // 起動時にCSVファイルが無かったときの処理
          LogAppender.println("保存済み小説リストCSVファイルが見つかりません: " + csvFile.getCanonicalPath());
          LogAppender.println("小説リストCSVファイル保存パス: " + csvPath.getCanonicalPath());
          LogAppender.println("新しく作成します");
          fos = new FileOutputStream(csvFile);
          OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
          BufferedWriter bw = new BufferedWriter(osw);
          bw.write(
              "\"novel ID\",\"author\",\"title\",\"chapters\",\"last updated\",\"check flag\",\"URL\",\"author ID\"\n");
          bw.close();
        }
      } catch (IOException e1) {
        LogAppender.println("保存済み小説リストCSVファイルの読み込みに失敗しました");
        e1.printStackTrace();
      }

    }

    // プロパティからのキャッシュパスの取り出しを試みる
    String cachePathString = props.getProperty("CachePath");
    if (new File(cachePathString).exists()) {
      if (new File(cachePathString).isDirectory()) {
        this.cachePath = new File(cachePathString);
      } else {
        LogAppender.println(
            "設定ファイルに記録されたキャッシュパスの名前 " + cachePathString + " は既に他のファイルに使われています。デフォルト値をセットします");
        this.cachePath = new File(this.jarPath + "cache");
      }
    } else {
      LogAppender.println("設定ファイルに記録されたキャッシュパス " + cachePathString + " は存在しません");
      this.cachePath = new File(cachePathString);
      try {
        this.cachePath.mkdirs();
      } catch (Exception e) {
        LogAppender.println("キャッシュパス " + cachePathString + " を作成できません。デフォルト値をセットします");
        this.cachePath = new File(this.jarPath + "cache");
      }
    }

  }

  /**
   * 終了時の処理 設定ファイルを保存 Macではアプリケーションウィンドウのクローズボックスのクリックではなく、cmd-QでJavaVMを終了させると 実行されないので注意 -
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
    DefaultTableColumnModel defaultTableColumnModel =
        (DefaultTableColumnModel) this.table.getColumnModel();
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
    fos = new FileOutputStream(this.jarPath + this.propFileName);
    this.props.store(fos, "nucJ Parameters");
    fos.close();

    csvFile = new File(csvPath.getCanonicalPath(), csvFileName);
    if (novelList != null)
      writeCSV(csvFile, novelList);

    super.finalize();
  }

  /**
   * Webアクセス-青空文庫テキストへの変換-分割-EPUB3ファイルへの変換-CSVファイルとテーブルの更新 別スレッド実行用SwingWorker
   */
  class WebConvertWorker extends SwingWorker<Object, Object> {

    Boolean flgCheckMultiple;

    // リストの更新チェック用コンストラクタ
    public WebConvertWorker(NovelList novelList, File csvFile) {
      flgCheckMultiple = true;
    }

    // 新規エントリ追加用のコンストラクタ
    public WebConvertWorker(NovelList novelList, File csvFile, String urlString) {
      flgCheckMultiple = false;
    }

    // スレッドで実行する処理
    @Override
    protected Object doInBackground() throws Exception {
      btnChkUpdt.setEnabled(false);
      NovelMeta novelMeta;
      if (flgCheckMultiple) {
        String urlString;
        String title;
        int i = 0;
        int j = novelList.novelMetaMap.keySet().size();
        for (String novelID : novelList.novelMetaMap.keySet()) {
          novelMeta = novelList.novelMetaMap.get(novelID);
          if (novelMeta.checkFlag) {
            urlString = novelMeta.url;
            title = novelMeta.title;
            LogAppender.println("「" + title + "」の処理を開始します");
            execWebConvert(urlString);
            LogAppender.println("「" + title + "」の処理が終わりました");
            LogAppender.println("====");
            LogAppender.println("");
          }
          // SwingWorkerインスタンスのプロパティprogressに値をセットする
          // SwingWorkerインスタンスにPropertyChangeListenerをセットすることで、値の変更を監視し、変更が検出されると読み出される
          i++;
          setProgress((i * 100) / j);
        }
      } else {
        execWebConvert(urlString);
      }
      progressBar.setValue(100);
      wait(2000);
      return null;
    }

    // 途中経過
    @Override
    protected void process(List<Object> chunks) {}

    // doInBackgroundで記述したスレッドが終了したら実行する処理
    @Override
    protected void done() {
      // 更新後の状態をCSVファイルから読み込んでnovelListとテーブルを更新
      btnChkUpdt.setEnabled(true);
      try {
        novelList = new NovelList(csvFile);
        defaultTableModel = novelList.getTableModel();
        table.setModel(defaultTableModel);
      } catch (IOException e) {
        LogAppender.println("変換後に更新されたはずのCSVファイルの読み込みに失敗した");
      }

      // テーブルカラム幅の復元
      DefaultTableColumnModel defaultTableColumnModel =
          (DefaultTableColumnModel) table.getColumnModel();
      int numCulmns = defaultTableColumnModel.getColumnCount();
      TableColumn column = null;
      int savedwidth;
      for (int idx = 0; idx < numCulmns; idx++) {
        if (props.getProperty("WidthColumn" + idx) != null) {
          savedwidth = Integer.parseInt(props.getProperty("WidthColumn" + idx));
          column = defaultTableColumnModel.getColumn(idx);
          column.setPreferredWidth(savedwidth);
        }
      }
      // 更新日時(カラムID=4)降順でソート実行
      sorter = new TableRowSorter<>(table.getModel());
      ArrayList sortKeys = new ArrayList<>();
      sortKeys.add(new RowSorter.SortKey(4, SortOrder.DESCENDING));
      sorter.setSortKeys(sortKeys);
      table.setRowSorter(sorter);

      // 作品個別の設定をセットした(かもしれない)propsをグローバル値に戻す
      // execWebConvertの最後でも実行しているので必要ないはずだが、念のために残しておく
      FileInputStream fis;
      try {
        fis = new FileInputStream(jarPath + propFileName);
        props.load(fis);
        fis.close();
      } catch (IOException e) {
        LogAppender.println("Propertiesへのグローバル値のロードに失敗しました");
        e.printStackTrace();
      }

      datePrevChecked = lastChkLbl.getText().substring(14);
      props.setProperty("DatePrevChecked", datePrevChecked);
      DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime ldtNow = LocalDateTime.now();
      dateLastChecked = dateTimeformatter.format(ldtNow);
      lastChkLbl.setText("Last Checked: " + dateLastChecked);
      props.setProperty("DateLastChecked", dateLastChecked);

      // 設定ファイル更新
      try {
        fos = new FileOutputStream(jarPath + propFileName);
        props.store(fos, "nucJ Parameters");
        fos.close();
      } catch (IOException e) {
        LogAppender.println("Propertiesの" + jarPath + propFileName + "への保存に失敗しました");
        e.printStackTrace();
      }

      LogAppender.println("処理が終了しました: " + dateLastChecked);

      progressBar.setValue(0);

    }

  }

  /** 青空文庫テキストの分割-EPUB3ファイルへの変換 別スレッド実行用SwingWorker */
  class ReConvertWorker extends SwingWorker<Object, Object> {

    ArrayList<String> listOfnovelIDs;

    // コンストラクタ
    public ReConvertWorker(ArrayList<String> listOfnovelIDs) {
      this.listOfnovelIDs = listOfnovelIDs;
    }

    // スレッドで実行する処理
    @Override
    protected Object doInBackground() throws Exception {
      int i = 0;
      int j = this.listOfnovelIDs.size();
      for (String novelID : this.listOfnovelIDs) {
        execReConvert(novelID);
        // SwingWorkerインスタンスのプロパティprogressに値をセットする
        // SwingWorkerインスタンスにPropertyChangeListenerをセットすることで、値の変更を監視し、変更が検出されると読み出される
        i++;
        setProgress((i * 100) / j);
      }
      progressBar.setValue(100);
      wait(2000);
      return null;
    }

    // 途中経過
    @Override
    protected void process(List<Object> chunks) {}

    // doInBackgroundで記述したスレッドが終了したら実行する処理
    @Override
    protected void done() {
      // 作品個別の設定をセットした(かもしれない)propsをグローバル値に戻す
      // execReConvertの最後でも実行しているので必要ないはずだが、念のために残しておく
      FileInputStream fis;
      try {
        fis = new FileInputStream(jarPath + propFileName);
        props.load(fis);
        fis.close();
      } catch (IOException e) {
        LogAppender.println("Propertiesへのグローバル値のロードに失敗しました");
        e.printStackTrace();
      }
      progressBar.setValue(0);

    }

  }

  // TextFieldに入力されたURLをリストに追加するアクション
  private class ActionAddEntry extends AbstractAction {
    public ActionAddEntry() {
      putValue(NAME, "Add Entry");
      putValue(SHORT_DESCRIPTION, "テキストフィールドのURLに目次ページを持つ作品をリストに加えます");
    }

    public void actionPerformed(ActionEvent e) {
      // TODO 既にリストにあるURLを入れた場合への対応
      // TODO URLじゃない文字列を入れたときの対応
      urlString = urlTextField.getText();
      WebConvertWorker webConvertWorker = new WebConvertWorker(novelList, csvFile, urlString);
      webConvertWorker.execute();
      // CSVには未登録だがキャッシュは既に存在する&&更新なしと判断される状態で新規登録しようとすると、
      // novelListとCSVファイルを更新する前に処理が止まるので追加されないが、対処すべきか迷う
    }
  }

  // テーブルに表示されたリストの内のチェック有効になってるエントリのアップデートを確認するアクション
  private class ActionChkUpdt extends AbstractAction {
    public ActionChkUpdt() {
      putValue(NAME, "Check Update");
      putValue(SHORT_DESCRIPTION, "小説の更新をチェックします");
    }

    public void actionPerformed(ActionEvent e) {

      WebConvertWorker webConvertWorker = new WebConvertWorker(novelList, csvFile);

      // プログレスバーの処理
      webConvertWorker.addPropertyChangeListener(new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          if ("progress".equals(evt.getPropertyName())) {
            progressBar.setValue((Integer) evt.getNewValue());
          }
        }
      });

      webConvertWorker.execute();

    }
  }

  // メニュー "File" のメニューアイテム "設定..." のアクション 設定ダイアログを開く
  private class ActionOpenDialogSettingsGlobal extends AbstractAction {
    public void actionPerformed(ActionEvent e) {
      try {
        FileInputStream fis = new FileInputStream(jarPath + propFileName);
        props.load(fis);
        fis.close();
      } catch (Exception e1) {
        InputStream iStream = this.getClass().getResourceAsStream("defaults/nucJ_default.ini");
        System.out.println("初期設定ファイル ./nucJ_default.ini がロードできません。デフォルト値のロードを試みます");
        try {
          props.load(iStream);
          iStream.close();
          System.out.println("デフォルト設定 ./src/defaults/nucJ_default.ini をロードしました");
        } catch (IOException e11) {
          System.out.println("デフォルト設定ファイル defaults/nucJ_default.ini のロードに失敗した");
          e11.printStackTrace();
        }
      }
      dialogConverterSettings = new DialogConverterSettings(frame, props, "global");
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
        fos = new FileOutputStream(jarPath + propFileName);
        props.store(fos, "nucJ Parameters");
        fos.close();
      } catch (Exception e1) {
        LogAppender.println("設定ファイル " + jarPath + propFileName + " の更新に失敗しました");
        e1.printStackTrace();
      }
    }
  }

  // テーブルコンテキストメニューのメニューアイテム用アクション 作品Webページを開く
  private class ActionOpenWeb extends AbstractAction {
    public void actionPerformed(ActionEvent e) {
      int[] selection = table.getSelectedRows();
      for (int i = 0; i < selection.length; i++) {
        String novelID = (String) table.getValueAt(selection[i], 0);
        NovelMeta novelMeta = novelList.novelMetaMap.get(novelID);
        String urlString = novelMeta.url;
        try {
          URI uri = new URI(urlString);
          Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException | IOException e1) {
          LogAppender.append(urlString);
          LogAppender.append(" は URLとして開けません\n");
          e1.printStackTrace();
        }
      }
    }
  }

  // テーブルコンテキストメニューのメニューアイテム用アクション リストアイテムの更新チェック可否切り替え
  private class ActionToggleChkUpdt extends AbstractAction {

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
  }

  // テーブルコンテキストメニューのメニューアイテム用アクション 作品個別変換設定
  private class ActionSetIndividualProps extends AbstractAction {
    public void actionPerformed(ActionEvent e) {

      String title;
      int numEntriesSelected = table.getSelectedRowCount();
      String individualPropsFilePath;

      // 選択された作品のIDを取得
      ArrayList<String> listNovelIDsToBePropped = new ArrayList<>();
      if (numEntriesSelected > 1) {
        title = "multiple";
        int[] selection = table.getSelectedRows();
        for (int idx : selection) {
          idx = table.convertRowIndexToModel(idx);
          listNovelIDsToBePropped.add((String) table.getModel().getValueAt(idx, 0));
        }
      } else if (numEntriesSelected == 1) {
        int idx = table.getSelectedRow();
        idx = table.convertRowIndexToModel(idx);
        title = (String) table.getModel().getValueAt(idx, 2);
        listNovelIDsToBePropped.add((String) table.getModel().getValueAt(idx, 0));
      } else {
        return;
      }

      // 既存設定の読み込み
      individualProps = new Properties();
      for (String novelID : listNovelIDsToBePropped) {
        String urlString = novelList.novelMetaMap.get(novelID).url;
        String cachePathString = props.getProperty("CachePath");
        individualPropsFilePath = Utils.getNovelWiseDstPath(urlString, cachePathString);
        try {
          FileInputStream fis = new FileInputStream(individualPropsFilePath + "convert.ini");
          individualProps.load(fis);
          fis.close();
          if (listNovelIDsToBePropped.size() > 1) {
            LogAppender.println("複数作品に対する変換設定のテンプレートとして、「"
                + novelList.novelMetaMap.get(novelID).title + "」の値を表示します");
          } else {
            LogAppender.println("選択された作品の保存済み個別設定を表示します");
          }
          break;
        } catch (Exception e1) {
          LogAppender.println("選択された作品の保存済み個別設定が見つかりません。グローバル値を表示します");
          individualProps = props;
        }
      }

      dialogConverterSettings = new DialogConverterSettings(frame, individualProps, title);
      dialogConverterSettings.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
          LogAppender.println(dialogConverterSettings.testString);
          if (dialogConverterSettings.approved) {
            individualProps = dialogConverterSettings.props;
            for (String novelID : listNovelIDsToBePropped) {
              String urlString = novelList.novelMetaMap.get(novelID).url;
              urlString = novelList.novelMetaMap.get(novelID).url;
              String individualPropsFilePath =
                  Utils.getNovelWiseDstPath(urlString, props.getProperty("CachePath"));
              try {
                // 設定ファイル更新
                File propFile = new File(individualPropsFilePath + "convert.ini");
                LogAppender.println(propFile.getAbsolutePath());
                fos = new FileOutputStream(propFile);
                individualProps.store(fos, "converter Parameters");
                fos.close();
              } catch (Exception e1) {
                LogAppender.println(novelID + " の個別設定値の保存でエラーが発生しました。設定の変更は保存されていません");
                e1.printStackTrace();
              }
            }
          }
        }
      });
      dialogConverterSettings.setLocationRelativeTo(frame);
      dialogConverterSettings.setModal(true);
      dialogConverterSettings.setVisible(true);
    }
  }

  // テーブルコンテキストメニューのメニューアイテム用アクション EPUB3/ビューワ閲覧用ファイルの再生成
  private class ActionReConvert extends AbstractAction {
    public void actionPerformed(ActionEvent e) {
      // 選択された作品のIDを取得
      ArrayList<String> listNovelIDsToBeTreated = new ArrayList<>();
      for (int idx : table.getSelectedRows()) {
        idx = table.convertRowIndexToModel(idx);
        listNovelIDsToBeTreated.add((String) table.getModel().getValueAt(idx, 0));
      }
      ReConvertWorker reConvertWorker = new ReConvertWorker(listNovelIDsToBeTreated);
      // プログレスバーの処理
      reConvertWorker.addPropertyChangeListener(new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          if ("progress".equals(evt.getPropertyName())) {
            progressBar.setValue((Integer) evt.getNewValue());
          }
        }
      });
      reConvertWorker.execute();
    }
  }

  // テーブルコンテキストメニューのメニューアイテム用アクション キャッシュディレクトリを開く
  private class ActionOpenIndividualCache extends AbstractAction {
    public void actionPerformed(ActionEvent e) {
      // 選択された作品のIDを取得
      ArrayList<String> listNovelIDsToBeTreated = new ArrayList<>();
      for (int idx : table.getSelectedRows()) {
        idx = table.convertRowIndexToModel(idx);
        listNovelIDsToBeTreated.add((String) table.getModel().getValueAt(idx, 0));
      }
      for (String novelID : listNovelIDsToBeTreated) {
        String urlString = novelList.novelMetaMap.get(novelID).url;
        String cachePathString = Utils.getNovelWiseDstPath(urlString, cachePath.getAbsolutePath());
        try {
          Desktop.getDesktop().open(new File(cachePathString));
        } catch (IOException e1) {
          LogAppender.println("キャッシュディレクトリのパス " + cachePathString + " は開けません");
          e1.printStackTrace();
        }
      }
    }
  }

  /**
   * 指定されたURLに目次ページがある小説を指定されたディレクトリにキャッシュし、青空文庫形式テキストに変換する
   * 呼び出しているWebAozoraConverterは青空文庫やアルカディアなどのサイトにも対応しているが、 追加したメタデータを抽出する部分がなろう系にしか対応していないので注意
   *
   * @param urlString
   * @param props
   * @param webConfigPath
   */
  private HashMap<String, Object> cacheAndAozorize(String urlString, Properties props,
      File webConfigPath) {

    NovelMeta novelMeta = null;
    Boolean updated = false;
    Boolean cached = false;
    File aozoraTxt = null;
    HashMap<String, Object> resultMap = new HashMap<>();

    resultMap.put("novelMeta", novelMeta);
    resultMap.put("updated", updated);
    resultMap.put("cached", cached);
    resultMap.put("aozoraTxt", aozoraTxt);

    try {
      webConverter = WebAozoraConverter.createWebAozoraConverter(urlString, webConfigPath);
      if (webConverter == null) {
        LogAppender.append(urlString);
        LogAppender.println(" はキャッシュできませんでした");
      }

      // キャッシュパスをプロパティから取り出してセットする
      String cachePathString = props.getProperty("CachePath");
      if (cachePathString.equals("") || cachePathString == null) {
        LogAppender.println("キャッシュパスが設定されていません");
        return resultMap;
      } else {
        cachePath = new File(this.jarPath + cachePathString);
        if (!cachePath.isDirectory()) {
          cachePath.mkdirs();
          System.out.println("キャッシュパスを作成します : " + cachePath.getCanonicalPath());
        }
        if (!cachePath.isDirectory()) {
          System.out.println("キャッシュパスが作成できませんでした");
          return resultMap;
        }
      }

      // 青空文庫形式テキストへの変換に必要なパラメータをpropsから取り出して型を整える
      int webInterval = (int) (Float.parseFloat(props.getProperty("WebInterval") + "f") * 1000);
      float webModifiedExpire = Float.parseFloat(props.getProperty("WebModifiedExpire"));
      Boolean webConvertUpdated = false;
      if (props.getProperty("WebConvertUpdated").equals("1")) {
        webConvertUpdated = true;
      }
      Boolean webModifiedOnly = false;
      if (props.getProperty("WebModifiedOnly").equals("1")) {
        webModifiedOnly = true;
      }
      Boolean webModifiedTail = false;
      if (props.getProperty("WebModifiedTail").equals("1")) {
        webModifiedTail = true;
      }
      int webBeforeChapter;
      if (props.getProperty("WebBeforeChapter") == null
          || props.getProperty("WebBeforeChapter").equals("")) {
        webBeforeChapter = 0;
      } else {
        webBeforeChapter = Integer.parseInt(props.getProperty("WebBeforeChapter"));
      }

      // 変換済みファイルconverted.txtが既に存在する場合、更新がなかった場合に備えて退避させておく(更新なしだと破壊されるので)
      String dstDirPath = Utils.getNovelWiseDstPath(urlString, cachePathString);
      String originalName = "converted.txt";
      String backupName = "converted.bak.txt";
      File originalFile = new File(dstDirPath + originalName);
      File backupFile = new File(dstDirPath + backupName);
      if (backupFile.exists() && !backupFile.isFile()) {
        LogAppender.println(urlString + " の1次変換済みテキストファイルのバックアップが作成できません。同名のディレクトリが既に存在します");
        return resultMap;
      }
      backupFile.delete();
      originalFile.renameTo(backupFile);

      // キャッシュされたhtmlファイルを青空文庫形式テキストに変換する
      File srcFile = webConverter.convertToAozoraText(urlString, cachePath, webInterval,
          webModifiedExpire, webConvertUpdated, webModifiedOnly, webModifiedTail, webBeforeChapter);

      if (srcFile == null) {
        if (webConverter.isCanceled()) {
          LogAppender.append(urlString);
          LogAppender.println(" の変換をキャンセルしました");
        }
        // 更新がなかった場合、退避させておいたconverted.txtを復帰させる
        new File(dstDirPath + originalName).delete();
        new File(dstDirPath + backupName).renameTo(new File(dstDirPath + originalName));
        aozoraTxt = new File(dstDirPath + originalName);
      } else {
        aozoraTxt = srcFile;
        if (webConverter.isUpdated()) {

          updated = true;
          String urlFilePath =
              CharUtils.escapeUrlToFile(urlString.substring(urlString.indexOf("//") + 2));
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

          cached = true;
          LogAppender.append(dstPath);
          LogAppender.println(" に キャッシュしました");

        } else {
          // 更新がなかった場合、退避させておいたconverted.txtを復帰させる
          new File(dstDirPath + originalName).delete();
          new File(dstDirPath + backupName).renameTo(new File(dstDirPath + originalName));
          aozoraTxt = new File(dstDirPath + originalName);
        }
      }
    } catch (IOException e1) {
      LogAppender.println("Web上データの取得-青空文庫テキストへの変換に失敗しました");
      e1.printStackTrace();
    }
    novelMeta = webConverter.getNovelMeta();
    resultMap.put("novelMeta", novelMeta);
    resultMap.put("updated", updated);
    resultMap.put("cached", cached);
    resultMap.put("aozoraTxt", aozoraTxt);
    return resultMap;
  }

  /** 青空文庫テキストをEpub3ファイルに変換する */
  private File convertAozoraToEpub3(File aozoraTxt, String dstPath) {

    // 出力ファイル
    File outFile = null;

    // コンバータとライターの設定
    int resizeW = 0;
    if (props.getPropertiesAsBoolean("ResizeW"))
      try {
        resizeW = Integer.parseInt(props.getProperty("ResizeNumW"));
      } catch (Exception e) {
      }
    int resizeH = 0;
    if (props.getPropertiesAsBoolean("ResizeH"))
      try {
        resizeH = Integer.parseInt(props.getProperty("ResizeNumH"));
      } catch (Exception e) {
      }
    // int pixels = 0;
    // if (jCheckPixel.isSelected()) try { pixels =
    // Integer.parseInt(jTextPixelW.getText())*Integer.parseInt(jTextPixelH.getText());
    // } catch (Exception e) {}
    int dispW = Integer.parseInt(props.getProperty("DispW"));
    int dispH = Integer.parseInt(props.getProperty("DispH"));
    int coverW = Integer.parseInt(props.getProperty("CoverW"));
    int coverH = Integer.parseInt(props.getProperty("CoverH"));
    int singlePageSizeW = Integer.parseInt(props.getProperty("SinglePageSizeW"));
    int singlePageSizeH = Integer.parseInt(props.getProperty("SinglePageSizeH"));
    int singlePageWidth = Integer.parseInt(props.getProperty("SinglePageWidth"));

    float imageScale = 0;
    if (props.getPropertiesAsBoolean("ImageScaleChecked"))
      try {
        imageScale = Float.parseFloat(props.getProperty("ImageScale"));
      } catch (Exception e) {
      }
    int imageFloatType = 0; // 0=無効 1=上 2=下
    int imageFloatW = 0;
    int imageFloatH = 0;
    if (props.getPropertiesAsBoolean("ImageFloat")) {
      imageFloatType = Integer.parseInt(props.getProperty("ImageFloatType")) + 1;
      // jComboImageFloatType : キー "ImageFloatType"でselectionIndexをそのまま保存
      // index 0 -> "上/左", index 1 -> "下/右"
      try {
        imageFloatW = Integer.parseInt(props.getProperty("ImageFloatW"));
      } catch (Exception e) {
      }
      try {
        imageFloatH = Integer.parseInt(props.getProperty("ImageFloatH"));
      } catch (Exception e) {
      }
    }
    float jpegQualty = 0.8f;
    try {
      jpegQualty = Integer.parseInt(props.getProperty("JpegQuality")) / 100f;
    } catch (Exception e) {
    }
    float gamma = 1.0f;
    if (props.getPropertiesAsBoolean("Gamma"))
      try {
        gamma = Float.parseFloat(props.getProperty("GammaValue"));
      } catch (Exception e) {
      }
    int autoMarginLimitH = 0;
    int autoMarginLimitV = 0;
    int autoMarginWhiteLevel = 0;
    float autoMarginPadding = 0;
    int autoMarginNombre = 0;
    float autoMarginNombreSize = 0.03f;
    if (props.getPropertiesAsBoolean("AutoMargin")) {
      try {
        autoMarginLimitH = Integer.parseInt(props.getProperty("AutoMarginLimitH"));
      } catch (Exception e) {
      }
      try {
        autoMarginLimitV = Integer.parseInt(props.getProperty("AutoMarginLimitV"));
      } catch (Exception e) {
      }
      try {
        autoMarginWhiteLevel = Integer.parseInt(props.getProperty("AutoMarginWhiteLevel"));
      } catch (Exception e) {
      }
      try {
        autoMarginPadding = Float.parseFloat(props.getProperty("AutoMarginPadding"));
      } catch (Exception e) {
      }
      autoMarginNombre = Integer.parseInt(props.getProperty("AutoMarginNombre"));
      try {
        autoMarginNombreSize = Float.parseFloat(props.getProperty("AutoMarginNombreSize")) * 0.01f;
      } catch (Exception e) {
      }
    }
    int rorateAngle = 0;
    int selectedRotateAngleIdx = Integer.parseInt(props.getProperty("RotateImage"));
    if (selectedRotateAngleIdx == 1)
      rorateAngle = 90;
    else if (selectedRotateAngleIdx == 2)
      rorateAngle = -90;

    int imageSizeType = Integer.parseInt(props.getProperty("ImageSizeType"));

    this.epub3Writer.setImageParam(dispW, dispH, coverW, coverH, resizeW, resizeH, singlePageSizeW,
        singlePageSizeH, singlePageWidth, imageSizeType, props.getPropertiesAsBoolean("FitImage"),
        props.getPropertiesAsBoolean("SvgImage"), rorateAngle, imageScale, imageFloatType,
        imageFloatW, imageFloatH, jpegQualty, gamma, autoMarginLimitH, autoMarginLimitV,
        autoMarginWhiteLevel, autoMarginPadding, autoMarginNombre, autoMarginNombreSize);
    this.epub3ImageWriter.setImageParam(dispW, dispH, coverW, coverH, resizeW, resizeH,
        singlePageSizeW, singlePageSizeH, singlePageWidth, imageSizeType,
        props.getPropertiesAsBoolean("FitImage"), props.getPropertiesAsBoolean("SvgImage"),
        rorateAngle, imageScale, imageFloatType, imageFloatW, imageFloatH, jpegQualty, gamma,
        autoMarginLimitH, autoMarginLimitV, autoMarginWhiteLevel, autoMarginPadding,
        autoMarginNombre, autoMarginNombreSize);
    // 目次階層化設定
    this.epub3Writer.setTocParam(props.getPropertiesAsBoolean("NavNest"),
        props.getPropertiesAsBoolean("NcxNest"));

    // スタイル設定
    // int marginUnitIdx =
    // Integer.parseInt(props.getProperty("PageMarginUnit"));
    // String pageMarginUnit = null;
    // if (marginUnitIdx == 0) {
    // pageMarginUnit = "em";
    // } else if (marginUnitIdx == 1) {
    // pageMarginUnit = "%";
    // }
    String[] pageMargin = props.getProperty("PageMargin").split(",", 4);
    String[] bodyMargin = props.getProperty("BodyMargin").split(",", 4);
    float lineHeight = 1.8f;
    try {
      lineHeight = Float.parseFloat(props.getProperty("LineHeight"));
    } catch (Exception e) {
    }
    int fontSize = 100;
    try {
      fontSize = (int) Float.parseFloat(props.getProperty("FontSize"));
    } catch (Exception e) {
    }

    int dakutenType = Integer.parseInt(props.getProperty("DakutenType"));

    this.epub3Writer.setStyles(pageMargin, bodyMargin, lineHeight, fontSize,
        props.getPropertiesAsBoolean("BoldUseGothic"),
        props.getPropertiesAsBoolean("GothicUseBold"));

    try {
      // 挿絵なし
      this.aozoraConverter.setNoIllust(props.getPropertiesAsBoolean("NoIllust"));
      // 栞用ID出力
      this.aozoraConverter.setWithMarkId(props.getPropertiesAsBoolean("MarkId"));
      // 変換オプション設定
      this.aozoraConverter.setAutoYoko(props.getPropertiesAsBoolean("AutoYoko"),
          props.getPropertiesAsBoolean("AutoYokoNum1"),
          props.getPropertiesAsBoolean("AutoYokoNum3"),
          props.getPropertiesAsBoolean("AutoYokoEQ1"));
      // 文字出力設定
      this.aozoraConverter.setCharOutput(dakutenType, props.getPropertiesAsBoolean("IvsBMP"),
          props.getPropertiesAsBoolean("IvsSSP"));
      // 全角スペースの禁則
      this.aozoraConverter
          .setSpaceHyphenation(Integer.parseInt(props.getProperty("SpaceHyphenation")));
      // 注記のルビ表示
      // propsのキー"ChukiRuby"の値: jRadioChukiRuby0 = "非表示" -> "0",
      // jRadioChukiRuby1 = "ルビ" -> "1", jRadioChukiRuby2 = "小書き" -> "2"
      // ∴ "0"なら false, false "1"なら true, false "2"なら false, true
      String propValueChukiRuby = props.getProperty("ChukiRuby");
      boolean chukiRuby1 = false;
      boolean chukiRuby2 = false;
      if (propValueChukiRuby.equals("1")) {
        chukiRuby1 = true;
      } else if (propValueChukiRuby.equals("2")) {
        chukiRuby2 = true;
      }
      this.aozoraConverter.setChukiRuby(chukiRuby1, chukiRuby2);
      // コメント
      this.aozoraConverter.setCommentPrint(props.getPropertiesAsBoolean("CommentPrint"),
          props.getPropertiesAsBoolean("CommentConvert"));

      // float表示
      this.aozoraConverter.setImageFloat(props.getPropertiesAsBoolean("ImageFloatPage"),
          props.getPropertiesAsBoolean("ImageFloatBlock"));

      // 空行除去
      int removeEmptyLine = Integer.parseInt(props.getProperty("RemoveEmptyLine"));
      int maxEmptyLine = Integer.parseInt(props.getProperty("MaxEmptyLine"));
      this.aozoraConverter.setRemoveEmptyLine(removeEmptyLine, maxEmptyLine);

      // 行頭字下げ
      this.aozoraConverter.setForceIndent(props.getPropertiesAsBoolean("ForceIndent"));

      // 強制改ページ
      if (props.getPropertiesAsBoolean("PageBreak")) {
        try {
          int forcePageBreakSize = 0;
          int forcePageBreakEmpty = 0;
          int forcePageBreakEmptySize = 0;
          int forcePageBreakChapter = 0;
          int forcePageBreakChapterSize = 0;
          forcePageBreakSize = Integer.parseInt(props.getProperty("PageBreakSize")) * 1024;
          if (props.getPropertiesAsBoolean("PageBreakEmpty")) {
            forcePageBreakEmpty = Integer.parseInt(props.getProperty("PageBreakEmptyLine"));
            forcePageBreakEmptySize =
                Integer.parseInt(props.getProperty("PageBreakEmptySize")) * 1024;
          }
          if (props.getPropertiesAsBoolean("PageBreakChapter")) {
            forcePageBreakChapter = 1;
            forcePageBreakChapterSize =
                Integer.parseInt(props.getProperty("PageBreakChapterSize")) * 1024;
          }
          // Converterに設定
          this.aozoraConverter.setForcePageBreak(forcePageBreakSize, forcePageBreakEmpty,
              forcePageBreakEmptySize, forcePageBreakChapter, forcePageBreakChapterSize);
        } catch (Exception e) {
          LogAppender.println("強制改ページパラメータ読み込みエラー");
        }
      }

      // 目次設定
      int maxLength = 64;
      try {
        maxLength = Integer.parseInt(props.getProperty("MaxChapterNameLength"));
      } catch (Exception e) {
      }

      this.aozoraConverter.setChapterLevel(maxLength,
          props.getPropertiesAsBoolean("ChapterExclude"),
          props.getPropertiesAsBoolean("ChapterUseNextLine"),
          props.getPropertiesAsBoolean("ChapterSection"), props.getPropertiesAsBoolean("ChapterH"),
          props.getPropertiesAsBoolean("ChapterH1"), props.getPropertiesAsBoolean("ChapterH2"),
          props.getPropertiesAsBoolean("ChapterH3"),
          props.getPropertiesAsBoolean("SameLineChapter"),
          props.getPropertiesAsBoolean("ChapterName"),
          props.getPropertiesAsBoolean("ChapterNumOnly"),
          props.getPropertiesAsBoolean("ChapterNumTitle"),
          props.getPropertiesAsBoolean("ChapterNumParen"),
          props.getPropertiesAsBoolean("ChapterNumParenTitle"),
          props.getPropertiesAsBoolean("ChapterPattern") ? props.getProperty("ChapterPatternText")
              : "");
    } catch (Exception e) {
      e.printStackTrace();
      LogAppender.append("エラーが発生しました : ");
      LogAppender.println(e.getMessage());
    }

    // 表題表記の文字列と、そのインデックスを紐づけたハッシュ
    HashMap<String, String> titleTypeMap = new HashMap<>();
    titleTypeMap.put("0", "表題 → 著者名");
    titleTypeMap.put("1", "著者名 → 表題");
    titleTypeMap.put("2", "表題 → 著者名(副題優先)");
    titleTypeMap.put("3", "表題のみ(1行)");
    titleTypeMap.put("4", "表題+著者のみ(2行)");
    titleTypeMap.put("5", "なし");

    // 表紙にする挿絵の位置-1なら挿絵は使わない ダイアログに設定項目がなく、propsにも保存されないパラメータ
    int coverImageIndex = -1;

    // 表紙情報追加
    String coverFileName = this.props.getProperty("Cover");
    if (coverFileName == null || props.getProperty("Cover").length() == 0) {
      coverFileName = ""; // 先頭の挿絵
      coverImageIndex = 0;
    } else if (coverFileName.contains("入力ファイル名と同じ")) {
      coverFileName = AozoraEpub3.getSameCoverFileName(aozoraTxt); // 入力ファイルと同じ名前+.jpg/.png
    } else if (coverFileName.contains("表紙なし")) {
      coverFileName = null; // 表紙無し
    }

    // 画像取得
    boolean isFile = true; // 画像が圧縮ファイル内にあるならfalseだが、ここでは決め打ちで良い
    ImageInfoReader imageInfoReader = new ImageInfoReader(isFile, aozoraTxt);

    // BookInfo取得
    BookInfo bookInfo = null;
    int txtIdx = 0; // 圧縮ファイル内のテキストファイルの位置を示す数値だが、ここでは参照されないので入ってさえいればなんでも良い
    int intEncType = Integer.parseInt(props.getProperty("EncType"));
    String encType = "UTF-8";
    if (intEncType == 0) {
      encType = "MS932";
    }

    Boolean pubFirst = props.getPropertiesAsBoolean("PubFirst");
    int titleTypeIndex = Integer.parseInt(props.getProperty("TitleType"));
    try {
      // テキストファイルからメタ情報や画像単独ページ情報を取得
      bookInfo = AozoraEpub3.getBookInfo(aozoraTxt, "txt", txtIdx, imageInfoReader,
          this.aozoraConverter, encType, BookInfo.TitleType.indexOf(titleTypeIndex), pubFirst);
    } catch (Exception e) {
      LogAppender.error("ファイルが読み込めませんでした : " + aozoraTxt.getPath());
      return null;
    }

    if (this.convertCanceled) {
      LogAppender.println("変換処理を中止しました : " + aozoraTxt.getAbsolutePath());
      return null;
    }

    Epub3Writer writer = this.epub3Writer;
    if (bookInfo == null) {
      LogAppender.error("書籍の情報が取得できませんでした");
      return null;
    }

    // オリジナルではここにソースがzipだった場合の処理が入っていた

    // オリジナルではここにプログレスバーの設定が入っていた

    // 表紙目次ページ出力設定
    bookInfo.insertCoverPage = props.getPropertiesAsBoolean("CoverPage");
    bookInfo.insertTocPage = props.getPropertiesAsBoolean("TocPage");
    bookInfo.insertCoverPageToc = props.getPropertiesAsBoolean("CoverPageToc");
    bookInfo.insertTitleToc = props.getPropertiesAsBoolean("TitleToc");

    // 表題の見出しが非表示で行が追加されていたら削除
    if (!bookInfo.insertTitleToc && bookInfo.titleLine >= 0) {
      bookInfo.removeChapterLineInfo(bookInfo.titleLine);
    }

    // 目次縦書き
    bookInfo.setTocVertical(props.getPropertiesAsBoolean("TocVertical"));
    // 縦書き横書き設定追加
    bookInfo.vertical = props.getPropertiesAsBoolean("Vertical");
    this.aozoraConverter.vertical = bookInfo.vertical;

    // 表紙設定
    // 表題左右中央
    if (!props.getPropertiesAsBoolean("TitlePageWrite")) {
      bookInfo.titlePageType = BookInfo.TITLE_NONE;
    } else {
      int propValue = Integer.parseInt(props.getProperty("TitlePage")); // "本文内"なら"0"、"中央"なら"1"、"横書き"なら"2"
      switch (propValue) {
        case BookInfo.TITLE_NORMAL:
          bookInfo.titlePageType = BookInfo.TITLE_NORMAL;
          break;
        case BookInfo.TITLE_MIDDLE:
          bookInfo.titlePageType = BookInfo.TITLE_MIDDLE;
          break;
        case BookInfo.TITLE_HORIZONTAL:
          bookInfo.titlePageType = BookInfo.TITLE_HORIZONTAL;
          break;
        default:
          break;
      }
    }

    // 先頭からの場合で指定行数以降なら表紙無し
    if ("".equals(coverFileName)) {
      try {
        int maxCoverLine = Integer.parseInt(props.getProperty("MaxCoverLine"));
        if (maxCoverLine > 0
            && (bookInfo.firstImageLineNum == -1 || bookInfo.firstImageLineNum >= maxCoverLine)) {
          coverImageIndex = -1;
          coverFileName = null;
        } else {
          coverImageIndex = bookInfo.firstImageIdx;
        }
      } catch (Exception e) {
      }
    }

    // 表紙ページの情報をbookInfoに設定
    bookInfo.coverFileName = coverFileName;
    bookInfo.coverImageIndex = coverImageIndex;

    String[] titleCreator = BookInfo.getFileTitleCreator(aozoraTxt.getName());
    if (props.getPropertiesAsBoolean("UseFileName")) {
      // ファイル名優先ならテキスト側の情報は不要
      bookInfo.title = "";
      bookInfo.creator = "";
      if (titleCreator[0] != null)
        bookInfo.title = titleCreator[0];
      if (titleCreator[1] != null)
        bookInfo.creator = titleCreator[1];
    } else {
      // テキストから取得できなければファイル名を利用
      if (bookInfo.title == null || bookInfo.title.length() == 0) {
        bookInfo.title = titleCreator[0] == null ? "" : titleCreator[0];
        if (bookInfo.creator == null || bookInfo.creator.length() == 0)
          bookInfo.creator = titleCreator[1] == null ? "" : titleCreator[1];
      }
    }

    if (this.convertCanceled) {
      LogAppender.println("変換処理を中止しました : " + aozoraTxt.getAbsolutePath());
      return null;
    }

    // 前回の変換設定を反映
    BookInfoHistory history = this.getBookInfoHistory(bookInfo);
    if (history != null) {
      if (bookInfo.title.length() == 0)
        bookInfo.title = history.title;
      bookInfo.titleAs = history.titleAs;
      if (bookInfo.creator.length() == 0)
        bookInfo.creator = history.creator;
      bookInfo.creatorAs = history.creatorAs;
      if (bookInfo.publisher == null)
        bookInfo.publisher = history.publisher;
      // 表紙設定
      if (props.getPropertiesAsBoolean("CoverHistory")) {
        bookInfo.coverEditInfo = history.coverEditInfo;
        bookInfo.coverFileName = history.coverFileName;
        bookInfo.coverExt = history.coverExt;
        bookInfo.coverImageIndex = history.coverImageIndex;

        // 確認ダイアログ表示しない場合はイメージを生成
        if (!props.getPropertiesAsBoolean("ChkConfirm") && bookInfo.coverEditInfo != null) {
          try {
            this.jConfirmDialog.jCoverImagePanel.setBookInfo(bookInfo);
            if (bookInfo.coverImageIndex >= 0
                && bookInfo.coverImageIndex < imageInfoReader.countImageFileNames()) {
              bookInfo.coverImage = imageInfoReader.getImage(bookInfo.coverImageIndex);
            } else if (bookInfo.coverImage == null && bookInfo.coverFileName != null) {
              bookInfo.loadCoverImage(bookInfo.coverFileName);
            }
            bookInfo.coverImage =
                this.jConfirmDialog.jCoverImagePanel.getModifiedImage(coverW, coverH);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }

    String outExt = props.getProperty("Ext");
    ////////////////////////////////
    // Kindleチェック
    File kindlegen = null;
    writer.setIsKindle(false);
    if (outExt.startsWith(".mobi")) {
      kindlegen = new File(this.jarPath + "kindlegen.exe");
      if (!kindlegen.isFile()) {
        kindlegen = new File(this.jarPath + "kindlegen");
        if (!kindlegen.isFile()) {
          kindlegen = null;
        }
      }
      if (kindlegen == null) {
        JOptionPane.showMessageDialog(frame, "kindlegenがありません\nkindlegen.exeをjarファイルの場所にコピーしてください",
            "kindlegenエラー", JOptionPane.WARNING_MESSAGE);
        LogAppender.println("変換処理をキャンセルしました");
        return null;
      }
      writer.setIsKindle(true);
    }

    // 確認ダイアログ 変換ボタン押下時にbookInfo更新
    if (props.getPropertiesAsBoolean("ChkConfirm")) {
      // 表題と著者設定 ファイル名から設定
      String title = "";
      String creator = "";
      if (bookInfo.title != null)
        title = bookInfo.title;
      if (bookInfo.creator != null)
        creator = bookInfo.creator;

      this.jConfirmDialog.setChapterCheck(props.getPropertiesAsBoolean("ChapterSection"),
          props.getPropertiesAsBoolean("ChapterH"), props.getPropertiesAsBoolean("ChapterH1"),
          props.getPropertiesAsBoolean("ChapterH2"), props.getPropertiesAsBoolean("ChapterH3"),
          props.getPropertiesAsBoolean("ChapterName"),
          props.getPropertiesAsBoolean("ChapterNumOnly")
              || props.getPropertiesAsBoolean("ChapterNumTitle")
              || props.getPropertiesAsBoolean("ChapterNumParen")
              || props.getPropertiesAsBoolean("ChapterNumParenTitle"),
          props.getPropertiesAsBoolean("ChapterPattern"));
      this.jConfirmDialog.showDialog(aozoraTxt, dstPath + File.separator, title, creator,
          Integer.parseInt(props.getProperty("TitleType")),
          props.getPropertiesAsBoolean("PubFirst"), bookInfo, imageInfoReader, frame.getLocation(),
          coverW, coverH);

      // ダイアログが閉じた後に再開
      if (this.jConfirmDialog.canceled) {
        this.convertCanceled = true;
        LogAppender.println("変換処理を中止しました : " + aozoraTxt.getAbsolutePath());
        return null;
      }
      if (this.jConfirmDialog.skipped) {
        this.setBookInfoHistory(bookInfo);
        LogAppender.println("変換をスキップしました : " + aozoraTxt.getAbsolutePath());
        return null;
      }

      // 変換前確認のチェックを反映
      if (!this.jConfirmDialog.jCheckConfirm2.isSelected())
        dialogConverterSettings.jCheckConfirm.setSelected(false);

      // 確認ダイアログの値をBookInfoに設定
      bookInfo.title = this.jConfirmDialog.getMetaTitle();
      bookInfo.creator = this.jConfirmDialog.getMetaCreator();
      bookInfo.titleAs = this.jConfirmDialog.getMetaTitleAs();
      bookInfo.creatorAs = this.jConfirmDialog.getMetaCreatorAs();
      bookInfo.publisher = this.jConfirmDialog.getMetaPublisher();

      // 著者が空欄なら著者行もクリア
      if (bookInfo.creator.length() == 0)
        bookInfo.creatorLine = -1;

      // プレビューでトリミングされていたらbookInfo.coverImageにBufferedImageを設定 それ以外はnullにする
      BufferedImage coverImage =
          this.jConfirmDialog.jCoverImagePanel.getModifiedImage(coverW, coverH);
      if (coverImage != null) {
        // Epub3Writerでイメージを出力
        bookInfo.coverImage = coverImage;
        // bookInfo.coverFileName = null;
        // 元の表紙は残す
        if (this.jConfirmDialog.jCheckReplaceCover.isSelected())
          bookInfo.coverImageIndex = -1;
      } else {
        bookInfo.coverImage = null;
      }

      this.setBookInfoHistory(bookInfo);
    } else {
      // 表題の見出しが非表示で行が追加されていたら削除
      if (!bookInfo.insertTitleToc && bookInfo.titleLine >= 0) {
        bookInfo.removeChapterLineInfo(bookInfo.titleLine);
      }
    }

    boolean autoFileName = props.getPropertiesAsBoolean("AutoFileName");
    boolean overWrite = props.getPropertiesAsBoolean("OverWrite");

    // Kindleは一旦tmpファイルに出力
    File outFileOrg = null;
    if (kindlegen != null) {
      outFile =
          AozoraEpub3.getOutFile(aozoraTxt, new File(dstPath), bookInfo, autoFileName, ".epub");
      File mobiFile = new File(
          outFile.getAbsolutePath().substring(0, outFile.getAbsolutePath().length() - 4) + "mobi");
      if (!overWrite && (mobiFile.exists() || (outExt.endsWith(".epub") && outFile.exists()))) {
        LogAppender.println("変換中止: " + aozoraTxt.getAbsolutePath());
        if (mobiFile.exists())
          LogAppender.println("ファイルが存在します: " + mobiFile.getAbsolutePath());
        else
          LogAppender.println("ファイルが存在します: " + outFile.getAbsolutePath());
        return null;
      }
      outFileOrg = outFile;
      try {
        outFile = File.createTempFile("kindle", ".epub", outFile.getParentFile());
        if (!outExt.endsWith(".epub"))
          outFile.deleteOnExit();
      } catch (IOException e) {
        outFile = outFileOrg;
        outFileOrg = null;
      }
    } else {
      File dstFile = new File(dstPath);
      outFile = AozoraEpub3.getOutFile(aozoraTxt, dstFile, bookInfo, autoFileName, outExt);
      // 上書き確認
      if (!overWrite && outFile.exists()) {
        LogAppender.println("変換中止: " + aozoraTxt.getAbsolutePath());
        LogAppender.println("ファイルが存在します: " + outFile.getAbsolutePath());
        return null;
      }
    }
    /*
     * if (overWrite && outFile.exists()) { int ret = JOptionPane.showConfirmDialog(this,
     * "ファイルが存在します\n上書きしますか？\n(取り消しで変換キャンセル)", "上書き確認", JOptionPane.YES_NO_CANCEL_OPTION); if (ret
     * == JOptionPane.NO_OPTION) { LogAppender.println("変換中止: "+srcFile.getAbsolutePath()); return;
     * } else if (ret == JOptionPane.CANCEL_OPTION) {
     * LogAppender.println("変換中止: "+srcFile.getAbsolutePath()); convertCanceled = true;
     * LogAppender.println("変換処理をキャンセルしました"); return; } }
     */

    ////////////////////////////////
    // 変換実行
    String ext = aozoraTxt.getName();
    ext = ext.substring(ext.lastIndexOf('.') + 1).toLowerCase();
    AozoraEpub3.convertFile(aozoraTxt, ext, outFile, this.aozoraConverter, writer, encType,
        bookInfo, imageInfoReader, txtIdx);

    imageInfoReader = null;
    // 画像は除去
    bookInfo.coverImage = null;

    // System.gc();

    // 変換中にキャンセルされた場合
    if (this.convertCanceled) {
      LogAppender.println("変換処理を中止しました : " + aozoraTxt.getAbsolutePath());
      return null;
    }

    ////////////////////////////////
    // kindlegen.exeがあれば実行
    try {
      if (kindlegen != null) {
        long time = System.currentTimeMillis();
        String outFileName = outFile.getAbsolutePath();
        LogAppender
            .println("kindlegenを実行します : " + kindlegen.getName() + " \"" + outFileName + "\"");
        ProcessBuilder pb = new ProcessBuilder(kindlegen.getAbsolutePath(), "-locale", "en",
            "-verbose", outFileName);
        this.kindleProcess = pb.start();
        BufferedReader br =
            new BufferedReader(new InputStreamReader(this.kindleProcess.getInputStream()));
        String line;
        int idx = 0;
        int cnt = 0;
        String msg = "";
        while ((line = br.readLine()) != null) {
          if (line.length() > 0) {
            System.out.println(line);
            if (msg.startsWith("Error"))
              msg += line;
            else
              msg = line;
            if (idx++ % 2 == 0) {
              if (cnt++ > 100) {
                cnt = 1;
                LogAppender.println();
              }
              LogAppender.append(".");
            }
          }
        }
        br.close();
        if (convertCanceled) {
          LogAppender.println("\n" + msg + "\nkindlegenの変換を中断しました");
        } else {
          if (outFileOrg != null) {
            // mobiリネーム
            File mobiTmpFile = new File(
                outFile.getAbsolutePath().substring(0, outFile.getAbsolutePath().length() - 4)
                    + "mobi");
            File mobiFile = new File(
                outFileOrg.getAbsolutePath().substring(0, outFileOrg.getAbsolutePath().length() - 4)
                    + "mobi");
            if (mobiFile.exists())
              mobiFile.delete();
            mobiTmpFile.renameTo(mobiFile);
            if (outExt.endsWith(".epub")) {
              // epubリネーム
              if (outFileOrg.exists())
                outFileOrg.delete();
              outFile.renameTo(outFileOrg);
            } else {
              outFile.delete();
            }
            LogAppender.println("\n" + msg + "\nkindlegen変換完了 ["
                + (((System.currentTimeMillis() - time) / 100) / 10f) + "s] -> "
                + mobiFile.getName());
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (this.kindleProcess != null)
        this.kindleProcess.destroy();
      this.kindleProcess = null;
    }

    return outFile;
  }

  /**
   * キャッシュと変換実行
   *
   * @param urlString
   * @return
   * @throws IOException
   */
  private Runnable execWebConvert(String urlString) throws IOException {

    if (urlString.length() <= 0) {
      return null;
    }

    // 作品個別のpropertyをロード
    String novelWiseDstPath = Utils.getNovelWiseDstPath(urlString, props.getProperty("CachePath"));
    try {
      FileInputStream fos = new FileInputStream(novelWiseDstPath + "convert.ini");
      individualProps.load(fos);
      fos.close();
    } catch (Exception e1) { // 作品個別のpropertyファイルが存在しない場合はグローバル値を使用
      individualProps = props;
      LogAppender.append("作品ID ");
      LogAppender.append(new File(novelWiseDstPath).getName().toString());
      LogAppender.append(" の作品別変換設定ファイルが見つかりません。デフォルト値を使用します\n");
    }

    // 変換設定以外の値は現行のnucJ.iniをそのまま使う(propsの方から読み出す)
    // 変換設定関連は(あれば)作品個別の値を使う(individualPropsの方から読み出す)
    // グローバル値は変更されないのでiniファイルへの書き戻しはしない

    // URLをキャッシュ
    HashMap<String, Object> resultMap = this.cacheAndAozorize(urlString, props, webConfigPath);

    NovelMeta novelMeta = (NovelMeta) resultMap.get("novelMeta");
    // Boolean cached = (Boolean) resultMap.get("cached");
    Boolean updated = (Boolean) resultMap.get("updated");
    if (!updated) {
      return null;
    }
    Boolean newlyAdded = false;
    if (novelList.novelMetaMap.get(novelMeta.novelID) == null) {
      newlyAdded = true;
    }
    File aozoraTxt = (File) resultMap.get("aozoraTxt");

    // EPUB3ファイル出力先の設定
    String dstPath = props.getProperty("EPUB3DstPath");
    if (dstPath == null) {
      LogAppender.println("EPUBファイル出力先が指定されていません。処理を中止します");
      return null;
    }
    if (props.getPropertiesAsBoolean("UseNovelwiseDirEPUB3")) {
      dstPath = dstPath + File.separator + novelMeta.novelID;
      if (new File(dstPath).exists()) {
        if (!new File(dstPath).isDirectory()) {
          LogAppender.println("指定されたEPUB3ファイル出力先ディレクトリと同名のファイルが既に存在します。処理を中止します");
          return null;
        }
      } else {
        new File(dstPath).mkdirs();
      }
    }

    // ビューワ用ファイル出力先の設定
    String dstPathForViewer = props.getProperty("ViewerDstPath");
    if (dstPathForViewer == null) {
      LogAppender.println("ビューワ用ファイル出力先が指定されていません。処理を中止します");
      return null;
    }
    if (props.getPropertiesAsBoolean("UseNovelwiseDirViewer")) {
      dstPathForViewer = dstPathForViewer + File.separator + novelMeta.novelID;
      if (new File(dstPathForViewer).exists()) {
        if (!new File(dstPathForViewer).isDirectory()) {
          LogAppender.println("指定されたビューワ用ファイル出力先ディレクトリと同名のファイルが既に存在します。処理を中止します");
          return null;
        }
      } else {
        new File(dstPathForViewer).mkdirs();
      }
    }

    // 紛らわしいが、ここまでで使われてるaozoraTxtはクラスAozoraTxtのインスタンスではなく、File("converted.txt")である
    // (クラスAozoraTxtを書く前に作ったの部分なので) クラスAozoraTxtはこの直後に初めて出てくる

    // converted.txtの中身が不正な(更新なしのために破壊された)場合、この行で処理が消滅しているはず
    // AozoraTxtのコンストラクタがArrayIndexOutOfBoundsExceptionを吐くようにしたのでキャッチできると思うが
    AozoraTxt aozoraBook = new AozoraTxt(aozoraTxt, "UTF-8");

    // 分割関連パラメータの設定
    boolean flagOutputForViewer = true;
    boolean flagOutputForEPUB3 = true;
    // 紛らわしいが、これ↓はEPUB3ファイルそのものの出力先ではなく、EPUB3変換の元になる分割された青空文庫テキストの出力先
    String dstPathForEPUB3 = aozoraTxt.getParent();
    Integer volumeLength = Integer.parseInt(individualProps.getProperty("VolumeLength"));
    // TODO volumeLength値の正当性検査
    int length = aozoraBook.getLength();
    if (volumeLength == null || volumeLength == 0) {
      // "一巻あたりの文字数上限が設定されていません"
    } else if ((length / volumeLength) > 20) {
      // "設定されている一巻あたりの文字数に従うと、全#{}巻になります。続行しますか？"
    }
    boolean forceChapterwise = individualProps.getPropertiesAsBoolean("SplitChapterWise");
    boolean allowSingleEmptyLine = individualProps.getPropertiesAsBoolean("AllowSingleEmptyLines");
    int successiveEmptyLinesLimit =
        Integer.parseInt(individualProps.getProperty("SuccessiveEmptyLinesLimit"));

    // 分割実行
    ArrayList<ArrayList<File>> listOfLists = aozoraBook.split(dstPathForViewer, dstPathForEPUB3,
        volumeLength, forceChapterwise, flagOutputForViewer, flagOutputForEPUB3,
        allowSingleEmptyLine, successiveEmptyLinesLimit);

    ArrayList<File> listSrcs = listOfLists.get(0);
    ArrayList<File> listAozora = listOfLists.get(1);
    ArrayList<File> listEpub3 = new ArrayList<>();

    // 青空文庫テキストからEPUB3ファイルへの変換実行
    for (File srcFile : listSrcs) {

      if (updated || newlyAdded) {
        if (srcFile != null && srcFile.isFile()) {
          listEpub3.add(convertAozoraToEpub3(srcFile, dstPath));
        }

        String novelID = novelMeta.novelID;
        novelList.novelMetaMap.put(novelID, novelMeta);

        try {
          writeCSV(csvFile, novelList);
        } catch (IOException e) {
          System.out.println("execConvertの中でCSVファイルの書き込みに失敗した");
          e.printStackTrace();
        }

      } else {
        LogAppender.println(novelMeta.novelID + ": EPUB3への変換は実行されません");
      }
    }

    // ビューワ閲覧用青空文庫TXTファイルリストの書き出し
    writeFileList(listAozora, "Viewer", novelMeta, props);

    // EPUBファイルリストの書き出し
    writeFileList(listEpub3, "EPUB3", novelMeta, props);

    return null;
  }

  /**
   * 変換再実行
   *
   * @param urlString
   * @return
   * @throws IOException
   */
  private Runnable execReConvert(String novelID) throws IOException {

    if (novelID.length() <= 0) {
      LogAppender.println("作品IDの値が空です");
      return null;
    }

    // 作品個別のpropertyをロード
    NovelMeta novelMeta = this.novelList.novelMetaMap.get(novelID);
    String urlString = novelMeta.url;
    String novelWiseDstPath = Utils.getNovelWiseDstPath(urlString, props.getProperty("CachePath"));
    try {
      FileInputStream fos = new FileInputStream(novelWiseDstPath + "convert.ini");
      individualProps.load(fos);
      fos.close();
    } catch (Exception e1) {
      individualProps = props; // 作品個別のpropertyファイルが存在しない場合はグローバル値を使用
      LogAppender.append("作品ID ");
      LogAppender.append(novelID);
      LogAppender.append(" の作品別変換設定ファイルが見つかりません。デフォルト値を使用します\n");
    }

    AozoraTxt aozoraBook;
    String srcFilePath = novelWiseDstPath + "converted.txt";
    File srcFile = new File(srcFilePath);
    try {
      aozoraBook = new AozoraTxt(srcFile, "UTF-8");
    } catch (Exception e) {
      LogAppender.append("作品ID ");
      LogAppender.append(novelID);
      LogAppender.append(" 青空文庫テキスト1次変換済みファイル converted.txt が見つかりません。変換を中止します\n");
      return null;
    }

    // 分割関連パラメータの設定
    boolean flagOutputForViewer = true;
    boolean flagOutputForEPUB3 = true;

    // ビューワ閲覧用青空文庫テキスト出力先の設定
    String dstPathForViewer = props.getProperty("ViewerDstPath");
    if (dstPathForViewer == null) {
      LogAppender.println("ビューワ閲覧用青空文庫テキスト出力先が指定されていません。処理を中止します");
      return null;
    }
    if (props.getPropertiesAsBoolean("UseNovelwiseDirViewer")) {
      dstPathForViewer = dstPathForViewer + File.separator + novelID;
      if (new File(dstPathForViewer).exists()) {
        if (!new File(dstPathForViewer).isDirectory()) {
          LogAppender.println("指定されたビューワ閲覧用青空文庫テキスト出力先ディレクトリと同名のファイルが既に存在します。処理を中止します");
          return null;
        }
      } else {
        new File(dstPathForViewer).mkdirs();
      }
    }

    // 紛らわしいが、これ↓はEPUB3ファイルそのものの出力先ではなく、EPUB3変換の元になる分割された青空文庫テキストの出力先
    String dstPathForEPUB3 = srcFile.getParent();
    Integer volumeLength = Integer.parseInt(individualProps.getProperty("VolumeLength"));
    // TODO volumeLength値の正当性検査
    int length = aozoraBook.getLength();
    if (volumeLength == null || volumeLength == 0) {
      // "一巻あたりの文字数上限が設定されていません"
    } else if ((length / volumeLength) > 20) {
      // TODO "設定されている一巻あたりの文字数に従うと、全#{}巻になります。続行しますか？"
    }
    boolean forceChapterwise = individualProps.getPropertiesAsBoolean("SplitChapterWise");
    boolean allowSingleEmptyLine = individualProps.getPropertiesAsBoolean("AllowSingleEmptyLines");
    int successiveEmptyLinesLimit =
        Integer.parseInt(individualProps.getProperty("SuccessiveEmptyLinesLimit"));

    // 分割実行
    ArrayList<ArrayList<File>> listOfLists = aozoraBook.split(dstPathForViewer, dstPathForEPUB3,
        volumeLength, forceChapterwise, flagOutputForViewer, flagOutputForEPUB3,
        allowSingleEmptyLine, successiveEmptyLinesLimit);

    // EPUB3ファイル出力先の設定
    String dstPath = props.getProperty("EPUB3DstPath");
    if (dstPath == null) {
      LogAppender.println("EPUBファイル出力先が指定されていません。処理を中止します");
      return null;
    }
    if (props.getPropertiesAsBoolean("UseNovelwiseDirEPUB3")) {
      dstPath = dstPath + File.separator + novelID;
      if (new File(dstPath).exists()) {
        if (!new File(dstPath).isDirectory()) {
          LogAppender.println("指定されたEPUB3ファイル出力先ディレクトリと同名のファイルが既に存在します。処理を中止します");
          return null;
        }
      } else {
        new File(dstPath).mkdirs();
      }
    }

    ArrayList<File> listSrcs = listOfLists.get(0);
    ArrayList<File> listAozora = listOfLists.get(1);
    ArrayList<File> listEpub3 = new ArrayList<>();

    // 青空文庫テキストからEPUB3ファイルへの変換実行
    for (File splittedSrcFile : listSrcs) {

      if (splittedSrcFile != null && splittedSrcFile.isFile()) {
        listEpub3.add(convertAozoraToEpub3(splittedSrcFile, dstPath));
      }

      // 再変換のときはnovelList, novelMeta, CSVファイルの更新は必要ないはず(ここに書いてあったのを消した)

    }

    // ビューワ閲覧用青空文庫TXTファイルリストの書き出し
    writeFileList(listAozora, "Viewer", novelMeta, props);

    // EPUBファイルリストの書き出し
    writeFileList(listEpub3, "EPUB3", novelMeta, props);

    return null;
  }

  /**
   * EPUBファイルまたはビューワ閲覧用青空文庫TXTのリストを書き出す
   *
   * @param listFiles
   * @param type "EPUB3" または "Viewer" でなければならない
   * @param novelID
   * @param props
   * @throws IllegalArgumentException
   * @throws IOException
   */
  private void writeFileList(ArrayList<File> listFiles, String type, NovelMeta novelMeta,
      Properties props) throws IllegalArgumentException, IOException {
    String fileName = "";
    if (type.equals("EPUB3")) {
      fileName = "EpubFiles.txt";
    } else if (type.equals("Viewer")) {
      fileName = "viewerFiles.txt";
    } else {
      throw new IllegalArgumentException("引数 String \"type\" は \"EPUB3\" または \"Viewer\" でなければならない");
    }
    String novelWiseDstPath =
        Utils.getNovelWiseDstPath(novelMeta.url, props.getProperty("CachePath"));
    File listFile = new File(novelWiseDstPath + fileName);
    FileOutputStream fos = new FileOutputStream(listFile);
    OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
    BufferedWriter bw = new BufferedWriter(osw);
    if (listFiles.size() > 0) {
      for (File outputFile : listFiles) {
        String line = outputFile.getName();
        bw.write(line);
        bw.newLine();
      }
    }
    bw.close();

  }

  /**
   * NovelListの内容をCSVファイルに書き出す MacOSなら平気なのだがWindowsではopenCSVのCSVWriterをそのまま使うと文字化けが起きるので、
   * java.io.OutputStreamWriterを挟み、OutputStreamWriterに文字コードを指定することで文字化けを回避
   */
  private void writeCSV(File csvFile, NovelList novelList) throws IOException {
    CSVWriter csvWriter;
    fos = new FileOutputStream(csvFile);
    Writer writer = new OutputStreamWriter(fos, "UTF-8");
    csvWriter = new CSVWriter(writer, ',', '"', "\n");
    List<String[]> outStrList = new ArrayList<>();
    String[] header = {"novel ID", "author", "title", "chapters", "last updated", "check flag",
        "URL", "author ID"};
    outStrList.add(header);
    for (String novelID : novelList.novelMetaMap.keySet()) {
      NovelMeta novelMeta = novelList.novelMetaMap.get(novelID);
      String[] row = {novelMeta.novelID, novelMeta.author, novelMeta.title,
          novelMeta.numSections.toString(), novelMeta.lastUpdate, novelMeta.checkFlag.toString(),
          novelMeta.url, novelMeta.authorID};
      outStrList.add(row);
    }
    csvWriter.writeAll(outStrList);
    csvWriter.close();
  }

  /**
   * ダブルクリックで列幅調節機能＋αを盛り込んだ改変版TableHeader
   * http://www.ne.jp/asahi/hishidama/home/tech/java/swing/JTable.html
   */
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
     * テーブルカラムをデータの幅に合わせる.
     *
     * @param vc 表示列番号
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

  ////////////////////////////////////////////////////////////////
  // 変換履歴
  ////////////////////////////////////////////////////////////////
  /** 変換履歴格納用 最大255件 */
  LinkedHashMap<String, BookInfoHistory> mapBookInfoHistory =
      new LinkedHashMap<String, BookInfoHistory>() {
        private static final long serialVersionUID = 1L;

        @SuppressWarnings("rawtypes")
        protected boolean removeEldestEntry(Map.Entry eldest) {
          return size() > 256;
        }
      };

  // 以前の変換情報取得
  BookInfoHistory getBookInfoHistory(BookInfo bookInfo) {
    String key = bookInfo.srcFile.getAbsolutePath();
    if (bookInfo.textEntryName != null)
      key += "/" + bookInfo.textEntryName;
    return mapBookInfoHistory.get(key);
  }

  void setBookInfoHistory(BookInfo bookInfo) {
    String key = bookInfo.srcFile.getAbsolutePath();
    if (bookInfo.textEntryName != null)
      key += "/" + bookInfo.textEntryName;
    mapBookInfoHistory.put(key, new BookInfoHistory(bookInfo));
  }

}
