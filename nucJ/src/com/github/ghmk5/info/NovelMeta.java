package com.github.ghmk5.info;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

// なろうの小説タイトルページをJsoupでパースしたDocumentを受け取ってメタデータを取得し、保持する
public class NovelMeta {

  // タイトルページURL
  public String url;

  // タイトル
  public String title;

  // 作品ID
  public String novelID;

  // 著者
  public String author;

  // 著者ID
  public String authorID;

  // 章分けの有無
  public Boolean isChaptered;

  // 節の数
  public Integer numSections;

  // 最新部分の追加日時 = Web上での更新日時
  public String lastUpdate;

  // ローカルコピーの更新日時
  public String lastLocalUpdate;

  // 更新チェックの可否
  public Boolean checkFlag;

  // 章通し番号をキー、章タイトルを値にとるマップ
  public HashMap<Integer, String> chapterTitleMap = new HashMap<>();

  // 章通し番号をキー、属する節の通し番号のリストを値にとるマップ
  public HashMap<Integer, ArrayList<Integer>> sectionIdxMap = new HashMap<>();

  // 節通し番号をキー、節タイトル, URL, 投稿日時, 改稿日時の配列を値にとるマップ
  public HashMap<Integer, String[]> sectionInfoMap = new HashMap<>();

  // JsoupのDocumentを引数にとるコンストラクタ
  public NovelMeta(Document document, String urlString) throws IOException {

    this.url = urlString;

    this.novelID = new URL(url).getPath().replaceAll("\\/", "");

    Element pTitle = document.select("p.novel_title").first();
    this.title = pTitle.text();

    Element divAuthor = document.select("div.novel_writername").first();
    this.author = divAuthor.text().replaceAll("^作者：", "");

    this.authorID = new URL(divAuthor.select("a").first().attributes().get("href")).getPath().replaceAll("\\/", "");

    this.checkFlag = true;

    // 章分けの有無 デフォルトは偽で、章タイトルを検出したら真にする
    this.isChaptered = false;

    // 章番号 章分けありなら1からスタート、なしなら全話が第0章に所属
    int chapterIdx = 0;

    // 節通し番号
    int sectionIdx = 0;

    // 節の数
    this.numSections = 0;

    // 日付文字列取得用のパターン
    Pattern pDate = Pattern.compile("\\d{4}\\/\\d{2}\\/\\d{2} \\d{2}:\\d{2}");

    // 改稿検出用のパターン
    Pattern pUpdate = Pattern.compile("（改）");

    // 各話目次部分を取得 div class = "index_box" で記述される
    Element index = document.select("div.index_box").first();

    for (Element e : index.children()) {
      // 章は div class="chapter_title" で記述される
      if (e.tagName() == "div") {
        String chapterTitle = e.text();
        chapterIdx++;
        chapterTitleMap.put(chapterIdx, chapterTitle);
        this.isChaptered = true;

        // 節は dl class="novel_sublist2" で記述される
      } else if (e.tagName() == "dl") {
        sectionIdx++;
        numSections++;
        // 節タイトル
        String sectionTitle = e.select("dd").text();

        // 節URL 相対パスで書かれているので絶対パスに変換して取得する
        String sectionUrl = e.select("a").first().attributes().get("href");
        sectionUrl = new URL(new URL(url), sectionUrl).toString();

        String str = e.select("dt.long_update").text();
        Matcher mDate = pDate.matcher(str);

        // 節投稿日時
        String published = "yyyy/mm/dd hh:mm"; // 投稿日時を拾い損なったときの初期値
        if (mDate.find()) {
          published = mDate.group();
        }
        Matcher mUpdate = pUpdate.matcher(str);

        // 節改稿日時
        String updated = "yyyy/mm/dd hh:mm"; // 改稿日時を拾い損なったときの初期値
        if (mUpdate.find()) {
          Element span = e.select("span").first();
          updated = span.attributes().get("title");
        }

        // 節情報をまとめた配列
        String[] sectionInfo = { sectionTitle, sectionUrl, published, updated };
        sectionInfoMap.put(sectionIdx, sectionInfo);
        ArrayList<Integer> sectionIdxList = sectionIdxMap.get(chapterIdx);
        if (sectionIdxList == null) {
          sectionIdxList = new ArrayList<>();
        }
        sectionIdxList.add(sectionIdx);
        sectionIdxMap.put(chapterIdx, sectionIdxList);
        this.lastUpdate = published; // 全てのセクションで代入されるが、その結果最後のセクションの投稿日時が全体の最新更新日時として残るはず
      }
    }

  }

  // ハッシュマップを引数に取るコンストラクタ
  // ハッシュマップのキーはCSVファイルのヘッダから novel ID,author,title,chapters,last updated,check
  // flag,URL,author ID
  public NovelMeta(HashMap<String, String> hashMap) {
    this.url = hashMap.get("URL");
    this.title = hashMap.get("title");
    this.novelID = hashMap.get("novel ID");
    this.author = hashMap.get("author");
    this.authorID = hashMap.get("author ID");
    this.lastUpdate = hashMap.get("last updated");
    this.checkFlag = Boolean.valueOf(hashMap.get("check flag"));
    this.isChaptered = null;
    try {
      this.numSections = Integer.parseInt(hashMap.get("chapters"));
    } catch (Exception e) {
      this.numSections = 0;
    }
    this.chapterTitleMap = null;
    this.sectionIdxMap = null;
    this.sectionInfoMap = null;
  }

  public static void main(String[] args) throws IOException {

    String url = "https://ncode.syosetu.com/n2710db/";
    // String url = "https://ncode.syosetu.com/n8725k/";

    // 作品情報と目次を出力してみるテスト
    Document document = Jsoup.connect(url).get();
    NovelMeta novelMeta = new NovelMeta(document, url);
    System.out.println("URL: " + novelMeta.url);
    System.out.println("タイトル: " + novelMeta.title);
    System.out.println("作品ID: " + novelMeta.novelID);
    System.out.println("著者: " + novelMeta.author);
    System.out.println("著者ID: " + novelMeta.authorID);

    for (Integer chapterIdx : novelMeta.sectionIdxMap.keySet()) {
      String chapterTitle = novelMeta.chapterTitleMap.get(chapterIdx);
      if (chapterTitle != null) {
        System.out.println(chapterTitle);
      } else {
        System.out.println("(章分けなし)");
      }
      ArrayList<Integer> sectionIdxs = novelMeta.sectionIdxMap.get(chapterIdx);
      for (Integer sectionIdx : sectionIdxs) {
        String[] sectionInfo = novelMeta.sectionInfoMap.get(sectionIdx);
        String sectionTitle = sectionInfo[0];
        String sectionUrl = sectionInfo[1];
        String published = sectionInfo[2];
        String updated = sectionInfo[3];
        String outStr = " " + sectionTitle + " URL:" + sectionUrl + " 初出:" + published;
        if (!updated.equals("yyyy/mm/dd hh:mm")) {
          outStr += " 改稿: ";
          outStr += updated;
        }
        System.out.println(outStr);
      }
    }
    //
    // // 最初の節を取得してみるテスト
    // String sectionUrl = novelMeta.sectionInfoMap.get(1)[1];
    // System.out.println(sectionUrl);
    // Document firstSection = Jsoup.connect(sectionUrl).get();
    //
  }

}
