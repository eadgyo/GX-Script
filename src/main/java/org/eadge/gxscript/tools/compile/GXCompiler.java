package org.eadge.gxscript.tools.compile;

import org.eadge.gxscript.data.entity.base.GXEntity;
import org.eadge.gxscript.data.entity.script.InputScriptGXEntity;
import org.eadge.gxscript.data.entity.script.OutputScriptGXEntity;
import org.eadge.gxscript.data.entity.base.StartImbricationGXEntity;
import org.eadge.gxscript.data.imbrication.ImbricationNode;
import org.eadge.gxscript.data.imbrication.compile.ImbricationNodeC;
import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.OutputAddresses;
import org.eadge.gxscript.tools.Tools;

import java.util.*;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Compile code in linear code
 */
public class GXCompiler
{
    protected Set<GXEntity> entitiesWithError = new HashSet<>();

    /**
     * Hold output index of one GXEntity
     */
    private class OutputIndex
    {
        public int index;
    }

    /**
     * Holds all outputs index for one GXEntity
     */
    private class EntityOutputIndices
    {
        public OutputIndex outputsIndices[];
    }

    private class EntityToOutputIndices
    {
        public Map<GXEntity, EntityOutputIndices> entityToOutputIndices;
    }

    /**
     * Compute imbrication func and data objects necessary for compilation
     * @param rawGXScript GXScript to compile
     * @return ImbricationNode containing necessary data to compile
     */
    protected ImbricationNodeC generateCompilationObjects(RawGXScript rawGXScript)
    {
        return generateCompilationObjects(new DataAddress(0), new HashMap<GXEntity, OutputAddresses>(), rawGXScript);
    }

    /**
     * Compute imbrication func and data objects necessary for compilation
     * @param startAddress start data address
     * @param outputAddressesMap already allocated output addresses (inputs and outputs)
     * @param rawGXScript GXScript to compile
     * @return ImbricationNode containing necessary data to compile
     */
    protected ImbricationNodeC generateCompilationObjects(DataAddress startAddress, Map<GXEntity, OutputAddresses> outputAddressesMap, RawGXScript rawGXScript)
    {
        Collection<GXEntity> entities = rawGXScript.getEntities();

        // Create level 0 of imbrication, it's the root of the tree
        // All entities are in this level
        ImbricationNodeC root = createImbricationNode(startAddress, outputAddressesMap, entities);

        // Get starting entities
        Collection<GXEntity> startingEntities = rawGXScript.getStartingEntities();

        // Add them to the to be treated stack
        root.addAllToBeTreated(startingEntities);

        // While all entities have not been processed
        while (!root.hasFinishedProcess())
        {
            // While to be treated stack is not empty
            while (!root.isToBeTreatedEmpty())
            {
                // Get the first element
                GXEntity beingTreated = root.popToBeTreated();

                // Get his level of imbrication
                ImbricationNode highestImbricationNode = root.getHighestImbricationNode(beingTreated);

                // If the GXEntity is a start imbrication GXEntity
                if (beingTreated instanceof StartImbricationGXEntity)
                {
                    // Start a new imbrication node on top of the highest imbrication node
                    highestImbricationNode.startImbricationNode((StartImbricationGXEntity) beingTreated);
                }
                else
                {
                    // It's a normal GXEntity
                    highestImbricationNode.treatEntity(beingTreated);
                }
            }

            Tools.endImbrication(root);
        }
        return root;
    }

