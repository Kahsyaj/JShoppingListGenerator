package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Meal;

public class MealManager extends Manager {
    public static String[] EDIT_FIELDS = {"name_meal"};
    public static String[] UNEDIT_FIELDS = {"id_meal", "deleted"};

    //Constructors
    public MealManager() {
        super();
        this.setTable("Meal");
    }

    public MealManager(DbFactory dbF) {
        super(dbF);
        this.setTable("Meal");
    }

    //Other methods
    public boolean dbCreate(Meal meal) {
        try {
            int currentId = this.getCurrentId();
            String query = String.format("INSERT INTO %s (%s) VALUES (?)", this.getTable(), EDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, meal.getName());
            st.executeUpdate();
            meal.setId(currentId);
            return true;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s creating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbCreate(Meal meal) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();
            int currentId = this.getCurrentId();
            String query = String.format("INSERT INTO %s (%s) VALUES (?)", this.getTable(), EDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, meal.getName());
            st.executeUpdate();
            meal.setId(currentId);
            rcp_mgr.dbCreate(meal.getRecipe());
            return true;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s full creating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbUpdate(Meal meal) {
        try {
            String query = String.format("UPDATE %s SET %s = ? WHERE %s = ?", this.getTable(), EDIT_FIELDS[0], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, meal.getName());
            st.setInt(2, meal.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s updating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbUpdate(Meal meal) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();
            rcp_mgr.dbUpdate(meal.getRecipe());
            String query = String.format("UPDATE %s SET %s = ? WHERE %s = ?", this.getTable(), EDIT_FIELDS[0], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, meal.getName());
            st.setInt(2, meal.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s full updating.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbSoftDelete(Meal meal) {
        try {
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, meal.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbHardDelete(Meal meal) {
        try {
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, meal.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbSoftDelete(Meal meal) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();
            rcp_mgr.dbSoftDelete(meal.getRecipe());
            String query = String.format("UPDATE %s SET %s = 1 WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[1], UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, meal.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the full %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbHardDelete(Meal meal) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();
            rcp_mgr.dbHardDelete(meal.getRecipe());
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, meal.getId());
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
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
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean fullDbSoftDelete(int id) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();
            rcp_mgr.dbSoftDelete(id);
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
            RecipeManager rcp_mgr = new RecipeManager();
            rcp_mgr.dbHardDelete(id);
            String query = String.format("DELETE FROM %s WHERE %s = ?", this.getTable(), UNEDIT_FIELDS[0]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the full %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public Meal dbLoad(int id) {
        try {
            String query = String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), UNEDIT_FIELDS[0], UNEDIT_FIELDS[1]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            ResultSet rslt = st.executeQuery();
            rslt.next();
            return new Meal(rslt, true);

        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public Meal dbLoad(String name) {
        try {
            String query = String.format("SELECT * FROM %s WHERE %s = ? AND %s = 0", this.getTable(), EDIT_FIELDS[0], UNEDIT_FIELDS[1]);
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setString(1, name);
            ResultSet rslt = st.executeQuery();
            rslt.next();
            return new Meal(rslt, true);

        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public ArrayList<Meal> dbLoadAll() {
        try {
            ArrayList<Meal> mealLst = new ArrayList<>();
            String query = String.format("SELECT * FROM %s WHERE %s = 0", this.getTable(), UNEDIT_FIELDS[1]);
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            while (rslt.next()) {
                mealLst.add(new Meal(rslt, false));
            }
            rslt.close();
            return mealLst;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the whole %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public boolean fullRestoreSoftDeleted(int id) {
        try {
            RecipeManager rcp_mgr = new RecipeManager();
            rcp_mgr.restoreSoftDeleted(id);
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
            String query = String.format("SELECT MAX(%s) as %s FROM %s", UNEDIT_FIELDS[0], UNEDIT_FIELDS[0], this.getTable());
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
}
