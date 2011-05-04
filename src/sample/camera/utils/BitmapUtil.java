package sample.camera.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;

/**
 * Bitmapユーティリティクラス
 * 
 * @author k-daigo
 */
public class BitmapUtil {

	/**
	 * Bitmapをbyte配列に変換する
	 * @param src 変換するBitmp
	 * @param format Bitmap.CompressFormatを指定する
	 * @param quality 画質
	 * @return 変換したbyte配列
	 */
	public static byte[] bmp2data(Bitmap src, Bitmap.CompressFormat format, int quality) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		src.compress(format, quality, outputStream);
		return outputStream.toByteArray();
	}
}
