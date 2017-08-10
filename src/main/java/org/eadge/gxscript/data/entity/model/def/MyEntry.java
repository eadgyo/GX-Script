package org.eadge.gxscript.data.entity.model.def;

import java.util.Map;

public class MyEntry<T1, T2> implements Map.Entry<T1, T2>
{
    T1 key;
    T2 value;

    public MyEntry(T1 key, T2 value)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public T1 getKey()
    {
        return key;
    }

    @Override
    public T2 getValue()
    {
        return value;
    }

    @Override
    public T2 setValue(T2 t2)
    {
        this.value = t2;
        return t2;
    }

    public void setKey(T1 key)
    {
        this.key = key;
    }
}