    public CompiledGXScript compile(RawGXScript rawGXScript)
    {
        // Get script's parameters
        Collection<InputScriptGXEntity> inputEntities = rawGXScript.getScriptInputEntities();
        Collection<OutputScriptGXEntity> outputEntities = rawGXScript.getScriptOutputEntities();

        // Generate parameters func
        ArrayList<Class> outputsScriptClasses = getScriptOutputsClasses(outputEntities);
        ArrayList<Class> inputsScriptClasses = getScriptInputClasses(inputEntities);

        // Reserve input and outputs entities
        Map<GXEntity, OutputAddresses> outputAddressesMap = new HashMap<>();

        // Alloc parameters input and retrieve the start address
        DataAddress startAddress = new DataAddress(0);

        // IMPORTANT: START WITH OUTPUTS
        allocScriptOutputs(outputEntities, outputAddressesMap);

        // Then alloc inputs
        allocScriptInputs(inputEntities, outputAddressesMap, startAddress);

        // Generate compilation objects
        ImbricationNodeC root = generateCompilationObjects(startAddress, outputAddressesMap, rawGXScript);

        // Don't forget to treat outputs entities at the end of the script
        treatOutputEntities(root, outputEntities);

        // Compile generated object
        return root.compile(inputsScriptClasses, outputsScriptClasses);
    }

    private void allocScriptInputs(Collection<InputScriptGXEntity> inputEntities,
                                   Map<GXEntity, OutputAddresses> outputAddressesMap,
                                   DataAddress currentAddress)
    {
        for (InputScriptGXEntity inputEntity : inputEntities)
        {
            OutputAddresses scriptInputAddresses = inputEntity.createScriptInputAddresses(currentAddress);
            outputAddressesMap.put(inputEntity, scriptInputAddresses);
        }
    }

    private void allocScriptOutputs(Collection<OutputScriptGXEntity> outputEntities,
                                    Map<GXEntity, OutputAddresses> outputAddressesMap)
    {
        DataAddress currentAddress = new DataAddress(0);
        for (OutputScriptGXEntity outputEntity : outputEntities)
        {
            OutputAddresses scriptOutputAddresses = outputEntity.createScriptOutputAddresses(currentAddress);
            outputAddressesMap.put(outputEntity, scriptOutputAddresses);
        }

        // Set to relative position
        int offset = -currentAddress.getAddress();
        for (OutputAddresses outputAddresses : outputAddressesMap.values())
        {
            outputAddresses.addOffset(offset);
        }
    }

    protected ImbricationNodeC createImbricationNode(Collection<GXEntity> entities)
    {
        return new ImbricationNodeC(entities);
    }

    protected ImbricationNodeC createImbricationNode(DataAddress startAddress, Map<GXEntity, OutputAddresses> outputAddressesMap, Collection<GXEntity> entities)
    {
        return new ImbricationNodeC(startAddress, outputAddressesMap, entities);
    }

    /**
     * Alloc data address for script input parameters and retrieve the corresponding start address
     * @param inputScriptGXEntities used parameter entities
     * @return inputScriptClasses store script's inputs
     */
    protected ArrayList<Class> getScriptInputClasses(Collection<InputScriptGXEntity> inputScriptGXEntities)
    {
        ArrayList<Class> inputScriptClasses = new ArrayList<>();

        for (InputScriptGXEntity gxEntity : inputScriptGXEntities)
        {
            inputScriptClasses.addAll(gxEntity.getScriptInputClasses());
        }

        return inputScriptClasses;
    }

    /**
     * Alloc data address for script input parameters and retrieve the corresponding start address
     * @param outputEntities used parameter entities
     * @return outputScriptClasses store script's outputs
     */
    protected ArrayList<Class> getScriptOutputsClasses(Collection<OutputScriptGXEntity> outputEntities)
    {
        ArrayList<Class> outputScriptClasses = new ArrayList<>();

        for (OutputScriptGXEntity gxEntity : outputEntities)
        {
            outputScriptClasses.addAll(gxEntity.getScriptOutputClasses());
        }

        return outputScriptClasses;
    }

    /**
     * Push output entities code at the end of the script
     * @param root used generated compiled object
     * @param outputEntities collection of output entities
     */
    protected void treatOutputEntities(ImbricationNodeC root, Collection<OutputScriptGXEntity> outputEntities)
    {
        for (OutputScriptGXEntity outputEntity : outputEntities)
        {
            root.treatOutputEntity(outputEntity);
        }
    }
}
