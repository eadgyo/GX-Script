package org.eadge.gxscript.data.script;

import org.eadge.gxscript.data.script.address.FuncAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;

import java.util.ArrayDeque;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Abstract class for funcs
 */
public abstract class Func
{
    /**
     * Run function
     * @param funcs all called funcs
     * @param funcsParameters corresponding funcs parameters
     * @param currFuncAddress address of the current called func
     * @param memoryStack memory stack of objects
     */
    public abstract void run(Func[] funcs,
                    FuncDataAddresses[] funcsParameters,
                    FuncAddress currFuncAddress,
                    ArrayDeque<Object> memoryStack);

}
