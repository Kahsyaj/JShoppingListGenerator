package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.ShoppingList;

public class ShoppingListManager extends Manager {

    //Constructors
    public ShoppingListManager() {
        super();
        this.setTable("ShoppingList");
    }

    public ShoppingListManager(DbFactory dbF) {
        super(dbF);
        this.setTable("ShoppingList");
    }

    //Other methods
}
