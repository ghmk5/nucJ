package com.github.ghmk5.txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/** 青空文庫テキストの本文およびタイトル・著者名・章・節などのメタ情報を含んだオブジェクト */
public class AozoraTxt {

  BufferedReader bufferedReader;

  String title;
  String author;
  ArrayList<AozoraChapter> listOfChapters;
  // ArrayList<AozoraSection> listOfSections;

  /** 一行目タイトル 二行目著者名 最終行［＃改ページ］\n からなるタイトル記述部分 */
  ArrayList<String> titlePage;

  /**
   * @param aozoraTxtFile
   *          WebAozoraConverterが生成したconverted.txt
   * @param charCode
   *          InputStreamReaderに与える文字コード判別子
   * @throws IOException
   */
  public AozoraTxt(File aozoraTxtFile, String charCode) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(aozoraTxtFile);
    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charCode);

    this.bufferedReader = new BufferedReader(inputStreamReader);

    titlePage = new ArrayList<>();

    // 最初の［＃改ページ］までをタイトルページとして読み出す
    this.title = bufferedReader.readLine();
    titlePage.add(this.title);
    this.title = StringUtils.chomp(this.title);
    this.author = bufferedReader.readLine();
    titlePage.add(this.author);
    this.author = StringUtils.chomp(this.author);

    Pattern patternPageBreak = Pattern.compile("^［＃改ページ］$");
    String line;
    boolean flgBreakRead = false;
    while (!flgBreakRead) {
      line = bufferedReader.readLine();
      Matcher matcherPageBreak = patternPageBreak.matcher(line);
      titlePage.add(line);
      if (matcherPageBreak.find())
        flgBreakRead = true;
    }

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
    Pattern pDateTagEnds = Pattern.compile("^［＃ここで字上げ終わり］$");
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
      } else if (flgDateTag) {
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

  public String getTitle() {
    return this.title;
  }

  public String getAuthor() {
    return this.author;
  }

  public ArrayList<AozoraChapter> getListOfChapters() {
    return this.listOfChapters;
  }

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
     *          章番号 0は章分けがない作品で使われる defaultChapterに与えられる 通常の章は番号1から始まる
     * @param title
     *          章タイトル defaultChapterの場合は"defaultChapterTitle"
     * @param listOfSections
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
