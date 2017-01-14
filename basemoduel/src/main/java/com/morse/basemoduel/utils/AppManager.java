package com.morse.basemoduel.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @author libiao
 * @ClassName: AppManager
 * @Description:用来管理所有的Activity.每一个Activity都会加载到activityList集合当中。 每一个Service都会加载到serviceList当中
 * ,
 * 当程序需要退出的时候关闭所有的Activity和所有的service
 * @date 2016-3-31 上午10:07:51
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack == null) {
            return null;
        }
        if (activityStack.size() == 0) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            finishActivity(activity);
        }

    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        try {
            if (activity != null) {
                activityStack.remove(activity);
                activity.finish();
                activity = null;
            }
        } catch (Exception e) {

        }

    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        Iterator<Activity> it = activityStack.iterator();
        Activity activity = null;
        while (it.hasNext()) {
            activity = it.next();
            if (activity == null) {
                it.remove();
                continue;
            }
            if (activity.getClass().equals(cls)) {
                it.remove();
                activity.finish();
            }
        }
        /*for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}*/
    }

    /**
     * 结束
     */
    public void finishMutilActivity(Activity activity) {
        if (activity == null) return;
        try {
            Iterator<Activity> it = activityStack.iterator();
            Activity activityItem = null;
            int size = 0;
            while (it.hasNext()) {
                activityItem = it.next();
                if (activityItem == null) {
                    it.remove();
                    continue;
                }
                /*if(activityItem == activity){
                    continue;
                }*/
                if (activityItem.getClass().getName().equals(activity.getClass().getName())) {
                    size++;
                    if (size > 1) {
                        activity.finish();
                        it.remove();
                        break;
                    }
                }
            }
        } catch (Exception e) {

        }

    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        try {
            if (activityStack != null) {
                for (int i = 0, size = activityStack.size(); i < size; i++) {
                    if (null != activityStack.get(i)) {
                        activityStack.get(i).finish();
                    }
                }
                activityStack.clear();
            }
        } catch (Exception e) {

        }

    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
//        MyApplication.mLoginState = false;
//        MyApplication.isQrCodeActivity = false;
        try {
//            MobclickAgent.onKillProcess(context);
            finishAllActivity();
            //ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            //activityMgr.killBackgroundProcesses(context.getPackageName());
            //System.exit(0);
        } catch (Exception e) {
        }
    }

    /**
     * 得到当前类名
     */
    public String getClassName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        RunningTaskInfo cinfo = runningTasks.get(0);
        ComponentName component = cinfo.topActivity;
        return component.getClassName();
    }

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 结束
     */
    public void onActivityStatusChg() {
        try {
            Iterator<Activity> it = activityStack.iterator();
//            ICommonActivity activityItem = null;
            int size = 0;
            boolean stoped = true;
            while (it.hasNext()) {
//                activityItem = (ICommonActivity) it.next();
//                if (activityItem == null) {
//                    it.remove();
//                    continue;
//                }
//                if (!activityItem.isStoped()) {
//                    stoped = false;
//                    return;
//                }
            }
//            Lg.d("onActivityStatusChg stoped = " + stoped);
//            SPreferenceUtil.setValue("keep_alive_is_stoped", stoped);
//            if(stoped){
//                KeepAliveUtils.getInstance().startAliveActivity(MyApplication.getAppContext());
//            }else{
//                KeepAliveUtils.getInstance().finishAliveActivity();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
