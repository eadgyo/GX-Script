package org.eadge.gxscript.data.script;

import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.script.address.FuncImbricationDataAddresses;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Holds memory, script and current read function
 */
public class Program
{
    /**
     * Objects in memory shared with other functions
     */
    private MemoryStack memoryStack = new MemoryStack();

    /**
     * Current read function
     */
    private FuncAddress currentFuncAddress = new FuncAddress(0);

    /**
     * List of functions and their parameters addresses
     */
    private FuncsStack funcsStack;

    public Program(FuncsStack funcsStack)
    {
        this.funcsStack = funcsStack;
    }

    public Program(Func[] funcs, FuncDataAddresses[] funcDataAddresses)
    {
        this.funcsStack = new FuncsStack(funcs, funcDataAddresses);
    }

    /**
     * Call the current pointed function
     */
    public void callCurrentFunc()
    {
        Func currentFunc = getCurrentFunc();

        currentFunc.run(this);
    }

    // Memory Stack

    /**
     * Add an object in memory
     *
     * @param object added object
     */
    public void pushInMemory(Object object)
    {
        memoryStack.add(object);
    }

    /**
     * Remove the last object from memory
     */
    public void popObjectFromMemory()
    {
        memoryStack.pop();
    }

    /**
     * Remove all objects in memory
     */
    public void clearMemory()
    {
        memoryStack.clear();
    }

    /**
     * Get objects pointed by the read function parameters
     *
     * @return current parameters objects
     */
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

    /**
     * Get an object from its address in memory
     *
     * @param dataAddress address of object in memory
     *
     * @return pointed object
     */
    public Object loadObject(DataAddress dataAddress)
    {
        return memoryStack.get(dataAddress);
    }

    /**
     * Replace the object at the specified address by another object
     *
     * @param dataAddress address of the replaced object
     * @param object      replacing object
     */
    public void setObject(DataAddress dataAddress, Object object)
    {
        memoryStack.set(dataAddress, object);
    }

    /**
     * Save current memory level
     */
    public void pushMemoryLevel()
    {
        memoryStack.pushLevel();
    }

    /**
     * Remove all objects from memory after the last level of memory, or all objects in memory if there are no pushed
     * memory levels
     */
    public void popMemoryLevel()
    {
        memoryStack.popLevel();
    }

    // Current Func Address

    /**
     * Set the address of the current read function
     *
     * @param currentFuncAddress address of read function
     */
    public void setCurrentFuncAddress(FuncAddress currentFuncAddress)
    {
        this.currentFuncAddress.setAddress(currentFuncAddress);
    }

    /**
     * Check if a func address is after the current read address
     *
     * @param testedFuncAddress tested func address
     *
     * @return true if the func addres has been passed, false otherwise
     */
    public boolean hasPassedAddress(FuncAddress testedFuncAddress)
    {
        return currentFuncAddress.isAfter(testedFuncAddress);
    }

    /**
     * Check if a func address is the same as the current read func address
     *
     * @param testedFuncAddress tested func address
     *
     * @return true if the tested func address is the same as the current read func address, false otherwise
     */
    public boolean isOnAddress(FuncAddress testedFuncAddress)
    {
        return currentFuncAddress.equals(testedFuncAddress);
    }

    /**
     * Get the current read func address
     *
     * @return current read func address
     */
    public FuncAddress getCurrentFuncAddress()
    {
        return currentFuncAddress;
    }

    /**
     * Move the current func address by one offset
     *
     * @param offset amount of motion
     */
    public void addCurrentFuncAddressOffset(int offset)
    {
        currentFuncAddress.addOffset(offset);
    }

    /**
     * Move the current func address by one offset
     *
     * @param offset amount of motion
     */
    public void addCurrentFuncAddressOffset(FuncAddress offset)
    {
        currentFuncAddress.addOffset(offset);
    }

    /**
     * Move the current func address to the next func address
     */
    public void nextFuncAddress()
    {
        currentFuncAddress.addOffset(1);
    }

    /**
     * Move the current func address to the previous func address
     */
    public void previousFuncAddress()
    {
        currentFuncAddress.addOffset(-1);
    }

    // Funcs Stack

    /**
     * Get the func at the current func address
     *
     * @return func at the current func address
     */
    public Func getCurrentFunc()
    {
        return funcsStack.getFunc(currentFuncAddress);
    }

    /**
     * Get the func parameters at the current func address
     *
     * @return func parameters at the current func address
     */
    public FuncDataAddresses getCurrentFuncParameters()
    {
        return funcsStack.getFuncParameters(currentFuncAddress);
    }

    /**
     * Get the func parameters with func addresses for imbrication process at the current func address
     *
     * @return func parameters at the current func address
     */
    public FuncImbricationDataAddresses getCurrentFuncImbricationParameters()
    {
        return funcsStack.getFuncImbricationParameters(currentFuncAddress);
    }

    /**
     * Get the number of funcs in funcs stack
     *
     * @return number of funcs in funcs stack
     */
    public int sizeFuncsStack()
    {
        return funcsStack.size();
    }

    /**
     * Get the number of objects in memory
     *
     * @return number of objects in memory
     */
    public int sizeMemoryStack()
    {
        return memoryStack.size();
    }

    /**
     * Check if all funcs have been read
     *
     * @return true if all funcs have been read, false otherwise
     */
    public boolean hasFinished()
    {
        return currentFuncAddress.getAddress() >= funcsStack.size();
    }

    /**
     * Run the program from the source address, and stopping at the target address. The target address is not read.
     *
     * @param sourceAddress source address
     * @param targetAddress target address, not read
     */
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
            setCurrentFuncAddress(targetAddress);
        }
    }
}
