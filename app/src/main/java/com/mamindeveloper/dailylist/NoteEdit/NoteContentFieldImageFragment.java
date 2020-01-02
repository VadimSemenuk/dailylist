package com.mamindeveloper.dailylist.NoteEdit;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mamindeveloper.dailylist.R;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;

public class NoteContentFieldImageFragment extends Fragment {
    private OnNoteContentFieldImageFragmentInteraction mListener;

    Bitmap imageBitmap;
    Uri imageUri;

    ImageView imageView;
    ImageButton removeButton;

    public NoteContentFieldImageFragment() {
    }

    public static NoteContentFieldImageFragment newInstance() {
        NoteContentFieldImageFragment fragment = new NoteContentFieldImageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_content_field_image, container, false);

        imageView = view.findViewById(R.id.image);
        removeButton = view.findViewById(R.id.remove);

        final Fragment _this = this;

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onImageRemove(_this);
            }
        });

        if (imageBitmap != null) {
            setImage(imageBitmap);
        }
        if (imageUri != null) {
            setImage(imageUri);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnNoteContentFieldImageFragmentInteraction) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setImage(Bitmap image) {
        imageView.setImageBitmap(image);
    }

    public void setImage(Uri image) {
        Picasso.get().load(image).into(imageView);
    }

    public interface OnNoteContentFieldImageFragmentInteraction {
        void onImageRemove(Fragment fragment);
    }
}
