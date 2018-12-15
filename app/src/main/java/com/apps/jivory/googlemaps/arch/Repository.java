package com.apps.jivory.googlemaps.arch;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.apps.jivory.googlemaps.models.Post;
import com.apps.jivory.googlemaps.models.User;

import java.util.HashSet;

public class Repository {
    public static final String TAG = "REPOSITORY";
    static Repository INSTANCE = null;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    private Repository(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public Task<AuthResult> loginEmailAndPassword(String email, final String password){
        return mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
            }
        });
    }

    public Task<AuthResult> registerEmailAndPassword(String email, String password){
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public void setFirebaseUser(FirebaseUser user){
        this.mUser = user;
    }



    public void writeNewUser(User user) {
        DatabaseReference usersRef = mDatabase.child("users").child(mUser.getUid());
        usersRef.setValue(user);
    }

    public void insertNewPost(Post post){
        post.setCreator(mUser.getUid());
        Log.d(TAG, "insertNewPost: " +post.addParticipant(mUser.getUid()));
        post.setPOST_ID(Post.createPostID());
        post.addParticipant(mUser.getUid());

        DatabaseReference postsRef = mDatabase.child("posts").child(post.getPOST_ID());
        postsRef.setValue(post);

        Log.d(TAG, "insertNewPost: "+ post.toString());
    }
    public void updatePost(Post post){
        DatabaseReference postsRef = mDatabase.child("posts").child(post.getPOST_ID());
        postsRef.setValue(post);
    }

    public void deletePost(String postID){
        DatabaseReference postsRef = mDatabase.child("posts").child(postID);
        Log.d(TAG, "deletePost: " +postsRef.toString());
        postsRef.removeValue();
    }

    public void updateUser(User user){
        DatabaseReference usersRef = mDatabase.child("users").child(mUser.getUid());
        usersRef.setValue(user);
    }


    public DatabaseReference getUserReference(){
        DatabaseReference usersRef = mDatabase.child("users").child(mUser.getUid());
        return usersRef;
    }

    public DatabaseReference getPostsReference(){
        DatabaseReference postsRef = mDatabase.child("posts");
        return postsRef;
    }
    public DatabaseReference getAllUsersReference(){
        return mDatabase.child("users");
    }

    public static Repository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }
}
