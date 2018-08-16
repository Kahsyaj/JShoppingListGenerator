package personnal.ahsyaj.jshoppinglistgenerator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.DbFactory;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.Manager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.MealManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListGenerator;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListManager;

public class MainActivity extends AppCompatActivity {
    //Data
    public static MainActivity activity;
    public static String[] CONF_FIELDS = {"user", "password", "database", "host", "port", "language"};
    public static String category;

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
            switch(view.getId()) {
                case R.id.quitButton:
                    MainActivity.this.finish();
                    break;
                case R.id.setupButton:
                    Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);

                    MainActivity.this.startActivity(setupIntent);
                    break;
                default:
                    MainActivity.category = (String) ((Button) view).getText();
                    Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);

                    MainActivity.this.startActivity(categoryIntent);
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

    //Other methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.initButtons();
        this.getConfig();
        IngredientManager mgr = new IngredientManager();
        mgr.open();
        mgr.dbCreate(new Ingredient("jojo"));
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
    }

    public static Manager getManager() {
        Manager mgr = null;
        switch (MainActivity.category) {
            case "Ingredients":
                mgr = new IngredientManager();
                break;
            case "Meals":
                mgr = new MealManager();
                break;
            case "ShoppingLists":
                mgr = new ShoppingListManager();
                break;
        }
        return mgr;
    }
}
