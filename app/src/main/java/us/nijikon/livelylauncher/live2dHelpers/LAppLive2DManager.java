/**
 *  modified by bowang
 */
package us.nijikon.livelylauncher.live2dHelpers;

import android.app.Activity;
import android.app.Application;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import jp.live2d.Live2D;
import jp.live2d.framework.L2DViewMatrix;
import jp.live2d.framework.Live2DFramework;
import jp.live2d.utils.android.SimpleImage;
import us.nijikon.livelylauncher.R;
import us.nijikon.livelylauncher.launcher.Launcher;


public class LAppLive2DManager
{
	
	static public final String TAG = "SampleLive2DManager";



	private LAppView view;
	
	private LAppModel model;

	private boolean reloadFlg;



	public void updateBackground(String path, boolean inapp){
		LAppDefine.back_image_path = path;
		LAppDefine.inApp =inapp;
		try {
			this.view.getRenderer().getBackground().setInput(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LAppLive2DManager()
	{
		Live2D.init();
		Live2DFramework.setPlatformManager(new PlatformManager());
		model = new LAppModel();
	}

	public void releaseModel()
	{
		model.release();
	}

	public void update(GL10 gl) {
		view.update();
		if (reloadFlg){
            reloadFlg = false;
			try {
				model.load(gl, LAppDefine.MODEL_EPSILON);
			} catch (Exception e) {
				Log.e(TAG,"ERRRRRRRRRRRRR");
			}
	    }

	}


    public LAppModel getModel()
	{
		return model;
	}

	
	public LAppView  createView(Activity act)
	{
		
		view = new LAppView(act) ;
		view.setLive2DManager(this);
		view.startAccel(act);
		return view ;
	}


	
	public void onResume()
	{
		if(LAppDefine.DEBUG_LOG) Log.d(TAG, "onResume");
		view.onResume();
	}


	
	public void onPause()
	{
		if(LAppDefine.DEBUG_LOG) Log.d(TAG, "onPause");
		view.onPause();
	}


	
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		if(LAppDefine.DEBUG_LOG) Log.d(TAG, "onSurfaceChanged " + width + " " + height);
		view.setupView(width, height);
		changeModel();

		//view.update();
	}


	//=========================================================
	
	//=========================================================
	
	public void changeModel()
	{
		reloadFlg = true;

	}


	//=========================================================
	
	//=========================================================
	
	public boolean tapEvent(float x,float y)
	{
		if(LAppDefine.DEBUG_LOG) Log.d(TAG, "tapEvent view x:" + x + " y:" + y);
		if(model.hitTest(LAppDefine.HIT_AREA_HEAD,x,y)){
			if(LAppDefine.DEBUG_LOG) Log.d(TAG, "Tap body.");
			model.startRandomMotion(LAppDefine.MOTION_NOD, LAppDefine.PRIORITY_NORMAL);
		}
		else if(model.hitTest(LAppDefine.HIT_AREA_BODY, x, y))
			{
				if(LAppDefine.DEBUG_LOG) Log.d(TAG, "Tap body.");
				model.startRandomMotion(LAppDefine.MOTION_SHY, LAppDefine.PRIORITY_FORCE );
			}
		return true;
	}

	public static void readWallPaper(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences("LIVELYLAUNCHER_WALLPAPPER", Context.MODE_PRIVATE);
		String path = sharedPreferences.getString("LivelyLauncherWallPath", LAppDefine.back_image_path);
		boolean inApp = sharedPreferences.getBoolean("LivelyLauncherWalInApp",true);
		if(path!=null){
			LAppDefine.back_image_path=path;
			LAppDefine.inApp = inApp;
		}else{
			LAppDefine.back_image_path = "walltest.png";
			LAppDefine.inApp = true;
		}
	}

	public static void writeWallPaper(Context context){
		SharedPreferences.Editor editor = context.getSharedPreferences("LIVELYLAUNCHER_WALLPAPPER",Context.MODE_PRIVATE).edit();
		editor.putString("LivelyLauncherWallPath",LAppDefine.back_image_path);
		editor.putBoolean("LivelyLauncherWalInApp", LAppDefine.inApp);
		editor.apply();
	}

	
	public void flickEvent(float x,float y)
	{
		if(LAppDefine.DEBUG_LOG) Log.d(TAG, "flick x:" + x + " y:" + y);
		if(model.hitTest(LAppDefine.HIT_AREA_HEAD,x,y)){
			model.startRandomMotion(LAppDefine.MOTION_POSITIVE,LAppDefine.PRIORITY_NORMAL);
		}
		else if(model.hitTest(LAppDefine.HIT_AREA_BODY,x,y)){
			model.startRandomMotion(LAppDefine.MOTION_HOMESCREEM,LAppDefine.PRIORITY_FORCE);
		}
	}
	
	public void maxScaleEvent()
	{
		if(LAppDefine.DEBUG_LOG) Log.d(TAG, "Max scale event.");
	}
	
	public void minScaleEvent()
	{
		if(LAppDefine.DEBUG_LOG) Log.d(TAG, "Min scale event.");
	}

	public void shakeEvent()
	{
		if(LAppDefine.DEBUG_LOG) Log.d(TAG, "Shake event.");
		model.startRandomMotion(LAppDefine.MOTION_SHAKE,LAppDefine.PRIORITY_FORCE);
	}


	public void setAccel(float x,float y,float z)
	{
		model.setAccel(x,y,z);
	}

	public void setDrag(float x,float y)
	{
		model.setDrag(x,y);
	}

	public L2DViewMatrix getViewMatrix()
	{
		return view.getViewMatrix();
	}

	public void startMotion(String name){
		model.startRandomMotion(name,LAppDefine.PRIORITY_FORCE);
	}
}
