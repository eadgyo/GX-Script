package org.eadge.gxscript.data.compile.program;

import org.eadge.gxscript.data.compile.program.func.FuncsStack;
import org.eadge.gxscript.data.compile.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.compile.script.func.Func;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class DisplayProgram extends Program
{
    private List<String> funcsName;
    private OutputStream outputStream;

    public DisplayProgram(FuncsStack funcsStack, List<String> funcsName, OutputStream outputStream)
    {
        super(funcsStack);
        this.funcsName = funcsName;
        this.outputStream = outputStream;
    }

    public DisplayProgram(Func[] funcs,
                          FuncDataAddresses[] funcDataAddresses,
                          List<String> funcsName, OutputStream outputStream)
    {
        super(funcs, funcDataAddresses);
        this.funcsName = funcsName;
        this.outputStream = outputStream;
    }

    /**
     * Get the func name at the current func address
     *
     * @return func name at the current func address
     */
    public String getCurrentFuncName()
    {
        return funcsName.get(getCurrentAbsoluteFuncAddress().getAddress());
    }

    /**
     * Call the current pointed script
     */
    public void callCurrentFunc()
    {
        Func currentFunc = getCurrentFunc();
        String name = getCurrentFuncName();
        try
        {
            outputStream.write((name + "\n").getBytes());
            outputStream.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        currentFunc.run(this);
    }


}
