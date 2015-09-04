package app.pomis.reciper;

import java.util.Comparator;

/**
 * Created by romanismagilov on 04.09.15.
 */
class ContentComparator implements Comparator<Content> {
    @Override
    public int compare(Content content, Content t1) {
        return content.content.compareTo(t1.content);
    }
}
