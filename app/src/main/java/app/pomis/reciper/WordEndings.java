package app.pomis.reciper;

/**
 * Created by romanismagilov on 02.09.15.
 */
public class WordEndings {
    static String getFor(String word){
        String ending="";
        if (      word.charAt(word.length()-1)=='а'
                ||word.charAt(word.length()-1)=='ь'
                )
            ending = "a";

        if (      word.charAt(word.length()-1)=='о'
                ||word.charAt(word.length()-1)=='е'
                )
            ending = "о";

        if (      word.charAt(word.length()-1)=='ы'
                ||word.charAt(word.length()-1)=='и'
                ||word.charAt(word.length()-1)=='я'
                ||word.equals("Яйца")
                )
            ending = "ы";

        if (
                 word.equals("Картофель")            // Исключения
                )
            ending = "";

        return ending;
    }
}
