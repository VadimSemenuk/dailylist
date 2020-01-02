package com.mamindeveloper.dailylist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mamindeveloper.dailylist.Main.MainActivity;
import com.mamindeveloper.dailylist.Models.User;
import com.mamindeveloper.dailylist.NoteEdit.NoteEditActivity;
import com.mamindeveloper.dailylist.NotesList.Note;
import com.mamindeveloper.dailylist.Repositories.AuthRepository;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ProfileActivity extends AppCompatActivity {

    User user;

    DateTimeFormatter backupDateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        user = AuthRepository.getInstance().getUser();
        backupDateFormatter = DateTimeFormat.forPattern("d MMMM yyyy, HH:mm");

        setBackupsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.sign_out:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setBackupsList() {
        LayoutInflater ltInflater = getLayoutInflater();
        LinearLayout backupsListView = findViewById(R.id.backups_list);

        for (int i = 0; i < user.getBackups().size(); i++) {
            View backupView = ltInflater.inflate(R.layout.backup_item, backupsListView, false);

            User.Backup backup = user.getBackups().get(i);

            String backupName = backupDateFormatter.print(backup.getDateCreated());
            if (backup.getAutoCreated()) {
                backupName = getString(R.string.backup_auto_created) + " " + backupName;
            }
            ((TextView) backupView.findViewById(R.id.name)).setText(backupName);

            backupView.findViewById(R.id.restore_backup).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restoreBackup();
                }
            });

            backupsListView.addView(backupView);
        }
    }

    private void restoreBackup() {
        restartApp();
    }

    private void restartApp() {
        Intent mStartActivity = new Intent(this, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}
