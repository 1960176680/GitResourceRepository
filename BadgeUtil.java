package com.northdoo_work.utils;

import java.lang.reflect.Field;

import com.northdoo_work.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

//BadgeUtil provides static utility methods to set "badge count" on Launcher (by Samsung, LG). 
//Currently, it's working from Android 4.0. 
//But some devices, which are released from the manufacturers, are not working.

/**
 * github - 在桌面图标显示未读消息数量 <br/>
 * 支持：三星 LG 小米 索尼
 * 
 * @author GitHub
 */
public class BadgeUtil
{

	private static final String ACTION_BADGE_COUNT_UPDATE = "android.intent.action.BADGE_COUNT_UPDATE";

	private static final String EXTRA_BADGE_COUNT = "badge_count";

	private static final String EXTRA_BADGE_COUNT_PACKAGE_NAME = "badge_count_package_name";

	private static final String EXTRA_BADGE_COUNT_CLASS_NAME = "badge_count_class_name";

	/**
	 *  - 原生适配 三星 LG  
	 * <br/>Other - 小米 索尼
	 * @param context
	 *            The context of the application package.
	 * @param count
	 *            Badge count to be set
	 */
	public static void setBadgeCount(Context context, int count)
	{
		if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {  //适配小米
            sendToXiaoMi(context, count);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) { // 适配索尼
            sendToSony(context, count);
        }else {
			//适配三星 LG
        	Intent badgeIntent = new Intent(ACTION_BADGE_COUNT_UPDATE);
        	badgeIntent.putExtra(EXTRA_BADGE_COUNT, count);
        	badgeIntent.putExtra(EXTRA_BADGE_COUNT_PACKAGE_NAME, context.getPackageName());
        	badgeIntent.putExtra(EXTRA_BADGE_COUNT_CLASS_NAME, getLauncherClassName(context));
        	context.sendBroadcast(badgeIntent);
		}
	}

	/**
	 * Reset badge count. The badge count is set to "0"
	 * 
	 * @param context
	 *            The context of the application package.
	 */
	public static void resetBadgeCount(Context context)
	{
		setBadgeCount(context, 0);
	}

	/**
	 * Retrieve launcher activity name of the application from the context
	 *
	 * @param context
	 *            The context of the application package.
	 * @return launcher activity name of this application. From the
	 *         "android:name" attribute.
	 */
	private static String getLauncherClassName(Context context)
	{
		PackageManager packageManager = context.getPackageManager();

		Intent intent = new Intent(Intent.ACTION_MAIN);
		// To limit the components this Intent will resolve to, by setting an
		// explicit package name.
		intent.setPackage(context.getPackageName());
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		// All Application must have 1 Activity at least.
		// Launcher activity must be found!
		ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

		// get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
		// if there is no Activity which has filtered by CATEGORY_DEFAULT
		if (info == null)
		{
			info = packageManager.resolveActivity(intent, 0);
		}

		return info.activityInfo.name;
	}
	
	/**
	 * 适配 小米  索尼 
	 * @param context
	 * @param count
	 */
	public static void setOtherBadgeCount(Context context, int count)
	{
		sendToXiaoMi(context, count);
		sendToSony(context, count);
	}
	
	/**
	 * -适配小米  在桌面显示维度消息数量
	 * @param context
	 * @param number
	 */
	public static void sendToXiaoMi(Context context, int number)
	{
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = null;
		boolean isMiUIV6 = true;
		try
		{
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			builder.setContentTitle("您有" + number + "未读消息");
			builder.setTicker("您有" + number + "未读消息");
			builder.setAutoCancel(true);
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setDefaults(Notification.DEFAULT_LIGHTS);
			notification = builder.build();
			// 以上代码为notification的初始化信息，在实际应用中，可以单独使用

			Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
			Object miuiNotification = miuiNotificationClass.newInstance();
			Field field = miuiNotification.getClass().getDeclaredField("messageCount");
			field.setAccessible(true);

			field.set(miuiNotification, number);// 设置信息数
			field = notification.getClass().getField("extraNotification");
			field.setAccessible(true);

			field.set(notification, miuiNotification);
			// Toast.makeText(this, "Xiaomi=>isSendOk=>1",
			// Toast.LENGTH_LONG).show();
		} catch (ClassNotFoundException e)//不是小米的手机
		{
			//e.printStackTrace();
			return ;
		}catch (Exception e) 
		{
			// miui 6之前的版本
			isMiUIV6 = false;
			Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
			localIntent.putExtra("android.intent.extra.update_application_component_name", context.getPackageName());
			localIntent.putExtra("android.intent.extra.update_application_message_text", number);
			context.sendBroadcast(localIntent);
		} finally
		{
			if (notification != null && isMiUIV6)
			{
				// miui6以上版本需要使用通知发送
				nm.notify(101010, notification);
			}
		}

	}
	
	/**
	 * -适配索尼  在桌面显示维度消息数量
	 * <br/>添加权限&lt;uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" />
	 * @param context
	 * @param number
	 */
	public static void sendToSony(Context context, int number)
	{
		boolean isShow = true;
		if ("0".equals(number))
		{
			isShow = false;
		}
		Intent localIntent = new Intent();
		localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);// 是否显示
		localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
		//localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", context.getPackageName());// 启动页
		localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", number);// 数字
		localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());// 包名
		context.sendBroadcast(localIntent);
	}
}