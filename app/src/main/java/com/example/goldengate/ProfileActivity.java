package com.example.goldengate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView name, location, aboutTxt,txtEmail,txt_exp_title,txt_emp_type,txt_s_e_date;
    private CircleImageView profileImageView;
    private EditText editTextAbout;
    private ImageView img_edit_about, img_edit_profile,backgroundImage;

    SharedPreferences sharedPreferences;
    String token;

    private String baseUrl; // Base URL of your backend API

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.txt_name);
        location = findViewById(R.id.txt_location);
        aboutTxt = findViewById(R.id.aboutTxt);
        editTextAbout = findViewById(R.id.about_edittext);
        profileImageView = findViewById(R.id.profileImg);
        img_edit_about = findViewById(R.id.img_edit);
        img_edit_profile = findViewById(R.id.edit_profile);
        backgroundImage = findViewById(R.id.background_img);
        txtEmail = findViewById(R.id.txt_email);
        txt_exp_title = findViewById(R.id.txt_exp_title);
        txt_emp_type = findViewById(R.id.txt_emp_type);
        txt_s_e_date = findViewById(R.id.txt_s_e_date);

        baseUrl = getString(R.string.BASE_API); // Get base API URL from resources

        sharedPreferences = getSharedPreferences("com.GoldenGate.GoldenGate.PREFS", MODE_PRIVATE);
        token = sharedPreferences.getString("TOKEN_KEY", "");

        // Load profile data
        loadProfileData();
        loadExperiencesData();



        // Edit About button click listener
        img_edit_about.setOnClickListener(v -> {
            String newAbout = editTextAbout.getText().toString().trim();
            updateAbout(newAbout);
        });

        // Edit Profile button click listener
        img_edit_profile.setOnClickListener(v -> {
            // Navigate to edit profile activity if needed
            Intent intent = new Intent(ProfileActivity.this, EditProfileIntroActivity.class);
            startActivity(intent);
        });
    }

    private void loadProfileData() {
        // Get token from SharedPreferences (assuming you have saved it during login/registration)


        Log.d("API", "loadProfileData: "+token);

        // Request to fetch profile details
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                baseUrl + "/profiles/profile",
                null,
                response -> {
                    try {
                        Log.d("API", "loadProfileData: "+response);
                        String username = response.getString("fullName");
                        String locationStr = response.getString("otherDetails");
                        String about = response.getString("bio");
                        String base64Avatar = response.getString("avatar");
                        String base64BackgroundImage = response.getString("backgroundImage");
                        String email = response.getString("email");


                        // Set fetched data to UI elements
                        txtEmail.setText(email);
                        name.setText(username);
                        location.setText(locationStr);
                        aboutTxt.setText(about);
                        loadBase64Image(base64Avatar);
                        loadBase64BackgroundImage(base64BackgroundImage);

                        // Pre-fill about editText
                        editTextAbout.setText(about);


                        Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e)  {
                        Log.d("API", "loadProfileData: "+e);
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(ProfileActivity.this, "Failed to fetch profile data", Toast.LENGTH_SHORT).show();
                    Log.e("ProfileActivity", "Error: " + error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                // Set Bearer token for authorization
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }


    private void loadExperiencesData(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                baseUrl+"/experiences",
                null,
                response -> {
                    try {
                        JSONObject res = (JSONObject) response.get(0);
                        Log.d("API", "loadExperiencesData: "+res);
                        String title = (String) res.get("title");
                        String type = (String) res.get("employmentType");
                        String s_e_date = (String) res.get("startDate");
                        s_e_date = s_e_date.substring(0,10);

                        txt_exp_title.setText(title);
                        txt_emp_type.setText(type);
                        txt_s_e_date.setText("Start : "+s_e_date);



//                        Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e)  {
                        Log.d("API", "loadExperiencesData: "+e);
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(ProfileActivity.this, "Failed to fetch profile data", Toast.LENGTH_SHORT).show();
                    Log.e("loadExperiencesData", "Error: " + error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    private void updateAbout(String newAbout) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.GoldenGate.GoldenGate.PREFS", MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN_KEY", "");

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("bio", newAbout);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                baseUrl + "/profiles/profile",
                requestBody,
                response -> {
                    Toast.makeText(ProfileActivity.this, "About updated successfully", Toast.LENGTH_SHORT).show();
                    loadProfileData(); // Refresh profile data after update
                },
                error -> {
                    Toast.makeText(ProfileActivity.this, "Failed to update about", Toast.LENGTH_SHORT).show();
                    Log.e("ProfileActivity", "Error: " + error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private Bitmap decodeBase64Image(String base64Image) {
        base64Image = base64Image.substring(base64Image.indexOf(",")  + 1);
        if (base64Image == null || base64Image.isEmpty()) {
            return null; // or handle appropriately
        }

        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    private void loadBase64Image(String base64Image) {
        Log.d("Base64Content", base64Image); // Log the base64Image content

        try {
            Bitmap bitmap = decodeBase64Image(base64Image);

            Glide.with(this)
                    .load(bitmap)
                    .placeholder(R.drawable.user) // Placeholder image
                    .error(R.drawable.user) // Error image if loading fails
                    .into(profileImageView);


            // Use the bitmap as needed
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // Handle the error appropriately (e.g., show error message)
        }
    }

    private void loadBase64BackgroundImage(String base64BGImage){
        try {

            Bitmap bitmap = decodeBase64Image(base64BGImage);

            Glide.with(this)
                    .load(bitmap)
                    .placeholder(R.drawable.user) // Placeholder image
                    .error(R.drawable.user) // Error image if loading fails
                    .into(backgroundImage);
            // Use the bitmap as needed
        } catch (IllegalArgumentException e) {
            Log.d("API", "loadBase64BackgroundImage: "+e);
            e.printStackTrace();
            // Handle the error appropriately (e.g., show error message)
        }
    }
}
