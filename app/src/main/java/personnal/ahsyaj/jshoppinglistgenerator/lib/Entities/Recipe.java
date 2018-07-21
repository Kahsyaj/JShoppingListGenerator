package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Recipe extends Entity {
    public static String[] DB_FIELDS = {"id_meal", "id_ingredient", "quantity", "deleted"};
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Integer> quantities;

    //Constructors
    public Recipe() {
        super();
        this.ingredients = new ArrayList<>();
        this.quantities = new ArrayList<>();
    }

    public Recipe(ResultSet rslt) {
        super();
        this.init(rslt);
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
        if (quantities.size() == this.ingredients.size()) {
            this.ingredients = ingredients;
        } else {
            throw new UnsupportedOperationException("The ingredients size don't match to quantities size.");
        }
    }

    public void setQuantities(ArrayList<Integer> quantities) {
        if (quantities.size() == this.ingredients.size()) {
            this.quantities = quantities;
        } else {
            throw new UnsupportedOperationException("The quantities size don't match to ingredients size.");
        }
    }

    //Other methods
    public void addIngredient(Ingredient ing, Integer qty) {
        this.ingredients.add(ing);
        this.quantities.add(qty);
    }

    public void setIngredientName(String name, String newName) {
        for (int i = 0; i < this.ingredients.size(); i++) {
            if (this.ingredients.get(i).getName().equals(name)) {
                this.ingredients.set(i, new Ingredient(name));
            }
        }
    }

    public void setIngredientQuantity(String name, Integer newQuantity) {
        for (int i = 0; i < this.ingredients.size(); i++) {
            if (this.ingredients.get(i).getName().equals(name)) {
                this.quantities.set(i, newQuantity);
            }
        }
    }

    @Override
    public String toString() {
        String repr = "[Recipe]\n";
        for (int i = 0; i < this.getIngredients().size(); i++) {
            repr += this.getIngredients().get(i).toString() + " - ";
            repr += String.format("[quantity] : %s\n", this.getQuantities().get(i).toString());
        }
        return repr;
    }

    public void init(ResultSet rslt) {
        try {
            rslt.next();
            this.setId(rslt.getInt(DB_FIELDS[0]));
            this.setDeleted(rslt.getInt(DB_FIELDS[3]));
            do {
                this.ingredients.add(new Ingredient(rslt));
                this.quantities.add(rslt.getInt(DB_FIELDS[2]));
            } while (rslt.next());

            rslt.beforeFirst();
        } catch (SQLException e) {
            System.err.println("An error occurred with the meal init.\n" + e.getMessage());
        }
    }
}