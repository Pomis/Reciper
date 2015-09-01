package app.pomis.reciper;

import android.view.View;

/**
 * Created by romanismagilov on 20.08.15.
 */
public class IconHolder {
    static public int getDishIconId(String KindOfDish){
        switch (KindOfDish){
            case "Суп": return R.drawable.soup;
            case "Второе": return R.drawable.dish;
            case "Котлеты": return R.drawable.kotlets;
            case "Пирог": return R.drawable.pirog;
            case "Шаурма": return R.drawable.shawarma;
            case "Десерт": return R.drawable.desert;
            case "Пицца": return R.drawable.pizza;
            case "Рулеты" : return R.drawable.roolet;
            case "Бутерброды" : return R.drawable.booter;
            case "Пельмени" : return R.drawable.pelmenidish;

            default: return R.drawable.defdish;
        }
    }

    static public int getContentIconId(String icoName){
        switch (icoName){
            case "Кабачок": return R.drawable.kabak;
            case "Лавровый лист": return R.drawable.lavr;
            case "Лук": return R.drawable.look;
            case "Масло": return R.drawable.maslow;
            case "Яйца": return R.drawable.egg;
            case "Соль": return R.drawable.salt;
            case "Сода": return R.drawable.soda;
            case "Мука": return R.drawable.mooka;
            case "Огурец": return R.drawable.ogurez;
            case "Помидор": return R.drawable.pomi;
            case "Сметана": return R.drawable.smetana;
            case "Майонез": return R.drawable.mayonez;
            case "Апельсин": return R.drawable.apelsin;
            case "Банан": return R.drawable.banan;
            case "Картофель": return R.drawable.kartoshka;
            case "Морковь": return R.drawable.morqov;
            case "Перец болгарский": return R.drawable.perecbolgar;
            case "Перец чёрный": return R.drawable.perecblack;
            case "Укроп": return R.drawable.ooqrop;
            case "Мясо": return R.drawable.meat;
            case "Сыр": return R.drawable.cheese;
            case "Капуста": return R.drawable.kapoosta;
            case "Колбаса": return R.drawable.kolbasa;
            case "Крабовые палочки": return R.drawable.krab;
            case "Кубик-бульон": return R.drawable.kubiq;
            case "Макароны": return R.drawable.makarons;
            case "Молоко": return R.drawable.moloko;
            case "Чеснок": return R.drawable.chesnoq;
            case "Грибы": return R.drawable.gribs;
            case "Сосиски": return R.drawable.sosisqa;
            case "Горошек": return R.drawable.gorokh;
            case "Армянский лаваш": return R.drawable.lawash;
            case "Ветчина": return R.drawable.vetchina;
            case "Кетчуп": return R.drawable.keptuch;
            case "Сахар": return R.drawable.sakhar;
            case "Пельмени": return R.drawable.pelmesh;
            case "Зелень": return R.drawable.ooqrop;
            case "Баклажан": return R.drawable.baqlajan;
            case "Курица": return R.drawable.kooritza;
            case "Петрушка": return R.drawable.petr;
            case "Ром или коньяк": return R.drawable.rom;
            case "Щавель": return R.drawable.shavel;
            case "Рис": return R.drawable.ris;
            case "Хлеб": return R.drawable.khleb;
            case "Яблоко": return R.drawable.yablo;
            case "Фарш": return R.drawable.farsh;
            case "Тушёнка": return R.drawable.tooshenqa;
            case "Гречка": return R.drawable.grecha;



            default: return R.drawable.product;
        }
    }
}
