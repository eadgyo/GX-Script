package org.eadge.gxscript.data.compile.program;

import org.eadge.gxscript.data.compile.script.address.DataAddress;
import org.eadge.gxscript.data.compile.script.address.DebugMemory;
import org.eadge.gxscript.data.compile.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.func.FuncsStack;

/**
 * Created by eadgyo on 11/09/16.
 *
 * Debug program
 */
public class DebugProgram extends Program
{
    private DebugMemory[] debugMemories;

    public DebugProgram(FuncsStack funcsStack, DebugMemory[] debugMemories)
    {
        super(funcsStack);

        this.debugMemories = debugMemories;
    }

    public DebugProgram(Func[] funcs, FuncDataAddresses[] funcDataAddresses, DebugMemory[] debugMemories)
    {
        super(funcs, funcDataAddresses);

        this.debugMemories = debugMemories;
    }

    @Override
    public void callCurrentFunc()
    {
        DebugMemory currentDebugMemory = getCurrentDebugMemory();

        super.callCurrentFunc();

        checkMemoryAddressAllocs(getCurrentDataAddress(), currentDebugMemory);
    }

    public DebugMemory getCurrentDebugMemory()
    {
        return debugMemories[getCurrentFuncAddress().getAddress()];
    }

    public void checkMemoryAddressAllocs(DataAddress currentDataAddress, DebugMemory debugMemory)
    {
        assert(currentDataAddress.getAddress() == debugMemory.getAddress());
    }
}
