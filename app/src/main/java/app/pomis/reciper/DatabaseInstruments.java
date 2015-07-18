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
        //DB.execSQL("DROP TABLE IF EXISTS Recipes");
        DB.execSQL("CREATE TABLE IF NOT EXISTS Recipes" +
                "(" +
                "RID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "ShortDescription nvarchar(1000)," +
                "Description nvarchar(4000)," +
                "Contents nvarchar(1000)," +
                "Name nvarchar(255)," +
                "Source nvarchar(1000)," +
                "Favorite INTEGER"+
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

    public void makeFavorite(String name, boolean fave){
        DB.execSQL("UPDATE Recipes " +
                "SET Favorite="+((fave)?1:0)+" "+
                "WHERE Name="+quote(name));
    }
    String quote(String string) {
        return "'" + string + "'";
    }

    public void insertBasicRecipes() {
        insertRecipe("Суп крестьянский", "Лук-порей разрезаем вдоль на две части и нарезаем. Отправляем на сковородку с растительным маслом обжариваться на среднем огне.\n" +
                        "Чистим, моем и нарезаем кабачки, сельдерей и перец.\n" +
                        "Морковь чистим, моем и натираем на крупной терке.\n" +
                        "Отправляем все подготовленные овощи к луку на сковородку обжариваться.\n" +
                        "Пока жарятся овощи, картофель очищаем, моем и нарезаем небольшими кубиками.\n" +
                        "В кастрюлю для супа выкладываем картофель, обжаренные овощи и наливаем воду. Солим и варим до готовности картофеля. Практически за пару минут до готовности добавляем перец молотый, лавровый лист и зелень. В самом конце добавляем измельченный чеснок.\n",
                "Вкусный и легкий суп, очень недорогой и готовится быстро. Крестьянский - потому что без мяса и с большим количеством овощей. Готовим крестьянский суп на скорую руку!\t",
                "Лук,Картофель,Перец болгарский,Морковь,Кабачок,Сельдерей,Укроп,Масло,Соль,Перец чёрный,Лавровый лист",
                "http://povar.ru/recipes/sup_krestyanskii_na_skoruiu_ruku-14500.html#close"
        );
        insertRecipe("Мясной пирог", "Смешать сметану с майонезом, добавить яйца и перемешать.\n" +
                        "Добавить муку, смешанную с содой и замесить тесто. Оно не должно быть густым!\n" +
                        "Нарезаем полукольцами лук порей.\n" +
                        "Сырое мясо очистить от прожилок и нарезать мелкими кубиками.\n" +
                        "Картофель чистим, моем и нарезаем маленькими кубиками.\n" +
                        "Делаем начинку для пирога. Для этого смешиваем мясо, лук и картофель. Солим и перчим.\n" +
                        "Форму для выпечки смазываем маслом и выкладываем в нее половину теста. Руки лучше смочить водой или растительным маслом.\n" +
                        "На слой теста выкладываем начинку, разравниваем.\n" +
                        "Укрываем начинку оставшимся тестом. Ставим в духовку выпекаться при температуре 180 градусов примерно на 1 час. Если верх пирога пригорает - накройте его фольгой.\n",
                "Вкусный и сытный пирог придется по вкусу всем, особенно мужчинам. А главное, что готовится этот мясной пирог на скорую руку - вам не придется тратить много времени и сил на его приготовление!\t",
                "Майонез,Сметана,Яйца,Мука,Сода,Соль,Мясо,Картофель,Лук,Перец чёрный",
                "http://povar.ru/recipes/myasnoi_pirog_na_skoruiu_ruku-14504.html"
        );
        insertRecipe("Сырники", "Хорошенько разотрем творог, от этого сырники получатся нежнее.\n" +
                        "Добавляем к творогу муку, яйцо, сахар. По желанию, можно добавить щепотку ванильного сахара.\n" +
                        "Быстренько все перемешиваем. Получившаяся масса не должна быть очень густой, но и не должна сильно липнуть к рукам. Регулируем плотность массы, добавляя или уменьшая количество муки.\n" +
                        "Для того, чтобы сырники получились примерно одинакового размера, формируем из получившегося творожного теста колбаску и разрезаем ее на равные части.\n" +
                        "Придаем кусочкам теста желаемую форму - круглую или овальную, для деток можно сделать и квадратные и треугольные сырники:) На разогретую с маслом сковородку выкладываем сырники и жарим.\n" +
                        "Обжариваем сырники на среднем огне до образования приятной золотистой корочки с обеих сторон.\n",
                "Такие сырники великолепно подойдут для быстрого завтрака или для капризных деток, которые не любят есть творог. Горячие и ароматные сырники на скорую руку едят все!\t",
                "Творог,Мука,Яйца,Сахар,Масло,Сахар",
                "http://povar.ru/recipes/syrniki_na_skoruiu_ruku-14408.html"
        );
        insertRecipe("Бананы Фламбе", "Из апельсина выжать сок, бананы очистить. \n" +
                        "На сковороду положить масло и сахар, нагревать на среднем огне, помешивая. Когда сахар пропитается маслом, влить сок, мешать до растворения сахара. \n" +
                        "Затем положить бананы и готовить их в сиропе минут 5, время от времени осторожно переворачивая (лучше делать это плоской широкой лопаткой, т.к. бананы быстро становятся мягкими и их легко повредить).\n" +
                        "Затем, не снимая с огня, влить ром и сразу же поджечь.\n" +
                        "При этом не подносите руки близко к сковороде, можно обжечься!\n" +
                        "Подождать, пока пламя погаснет само собой и снять сковороду с огня. \n" +
                        "При подаче бананы выложить на тарелку (аккуратно, т.к. они очень мягкие!), полить сиропом, можно посыпать орешками.\n",
                "Для детей этот десерт можно приготовить без алкоголя, просто потушив бананы в сиропе, получится \"не фламбе\", но тоже будет вкусно.\n" +
                        "Фламбирование или фламбе (от французского flamber - пылать, пламенеть) - это приём кулинарной обработки, при котором блюдо поливают крепким алкогольным напитком и затем поджигают. " +
                        "Спирт постепенно выгорает и придаёт блюду неповторимый вкус и аромат.",
                "Банан,Апельсин,Ром или коньяк,Масло,Сахар",
                "http://gotovim-doma.ru/forum/viewtopic.php?p=1066206");
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
    public ArrayList<Recipe> loadFaves(){
        ArrayList<Recipe> list = new ArrayList<>();
        String query = String.format("SELECT * FROM Recipes WHERE Favorite=1");
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
