package com.github.ghmk5.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ファイル名をチェックし、ファイル名に使えない文字を置換・削除するクラス
 *
 */
public class FileName {

  static Pattern pForbiddenChars;
  static Matcher mForbiddenChars;
  static HashMap<String, String> multiByteReplacementMap;
  // static HashMap<string, String>

  static {
    pForbiddenChars = Pattern.compile("[\\\\¥:*?\"<>|/]");

    multiByteReplacementMap = new HashMap<>();
    multiByteReplacementMap.put("\\", "＼");
    multiByteReplacementMap.put("¥", "￥");
    multiByteReplacementMap.put(":", "：");
    multiByteReplacementMap.put("*", "＊");
    multiByteReplacementMap.put("?", "？");
    multiByteReplacementMap.put("\"", "”");
    multiByteReplacementMap.put("<", "＜");
    multiByteReplacementMap.put(">", "＞");
    multiByteReplacementMap.put("|", "｜");
    multiByteReplacementMap.put("/", "／");

  }

  // インスタンスなし
  public FileName() {
  }

  /**
   * 所与の文字列fileNameに、Windowsでファイル名に使えない文字が含まれるかどうかを調べる
   * 使用不可文字が含まれないときはtrue、含まれるときはfalseを返す
   * 
   * @param fileName
   * @return
   */
  public static boolean check(String fileName) {
    mForbiddenChars = pForbiddenChars.matcher(fileName);
    return !mForbiddenChars.find();
  }

  /**
   * 所与の文字列fileNameにWindowsでファイル名に使えない文字が含まれる場合、 使用不可文字を対応する多バイト文字に置き換えて返す (例) \
   * -> ＼、¥ -> ￥
   * 
   * @param fileName
   * @return
   */
  public static String replaceToMultiByte(String fileName) {
    String charactor;
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < fileName.length(); i++) {
      charactor = String.valueOf(fileName.charAt(i));
      mForbiddenChars = pForbiddenChars.matcher(charactor);
      if (mForbiddenChars.find()) {
        stringBuilder.append(multiByteReplacementMap.get(charactor));
      } else {
        stringBuilder.append(charactor);
      }
    }
    return stringBuilder.toString();
  }

  /**
   * 所与の文字列fileNameにWindowsでファイル名に使えない文字が含まれる場合、 使用不可文字を取り除いて返す
   * 使用不可文字を取り除いた結果、返すべき文字列の長さが0になった場合、あるいは拡張子だけ残った場合はIllegalArgumentExceptionを出す
   * 
   * @param fileName
   * @return
   * @throws IllegalArgumentException
   */
  public static String removeIllegalChar(String fileName) throws IllegalArgumentException {

    String newFileName;
    String charactor;
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < fileName.length(); i++) {
      charactor = String.valueOf(fileName.charAt(i));
      mForbiddenChars = pForbiddenChars.matcher(charactor);
      if (!mForbiddenChars.find())
        stringBuilder.append(charactor);
    }
    newFileName = stringBuilder.toString();
    if (newFileName.length() == 0 || (!fileName.startsWith(".") && newFileName.startsWith(".")))
      throw new IllegalArgumentException("filename must contain at least a legal charactor.");
    return stringBuilder.toString();
  }

}
