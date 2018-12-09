package personnal.ahsyaj.jshoppinglistgenerator;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Adapters.FieldsAdapter;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Adapters.ShoppingListAdapter;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.ShoppingList;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.MealManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.PurchaseManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.RecipeManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ActivityGetter;

public final class ItemActivity extends AppCompatActivity implements ActivityGetter, View.OnClickListener {
    private static final String NAME = "ItemActivity";

    private final IngredientManager ingredientManager = new IngredientManager();
    private RecyclerView.Adapter fieldAdapter;
    private Entity target;
    private String action;
    private RecyclerView itemFields;
    private EditText itemName = null;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            ArrayList emptyItm = new ArrayList();

            emptyItm.add(new Ingredient(""));
            emptyItm.add("");
            ((FieldsAdapter) ItemActivity.this.fieldAdapter).add(emptyItm);

            return;
        } else if (view.getId() == R.id.save_button) {
            switch (MainActivity.category) {
                case "Ingredients":
                    this.saveIngredientEvent();

                    break;
                case "Meals":
                    this.saveMealEvent();

                    break;
                case "ShoppingLists":

                    break;
                default:
                    return;
            }
        }

        ItemActivity.this.finish();
    }

    public void refreshView() {
        if (this.target == null) {
            return;
        }

        String title;

        switch(MainActivity.category) {
            case "Ingredients":
                title = ((Ingredient) this.target).getName();

                break;
            case "Meals":
                title = ((Meal) this.target).getName();

                break;
            case "ShoppingLists":
                title = ((ShoppingList) this.target).getDate();

                break;
            default:
                return;
        }

        this.fieldAdapter.notifyDataSetChanged();
        this.itemName.setText(title);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGetter.putActivity(NAME, this);

        this.setContentView(R.layout.activity_item);
        this.init();
        this.initButtons();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityGetter.removeActivity(NAME);
    }

    private void initButtons() {
        this.findViewById(R.id.save_button).setOnClickListener(this);
        this.findViewById(R.id.back_button).setOnClickListener(this);
        this.findViewById(R.id.add_button).setOnClickListener(this);
    }

    private void init() {
        this.target = (Entity) this.getIntent().getSerializableExtra("target");
        this.action = this.getIntent().getStringExtra("action");
        this.itemName = this.findViewById(R.id.item_name);
        this.itemFields = this.findViewById(R.id.item_fields);

        ArrayList items = null;

        switch (MainActivity.category) {
            case "Ingredients":
                items = new ArrayList();

                this.fieldAdapter = new FieldsAdapter(this.target, items, R.layout.field_row);

                this.itemName.setText((this.target != null) ? ((Ingredient) this.target).getName() : "");

                break;
            case "Meals":
                Meal meal = (Meal) this.target;
                items = (meal != null && meal.getRecipe() != null) ? meal.getRecipe().getIngredients() : new
                        ArrayList();

                this.fieldAdapter = new FieldsAdapter(this.target, items, R.layout.field_row);

                this.itemName.setText((meal != null) ? meal.getName() : "");
                this.findViewById(R.id.add_button).setVisibility(View.VISIBLE);

                break;
            case "ShoppingLists":
                ShoppingList shpLst = (ShoppingList) this.target;
                items = (shpLst != null) ? (ArrayList) shpLst.getList() : new ArrayList();

                this.fieldAdapter = new ShoppingListAdapter(items);

                this.itemName.setText((shpLst != null) ? shpLst.getDate() : "");

                break;
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((this.getApplicationContext()));

        this.itemFields.setLayoutManager(layoutManager);
        this.itemFields.setItemAnimator(new DefaultItemAnimator());
        this.itemFields.setAdapter(this.fieldAdapter);

    }

    private void saveIngredientEvent() {
        Ingredient ingredient;
        String name = ((TextView) ItemActivity.this.findViewById(R.id.item_name)).getText().toString();

        if (ItemActivity.this.action.equals("Create")) {
            ingredient = new Ingredient(name);

            if (this.ingredientManager.isDeleted(ingredient)) {
                this.ingredientManager.restoreSoftDeleted(name);

                int ingId = this.ingredientManager.dbLoad(name).getId();
                RecipeManager rcpMgr = new RecipeManager();

                rcpMgr.restoreSoftDeletedIngredient(ingId);
            } else {
                this.ingredientManager.dbCreate(ingredient);
            }
        } else {
            ingredient = this.ingredientManager.dbLoad(ItemActivity.this.target.getId());

            ingredient.setName(name);
            this.ingredientManager.dbUpdate(ingredient);
        }
    }

    private void saveMealEvent() {
        int ingNumber = ItemActivity.this.itemFields.getChildCount();

        if (ingNumber == 0) {
            return;
        }

        Meal meal;
        MealManager mealMgr = new MealManager();
        String name = ((EditText)ItemActivity.this.findViewById(R.id.item_name)).getText().toString();

        if (ItemActivity.this.action.equals("Create")) {
            meal = new Meal(name);

            if (mealMgr.isDeleted(meal)) {
                mealMgr.restoreSoftDeleted(name);

                int mealId = mealMgr.dbLoad(name).getId();
                RecipeManager rcpMgr = new RecipeManager();
                PurchaseManager pMgr = new PurchaseManager();

                rcpMgr.restoreSoftDeleted(mealId);
                pMgr.restoreSoftDeletedMeal(mealId);

            }
        } else {
            meal = mealMgr.dbLoad(ItemActivity.this.target.getId());

            meal.setName(ItemActivity.this.itemName.getText().toString());
        }

        for (int i = 0; i < ingNumber; i++) {
            ConstraintLayout current = (ConstraintLayout) ItemActivity.this.itemFields.getChildAt(i);
            String ingName = ((EditText) current.getViewById(R.id.item_name)).getText().toString();
            String qty = ((EditText) current.getViewById(R.id.item_quantity)).getText().toString();

            if (ingName.equals("") || qty.equals("")) {
                continue;
            }

            Ingredient ing = this.ingredientManager.dbLoad(ingName);

            if (ing == null) {
                ing = new Ingredient(ingName);

                this.ingredientManager.dbCreate(ing);
            }

            meal.getRecipe().appendIngredient(ing, Integer.valueOf(qty));
        }

        if (ItemActivity.this.action.equals("Create") && !mealMgr.isDeleted(meal)) {
            mealMgr.fullDbCreate(meal);
        } else {
            mealMgr.fullDbUpdate(meal);
        }
    }
}
