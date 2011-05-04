package sample.camera.utils;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文字関係のユーティリティ
 * 
 * @version 1.00
 */
public class TextUtil {

	/**
	 * 全角カタカナの文字コード開始コード
	 */
	private static final int UNICODE_ZEN_KATAKANA_START = 0x30A0;

	/**
	 * 全角カタカナの文字コード終了コード
	 */
	private static final int UNICODE_ZEN_KATAKANA_END = 0x30FA;

	/**
	 * 全角平仮名の文字コード開始コード
	 */
	private static final int UNICODE_ZEN_HIRAGANA_START = 0x3040;

	/**
	 * 全角平仮名の文字コード終了コード
	 */
	private static final int UNICODE_ZEN_HIRAGANA_END = 0x309A;

	/**
	 * 全角カタカナと平仮名のオフセット
	 */
	private static final int UNICODE_KANA_HIRA_OFFSET = 0x60;

	/**
	 * デフォルトコンストラクタは隠蔽
	 */
	protected TextUtil() {
	}

	/**
	 * キャメル式の文字列に変換する<br>
	 * 文字列の先頭は小文字<br>
	 * セパレータは「_（アンダースコア）」
	 * 
	 * @param val
	 * @return 変換後の文字列
	 */
	public static String camelize(String val) {
		if (val == null) {
			return "";
		}

		val = val.toLowerCase();
		String[] array = val.split("_");
		if (array.length == 1) {
			return capitalize(val);
		} else if (array.length == 0) {
			return val;
		}

		StringBuffer buf = new StringBuffer();
		buf.append(array[0]);
		for (int ii = 1; ii < array.length; ii++) {
			buf.append(capitalize(array[ii]));
		}

		return buf.toString();
	}

	/**
	 * 文字列の先頭を大文字にする
	 * 
	 * @param val
	 *            ：変換を行う文字列
	 * @return　変換後の文字列
	 */
	public static String capitalize(final String val) {
		if (isBlank(val)) {
			return "";
		}

		char[] chars = val.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);

