package org.eadge.gxscript.data.compile.program.memory;

import org.eadge.gxscript.data.compile.script.address.DataAddress;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Memory stack holdings shared objects
 */
public class MemoryStack extends ArrayList<Object>
{
    /**
     * Stack of saved level of memory
     */
    private Stack<Integer> memoryLevels = new Stack<>();


    /**
     * Store the address of the start of memory
     */
    private Stack<Integer> zeroLevels = new Stack<>();

    /**
     * Get the object at the data address
     * @param dataAddress data address of object
     * @return object at the data address
     */
    public Object get(DataAddress dataAddress)
    {
        return super.get(dataAddress.getAddress() + getZeroAddress());
    }

    /**
     * Replace one object at a data address by new one
     * @param dataAddress replace data address
     * @param object new object to replace the object at the data address
     */
    public void set(DataAddress dataAddress, Object object) { set(dataAddress.getAddress() + getZeroAddress(), object); }

    /**
     * Pop elements from memory to the reach the specified size
     * @param size size to reach
     */
    public void popTo(int size)
    {
        assert (size < this.size());

        removeRange(size, this.size());
    }

    /**
     * Remove the last element in memory
     */
    public void pop()
    {
        remove(this.size() - 1);
    }

    /**
     * Save current memory state
     */
    public void saveState()
    {
        memoryLevels.add(this.size());
    }

    /**
     * Save the current level address, and set as zero
     */
    public void pushLevel()
    {
        saveState();
        pushZeroAddress();
    }

    /**
     * Remove all objects from memory after the last level of memory, or all objects in memory if there are no pushed
     * save state
     */
    public void restoreState()
    {
        if (memoryLevels.size() == 0)
        {
            this.clear();
        }
        else
        {
            popTo(memoryLevels.pop());
        }
    }

    /**
     * Get the top memory stack address
     * I's the address of the next added object.
     *
     * @return current memory address
     */
    public DataAddress getCurrentAbsoluteDataAddress()
    {
        return new DataAddress(size());
    }

    /**
     * Set the zero address representing relative address
     * @param zeroAddress address offset from absolute memory 0
     */
    private void pushZeroAddress(int zeroAddress)
    {
        this.zeroLevels.add(zeroAddress);
    }

    /**
     * Restore zero address to last used zero address
     */
    private void popZeroAddress()
    {
        this.zeroLevels.pop();
    }

    /**
     * Save the current stack position as the zero address
     */
    private void pushZeroAddress()
    {
        pushZeroAddress(size());
    }

    private int getZeroAddress()
    {
        return zeroLevels.lastElement();
    }

    /**
     * Restore to last level address, and reset last used zero
     */
    public void popLevel()
    {
        restoreState();
        popZeroAddress();
    }

    /**
     * Get the number of pushed levels on memory
     * @return number of pushed levels
     */
    public int getNumberOfLevels() { return memoryLevels.size(); }

}
