package se.jeeves.xperiencestatemachine.access.util;



import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpParams;

/**
 * Created by max.gabrielsson on 2014-10-15.
 */
public class JsbJSONInvoker {
    public static JSONObject invoke(String invokeUrl, String urlParameters, String userName, String password) {

        String response = "";

        try {
            URL url = new URL(invokeUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            String authorizationString = "Basic " + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.DEFAULT);

            connection.setRequestProperty("Authorization", authorizationString.replace("\n", ""));
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
                response = convertInputStreamToString(inputStream);
            } else {
                Log.e("Error....", "Failed to download file");
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            return new JSONObject(response);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e);
        }

        return null;

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));

        String line;
        StringBuilder result = new StringBuilder();

        while((line = bufferedReader.readLine()) != null){
            result.append(line);
        }

        /* Close Stream */
        inputStream.close();

        return result.toString();
    }

}