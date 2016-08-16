package se.jeeves.xperiencestatemachine.access.impl;

import android.os.SystemClock;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import se.jeeves.xperiencestatemachine.MyFirebaseInstanceIdService;
import se.jeeves.xperiencestatemachine.access.definition.MethodCall;

/**
 * Created by max.gabrielsson on 16/08/16.
 */
public class WatchdogCall implements MethodCall {

    @Override
    public JSONObject getParameters() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("Id", FirebaseInstanceId.getInstance().getToken());
        return json;
    }

    @Override
    public String getAlias() {
        return "Watchdog";
    }

    @Override
    public String getService() {
        return "UpdateWatchdog";
    }
}
