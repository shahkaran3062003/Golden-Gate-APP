package com.example.goldengate;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.todkars.shimmer.ShimmerRecyclerView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {


    List<PostModel> list;
    PostAdapter adapter;
    ShimmerRecyclerView recyclerView, recyclerViewStory;
    private List<String> followingList;


    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.post_recycler);
        list = new ArrayList<>();
        followingList = new ArrayList<>();
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

        //Story RecyclerView
        recyclerViewStory.setHasFixedSize(true);
        recyclerViewStory.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewStory.setNestedScrollingEnabled(false);

        //Functions
        Read_Posts();
        GetAllUsersId();
    }


    //----------------------------------Read Posts--------------------------------//
    private void Read_Posts() {
        recyclerView.hideShimmer();
//        ref.child(ALL_POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                list.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    PostModel model = dataSnapshot.child(INFO).getValue(PostModel.class);
//                    list.add(model);
//                }
//                Collections.reverse(list);
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
    }



    //--------------------------------Get All Users Id--------------------------------//
    private void GetAllUsersId() {
//        followingList = new ArrayList<>();
//        ref.child(USER_CONSTANT).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                followingList.clear();
//                UserModel model = null;
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    model = dataSnapshot.child(INFO).getValue(UserModel.class);
//                    assert model != null;
//                    if (!model.getKey().equals(user.getUid())) {
//                        followingList.add(dataSnapshot.getKey());
//                    }
//                }
//                readStory();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}