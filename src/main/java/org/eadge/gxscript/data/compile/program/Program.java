package org.eadge.gxscript.data.compile.program;

import org.eadge.gxscript.data.compile.script.address.DataAddress;
import org.eadge.gxscript.data.compile.script.address.FuncAddress;
import org.eadge.gxscript.data.compile.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.compile.script.address.FuncImbricationDataAddresses;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.func.FuncsStack;
import org.eadge.gxscript.data.compile.program.memory.MemoryStack;

import java.util.Stack;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Holds memory, script and current read script
 */
public class Program
{
    /**
     * Objects in memory shared with other functions
     */
    private MemoryStack memoryStack = new MemoryStack();

    /**
     * Current read script
     */
    private FuncAddress currentFuncAddress = new FuncAddress(0);

    /**
     * List of functions and their parameters addresses
     */
    private FuncsStack funcsStack;

    /**
     * Store funcs levels
     */
    private Stack<Integer> funcsLevels = new Stack<>();

    public Program(FuncsStack funcsStack)
    {
        this.funcsStack = funcsStack;
        memoryStack.pushLevel();
        funcsLevels.add(0);
    }

    public Program(Func[] funcs, FuncDataAddresses[] funcDataAddresses)
    {
        this.funcsStack = new FuncsStack(funcs, funcDataAddresses);
        memoryStack.pushLevel();
        funcsLevels.add(0);
    }

    /**
     * Call the current pointed script
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
     * Get objects pointed by the read script parameters
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
     * Get current data address at the top of memory stack.
     * It's the address of the next added object.
     *
     * @return current data address
     */
    public DataAddress getCurrentDataAddress()
    {
        return memoryStack.getCurrentAbsoluteDataAddress();
    }

    /**
     * Get current data address at the top of memory stack.
     * It's the address of the next added object.
     *
     * @return current data address
     */
    public DataAddress getLastSavedDataAddress()
    {
        return memoryStack.getLastMemoryLevel();
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
     * Save current memory state
     */
    public void saveMemoryState()
    {
        memoryStack.saveState();
    }

    /**
     * Remove all objects from memory after the last level of memory, or all objects in memory if there are no pushed
     * memory state
     */
    public void restoreMemoryState()
    {
        memoryStack.restoreState();
    }

    /**
     * Get the number of pushed levels on memory
     * @return number of pushed levels
     */
    public int getNumberOfLevels()
    {
        return memoryStack.getNumberOfLevels();
    }

    /**
     * Save the current level address, and set as zero
     */
    public void pushLevel()
    {
        memoryStack.pushLevel();
        pushFuncLevel();
    }

    /**
     * Restore to last level address, and reset last used zero
     */
    public void popLevel()
    {
        memoryStack.popLevel();
    }

    private void pushFuncLevel()
    {
        funcsLevels.add(getCurrentAbsoluteFuncAddress().getAddress() + 1);
        currentFuncAddress.setAddress(0);
    }

    private void popFuncLevel()
    {
        currentFuncAddress.setAddress(funcsLevels.pop());
    }

    private int getFuncOffset()
    {
        return funcsLevels.lastElement();
    }

    // Current Func Address

    /**
     * Set the address of the current read script
     *
     * @param currentFuncAddress address of read script
     */
    public void setCurrentFuncAddress(FuncAddress currentFuncAddress)
    {
        this.currentFuncAddress.setAddress(currentFuncAddress);
    }

    /**
     * Set the address of the next read script
     *
     * @param nextReadAddress address of read script
     */
    public void setNextFuncAddress(FuncAddress nextReadAddress)
    {
        this.currentFuncAddress.setAddress(nextReadAddress.previous());
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
        return getCurrentFuncAddress().isAfter(testedFuncAddress);
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
        return getCurrentFuncAddress().equals(testedFuncAddress);
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
     * Get the current read absolute func address
     *
     * @return current read absolute func address
     */
    public FuncAddress getCurrentAbsoluteFuncAddress()
    {
        return currentFuncAddress.addOffset(getFuncOffset());
    }

    /**
     * Move the current func address by one offset
     *
     * @param offset amount of motion
     */
    public void addCurrentFuncAddressOffset(int offset)
    {
        currentFuncAddress.selfAddOffset(offset);
    }

    /**
     * Move the current func address by one offset
     *
     * @param offset amount of motion
     */
    public void addCurrentFuncAddressOffset(FuncAddress offset)
    {
        currentFuncAddress.selfAddOffset(offset);
    }

    /**
     * Move the current func address to the next func address
     */
    public void nextFuncAddress()
    {
        currentFuncAddress.selfAddOffset(1);
    }

    /**
     * Move the current func address to the previous func address
     */
    public void previousFuncAddress()
    {
        currentFuncAddress.selfAddOffset(-1);
    }

    // Funcs Stack

    /**
     * Get the func at the current func address
     *
     * @return func at the current func address
     */
    public Func getCurrentFunc()
    {
        return funcsStack.getFunc(getCurrentAbsoluteFuncAddress());
    }

    /**
     * Get the func parameters at the current func address
     *
     * @return func parameters at the current func address
     */
    public FuncDataAddresses getCurrentFuncParameters()
    {
        return funcsStack.getFuncParameters(getCurrentAbsoluteFuncAddress());
    }

    /**
     * Get the func parameters with func addresses for imbrication process at the current func address
     *
     * @return func parameters at the current func address
     */
    public FuncImbricationDataAddresses getCurrentFuncImbricationParameters()
    {
        return funcsStack.getFuncImbricationParameters(getCurrentAbsoluteFuncAddress());
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
        return getCurrentAbsoluteFuncAddress().getAddress() >= funcsStack.size();
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
            // Call the current pointed script
            callCurrentFunc();

            // Go to next script
            nextFuncAddress();
        }

        if (hasPassedAddress(targetAddress))
        {
            setCurrentFuncAddress(targetAddress);
        }
    }

    /**
     * Run the program in a imbrication level, from the sourcea address, and stopping at the target address. At the end,
     * restore the savedBockAddress
     *
     * @param sourceAddress    source address
     * @param targetAddress    target address, not read
     * @param savedBlockAddress restored address at the end
     */
    public void runImbrication(FuncAddress sourceAddress, FuncAddress targetAddress, FuncAddress savedBlockAddress)
    {
        // Save the current state of memory
        saveMemoryState();

        // Call functions
        runFromAndUntil(sourceAddress, targetAddress);

        // Remove added memory
        restoreMemoryState();

        // Reset to
        setCurrentFuncAddress(savedBlockAddress);
    }

    /**
     * Reserve space on memory
     * @param numberOfReservedSlots number of reserved slots
     */
    public void reserve(int numberOfReservedSlots)
    {
        for (int allocIndex = 0; allocIndex < numberOfReservedSlots; allocIndex++)
        {
            pushInMemory(null);
        }
    }
}
