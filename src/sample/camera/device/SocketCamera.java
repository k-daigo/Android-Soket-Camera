package sample.camera.device;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * カメラサーバから画像を取得するクラス<br>
 * （シングルトン）
 * 
 * @author k-daigo
 */
public class SocketCamera {
	private static final String LOG_TAG = "SocketCamera:";
	private static final int SOCKET_TIMEOUT = 1000;

	// サーバのアドレス
	private static final String SERVER_ADDRESS = "192.168.111.100";
	private static final int SERVER_PORT = 9889;

	// 自instance
	private static SocketCamera socketCamera;

	private CameraPreview cameraPreview;
	private Camera parametersCamera;
	private SurfaceHolder surfaceHolder;

	private final boolean preserveAspectRatio = true;
	private final Paint paint = new Paint();

	// 画像サイズ
	private int width = 240;
	private int height = 200;
	private Rect bounds = new Rect(0, 0, width, height);

	private Bitmap currentBitmap = null;
	
	/**
	 * コンストラクタは隠蔽
	 */
	private SocketCamera() {
	}

	/**
	 * 当クラスのインスタンスを返す
	 * 
	 * @return SocketCameraのインスタンス
	 */
	public static SocketCamera getInstance() {
		if (socketCamera == null) {
			socketCamera = new SocketCamera();
		}
		return socketCamera;
	}

	/**
	 * プレビューを開始する
	 */
	public void startPreview() {
		cameraPreview = new CameraPreview();
		cameraPreview.start();
	}

	/**
	 * プレビューを停止する
	 */
	public void stopPreview() {
		cameraPreview.stopPreview();
	}

	/**
	 * レビュー中か返す
	 * @return	true	: プレビュー中
	 * 			false	: プレビュー中でない
	 */
	public boolean isPreviewing()
	{
		return cameraPreview.isPreviewing();
	}

	/**
	 * 直前の画像を返す
	 * @return Bitmap画像
	 */
	public Bitmap getCaptur()
	{
		return this.currentBitmap;
	}

	/**
	 * SurfaceHolderを設定する
	 * @param surfaceHolder
	 * @throws IOException
	 */
	public void setPreviewDisplay(SurfaceHolder surfaceHolder) throws IOException {
		this.surfaceHolder = surfaceHolder;
		this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
	}

	/**
	 * カメラパラメータを設定する
	 * @param parameters
	 */
	public void setParameters(Camera.Parameters parameters) {
		parametersCamera.setParameters(parameters);
		Size size = parameters.getPreviewSize();
		bounds = new Rect(0, 0, size.width, size.height);
	}

	/**
	 * カメラパラメータを返す
	 * @return カメラパラメータ
	 */
	public Camera.Parameters getParameters() {
		Log.i(LOG_TAG, "Getting Socket Camera parameters");
		return parametersCamera.getParameters();
	}

	/**
	 * カメラサーバから画像を取得するクラス
	 * 
	 * @author k-daigo
	 */
	private class CameraPreview extends Thread {
		private boolean previewing = false;

		/**
		 * キャプチャ中か返す
		 * @return
		 */
		public boolean isPreviewing()
		{
			return this.previewing;
		}
		
		/**
		 * キャプチャを開始する
		 */
		public void startPreview() {
			this.previewing = true;
		}

		/**
		 * キャプチャを停止する
		 */
		public void stopPreview() {
			this.previewing = false;
		}
		
		/**
		 * キャプチャスレッド開始
		 */
		@Override
		public void run() {
			this.startPreview();

			while (previewing) {
				Canvas canvas = null;
				try {
					canvas = surfaceHolder.lockCanvas(null);
					synchronized (surfaceHolder) {
						Bitmap bitmap = this.getData();

						if (bounds.right == bitmap.getWidth() && bounds.bottom == bitmap.getHeight()) {
							canvas.drawBitmap(bitmap, 0, 0, null);
							
						} else {
							Rect dest;
							if (preserveAspectRatio) {
								dest = new Rect(bounds);
								dest.bottom = bitmap.getHeight() * bounds.right / bitmap.getWidth();
								dest.offset(0, (bounds.bottom - dest.bottom) / 2);
							} else {
								dest = bounds;
							}
							
							if (canvas != null) {
								currentBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
								canvas.drawBitmap(bitmap, null, dest, paint);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}

		/**
		 * サーバから画像を取得する
		 * 
		 * @return Bitmap
		 * @throws IOException
		 */
		private Bitmap getData() throws IOException {
			Socket socket = null;
			Bitmap bitmap = null;
			try {
				socket = new Socket();
				socket.bind(null);
				socket.setSoTimeout(SOCKET_TIMEOUT);
				socket.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), SOCKET_TIMEOUT);

				InputStream in = socket.getInputStream();
				bitmap = BitmapFactory.decodeStream(in);
			} finally {
				if(socket != null){
					socket.close();
				}
			}

			return bitmap;
		}
	}
}
