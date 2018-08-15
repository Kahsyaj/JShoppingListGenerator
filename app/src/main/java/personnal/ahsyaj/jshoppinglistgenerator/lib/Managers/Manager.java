package personnal.ahsyaj.jshoppinglistgenerator.lib.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Entity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import personnal.ahsyaj.jshoppinglistgenerator.MainActivity;

public abstract class Manager {
    protected final static int VERSION = 1;
    protected final static String FILE_NAME = "shplst.db";

    protected String[] EDIT_FIELDS;
    protected String[] UNEDIT_FIELDS;

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
            Cursor rslt = this.database.rawQuery(String.format("SELECT COUNT(*) as cpt FROM %s",
                    this.getTable()), null);

            if (!rslt.moveToNext()) {
                return 0;
            } else {
                int nb = rslt.getInt(0);

                rslt.close();
                return nb;
            }
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred while getting the elements number from the %s table.\n", this.getTable()) + e.getMessage());
            return -1;
        }
    }

    public int getCurrentId() {
        try {
            Cursor rslt = this.database.rawQuery(String.format("SELECT MAX(%s) as %s FROM %s",
                    UNEDIT_FIELDS[0], UNEDIT_FIELDS[0], this.getTable()), null);

            if (!rslt.moveToNext()) {
                return 1;
            }
            int currentId = rslt.getInt(0) + 1;
            rslt.close();
            return currentId;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s id querying.\n", this.getTable()) + e.getMessage());
            return 0;
        }
    }

    public ArrayList<Integer> getIds() {
        try {
            ArrayList<Integer> idLst = new ArrayList<>();
            Cursor rslt = this.database.rawQuery(String.format("SELECT %s FROM %s WHERE %s = 0",
                    UNEDIT_FIELDS[0], this.getTable(), UNEDIT_FIELDS[1]), null);

            while (rslt.moveToNext()) {
                idLst.add(rslt.getInt(0));
            }
            if (idLst.size() == 0) {
                return null;
            } else {
                return idLst;
            }
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s ids querying.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }

    public boolean restoreSoftDeleted() {
        try {
            ContentValues data = new ContentValues();
            String whereClause = "deleted = ?";
            String[] whereArgs = {"0"};

            data.put("deleted", 0);
            return (this.database.update(this.table, data, whereClause, whereArgs) != 0);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with all soft deleted %s restoring.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean restoreSoftDeleted(int id) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("deleted = ? AND %s = ?", UNEDIT_FIELDS[0]);
            String[] whereArgs = {"1", String.valueOf(id)};

            data.put(UNEDIT_FIELDS[1], 0);
            return (this.database.update(this.table, data, whereClause, whereArgs) != 1);
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the soft deleted %s restoring.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbSoftDelete(int id) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", UNEDIT_FIELDS[0]);
            String[] whereArgs = {String.valueOf(id)};

            data.put(UNEDIT_FIELDS[1], 1);
            return (this.database.update(this.table, data, whereClause, whereArgs) != 0);
        } catch (SQLiteException e) {
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

    public boolean dbSoftDelete(personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity element) {
        try {
            return this.dbSoftDelete(element.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s soft deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public boolean dbHardDelete(personnal.ahsyaj.jshoppinglistgenerator.lib.Entities.Entity element) {
        try {
            return this.dbHardDelete(element.getId());
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the %s hard deletion.\n", this.getTable()) + e.getMessage());
            return false;
        }
    }

    public Cursor queryAll() {
        try {
            Cursor rslt = this.database.rawQuery(String.format("SELECT * FROM %s WHERE deleted = 0",
                    this.getTable()), null);

            rslt.moveToNext();
            return rslt;
        } catch (SQLiteException e) {
            System.err.println(String.format("An error occurred with the whole %s loading.\n", this.getTable()) + e.getMessage());
            return null;
        }
    }
}
