package personnal.ahsyaj.jshoppinglistgenerator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ActivityGetter;

public final class SetupActivity extends AppCompatActivity implements ActivityGetter {
    private static final String NAME = "SetupActivity";
    public static final String[] CONF_FIELDS = {"user", "password", "database", "host", "port", "language"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGetter.putActivity(NAME, this);
        this.setContentView(R.layout.activity_setup);
        this.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityGetter.removeActivity(NAME);
    }

    private void init() {
        this.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ActivityGetter.getActivity("MainActivity"));
                SharedPreferences.Editor prefsEditor = prefs.edit();
                TextView userName = SetupActivity.this.findViewById(R.id.userNameInput);
                TextView password = SetupActivity.this.findViewById(R.id.passwordInput);
                TextView database = SetupActivity.this.findViewById(R.id.databaseInput);
                TextView host = SetupActivity.this.findViewById(R.id.hostInput);
                TextView port = SetupActivity.this.findViewById(R.id.portInput);
                Spinner language = SetupActivity.this.findViewById(R.id.languageInput);

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

        this.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetupActivity.this.finish();
            }
        });
    }
}
