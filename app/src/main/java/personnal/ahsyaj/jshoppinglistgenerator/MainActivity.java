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
    //Main menu
    private Button ingredientsButton = null;
    private Button mealsButton = null;
    private Button shoppingListsButton = null;
    private Button generateButton = null;
    private ImageButton quitButton = null;
    private ImageButton setupButton = null;

    //Category layout
    private Button catBackButton = null;
    private Button addButton = null;

    //Item layout
    private Button itemSaveButton = null;
    private Button itemBackButton = null;

    //Setup layout
    private Button setupSaveButton = null;
    private Button setupBackButton = null;

    //Generate layout
    private Button genBackButton = null;
    private Button reloadButton = null;

    private class CustomButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.quitButton:
                    MainActivity.this.finish();
                    break;

                case R.id.setupButton:
                    MainActivity.this.setContentView(R.layout.setup_layout);
                    MainActivity.this.initSetupLayoutButtons();
                    break;

                case R.id.setupBackButton:
                    MainActivity.this.setContentView(R.layout.activity_main);
                    MainActivity.this.initMainLayoutButtons();
                    break;

                case R.id.setupSaveButton:
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    TextView userName = findViewById(R.id.userNameInput);
                    TextView password = findViewById(R.id.passwordInput);
                    TextView database = findViewById(R.id.databaseInput);
                    TextView host = findViewById(R.id.hostInput);
                    TextView port = findViewById(R.id.portInput);
                    Spinner language = findViewById(R.id.languageInput);

                    prefsEditor.putString(confFields[0], userName.getText().toString());
                    prefsEditor.putString(confFields[1], password.getText().toString());
                    prefsEditor.putString(confFields[2], database.getText().toString());
                    prefsEditor.putString(confFields[3], host.getText().toString());
                    prefsEditor.putString(confFields[4], port.getText().toString());
                    prefsEditor.putString(confFields[5], language.getSelectedItem().toString());
                    prefsEditor.commit();
                    MainActivity.this.getConfig();
                    MainActivity.this.setContentView(R.layout.activity_main);
                    MainActivity.this.initMainLayoutButtons();
                    break;

                case R.id.catBackButton:
                    MainActivity.this.setContentView(R.layout.activity_main);
                    MainActivity.this.initMainLayoutButtons();
                    break;

                case R.id.addButton:
                    MainActivity.this.setContentView(R.layout.item_layout);
                    MainActivity.this.initItemLayoutButtons();
                    break;

                case R.id.itemSaveButton:
                    break;

                case R.id.itemBackButton:
                    MainActivity.this.setContentView(R.layout.category_layout);
                    MainActivity.this.initCategoryLayoutButtons();

                    TextView previousSubTitle = MainActivity.this.findViewById(R.id.subTitle);

                    previousSubTitle.setText(MainActivity.this.category);
                    break;

                case R.id.reloadButton:
                    break;

                case R.id.genBackButton:
                    MainActivity.this.setContentView(R.layout.generated_shoppinglist_layout);
                    MainActivity.this.intiGenLayoutButtons();
                    break;

                default:
                    MainActivity.this.setContentView(R.layout.category_layout);
                    MainActivity.this.initCategoryLayoutButtons();

                    MainActivity.this.category = (String) ((Button) view).getText();

                    TextView subTitle = MainActivity.this.findViewById(R.id.subTitle);

                    subTitle.setText(MainActivity.this.category);
                    break;
            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        this.setContentView(R.layout.activity_main);

        this.initMainLayoutButtons();
        this.getConfig();

        IngredientManager ing_mgr = new IngredientManager(new DbFactory(this.user, this.password, this.database, this.host, this.port));
        System.out.println(ing_mgr.dbLoad(1).toString());
    }

    public boolean initMainLayoutButtons() {
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

    public boolean initSetupLayoutButtons() {
        this.setupSaveButton = this.findViewById(R.id.setupSaveButton);
        this.setupBackButton = this.findViewById(R.id.setupBackButton);

        if (this.setupSaveButton == null || this.setupBackButton == null) {
            return false;
        }

        this.setupSaveButton.setOnClickListener(new CustomButtonOnClickListener());
        this.setupBackButton.setOnClickListener(new CustomButtonOnClickListener());
        return true;
    }

    public boolean initCategoryLayoutButtons() {
        this.catBackButton = this.findViewById(R.id.catBackButton);
        this.addButton = this.findViewById(R.id.addButton);

        if (this.catBackButton == null || this.addButton == null) {
            return false;
        }

        this.catBackButton.setOnClickListener(new CustomButtonOnClickListener());
        this.addButton.setOnClickListener(new CustomButtonOnClickListener());
        return true;
    }

    public boolean initItemLayoutButtons() {
        this.itemSaveButton = this.findViewById(R.id.itemSaveButton);
        this.itemBackButton = this.findViewById(R.id.itemBackButton);

        if (this.itemSaveButton == null || this.itemBackButton == null) {
            return false;
        }

        this.itemSaveButton.setOnClickListener(new CustomButtonOnClickListener());
        this.itemBackButton.setOnClickListener(new CustomButtonOnClickListener());
        return true;
    }

    public boolean intiGenLayoutButtons() {
        this.reloadButton = this.findViewById(R.id.reloadButton);
        this.genBackButton = this.findViewById(R.id.genBackButton);

        if (this.reloadButton == null || this.genBackButton == null) {
            return false;
        }

        this.reloadButton.setOnClickListener(new CustomButtonOnClickListener());
        this.genBackButton.setOnClickListener(new CustomButtonOnClickListener());
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
