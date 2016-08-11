package org.eadge.gxscript.tools.run;

import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.address.FuncAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;

import java.util.ArrayDeque;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Run GX compiled code
 */
public class GXRunner
{
    public void run(CompiledGXScript compiledGXScript)
    {
        // Create function pointer
        FuncAddress currFuncAddress = new FuncAddress(0);

        // Create stack of data
        ArrayDeque<Object> memoryStack = new ArrayDeque<>();

        // Get list of func and correspond parameters
        Func[] funcs = compiledGXScript.getCalledFunctions();
        FuncDataAddresses[] funcsParameters = compiledGXScript.getCalledFunctionsParameters();

        // While there are functions left
        while (currFuncAddress.getAddress() < funcs.length)
        {
            // Get function and corresponding parameters
            Func currentFunc = funcs[currFuncAddress.getAddress()];
            FuncDataAddresses funcParameters = funcsParameters[currFuncAddress.getAddress()];

            // Call function
            currentFunc.run(funcs, funcsParameters, currFuncAddress, memoryStack);

            // Move func pointer down
            currFuncAddress.addOffset(1);
        }
    }
}
