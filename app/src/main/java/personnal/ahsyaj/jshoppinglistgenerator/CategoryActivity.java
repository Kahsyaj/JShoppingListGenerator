package personnal.ahsyaj.jshoppinglistgenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.Manager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.MealManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListManager;

public class CategoryActivity extends AppCompatActivity {

    private Button backButton = null;
    private Button addButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_category);

        TextView subTitle = this.findViewById(R.id.subTitle);

        subTitle.setText(MainActivity.category);
        this.initButtons();
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
        RecyclerView itemLst = this.findViewById(R.id.itemList);
        ArrayList elements = null;

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
    }
}
