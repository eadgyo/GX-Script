package org.eadge.gxscript.data.imbrication.compile;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.imbrication.StartImbricationEntity;
import org.eadge.gxscript.data.imbrication.ImbricationNode;
import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.address.*;

import java.util.*;

/**
 * Created by eadgyo on 05/08/16.
 *
 * Imbrication node for compilation process
 */
public class ImbricationNodeC extends ImbricationNode
{
    /**
     * Called func
     */
    private ArrayList<Func> calledFunctions = new ArrayList<>();

    /**
     * Indices of parameters of called function
     */
    private ArrayList<FuncDataAddresses> calledFunctionsParameters = new ArrayList<>();

    /**
     * Saved Outputs
     */
    private Map<Entity, OutputAddresses> outputAddressesMap = new HashMap<>();

    /**
     * Current top stack data addresses
     */
    private DataAddress currentDataAddress = new DataAddress(0);

    /**
     * Save imbrication node to make imbrication code continuous (call and stack)
     */
    private Map<StartImbricationEntity, ArrayList<ImbricationNodeC>> savedParallelsImbricationsNodes = new HashMap<>();

    public ImbricationNodeC(int imbricationNode,
                            StartImbricationEntity startImbricationEntity,
                            Set<Entity> allElements)
    {
        super(imbricationNode, startImbricationEntity, allElements);
    }

    public ImbricationNodeC(Collection<Entity> allElements)
    {
        super(allElements);
    }

    public ImbricationNodeC(Set<Entity> allElements)
    {
        super(allElements);
    }

    public CompiledGXScript compile()
    {
        return new CompiledGXScript(calledFunctions, calledFunctionsParameters);
    }

    @Override
    public ImbricationNodeC pushImbrication(int imbricationIndex,
                                            StartImbricationEntity startImbricationEntity,
                                            Set<Entity> inImbricationEntities)
    {
        // Create imbrication
        ImbricationNodeC child = new ImbricationNodeC(imbricationIndex, startImbricationEntity, inImbricationEntities);

        // Push imbrication
        addChild(child);

        // Save imbrication node if to make parallels imbrication code continous
        saveImbricationNode(child);

        // Treat internal imbricated outputs
        child.treatImbricationOutputs();

        return child;
    }

    /**
     * Save imbrication node to make parallels imbrication node continuous
     *
     * @param child saved imbrication node
     */
    private void saveImbricationNode(ImbricationNodeC child)
    {
        StartImbricationEntity startImbricationEntity = child.getStartImbricationEntity();

        ArrayList<ImbricationNodeC> imbricationNodeCs;

        // If it's the first imbrication from this start imbrication entity
        if (child.getImbricationOutputIndex() == 0)
        {
            // Create a new Imbrication level
            imbricationNodeCs = new ArrayList<>();

            // Create save imbrication, to make parallels imbrication code continuous
            savedParallelsImbricationsNodes.put(startImbricationEntity, imbricationNodeCs);
        }
        else
        {
            // Just get existing one
            imbricationNodeCs = savedParallelsImbricationsNodes.get(startImbricationEntity);
        }

        // Add this imbrication to the saved imbrication list
        imbricationNodeCs.add(child);
    }

    @Override
    public void treatStartImbricationEntity(StartImbricationEntity startImbricationEntity)
    {
        super.treatStartImbricationEntity(startImbricationEntity);

        // Get saved imbrication nodes
        ArrayList<ImbricationNodeC> imbricationNodeCs = savedParallelsImbricationsNodes.remove(startImbricationEntity);
        assert (imbricationNodeCs != null);

        // --> Start Entity imbrication
        // Get imbricated start function
        FuncAddress[] funcAddresses = getFuncAddresses(imbricationNodeCs);

        // Add code (call functions and parameters) of start imbrication entity
        startImbricationEntity.pushStartImbricationCode(outputAddressesMap,
                                                        funcAddresses,
                                                        calledFunctions,
                                                        calledFunctionsParameters);

        // Update the address taking into account added functions
        updateFuncsAddresses(funcAddresses);

        // --> Imbricated entities
        // Push all saved code of all imbricated entities
        pushCode(imbricationNodeCs);

        // --> After imbrication entities
        // Create func imbricated entities
        treatNotImbricatedOutputs(startImbricationEntity);
    }

