package com.example.findmapvol7_0;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

public class FINDmap_vol7_0Activity extends MapActivity {
	private static final int REGISTER_SEND = 0;
	private static final String TAG = "FINDmapvol6Activit";
	private static final boolean DEBUG = true;
	private Bitmap Selectimg;
	private static final String ACTION_LOCATION_UPDATE = "com.android.practice.map.ACTION_LOCATION_UPDATE";
	protected String  stringName = null;
	protected BitmapDrawable drawableSlectImage;
	//private static String stringAddress =null;

	private MapController mMapController;
	private MapView mMapView;
	private MyLocationOverlay mMyLocationOverlay;
	private MyItemizedOverlay mItemizedOverlay;
	String message = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findmap_vol7_0);
		 initMapSet();
    }

	@Override
	protected void onResume() {
		super.onResume();
		setOverlays();
		setIntentFilterToReceiver();
		requestLocationUpdates();
	}

	@Override
	protected void onPause() {
		super.onPause();
		resetOverlays();
		removeUpdates();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	@Override
	protected boolean isRouteDisplayed() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
	//メニュー画面の生成
		 @Override
		    public boolean onCreateOptionsMenu(Menu menu) {
		        super.onCreateOptionsMenu(menu);
		        MenuItem camera = menu.add("カメラ起動");
		        MenuItem registration = menu.add("登録画面");

		        //カメラボタンを押した時の処理
		        OnMenuItemClickListener cameralistener = new OnMenuItemClickListener(){
		            public boolean onMenuItemClick(MenuItem item){
		            	//インテンドを使用しCamera Activityを呼び出す
		            	 Intent intent =  new Intent(FINDmap_vol7_0Activity.this, CameraActivity.class);
		                startActivity(intent);

		                return false;
		            }
		        };

		        //登録ボタンを押した時の処理
		        OnMenuItemClickListener registrationlistener = new OnMenuItemClickListener(){
		            public boolean onMenuItemClick(MenuItem item){
		            	//インテンドを使用しRegistration Activityを呼び出す
		            	Intent intent =  new Intent(FINDmap_vol7_0Activity.this, RegisterActivity.class);
		            	startActivityForResult(intent,REGISTER_SEND );

		                return false;
		            }
		        };

		        camera.setOnMenuItemClickListener(cameralistener);
		        registration.setOnMenuItemClickListener(registrationlistener);
		        return true;
		    }


		 //登録画面からの返信の処理
		 protected void onActivityResult(int requestCode, int resultCode, Intent data){

			if(requestCode == REGISTER_SEND && resultCode == RESULT_OK){
				 try {

			      	Uri PhotoImageUri = data.getData();
			      	Log.d(message, "オッケー"+data);

			       stringName = data.getExtras().getString("name");
			       //stringAddress = data.getExtras().getString("address");
			        	Log.d(message, "オッケー2"+stringName);
			        	//Log.d(message, "オッケー3"+stringAddress);

			      	InputStream in;
			      	in = getContentResolver().openInputStream(data.getData());
			      		Selectimg = BitmapFactory.decodeStream(in);
					 	Toast.makeText(this,stringName+" 登録できたよ！！", Toast.LENGTH_LONG).show();

					 	Log.d(message,"OK"+Selectimg.getWidth());
			  		Log.d(message, "OK3"+Selectimg);
			  		Log.d(message, "OK4"+PhotoImageUri);

					} catch (FileNotFoundException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
			 }
		 }

	private void initMapSet(){
		//MapView objectの取得
        mMapView = (MapView)findViewById(R.id.MapView);
        //MapView#setBuiltInZoomControl()でZoom controlをbuilt-in moduleに任せる
        mMapView.setBuiltInZoomControls(true);
        //MapController objectを取得
        mMapController = mMapView.getController();
	}
	private void setOverlays(){
        //User location表示用のMyLocationOverlay objectを取得
		mMyLocationOverlay = new MyLocationOverlay(this, mMapView);
		//初めてLocation情報を受け取った時の処理を記載
		//試しにそのLocationにanimationで移動し、zoom levelを19に変更
        mMyLocationOverlay.runOnFirstFix(new Runnable(){
        	public void run(){
        		GeoPoint gp = mMyLocationOverlay.getMyLocation();
				mMapController.animateTo(gp);
				mMapController.setZoom(19);
        	}
        });
        drawableSlectImage = new BitmapDrawable(Selectimg);
       Log.d(message, "OK4"+drawableSlectImage);
      //LocationManagerからのLocation update取得
      		mMyLocationOverlay.enableMyLocation();

      		//OverlayItemを表示するためのMyItemizedOverlayを拡張したclassのobjectを取得
      		mItemizedOverlay = new MyItemizedOverlay(drawableSlectImage, this);

      		//overlayのlistにMyLocationOverlayを登録
              List<Overlay> overlays = mMapView.getOverlays();
              overlays.add(mMyLocationOverlay);
              //overlayのlistに拡張したItemizedOverlayを拡張したclassを登録
              overlays.add(mItemizedOverlay);
      	}

	private void resetOverlays(){
        //LocationManagerからのLocation update情報を取得をcancel
		mMyLocationOverlay.disableMyLocation();

		//overlayのlistからMyLocationOverlayを削除
		List<Overlay> overlays = mMapView.getOverlays();
        overlays.remove(mMyLocationOverlay);
		//overlayのlistからItemizedOverlayを拡張したclassを削除
        overlays.remove(mItemizedOverlay);
	}

	private void setIntentFilterToReceiver(){
		final IntentFilter filter = new IntentFilter();
    	filter.addAction(ACTION_LOCATION_UPDATE);
    	registerReceiver(new LocationUpdateReceiver(), filter);
	}


	private void requestLocationUpdates(){
		final PendingIntent requestLocation = getRequestLocationIntent(this);
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        for(String providerName: lm.getAllProviders()){
			if(lm.isProviderEnabled(providerName)){
				lm.requestLocationUpdates(providerName, 0, 0, requestLocation);
				if(DEBUG){
					//Toast.makeText(this, "Request Location Update", Toast.LENGTH_SHORT).show();
					if(DEBUG)Log.d(TAG, "Provider: " + providerName);
				}
			}
		}
	}

	private PendingIntent getRequestLocationIntent(Context context){
		return PendingIntent.getBroadcast(context, 0, new Intent(ACTION_LOCATION_UPDATE),
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private void removeUpdates(){
    	final PendingIntent requestLocation = getRequestLocationIntent(this);
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		lm.removeUpdates(requestLocation);
		if(DEBUG)Toast.makeText(this, "Remove update", Toast.LENGTH_SHORT).show();
    }

	public class LocationUpdateReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action == null){
				return;
			}
			if(action.equals(ACTION_LOCATION_UPDATE)){
				//Location情報を取得
				Location loc = (Location)intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
				if(loc != null){
					//試しにMapControllerで現在値に移動する
					mMapController.animateTo(new GeoPoint((int)(loc.getLatitude() * 1E6), (int)(loc.getLongitude() * 1E6)));
					//if(DEBUG)Toast.makeText(context, "latitude:" + loc.getLatitude() + "\nlongitude:" + loc.getLongitude(), Toast.LENGTH_SHORT).show();
				}
			}
		}

	}


}

