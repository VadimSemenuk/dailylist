package com.mamindeveloper.dailylist.NoteEdit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mamindeveloper.dailylist.ColorPicker.ColorPickerFragment;
import com.mamindeveloper.dailylist.ColorPicker.ColorPickerItem;
import com.mamindeveloper.dailylist.Constants;
import com.mamindeveloper.dailylist.Enums.NoteActions;
import com.mamindeveloper.dailylist.NotesList.Note;
import com.mamindeveloper.dailylist.NotesList.NoteContentField;
import com.mamindeveloper.dailylist.NotesList.NoteContentFieldListItem;
import com.mamindeveloper.dailylist.NotesList.NoteContentFieldTextArea;
import com.mamindeveloper.dailylist.NotesList.NoteTypes;
import com.mamindeveloper.dailylist.R;
import com.mamindeveloper.dailylist.Repositories.NoteRepository;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Set;

public class NoteEditActivity
        extends AppCompatActivity
        implements NoteContentFieldTextAreaFragment.OnNoteContentFieldTextAreaFragmentInteraction,
        NoteContentFieldListItemFragment.OnNoteContentFieldListItemFragmentInteraction,
        NoteContentFieldImageFragment.OnNoteContentFieldImageFragmentInteraction {

    DateTimeFormatter timeFormatter;

    Note note;
    DateTime date;
    DateTime startDateTime;
    DateTime endDateTime;
    NoteTypes noteType;

    ArrayList<Fragment> contentFieldsViews = new ArrayList<>();
    LinearLayout contentFieldsWrapper;
    Fragment contentFieldFragmentToAddOnResume;
    TextView startTimeView;
    TextView endTimeView;
    ImageButton startTimeButton;
    ImageButton endTimeButton;
    Switch notificationView;
    ColorPickerFragment colorPickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        Intent intent = getIntent();
        date = (DateTime) intent.getSerializableExtra("date");
        noteType = NoteTypes.valueOf(intent.getIntExtra("noteType", 1));

        timeFormatter = DateTimeFormat.forPattern("HH:mm");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        DateTimeFormatter dateMonthFormatter = DateTimeFormat.forPattern("d MMMM");
        DateTimeFormatter yearFormatter = DateTimeFormat.forPattern("yyyy");
        actionBar.setTitle(dateMonthFormatter.print(date));
        actionBar.setSubtitle(yearFormatter.print(date));

        contentFieldsWrapper = findViewById(R.id.content_fields_wrapper);
        startTimeView = findViewById(R.id.start_time);
        endTimeView = findViewById(R.id.end_time);
        startTimeButton = findViewById(R.id.add_start_time);
        endTimeButton = findViewById(R.id.add_end_time);
        notificationView = findViewById(R.id.notification);
        colorPickerFragment = (ColorPickerFragment) getSupportFragmentManager().findFragmentById(R.id.color_picker);

        if (false) {
            setLastActionView();
        }

        Set<Integer> _keys = Constants.NOTE_COLORS.keySet();
        Integer[] keys = _keys.toArray(new Integer[_keys.size()]);
        ArrayList<ColorPickerItem> colors = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            ColorPickerItem color = new ColorPickerItem();
            color.id = keys[i];
            color.value = Constants.NOTE_COLORS.get(keys[i]);
            colors.add(color);
        }
        colorPickerFragment.setColors(colors);

        startTimeView.setPaintFlags(startTimeView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        endTimeView.setPaintFlags(endTimeView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (noteType == NoteTypes.Note) {
            findViewById(R.id.time_controls_wrapper).setVisibility(View.GONE);
        }

        notificationView.setClickable(false);

        findViewById(R.id.add_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextField();
            }
        });

        findViewById(R.id.add_list_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addListItemField();
            }
        });

        findViewById(R.id.add_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageField();
            }
        });

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(0);
            }
        });

        startTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(0);
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(1);
            }
        });

        endTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_edit, menu);

        if (noteType == NoteTypes.Note) {
            menu.findItem(R.id.app_bar_note_edit_calendar).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.app_bar_note_edit_confirm:
                save();
                this.finish();
                return true;
            case R.id.app_bar_note_edit_calendar:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (contentFieldFragmentToAddOnResume != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.content_fields_wrapper, contentFieldFragmentToAddOnResume).commit();

            contentFieldFragmentToAddOnResume = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        NoteContentFieldImageFragment noteContentFieldImageFragment = NoteContentFieldImageFragment.newInstance();
        contentFieldFragmentToAddOnResume = noteContentFieldImageFragment;

        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && returnedIntent != null) {
                        Bitmap selectedImage = (Bitmap) returnedIntent.getExtras().get("data");
                        noteContentFieldImageFragment.imageBitmap = selectedImage;
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && returnedIntent != null) {
//                        Uri selectedImage = returnedIntent.getData();
//                        imageView.setImageURI(selectedImage);

//                        Uri selectedImage = returnedIntent.getData();
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                            }
//                        }

                        Uri selectedImage = returnedIntent.getData();
                        noteContentFieldImageFragment.imageUri = selectedImage;
                    }
                    break;
            }
        }
    }

    private void addTextField() {
        NoteContentFieldTextAreaFragment noteContentFieldTextAreaFragment = NoteContentFieldTextAreaFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_fields_wrapper, noteContentFieldTextAreaFragment).commit();

        contentFieldsViews.add(noteContentFieldTextAreaFragment);
    }

    private void addListItemField() {
        NoteContentFieldListItemFragment noteContentFieldListItemFragment = NoteContentFieldListItemFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_fields_wrapper, noteContentFieldListItemFragment).commit();

        contentFieldsViews.add(noteContentFieldListItemFragment);
    }

    private void addImageField() {
        View dialogView = getLayoutInflater().inflate(R.layout.note_image_field_source_bottom_sheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);

        LinearLayout cameraView = (LinearLayout) dialogView.findViewById(R.id.camera);
        LinearLayout galleryView = (LinearLayout) dialogView.findViewById(R.id.gallery);
        cameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoFromCamera();
                dialog.dismiss();
            }
        });
        galleryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoFromGallery();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void takePhotoFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    private void takePhotoFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private void setTime(final int type) {
        DateTime now = DateTime.now();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (type == 0) {
                            startDateTime = new DateTime(date).withHourOfDay(hourOfDay).withMinuteOfHour(minute);
                            notificationView.setChecked(true);
                            notificationView.setClickable(true);
                        } else if (type == 1) {
                            endDateTime = new DateTime(date).withHourOfDay(hourOfDay).withMinuteOfHour(minute);
                        }
                        setTimeView();
                    }
                }, now.getHourOfDay(), now.getMinuteOfHour(), true);

        timePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getString(R.string.time_pick_clear), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type == 0) {
                    startDateTime = null;
                    notificationView.setChecked(false);
                    notificationView.setClickable(false);
                } else if (type == 1) {
                    endDateTime = null;
                }
                setTimeView();
                setNotificationCheckbox();
            }
        });

        timePickerDialog.show();
    }

    private void setTimeView() {
        if (startDateTime != null) {
            startTimeView.setVisibility(View.VISIBLE);
            startTimeButton.setVisibility(View.GONE);
            startTimeView.setText(timeFormatter.print(startDateTime));
        } else {
            startTimeView.setVisibility(View.GONE);
            startTimeButton.setVisibility(View.VISIBLE);
        }

        if (endDateTime != null) {
            endTimeView.setVisibility(View.VISIBLE);
            endTimeButton.setVisibility(View.GONE);
            endTimeView.setText(timeFormatter.print(endDateTime));
        } else {
            endTimeView.setVisibility(View.GONE);
            endTimeButton.setVisibility(View.VISIBLE);
        }
    }

    private void setNotificationCheckbox() {
        if (startDateTime != null) {

        }
    }

    private void setLastActionView() {
        String text = "";

        switch(note.lastAction) {
            case ADD:
                text = getResources().getString(R.string.note_action_add);
                break;
            case EDIT:
                text = getResources().getString(R.string.note_action_edit);
                break;
            case DELETE:
                text = getResources().getString(R.string.note_action_delete);
                break;
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern("d MMMM yyyy, HH:mm");
        text += ": " + formatter.print(note.lastActionDate);

        ((TextView) findViewById(R.id.last_action)).setText(text);
    }

    private void save() {
        Note note = new Note();
        note.colorId = colorPickerFragment.getSelectedColor().id;
        note.date = date;
        note.startDateTime = startDateTime;
        note.endDateTime = endDateTime;
        note.isFinished = false;
        note.isNotificationEnabled = notificationView.isChecked();
        note.lastActionDate = DateTime.now();
        note.lastAction = NoteActions.ADD;
        note.type = noteType;
        note.title = ((EditText) findViewById(R.id.title)).getText().toString();

        ArrayList<NoteContentField> contentFields = new ArrayList<>();
        for (int i = 0; i < contentFieldsViews.size(); i++) {
            Fragment fragment = contentFieldsViews.get(i);
            if (fragment instanceof NoteContentFieldTextAreaFragment) {
                String value = ((NoteContentFieldTextAreaFragment) fragment).getInputValue();
                contentFields.add(new NoteContentFieldTextArea(value));
            } else if (fragment instanceof NoteContentFieldListItemFragment) {
                String text = ((NoteContentFieldListItemFragment) fragment).getInputValue();
                Boolean isChecked = ((NoteContentFieldListItemFragment) fragment).getCheckedState();
                contentFields.add(new NoteContentFieldListItem(text, isChecked));
            }
        }
        note.contentFields = contentFields;
        NoteRepository.getInstance().addNote(note);
    }

    public void onTextAreaRemove(Fragment fragment) {
        onContentFieldRemove(fragment);
    }

    public void onListItemRemove(Fragment fragment) {
        onContentFieldRemove(fragment);
    }

    public void onImageRemove(Fragment fragment) {
        onContentFieldRemove(fragment);
    }

    public void onContentFieldRemove(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragment).commit();

        contentFieldsViews.remove(fragment);
    }
}
