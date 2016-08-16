package se.jeeves.xperiencestatemachine.access.definition;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import se.jeeves.xperiencestatemachine.access.util.JsbJSONInvoker;

/**
 * Created by max.gabrielsson on 15/08/16.
 */
public class Access {
    private static final String server = "http://server:9000/jsb-url/";
    private static final String TAG = "se.jeeves.Access";
    private String signature;
    private String password;

    public JSONObject invoke(MethodCall methodCall) {

        String fullUrl = server + methodCall.getAlias() + "/" +methodCall.getService() + "/json";
        try {

            Log.d(TAG, "URL (" + fullUrl + ") requested with parameters (" +
                getRequestString(methodCall.getParameters()) + ") by " + signature);
            return JsbJSONInvoker.invoke(fullUrl, getRequestString(methodCall.getParameters()), signature, password);
        } catch (JSONException e) {
            Log.d(TAG, "Something wrong with JSON format of MethodCall", e);
        }
        return new JSONObject();
    }

    private String getRequestString(JSONObject jsonObject) {
        Iterator<String> it = jsonObject.keys();
        StringBuffer sb = new StringBuffer();
        sb.append("?");
        while(it.hasNext()) {
            if(sb.length()>1) sb.append("&");
            try {
                String key = it.next();
                sb.append(key);
                sb.append("=");
                sb.append(getStringRepresentation(jsonObject.get(key)));

            } catch (JSONException e) {
                Log.d(TAG, "Cannot get string representation of jsonObject for key.", e);
                //Disregard the key, will be considered null in JSB key=&key2=....
            }
        }
        return sb.toString();
    }

    private String getStringRepresentation(Object object) throws JSONException {
        return object.toString();
    }
}
