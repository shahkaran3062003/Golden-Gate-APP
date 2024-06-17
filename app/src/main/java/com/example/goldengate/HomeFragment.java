package com.example.goldengate;


import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.todkars.shimmer.ShimmerRecyclerView;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {


    List<PostModel> list;
    PostAdapter adapter;
    ShimmerRecyclerView recyclerView, recyclerViewStory;
    private List<String> followingList;
    String base_API;
    String request_API = "/homefeed/posts";

    private int currentPage = 0;
    private final int pageSize = 2;
    private boolean isLoading = false;
    String token;


    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.post_recycler);
        list = new ArrayList<>();
        followingList = new ArrayList<>();
        base_API = getString(R.string.BASE_API);
        request_API  = base_API + request_API;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.GoldenGate.GoldenGate.PREFS", MODE_PRIVATE);
        token = sharedPreferences.getString("TOKEN_KEY", "");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Post RecyclerView
        recyclerView.showShimmer();
        adapter = new PostAdapter(getContext(), list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setNestedScrollingEnabled(false);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                // If the last visible item is close to the end of the list, load more items
                if (!isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                    Read_Posts();
                }
            }
        });

        //Functions
        Read_Posts();

    }




    private void Read_Posts(){

        Log.d("API", "Read_Posts: REQUEST SEND");
        String url = request_API+"?page=" + currentPage + "&size=" + pageSize;
        Log.d("API", "Read_Posts: "+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Parse the response and update your UI
                    // ...

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray contentArray = jsonObject.getJSONArray("content");
                        list.clear();
                        for (int i = 0; i < contentArray.length(); i++) {
                            JSONObject contentObject = contentArray.getJSONObject(i);
                            String fullName = contentObject.getString("fullName");
                            String avatar = contentObject.getString("avatar");
                            int postId = contentObject.getInt("postId");
                            String content = contentObject.getString("content");

                            JSONArray imagesArray = contentObject.getJSONArray("images");
                            String postImage;
                            if (imagesArray.length() > 0) {
                                postImage = imagesArray.getString(0);
                            } else {
                                continue;
                            }


                            PostModel model = new PostModel(postImage, content, fullName, avatar);
                            list.add(model);

                        }

                        Collections.reverse(list);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    currentPage++; // Increment the page number on successful load
                    isLoading = false;
                }, error -> {
            // Handle error
            isLoading = false;
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token); // Replace 'token' with your actual token
                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(stringRequest);


    }




    private Bitmap decodeBase64Image(String base64Image) {
        if(base64Image.substring(0, 4).equals("data")) {
            base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
        }
        if (base64Image == null || base64Image.isEmpty()) {
            return null; // or handle appropriately
        }

        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}