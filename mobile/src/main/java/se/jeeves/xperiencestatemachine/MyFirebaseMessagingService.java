package se.jeeves.xperiencestatemachine;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import se.jeeves.xperiencestatemachine.access.definition.Access;
import se.jeeves.xperiencestatemachine.access.impl.WatchdogCall;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String SENDER_ID = "1060277659547";
    private ServiceHandler mServiceHandler; // Handler that receives messages from the thread
    private Looper mServiceLooper;

    public MyFirebaseMessagingService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        android.os.Debug.waitForDebugger();

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Thread.NORM_PRIORITY);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        RemoteMessage.Notification n = remoteMessage.getNotification();

        Message message = mServiceHandler.obtainMessage();

        String state = remoteMessage.getData().get("state");
        if(state != null) {
            message.getData().putInt("state", Integer.valueOf(state));
        }

        mServiceHandler.handleMessage(message);

        replyToAppServer();


        stopSelf(message.arg1);
    }

    private void replyToAppServer() {

        Access access = new Access();
        WatchdogCall watchdogCall = new WatchdogCall();
//        JSONObject json = access.invoke(watchdogCall);
        //Log.d("ACCESS", json.toString());

    }


}
