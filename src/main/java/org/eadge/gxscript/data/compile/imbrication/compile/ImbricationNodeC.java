package org.eadge.gxscript.data.compile.imbrication.compile;

import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.entity.model.script.OutputScriptGXEntity;
import org.eadge.gxscript.data.entity.model.base.StartImbricationGXEntity;
import org.eadge.gxscript.data.compile.imbrication.ImbricationNode;
import org.eadge.gxscript.data.compile.script.CompiledGXScript;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.script.address.*;

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
     * Indices of parameters of called script
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

    public ImbricationNodeC(DataAddress startAddress,
                            Map<GXEntity, OutputAddresses> outputAddressesMap,
                            Collection<GXEntity> allElements)
    {
        super(allElements);
        this.outputAddressesMap.putAll(outputAddressesMap);
        this.currentDataAddress.setAddress(startAddress);
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
        return new CompiledGXScript(new ArrayList<Class>(), new ArrayList<Class>(), calledFunctions, calledFunctionsParameters);
    }

    /**
     * Compile script with his parameters
     * @param inputsScriptClasses classes of script's inputs
     * @param outputsScriptClasses classes of script's outputs
     * @return compiled GXScript
     */
    public CompiledGXScript compile(Collection<Class> inputsScriptClasses, Collection<Class> outputsScriptClasses)
    {
        return new CompiledGXScript(inputsScriptClasses, outputsScriptClasses, calledFunctions, calledFunctionsParameters);
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
        // Get imbricated start script
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
            currentFuncAddress.selfAddOffset(imbricationNodeC.getCalledFunctions().size());
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
     * Transform script addresses relative at an higher level of imbrication, to absolute addresses in this
     * imbrication
     * UPDATE BEFORE INSERTING CALLED FUNCTIONS PARAMETERS
     *
     * @param nextAddedCalledFunctionsParameters addresses of script that will be added in this level of imbrication
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
     * Transform script addresses relative at an higher level of imbrication, to absolute addresses in this
     * imbrication
     * UPDATE BEFORE INSERTING CALLED FUNCTIONS PARAMETERS
     *
     * @param nextAddedCalledFunctionsParameters addresses of script that will be added in this level of imbrication
     */
    private void updateFuncsAddresses(FuncAddress[] nextAddedCalledFunctionsParameters)
    {
        // Create current func address to update added funcs
        FuncAddress currentFuncAddress = new FuncAddress(calledFunctions.size());

        for (FuncAddress calledFuncAddress : nextAddedCalledFunctionsParameters)
        {
            calledFuncAddress.selfAddOffset(currentFuncAddress);
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
    }

    public void treatEntity(GXEntity gxEntity)
    {
        super.treatEntity(gxEntity);

        // Get all script necessary output entities addresses
        Map<GXEntity, OutputAddresses> outputAddressesMap = getOutputAddresses(gxEntity.getAllInputEntities());

        // Add funcs and funcsData of gxEntity
        gxEntity.pushEntityCode(calledFunctions, calledFunctionsParameters, outputAddressesMap);

        // Create outputs and register in  map addresses
        OutputAddresses outputAddresses = gxEntity.createAndAllocOutputs(currentDataAddress);

        if (outputAddresses != null)
        {
            this.outputAddressesMap.put(gxEntity, outputAddresses);
        }
    }

    public void treatOutputEntity(OutputScriptGXEntity outputEntity)
    {
        super.treatEntity(outputEntity);

        // Get all script necessary output entities addresses
        Collection<GXEntity>           allInputEntities   = outputEntity.getAllInputEntities();
        allInputEntities.add(outputEntity);
        Map<GXEntity, OutputAddresses> outputAddressesMap = getOutputAddresses(allInputEntities);

        // Add funcs and funcsData of gxEntity
        outputEntity.pushOutputCopyCode(calledFunctions, calledFunctionsParameters, outputAddressesMap);

        // Create outputs and register in  map addresses
        OutputAddresses outputAddresses = outputEntity.createAndAllocOutputs(currentDataAddress);

        if (outputAddresses != null)
        {
            this.outputAddressesMap.put(outputEntity, outputAddresses);
        }
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
