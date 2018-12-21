package com.github.ghmk5.util;

import java.io.File;

import com.github.hmdev.util.CharUtils;

/**
 * 汎用性が低く雑多なメソッドを格納するクラス
 *
 * @author mk5
 *
 */
public class Utils {

  public Utils() {
  }

  /**
   * cachePathとURLから作品個別のキャッシュパスを生成する
   *
   * @param urlString
   * @param cachePathString
   * @return 絶対パス(文字列) 末尾にはセパレータがつく
   */
  public static String getNovelWiseDstPath(String urlString, String cachePathString) {
    String dstPath;

    String urlFilePath = CharUtils.escapeUrlToFile(urlString.substring(urlString.indexOf("//") + 2));
    String urlParentPath = urlFilePath;
    File cachePath = new File(cachePathString);

    boolean isPath = false;
    if (urlFilePath.endsWith("/")) {
      isPath = true;
      urlFilePath += "index.html";
    } else
      urlParentPath = urlFilePath.substring(0, urlFilePath.lastIndexOf('/') + 1);

    dstPath = cachePath.getAbsolutePath() + "/";
    if (isPath)
      dstPath += urlParentPath;
    else
      dstPath += urlFilePath + "_converted/";

    // Windows環境だと\と/が混在するので統一 -- Fileのコンストラクタはセパレータの混在を許容する模様
    dstPath = new File(dstPath).toString();
    if (!dstPath.endsWith(File.separator))
      dstPath += File.separator;

    return dstPath;
  }

}
