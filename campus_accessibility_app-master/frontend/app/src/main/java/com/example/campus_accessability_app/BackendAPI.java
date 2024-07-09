package com.example.campus_accessability_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BackendAPI {
    private String BACKEND_URL = "https://testinground.eu.pythonanywhere.com/";
    private OkHttpClient client = new OkHttpClient();
    private static SharedPreferences sharedPref;
    private static String token;
    public Context context;
    public static Map<String, Building> buildings = new LinkedHashMap<>();
    public static ArrayList<Report> reports = new ArrayList();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public BackendAPI(Context context) {
        sharedPref = context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", null);
        this.context = context;
    }

    public void saveAndSetToken(String token) {
        BackendAPI.token = token;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", token);
        editor.apply();
    }

    private void postData(String path, JSONObject json, Callback callback) {
        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request.Builder request = new Request.Builder().url(BACKEND_URL + path).post(body);
        if (token != null) {
            request.addHeader("token", token);
        }
        Call call = client.newCall(request.build());
        call.enqueue(callback);
    }

    private void getData(String path, Map<String,String> params, Callback callback) {
        HttpUrl.Builder url = HttpUrl.parse(BACKEND_URL + path).newBuilder();
        if (params != null) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                url.addQueryParameter(param.getKey(),param.getValue());
            }
        }
        Request.Builder request = new Request.Builder().url(url.build());
        if (token != null) {
            request.addHeader("token", token);
        }
        Call call = client.newCall(request.build());
        call.enqueue(callback);
    }

    public void signUp(String username, String password, Integer accessibilityLevel, Callback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
            data.put("password", password);
            data.put("accessibilitylevel", accessibilityLevel);
        } catch (Exception e) {
            // TODO: Handle errors better
            Log.e("myapp", Log.getStackTraceString(e));
        }
        postData("register", data, callback);
    }

    public void login(String username, String password, Callback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
            data.put("password", password);
        } catch (Exception e) {
            // TODO: Handle errors better
            Log.e("myapp", Log.getStackTraceString(e));
        }
        postData("login", data, callback);
    }

    public void isLoggedIn(Callback callback) {
        getData("isloggedin", null, callback);
    }

    public void calculateRoute(Integer startNode, Integer endNode, AvoidTheseEdges reported, Callback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put("startNode", startNode.toString());
            data.put("endNode", endNode.toString());
            data.put("reported", reported.getAsJSONArray());
        } catch (Exception e) {
            // TODO: Handle errors better
            Log.e("myapp", Log.getStackTraceString(e));
        }
        postData("calculateroute", data, callback);
    }

    public void updateBuildingInformation() {
        getData("getbuildingsinformation", null, new Callback() {
            // TODO: Handle errors better
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: Probably will crash as not wrapped in runOnUiThread
                Log.e("myapp", Log.getStackTraceString(e));
                Toast.makeText(context, "An error has occurred", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    if (result.getBoolean("success")) {
                        JSONObject buildingsData = result.getJSONObject("buildings");
                        for (Iterator<String> it = buildingsData.keys(); it.hasNext(); ) {
                            String buildingName = it.next();
                            JSONObject buildingInfo = buildingsData.getJSONObject(buildingName);
                            buildings.put(buildingName, new Building(buildingName, buildingInfo));
                        }
                    }
                } catch (Exception e) {
                    // TODO: Handle errors better
                    Log.e("myapp", Log.getStackTraceString(e));
                }
            }
        });
    }

    public void submitReport(Integer startNode, Integer endNode, String comment, Callback callback) {
        JSONObject data = new JSONObject();
        try {
            data.put("startNode", startNode);
            data.put("endNode", endNode);
            data.put("comment", comment);
        } catch (Exception e) {
            // TODO: Handle errors better
            Log.e("myapp", Log.getStackTraceString(e));
        }
        postData("submitreport", data, callback);
    }

    public void fetchReports() {
        getData("getreports", null, new Callback() {
            // TODO: Handle errors better
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: Probably will crash as not wrapped in runOnUiThread
                Log.e("myapp", Log.getStackTraceString(e));
                Toast.makeText(context, "An error has occurred", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    if (result.getBoolean("success")) {
                        reports = new ArrayList<>();
                        JSONArray reportsData = result.getJSONArray("reports");
                        for (int i = 0; i < reportsData.length(); i++) {
                            JSONObject reportData = reportsData.getJSONObject(i);
                            Report report = new Report(reportData.getInt("startNode"), reportData.getInt("endNode"), reportData.getString("comment"), reportData.getDouble("timestamp"), reportData.getString("username"));
                            reports.add(report);
                        }
                    }
                } catch (Exception e) {
                    // TODO: Handle errors better
                    Log.e("myapp", Log.getStackTraceString(e));
                }
            }
        });
    }
}