    @Override
    public void endImbrication(ImbricationNode child)
    {
        // Remove imbrication and all elements in imbrications
        removeImbricationAndAllElementsInImbrication(child);

        StartImbricationEntity startImbricationEntity = child.getStartImbricationEntity();

        // If all parallels imbrications have NOT been treated
        if (child.getImbricationOutputIndex() != startImbricationEntity.getNumberOfParallelsImbrications() - 1)
        {
            // Start next parallel imbrication
            startImbricationNode(startImbricationEntity, child.getImbricationOutputIndex() + 1);
        }
        else
        {
            // End start imbrication entity
            treatStartImbricationEntity(startImbricationEntity);
        }
    }

    /**
     * Get start imbrications nodes func addresses
     *
     * @param imbricationNodeCs collection of imbrications nodes
     *
     * @return address of imbrication
     */
    private FuncAddress[] getFuncAddresses(ArrayList<ImbricationNodeC> imbricationNodeCs)
    {
        FuncAddress funcsAddresses[] = new FuncAddress[imbricationNodeCs.size() + 1];

        // Create func address to estimate imbrications start and end func addresses
        FuncAddress currentFuncAddress = new FuncAddress(0);

        for (int i = 0; i < imbricationNodeCs.size(); i++)
        {
            ImbricationNodeC imbricationNodeC = imbricationNodeCs.get(i);

            // Save the funcs address
            funcsAddresses[i] = currentFuncAddress.clone();

            // Alloc functions
            currentFuncAddress.addOffset(imbricationNodeC.getCalledFunctions().size());
        }

        // Save the last address end of imbrication
        funcsAddresses[imbricationNodeCs.size()] = currentFuncAddress.clone();

        return funcsAddresses;
    }

    /**
     * Remove parallels saved imbrications nodes, and merge code with this level of imbrication
     *
     * @param imbricationNodeCs used collection of imbrication with code
     */
    private void pushCode(ArrayList<ImbricationNodeC> imbricationNodeCs)
    {
        // Push code in the right order
        for (ImbricationNodeC imbricationNodeC : imbricationNodeCs)
        {
            // Push code
            pushCode(imbricationNodeC);
        }
    }

    /**
     * Push code of this imbrication node at this level
     *
     * @param imbricationNodeC used imbrication node
     */
    private void pushCode(ImbricationNodeC imbricationNodeC)
    {
        // Update before inserting in called functions
        // Update functions addresses
        updateFuncsAddresses(imbricationNodeC.getCalledFunctionsParameters());

        // Update memory addresses
        updateMemoryAddresses(imbricationNodeC.getAllOutputAddresses());

        // Push corresponding func parameters
        calledFunctionsParameters.addAll(imbricationNodeC.getCalledFunctionsParameters());

        // Push called func
        calledFunctions.addAll(imbricationNodeC.getCalledFunctions());
    }

    /**
     * Update addresses location of data with this level of imbrication
     *
     * @param allOutputAddresses updated data addresses
     */
    private void updateMemoryAddresses(Collection<OutputAddresses> allOutputAddresses)
    {
        for (OutputAddresses outputAddresses : allOutputAddresses)
        {
            outputAddresses.addOffset(currentDataAddress);
        }
    }

    /**
     * Transform function addresses relative at an higher level of imbrication, to absolute addresses in this
     * imbrication
     * UPDATE BEFORE INSERTING CALLED FUNCTIONS PARAMETERS
     *
     * @param nextAddedCalledFunctionsParameters addresses of function that will be added in this level of imbrication
     */
    private void updateFuncsAddresses(Collection<FuncDataAddresses> nextAddedCalledFunctionsParameters)
    {
        // Create current func address to update added funcs
        FuncAddress currentFuncAddress = new FuncAddress(calledFunctions.size());

        for (FuncDataAddresses calledFunctionsParameters : nextAddedCalledFunctionsParameters)
        {
            if (calledFunctionsParameters instanceof FuncImbricationDataAddresses)
            {
                ((FuncImbricationDataAddresses) calledFunctionsParameters).addOffsetFuncs(currentFuncAddress);
            }
        }
    }

