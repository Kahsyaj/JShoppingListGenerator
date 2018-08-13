package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import personnal.ahsyaj.jshoppinglistgenerator.MainActivity;

public abstract class Manager {
    protected final static int VERSION = 1;
    protected final static String FILE_NAME = "shplst.db";

    protected SQLiteDatabase database = null;
    protected DbHandler handler = null;

    protected String table;

    //Constructors
    public Manager(Context context) {
        this.handler = new DbHandler(context, FILE_NAME, null, VERSION);
    }

    public Manager() {
        this.handler = new DbHandler(MainActivity.getActivity(), FILE_NAME, null, VERSION);
    }

    //Getters

    public static int getVERSION() {
        return VERSION;
    }

    public static String getFileName() {
        return FILE_NAME;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public DbHandler getHandler() {
        return handler;
    }

    public String getTable() {
        return this.table;
    }

    //Setters
    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void setHandler(DbHandler handler) {
        this.handler = handler;
    }

    public void setTable(String table) {
        this.table = table;
    }

    //Other methods
    public SQLiteDatabase open() {
        this.database = this.handler.getWritableDatabase();
        return this.database;
    }

    public void close() {
        this.handler.close();
    }

    public int getElementsNumber() {
        try {
            String query = String.format("SELECT COUNT(*) as cpt FROM %s", this.getTable());
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            if (!rslt.next()) {
                return 0;
            } else {
                return rslt.getInt("cpt");
            }
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred while getting the elements number from the %s table.\n", this.getTable()) + e.getMessage());
            return -1;
        }
    }

    public boolean restoreSoftDeleted() {
        try {
            String query = String.format("UPDATE %s SET deleted = 0 WHERE deleted = 1", this.getTable());
            Statement st = this.getConnector().createStatement();
            return st.executeUpdate(query) != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with all soft deleted %s restoring.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean restoreSoftDeleted(int id) {
        try {
            String query = String.format("UPDATE %s SET deleted = 0 WHERE deleted = 1 AND %s = ?", this.getTable());
            PreparedStatement st = this.getConnector().prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() != 0;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the soft deleted %s restoring.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public ResultSet queryAll() {
        try {
            String query = String.format("SELECT * FROM %s WHERE deleted = 0", this.getTable());
            Statement st = this.getConnector().createStatement();
            ResultSet rslt = st.executeQuery(query);
            rslt.next();
            return rslt;
        } catch (SQLException e) {
            System.err.println(String.format("An error occurred with the whole %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }
}
