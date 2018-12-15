package com.apps.jivory.googlemaps.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.apps.jivory.googlemaps.R;
import com.apps.jivory.googlemaps.models.Post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditPostFragment extends DialogFragment {
    public static final String TAG = "EditPostFragment";
    private EditPostFragmentListener createPostFragmentListener;

    private EditText editTextPostTitle, editTextPostDescription;
    private Button btnPostSave;
    private Spinner amtUser;

    private View view;

    private Post post;


    public EditPostFragment(){
    }

    @SuppressLint("ValidFragment")
    public EditPostFragment(Post p){
        this.post = p;
    }

    /** Interface for activity to implement**/
    public interface EditPostFragmentListener {
        void onPostSaved(Post p);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_post, container, false);
        return view;
    }

    /**
     * Grabs the context right as fragment is attached to activity
     * Here I check to see if the listener is added to activity
     * */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof EditPostFragmentListener){
            createPostFragmentListener = (EditPostFragmentListener) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
    }

    /** Initializing widgets and button actions**/
    private void initViews() {
        amtUser = view.findViewById(R.id.spinner_PostFragment_AmtUsers);
        Integer[] max = new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, max);
        amtUser.setAdapter(adapter);
        amtUser.setOnItemSelectedListener(spinnerListener);

        btnPostSave = view.findViewById(R.id.button_PostFragment_Save);
        btnPostSave.setOnClickListener(e -> {
            if(post == null){
                post = createPost();
                Log.d(TAG, "initViews: ");
            } else {
                savePost();
            }
            createPostFragmentListener.onPostSaved(post);
            dismiss();
        });

        editTextPostTitle = view.findViewById(R.id.editText_PostFragment_Title);
        editTextPostDescription = view.findViewById(R.id.editText_PostFragment_Description);

        if(post!=null){
            editTextPostTitle.setText(post.getTitle());
            editTextPostDescription.setText(post.getDescription());
        }

    }

    /** Called if post was passed**/
    private void savePost(){
        String title = editTextPostTitle.getText().toString();
        String description = editTextPostDescription.getText().toString();
        int maxParticipants = (Integer) amtUser.getSelectedItem();

        post.setTitle(title);
        post.setDescription(description);
        post.setMaxParticipants(maxParticipants);
    }

    /** Called if creating all new post**/
    private Post createPost() {
        String title = editTextPostTitle.getText().toString();
        String description = editTextPostDescription.getText().toString();
        int maxParticipants = (Integer) amtUser.getSelectedItem();

        return new Post(title,description, maxParticipants);
    }

    private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Integer value = Integer.parseInt(parent.getItemAtPosition(position).toString());
            Log.d(TAG, "onItemSelected: " + value);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


}
