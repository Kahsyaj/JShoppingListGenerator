package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.ShoppingList;

public class ShoppingListGenerator extends Manager {
    private int mealNumber;

    //Constructors
    public ShoppingListGenerator() {
        super();
        this.mealNumber = 0;
    }

    public ShoppingListGenerator(int nb) {
        super();
        this.mealNumber = nb;
    }

    //Getters
    public int getMealNumber() {
        return this.mealNumber;
    }

    //Setters
    public void setMealNumber(int nb) {
        this.mealNumber = nb;
    }

    //Other methods

}
