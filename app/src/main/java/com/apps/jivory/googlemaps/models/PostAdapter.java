package com.apps.jivory.googlemaps.models;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.jivory.googlemaps.R;
import com.apps.jivory.googlemaps.fragments.EditPostFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder>{
    public static final String TAG = "PostAdapter";
    private List<Post>  posts = new ArrayList<>();
    private User currentUser;

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        Post p = posts.get(position);

        Log.d(TAG, "onBindViewHolder: " + currentUser.getUSER_ID());
        Log.d(TAG, "onBindViewHolder: " + p.getCreator());

        if(!p.getCreator().equals(currentUser.getUSER_ID())){
            holder.textViewCreator.setText(p.getCreator());
        }

        holder.textViewTitle.setText(p.getTitle());
        holder.textViewCreator.setText("You");
        holder.textViewDescription.setText(p.getDescription());
        holder.textViewID.setText(p.getPOST_ID());
        holder.setCurrentPost(p);
    }

    public String getPostID(int posistion){
        return posts.get(posistion).getPOST_ID();
    }


    public void setPosts(Map<String, Post> posts){
        this.posts = new ArrayList<>(posts.values());
        notifyDataSetChanged();
    }

    public void setUser(User user){
        this.currentUser = user;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class PostHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewCreator;
        private TextView textViewLocation;
        private TextView textViewDescription;
        private TextView textViewID;
        private Post currentPost;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewTitle = itemView.findViewById(R.id.textView_Post_Title);
            this.textViewDescription = itemView.findViewById(R.id.textView_Post_Description);
            this.textViewCreator = itemView.findViewById(R.id.textView_Post_Creator);
            this.textViewLocation = itemView.findViewById(R.id.textView_Post_Location);
            this.textViewID = itemView.findViewById(R.id.textView_Post_ID);

            itemView.setOnClickListener(v -> {
                EditPostFragment dialogFragment = new EditPostFragment(currentPost);
                dialogFragment.show(((FragmentActivity)itemView.getContext()).getSupportFragmentManager(),"Edit Post");
            });
        }

        private void setCurrentPost(Post post){
            this.currentPost = post;
        }
    }
}
