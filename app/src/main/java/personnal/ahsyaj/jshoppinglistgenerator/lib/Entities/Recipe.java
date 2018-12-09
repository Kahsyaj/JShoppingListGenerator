package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteException;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;

public final class Recipe extends Entity {
    private static final String[] DB_FIELDS = {"id_meal", "id_ingredient", "quantity", "deleted"};
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

    public Integer getQuantity(Ingredient ingredient) {
        for (int i = 0; i < this.size(); i++) {
            if (this.getIngredient(i).getName().equals(ingredient.getName())) {
                return this.getQuantity(i);
            }
        }

        return 0;
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
            this.setQuantity(ing, this.getQuantity(ing) + qty);
        }

        this.appendIngredient(ing, qty);
    }

    public void appendIngredient(Ingredient ing, Integer qty) {
        if (this.inRecipe(ing)) {
            return;
        }

        ArrayList ingredient = new ArrayList();

        ingredient.add(ing);
        ingredient.add(qty);
        this.ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ing) {
        if (this.inRecipe(ing)) {
            for (ArrayList pair : this.ingredients) {
                if (pair.get(0) == ing) {
                    this.ingredients.remove(pair);

                    return;
                }
            }
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

    public String className() {
        return "Recipe";
    }

    private void init(Cursor rslt, boolean close) throws CursorIndexOutOfBoundsException {
        try {
            IngredientManager ing_mgr = new IngredientManager();

            this.setId(rslt.getInt(0));
            this.setDeleted(rslt.getInt(3));

            do {
                if (rslt.getInt(0) != this.getId()) {
                    break;
                }

                this.appendIngredient(ing_mgr.dbLoad(rslt.getInt(1)), rslt.getInt(2));
            } while (rslt.moveToNext());

            if (close) {
                rslt.close();
            }

            this.toString();
        } catch (SQLiteException e) {
            System.err.println("An error occurred with the recipe init.\n" + e.getMessage());
        }
    }
}