package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Recipe;


public class RecipeManager extends Manager {
    public static final String[] EDIT_FIELDS = {"id_ingredient", "quantity"};
    public static final String[] UNEDIT_FIELDS = {"id_meal", "deleted"};

    //Constructors
    public RecipeManager(Context context) {
        super(context);
        this.setTable("Recipe");
    }

    public RecipeManager() {
        super();
        this.setTable("Recipe");
    }

    //Other methods
    public boolean dbCreate(Recipe recipe) {
        try {
            for (int i = 0; i < recipe.size(); i++) {
                ContentValues data = new ContentValues();

                data.put(UNEDIT_FIELDS[0], recipe.getId());
                data.put(EDIT_FIELDS[0], recipe.getIngredient(i).getId());
                data.put(EDIT_FIELDS[1], recipe.getQuantity(i));

                this.database.insert(this.table, null, data);
            }
            return true;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s creating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbCreate(Recipe recipe) {
        try {
            IngredientManager ing_mgr = new IngredientManager();

            for (int i = 0; i < recipe.size(); i++) {
                ing_mgr.dbCreate(recipe.getIngredient(i));
            }
            this.dbCreate(recipe);

            return true;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s full creating.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    //Pretty dirty... But easier than comparing all the ingredients and setting/deleting for each differences
    public boolean dbUpdate(Recipe recipe) {
        this.dbHardDelete(recipe);
        return this.dbCreate(recipe);
    }

    public boolean fullDbUpdate(Recipe recipe) {
        IngredientManager ing_mgr = new IngredientManager();
        for (int i = 0; i < recipe.size(); i++) {
            ing_mgr.dbUpdate(recipe.getIngredient(i));
        }
        return this.dbUpdate(recipe);
    }

    public boolean dbSoftDelete(Recipe recipe) {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, recipe.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbHardDelete(Recipe recipe) {
        try {
            return this.dbHardDelete(recipe.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s hard deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbSoftDelete(Recipe recipe) {
        try {
            IngredientManager ing_mgr = new IngredientManager();
            for (int i = 0; i < recipe.size(); i++) {
                ing_mgr.dbSoftDelete(recipe.getIngredient(i));
            }
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, recipe.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbHardDelete(Recipe recipe) {
        try {
            IngredientManager ing_mgr = new IngredientManager();
            boolean status = this.dbHardDelete(recipe);

            for (int i = 0; i < recipe.size(); i++) {
                ing_mgr.dbHardDelete(recipe.getIngredient(i));
            }
            return status;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean dbSoftDelete(int id) {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbHardDelete(int id) {
        try {
            String whereClause = String.format("%s = ?", UNEDIT_FIELDS[0]);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return (this.database.delete(this.table, whereClause, whereArgs) != 0);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s hard deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public boolean fullDbSoftDelete(int id) {
        try {
            IngredientManager ing_mgr = new IngredientManager();
            Recipe rcp = this.dbLoad(id);
            for (int i = 0; i < rcp.size(); i++) {
                ing_mgr.dbSoftDelete(rcp.getIngredient(i));
            }
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbHardDelete(int id) {
        try {
            IngredientManager ing_mgr = new IngredientManager();
            Recipe rcp = this.dbLoad(id);
            boolean status = this.dbHardDelete(id);

            for (int i = 0; i < rcp.size(); i++) {
                ing_mgr.dbHardDelete(rcp.getIngredient(i));
            }
            return status;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());

            return false;
        }
    }

    public Recipe dbLoad(int id) {
        try {
            String query = String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            ResultSet rslt = st.executeQuery();
            rslt.next();
            return new Recipe(rslt, true);
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public ArrayList<Recipe> dbLoadAll() {
        try {
            ArrayList<Recipe> recipeLst = new ArrayList<>();
            String query = String.format("SELECT * FROM %s WHERE %s = 0", this.getTable(), UNEDIT_FIELDS[1]);
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            while (rslt.next()) {
                recipeLst.add(new Recipe(rslt, false));
            }
            rslt.close();
            return recipeLst;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the whole %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public boolean fullRestoreSoftDeleted(int id) {
        try {
            IngredientManager ing_mgr = new IngredientManager();
            Recipe rcp = this.dbLoad(id);
            for (int i = 0; i < rcp.size(); i++) {
                ing_mgr.restoreSoftDeleted(rcp.getIngredient(i).getId());
            }
            String query = String.format("UPDATE %s SET %s = 0 WHERE %s = 1 AND %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the soft deleted %s full restoring.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public int getCurrentId() {
        try {
            String query = String.format("SELECT MAX(%s) FROM %s", UNEDIT_FIELDS[0], this.getTable());
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            if (!rslt.next()) {
                return 1;
            }
            int currentId = (rslt.getInt(String.format("%s", UNEDIT_FIELDS[0])) + 1);
            rslt.close();
            return currentId;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s id querying.\n", this.getTable()) + e.getMessage());
            return 0;
        }
    }

    public ArrayList<Integer> getIds() {
        try {
            ArrayList<Integer> idLst = new ArrayList<>();
            String query = String.format("SELECT %s FROM %s WHERE %s = 0", UNEDIT_FIELDS[0], this.getTable(), UNEDIT_FIELDS[1]);
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            while (rslt.next()) {
                idLst.add(rslt.getInt(UNEDIT_FIELDS[0]));
            }
            if (idLst.size() == 0) {
                return null;
            } else {
                return idLst;
            }
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s ids querying.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }
}
