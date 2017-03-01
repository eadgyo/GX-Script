package org.eadge.gxscript.data.script;

import org.eadge.gxscript.data.script.address.DataAddress;

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
     * Get the object at the data address
     * @param dataAddress data address of object
     * @return object at the data address
     */
    public Object get(DataAddress dataAddress)
    {
        return super.get(dataAddress.getAddress());
    }

    /**
     * Replace one object at a data address by new one
     * @param dataAddress replace data address
     * @param object new object to replace the object at the data address
     */
    public void set(DataAddress dataAddress, Object object) { set(dataAddress.getAddress(), object); }

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
     * Save current memory level
     */
    public void pushLevel()
    {
        memoryLevels.add(this.size());
    }

    /**
     * Remove all objects from memory after the last level of memory, or all objects in memory if there are no pushed
     * memory levels
     */
    public void popLevel()
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
    public DataAddress getCurrentDataAddress()
    {
        return new DataAddress(size());
    }

    /**
     * Get the number of pushed levels on memory
     * @return number of pushed levels
     */
    public int getNumberOfLevels() { return memoryLevels.size(); }

    /**
     * Get the address of the last pushed levels
     * @return address of the last pushed levels
     */
    public DataAddress getLastPushedLevelAddress()
    {
        return getPushedLevelAddress(getNumberOfLevels() - 1);
    }

    /**
     * Get the data address of the pushed level
     * @param levelIndex used pushed level index
     * @return data address of the pushed level
     */
    public DataAddress getPushedLevelAddress(int levelIndex)
    {
        return new DataAddress(memoryLevels.get(levelIndex));
    }

}
