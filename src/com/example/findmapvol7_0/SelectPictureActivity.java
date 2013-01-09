package com.example.findmapvol7_0;

import java.io.InputStream;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SelectPictureActivity extends Activity{
	private static final int REQUEST_GALLERY = 0;
	private ImageView imgView;
	private Bitmap img;
	private Uri photoUri;
	private Intent returnIntent;
	String message11 = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.selectpicture);

	imgView = (ImageView)findViewById(R.id.imgview_id);

	// ギャラリー呼び出し
	Intent intent = new Intent();
	intent.setType("image/*");
	intent.setAction(Intent.ACTION_GET_CONTENT);
	startActivityForResult(intent, REQUEST_GALLERY);

	// OKボタンのクリックリスナー設定
    Button OKbottonBtn = (Button)findViewById(R.id.OKbotton);
    OKbottonBtn.setTag("OKbotton");
    OKbottonBtn.setOnClickListener(new ButtonClickListener());

    // canselボタンのクリックリスナー設定
    Button cancelBtn = (Button)findViewById(R.id.cancelbotton);
    cancelBtn.setTag("cancelbotton");
    cancelBtn.setOnClickListener(new ButtonClickListener());

	}

	//端末のBackキーを無効にする
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	    if (event.getAction()==KeyEvent.ACTION_DOWN) {
	        switch (event.getKeyCode()) {
	        case KeyEvent.KEYCODE_BACK:
	            // ダイアログ表示など特定の処理を行いたい場合はここに記述
	            // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
	            return true;
	        }
	    }
	    return super.dispatchKeyEvent(event);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub

	if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
	try {
		Log.d(message11, "おっけ"+ RESULT_OK);
		Log.d(message11, "おっけ"+ data);
		// 選択した画像のURIを取得
		photoUri = data.getData();

		Log.d(message11, "おｋ"+photoUri);
	InputStream in = getContentResolver().openInputStream(data.getData());
	img = BitmapFactory.decodeStream(in);
	in.close();
	// 選択した画像を表示
	imgView.setImageBitmap(img);


	} catch (Exception e) {

			}
		}
	}

    class ButtonClickListener implements OnClickListener, android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO 自動生成されたメソッド・スタブ
			// タグの取得
    		String tag = (String)v.getTag();


			Log.d(message11, "おｋ"+img);
			//OKボタンの処理
		 if(tag.equals("OKbotton")){

			 	//選択された写真データをRegistrationActiviyに渡す
         	Intent picIntent =  new Intent(SelectPictureActivity.this, RegisterActivity.class);

         	returnIntent = picIntent.setData(photoUri);
         	setResult(RESULT_OK, returnIntent);

         	Log.d(message11, "おっけ"+photoUri);
         	Log.d(message11, "おっけ"+returnIntent);
         	if(img!=null){
         	         	img.recycle();
         	         	imgView.setImageBitmap(null);
         	         	finish();}
         }
		//Cancelボタンの処理
		 	else if(tag.equals("cancelbotton")){
		 			if(img!=null){
		 					img.recycle();
		 					imgView.setImageBitmap(null);
		        // img=null;
		 				}
		         // Activityを閉じる
		 		finish();
		 			}
            	}


		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO 自動生成されたメソッド・スタブ

		}
    }
}