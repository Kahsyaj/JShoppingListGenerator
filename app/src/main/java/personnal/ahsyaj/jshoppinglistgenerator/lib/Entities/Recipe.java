package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;

public class Recipe extends Entity {
    public static String[] DB_FIELDS = {"id_meal", "id_ingredient", "quantity", "deleted"};
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<Integer> quantities = new ArrayList<>();

    //Constructors
    public Recipe() {
        super();
    }

    public Recipe(ResultSet rslt, boolean close) {
        super();
        this.init(rslt, close);
    }

    public Recipe(int id, ArrayList<Ingredient> ingredients, ArrayList<Integer> quantities) {
        super(id);
        this.ingredients = ingredients;
        this.quantities = quantities;
    }

    //Getters
    public ArrayList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public ArrayList<Integer> getQuantities() {
        return this.quantities;
    }

    //Setters
    public void setIngredients(ArrayList<Ingredient> ingredients) {
        if (quantities.size() == this.size()) {
            this.ingredients = ingredients;
        } else {
            throw new UnsupportedOperationException("The ingredients size don't match to quantities size.");
        }
    }

    public void setQuantities(ArrayList<Integer> quantities) {
        if (quantities.size() == this.size()) {
            this.quantities = quantities;
        } else {
            throw new UnsupportedOperationException("The quantities size don't match to ingredients size.");
        }
    }

    //Other methods
    public Ingredient getIngredient(int index) {
        return this.ingredients.get(index);
    }

    public Integer getQuantity(int index) {
        return this.quantities.get(index);
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
            this.setIngredientQuantity(ing.getName(), qty);
        } else {
            this.ingredients.add(ing);
            this.quantities.add(qty);
        }
    }

    public void setIngredientName(String name, String newName) {
        for (int i = 0; i < this.size(); i++) {
            if (this.getIngredient(i).getName().equals(name)) {
                this.ingredients.set(i, new Ingredient(name));
            }
        }
    }

    public void setIngredientQuantity(String name, Integer newQuantity) {
        for (int i = 0; i < this.size(); i++) {
            if (this.getIngredient(i).getName().equals(name)) {
                this.quantities.set(i, newQuantity);
            }
        }
    }

    public int size() {
        return this.getIngredients().size();
    }

    @Override
    public String toString() {
        String repr = String.format("[Recipe]\n[%s] : %s\n", DB_FIELDS[0], String.valueOf(this.getId()));
        for (int i = 0; i < this.size(); i++) {
            repr += this.getIngredient(i).toString() + " - ";
            repr += String.format("[quantity] : %s\n", this.getQuantities().get(i).toString());
        }
        return repr;
    }

    public void init(ResultSet rslt, boolean close) {
        try {
            IngredientManager ing_mgr = new IngredientManager();
            this.setId(rslt.getInt(DB_FIELDS[0]));
            this.setDeleted(rslt.getInt(DB_FIELDS[3]));
            do {
                this.ingredients.add(ing_mgr.dbLoad(rslt.getInt(DB_FIELDS[1])));
                this.quantities.add(rslt.getInt(DB_FIELDS[2]));
            } while (rslt.next());
            if (close) {
                rslt.close();
            }
        } catch (SQLException e) {
            System.err.println("An error occurred with the recipe init.\n" + e.getMessage());
        }
    }
}