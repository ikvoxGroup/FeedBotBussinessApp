package ownerapp.com.ikvox.pratikriya.gcmNotification;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
 public static int count;
	@Override
	public void onReceive(Context context, Intent intent) {
		count =0;
		ComponentName comp = new ComponentName(context.getPackageName(),
				GCMNotificationIntentService.class.getName());
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
		count ++;
	}
}
