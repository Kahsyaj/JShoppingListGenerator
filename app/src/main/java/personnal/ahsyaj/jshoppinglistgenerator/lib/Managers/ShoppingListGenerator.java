package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.content.Context;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Purchase;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.ShoppingList;

public class ShoppingListGenerator extends Manager {
    private int mealNumber;

    //Constructors
    public ShoppingListGenerator(Context context, int nb) {
        super(context);
        this.mealNumber = nb;
    }

    public ShoppingListGenerator(int nb) {
        super();
        this.mealNumber = nb;
    }

    public ShoppingListGenerator() {
        super();
        this.mealNumber = 0;
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
    public ShoppingList generate() {
        MealManager m_mgr = new MealManager();
        ShoppingListManager s_mgr = new ShoppingListManager();
        PurchaseManager p_mgr = new PurchaseManager();
        ArrayList<Integer> ids = m_mgr.getIds();
        ShoppingList shpLst = new ShoppingList(0, new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE).format(new Date()));
        s_mgr.dbCreate(shpLst);
        Purchase purchase = new Purchase(shpLst.getId());
        for (int i = 0; i < this.mealNumber && ids.size() > 0; i++) {
            int rdmI = (int) (Math.random() * (ids.size() - 1));
            System.out.println(rdmI);
            purchase.addMeal(m_mgr.dbLoad(ids.get(rdmI)));
            ids.remove(rdmI);
        }
        p_mgr.dbCreate(purchase);
        shpLst.setPurchase(purchase);
        return shpLst;
    }

}
