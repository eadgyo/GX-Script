package org.eadge.gxscript.data.compile.script.address;

/**
 * Created by eadgyo on 11/09/16.
 *
 * Debug memory address
 */
public class DebugMemory extends DataAddress
{
    public DebugMemory()
    {
    }

    public DebugMemory(int address)
    {
        super(address);
    }

    public DebugMemory(DataAddress dataAddress)
    {
        super(dataAddress);
    }
}
