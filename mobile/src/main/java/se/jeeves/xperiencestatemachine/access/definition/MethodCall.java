package se.jeeves.xperiencestatemachine.access.definition;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by max.gabrielsson on 15/08/16.
 */
public interface MethodCall {

    JSONObject getParameters() throws JSONException;

    String getAlias();

    String getService();
}
