package com.ione.iseller;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Author: Shiv Bhushan Tripathi.
 * Date Started: 24/ 03/ 2017.
 * Description: Class that handle welcome and login Screen.
 * @copyright iOne: A company of Ikai.
 */

class JSONParser {

    private String charset = "UTF-8";
    private HttpsURLConnection conn;

    JSONObject makeHttpRequest(String url, String method,
                                      HashMap<String, String> params) {

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0){
                    sbParams.append("&");
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), charset));

            } catch (UnsupportedEncodingException e) {
                return null;
            }
            i++;
        }

        URL urlObj;

        if (method.equals("POST")) {
            // request method is POST
            try {
                urlObj = new URL(url);

                conn = (HttpsURLConnection) urlObj.openConnection();

                conn.setDoOutput(true);

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Accept-Charset", charset);

//                conn.setReadTimeout(15000);
//                conn.setConnectTimeout(25000);

                conn.connect();

                String paramsString = sbParams.toString();

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();

            } catch (IOException e) {
                return null;
            }
        }
        else if(method.equals("GET")){
            // request method is GET

            if (sbParams.length() != 0) {
                url += "?" + sbParams.toString();
            }

            try {
                urlObj = new URL(url);

                conn = (HttpsURLConnection) urlObj.openConnection();

                conn.setDoOutput(false);

                conn.setRequestMethod("GET");

                conn.setRequestProperty("Accept-Charset", charset);

//                conn.setConnectTimeout(25000);

                conn.connect();

            } catch (IOException e) {
                return null;
            }

        }
        try {
            StringBuilder result;
            if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                try {
                    //Receive the response from the server
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                } catch (IOException e) {
                    return null;
                }

                conn.disconnect();
                JSONObject jObj;
                // try parse the string to a JSON object
                try {
                    jObj = new JSONObject(result.toString());
                } catch (JSONException e) {
                    return null;
                }

                // return JSON Object
                return jObj;
            } else {
                return null;
            }

        } catch (IOException e) {
            return null;
        }
    }
}
