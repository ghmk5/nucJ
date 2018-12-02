package com.github.ghmk5.info;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

import com.opencsv.CSVReader;

public class NovelList {

  public HashMap<String, NovelMeta> novelMetaMap; // データテーブル本体 キーは作品ID(全小文字)
  String[] tableHeader; // JTableに使うヘッダ CSVヘッダの左側5列 novel
                        // ID,author,title,chapters,last updated

  // CSVファイルを引数にとるコンストラクタ
  // UTF-8 改行LF カンマ区切り 引用符" ファイルの最初の行がヘッダであると仮定
  // カラムは novel ID,author,title,chapters,last updated,check flag,URL,author ID
  public NovelList(File csvFile) throws IOException {
    novelMetaMap = new HashMap<>();
    tableHeader = new String[5];
    InputStream inputStream = new FileInputStream(csvFile);
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
    CSVReader csvReader = new CSVReader(inputStreamReader, ',', '"', 0);
    String[] csvHeader = csvReader.readNext();
    String[] line;
    HashMap<String, String> hashMap;
    int i;
    while ((line = csvReader.readNext()) != null) {
      hashMap = new HashMap<>();
      for (i = 0; i < csvHeader.length; i++) {
        hashMap.put(csvHeader[i], line[i]);
      }

      String novelID = hashMap.get("novel ID");
      if (novelID.equals("") || novelID == null) {
        String urlString = hashMap.get("URL");
        String[] strings = urlString.split("/", 0);
        novelID = strings[strings.length - 1];
      }

      hashMap.put("novel ID", novelID);
      NovelMeta novelMeta = new NovelMeta(hashMap);

      this.novelMetaMap.put(novelID, novelMeta);

    }
    for (i = 0; i < 5; i++) {
      this.tableHeader[i] = csvHeader[i];
    }
    csvReader.close();
  }

  // 作品IDをキー、データの配列を値とするハッシュと、ヘッダ行のArrayListを引数にとるコンストラクタ
  public NovelList(HashMap<String, ArrayList<Object>> dataMap, String[] header) {
    // TODO これがなんで必要なのかわからなくなった
  }

  // エントリを追加するメソッド
  public void addEntry(NovelMeta novelMeta) {
    this.novelMetaMap.put(novelMeta.novelID, novelMeta);
  }

  // エントリを削除するメソッド
  public void delEntry(String novelID) {
    this.novelMetaMap.remove(novelID);
  }

  // novelMetaMapを元にDefaultTableModelを生成して返すメソッド
  public DefaultTableModel getTableModel() {
    DefaultTableModel tableModel = new DefaultTableModel(this.tableHeader, 0);
    for (String novelID : this.novelMetaMap.keySet()) {
      NovelMeta novelMeta = this.novelMetaMap.get(novelID);
      String[] line = { novelMeta.novelID, novelMeta.author, novelMeta.title, novelMeta.numSections.toString(),
          novelMeta.lastUpdate };
      tableModel.addRow(line);
    }
    return tableModel;
  }

}
