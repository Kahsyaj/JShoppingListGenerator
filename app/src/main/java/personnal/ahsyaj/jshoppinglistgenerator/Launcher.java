package personnal.ahsyaj.jshoppinglistgenerator;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Purchase;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Recipe;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.ShoppingList;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.DbFactory;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.Manager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.MealManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.PurchaseManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.RecipeManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListGenerator;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.ShoppingListManager;

public class Launcher {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        /*TESTING LOG
        * Listed methods means they're working
        *
        * Ingredient : init ; toString ;
        * Meal : init ; toString ;
        * Purchase : init ; toString ;
        * Recipe : init ; toString ;
        * ShoppingList : init ; toString ;
        *
        * IngredientManager : dbLoad ; getCurrentId ; dbCreate ; softDelete ; restoreSoftDeleted ; getIds ; dbUpdate
        * MealManager : dbLoad ; getCurrentId ; dbCreate ; softDelete ; restoreSoftDeleted ; getIds ; dbUpdate ; fullDbUpdate
        * PurchaseManager : dbLoad ; dbCreate ; softDelete ; restoreSoftDeleted ; getIds ;
        * RecipeManager : dbLoad ; dbCreate ; softDelete ; restoreSoftDeleted ; getIds ;
        * ShoppingListManager : dbLoad ; getCurrentId ; dbCreate ; softDelete ; restoreSoftDeleted ; getIds ;
        * ShoppingListGenerator : generate ;
        * */
        IngredientManager ing_mgr = new IngredientManager(new DbFactory("id6237985_ahsyaj", "frk7xet3g5pny", "id6237985_shoppinglistgenerator", "localhost", "3306"));
        ing_mgr.initDb();

        Ingredient ing2 = ing_mgr.dbLoad(149);
        Ingredient ing3 = new Ingredient("lololo vfvf");
        //ing_mgr.dbCreate(ing2);
        //ing_mgr.restoreSoftDeleted(15);
        //System.out.println(ing2.toString());

        RecipeManager rcp_mgr = new RecipeManager();
        Recipe rcp = rcp_mgr.dbLoad(1);
        ArrayList<Ingredient> ing_lst = new ArrayList<>();
        ArrayList<Integer> int_lst = new ArrayList<>();

        ing_lst.add(ing2);
        int_lst.add(new Integer(8000));
        int_lst.add(new Integer(10000));
        Recipe rcp2 = new Recipe(3, ing_lst, int_lst);
        //rcp_mgr.dbCreate(rcp2);
        //System.out.println(rcp.toString());

        MealManager meal_mgr = new MealManager();
        Meal meal = meal_mgr.dbLoad(1);
        Meal meal2 = meal_mgr.dbLoad(2);
        meal.getRecipe().addIngredient(ing2, 100000);
        meal_mgr.fullDbUpdate(meal);
        //meal_mgr.dbCreate(meal2);
        //System.out.println(meal2.getId());

        PurchaseManager purchase_mgr = new PurchaseManager();
        Purchase purchase = purchase_mgr.dbLoad(1);
        Purchase p2 = new Purchase(2);
        p2.getMeals().add(meal);
        p2.getMeals().add(meal2);
        //purchase_mgr.dbCreate(p2);
        //System.out.println(purchase.toString());

        ShoppingListManager s_mgr = new ShoppingListManager();
        ShoppingList s1 = s_mgr.dbLoad(1);
        ShoppingList s2 = new ShoppingList();
        s2.setDate("2019-04-17");
        s2.setPurchase(purchase);
        //s_mgr.dbCreate(s2);
        //System.out.println(s2.toString());
        ArrayList<Integer> ids = purchase_mgr.getIds();
        for (int i = 0; i < ids.size(); i++) {
            //System.out.println(ids.get(i).toString());
        }
        ShoppingListGenerator s = new ShoppingListGenerator(2);
        s1 = s.generate();
        System.out.println(s1.toString());
    }
}
