package personnal.ahsyaj.jshoppinglistgenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Adapters.FieldsAdapter;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.ShoppingList;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Models.ActivityGetter;

public class ItemActivity extends AppCompatActivity implements ActivityGetter {
    private final String name = "ItemActivity";
    private Entity target;
    private Button saveButton = null;
    private Button backButton = null;

    //Constructors
    public ItemActivity() {
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
        setContentView(R.layout.activity_item);
        this.initButtons();

        this.target = (Entity) this.getIntent().getSerializableExtra("target");
        this.initView();
    }

    public boolean initButtons() {
        this.saveButton = this.findViewById(R.id.itemSaveButton);
        this.backButton = this.findViewById(R.id.itemBackButton);

        if (this.saveButton == null || this.backButton == null) {
            return false;
        }
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (MainActivity.category) {
                    case "Ingredients":
                        String name = ((TextView) ItemActivity.this.findViewById(R.id.itemName)).getText().toString();
                        new IngredientManager().dbCreate(new Ingredient(name));
                        CategoryActivity catAct = (CategoryActivity)ActivityGetter.getActivity("CategoryActivity");
                        if (catAct != null) {
                            catAct.initView();
                        }
                        ItemActivity.this.finish();
                        break;
                    case "Meals":
                        break;
                    case "ShoppingLists":
                        break;
                }
            }
        });
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemActivity.this.finish();
            }
        });
        return true;
    }

    public void initView() {
        ArrayList items = null;

        switch(MainActivity.category) {
            case "Ingredients":
                items = new ArrayList();
                break;
            case "Meals":
                Meal meal = (Meal) this.target;
                items = meal.getRecipe().getIngredients();
                break;
            case "ShoppingLists":
                ShoppingList shpLst = (ShoppingList) this.target;
                items = shpLst.getList();
                break;
        }
        if (!MainActivity.category.equals("Ingredients")) {
            ArrayList emptyField = new ArrayList<>();

            emptyField.add(new Ingredient());
            emptyField.add(0);
            items.add(emptyField);
        }

        RecyclerView itemLst = this.findViewById(R.id.itemFields);
        FieldsAdapter adapter = new FieldsAdapter(items, R.layout.field_row);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((this.getApplicationContext()));

        itemLst.setLayoutManager(layoutManager);
        itemLst.setItemAnimator(new DefaultItemAnimator());
        itemLst.setAdapter(adapter);
    }
}
