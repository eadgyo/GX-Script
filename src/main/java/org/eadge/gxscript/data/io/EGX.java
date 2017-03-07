package org.eadge.gxscript.data.io;


import java.util.HashMap;

/**
 * Created by eadgyo on 05/03/17.
 *
 * Used to save GXElements
 */
public class EGX extends HashMap<String, EGXGroup>
{
    public void add(EGXGroup egxGroup)
    {
        put(egxGroup.getName(), egxGroup);
    }

    public void remove(EGXGroup egxGroup)
    {
        remove(egxGroup.getName());
    }
}
