package org.eadge.gxscript.tools.check;

import java.util.Comparator;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Compare numbers
 */
public class NumberComparator<T extends Number & Comparable> implements Comparator<T>
{
    public int compare( T a, T b ) throws ClassCastException {
        //noinspection unchecked
        Double a1 = a.doubleValue();
        Double b1 = b.doubleValue();
        return a1.compareTo(b1.doubleValue());
    }
}

