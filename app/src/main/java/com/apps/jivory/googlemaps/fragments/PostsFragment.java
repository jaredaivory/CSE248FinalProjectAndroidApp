package com.apps.jivory.googlemaps.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apps.jivory.googlemaps.R;
import com.apps.jivory.googlemaps.models.Post;
import com.apps.jivory.googlemaps.models.PostAdapter;
import com.apps.jivory.googlemaps.models.User;
import com.apps.jivory.googlemaps.viewmodels.MainViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostsFragment extends Fragment {
    public static final String TAG = "PostsFragment";

    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private PostAdapter adapter;

    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_posts, container, false);
        initializeViews();

        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LiveData<DataSnapshot> postsData = mainViewModel.getPostData();
        postsData.observe(getActivity(), dataSnapshot -> {
            GenericTypeIndicator<HashMap<String, Post>> t = new GenericTypeIndicator<HashMap<String,Post>>() {};
            HashMap<String,Post> posts = dataSnapshot.getValue(t);
            if(posts!=null){
                adapter.setPosts(posts);
            }
        });

        LiveData<DataSnapshot> userData = mainViewModel.getUserData();
        userData.observe(getActivity(), dataSnapshot -> {
            User user = dataSnapshot.getValue(User.class);
            user.setUSER_ID(dataSnapshot.getKey());
            adapter.setUser(user);
        });

    }

    private void initializeViews(){
        adapter = new PostAdapter();

        recyclerView = view.findViewById(R.id.recyclerView_Posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mainViewModel.deletePost(adapter.getPostID(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Post deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

    }
}
