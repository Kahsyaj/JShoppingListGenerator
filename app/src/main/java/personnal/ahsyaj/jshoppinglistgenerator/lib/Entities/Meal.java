package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.sql.ResultSet;
import java.sql.SQLException;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.RecipeManager;

public class Meal extends Entity {
    public static String[] DB_FIELDS = {"id_meal", "name_meal", "deleted"};
    private String name;
    private Recipe recipe;

    //Constructors
    public Meal() {
        super();
        this.name = "";
        this.recipe = new Recipe();
    }

    public Meal(Cursor rslt, boolean close) {
        super();
        this.init(rslt, close);
    }

    public Meal(String name) {
        super();
        this.name = name;
    }

    public Meal(int id, String name) {
        super(id);
        this.name = name;
        this.recipe = new Recipe();
    }

    public Meal(int id, String name, Recipe recipe) {
        super(id);
        this.name = name;
        this.recipe = recipe;
    }

    //Getters
    public String getName() {
        return this.name;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    //Other methods
    @Override
    public String toString() {
        String repr = String.format("[Meal]\n[%s] : %s - [%s] : %s\n", DB_FIELDS[0], String.valueOf(this.getId()), DB_FIELDS[1], this.getName());

        repr += this.recipe.toString();
        return repr;
    }

    public void init(Cursor rslt, boolean close) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();

            this.setId(rslt.getInt(0));
            this.setName(rslt.getString(1));
            this.setDeleted(rslt.getInt(2));
            this.setRecipe((Recipe) rcp_mgr.dbLoad(this.getId()));
            if (close) {
                rslt.close();
            }
        } catch (SQLiteException e) {
            System.err.println("An error occurred with the meal init.\n" + e.getMessage());
        }
    }
}
