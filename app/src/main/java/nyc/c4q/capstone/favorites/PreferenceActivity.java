package nyc.c4q.capstone.favorites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;

import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nyc.c4q.capstone.MainActivity;
import nyc.c4q.capstone.R;
import nyc.c4q.capstone.models.PreferencesModel;
import nyc.c4q.capstone.utils.FirebaseDataHelper;

import static nyc.c4q.capstone.MainActivity.firebaseDataHelper;

public class PreferenceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "error";
    private Button medical_btn, housing_btn, education_btn, business_btn, volunteer_btn, events_btn, community_btn, sports_btn, tragedy_btn, save_preferences;
    private String medical, housing, education, business, volunteer, events, community, sports, tragedy;
    int color = 0;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private static final String SHARED_PREFS_KEY = "sharedPrefsTesting";
    FirebaseAuth authorization;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        setActionBarTitle("my preferences");

        medical_btn = findViewById(R.id.medButton);
        medical_btn.setOnClickListener(this);

        housing_btn = findViewById(R.id.housing_button);
        housing_btn.setOnClickListener(this);
        education_btn = findViewById(R.id.education_button);
        education_btn.setOnClickListener(this);
        business_btn = findViewById(R.id.business);
        business_btn.setOnClickListener(this);
        volunteer_btn = findViewById(R.id.volunteerButton);
        volunteer_btn.setOnClickListener(this);
        events_btn = findViewById(R.id.eventsButton);
        events_btn.setOnClickListener(this);
        community_btn = findViewById(R.id.commButton);
        community_btn.setOnClickListener(this);
        sports_btn = findViewById(R.id.sports_button);
        sports_btn.setOnClickListener(this);
        tragedy_btn = findViewById(R.id.tragedy_button);
        tragedy_btn.setOnClickListener(this);
        save_preferences = findViewById(R.id.submit_button);
        save_preferences.setOnClickListener(this);

        firebaseDataHelper = new FirebaseDataHelper();
        preferences = getApplicationContext().getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        editor = preferences.edit();

        authorization = FirebaseAuth.getInstance();
        user = authorization.getCurrentUser();

    }

    @Override
    public void onClick(View view) {
        if (color == 0) {
            color = 1;
            view.setBackground(getResources().getDrawable(R.drawable.second_preference_round_button));
            saveString(color, view);
        } else {
            color = 0;
            view.setBackground(getResources().getDrawable(R.drawable.preference_round_button));
            saveString(color, view);
        }

    }

    private void saveString(int color, View view) {
        switch (view.getId()) {
            case R.id.medButton:
                if (color == 1) {
                    medical = "Medical";
                } else {
                    medical = "";
                }
                break;
            case R.id.housing_button:
                if (color == 1) {
                    housing = "Housing";
                } else {
                    housing = "";
                }
                break;
            case R.id.education_button:
                if (color == 1) {
                    education = "Education";
                } else {
                    education = "";
                }
                break;
            case R.id.business:
                if (color == 1) {
                    business = "Business";
                } else {
                    business = "";
                }
                break;
            case R.id.volunteerButton:
                if (color == 1) {
                    volunteer = "Volunteer";
                } else {
                    volunteer = "";
                }
                break;
            case R.id.eventsButton:
                if (color == 1) {
                    events = "Events";
                } else {
                    events = "";
                }
                break;
            case R.id.commButton:
                if (color == 1) {
                    community = "Community";
                } else {
                    community = "";
                }
                break;
            case R.id.sports_button:
                if (color == 1) {
                    sports = "Sports";
                } else {
                    sports = "";
                }
                break;
            case R.id.tragedy_button:
                if (color == 1) {
                    tragedy = "Tragedy";
                } else {
                    tragedy = "";
                }
                break;
            case R.id.submit_button:
                PreferencesModel model = new PreferencesModel(medical, housing, education, business, volunteer, events, community, sports, tragedy);
                firebaseDataHelper.getPreferencesDatabaseReference().child(user.getUid()).setValue(model);
                Intent intent = new Intent(PreferenceActivity.this, MainActivity.class);
                startActivity(intent);
        }

    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.rounded_shape_dark_blue));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
}


