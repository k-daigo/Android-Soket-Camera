package sample.camera.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日時ユーティリティ
 * 
 * @version 1.00
 */
public class DateUtil {

	/**
	 * yyyy/MM/ddを「/」で分割した際の配列サイズ
	 */
	private static final int SPLITED_YMD_LENGTH = 3;

	/**
	 * 年が２桁で指定された場合に1900年代とするか2000年代とするか判定する敷居値
	 */
	private static final int AIMAI_YEAR_HANTEI_YEAR = 80;

	/**
	 * yyyy/MM/ddを各要素で分割した際の要素数
	 */
	private static final int YMD_ITEM_COUNT = 3;

	/**
	 * コンストラクタは隠蔽
	 */
	protected DateUtil() {
	}

	/**
	 * システム日付を「YYYY/MM/DD」形式で返す
	 * 
	 * @return システム日付（YYYY/MM/DD）
	 */
	public static String getNowDate() {
		return getFormatDate("yyyy/MM/dd");
	}

	/**
	 * システム日付を「YYYY」形式で返す
	 * 
	 * @return システム日付（YYYY）
	 */
	public static String getNowYear() {
		return getFormatDate("yyyy");
	}

	/**
	 * システム日付を「MM」形式で返す
	 * 
	 * @return システム日付（MM）
	 */
	public static String getNowMonth() {
		return getFormatDate("MM");
	}

	/**
	 * ±n月の年計算
	 * 
	 * @param targetDate
	 *            "yyyy/MM/dd"の年月を指定する
	 * @param addYear
	 *            取得したい年の相対年を指定する
	 * @return ±n月の年計算結果（yyyy）
	 */
	public static String addYear(String targetDate, int addYear) {
		return addYear2format(targetDate, addYear, "yyyy");
	}

	/**
	 * ±n月の年計算
	 * 
	 * @param targetDate
	 *            "yyyy/MM/dd"の年月を指定する
	 * @param addYear
	 *            取得したい年の相対年を指定する
	 * @param format
	 *            日付の書式（yyyy/MM/ddなど）
	 * @return ±n月の年計算結果をformatの書式で返す
	 */
	public static String addYear2format(String targetDate, int addYear, String format) {
		if (targetDate == null || targetDate.trim().length() == 0 || targetDate.replace("　", " ").trim().length() == 0) {
			return "";
		}

		String[] targetDates = targetDate.split("/");

		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile("^\\D+");
		matcher = pattern.matcher(targetDates[0]);
		boolean isMatch = matcher.matches();

		if (isMatch || targetDates.length != YMD_ITEM_COUNT) {
			return "";
		}

		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(targetDates[0]), Integer.parseInt(targetDates[1]) - 1, Integer.parseInt(targetDates[2]));
		cal.add(Calendar.YEAR, addYear);

