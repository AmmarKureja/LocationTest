package com.ammarkureja.locationtest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ammar on 7/10/2017.
 */

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

public class GetMatrixJsonData extends AsyncTask<String, Void, String > {

    private final onDataAvailable callBack;
    private DownloadStatus mDownloadStatus;
    private static final String TAG = "GetMatrixJsonData";

    interface onDataAvailable {
        void onDataAvailable(String parsedData, DownloadStatus status);
    }

    GetMatrixJsonData(onDataAvailable mCallBack) {
        this.callBack = mCallBack;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    @Override
    protected void onPostExecute(String result) {

        if (callBack != null) {
            callBack.onDataAvailable(result, mDownloadStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: response code is "+response);
            if (response == HttpURLConnection.HTTP_OK) {

                StringBuilder result = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                String json = result.toString();
                Log.d("JSON", json);
                JSONObject root = new JSONObject(json);
                JSONArray array_rows = root.getJSONArray("rows");
                Log.d("JSON", "array_rows:" + array_rows);
                JSONObject object_rows = array_rows.getJSONObject(0);
                Log.d("JSON", "object_rows:" + object_rows);
                JSONArray array_elements = object_rows.getJSONArray("elements");
                Log.d("JSON", "array_elements:" + array_elements);
                JSONObject object_elements = array_elements.getJSONObject(0);
                Log.d("JSON", "object_elements:" + object_elements);
                JSONObject object_duration = object_elements.getJSONObject("duration");
                JSONObject object_distance = object_elements.getJSONObject("distance");

                Log.d("JSON", "object_duration:" + object_duration);
                mDownloadStatus = DownloadStatus.OK;
                return object_duration.getString("value") + "," + object_distance.getString("value");
            }

        }

        catch (MalformedURLException e) {
            Log.d(TAG, "doInBackground: problem with converting string to url "+e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "doInBackground: problem with opening connection "+ e.getMessage());
        } catch (SecurityException e) {
            Log.d(TAG, "doInBackground: Security Exception, need permission "+e.getMessage());
        } catch (JSONException e) {
            Log.d(TAG, "doInBackground: JSON parsing exception "+e.getMessage());
        }

        finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream "+ e.getMessage() );
                }
            }
        }
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
