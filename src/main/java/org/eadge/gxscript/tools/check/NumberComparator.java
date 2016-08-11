package org.eadge.gxscript.tools.check;

import java.util.Comparator;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Compare numbers
 */
public class NumberComparator<T extends Number & Comparable> implements Comparator<T>
{
    public int compare( T a, T b ) throws ClassCastException
    {
        //noinspection unchecked
        return a.compareTo( b );
    }
}