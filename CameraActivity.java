package h0910960060.findmapvol6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CameraActivity extends Activity {
	// カメラの宣言
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// インテントの生成
		Intent intent = new Intent();
		// インテントのアクションを指定
		intent.setAction("android.media.action.IMAGE_CAPTURE");
		// 標準カメラアプリケーションのアクティビティを起動
		startActivityForResult(intent, 0);
		// Activityを閉じる
		finish();
	}
}
