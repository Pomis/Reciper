package app.pomis.reciper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by romanismagilov on 16.07.15.
 */
public class DatabaseInstruments {

    DBHelper dbHelper;
    SQLiteDatabase DB;
    static public DatabaseInstruments singleton;

    public DatabaseInstruments(Context context) {
        dbHelper = new DBHelper(context);
        DB = dbHelper.getWritableDatabase();
        createDB();
        singleton = this;
    }

    public void createDB() {
        DB.execSQL("DROP TABLE IF EXISTS Recipes");
        DB.execSQL("CREATE TABLE IF NOT EXISTS Recipes" +
                "(" +
                "RID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "ShortDescription nvarchar(1000)," +
                "Description nvarchar(4000)," +
                "Contents nvarchar(1000)," +
                "Name nvarchar(255)," +
                "Source nvarchar(1000)" +
                ")");

        String query = String.format("SELECT * FROM Recipes");
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
        if (cursor.getCount() == 0)
            insertBasicRecipes();
        cursor.close();
    }

    public void insertRecipe(String name, String description, String shortDescription, String contents, String source) {

        DB.execSQL("INSERT INTO Recipes(Name, Description, ShortDescription, Contents, Source) VALUES(" +
                quote(name) + "," + quote(description) + "," + quote(shortDescription) + "," + quote(contents)
                + "," + quote(source) +
                ")");
    }
    String quote(String string){
        return "'"+string+"'";
    }

    public void insertBasicRecipes() {
        insertRecipe("Рецепт кеки", "кека кккк", "nfjksdfkjsd", "heyywuiajsd, jifdjog, dfhgi d, idufhgi , duifhgdi g", "dnfsjkdf");
    }

    public ArrayList<Recipe> loadBasicRecipes() {
        ArrayList<Recipe> list = new ArrayList<>();
        String query = String.format("SELECT * FROM Recipes");
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
        while (cursor.moveToNext()) {
            Recipe recipe = new Recipe(
                    cursor.getString(4),
                    cursor.getString(1),
                    cursor.getString(2),
                    new ArrayList<>(Arrays.asList(cursor.getString(3).split(",")))
            );
            list.add(recipe);
        }
        cursor.close();
        return list;
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
