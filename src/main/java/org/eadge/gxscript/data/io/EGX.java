package org.eadge.gxscript.data.io;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eadgyo on 05/03/17.
 *
 * Used to save GXElements
 */
public class EGX implements Serializable
{
    private Map<String, EGXGroup> egxGroups;

    public EGX()
    {
        new HashMap<>();
    }
}
