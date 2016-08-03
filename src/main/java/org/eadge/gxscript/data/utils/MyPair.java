package org.eadge.gxscript.data.utils;

/**
 * Created by eadgyo on 19/07/16.
 *
 */
public class MyPair<L, R>
{
    private L l;
    private R r;

    public MyPair(L l, R r)
    {
        this.l = l;
        this.r = r;
    }

    public L getLeft()
    {
        return l;
    }

    public void setLeft(L l)
    {
        this.l = l;
    }

    public R getRight()
    {
        return r;
    }

    public void setRight(R r)
    {
        this.r = r;
    }

    public int compareTo(Object o)
    {
        return 0;
    }

    public Object setValue(Object o)
    {
        return null;
    }
}
