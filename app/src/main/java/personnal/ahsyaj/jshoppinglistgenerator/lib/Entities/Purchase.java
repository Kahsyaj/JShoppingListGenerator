package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.MealManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.RecipeManager;

public class Purchase extends Entity {
    public static String[] DB_FIELDS = {"id_shoppinglist", "id_meal", "deleted"};
    private ArrayList<Meal> meals = new ArrayList<>();

    //Constructors
    public Purchase() {
        super();
    }

    public Purchase(int id) {
        super(id);
    }

    public Purchase(int id, ArrayList<Meal> meals) {
        super(id);
        this.meals = meals;
    }

    public Purchase(ResultSet rslt, boolean close) {
        super();
        this.init(rslt, close);
    }

    //Getters
    public ArrayList<Meal> getMeals() {
        return this.meals;
    }

    //Setters
    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }

    //Other methods
    public int size() {
        return this.meals.size();
    }

    public void addMeal(Meal meal) {
        this.meals.add(meal);
    }

    public Meal getMeal(int index) {
        return this.meals.get(index);
    }

    @Override
    public String toString() {
        String repr = String.format("[Purchase]\n[%s] : %s\n", DB_FIELDS[0], String.valueOf(this.getId()));
        for (int i = 0; i < this.size(); i++) {
            repr += this.getMeal(i).toString();
        }
        return repr;
    }

    public void init(ResultSet rslt, boolean close) {
        try {
            MealManager meal_mgr = new MealManager();
            this.setId(rslt.getInt(DB_FIELDS[0]));
            this.setDeleted(rslt.getInt(DB_FIELDS[2]));
            do {
                this.addMeal(meal_mgr.dbLoad(rslt.getInt(DB_FIELDS[1])));
            } while (rslt.next());
            if (close) {
                rslt.close();
            }
        } catch (SQLException e) {
            System.err.println("An error occurred with the purchase init.\n" + e.getMessage());
        }
    }
}