		return (new SimpleDateFormat(format)).format(cal.getTime());
	}

	/**
	 * ±n月の年月計算
	 * 
	 * @param monthVal
	 *            "yyyy/MM"の年月を指定する
	 * @param addMonth
	 *            取得したい年月の相対月を指定する
	 * @return ±n月の年月計算（yyyy/MM形式）<br>
	 *         「yyyy/MM」形式にマッチしない値を指定した場合は、空文字を返す
	 */
	public static String addMonth(String monthVal, int addMonth) {
		if (monthVal == null || monthVal.trim().length() == 0 || monthVal.replace("　", " ").trim().length() == 0) {
			return "";
		}

		String[] targetDates = monthVal.split("/");

		Matcher matcher = Pattern.compile("^\\D+").matcher(targetDates[0]);
		boolean isMatch = matcher.matches();

		if (isMatch || targetDates.length != 2) {
			return "";
		}

		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(targetDates[0]), Integer.parseInt(targetDates[1]) - 1, 1);
		cal.add(Calendar.MONTH, addMonth);
		String date = new SimpleDateFormat("yyyy/MM").format(cal.getTime());
		return date;
	}

	/**
	 * ±n日の日付計算
	 * 
	 * @param date
	 *            元となる"yyyy/MM/dd"を指定する
	 * @param addDate
	 *            取得したい日付の相対日を指定する
	 * @return ±n月の年月計算（yyyy/MM/dd形式）
	 */
	public static String addDate(String date, int addDate) {
		if (date == null || date.trim().length() == 0 || date.replace("　", " ").trim().length() == 0) {
			return "";
		}

		String[] targetDates = date.split("/");

		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile("^\\D+");
		matcher = pattern.matcher(targetDates[0]);
		boolean isMatch = matcher.matches();

		if (isMatch || targetDates.length != YMD_ITEM_COUNT) {
			return "";
		}

		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(targetDates[0]), Integer.parseInt(targetDates[1]) - 1, Integer.parseInt(targetDates[2]));
		cal.add(Calendar.DATE, addDate);
		return new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());
	}

	/**
	 * システム日付を「dd」形式で返す
	 * 
	 * @return システム日付（dd）
	 */
	public static String getNowDay() {
		return getFormatDate("dd");
	}

	/**
	 * システム時間を「HH:mm:ss」形式で返す
	 * 
	 * @return システム時間（HH:mm:ss）
	 */
	public static String getNowTime() {
		return getFormatDate("HH:mm:ss");
	}

	/**
	 * 指定のフォーマットでシステム日付を返す
	 * 
	 * @param format
	 * @return 指定のフォーマットで整形された日付文字列
	 */
	public static String getFormatDate(String format) {
		if (format == null) {
			return "";
		}

		Date dt = new Date();
		SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
		df.applyPattern(format);
		String date = df.format(dt);
		if (date != null) {
			return date;
		}
		return "";
	}

	/**
	 * システム日付をから年度を取得し、文字列で返す
	 * 
	 * @return システム年度（YYYY）
	 */
	public static String getNendo() {
		int nowYear = Integer.parseInt(getNowYear());
		int nowMonth = Integer.parseInt(getNowMonth());
		if (Calendar.JANUARY + 1 <= nowMonth && nowMonth <= Calendar.MARCH + 1) {
			nowYear--;
		}
		return Integer.toString(nowYear);
	}

	/**
	 * 指定の日付、日時文字列から、当該年度を返す<br>
	 * dateは先頭が「yyyy/MM」で始まっていれば、それ以降のフォーマットは不問
	 * 
	 * @param date
	 *            日付文字列
	 * @return 年度文字列
	 */
	public static String getNendo(String date) {
		String[] tm = date.split("/");
		if (tm.length < 2) {
			throw new IllegalArgumentException("指定の日付が「yyyy/MM」で始まっていません");
		}

		int year = Integer.parseInt(tm[0]);
		int month = Integer.parseInt(tm[1]);
		if (Calendar.JANUARY + 1 <= month && month <= Calendar.MARCH + 1) {
			year--;
		}
		return Integer.toString(year);
	}

	/**
	 * 指定の日付を指定のフォーマットに変換して返す
	 * 
	 * @param date
	 *            日付文字列
	 * @param format
	 *            変換文字列（yyyy/MM/ddなど）
	 * @return 指定の日付形式に変換した文字列
	 */
	public static String convertDate(String date, String format) {
		if (date == null || date.trim().length() == 0 || date.replace("　", " ").trim().length() == 0) {
			return "";
		}

		String[] dates = date.split("/");
		if (dates.length < SPLITED_YMD_LENGTH) {
			return "";
		}

		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		// 2桁の年を4桁年に変換
		if (year.length() == 2) {
			if (Integer.parseInt(year) < AIMAI_YEAR_HANTEI_YEAR) {
				year = "20" + year;
			} else {
				year = "19" + year;
			}
		}

		Calendar cal = Calendar.getInstance();
		int iy = Integer.parseInt(year);
		int im = Integer.parseInt(month) - 1;
		int id = Integer.parseInt(day);
		cal.set(iy, im, id);
		java.util.Date convDt = cal.getTime();

		// デフォルトのロケールの日付フォーマッタを入手
		SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();

		// 日付フォーマットを設定
		df.applyPattern(format);

		// dt を 指定フォーマットへ変換
		String convDtS = df.format(convDt);
		if (convDtS == null) {
			return "";
		}

		return convDtS;
	}

	/**
	 * 「yy/M/d」などの日付文字列を「yyyy/MM/dd」形式に変換する
	 * 
	 * @param date
	 * @return yyyy/MM/dd形式の文字列
	 */
	public static String convertDate(String date) {
		StringTokenizer st = new StringTokenizer(date, "/");
		List<String> dt = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			dt.add(st.nextToken());
		}
		if (dt.size() < SPLITED_YMD_LENGTH) {
			return "";
		}

		String year = (String) dt.get(0);
		String month = (String) dt.get(1);
		String day = (String) dt.get(2);

		// 2桁の年を4桁年に変換
		if (year.length() == 2) {
			if (Integer.parseInt(year) < AIMAI_YEAR_HANTEI_YEAR) {
				year = "20" + year;
			} else {
				year = "19" + year;
			}
		}

		Calendar cal = Calendar.getInstance();
		int iy = Integer.parseInt(year);
		int im = Integer.parseInt(month) - 1;
		int id = Integer.parseInt(day);
		cal.set(iy, im, id);
		java.util.Date convDt = cal.getTime();

		// デフォルトのロケールの日付フォーマッタを入手
		SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();

		// 日付フォーマットを設定
		df.applyPattern("yyyy/MM/dd");

		// dt を 指定フォーマットへ変換
		String convDtS = df.format(convDt);
		if (convDtS == null) {
			return "";
		}

		return convDtS;
	}

	/**
	 * 「YYYY/MM/DD」形式の日付を<code>Calendar</code>で返す
	 * 
	 * @param date
	 *            a <code>String</code>：「YYYY/MM/DD」形式の文字列
	 * @return システム日付
	 */
	public static Calendar getCalendar(String date) {
		StringTokenizer st = new StringTokenizer(date, "/");
		List<String> dt = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			dt.add(st.nextToken());
		}

		String year = "";
		String month = "";
		String day = "";
		try {
			year = (String) dt.get(0);
			month = (String) dt.get(1);
			day = (String) dt.get(2);

		} catch (IndexOutOfBoundsException ex) {
			throw new IllegalArgumentException();
		}

		Calendar cal = Calendar.getInstance();
		int iy = Integer.parseInt(year);
		int im = Integer.parseInt(month) - 1;
		int id = Integer.parseInt(day);
		cal.set(iy, im, id);

		return cal;
	}

	/**
	 * 「YYYY/MM/DD」形式で指定された日付の末日を返す
	 * 
	 * @param date
	 * @return 「YYYY/MM/DD」形式の末日
	 */
	public static String getDayOfMonth(String date) {
		int dayOfMonth = getDayByDayOfMonth(date);
		String day = "0" + String.valueOf(dayOfMonth);
		return convertDate(date, "yyyy/MM") + day.substring(day.length() - 2, 2);
	}

	/**
	 * 「YYYY/MM/DD」形式で指定された日付の末日を返す
	 * 
	 * @param date
	 * @return 末日
	 */
	public static int getDayByDayOfMonth(String date) {
		Calendar calendar = getCalendar(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 「YYYY/MM/DD」形式で指定された日付の曜日を返す
	 * 
	 * @param date
	 * @return "日", "月", "火", "水", "木", "金", "土"のいずれかを文字列で返す
	 */
	public static String getYoubi(String date) {
		if (date == null) {
			return "";
		}

		String[] youbi = { "日", "月", "火", "水", "木", "金", "土" };
		Calendar calendar = getCalendar(date);
		return youbi[calendar.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * 指定の日付の曜日をCalendarの曜日で返す
	 * 
	 * @param date
	 *            　「yyyy/MM/dd」形式の日付文字列
	 * @return 曜日（Calendarの曜日定数値）
	 */
	public static int getEngYoubi(String date) {
		Calendar calendar = getCalendar(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 指定の日付（yyyy/MM/dd）に曜日を付加する
	 * 
	 * @param date
	 *            　「yyyy/MM/dd」形式の日付文字列
	 * @return　「yyyy/MM/dd(曜日)」
	 */
	public static String getYoubiWithKakko(String date) {
		String yobi = DateUtil.getYoubi(date);
		if ("".equals(yobi)) {
			return "";
		}

		return date + "(" + yobi + ")";
	}

	/**
	 * 有効な日付かチェックする<br>
	 * 日付がnull、ブランクの場合のtrueを返す
	 * 
	 * @param checkDate
	 *            チェックする日付
	 * @param format
	 *            書式（yyyy/MM/ddなど）
	 * @return true : 有効である<br>
	 *         false : 無効な日付である
	 */
	public static boolean isValidDate(String checkDate, String format) {
		if (TextUtil.isBlank(checkDate)) {
			return true;
		}

		if (checkDate.length() != format.length()) {
			return false;
		}

		SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance();
		sdf.applyPattern(format);
		sdf.setLenient(false);
		try {
			sdf.parse(checkDate);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 有効な日付かチェックする<br>
	 * 日付がnull、ブランクの場合のtrueを返す
	 * 
	 * @param checkDate
	 *            チェックする日付（yyyy/MM/dd）
	 * @return true : 有効である<br>
	 *         false : 無効な日付である
	 */
	public static boolean isValidDate(String checkDate) {
		return isValidDate(checkDate, "yyyy/MM/dd");
	}

	/**
	 * Timestamp型の日付を指定のフォーマットのString型に変換し返す
	 * 
	 * @param date
	 * @param format
	 * @return 指定のフォーマットで整形された日付文字列
	 */
	public static String getFormatDateByTimestamp(Timestamp date, String format) {
		if (format == null || date == null) {
			return "";
		}
		String dateStr = new SimpleDateFormat(format).format(date);

		if (dateStr != null) {
			return dateStr;
		}
		return "";
	}
}
