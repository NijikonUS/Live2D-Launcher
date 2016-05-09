/**
 *  modified by bowang
 */
package us.nijikon.livelylauncher.live2dHelpers;


public class LAppDefine
{
	
	public static boolean DEBUG_LOG=true;
	public static boolean DEBUG_TOUCH_LOG=false;
	public static boolean DEBUG_DRAW_HIT_AREA=false;

	public static String back_image_path = "walltest.png";

	public static boolean inApp = true;
	
	
	public static final float VIEW_MAX_SCALE = 1f;
	public static final float VIEW_MIN_SCALE = 1f;
//	public static final float VIEW_MAX_SCALE = 0.8f;
//	public static final float VIEW_MIN_SCALE = 0.8f;

	public static final float VIEW_LOGICAL_LEFT = -1;
	public static final float VIEW_LOGICAL_RIGHT = 1;

	public static final float VIEW_LOGICAL_MAX_LEFT = -2;
	public static final float VIEW_LOGICAL_MAX_RIGHT = 2;
	public static final float VIEW_LOGICAL_MAX_BOTTOM = -2;
	public static final float VIEW_LOGICAL_MAX_TOP = 2;


	public static final String MODEL_EPSILON    = "live2d/epsilon/Epsilon.model.json";


	public static final String MOTION_GROUP_IDLE		="idle";
	public static final String MOTION_NULL             ="test";
	public static final String MOTION_POSITIVE         ="positive";
	public static final String MOTION_NOD				="nodding";
	public static final String MOTION_SAD    			="sad";
	public static final String MOTION_ANGRY			="angry";
	public static final String MOTION_HOMESCREEM		="homeScreenRandom";
	public static final String MOTION_SHY           ="shy";
	public static final String MOTION_SHAKE = "shake";
	static final String HIT_AREA_HEAD		="head";
	static final String HIT_AREA_BODY		="body";



	public static final int PRIORITY_IDLE		= 1;
	public static final int PRIORITY_NORMAL		= 2;
	public static final int PRIORITY_FORCE		= 3;

}
