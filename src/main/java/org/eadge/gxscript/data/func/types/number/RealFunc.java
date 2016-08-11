package org.eadge.gxscript.data.func.types.number;

import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.address.FuncAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;

import java.util.ArrayDeque;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Real number creator func
 */
public class RealFunc extends Func
{
    private float defaultFloat;
    public RealFunc(float defaultFloat)
    {
        this.defaultFloat = defaultFloat;
    }

    @Override
    public void run(Func[] funcs,
                    FuncDataAddresses[] funcsParameters,
                    FuncAddress currFuncAddress,
                    ArrayDeque<Object> memoryStack)
    {
        memoryStack.add(defaultFloat);
    }
}
