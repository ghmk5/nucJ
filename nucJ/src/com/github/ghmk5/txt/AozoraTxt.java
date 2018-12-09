package com.github.ghmk5.txt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/** 青空文庫テキストの本文およびタイトル・著者名・章・節などのメタ情報を含んだオブジェクト */
public class AozoraTxt {

  BufferedReader bufferedReader;

  String title;
  String author;
  String summary;
  ArrayList<AozoraChapter> listOfChapters;
  // ArrayList<AozoraSection> listOfSections;

  /** 一行目タイトル 二行目著者名 最終行［＃改ページ］\n からなるタイトル記述部分 */
  ArrayList<String> titlePage;

  /**
   * 青空文庫テキストの本文およびタイトル・著者名・章・節などのメタ情報を含んだオブジェクト
   * 
   * @param aozoraTxtFile
   *          WebAozoraConverterが生成したconverted.txt
   * @param charCode
   *          InputStreamReaderに与える文字コード識別子
   * @throws IOException
   */
  public AozoraTxt(File aozoraTxtFile, String charCode) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(aozoraTxtFile);
    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charCode);

    this.bufferedReader = new BufferedReader(inputStreamReader);

    // 第一行からタイトル、第二行から著者名を取得
    this.title = bufferedReader.readLine();
    this.title = StringUtils.chomp(this.title);
    this.author = bufferedReader.readLine();
    this.author = StringUtils.chomp(this.author);

    // 最初の改ページまでの部分から概要を取得
    Pattern patternPageBreak = Pattern.compile("^［＃改ページ］$");
    Matcher matcherPageBreak;
    Pattern patternAozoraTag = Pattern.compile("^［＃");
    Matcher matcherAozoraTag;
    boolean flgBreakRead = false;
    this.summary = "";
    while (!flgBreakRead) {
      String line = bufferedReader.readLine();
      matcherPageBreak = patternPageBreak.matcher(line);
      matcherAozoraTag = patternAozoraTag.matcher(line);
      if (!matcherAozoraTag.find()) {
        if (!line.equals("")) {
          summary += line;
          summary += "\n";
        }
      }
      if (matcherPageBreak.find())
        flgBreakRead = true;
    }
    this.summary = this.summary.replaceAll("^\n+", "");
    this.summary = this.summary.replaceAll("\n+$", "");

    listOfChapters = new ArrayList<>();

    Pattern pChapterTagStarts = Pattern.compile("^［＃ここから大見出し］$");
    Matcher matcherChapterTagStarts;
    Pattern pChapterTagEnds = Pattern.compile("^［＃ここで大見出し終わり］$");
    Matcher matcherChapterTagEnds;
    Pattern pSectionStarts = Pattern.compile("^［＃ここから中見出し］$");
    Matcher matcherSectionTagStarts;
    Pattern pSectionEnds = Pattern.compile("^［＃ここで中見出し終わり］$");
    Matcher matcherSectionTagEnds;
    Pattern pDateTagStarts = Pattern.compile("^［＃ここから地から１字上げ］$");
    Matcher matcherDateStarts;
    Pattern pDateTagEnds = Pattern.compile("^［＃ここで字上げ終わり］$"); // このタグは挿絵・囲み記述の後ろにも出てくるが、既にfalseになっているフラグを更にfalseにするだけなので問題ない
    Matcher matcherDateEnds;
    Pattern pDatePublished = Pattern.compile("([0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}) +公開");
    Matcher matcherDatePublished;
    Pattern pDateUpdated = Pattern.compile("([0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}) +改稿");
    Matcher matcherDateUpdated;
    Pattern pTagLine = Pattern.compile("^［＃[^］]+］$");
    Matcher matcharTagLine;

    boolean flgChapterTitle = false;// 章タイトルを読んでいる間はtrue したがって最初はfalse
    boolean flgSectionTitle = false;// 節内を読んでいる間はtrue したがって最初はfalse
    boolean flgDateTag = false;

    int chapterIdx = 0;
    String chapterTitle;
    AozoraChapter currentChapter = new AozoraChapter(chapterIdx, "defaultChapterTitle");

    int sectionIdx = 0;
    String sectionTitle;
    AozoraSection currentSection = new AozoraSection(sectionIdx, chapterIdx, "defaultSectionTitle");

    String line;
    while ((line = bufferedReader.readLine()) != null) {
      matcherChapterTagStarts = pChapterTagStarts.matcher(line);
      matcherChapterTagEnds = pChapterTagEnds.matcher(line);
      matcherSectionTagStarts = pSectionStarts.matcher(line);
      matcherSectionTagEnds = pSectionEnds.matcher(line);
      matcherDateStarts = pDateTagStarts.matcher(line);
      matcherDateEnds = pDateTagEnds.matcher(line);
      matcharTagLine = pTagLine.matcher(line);
      matcherDatePublished = pDatePublished.matcher(line);
      matcherDateUpdated = pDateUpdated.matcher(line);

      if (matcherChapterTagStarts.find())
        flgChapterTitle = true;
      if (matcherSectionTagStarts.find())
        flgSectionTitle = true;
      if (matcherDateStarts.find())
        flgDateTag = true;

      if (flgChapterTitle) { // タグ行を含め、章タイトル部分を読んでいる
        if (!matcharTagLine.find()) { // タグ行ではない = 章タイトル
          listOfChapters.add(currentChapter);
          if (!currentSection.getTitle().equals("defaultSectionTitle")) {
            currentChapter.addSection(currentSection);
            currentChapter.setLength(currentChapter.getLength() + currentSection.getLength());
          }
          chapterIdx++;
          chapterTitle = StringUtils.chomp(line);
          currentChapter = new AozoraChapter(chapterIdx, chapterTitle);
          currentSection = new AozoraSection(sectionIdx, chapterIdx, "defaultSectionTitle");
        } else {
          // タグ行を読んでいる
        }
      } else if (flgSectionTitle) { // タグ行を含め、節タイトル部分を読んでいる
        if (!matcharTagLine.find()) { // タグ行ではない = 節タイトル
          if (!currentSection.getTitle().equals("defaultSectionTitle")) {
            currentChapter.addSection(currentSection);
            currentChapter.setLength(currentChapter.getLength() + currentSection.getLength());
          }
          sectionIdx++;
          sectionTitle = StringUtils.chomp(line);
          currentSection = new AozoraSection(sectionIdx, chapterIdx, sectionTitle);
        }
      } else if (flgDateTag) { // タグ行を含め、投稿改稿日時部分を読んでいる
        if (!matcharTagLine.find()) { // タグ行ではない = 投稿改稿日時の行
          if (matcherDatePublished.find())
            currentSection.setDatePublished(matcherDatePublished.group(1));
          if (matcherDateUpdated.find())
            currentSection.setDateUpdated(matcherDateUpdated.group(1));
        }
      } else {
        currentSection.addSentence(line);
        currentSection.setLength(currentSection.getLength() + line.length());
      }

      if (matcherChapterTagEnds.find())
        flgChapterTitle = false;
      if (matcherSectionTagEnds.find())
        flgSectionTitle = false;
      if (matcherDateEnds.find())
        flgDateTag = false;

    }

    currentChapter.addSection(currentSection);
    this.listOfChapters.add(currentChapter);

    bufferedReader.close();
  }

  /**
   * タイトルを返す
   * 
   * @return String title
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * 著者名を返す
   * 
   * @return String author
   */
  public String getAuthor() {
    return this.author;
  }

  /**
   * 章のリストを返す
   * 
   * @return ArrayList<AozoraChapter> listOfChapters
   */
  public ArrayList<AozoraChapter> getListOfChapters() {
    return this.listOfChapters;
  }

  /**
   * 分割した青空文庫テキストファイルを保存し、EPUB3変換用ファイルのリストを返す
   * 
   * @param dstPathForViewer
   *          ビューワー閲覧用ファイルの保存パス
   * @param dstPathForEPUB3
   *          EPUB3変換用ファイルの保存パス
   * @param volumeLength
   *          分割ファイルの文字数 この数字を超えないように節単位で分割される
   * @param forceChapterwise
   *          この値がtrueのとき、強制的に章単位で分割する
   * @param flagOutputForViewer
   *          この値がtrueのとき、ビューワー閲覧用ファイルを出力する
   * @param flagOutputForEPUB3
   *          この値がtrueのとき、EPUB3変換用ファイルを出力する
   * @param allowSingleEmptyLine
   *          この値がtrueのとき、本文中の単一の空行をそのまま残す パラグラフ毎に空行を挟んでいる作品用
   *          successiveEmptyLinesLimitと合わせて用いることにより、単一の空行は削除し、連続した空行を一つにまとめることができる
   * @param successiveEmptyLinesLimit
   *          連続した空行の最大数 この数を超えて連続する空行はこの数まで減らされる
   * @return EPUB3変換用ファイルのリスト
   * @throws IOException
   */
  public ArrayList<File> split(String dstPathForViewer, String dstPathForEPUB3, int volumeLength,
      boolean forceChapterwise, boolean flagOutputForViewer, boolean flagOutputForEPUB3, boolean allowSingleEmptyLine,
      int successiveEmptyLinesLimit) throws IOException {
    ArrayList<File> outFiles = new ArrayList<>();

    String fileNameForViewer;
    File dstFileForViewer;
    FileOutputStream fosForViewer;
    Writer writerForViewer;
    BufferedWriter bwForViewer;

    String fileNameForEPUB3;
    File dstFileForEPUB3;
    FileOutputStream fosForEPUB3;
    Writer writerForEPUB3;
    BufferedWriter bwForEPUB3;

    int volumeNumber = 1;

    // 「初期化されていない可能性がある」エラーを回避するのが難しいので、直接BufferedWriterに流し込むのではなく、
    // 一旦ArrayList<String>に流し込んでから、出力フラグに応じて書き出すようにする
    // -- BufferedWriterを初期化するとファイルが生成されるので、フラグがfalseなら出力しないという動作を実現する上で都合が悪い
    ArrayList<ArrayList<String>> volumesAsListForEPUB3 = new ArrayList<>();
    ArrayList<String> contentsAsListForEPUB3 = new ArrayList<>();
    ArrayList<ArrayList<String>> volumesAsListForViewer = new ArrayList<>();
    ArrayList<String> contentsAsListForViewer = new ArrayList<>();

    contentsAsListForEPUB3.add(this.titlePageForEPUB3(volumeNumber));
    contentsAsListForViewer.add(this.titlePageForViewer(volumeNumber));

    // ビューワー用出力に含めないタグ行を検出するためのパターンとマッチャ
    // Pattern pTagSectionLine = Pattern.compile("［＃区切り線］");
    // Matcher mTagSectionLine;
    Pattern pTagMinusIndentStart = Pattern.compile("［＃ここから.字上げ］");
    Matcher mTagMinusIndentStart;
    Pattern pTagMinusIndentEnd = Pattern.compile("［＃ここで字上げ終わり］");
    Matcher mTagMinusIndentEnd;
    Pattern pTagSetSmallStart = Pattern.compile("［＃ここから.段階小さな文字］");
    Matcher mTagSetSmallStart;
    Pattern pTagSetSmallEnd = Pattern.compile("［＃ここで小さな文字終わり］");
    Matcher mTagSetSmallEnd;

    int currentLength = 0;
    String dateLine = "";

    for (AozoraChapter aozoraChapter : this.getListOfChapters()) {

      if (aozoraChapter.length == 0) {
        continue; // 章分けがある場合のデフォルト章 空のはず
      } else if (!aozoraChapter.title.equals("defaultChapterTitle")) { // 章分けありの通常章なら章タイトルを書き込む
        contentsAsListForEPUB3.add("［＃ここから大見出し］\n" + aozoraChapter.title + "\n［＃ここで大見出し終わり］\n\n");
        contentsAsListForViewer
            .add("［＃ここから３字下げ］\n［＃ここから大見出し］\n" + aozoraChapter.title + "\n［＃ここで大見出し終わり］\n［＃ここで字下げ終わり］\n\n");
      } // 章分けなしの場合、章タイトルの書き込みをスキップする

      for (AozoraSection aozoraSection : aozoraChapter.getListOfSections()) {
        if (currentLength + aozoraSection.length > volumeLength) {
          //  規定容量に到達したvolumeをリストに追加・volumeインクリメント・新規volumeを生成
          volumesAsListForEPUB3.add(contentsAsListForEPUB3);
          contentsAsListForEPUB3 = new ArrayList<>();
          volumesAsListForViewer.add(contentsAsListForViewer);
          contentsAsListForViewer = new ArrayList<>();
          currentLength = 0;
          volumeNumber++;
          // 新規volumeの先頭にタイトルページを追加
          contentsAsListForEPUB3.add(this.titlePageForEPUB3(volumeNumber));
          contentsAsListForViewer.add(this.titlePageForViewer(volumeNumber));
        }
        currentLength += aozoraSection.length;

        // 節タイトルを追加
        contentsAsListForEPUB3.add("［＃ここから中見出し］\n" + aozoraSection.title + "\n［＃ここで中見出し終わり］\n\n");
        contentsAsListForViewer
            .add("\n［＃ここから５字下げ］\n［＃ここから中見出し］\n" + aozoraSection.title + "\n［＃ここで中見出し終わり］\n［＃ここで字下げ終わり］\n\n");

        // 投稿改稿日時を追加
        dateLine = "［＃ここから地から１字上げ］\n［＃ここから１段階小さな文字］\n" + aozoraSection.datePublished + " 公開";
        if (aozoraSection.getDateUpdated() != null)
          dateLine += "　" + aozoraSection.getDateUpdated() + " 改稿";
        dateLine += "\n［＃ここで小さな文字終わり］\n［＃ここで字上げ終わり］\n";
        contentsAsListForEPUB3.add(dateLine);

        dateLine = "［＃ここから地から１字上げ］\n" + aozoraSection.datePublished + " 公開";
        if (aozoraSection.getDateUpdated() != null)
          dateLine += "　" + aozoraSection.getDateUpdated() + " 改稿";
        dateLine += "\n［＃ここで字上げ終わり］\n\n";
        contentsAsListForViewer.add(dateLine);

        // 本文を追加
        for (String line : aozoraSection.getEmptyLinesTreatedBody(allowSingleEmptyLine, successiveEmptyLinesLimit)) {
          contentsAsListForEPUB3.add(line + "\n");

          // ビューワーに解釈できないタグの行を除く処理 字上げ 文字サイズ
          // TODO 区切り線はあとで字下げタグと一緒に置換したほうがきれいな仕上がりになると思う
          mTagMinusIndentStart = pTagMinusIndentStart.matcher(line);
          mTagMinusIndentEnd = pTagMinusIndentEnd.matcher(line);
          mTagSetSmallStart = pTagSetSmallStart.matcher(line);
          mTagSetSmallEnd = pTagSetSmallEnd.matcher(line);
          if (!mTagMinusIndentStart.find() && !mTagMinusIndentEnd.find() && !mTagSetSmallStart.find()
              && !mTagSetSmallEnd.find())
            contentsAsListForViewer.add(line + "\n");
        }
      }
    }
    volumesAsListForEPUB3.add(contentsAsListForEPUB3);
    volumesAsListForViewer.add(contentsAsListForViewer);

    volumeNumber = 1;
    if (flagOutputForEPUB3) {
      for (ArrayList<String> contentsAsList : volumesAsListForEPUB3) {
        fileNameForEPUB3 = "converted" + String.format("%02d", volumeNumber) + ".txt";
        if (!dstPathForEPUB3.endsWith("/"))
          dstPathForEPUB3 += "/";
        dstFileForEPUB3 = new File(dstPathForEPUB3 + fileNameForEPUB3);
        fosForEPUB3 = new FileOutputStream(dstFileForEPUB3);
        writerForEPUB3 = new OutputStreamWriter(fosForEPUB3, "UTF-8");
        bwForEPUB3 = new BufferedWriter(writerForEPUB3);
        for (String line : contentsAsList) {
          bwForEPUB3.write(line);
        }
        volumeNumber++;
        outFiles.add(dstFileForEPUB3);
        bwForEPUB3.close();
      }
    }

    volumeNumber = 1;
    if (flagOutputForViewer) {
      for (ArrayList<String> contentsAsList : volumesAsListForViewer) {
        fileNameForViewer = "[" + this.author + "] " + this.title + " " + String.format("%02d", volumeNumber) + ".txt";
        if (!dstPathForViewer.endsWith("/"))
          dstPathForViewer += "/";
        dstFileForViewer = new File(dstPathForViewer + fileNameForViewer);
        fosForViewer = new FileOutputStream(dstFileForViewer);
        writerForViewer = new OutputStreamWriter(fosForViewer, "UTF-8");
        bwForViewer = new BufferedWriter(writerForViewer);
        for (String line : contentsAsList) {
          bwForViewer.write(line);
        }
        volumeNumber++;
        bwForViewer.close();
      }
    }

    return outFiles;
  }

  public String titlePageForEPUB3(int volumeNumber) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.title);
    stringBuilder.append(" ");
    stringBuilder.append(String.format("%02d", volumeNumber));
    stringBuilder.append("\n");
    stringBuilder.append(this.author);
    stringBuilder.append("\n");
    if (volumeNumber == 1 && this.summary.length() > 0) {
      stringBuilder.append("\n［＃区切り線］\n\n［＃ここから２字下げ］\n［＃ここから２字上げ］\n");
      stringBuilder.append(this.summary);
      stringBuilder.append("\n［＃ここで字上げ終わり］\n［＃ここで字下げ終わり］\n\n［＃区切り線］\n\n");
    }
    stringBuilder.append("［＃改ページ］\n");
    return stringBuilder.toString();
  }

  public String titlePageForViewer(int volumeNumber) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("［＃ページの左右中央］\n［＃ここから２字下げ］\n［＃ここから大見出し］\n");
    stringBuilder.append(this.title);
    stringBuilder.append(" ");
    stringBuilder.append(String.format("%02d", volumeNumber));
    stringBuilder.append("\n");
    stringBuilder.append("［＃ここで字下げ終わり］\n\n");
    stringBuilder.append("［＃ここから地から２字上げ］\n");
    stringBuilder.append(this.author);
    stringBuilder.append("\n");
    stringBuilder.append("［＃ここで字上げ終わり］\n［＃ここで大見出し終わり］\n");
    stringBuilder.append("［＃改ページ］\n");
    if (volumeNumber == 1 && this.summary.length() > 0) {
      stringBuilder.append("\n［＃区切り線］\n\n［＃ここから２字下げ］\n");
      stringBuilder.append(this.summary);
      stringBuilder.append("\n［＃ここで字下げ終わり］\n\n［＃区切り線］\n\n");
    }
    stringBuilder.append("［＃改ページ］\n");
    return stringBuilder.toString();
  }

  /**
   * 章のリストをセットする
   * 
   * @param listOfChapters
   */
  public void setListOfChapters(ArrayList<AozoraChapter> listOfChapters) {
    this.listOfChapters = listOfChapters;
  }

  /** 青空文庫テキスト内の章 */
  public class AozoraChapter {

    int idx;
    int length;
    String title;
    ArrayList<AozoraSection> listOfSections;

    /**
     * @param idx
     *          章番号 0は章分けがない作品で使われる defaultChapter 通常の章は番号1から始まる
     *          したがって、章分けがある作品では第0章は空であり、章分けがない作品では全ての節が第0章に属する
     * @param title
     *          章タイトル defaultChapterの場合は"defaultChapterTitle"
     */
    public AozoraChapter(int idx, String title) {
      this.idx = idx;
      this.title = title;
      this.listOfSections = new ArrayList<>();
      this.length = 0;
    }

    /**
     * @param idx
     *          章番号 0は章分けがない作品で使われる defaultChapter 通常の章は番号1から始まる
     *          したがって、章分けがある作品では第0章は空であり、章分けがない作品では全ての節が第0章に属する
     * @param title
     *          章タイトル defaultChapterの場合は"defaultChapterTitle"
     * @param listOfSections
     *          ArrayList<AozoraSection>
     * 
     */
    public AozoraChapter(int idx, String title, ArrayList<AozoraSection> listOfSections) {
      this.idx = idx;
      this.title = title;
      this.listOfSections = listOfSections;
      this.length = 0;
    }

    public void addSection(AozoraSection section) {
      this.listOfSections.add(section);
    }

    public void setIdx(int idx) {
      this.idx = idx;
    }

    public void setLength(int length) {
      this.length = length;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public int getIdx() {
      return this.idx;
    }

    public int getLength() {
      return this.length;
    }

    public String getTitle() {
      return this.title;
    }

    public ArrayList<AozoraSection> getListOfSections() {
      return this.listOfSections;
    }

  }

  /** 青空文庫テキスト内の節(投稿単位) */
  public class AozoraSection {

    int idx;
    int length;
    int chapterIdx;
    String title;
    String datePublished;
    String dateUpdated;
    ArrayList<String> body;

    /**
     * @param idx
     *          節番号 0はインスタンス作成時に自動的に生成されるdefaultSectionに与えられる 通常の節は番号1から始まる
     * @param chapterIdx
     *          この節が属する章の番号
     * @param title
     *          節タイトル defaultSectionの場合は"defaultSectionTitle"
     */
    public AozoraSection(int idx, int chapterIdx, String title) {
      this.idx = idx;
      this.length = 0;
      this.chapterIdx = chapterIdx;
      this.title = title;
      this.body = new ArrayList<>();
    }

    /**
     * @param idx
     *          節番号 0はインスタンス作成時に自動的に生成されるdefaultSectionに与えられる 通常の節は番号1から始まる
     * @param chapterIdx
     *          この節が属する章の番号
     * @param title
     *          節タイトル defaultSectionの場合は"defaultSectionTitle"
     * @param body
     *          パラグラフのリストとしての本文 節タイトルを含まない 各パラグラフは改行コードを含まない
     */
    public AozoraSection(int idx, int chapterIdx, String title, ArrayList<String> body) {
      this.idx = idx;
      this.length = 0;
      this.chapterIdx = chapterIdx;
      this.title = title;
      this.body = body;
    }

    public void addSentence(String sentence) {
      this.body.add(sentence);
    }

    public void setIdx(int idx) {
      this.idx = idx;
    }

    public int getIdx() {
      return this.idx;
    }

    public void setLength(int length) {
      this.length = length;
    }

    public int getLength() {
      return this.length;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getTitle() {
      return this.title;
    }

    public void setBodyAsList(ArrayList<String> body) {
      this.body = body;
    }

    public ArrayList<String> getBodyAsList() {
      return this.body;
    }

    /**
     * 空行調整をした本文を返す
     * 
     * @param allowSingleEmptyLine
     *          この値がtrueのとき、本文中の単一の空行をそのまま残す パラグラフ毎に空行を挟んでいる作品用
     *          successiveEmptyLinesLimitと合わせて用いることにより、単一の空行は削除し、連続した空行を一つにまとめることができる
     * @param successiveEmptyLinesLimit
     *          連続した空行の最大数 この数を超えて連続する空行はこの数まで減らされる
     * @return 要素として行を持つArrayList 要素末尾に改行コードは付かない
     */
    public ArrayList<String> getEmptyLinesTreatedBody(boolean allowSingleEmptyLine, int successiveEmptyLinesLimit) {

      ArrayList<String> emptyLinesTreatedBody = new ArrayList<>();

      int successiveEmptyLinesCount = 0; // ポインタ直前までの連続した空行の数

      Pattern patternEmptyLine = Pattern.compile("^[　 \t]+$");
      Matcher matcher;

      for (String line : this.getBodyAsList()) {
        matcher = patternEmptyLine.matcher(line);
        if (line.equals("") || matcher.find()) {
          if (allowSingleEmptyLine) {
            if (successiveEmptyLinesCount == 0) {
              emptyLinesTreatedBody.add("");
            } else if (successiveEmptyLinesCount < successiveEmptyLinesLimit) {
              emptyLinesTreatedBody.add("");
            }
          } else {
            if (successiveEmptyLinesCount > 0 && successiveEmptyLinesCount < successiveEmptyLinesLimit) {
              emptyLinesTreatedBody.add("");
            } else if (successiveEmptyLinesCount == successiveEmptyLinesLimit) {
              emptyLinesTreatedBody.add("");
            }
          }
          successiveEmptyLinesCount++;
        } else {
          if (successiveEmptyLinesCount > 1 && successiveEmptyLinesCount <= successiveEmptyLinesLimit
              && !allowSingleEmptyLine) {
            emptyLinesTreatedBody.add(""); // 単一空行を許さない場合で連続空行が許容数以下のとき、最初の空行が削られてるので補完
          }
          emptyLinesTreatedBody.add(line); // 文字列を出力
          successiveEmptyLinesCount = 0;
        }
      }

      return emptyLinesTreatedBody;
    }

    public String getDatePublished() {
      return this.datePublished;
    }

    public void setDatePublished(String datePublished) {
      this.datePublished = datePublished;
    }

    public String getDateUpdated() {
      return this.dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
      this.dateUpdated = dateUpdated;
    }

  }

}
