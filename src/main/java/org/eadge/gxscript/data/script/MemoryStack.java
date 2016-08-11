package org.eadge.gxscript.data.script;

import org.eadge.gxscript.data.script.address.DataAddress;

import java.util.ArrayList;

/**
 * Created by eadgyo on 11/08/16.
 */
public class MemoryStack extends ArrayList<Object>
{
    public Object get(DataAddress dataAddress)
    {
        return super.get(dataAddress.getAddress());
    }
}
