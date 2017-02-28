package org.eadge.gxscript.tools.compile;

import org.eadge.gxscript.classic.entity.function.ParameterGXEntity;
import org.eadge.gxscript.data.entity.GXEntity;
import org.eadge.gxscript.data.entity.StartImbricationGXEntity;
import org.eadge.gxscript.data.imbrication.ImbricationNode;
import org.eadge.gxscript.data.imbrication.compile.ImbricationNodeC;
import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;
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
        return generateCompilationObjects(new DataAddress(0),
                                          new ArrayList<Func>(),
                                          new ArrayList<FuncDataAddresses>(),
                                          rawGXScript);
    }

    /**
     * Compute imbrication func and data objects necessary for compilation
     * @param startAddress start data address
     * @param GXScriptFuncParameters script parameters funcs
     * @param GXScriptFuncParametersAddresses script parameters addresses
     * @param rawGXScript GXScript to compile
     * @return ImbricationNode containing necessary data to compile
     */
    protected ImbricationNodeC generateCompilationObjects(DataAddress startAddress,
                                                          Collection<Func> GXScriptFuncParameters,
                                                          Collection<FuncDataAddresses> GXScriptFuncParametersAddresses,
                                                          RawGXScript rawGXScript)
    {
        Collection<GXEntity> entities = rawGXScript.getEntities();

        // Create level 0 of imbrication, it's the root of the tree
        // All entities are in this level
        ImbricationNodeC root = createImbricationNode(startAddress,
                                                      GXScriptFuncParameters,
                                                      GXScriptFuncParametersAddresses,
                                                      entities);

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
        Collection<GXEntity> parameterEntities = getParameterEntities(rawGXScript);

        // Generate parameters func
        ArrayList<Func> funcs = new ArrayList<>(parameterEntities.size());
        ArrayList<FuncDataAddresses>  funcDataAddresses = new ArrayList<>(parameterEntities.size());

        // Alloc parameters input and retrieve the start address
        DataAddress startAddress = allocParametersInputs(parameterEntities, funcs, funcDataAddresses);

        // Generate compilation objects
        ImbricationNodeC root = generateCompilationObjects(startAddress, funcs, funcDataAddresses, rawGXScript);

        int numberOfScriptParameters = funcDataAddresses.size();
        return root.compile(numberOfScriptParameters);
    }

    protected ImbricationNodeC createImbricationNode(Collection<GXEntity> entities)
    {
        return new ImbricationNodeC(entities);
    }

    protected ImbricationNodeC createImbricationNode(DataAddress startAddress,
                                                     Collection<Func> GXScriptFuncParameters,
                                                     Collection<FuncDataAddresses> GXScriptFuncParametersAddresses,
                                                     Collection<GXEntity> entities)
    {
        return new ImbricationNodeC(startAddress, GXScriptFuncParameters, GXScriptFuncParametersAddresses, entities);
    }

    protected Collection<GXEntity> getParameterEntities(RawGXScript rawGXScript)
    {
        // Parameters entities have no inputs
        // Get all starting entities with no inputs used
        Collection<GXEntity> startingEntities = rawGXScript.getStartingEntities();

        // Init parameter entities
        Collection<GXEntity> parameterEntities = new HashSet<>();

        // Search for parameters entities
        for (GXEntity GXEntity : startingEntities)
        {
            if (GXEntity instanceof ParameterGXEntity)
            {
                parameterEntities.add(GXEntity);
            }
        }

        return parameterEntities;
    }

    /**
     * Alloc data address for script input parameters and retrieve the corresponding start address
     * @param parameterEntities used parameter entities
     * @param funcs funcs array to be filled with parameter entities func
     * @param funcDataAddresses funcs array to be filled with parameter entities func data addresses
     * @return start address
     */
    protected DataAddress allocParametersInputs(Collection<GXEntity> parameterEntities,
                                                  ArrayList<Func> funcs,
                                                  ArrayList<FuncDataAddresses>  funcDataAddresses)
    {
        DataAddress startAddress = new DataAddress();

        int i = 0;
        for (Iterator<GXEntity> it = parameterEntities.iterator(); it.hasNext(); i++)
        {
            GXEntity GXEntity = it.next();
            funcs.set(i, GXEntity.getFunc());
            funcDataAddresses.set(i, ((ParameterGXEntity) GXEntity).allocParameterAddresses(startAddress));
        }

        return startAddress;
    }
}
