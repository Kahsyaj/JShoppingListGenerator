package personnal.ahsyaj.jshoppinglistgenerator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Models.ActivityGetter;

public class SetupActivity extends AppCompatActivity implements ActivityGetter {
    private final String name = "SetupActivity";
    private Button saveButton = null;
    private Button backButton = null;

    public static String[] CONF_FIELDS = {"user", "password", "database", "host", "port", "language"};

    //Constructors
    public SetupActivity() {
        super();
        ActivityGetter.putActivity(this.name, this);
    }

    //Destructor
    public void finalize() {
        ActivityGetter.removeActivity(this.name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        this.initButtons();
    }

    public boolean initButtons() {
        this.saveButton = this.findViewById(R.id.setupSaveButton);
        this.backButton = this.findViewById(R.id.setupBackButton);

        if (this.saveButton == null || this.backButton == null) {
            return false;
        }

        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ActivityGetter.getActivity("MainActivity"));
                SharedPreferences.Editor prefsEditor = prefs.edit();
                TextView userName = findViewById(R.id.userNameInput);
                TextView password = findViewById(R.id.passwordInput);
                TextView database = findViewById(R.id.databaseInput);
                TextView host = findViewById(R.id.hostInput);
                TextView port = findViewById(R.id.portInput);
                Spinner language = findViewById(R.id.languageInput);

                prefsEditor.putString(CONF_FIELDS[0], userName.getText().toString());
                prefsEditor.putString(CONF_FIELDS[1], password.getText().toString());
                prefsEditor.putString(CONF_FIELDS[2], database.getText().toString());
                prefsEditor.putString(CONF_FIELDS[3], host.getText().toString());
                prefsEditor.putString(CONF_FIELDS[4], port.getText().toString());
                prefsEditor.putString(CONF_FIELDS[5], language.getSelectedItem().toString());
                prefsEditor.apply();
                SetupActivity.this.finish();
            }
        });
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetupActivity.this.finish();
            }
        });
        return true;
    }
}
