package com.github.ghmk5.util;

/**
 * unicode文字列の桁揃えを行うクラス インスタンスなし
 */
public class UnicodeString {

  /**
   * 1バイト文字の一文字あたりの表示桁数を1、多バイト文字の一文字あたりの表示桁数を2とみなした上でunicode文字列の桁揃えを行う
   * flaggedWidthの正負に従い、1バイトスペースで右ないし左詰めに桁揃えされた文字列を返す
   * srcStringの表示幅がflaggedWidthの絶対値を超えるとき、flagTruncateがtrueであればflaggedWidthに収まる幅まで切り詰められる
   * 
   * @param srcString
   *          unicode文字列 ASCIIなら問題ないが、SJISやEUCJだと何が起こるかわからない
   * @param flaggedWidth
   *          揃える桁数 正で右詰め 負で左詰め
   * @param flagTruncate
   *          falseであれば切り詰めが行われず、srcStringの表示幅がflaggedWidthの絶対値を超える場合は桁揃えも行われない
   * @return 半角スペースで桁揃えされた文字列
   */
  public static String format(String srcString, int flaggedWidth, boolean flagTruncate) {
    String formattedString = null;

    // 収めるべき桁数の絶対値
    int totalWidth = Math.abs(flaggedWidth);

    // flagTruncateがtureであり、かつ文字幅が収めるべき桁数より大きい場合、切り詰める
    String outString = srcString;
    if (flagTruncate) {
      if (UnicodeString.getWidth(srcString) > totalWidth) {
        outString = UnicodeString.truncate(srcString, totalWidth);
      }
    }

    // 与えられた桁数を埋める長さの空白を組み立てる
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < totalWidth - (UnicodeString.getWidth(outString)); i++) {
      sb.append(" ");
    }
    String spaces = sb.toString();

    if (flaggedWidth > 0) {
      formattedString = spaces + outString;
    } else {
      formattedString = outString + spaces;
    }

    return formattedString;
  }

  /**
   * 全角半角混じりのunicode文字列の幅(半角文字での桁数)を計算する
   * 
   * @param string
   *          幅を知りたい文字列
   * @return int 半角文字での桁数
   */
  public static int getWidth(String string) {
    int width = 0;
    for (int i = 0; i < string.length(); i++) {
      char c = string.charAt(i);
      // ( 英数字 ) ( \ 記号 ) ( ~ 記号 ) ( 半角カナ )
      if ((c <= '\u007e') || (c == '\u00a5') || (c == '\u203e') || (c >= '\uff61' && c <= '\uff9f')) {
        width++;
      } else { // その他 (全角)
        width += 2;
      }
    }
    return width;
  }

  /**
   * 全角半角混じりのunicode文字列を、半角文字での桁数で与えられる幅まで切り詰める
   * stringの表示桁数がwidthより小さい場合、stringをそのまま返す
   * 文字単位で切り詰めると1バイト文字2桁分づつでしか幅を変えられない場合があるため、
   * stringの表示桁数がwidthより大きい(=切り詰めが行われる)場合でも、戻り値の表示桁数がwidthと等しくならず、1桁短いことがある
   * 
   * @param string
   * @param width
   * @return
   */
  public static String truncate(String string, int width) {
    String truncatedString;
    if (UnicodeString.getWidth(string) > width) {
      StringBuilder sb = new StringBuilder();
      int j = 0;
      while (UnicodeString.getWidth(sb.toString()) < width) {
        sb.append(string.charAt(j));
        j++;
      }
      truncatedString = sb.toString();
      if (UnicodeString.getWidth(truncatedString) > width)
        truncatedString = truncatedString.substring(0, truncatedString.length() - 1);
    } else {
      truncatedString = string;
    }
    return truncatedString;
  }

}
