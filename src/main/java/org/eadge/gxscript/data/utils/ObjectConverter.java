package org.eadge.gxscript.data.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ObjectConverter
{
    public static Object convert(String s, Class<?> requestedClass)
    {
        if (requestedClass.equals(Object.class))
            return s;
        if (requestedClass.equals(Number.class))
            return Double.parseDouble(s);

        try
        {
            Constructor<?> cons = requestedClass.getConstructor(String.class);
            return cons.newInstance(s);
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
