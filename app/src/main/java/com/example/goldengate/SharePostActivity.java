package com.example.goldengate;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SharePostActivity extends AppCompatActivity {

    EditText edit_text;
    ImageView post_img, btn_select_img, profileImg, closeImg;
    TextView userName;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    TextView btn_post;
    AppSharedPreferences appSharedPreferences;
    LoadingDialog loadingDialog;

    String baseAPI;

    private ActivityResultLauncher<Intent> launcher;


    String requestAPI;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);

        appSharedPreferences = new AppSharedPreferences(this);
        loadingDialog = new LoadingDialog(this);
        edit_text = findViewById(R.id.edit_text);
        post_img = findViewById(R.id.post_img);
        btn_select_img = findViewById(R.id.img3);
        btn_post = findViewById(R.id.btn_post);
        userName = findViewById(R.id.user_name);
        profileImg = findViewById(R.id.user_img);
        closeImg = findViewById(R.id.close_img);

        baseAPI = getString(R.string.BASE_API);
        requestAPI = "/postRequest";
        requestAPI = baseAPI+requestAPI;

        SharedPreferences sharedPreferences = getSharedPreferences("com.GoldenGate.GoldenGate.PREFS", MODE_PRIVATE);
        token = sharedPreferences.getString("TOKEN_KEY", "");

        Log.d("API", "onCreate: "+token);

        userName.setText(appSharedPreferences.getUserName());
//        Glide.with(this).load(appSharedPreferences.getImgUrl()).into(profileImg);

        // Close Activity
        closeImg.setOnClickListener(v -> finish());

        // Select Image
        btn_select_img.setOnClickListener(v -> selectImage());

        edit_text.requestFocus();
        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_post.setTextColor(Color.BLACK);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // Post Button
        btn_post.setOnClickListener(v ->
        {

                if (!edit_text.getText().toString().isEmpty())
                    loadingDialog.startLoadingDialog();
                uploadData();

        });


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK){
                Uri selectedImageUri = result.getData().getData();

                if(selectedImageUri != null){
                    post_img.setImageURI(selectedImageUri);
                    post_img.setTag(selectedImageUri);
                }

            }
        });
    }


    void uploadData() {


        Bitmap bitmap;
        bitmap = ((BitmapDrawable) post_img.getDrawable()).getBitmap();


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("content", edit_text.getText().toString());
            JSONArray imgArray = new JSONArray();
            imgArray.put(convertImageToBase64(bitmap));
            jsonObject.put("image", imgArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST,
                requestAPI,
                jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("API", "onResponse: "+response.toString());
                            Toast.makeText(SharePostActivity.this, ""+response.get("message"), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                            startActivity(new Intent(SharePostActivity.this, HomeActivity.class));
                            finish();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SharePostActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                        Log.d("API", "onErrorResponse: "+error.getMessage());
                        startActivity(new Intent(SharePostActivity.this, HomeActivity.class));
                        finish();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            };




//        StringRequest stringObjectRequest = new StringRequest(Method.POST,
//                requestAPI,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        if (response.equals("Post saved successfully.")) {
//                            Toast.makeText(SharePostActivity.this, "Post uploaded successfully", Toast.LENGTH_SHORT).show();
//                            loadingDialog.dismissDialog();
//                            startActivity(new Intent(SharePostActivity.this, HomeActivity.class));
//                            finish();
//                        } else {
//                            Toast.makeText(SharePostActivity.this, "Post upload failed: " + response, Toast.LENGTH_SHORT).show();
//                            loadingDialog.dismissDialog();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("API", "Login Error: " + error);
//                Toast.makeText(SharePostActivity.this, "Post upload failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                loadingDialog.dismissDialog();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", "Bearer " + token);
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("content", edit_text.getText().toString());
//                params.put("images", convertImageToBase64(bitmap));
//
//                return params;
//
//            }

            ;
        };

        requestQueue.add(jsonObjectRequest);


    }


    void selectImage(){
        Intent img = new Intent();
        img.setType("image/*");
        img.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(img);
    }

    private String convertImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}