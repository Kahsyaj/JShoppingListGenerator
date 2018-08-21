package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;

public class Recipe extends Entity {
    public static String[] DB_FIELDS = {"id_meal", "id_ingredient", "quantity", "deleted"};
    private ArrayList<ArrayList> ingredients = new ArrayList<>();


    //Constructors
    public Recipe() {
        super();
    }

    public Recipe(Cursor rslt, boolean close) {
        super();
        this.init(rslt, close);
    }

    public Recipe(int id, ArrayList<ArrayList> ingredients) {
        super(id);
        this.ingredients = ingredients;
    }

    //Getters
    public ArrayList<ArrayList> getIngredients() {
        return this.ingredients;
    }

    //Setters
    public void setIngredients(ArrayList<ArrayList> ingredients) {
        this.ingredients = ingredients;
    }

    //Other methods
    public Ingredient getIngredient(int index) {
        return (Ingredient) this.ingredients.get(index).get(0);
    }

    public void setIngredient(int index, Ingredient ingredient) {
        this.ingredients.get(index).set(0, ingredient);
    }

    public Integer getQuantity(int index) {
        return (Integer) this.ingredients.get(index).get(1);
    }

    public void setQuantity(int index, Integer qty) {
        this.ingredients.get(index).set(1, qty);
    }

    public void setQuantity(Ingredient ingredient, Integer qty) {
        for (int i = 0; i < this.size(); i++) {
            if (ingredient.getName().equals(this.getIngredient(i).getName())) {
                this.setQuantity(i, qty);
            }
         }
    }

    public boolean inRecipe(Ingredient ing) {
        for (int i = 0; i < this.size(); i++) {
            if (this.getIngredient(i).getName().equals(ing.getName())) {
                return true;
            }
        }
        return false;
    }

    public void addIngredient(Ingredient ing, Integer qty) {
        if (this.inRecipe(ing)) {
            this.setQuantity(ing, qty);
        } else {
            ArrayList ingredient = new ArrayList();
            ingredient.add(ing);
            ingredient.add(qty);
            this.ingredients.add(ingredient);
        }
    }

    public int size() {
        return this.ingredients.size();
    }

    @Override
    public String toString() {
        StringBuilder repr = new StringBuilder(String.format("[Recipe]\n[%s] : %s\n", DB_FIELDS[0], String.valueOf(this.getId())));

        for (int i = 0; i < this.size(); i++) {
            repr.append(this.getIngredient(i).toString()).append(" - ");
            repr.append(String.format("[quantity] : %s\n", this.getQuantity(i).toString()));
        }
        return repr.toString();
    }

    public void init(Cursor rslt, boolean close) {
        try {
            IngredientManager ing_mgr = new IngredientManager();

            this.setId(rslt.getInt(0));
            this.setDeleted(rslt.getInt(3));
            do {
                this.addIngredient(ing_mgr.dbLoad(rslt.getInt(1)), rslt.getInt(2));
            } while (rslt.moveToNext());
            if (close) {
                rslt.close();
            }
        } catch (SQLiteException e) {
            System.err.println("An error occurred with the recipe init.\n" + e.getMessage());
        }
    }

    public String className() {
        return "Recipe";
    }
}