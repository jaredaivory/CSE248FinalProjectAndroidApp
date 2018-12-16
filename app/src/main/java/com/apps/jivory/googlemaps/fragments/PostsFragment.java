package com.apps.jivory.googlemaps.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apps.jivory.googlemaps.R;
import com.apps.jivory.googlemaps.models.Post;
import com.apps.jivory.googlemaps.observers.FirebaseObserver;
import com.apps.jivory.googlemaps.models.PostAdapter;
import com.apps.jivory.googlemaps.models.PostHashMap;
import com.apps.jivory.googlemaps.models.User;
import com.apps.jivory.googlemaps.models.UsersHashMap;
import com.apps.jivory.googlemaps.viewmodels.MainViewModel;
import com.google.firebase.database.DataSnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostsFragment extends Fragment implements FirebaseObserver{
    public static final String TAG = "PostsFragment";

    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private PostAdapter adapter;

    private PostHashMap posts;
    private UsersHashMap users;
    private User currentUser;


    private View view;

    public PostsFragment(){

    }

    @SuppressLint("ValidFragment")
    public PostsFragment(PostHashMap posts, UsersHashMap users, User currentUser){
        this.posts = posts;
        this.users = users;
        this.currentUser = currentUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_posts, container, false);
        initializeViews();

        // remove and add interface
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        posts.registerObserver(this);
        users.registerObserver(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh();
    }

    private void refresh(){
        adapter.setPosts(posts);
        adapter.setUser(currentUser);
        adapter.setUsers(users);
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
                deletePost(viewHolder);
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void deletePost(RecyclerView.ViewHolder viewHolder){
        String key = adapter.getPostID(viewHolder.getAdapterPosition());
        if(posts.containsKey(key) ){
            Post p = posts.get(key);
            if(p.getCreator().equals(currentUser.getUSER_ID())){
                mainViewModel.deletePost(key);
                Toast.makeText(getContext(), "Post deleted", Toast.LENGTH_SHORT).show();
            } else {
                // do nothing
                Toast.makeText(getContext(), "Dismissed Post", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        posts.removeObserver(this);
        users.removeObserver(this);
    }

    @Override
    public void onChanged() {
        Log.d(TAG, "onChanged: Posts" + posts.toString());
        Log.d(TAG, "onChanged: Users" + users.toString());
        refresh();
    }
}
