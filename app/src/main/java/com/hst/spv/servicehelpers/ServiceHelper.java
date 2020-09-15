package com.hst.spv.servicehelpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hst.spv.R;
import com.hst.spv.app.AppController;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.PreferenceStorage;
import com.hst.spv.utils.SPVConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 15-09-2020.
 */

public class ServiceHelper {

    private String TAG = "Get Name";
    private Context context;
    private IServiceListener iServiceListener;

    public ServiceHelper(Context context) {
        this.context = context;
    }

    public void setServiceListener(IServiceListener iServiceListener) {
        this.iServiceListener = iServiceListener;
    }

    public void makeGetServiceCall(String params, String urls) {
        Log.d(TAG, "making sign in request" + params);
        String baseURL = "";
        try {
            URI uri = new URI(urls.replace(" ", "%20"));
            baseURL = uri.toString();

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    baseURL, params,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            iServiceListener.onResponse(response);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        Log.d(TAG, "error during sign up" + error.getLocalizedMessage());

                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            iServiceListener.onError(jsonObject.getString(SPVConstants.PARAM_MESSAGE));
                            String status = jsonObject.getString("status");
                            Log.d(TAG, "signup status is" + status);
                        } catch (UnsupportedEncodingException e) {
                            iServiceListener.onError(context.getResources().getString(R.string.error_occurred));
                            e.printStackTrace();
                        } catch (JSONException e) {
                            iServiceListener.onError(context.getResources().getString(R.string.error_occurred));
                            e.printStackTrace();
                        }

                    } else {
                        iServiceListener.onError(context.getResources().getString(R.string.error_occurred));
                    }
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void makeGetServiceCalls(String params, String urls) {

        String baseURL = "";
        try {
            URI uri = new URI(urls.replace(" ", "%20"));
            baseURL = uri.toString();
            String URL = urls;
//            JSONObject jsonBody = new JSONObject();
//
//            jsonBody.put("email", "abc@abc.com");
//            jsonBody.put("password", "asdfaf");
//            jsonBody.put("name", "Naren");

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, baseURL, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d(TAG, response.toString());
                    iServiceListener.onResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        Log.d(TAG, "error during sign up" + error.getLocalizedMessage());

                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            iServiceListener.onError(jsonObject.getString(SPVConstants.PARAM_MESSAGE));
                            String status = jsonObject.getString("status");
                            Log.d(TAG, "signup status is" + status);
                        } catch (UnsupportedEncodingException e) {
                            iServiceListener.onError(context.getResources().getString(R.string.error_occurred));
                            e.printStackTrace();
                        } catch (JSONException e) {
                            iServiceListener.onError(context.getResources().getString(R.string.error_occurred));
                            e.printStackTrace();
                        }

                    } else {
                        iServiceListener.onError(context.getResources().getString(R.string.error_occurred));
                    }

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic" + PreferenceStorage.getIMEI(context));//put your token here
                    return headers;
                }
            };
            jsonOblect.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonOblect);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();

    }
}
