/**
 *  modified by bowang
 */
package us.nijikon.livelylauncher.live2dHelpers;

import android.app.Activity;
import android.app.Application;
import android.app.LauncherActivity;
import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import jp.live2d.Live2D;
import jp.live2d.framework.L2DViewMatrix;
import jp.live2d.framework.Live2DFramework;
import us.nijikon.livelylauncher.launcher.Launcher;


public class LAppLive2DManager
{
	
	static public final String TAG = "SampleLive2DManager";

	private LAppView 				view;						

	
	private LAppModel model;


	
//	private int 					modelCount		=-1;
	private boolean 				reloadFlg;



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



	//=========================================================
	
	//=========================================================
	
	public LAppView  createView(Activity act)
	{
		
		view = new LAppView( act ) ;
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
		view.setupView(width,height);
		changeModel();
	}


	//=========================================================
	
	//=========================================================
	
	public void changeModel()
	{
		reloadFlg=true;

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
		model.startRandomMotion(LAppDefine.MOTION_NULL,LAppDefine.PRIORITY_FORCE);
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
