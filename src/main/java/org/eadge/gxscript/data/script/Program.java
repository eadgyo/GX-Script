package org.eadge.gxscript.data.script;

import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.script.address.FuncImbricationDataAddresses;
import org.eadge.gxscript.data.exception.HasPassedTargetAddressException;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Holds memory, script and current read function
 */
public class Program
{
    private MemoryStack memoryStack = new MemoryStack();
    private FuncAddress currentFuncAddress = new FuncAddress(0);
    private FuncsStack funcsStack;

    public Program(FuncsStack funcsStack)
    {
        this.funcsStack = funcsStack;
    }

    public Program(Func[] funcs, FuncDataAddresses[] funcDataAddresses)
    {
        this.funcsStack = new FuncsStack(funcs, funcDataAddresses);
    }

    public void callCurrentFunc()
    {
        Func currentFunc = getCurrentFunc();

        currentFunc.run(this);
    }

    // Memory Stack
    public void pushInMemory(Object object)
    {
        memoryStack.add(object);
    }

    public void popFromMemory()
    {
        memoryStack.remove(memoryStack.size() - 1);
    }

    public Object[] loadCurrentParametersObjects()
    {
        FuncDataAddresses currentFuncParameters = getCurrentFuncParameters();

        // Create objects
        Object objects[] = new Object[currentFuncParameters.numberOfInputs()];

        // Get object from memory
        for (int inputIndex = 0; inputIndex < objects.length; inputIndex++)
        {
            objects[inputIndex] = loadObject(currentFuncParameters.getInputAddress(inputIndex));
        }

        return objects;
    }

    public Object loadObject(DataAddress dataAddress)
    {
        return memoryStack.get(dataAddress);
    }

    public void setObject(DataAddress dataAddress, Object object) { memoryStack.set(dataAddress, object); }

    // Current Func Address
    public void setCurrentFuncAddress(FuncAddress currentFuncAddress)
    {
        this.currentFuncAddress.setAddress(currentFuncAddress);
    }

    public boolean hasPassedAddress(FuncAddress testedFuncAddress)
    {
        return currentFuncAddress.isAfter(testedFuncAddress);
    }

    public boolean isOnAddress(FuncAddress testedFuncAddress)
    {
        return currentFuncAddress.equals(testedFuncAddress);
    }

    public FuncAddress getCurrentFuncAddress()
    {
        return currentFuncAddress;
    }

    public void addCurrentFuncAddressOffset(int offset)
    {
        currentFuncAddress.addOffset(offset);
    }

    public void addCurrentFuncAddressOffset(FuncAddress funcAddress)
    {
        currentFuncAddress.addOffset(funcAddress);
    }

    public void nextFuncAddress()
    {
        currentFuncAddress.addOffset(1);
    }

    public void previousFuncAddress()
    {
        currentFuncAddress.addOffset(-1);
    }

    // Funcs Stack
    public Func getCurrentFunc()
    {
        return funcsStack.getFunc(currentFuncAddress);
    }

    public FuncDataAddresses getCurrentFuncParameters()
    {
        return funcsStack.getFuncParameters(currentFuncAddress);
    }

    public FuncImbricationDataAddresses getCurrentFuncImbricationParameters()
    {
        return funcsStack.getFuncImbricationParameters(currentFuncAddress);
    }

    public int sizeFuncsStack()
    {
        return funcsStack.size();
    }

    public int sizeMemoryStack()
    {
        return memoryStack.size();
    }

    public boolean hasFinished()
    {
        return currentFuncAddress.getAddress() >= funcsStack.size();
    }

    public void runFromAndUntil(FuncAddress sourceAddress, FuncAddress targetAddress)
    {
        assert (sourceAddress.isBefore(targetAddress));

        setCurrentFuncAddress(sourceAddress);

        while (!isOnAddress(targetAddress) && !hasPassedAddress(targetAddress))
        {
            // Call the current pointed function
            callCurrentFunc();

            // Go to next function
            nextFuncAddress();
        }

        if (hasPassedAddress(targetAddress))
        {
            throw new HasPassedTargetAddressException();
        }
    }
}