		return new String(chars);
	}

	/**
	 * 指定の文字列がnull、またはブランクか判定する<br>
	 * trim()を行ってブランク判定する
	 * 
	 * @param val
	 * @return boolean :　true=null、またはブランクである　false=ブランクでない
	 */
	public static boolean isBlank(String val) {
		if (val == null) {
			return true;
		}

		if (val == "\n") {
			return false;
		}

		if ("".equals(val.trim()) || "".equals(val.replace("　", " ").trim())) {
			return true;
		}

		return false;
	}

	/**
	 * ゼロ埋め
	 * 
	 * @param val
	 *            ：対象文字列
	 * @param size
	 *            ：ゼロ埋め後の文字列の桁数
	 * @return ゼロ埋めした文字列
	 */
	public static String zeroUme(int val, int size) {
		if (size <= 0) {
			return "";
		}

		if (val < 0) {
			return String.valueOf(val);
		}

		return String.format("%0" + size + "d", val);
	}

	/**
	 * NVL変換 ・指定のobjがnull、または空の場合、valを返す<br>
	 * ・それ以外はtargetを返す
	 * 
	 * @param target
	 *            ：チェック対象のオブジェクト
	 * @param val
	 *            ：targetがnull、空の場合に戻り値とする値
	 * @return String
	 */
	public static String nvl(String target, String val) {
		if (target == null) {
			return val;
		}
		if ("".equals(target)) {
			return val;
		}

		return target;
	}

	/**
	 * 画面で文字化けが発生しうる文字のコンバートを行う<br>
	 * 以下の文字の変換を行う<br>
	 * ・波ダッシュ<br>
	 * ※内部でUnsupportedEncodingExceptionが発生した場合は、RuntimeExceptionを発生する
	 * 
	 * @param val
	 *            : SJIS系の文字コード（Shift_JIS、MS932など）の文字列
	 * @return 変換後の文字列
	 */
	public static String sjis2ms932(String val) {
		if (val == null || "".equals(val)) {
			return "";
		}

		String ms932Val = "";
		for (int pos = 0; pos < val.length(); pos++) {
			String posVal = val.substring(pos, pos + 1);
			try {
				ms932Val = new String(posVal.getBytes("Shift_JIS"), "MS932");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}

			if (ms932Val.indexOf("～") >= 0) {
				int convPos = ms932Val.indexOf("～");
				val = val.replace(val.charAt(pos), ms932Val.charAt(convPos));
			}
		}
		return val;
	}

	/**
	 * 指定の値がnullの場合、空文字を返す
	 * 
	 * @param val
	 * @return 指定の値がnullの場合、空文字を返す。それ以外は入力の文字列をそのまま返す
	 */
	public static String n2b(String val) {
		if (val == null) {
			return "";
		}
		return val;
	}

	/**
	 * TSVをListに変換する（簡易版）<br>
	 * ・１行分のカンマ区切りの文字列をListに格納する
	 * 
	 * @param tsvVal
	 *            　：TSV（タブ区切り）形式の文字列
	 * @return 文字列を格納したList<String>
	 */
	public static List<String> tsv2List(String tsvVal) {
		if (tsvVal == null || tsvVal.length() == 0) {
			return new ArrayList<String>(0);
		}
		return Arrays.asList(tsvVal.split("\t"));
	}

	/**
	 * CSVをListに変換する（簡易版）<br>
	 * ・１行分のカンマ区切りの文字列をListに格納する
	 * 
	 * @param csvStr
	 *            　：CSV形式の文字列
	 * @return 文字列を格納したList
	 */
	public static List<String> csv2List(String csvStr) {
		List<String> list = new ArrayList<String>();

		if (csvStr == null || csvStr.length() == 0) {
			return new ArrayList<String>(0);
		}

		String[] strs = csvStr.split(",");
		for (int ii = 0; ii < strs.length; ii++) {
			String val = strs[ii];
			if ("".equals(val)) {
				continue;
			}

			list.add(val);
		}
		return list;
	}

	/**
	 * ListをCSVに変換する
	 * 
	 * @param list
	 *            　：文字列が格納されたList
	 * @return CSV形式の文字列
	 */
	public static String list2csv(List<String> list) {
		StringBuffer sbRe = new StringBuffer();

		if (list == null || list.size() == 0) {
			return "";
		}

		for (int ii = 0; ii < list.size(); ii++) {
			sbRe.append(list.get(ii));
			sbRe.append(",");
		}

		if (sbRe.length() > 0) {
			sbRe.setLength(sbRe.length() - 1);
		}
		return sbRe.toString();
	}

	private static final int CHECK_JOYO_KANJI_ERROR_CD = -99;

	/**
	 * 外字の有無をチェックする。<br>
	 * ・外字がある場合、最初の文字位置を戻します。<br>
	 * ・外字がない場合は、<b>-1</b>が戻ります。<br>
	 * ・チェック不能（変換エラー）の場合は、<b>-99</b>を戻します。
	 * 
	 * @param str
	 *            : チェックを行う文字列
	 * @return 外字がある場合はその文字位置、無い場合は -1
	 */
	public static int checkJoyoKanji(String str) {
		if (str == null || str.length() == 0) {
			return -1;
		}
		if (str.indexOf("\uFFFD") >= 0) {
			return str.indexOf("\uFFFD");
		}
		String chk = null;
		try {
			chk = new String(str.getBytes("MS932"), "Shift_JIS");
		} catch (Exception e) {
			return CHECK_JOYO_KANJI_ERROR_CD;
		}
		return chk.indexOf("\uFFFD");
	}

	/**
	 * 同一値が存在するかのチェックを行う<br>
	 * ","で連結された文字列同士で、同一の値が存在するかチェックする。
	 * 
	 * @param st1
	 *            ：","で連結した比較する文字列
	 * @param st2
	 *            ：","で連結した比較する文字列
	 * @return 同一値がある場合true、それ以外はfalseを返す
	 */
	public static boolean checkDouble(String st1, String st2) {
		if (st1 == null || st2 == null || st1.equals("") || st2.equals("")) {
			return false;
		}
		if (st1.equals(st2)) {
			return true;
		}

		String[] dts = st1.split(",");
		List<String> chkl = new ArrayList<String>();
		for (int i = 0; i < dts.length; i++) {
			chkl.add(dts[i]);
		}
		dts = st2.split(",");
		for (int i = 0; i < dts.length; i++) {
			if (chkl.contains(dts[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 全角カタカナを平仮名に変換する<br>
	 * 全角カタカナ以外はそのまま設定する
	 * 
	 * @param org
	 *            : 変換対象の文字列
	 * @return 変換後の文字列
	 */
	public static String cnvKata2Hira(String org) {
		if (org == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < org.length(); i++) {
			char chr = org.charAt(i);
			if (chr >= UNICODE_ZEN_KATAKANA_START && chr <= UNICODE_ZEN_KATAKANA_END) {
				chr -= UNICODE_KANA_HIRA_OFFSET;
			}
			sb.append(chr);
		}
		return sb.toString();
	}

	/**
	 * 平仮名を全角カタカナに変換する。<br>
	 * 平仮名以外はそのまま設定する。
	 * 
	 * @param org
	 *            : 変換対象の文字列
	 * @return 変換後の文字列
	 */
	public static String cnvHira2Kata(String org) {
		if (org == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < org.length(); i++) {
			char chr = org.charAt(i);
			if (chr >= UNICODE_ZEN_HIRAGANA_START && chr <= UNICODE_ZEN_HIRAGANA_END) {
				chr += UNICODE_KANA_HIRA_OFFSET;
			}
			sb.append(chr);
		}
		return sb.toString();
	}

	private static final String ZENKAKU = "　＋－＊／＝｜！？”＃＠＄％＆’｀（）［］，．；：＿＜＞＾";
	private static final String HANKAKU = " +-*/=|!?\"#@$%&'`()[],.;:_<>^";

	/**
	 * 全角の英数字(Ａ-Ｚａ-ｚ０-９記号)を半角文字(A-Za-z0-9記号)に変換する<br>
	 * 変換できない文字はそのまま設定されます。
	 * 
	 * @param org
	 *            : 変換前の文字列
	 * @return 変換後の文字列
	 */
	public static String cnv2Han(String org) {
		if (org == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < org.length(); i++) {
			char chr = org.charAt(i);
			int ix = ZENKAKU.indexOf(chr);
			if (chr >= '０' && chr <= '９') {
				chr += '0' - '０';
			} else if (chr >= 'Ａ' && chr <= 'Ｚ') {
				chr += 'A' - 'Ａ';
			} else if (chr >= 'ａ' && chr <= 'ｚ') {
				chr += 'a' - 'ａ';
			} else if (ix > -1) {
				chr = HANKAKU.charAt(ix);
			}
			sb.append(chr);
		}
		return sb.toString();
	}

	/**
	 * 半角文字(A-Za-z0-9記号)を全角の英数字(Ａ-Ｚａ-ｚ０-９記号)に変換する<br>
	 * 変換できない文字はそのまま設定されます。
	 * 
	 * @param org
	 *            : 変換前の文字列
	 * @return 変換後の文字列
	 */
	public static String cnv2Zen(String org) {
		if (org == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < org.length(); i++) {
			char chr = org.charAt(i);
			int ix = HANKAKU.indexOf(chr);

			if (chr >= '0' && chr <= '9') {
				chr += '０' - '0';
			} else if (chr >= 'A' && chr <= 'Z') {
				chr += 'Ａ' - 'A';
			} else if (chr >= 'a' && chr <= 'z') {
				chr += 'ａ' - 'a';
			} else if (ix > -1) {
				chr = ZENKAKU.charAt(ix);
			}
			sb.append(chr);
		}
		return sb.toString();
	}

	/**
	 * 全角の英数字(Ａ-Ｚａ-ｚ０-９記号)を半角英小文字(a-z0-9記号)に変換する。<br>
	 * A-Z,a-z,0-9以外の文字は無視されます。
	 * 
	 * @param org
	 *            : 変換前の文字列
	 * @return 変換後の文字列
	 */
	public static String cnv2Asc(String org) {
		if (org == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < org.length(); i++) {
			int nv = Character.getNumericValue(org.charAt(i));
			if (nv > -1) {
				sb.append(Character.forDigit(nv, Character.MAX_RADIX));
			} else {
				sb.append(org.charAt(i));
			}
		}
		return sb.toString();
	}

	private static final String HANKANA = "ｱｲｳｴｵｧｨｩｪｫｶｷｸｹｺｻｼｽｾｿﾀﾁﾂｯﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖｬｭｮﾗﾘﾙﾚﾛﾜｦﾝｰ､｡";
	private static final String ZENKANA = "アイウエオァィゥェォカキクケコサシスセソタチツッテトナニヌネノハヒフヘホマミムメモヤユヨャュョラリルレロワヲンー、。";
	private static final String DAKUTEN = "ﾞﾟ";

	/**
	 * 半角カナを全角カナに変換する。<br>
	 * 濁点(ﾞﾟ)は一文字に変換します。
	 * 
	 * @param org
	 *            : 変換対象の文字列
	 * @return 変換後の文字列
	 */
	public static String kanaHan2Zen(String org) {
		if (org == null) {
			return "";
		}

		int idx;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < org.length(); i++) {
			char cr = org.charAt(i);
			idx = HANKANA.indexOf(cr);
			if (idx >= 0) {
				cr = ZENKANA.charAt(idx);
			}
			idx = DAKUTEN.indexOf(cr);

			if (idx >= 0 && sb.length() - 1 != -1) {
				if (org.charAt(i) == org.charAt(i - 1)) {
					sb.append(cr);
					continue;
				}
				cr = sb.charAt(sb.length() - 1);
				cr++;
				cr += idx;
				sb.setLength(sb.length() - 1);
			}

			sb.append(cr);
		}
		return sb.toString();
	}

	/**
	 * 全角カタカナを半角カタカナに変換する。
	 * 
	 * @param targetVal
	 *            : 変換対象の文字列
	 * @return 変換後の文字列
	 */
	public static String kanaZen2Han(String targetVal) {
		if (targetVal == null) {
			return "";
		}
		int idx;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < targetVal.length(); i++) {
			char cr = targetVal.charAt(i);
			char cr2 = cr;

			idx = ZENKANA.indexOf(cr);
			if (idx >= 0) {
				cr = HANKANA.charAt(idx);
			} else {
				cr2--;
				idx = ZENKANA.indexOf(cr2);
				if (idx >= 0) {
					sb.append(HANKANA.charAt(idx));
					cr = 'ﾞ';
				} else {
					cr2--;
					idx = ZENKANA.indexOf(cr2);
					if (idx >= 0) {
						sb.append(HANKANA.charAt(idx));
						cr = 'ﾟ';
					}
				}
			}
			sb.append(cr);
		}
		return sb.toString();
	}

	/**
	 * 全角英小文字から全角大文字に変換する
	 * 
	 * @param targetVal
	 *            : 変換対象の文字列
	 * @return 変換後の文字列
	 */
	public static String toUpper4zen(String targetVal) {
		if (targetVal == null) {
			return "";
		}

		return targetVal.toUpperCase();
	}

	/**
	 * 濁音、半濁音と静音のマッチングMap
	 */
	private static Map<String, String> dakuonMap = new HashMap<String, String>();
	static {
		dakuonMap.put("が", "か");
		dakuonMap.put("ぎ", "き");
		dakuonMap.put("ぐ", "く");
		dakuonMap.put("げ", "け");
		dakuonMap.put("ご", "こ");
		dakuonMap.put("ざ", "さ");
		dakuonMap.put("じ", "し");
		dakuonMap.put("ず", "す");
		dakuonMap.put("ぜ", "せ");
		dakuonMap.put("ぞ", "そ");
		dakuonMap.put("だ", "た");
		dakuonMap.put("ぢ", "ち");
		dakuonMap.put("づ", "つ");
		dakuonMap.put("で", "て");
		dakuonMap.put("ど", "と");
		dakuonMap.put("ば", "は");
		dakuonMap.put("び", "ひ");
		dakuonMap.put("ぶ", "ふ");
		dakuonMap.put("べ", "へ");
		dakuonMap.put("ぼ", "ほ");
		dakuonMap.put("ぱ", "は");
		dakuonMap.put("ぴ", "ひ");
		dakuonMap.put("ぷ", "ふ");
		dakuonMap.put("ぺ", "へ");
		dakuonMap.put("ぽ", "ほ");
		dakuonMap.put("ガ", "カ");
		dakuonMap.put("ギ", "キ");
		dakuonMap.put("グ", "ク");
		dakuonMap.put("ゲ", "ケ");
		dakuonMap.put("ゴ", "コ");
		dakuonMap.put("ザ", "サ");
		dakuonMap.put("ジ", "シ");
		dakuonMap.put("ズ", "ス");
		dakuonMap.put("ゼ", "セ");
		dakuonMap.put("ゾ", "ソ");
		dakuonMap.put("ダ", "タ");
		dakuonMap.put("ヂ", "チ");
		dakuonMap.put("ヅ", "ツ");
		dakuonMap.put("デ", "テ");
		dakuonMap.put("ド", "ト");
		dakuonMap.put("バ", "ハ");
		dakuonMap.put("ビ", "ヒ");
		dakuonMap.put("ブ", "フ");
		dakuonMap.put("ベ", "ヘ");
		dakuonMap.put("ボ", "ホ");
		dakuonMap.put("パ", "ハ");
		dakuonMap.put("ピ", "ヒ");
		dakuonMap.put("プ", "フ");
		dakuonMap.put("ペ", "ヘ");
		dakuonMap.put("ポ", "ホ");
	}

	/**
	 * 濁音、半濁音を文字列から除去
	 * 
	 * @param targetVal
	 *            : 変換対象の文字列
	 * @return 変換後の文字列
	 */
	public static String dakuon2seion(String targetVal) {
		if (targetVal == null) {
			return "";
		}

		for (int ii = 0; ii < targetVal.length(); ii++) {
			String strKey = String.valueOf(targetVal.charAt(ii));
			if (dakuonMap.containsKey(strKey)) {
				targetVal = targetVal.replaceAll(strKey, dakuonMap.get(strKey));
			}
		}

		return targetVal;
	}

	/**
	 * 拗音と大文字のマッチングMap
	 */
	private static Map<String, String> yonMap = new HashMap<String, String>();
	static {
		yonMap.put("ぁ", "あ");
		yonMap.put("ぃ", "い");
		yonMap.put("ぅ", "う");
		yonMap.put("ぇ", "え");
		yonMap.put("ぉ", "お");
		yonMap.put("ァ", "ア");
		yonMap.put("ィ", "イ");
		yonMap.put("ゥ", "ウ");
		yonMap.put("ェ", "エ");
		yonMap.put("ォ", "オ");
		yonMap.put("ヵ", "カ");
		yonMap.put("ヶ", "ケ");
		yonMap.put("っ", "つ");
		yonMap.put("ッ", "ツ");
		yonMap.put("ャ", "ヤ");
		yonMap.put("ュ", "ユ");
		yonMap.put("ョ", "ヨ");
		yonMap.put("ヮ", "ワ");
		yonMap.put("ゃ", "や");
		yonMap.put("ゅ", "ゆ");
		yonMap.put("ょ", "よ");
		yonMap.put("ゎ", "わ");
	}

	/**
	 * 拗音を大文字に変換する
	 * 
	 * @param targetVal
	 *            : 変換対象の文字列
	 * @return 変換後の文字列
	 */
	public static String toUpperYoon(String targetVal) {
		if (targetVal == null) {
			return "";
		}

		for (int ii = 0; ii < targetVal.length(); ii++) {
			String strKey = String.valueOf(targetVal.charAt(ii));
			if (yonMap.containsKey(strKey)) {
				targetVal = targetVal.replaceAll(strKey, yonMap.get(strKey));
			}
		}

		return targetVal;
	}

	/**
	 * 全角カナ、数字以外の文字を取り除く
	 * 
	 * @param targetVal
	 *            　：　変換対象の文字列
	 * @return 変換後の文字列
	 */
	public static String toZenKanaAndSuji(String targetVal) {
		if (targetVal == null || "".equals(targetVal)) {
			return "";
		}

		return targetVal.replaceAll("[^ァ-ヶ０-９]", "");
	}

	/**
	 * 全角記号を取り除く<br>
	 * 注）罫線は取り除かない
	 * 
	 * @param targetVal
	 *            変換対象文字列
	 * @return　変換後の文字列
	 */
	public static String delZenKigo(String targetVal) {
		if (targetVal == null || "".equals(targetVal)) {
			return "";
		}

		String val = targetVal.replaceAll("[！”＃＄％＆’（）＝～｜‘｛＋＊｝＜＞？＿－＾￥＠「；：」、。・ー，． 　“／]", "");

		return val;
	}

	/**
	 * 指定の文字列の先頭からpos文字分の文字列を返す<br>
	 * posに負の数を与えた場合は、文字列の終端からの文字を抜き出す
	 * 
	 * @param val
	 *            　対象の文字列
	 * @param pos
	 *            　抜き出す文字数
	 * @return　抜き出した文字列
	 */
	public static String left(String val, int pos) {
		if (val == null || val.equals("")) {
			return "";
		}

		if (pos < 0) {
			return TextUtil.right(val, pos * -1);
		}

		if (val.length() < pos) {
			return val;
		}

		return val.substring(0, pos);
	}

	/**
	 * 指定の文字列の終端からpos文字分の文字列を返す
	 * 
	 * @param val
	 *            　対象の文字列
	 * @param pos
	 *            　抜き出す文字数
	 * @return　抜き出した文字列
	 */
	public static String right(String val, int pos) {
		if (val == null || val.equals("")) {
			return "";
		}

		if (pos < 0) {
			return "";
		}

		if (val.length() < pos) {
			return val;
		}

		return val.substring(val.length() - pos, val.length());
	}

	/**
	 * 文字列の前後に指定の文字列を付加する<br>
	 * valがnullまたはブランクの場合は、付加しない。
	 * 
	 * @param val
	 *            　対象文字列
	 * @param prefix
	 *            　前方に付加する文字列
	 * @param sufix
	 *            　後方に付加する文字列
	 * @return　編集後の文字列
	 */
	public static String addStr(String val, String prefix, String sufix) {
		if (val == null || val.trim().length() == 0) {
			return "";
		}
		if (prefix == null) {
			prefix = "";
		}
		if (sufix == null) {
			sufix = "";
		}
		return prefix + val + sufix;
	}

	/**
	 * 数値をカンマ付きの文字列でエス
	 * 
	 * @param num
	 *            整形する数値
	 * @return　整形した文字列
	 */
	public static String numberFormat(long num) {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(num);
	}

	/**
	 * スペース区切りのキーワードをList化する<br>
	 * limit（最大キーワード数）までをListに格納する<br>
	 * 半角文字は全角に変換する
	 * 
	 * @param keyword
	 *            キーワード
	 * @param limit
	 *            最大キーワード数
	 * @param isRepDel
	 *            true : 重複キーワードを除去<br>
	 *            false : 重複キーワードを除去しない
	 * @return キーワードのList
	 */
	public static List<String> createKeywordList(String keyword, int limit, boolean isRepDel) {

		// 全角スペースを半角スペースに変換
		String changedVal = TextUtil.cnv2Zen(keyword).replaceAll("　", " ").trim();

		// 空白で区切ってリストに変換
		String[] words = changedVal.split(" ");

		List<String> keywordList = new ArrayList<String>(0);
		for (String val : words) {
			if (isBlank(val.trim())) {
				continue;
			}

			if (!isRepDel) {
				keywordList.add(val);

			} else {

				// 同一キーワード除去
				if (!keywordList.contains(val)) {
					keywordList.add(val);
				}
			}

			// limitまでを使用
			if (keywordList.size() >= limit) {
				break;
			}
		}

		return keywordList;
	}

	/**
	 * 左空白除去<br>
	 * 文字列の左側の全角、半角スペースを除去する<br>
	 * 
	 * @param str
	 *            修正対象文字列
	 * @return String 修正後文字列
	 */
	public static final String leftTrim(String str) {
		String temp = "";
		int start = 0;

		if (str == null || str.length() == 0) {
			return "";
		}

		for (int i = 0; i < str.length(); i++) {
			temp = str.substring(i, i + 1);
			if (!temp.equals("　") && !temp.equals(" ")) {
				start = i;
				break;
			}
		}

		if (temp.equals("　") || temp.equals(" ")) {
			return "";
		}

		return str.substring(start, str.length());

	}

	/**
	 * 右空白除去<br>
	 * 文字列の右側の全角、半角スペースを除去する<br>
	 * 
	 * @param str
	 *            修正対象文字列
	 * @return String 修正後文字列
	 */
	public static final String rightTrim(String str) {

		String temp = "";
		int end = 0;
		if (str == null || str.length() == 0) {
			return "";
		}

		for (int i = str.length() - 1; i >= 0; i--) {
			temp = str.substring(i, i + 1);
			if (!temp.equals("　") && !temp.equals(" ")) {
				end = i;
				break;
			}
		}

		temp = str.substring(0, end + 1);
		if (temp.equals("　") || temp.equals(" ")) {
			temp = "";
		}
		return temp;
	}

	/**
	 * 前後の空白除去<br>
	 * 文字列の前後の半角、全角スペースを削除する.<br>
	 * 
	 * @param str
	 *            修正対象文字列
	 * @return String 修正後文字列
	 */
	public static String allTrim(String str) {
		if (str == null) {
			str = "";
			return str;
		}
		String tmp = "";
		tmp = rightTrim(str);
		tmp = leftTrim(tmp);

		// 全角空白１文字になったら空文字を設定
		if (str.equals("　")) {
			str = "";
		}
		return tmp;
	}

	/**
	 * ファイルやフォルダのパスをロギングする際に使用する。<br>
	 * 「\」を「\\」に変換する<br>
	 * 
	 * @param str
	 *            修正対象文字列
	 * @return String 修正後文字列
	 */
	public static String replaceEscapeStr(String str) {
		if (str == null) {
			str = "";
			return str;
		}
		String tmp = str.replace("\\", "\\\\");
		return tmp;
	}
}
