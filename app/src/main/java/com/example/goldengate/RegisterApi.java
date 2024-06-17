package com.example.goldengate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.goldengate.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterApi {

    public static String baseAPI;
    private static String REGISTER_URL = "/auth/register";
    private static final String PREFS_NAME = "com.GoldenGate.GoldenGate.PREFS";
    private static final String TOKEN_KEY = "TOKEN_KEY";

    public static void registerUser(Context context, RegisterRequest registerRequest) {
        // Create the request payload

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("username", registerRequest.getUsername());
            jsonRequest.put("email", registerRequest.getEmail());
            jsonRequest.put("password", registerRequest.getPassword());
            jsonRequest.put("role", registerRequest.getRole().toString()); // Assuming Role is an enum
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to create JSON request", Toast.LENGTH_SHORT).show();
            return;
        }


        baseAPI = context.getString(R.string.BASE_API);
        REGISTER_URL = baseAPI+REGISTER_URL;

        // Create a new request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Create the JSON request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                REGISTER_URL,
                jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract the token from the response
                            String token = response.getString("token");

                            // Store the token in SharedPreferences
                            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(TOKEN_KEY, token);
                            editor.apply();

                            // Show success message
                            Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, LoginActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Failed to parse response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error response
                        Log.d("API", "onErrorResponse: "+error);
                        Toast.makeText(context, "Registration failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest);
    }

    // Method to retrieve the token from SharedPreferences
    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    // Method to clear the token from SharedPreferences
    public static void clearToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();
    }
}
