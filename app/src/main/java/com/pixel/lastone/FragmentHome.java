package com.pixel.lastone;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Shailesh on 04-Apr-16.
 */
public class FragmentHome extends Libs {

    public static String admNoImportedString = null;
    public static String homeNameString = null;
    SaveSharedPreference saveSharedPreference;
    TextView homeName,navHeaderName;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String JSON_STRING = "";
    View  navRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    public void setAdmissionNo(String AdmNo) {
        admNoImportedString = AdmNo;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        saveSharedPreference = new SaveSharedPreference();

        homeName = (TextView) getActivity().findViewById(R.id.home_name);
        BackgroundTask backgroundTask = new BackgroundTask(getContext());
        backgroundTask.execute(admNoImportedString);

    }

    public class BackgroundTask extends AsyncTask<String, Void, String> {
        String jsonGetPatientDetailsURL;
        Context context;

        BackgroundTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            jsonGetPatientDetailsURL = "http://mediworld.orgfree.com/jsonGetPatientDetails.php";
        }

        @Override
        protected String doInBackground(String... params) {
            String PATIENT_ADM_NO = params[0];
            if(PATIENT_ADM_NO!=null) {
                try {
                    URL url = new URL(jsonGetPatientDetailsURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String data = URLEncoder.encode("PATIENT_ADM_NO", "UTF-8") + "=" + URLEncoder.encode(PATIENT_ADM_NO, "UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((JSON_STRING = bufferedReader.readLine()) != null) {

                        stringBuilder.append(JSON_STRING + "\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return JSON_STRING = stringBuilder.toString().trim();


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Void... avoid) {
            super.onProgressUpdate(avoid);
        }

        @Override
        protected void onPostExecute(String result) {
            JSON_STRING = result;

            if (JSON_STRING != null) {
                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    jsonArray = jsonObject.getJSONArray("Patient_Details_From_Server");
                    JSONObject JO = jsonArray.getJSONObject(0);
                    homeNameString = JO.getString("Name");
                    homeName.setText(homeNameString);
                    saveSharedPreference.setName(context, homeNameString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (JSON_STRING == "") {
                Message.message(context, "JSON_STRING is null");
            }
        }
    }

}
