package personnal.ahsyaj.jshoppinglistgenerator;

import java.sql.SQLException;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Ingredient;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Purchase;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Recipe;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.DbFactory;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.IngredientManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.Manager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.MealManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.PurchaseManager;
import personnal.ahsyaj.jshoppinglistgenerator.lib.Managers.RecipeManager;

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
        * Ingredient : init ;
        * Meal : init ;
        * Purchase : init ;
        * Recipe : init ;
        * ShoppingList : init ;
        *
        * IngredientManager : dbLoad ; getCurrentId ; dbCreate
        * MealManager : dbLoad ; getCurrentId ; dbCreate
        * PurchaseManager : dbLoad ; dbCreate
        * RecipeManager : dbLoad ; dbCreate
        * ShoppingListManager : dbLoad
        * */
        IngredientManager ing_mgr = new IngredientManager();
        Ingredient ing = ing_mgr.dbLoad(15);
        Ingredient ing2 = ing_mgr.dbLoad(149);
        Ingredient ing3 = new Ingredient("lololo vfvf");
        //ing_mgr.dbCreate(ing2);
        //System.out.println(ing2.toString());

        RecipeManager rcp_mgr = new RecipeManager();
        Recipe rcp = rcp_mgr.dbLoad(1);
        ArrayList<Ingredient> ing_lst = new ArrayList<>();
        ArrayList<Integer> int_lst = new ArrayList<>();
        ing_lst.add(ing);
        ing_lst.add(ing2);
        int_lst.add(new Integer(8000));
        int_lst.add(new Integer(10000));
        Recipe rcp2 = new Recipe(3, ing_lst, int_lst);
        //rcp_mgr.dbCreate(rcp2);
        //System.out.println(rcp.toString());

        MealManager meal_mgr = new MealManager();
        Meal meal = meal_mgr.dbLoad(1);
        Meal meal2 = meal_mgr.dbLoad(2);
        //meal_mgr.dbCreate(meal2);
        //System.out.println(meal2.getId());

        PurchaseManager purchase_mgr = new PurchaseManager();
        Purchase purchase = purchase_mgr.dbLoad(1);
        Purchase p2 = new Purchase(2);
        p2.getMeals().add(meal);
        p2.getMeals().add(meal2);
        //purchase_mgr.dbCreate(p2);
        //System.out.println(purchase.toString());
    }
}