    /**
     * Transform function addresses relative at an higher level of imbrication, to absolute addresses in this
     * imbrication
     * UPDATE BEFORE INSERTING CALLED FUNCTIONS PARAMETERS
     *
     * @param nextAddedCalledFunctionsParameters addresses of function that will be added in this level of imbrication
     */
    private void updateFuncsAddresses(FuncAddress[] nextAddedCalledFunctionsParameters)
    {
        // Create current func address to update added funcs
        FuncAddress currentFuncAddress = new FuncAddress(calledFunctions.size());

        for (FuncAddress calledFuncAddress : nextAddedCalledFunctionsParameters)
        {
            calledFuncAddress.addOffset(currentFuncAddress);
        }
    }

    /**
     * MUST BE CALLED ON IMBRICATION NODE
     * Treat internal imbricated outputs on this imbricated level
     * For example, int used for FOR Loops
     */
    public void treatImbricationOutputs()
    {
        // Treat imbricated index start imbrication
        OutputAddresses outputAddresses = startImbricationEntity.createAndAllocImbricatedOutputs(imbricationOutputIndex,
                                                                                                 currentDataAddress);
        outputAddressesMap.put(startImbricationEntity, outputAddresses);
    }

    /**
     * MUST BE CALLED ON NOT IMBRICATED NODE
     * Treat NOT imbricated outputs and functions addresses of the start imbrication entity
     *
     * @param startImbricationEntity used start imbrication entity
     */
    public void treatNotImbricatedOutputs(StartImbricationEntity startImbricationEntity)
    {
        // Create NOT imbricated outputs and register addresses in map addresses
        OutputAddresses outputAddresses = startImbricationEntity.createAndAllocOutputs(currentDataAddress);
        outputAddressesMap.put(startImbricationEntity, outputAddresses);

        // Get all input necessary output entities addresses
        Map<Entity, OutputAddresses> outputAddressesMap = getOutputAddresses(startImbricationEntity
                                                                                     .getAllInputEntities());
    }

    public void treatEntity(Entity entity)
    {
        super.treatEntity(entity);

        // Get all input necessary output entities addresses
        Map<Entity, OutputAddresses> outputAddressesMap = getOutputAddresses(entity.getAllInputEntities());

        // Add funcs and funcsData of entity
        entity.pushEntityCode(calledFunctions, calledFunctionsParameters, outputAddressesMap);

        // Create outputs and register in  map addresses
        OutputAddresses outputAddresses = entity.createAndAllocOutputs(currentDataAddress);
        this.outputAddressesMap.put(entity, outputAddresses);
    }

    public ArrayList<Func> getCalledFunctions()
    {
        return calledFunctions;
    }

    public ArrayList<FuncDataAddresses> getCalledFunctionsParameters()
    {
        return calledFunctionsParameters;
    }

    public Collection<OutputAddresses> getAllOutputAddresses()
    {
        return outputAddressesMap.values();
    }

    public Map<Entity, OutputAddresses> getOutputAddresses(Collection<Entity> entities)
    {
        Map<Entity, OutputAddresses> allOutputAddresses = new HashMap<>();

        for (Entity entity : entities)
        {
            if (entity != null)
            {
                // Get the corresponding outputs
                OutputAddresses outputAddresses = getOutputAddresses(entity);

                assert (outputAddresses != null);

                allOutputAddresses.put(entity, outputAddresses);
            }
        }

        return allOutputAddresses;
    }

    public OutputAddresses getOutputAddresses(Entity entity)
    {
        // Search for this entity at this level
        OutputAddresses outputAddresses = outputAddressesMap.get(entity);

        // If entity not found at this level
        if (outputAddresses == null)
        {
            if (parent != null)
            {
                // search in lower level
                return ((ImbricationNodeC) parent).getOutputAddresses(entity);
            }
            return null;
        }
        else
        {
            return outputAddresses;
        }
    }
}
