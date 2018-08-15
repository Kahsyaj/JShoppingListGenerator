package personnal.ahsyaj.jshoppinglistgenerator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.DbFactory;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;

public class MainActivity extends AppCompatActivity {
    //Data
    private static MainActivity activity;
    private String category = "";
    public static String[] confFields = {"user", "password", "database", "host", "port", "language"};
    private String user = "";
    private String password = "";
    private String database = "";
    private String host = "";
    private String port = "";
    private String language = "";

    //Buttons
    private Button ingredientsButton = null;
    private Button mealsButton = null;
    private Button shoppingListsButton = null;
    private Button generateButton = null;
    private ImageButton quitButton = null;
    private ImageButton setupButton = null;

    private class CustomButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            MainActivity.this.setContentView(R.layout.category_layout);

            MainActivity.this.category = (String) ((Button) view).getText();

            TextView subTitle = MainActivity.this.findViewById(R.id.subTitle);

            subTitle.setText(MainActivity.this.category);
        }
    }

    //Constructor
    public MainActivity() {
        super();
        MainActivity.activity = this;
    }

    //Getters
    public static MainActivity getActivity() {
        return MainActivity.activity;
    }

    //Other methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.initButtons();
        this.getConfig();
    }

    public boolean initButtons() {
        this.ingredientsButton = findViewById(R.id.ingredientsButton);
        this.mealsButton = findViewById(R.id.mealsButton);
        this.shoppingListsButton = findViewById(R.id.shoppinglistsbutton);
        this.generateButton = findViewById(R.id.generateButton);
        this.quitButton = findViewById(R.id.quitButton);
        this.setupButton = findViewById(R.id.setupButton);

        if (this.ingredientsButton == null || this.mealsButton == null ||
                this.shoppingListsButton == null || this.generateButton == null ||
                this.quitButton == null || this.setupButton == null) {
            return false;
        }

        this.ingredientsButton.setOnClickListener(new CustomButtonOnClickListener());
        this.mealsButton.setOnClickListener(new CustomButtonOnClickListener());
        this.shoppingListsButton.setOnClickListener(new CustomButtonOnClickListener());
        this.generateButton.setOnClickListener(new CustomButtonOnClickListener());
        this.quitButton.setOnClickListener(new CustomButtonOnClickListener());
        this.setupButton.setOnClickListener(new CustomButtonOnClickListener());
        return true;
    }

    public void getConfig() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.user = prefs.getString(confFields[0], "root");
        this.password = prefs.getString(confFields[1], "root");
        this.database = prefs.getString(confFields[2], "ShoppingListGenerator");
        this.host = prefs.getString(confFields[3], "localhost");
        this.port = prefs.getString(confFields[4], "3306");
        this.language = prefs.getString(confFields[5], "English");
    }
}
