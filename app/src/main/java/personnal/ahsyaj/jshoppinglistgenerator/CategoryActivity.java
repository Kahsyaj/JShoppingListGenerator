package personnal.ahsyaj.jshoppinglistgenerator;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Adapters.ItemsAdapter;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.Manager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.MealManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Models.ActivityGetter;

public class CategoryActivity extends AppCompatActivity implements ActivityGetter {
    private final String name = "CategoryActivity";
    private Button backButton = null;
    private Button addButton = null;

    //Constructors
    public CategoryActivity() {
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
        this.setContentView(R.layout.activity_category);

        TextView subTitle = this.findViewById(R.id.subTitle);

        subTitle.setText(MainActivity.category);
        this.initButtons();
        this.initView();
    }

    public boolean initButtons() {
        this.backButton = this.findViewById(R.id.catBackButton);
        this.addButton = this.findViewById(R.id.addButton);

        if (this.backButton == null || this.addButton == null) {
            return false;
        }
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryActivity.this.finish();
            }
        });
        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, ItemActivity.class);
                CategoryActivity.this.startActivity(intent);
            }
        });
        return true;
    }

    public void initView() {
        ArrayList<Entity> elements = null;

        switch(MainActivity.category) {
            case "Ingredients":
                elements = new IngredientManager().dbLoadAll();
                break;
            case "Meals":
                elements = new MealManager().dbLoadAll();
                break;
            case "ShoppingLists":
                elements = new ShoppingListManager().dbLoadAll();
                break;
        }

        RecyclerView itemLst = this.findViewById(R.id.itemList);
        ItemsAdapter adapter = new ItemsAdapter(elements, R.layout.item_row);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((this.getApplicationContext()));

        itemLst.setLayoutManager(layoutManager);
        itemLst.setItemAnimator(new DefaultItemAnimator());
        itemLst.setAdapter(adapter);
    }
}
