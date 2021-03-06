package personnal.ahsyaj.jshoppinglistgenerator.lib.Entities;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteException;

public final class Ingredient extends Entity {
    private static final String[] DB_FIELDS = {"id_ingredient", "name_ingredient", "deleted"};
    private String name;

    //Constructors
    public Ingredient() {
        super();

        this.name = "";
    }

    public Ingredient(Cursor rslt, boolean close) {
        super();
        this.init(rslt, close);
    }

    public Ingredient(String name) {
        super();

        this.name = name;
    }

    public Ingredient(int id, String name) {
        super(id);

        this.name = name;
    }

    //Getters
    public String getName() {
        return this.name;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    //Other methods

    @Override
    public String toString() {
        return String.format("[Ingredient]\n[id_ingredient] : %s - [name_ingredient] : %s",
                String.valueOf(this.getId()), this.getName());
    }

    public String className() {
        return "Ingredient";
    }


    private void init(Cursor rslt, boolean close) throws CursorIndexOutOfBoundsException {
        try {
            this.setId(rslt.getInt(0));
            this.setName(rslt.getString(1));
            this.setDeleted(rslt.getInt(2));

            if (close) {
                rslt.close();
            }
        } catch (SQLiteException e) {
            System.err.println("An error occurred with the ingredient init.\n" + e.getMessage());
        }
    }
}
