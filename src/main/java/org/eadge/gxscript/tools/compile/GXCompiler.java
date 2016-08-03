package org.eadge.gxscript.tools.compile;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.RawGXScript;

import java.util.*;

/**
 * Created by eadgyo on 02/08/16.
 */
public class GXCompiler
{
    private class FuncList
    {
        public Map<Func, Integer> funcToIndex = new HashMap<>();
        public ArrayList<Integer> funcs = new ArrayList<>();

        public int getOrCreateIndexFunc(Func func)
        {
            return -1;
        }
    }

    private class ScriptFunc
    {
        // Create stored func list
        // Can be used to convert func to integer and just using one object
        private FuncList funcList;
    }

    private class StackData
    {

    }


    private class OutputIndex
    {
        public int index;
    }

    /**
     * Holds all outputs index for one entity
     */
    private class EntityOutputIndices
    {
        public OutputIndex outputsIndices[];
    }

    private class EntityToOutputIndices
    {
        public Map<Entity, EntityOutputIndices> entityToOutputIndices;
    }

    private class ImbricationLevel
    {
        public Entity startingEntity = null;
    }

    private class Imbrications
    {
        public Deque<ImbricationLevel> imbricationsStack = new ArrayDeque<>();
    }

    public CompiledGXScript compile(RawGXScript rawGXScript)
    {

        // Create script called functions
        // Create stack data

        // Create list of imbrication

        // Create already treated set
        // Create treated stack

        // Get all starting entities
        // Add all starting entities to treatedStack

        // Function(List<Entity> alreadyTreated, StackTreated, List<Imbrications>, Imbrications)

        // Try to make it recursive

        // While all elements are not all treated
        while()
        {
            // While there are still elements to treat in stack
            while()
            {
                // Create data linked to function (data func)

                // For all input
                for ()
                {
                    // Get the corresponding OutputIndices
                    // Add this stack index to data func, for the corresponding input
                }

                // Create EntityOutputIndices
                // For all used outputs
                for ()
                {
                    // Add output to stack
                    // Store stack index to entityOutput indices

                    // For all linked outputs entities
                    for ()
                    {
                        // Add block treated stack which have all input in already treated stack
                        // If the block is imbricatedBlock creator
                        if ()
                        {
                            // If we are not in an imbrication
                            if ()
                            {
                                // Create imbrications
                            }

                            // Push one level in imbricationLevel
                        }
                    }
                }
                // Add block to script Block
                // Add data linked to func call
            }

            // -->
        }

        return null;
    }
}
