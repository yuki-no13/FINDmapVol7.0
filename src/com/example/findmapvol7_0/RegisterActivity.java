package com.example.findmapvol7_0;


import java.io.InputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private Uri PhotoImageUri;
	private ImageView SelectimgView;
	private Bitmap SelectImg;
	private EditText  name;
	private EditText  address;
	private EditText  ID;
	private String StringName=null;
    private  String StringAddress = null;
	public static final int CNTER_HORIZONTAL = 0;
	public static final int SELECTED_PICT = 0;
	CreateProductHelper helper = null;
    SQLiteDatabase db = null;
    String message11 = null;

    // onCreateメソッド(画面初期表示イベントハンドラ)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // スーパークラスのonCreateメソッド呼び出し
        super.onCreate(savedInstanceState);
        // レイアウト設定ファイルの指定
        setContentView(R.layout.registration);


        // 写真ボタンのクリックリスナー設定
        Button pictureBtn = (Button)findViewById(R.id.picture);
        pictureBtn.setTag("picture");
        pictureBtn.setOnClickListener(new PictuerButtonClickListener());
        // 登録ボタンのクリックリスナー設定
        Button insertBtn = (Button)findViewById(R.id.insert);
        insertBtn.setTag("insert");
        insertBtn.setOnClickListener(new ButtonClickListener());
        // 更新ボタンのクリックリスナー設定
        Button updatetBtn = (Button)findViewById(R.id.update);
        updatetBtn.setTag("update");
        updatetBtn.setOnClickListener(new ButtonClickListener());
        // 削除ボタンのクリックリスナー設定
        Button delBtn = (Button)findViewById(R.id.delete);
        delBtn.setTag("delete");
        delBtn.setOnClickListener(new ButtonClickListener());
        // 表示ボタンのクリックリスナー設定
        Button selectBtn = (Button)findViewById(R.id.select);
        selectBtn.setTag("select");
        selectBtn.setOnClickListener(new ButtonClickListener());

        // DB作成
        helper = new CreateProductHelper(RegisterActivity.this);
    }

    class PictuerButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			 // 写真ボタンが押された場合
            	Intent intent =  new Intent(RegisterActivity.this,SelectPictureActivity .class);
                startActivityForResult(intent,SELECTED_PICT);
               //選択されている写真領域のメモリーを開放
                if(SelectImg !=null){
                	//SelectImg.recycle();
                	SelectImg=null;
                	SelectimgView.setImageDrawable(null);

                	Log.d(message11, "OK1"+SelectImg);

                }

		}
    }

  //slectpictureからの返信後の処理
  	@Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {

  			Log.d(message11, "OK"+requestCode);
      	if (requestCode==SELECTED_PICT  && resultCode == RESULT_OK) {

      	Intent picIntent = getIntent();

      	PhotoImageUri = data.getData();

      	Log.d(message11, "OK1"+data);
  		Log.d(message11, "OK2"+picIntent);
  		Log.d(message11, "OK3"+PhotoImageUri);
      	if(PhotoImageUri!=null){

       //選択画像を表示
      	try {
      		BitmapFactory.Options opt = new BitmapFactory.Options();
      		opt.inSampleSize=1;
      		SelectImg= BitmapFactory.decodeFile("sdcard/img.png", opt);
      		SelectimgView = (ImageView) findViewById(R.id.selectiamge);
      		InputStream in = getContentResolver().openInputStream(data.getData());
      		SelectImg = BitmapFactory.decodeStream(in);

      		Log.d(message11, "OK4"+opt);

      		SelectimgView.setImageBitmap(SelectImg);

      	}catch (Exception e) {
      		Log.d(message11,"OK5");
      		}
  		}
      }
   }

    // クリックリスナー定義
    class ButtonClickListener implements OnClickListener {


		// onClickメソッド(ボタンクリック時イベントハンドラ)
    	public void onClick(View v){

    		// タグの取得
    		String tag = (String)v.getTag();

    		// メッセージ表示用
            String message  = "";
            TextView label = (TextView)findViewById(R.id.message);

            // 入力情報取得
			name = (EditText)findViewById(R.id.name);
			name.getText();
			StringName = name.getText().toString();
           

            // テーブルレイアウトオブジェクト取得
           TableLayout tablelayout = (TableLayout)findViewById(R.id.list);

            // テーブルレイアウトのクリア
            tablelayout.removeAllViews();

            // 該当DBオブジェクト取得
            db = helper.getWritableDatabase();

        	// 登録ボタンが押された場合
    		if(tag.equals("insert")){

                // テーブル作成
                try{

                	// SQL文定義
                    String sql
                         = "create table people(_id integer primary key autoincrement," +
                           "name text not null," +
                           "address text not null," +
                           "ID text not null)";
                    // SQL実行
                    db.execSQL(sql);

                    // メッセージ設定
                    message = "テーブルを作成しました！\n";

                }catch(Exception e){
                	message  = "テーブルは作成されています！\n";
                	Log.e("ERROR",e.toString());
                }

                // データ登録
                try{

                    // トランザクション制御開始
                    db.beginTransaction();

                    // 登録データ設定
                    ContentValues val = new ContentValues();
                    val.put("name", name.getText().toString());
                    val.put("address", address.getText().toString());
                    val.put("ID", ID.getText().toString());
                    // データ登録
                    db.insert("people", null, val);

                    // コミット
                    db.setTransactionSuccessful();

                    // トランザクション制御終了
                    db.endTransaction();

                	// メッセージ設定
                    message += "データを登録しました！";

                }catch(Exception e){
                	message  = "データ登録に失敗しました！";
                	Log.e("ERROR",e.toString());
                }
                //登録データをFINDmapActivityに渡す
                try{
                	Intent RegisterIntent =  new Intent(RegisterActivity.this,FINDmap_vol7_0Activity.class);

                	RegisterIntent.setData(PhotoImageUri);

                	Log.d(message11, "OK6"+PhotoImageUri);

                	RegisterIntent.putExtra( "name",StringName);
                	//RegisterIntent.putExtra( "address",StringAddress);
                	 Log.d(message11, "OK7"+StringName);

                	 setResult(RESULT_OK, RegisterIntent);
                	 if(SelectImg !=null){
                     	SelectImg.recycle();
                     	//SelectImg=null;
                     	SelectimgView.setImageDrawable(null);

                     	//Log.d(message11, "OK1"+SelectImg);

                     	finish();
                     }
                }catch(Exception e){
                	message  = "データ登録に失敗しました！";
                	Log.e("ERROR",e.toString());
                }

            // 更新ボタンが押された場合
    		}else if(tag.endsWith("update")){

    			// ファイルのデータ削除
    			try{
    				// 更新条件
    				String condition = null;
    				if(name != null && !name.equals("")){
    					condition = "name = '" + name.getText().toString() + "'";
    				}

                    // トランザクション制御開始
                    db.beginTransaction();

                    // 更新データ設定
                    ContentValues val = new ContentValues();
                   val.put("ID", ID.getText().toString());
                     val.put("name", name.getText().toString());
                    // データ更新
                    db.update("people", val, condition, null);

                    // コミット
                    db.setTransactionSuccessful();

                    // トランザクション制御終了
                    db.endTransaction();

                    // メッセージ設定
                    message = "データを更新しました！";
                }catch(Exception e){
                    // メッセージ設定
                    message = "データ更新に失敗しました！";
                    Log.e("ERROR",e.toString());
                }

        	// 削除ボタンが押された場合
    		}else if(tag.endsWith("delete")){

    			// ファイルのデータ削除
    			try{
    				// 削除条件
    				String condition = null;
    				if(name != null && !name.equals("")){
    					condition = "name = '" + name.getText().toString() + "'";
    				}

                    // トランザクション制御開始
                    db.beginTransaction();

                    // データ削除
                    db.delete("people", condition, null);

                    // コミット
                    db.setTransactionSuccessful();

                    // トランザクション制御終了
                    db.endTransaction();

                    // メッセージ設定
                    message = "データを削除しました！";
                }catch(Exception e){
                    // メッセージ設定
                    message = "データ削除に失敗しました！";
                    Log.e("ERROR",e.toString());
                }

            // 表示ボタンが押された場合
    		}else if(tag.equals("select")){

                // データ取得
                try{
                	// 該当DBオブジェクト取得
                    db = helper.getReadableDatabase();

                    // 列名定義
                    String columns[] = {"name","address","ID"};

                    // データ取得
                    Cursor cursor = db.query("people", columns, null, null, null, null, "name");

                    // テーブルレイアウトの表示範囲を設定
                    tablelayout.setStretchAllColumns(true);

                    // テーブル一覧のヘッダ部設定
                    TableRow headrow = new TableRow(RegisterActivity.this);
                    TextView headtxt1 = new TextView(RegisterActivity.this);
                    headtxt1.setText("名前");
                    headtxt1.setGravity(Gravity.CENTER_HORIZONTAL);
                    headtxt1.setWidth(60);
                    TextView headtxt2 = new TextView(RegisterActivity.this);
                    headtxt2.setText("住所");
                    headtxt2.setGravity(Gravity.CENTER_HORIZONTAL);
                    headtxt2.setWidth(100);
                    TextView headtxt3 = new TextView(RegisterActivity.this);
                    headtxt3.setText("ID");
                    headtxt3.setGravity(Gravity.CENTER_HORIZONTAL);
                    headtxt3.setWidth(60);
                    headrow.addView(headtxt1);
                    headrow.addView(headtxt2);
                    headrow.addView(headtxt3);
                    tablelayout.addView(headrow);

                    // 取得したデータをテーブル明細部に設定
                    while(cursor.moveToNext()){

                        TableRow row = new TableRow(RegisterActivity.this);
                        TextView nametxt = new TextView(RegisterActivity.this);
                        nametxt.setGravity(Gravity.CENTER_HORIZONTAL);
                        nametxt.setText(cursor.getString(0));
                        TextView addresstxt = new TextView(RegisterActivity.this);
                        addresstxt.setGravity(Gravity.CENTER_HORIZONTAL);
                        addresstxt.setText(cursor.getString(1));
                        TextView IDtxt = new TextView(RegisterActivity.this);
                        IDtxt.setGravity(Gravity.CENTER_HORIZONTAL);
                        IDtxt.setText(cursor.getString(2));
                        row.addView(nametxt);
                        row.addView(addresstxt);
                        row.addView(IDtxt);
                        tablelayout.addView(row);

                        // メッセージ設定
                        message = "データを取得しました！";
                    }

                }catch(Exception e){
                	// メッセージ設定8
                    message = "データ取得に失敗しました！";
                    Log.e("ERROR",e.toString());
                }
    		}

            // DBオブジェクトクローズ
            db.close();

            // メッセージ表示
            label.setText(message);
    	}
    }



}
