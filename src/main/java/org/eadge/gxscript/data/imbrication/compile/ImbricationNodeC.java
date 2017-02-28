package org.eadge.gxscript.data.imbrication.compile;

import org.eadge.gxscript.data.entity.GXEntity;
import org.eadge.gxscript.data.entity.StartImbricationGXEntity;
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
    protected ArrayList<Func> calledFunctions = new ArrayList<>();

    /**
     * Indices of parameters of called function
     */
    protected ArrayList<FuncDataAddresses> calledFunctionsParameters = new ArrayList<>();

    /**
     * Saved Outputs addresses at this exact level of imbrication
     */
    protected Map<GXEntity, OutputAddresses> outputAddressesMap = new HashMap<>();

    /**
     * All Saved Outputs addresses at HIGHER level of imbrication
     * Used to update their memory addresses
     */
    private ArrayList<OutputAddresses> savedOutputAddresses = new ArrayList<>();

    /**
     * Current top stack data addresses
     */
    protected DataAddress currentDataAddress = new DataAddress(0);

    /**
     * Save imbrication node to make imbrication code continuous (call and stack)
     */
    protected Map<StartImbricationGXEntity, ArrayList<ImbricationNodeC>> savedParallelsImbricationsNodes = new HashMap<>();

    public ImbricationNodeC(int imbricationNode,
                            StartImbricationGXEntity startImbricationEntity,
                            Set<GXEntity> allElements)
    {
        super(imbricationNode, startImbricationEntity, allElements);
    }

    public ImbricationNodeC(Collection<GXEntity> allElements)
    {
        super(allElements);
    }

    public ImbricationNodeC(DataAddress dataAddress,
                            Collection<Func> GXScriptFuncParameters,
                            Collection<FuncDataAddresses> GXScriptFuncParametersAddresses,
                            Collection<GXEntity> allElements)
    {
        super(allElements);
        this.currentDataAddress.setAddress(dataAddress);
        this.calledFunctions.addAll(GXScriptFuncParameters);
        this.calledFunctionsParameters.addAll(GXScriptFuncParametersAddresses);
    }

    public ImbricationNodeC(Set<GXEntity> allElements)
    {
        super(allElements);
    }

    /**
     * Compile script with no parameters
     * @return compiled GXScript
     */
    public CompiledGXScript compile()
    {
        return new CompiledGXScript(0, calledFunctions, calledFunctionsParameters);
    }

    /**
     * Compile script with his parameters
     * @param numberOfScriptParameters number of parameters used for the script
     * @return compiled GXScript
     */
    public CompiledGXScript compile(int numberOfScriptParameters)
    {
        return new CompiledGXScript(numberOfScriptParameters, calledFunctions, calledFunctionsParameters);
    }

    @Override
    public ImbricationNodeC pushImbrication(int imbricationIndex,
                                            StartImbricationGXEntity startImbricationEntity,
                                            Set<GXEntity> inImbricationEntities)
    {
        // Create imbrication
        ImbricationNodeC child = createImbrication(imbricationIndex, startImbricationEntity, inImbricationEntities);

        // Push imbrication
        addChild(child);

        // Save imbrication node if to make parallels imbrication code continous
        saveImbricationNode(child);

        // Treat internal imbricated outputs
        child.treatImbricationOutputs();

        return child;
    }

    @Override
    protected ImbricationNodeC createImbrication(int imbricationIndex,
                                                 StartImbricationGXEntity startImbricationEntity,
                                                 Set<GXEntity> inImbricationEntities)
    {
        return new ImbricationNodeC(imbricationIndex, startImbricationEntity, inImbricationEntities);
    }

    /**
     * Save imbrication node to make parallels imbrication node continuous
     *
     * @param child saved imbrication node
     */
    protected void saveImbricationNode(ImbricationNodeC child)
    {
        StartImbricationGXEntity startImbricationEntity = child.getStartImbricationEntity();

        ArrayList<ImbricationNodeC> imbricationNodeCs;

        // If it's the first imbrication from this start imbrication GXEntity
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
    public void treatStartImbricationEntity(StartImbricationGXEntity startImbricationEntity)
    {
        super.treatStartImbricationEntity(startImbricationEntity);

        // Get saved imbrication nodes
        ArrayList<ImbricationNodeC> imbricationNodes = savedParallelsImbricationsNodes.remove(startImbricationEntity);
        assert (imbricationNodes != null);

        // --> Start GXEntity imbrication
        // Get imbricated start function
        FuncAddress[] funcAddresses = getFuncAddresses(imbricationNodes);

        // Add code (call functions and parameters) of start imbrication GXEntity
        startImbricationEntity.pushStartImbricationCode(outputAddressesMap,
                                                        funcAddresses,
                                                        calledFunctions,
                                                        calledFunctionsParameters);

        // Update the address taking into account added functions
        updateFuncsAddresses(funcAddresses);

        // --> Imbricated entities
        // Push all saved code of all imbricated entities
        updateAndPushCode(imbricationNodes);

        // --> After imbrication entities
        // Create func imbricated entities
        treatNotImbricatedOutputs(startImbricationEntity);
    }

    @Override
    public void endImbrication(ImbricationNode child)
    {
        // Remove imbrication and all elements in imbrications
        removeImbricationAndAllElementsInImbrication(child);

        StartImbricationGXEntity startImbricationEntity = child.getStartImbricationEntity();

        // If all parallels imbrications have NOT been treated
        if (child.getImbricationOutputIndex() != startImbricationEntity.getNumberOfParallelsImbrications() - 1)
        {
            // Start next parallel imbrication
            startImbricationNode(startImbricationEntity, child.getImbricationOutputIndex() + 1);
        }
        else
        {
            // End start imbrication GXEntity
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
    protected FuncAddress[] getFuncAddresses(ArrayList<ImbricationNodeC> imbricationNodeCs)
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
    protected void updateAndPushCode(Collection<ImbricationNodeC> imbricationNodeCs)
    {
        // Push code in the right order
        for (ImbricationNodeC imbricationNodeC : imbricationNodeCs)
        {
            // Push code
            updateAndPushCode(imbricationNodeC);
        }
    }

    /**
     * Push code of this imbrication node at this level
     *
     * @param imbricationNodeC used imbrication node
     */
    protected void updateAndPushCode(ImbricationNodeC imbricationNodeC)
    {
        // Update functions addresses of this imbrication node
        updateFuncsAddresses(imbricationNodeC.getCalledFunctionsParameters());

        // Update memory addresses of this imbrication node
        updateMemoryAddresses(imbricationNodeC.getAllOutputAddresses());
        updateMemoryAddresses(imbricationNodeC.getAllSavedOutputAddresses());

        // Push corresponding func parameters
        calledFunctionsParameters.addAll(imbricationNodeC.getCalledFunctionsParameters());

        // Push called func
        calledFunctions.addAll(imbricationNodeC.getCalledFunctions());

        // Save output addresses
        savedOutputAddresses.addAll(imbricationNodeC.getAllOutputAddresses());
        savedOutputAddresses.addAll(imbricationNodeC.getAllSavedOutputAddresses());
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
    private void treatImbricationOutputs()
    {
        // Treat imbricated index start imbrication
        OutputAddresses outputAddresses = startImbricationEntity.createAndAllocImbricatedOutputs(imbricationOutputIndex,
                                                                                                 currentDataAddress);
        outputAddressesMap.put(startImbricationEntity, outputAddresses);
    }

    /**
     * MUST BE CALLED ON NOT IMBRICATED NODE
     * Treat NOT imbricated outputs and functions addresses of the start imbrication GXEntity
     *
     * @param startImbricationEntity used start imbrication GXEntity
     */
    private void treatNotImbricatedOutputs(StartImbricationGXEntity startImbricationEntity)
    {
        // Create NOT imbricated outputs and register addresses in map addresses
        OutputAddresses outputAddresses = startImbricationEntity.createAndAllocOutputs(currentDataAddress);
        outputAddressesMap.put(startImbricationEntity, outputAddresses);

        // Get all function necessary output entities addresses
        Map<GXEntity, OutputAddresses> outputAddressesMap = getOutputAddresses(startImbricationEntity
                                                                                     .getAllInputEntities());
    }

    public void treatEntity(GXEntity GXEntity)
    {
        super.treatEntity(GXEntity);

        // Get all function necessary output entities addresses
        Map<GXEntity, OutputAddresses> outputAddressesMap = getOutputAddresses(GXEntity.getAllInputEntities());

        // Add funcs and funcsData of GXEntity
        GXEntity.pushEntityCode(calledFunctions, calledFunctionsParameters, outputAddressesMap);

        // Create outputs and register in  map addresses
        OutputAddresses outputAddresses = GXEntity.createAndAllocOutputs(currentDataAddress);
        this.outputAddressesMap.put(GXEntity, outputAddresses);
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

    public Collection<OutputAddresses> getAllSavedOutputAddresses()
    {
        return savedOutputAddresses;
    }

    public Map<GXEntity, OutputAddresses> getOutputAddresses(Collection<GXEntity> entities)
    {
        Map<GXEntity, OutputAddresses> allOutputAddresses = new HashMap<>();

        for (GXEntity GXEntity : entities)
        {
            if (GXEntity != null)
            {
                // Get the corresponding outputs
                OutputAddresses outputAddresses = getOutputAddresses(GXEntity);

                assert (outputAddresses != null);

                allOutputAddresses.put(GXEntity, outputAddresses);
            }
        }

        return allOutputAddresses;
    }

    public OutputAddresses getOutputAddresses(GXEntity GXEntity)
    {
        // Search for this GXEntity at this level
        OutputAddresses outputAddresses = outputAddressesMap.get(GXEntity);

        // If GXEntity not found at this level
        if (outputAddresses == null)
        {
            if (parent != null)
            {
                // search in lower level
                return ((ImbricationNodeC) parent).getOutputAddresses(GXEntity);
            }
            return null;
        }
        else
        {
            return outputAddresses;
        }
    }
}
