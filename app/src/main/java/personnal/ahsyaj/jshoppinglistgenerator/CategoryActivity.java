package personnal.ahsyaj.jshoppinglistgenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Adapters.ItemsAdapter;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.MealManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ActivityGetter;

public final class CategoryActivity extends AppCompatActivity implements ActivityGetter, View.OnClickListener {
    private static final String NAME = "CategoryActivity";

    private ItemsAdapter adapter;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                CategoryActivity.this.finish();

                break;
            case R.id.add_button:
                Intent intent = new Intent(CategoryActivity.this, ItemActivity.class);

                intent.putExtra("action", "Create");
                this.startActivity(intent);

                break;
            default:
                break;
        }
    }

    public void refreshView() {
        this.adapter.setItems(this.getItems());
        this.adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGetter.putActivity(NAME, this);
        this.setContentView(R.layout.activity_category);


        this.init();
        this.initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.refreshView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityGetter.removeActivity(NAME);
    }

    private void init() {
        RecyclerView itemList = this.findViewById(R.id.item_list);
        ArrayList<Entity> elements = this.getItems();
        TextView subTitle = this.findViewById(R.id.subtitle);

        subTitle.setText(MainActivity.category);

        this.adapter = new ItemsAdapter(elements, R.layout.item_row);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((this.getApplicationContext()));

        itemList.setLayoutManager(layoutManager);
        itemList.setItemAnimator(new DefaultItemAnimator());
        itemList.setAdapter(this.adapter);
    }

    private ArrayList<Entity> getItems() {
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

        return elements;
    }

    private void initEvents() {
        this.findViewById(R.id.back_button).setOnClickListener(this);
        this.findViewById(R.id.add_button).setOnClickListener(this);
    }
}
