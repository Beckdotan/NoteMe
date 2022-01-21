package com.example.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class AddNoteActivity extends AppCompatActivity {

    TextView headText;
    TextView bodyText;
    Button submitButton;

    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //cosmetics
        getSupportActionBar().hide();

        //connecting Ui components
        headText = findViewById(R.id.headlineText);
        bodyText = findViewById(R.id.bodyText);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prepering data
                Log.i("on click", "start prepering data "); //debug

                String head = headText.getText().toString();
                String body = bodyText.getText().toString();
                double lat = MapActivity.mLastKnownLocation.getLatitude();
                double lon = MapActivity.mLastKnownLocation.getLongitude();
                int likes = 0;

                Log.i("on click", "after prepering data "); //debug
                //creating note
                Note note = new Note(false, head, body, lon, lat, likes);

                //---------sending Note To Server------------//
                String isSaved = saveNoteInDB(note);





             //Just for fun - proccesing dialog
                final ProgressDialog progressDialog = new ProgressDialog(AddNoteActivity.this);
                progressDialog.setMessage("Publishing your note...");
                progressDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.hide();
                        if (isSaved.equals("0")) {
                            createToast("Error - Wasn't Saved");
                            //deleting the published note info

                        } else {
                            createToast("We Got Your Note");
                            headText.setText("");
                            bodyText.setText("");
                        }
                    }
                }, 1000);
            }
        });


    }

    private void createToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    //sending the Note to DB.
    //if works - return the automatic key was given and send toast to user
    //else return "0" as didnt happen.  and send toast to user.
    public String saveNoteInDB(Note note) {
        // Write a message to the database
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Notes/").push();
            //getting the new key from DB
            String key = myRef.getKey();
            //saving it for id in Note
            note.setId(key);
            //sending to DB
            myRef.setValue(note);
            Log.i("saveNoteInDB", "saved in DB! :)");

            /// !!!!!!!!!!!! ----- Can add the note id to the user notes list here ----- !!!!!!!!!

            return key;
        } catch (Error e) {
            Log.e("saveNoteInDB", "didn't work");
            return "0";
        }


    }

}