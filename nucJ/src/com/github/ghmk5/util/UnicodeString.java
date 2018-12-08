package com.github.ghmk5.util;

/**
 * unicode文字列の桁揃えを行うクラス インスタンスなし
 */
public class UnicodeString {

  /**
   * @param srcString
   *          unicode文字列 ASCIIなら問題ないが、SJISやEUCJだと何が起こるかわからない
   * @param flaggedWidth
   *          揃える桁数 正で右詰め 負で左詰め
   * @return 半角スペースで桁揃えされた文字列
   */
  public static String format(String srcString, int flaggedWidth) {
    String formattedString = null;

    int width = Math.abs(flaggedWidth);

    int length = 0;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < srcString.length(); i++) {
      char c = srcString.charAt(i);
      // ( 英数字 ) ( \ 記号 ) ( ~ 記号 ) ( 半角カナ )
      if ((c <= '\u007e') || (c == '\u00a5') || (c == '\u203e') || (c >= '\uff61' && c <= '\uff9f')) {
        length++;
      } else { // その他 (全角)
        length += 2;
      }
      if (length <= width) {
        sb.append(c);
      } else {
        break;
      }
    }
    String truncatedString = sb.toString();

    sb = new StringBuilder();
    for (int i = 0; i < (width - length); i++) {
      sb.append(" ");
    }
    String spaces = sb.toString();

    if (flaggedWidth > 0) {
      formattedString = spaces + truncatedString;
    } else {
      formattedString = truncatedString + spaces;
    }

    return formattedString;
  }

}
