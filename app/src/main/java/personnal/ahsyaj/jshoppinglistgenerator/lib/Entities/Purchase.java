package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

import java.util.ArrayList;

public class Purchase extends Entity {
    public static String[] DB_FIELDS = {"id_shoppinglist", "id_ingredient", "quantity", "deleted"};
    private ArrayList<Meal> meals;

    //Constructors
    public Purchase() {
        super();
        this.meals = new ArrayList<>();
    }

    public Purchase(int id) {
        super(id);
        this.meals = new ArrayList<>();
    }

    public Purchase(int id, ArrayList<Meal> meals) {
        super(id);
        this.meals = meals;
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

    @Override
    public String toString() {
        String repr = "[Recipe]\n";
        for (int i = 0; i < this.meals.size(); i++) {
            repr += this.meals.get(i).toString();
        }
        return repr;
    }
}
