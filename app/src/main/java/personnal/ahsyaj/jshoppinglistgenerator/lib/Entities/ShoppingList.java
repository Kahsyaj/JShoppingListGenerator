package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteException;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.PurchaseManager;

public final class ShoppingList extends Entity {
    private static final String[] DB_FIELDS = {"id_shoppinglist", "date_shoppinglist", "deleted"};
    private String date;
    private Purchase purchase;

    //Constructors
    public ShoppingList() {
        super();

        this.date = "";
        this.purchase = new Purchase();
    }

    public ShoppingList(Cursor rslt, boolean close) {
        super();
        this.init(rslt, close);
    }

    public ShoppingList(int id) {
        super(id);

        this.date = "";
        this.purchase = new Purchase();
    }

    public ShoppingList(int id, String date) {
        super(id);

        this.date = date;
        this.purchase = new Purchase();
    }

    public ShoppingList(int id, String date, Purchase purchase) {
        super(id);

        this.date = date;
        this.purchase = purchase;
    }

    //Getters
    public String getDate() {
        return this.date;
    }

    public Purchase getPurchase() {
        return this.purchase;
    }

    //Setters
    public void setDate(String date) {
        this.date = date;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    //Other methods
    @Override
    public String toString() {
        String repr = String.format("[ShoppingList]\n[%s] : %s - [%s] : %s\n", DB_FIELDS[0], String.valueOf(this.getId()), DB_FIELDS[1], this.getDate());

        repr += this.purchase.toString();

        return repr;
    }

    public String className() {
        return "ShoppingList";
    }

    public List<ArrayList> getList() {
        Recipe tmpRecipe = new Recipe();

        for (Meal meal : this.getPurchase().getMeals()) {
            for (int i = 0; i < meal.getRecipe().size(); i++) {
                tmpRecipe.addIngredient(meal.getRecipe().getIngredient(i), meal.getRecipe().getQuantity(i));
            }
        }

        return tmpRecipe.getIngredients();
    }

    private void init(Cursor rslt, boolean close) throws CursorIndexOutOfBoundsException {
        try {
            PurchaseManager p_mgr = new PurchaseManager();

            this.setId(rslt.getInt(0));
            this.setDate(rslt.getString(1));
            this.setDeleted(rslt.getInt(2));

            this.purchase = p_mgr.dbLoad(this.getId());

            if (close) {
                rslt.close();
            }
        } catch (SQLiteException e) {
            System.err.println("An error occurred with the recipe init.\n" + e.getMessage());
        }
    }
}
