package sample.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 画面にガイドラインを表示するクラス
 * 
 * @author k-daigo
 */
public class OverlayView extends View {
	public OverlayView(Context context) {
		super(context);
	}

	public OverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OverlayView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * ガイドラインの描画を行う
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.RED);

		float endX = canvas.getWidth();
		float centerX = endX / 2.0f;
		float endY = canvas.getHeight();
		float centerY = endY / 2.0f;

		canvas.drawLine(0, centerY, endX, centerY, paint);
		canvas.drawLine(centerX, 0, centerX, endY, paint);
	}
}
