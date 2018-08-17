package personnal.ahsyaj.jshoppinglistgenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;

public class ItemActivity extends AppCompatActivity {
    private Button saveButton = null;
    private Button backButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        this.initButtons();
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
                        CategoryActivity.activity.initView();
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
}
