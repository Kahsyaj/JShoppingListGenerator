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

    protected SQLiteDatabase database = null;
    protected DbHandler handler = null;

    protected String table;

    //Constructors
    public Manager(Context context) {
        this.handler = new DbHandler(context, FILE_NAME, null, VERSION);
    }

    public Manager() {
        this.handler = new DbHandler(MainActivity.activity, FILE_NAME, null, VERSION);
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
