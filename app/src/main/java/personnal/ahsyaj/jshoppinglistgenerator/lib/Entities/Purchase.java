package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteException;
import java.util.ArrayList;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.MealManager;

public final class Purchase extends Entity {
    private static final String[] DB_FIELDS = {"id_shoppinglist", "id_meal", "deleted"};
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

    public Purchase(Cursor rslt, boolean close) {
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
        StringBuilder repr = new StringBuilder(String.format("[Purchase]\n[%s] : %s\n", DB_FIELDS[0], String.valueOf(this.getId())));

        for (int i = 0; i < this.size(); i++) {
            repr.append(this.getMeal(i).toString());
        }
        return repr.toString();
    }

    public String className() {
        return "Purchase";
    }

    private void init(Cursor rslt, boolean close) throws CursorIndexOutOfBoundsException {
        try {
            MealManager meal_mgr = new MealManager();

            this.setId(rslt.getInt(0));
            this.setDeleted(rslt.getInt(2));

            do {
                this.addMeal((Meal)meal_mgr.dbLoad(rslt.getInt(1)));
            } while (rslt.moveToNext());

            if (close) {
                rslt.close();
            }
        } catch (SQLiteException e) {
            System.err.println("An error occurred with the purchase init.\n" + e.getMessage());
        }
    }
}

