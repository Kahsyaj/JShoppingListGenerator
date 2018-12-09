package personnal.ahsyaj.jshoppinglistgenerator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.Manager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.MealManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ActivityGetter;

public final class MainActivity extends AppCompatActivity implements ActivityGetter, View.OnClickListener {
    //Data
    public static String[] CONF_FIELDS = {"user", "password", "database", "host", "port", "language"};
    private static final String NAME = "MainActivity";
    public static String category;

    //Getters

    //Other methods
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quit_button:
                MainActivity.this.finish();
                break;
            case R.id.setup_button:
                Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);

                MainActivity.this.startActivity(setupIntent);
                break;
            case R.id.generate_button:
                Intent generateIntent = new Intent(MainActivity.this, GenerateActivity.class);

                MainActivity.this.startActivity(generateIntent);
                break;
            default:
                MainActivity.category = (String) ((Button) view).getText();
                Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);

                MainActivity.this.startActivity(categoryIntent);
                break;
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGetter.putActivity(NAME, this);
        this.setContentView(R.layout.activity_main);
        this.init();
        this.getConfig();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityGetter.removeActivity(NAME);
    }

    private void init() {
        this.findViewById(R.id.ingredients_button).setOnClickListener(this);
        this.findViewById(R.id.meals_button).setOnClickListener(this);
        this.findViewById(R.id.shoppinglists_button).setOnClickListener(this);
        this.findViewById(R.id.generate_button).setOnClickListener(this);
        this.findViewById(R.id.quit_button).setOnClickListener(this);
        this.findViewById(R.id.setup_button).setOnClickListener(this);
    }
}
