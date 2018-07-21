package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

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

    public Meal(ResultSet rslt) {
        super();
        this.init(rslt);
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
        String repr = String.format("[Meal]\n[id_meal] : %s - [name_meal] : %s\n", String.valueOf(this.getId()), this.getName());
        repr += this.recipe.toString();
        return repr;
    }

    public void init(ResultSet rslt) {
        try {
            this.setId(rslt.getInt(DB_FIELDS[0]));
            this.setName(rslt.getNString(DB_FIELDS[1]));
            this.setDeleted(rslt.getInt(DB_FIELDS[2]));
            RecipeManager rcp_mgr = new RecipeManager();
            this.setRecipe(rcp_mgr.dbLoad(this.getId()));
        } catch (SQLException e) {
            System.err.println("An error occurred with the meal init.\n" + e.getMessage());
        }
    }
}
