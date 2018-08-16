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

    }
}
