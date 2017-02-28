package org.eadge.gxscript.data.entity;

import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.script.address.OutputAddresses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Parent GXEntity model
 *
 * GXEntity can have one GXEntity per function and multiples entities per output
 */
public interface GXEntity extends Cloneable
{
    /**
     * Used to reset class
     */
    public static final Class DEFAULT_CLASS = Object.class;

    public abstract Object clone();

    //----------------------------
    //---------- Input -----------
    //----------------------------

    /**
     * Get the class of the function object at the index
     *
     * @param index function index
     *
     * @return class of the function object at the index
     */
    public abstract Class getInputClass(int index);

    /**
     * Get all the classes of the function objects
     *
     * @return classes of the function objects
     */
    public abstract Collection<Class> getAllInputClasses();

    /**
     * Get the number of function objects
     *
     * @return number of function objects
     */
    public abstract int getNumberOfInputs();

    /**
     * Check if one GXEntity is contained at the function
     * @param inputIndex function index
     * @param GXEntity tested GXEntity
     * @return true if GXEntity is contained at function index, false otherwise
     */
    public abstract boolean inputContains(int inputIndex, GXEntity GXEntity);

    /**
     * Get the function GXEntity at the index or null if there are no entities
     *
     * @param index function index
     *
     * @return function GXEntity at the index or null if there are no entities
     */
    public abstract GXEntity getInputEntity(int index);

    /**
     * Check if function entry at index is used
     *
     * @param index function index
     *
     * @return true if the function entry is used, false otherwise
     */
    public abstract boolean isInputUsed(int index);

    /**
     * Get the index of the output linked to one GXEntity or -1 if it's not a output GXEntity
     *
     * @param inputIndex used function index
     *
     * @return index of the linked to output GXEntity, or -1 if GXEntity is not link on output
     */
    public abstract int getIndexOfOutputFromEntityOnInput(int inputIndex);

    /**
     * Get output class from the GXEntity on function
     *
     * @param inputIndex function index
     *
     * @return class linked on from function GXEntity
     */
    public abstract Class getOutputClassFromInputEntity(int inputIndex);

    /**
     * Get all the function entities
     *
     * @return all function entities
     */
    public abstract Collection<GXEntity> getAllInputEntities();

    /**
     * Check if GXEntity have necessary function
     *
     * @return true if GXEntity have enough function, false otherwise
     */
    public abstract boolean hasAllNeededInput();

    /**
     * Check if the function at the index is valid
     *
     * @param inputIndex function index
     *
     * @return true if the function is valid, false otherwise
     */
    public boolean isValidInput(int inputIndex);

    /**
     * Add link on function
     * DO NOT USE THIS ALONE
     * @param inputIndex output index
     * @param GXEntity added GXEntity
     * @param entityOutputIndex corresponding output index
     */
    public abstract void addLinkInput(int inputIndex, int entityOutputIndex, GXEntity GXEntity);

    /**
     * Remove one GXEntity link on function at the given index
     * DO NOT USE THIS ALONE
     * @param inputIndex function entry to remove GXEntity
     */
    public abstract void removeLinkInput(int inputIndex);

    /**
     * Modify the information on the linked index of one inputEntity
     * @param inputIndex function index
     * @param newOutputIndex updated output index
     */
    public abstract void changeIndexOfOutputFromEntityOnInput(int inputIndex, int newOutputIndex);

    /**
     * Create a link between this GXEntity considered as function and one other output GXEntity
     * @param inputIndex function index
     * @param entityOutput other GXEntity output index
     * @param GXEntity output GXEntity
     */
    public abstract void linkAsInput(int inputIndex, int entityOutput, GXEntity GXEntity);


    /**
     * Remove link on function at the given function index
     * @param inputIndex function index
     */
    public abstract void unlinkAsInput(int inputIndex);

    /**
     * Check if all the inputs of are valid
     *
     * @return true if inputs are valid, false otherwise
     */
    public boolean hasValidInput();

    /**
     * Get number of output returning variables
     *
     * @return number of outputs returning variables
     */
    public abstract int getNumberOfVariableOutput();

    /**
     * Check if an function takes variable
     * @param inputIndex function index
     * @return true if function takes variable, false otherwise
     */
    public abstract boolean isVariableInput(int inputIndex);

    /**
     * Check if an function at the given index is necessary
     * @param inputIndex function index
     * @return true if the function at index is needed, false otherwise
     */
    public abstract boolean isInputNeeded(int inputIndex);

    /**
     * Get all necessary inputs index
     * @return all necessary inputs index
     */
    public abstract Collection<Integer> getAllInputsNeeded();

    //----------------------------
    //----------- Output ---------
    //----------------------------

    /**
     * Get the class of the output object at the index
     *
     * @param index output index
     *
     * @return class of the output object at the index
     */
    public abstract Class getOutputClass(int index);

    /**
     * Get all the classes of the output objects
     *
     * @return classes of the output objects in collection
     */
    public abstract Collection<Class> getAllOutputClasses();

    /**
     * Get the number of output objects
     *
     * @return number of output objects
     */
    public abstract int getNumberOfOutputs();

    /**
     * Get the output entities at the index
     *
     * @param index output index
     *
     * @return output entities at the index
     */
    public abstract Collection<GXEntity> getOutputEntities(int index);

    /**
     * Check if output entry at index is used
     *
     * @param index output index
     *
     * @return true if the output entry is used, false otherwise
     */
    public abstract boolean isOutputUsed(int index);

    /**
     * Check if one GXEntity is contained at the output
     * @param outputIndex output index
     * @param GXEntity tested GXEntity
     * @return true if GXEntity is contained at output index, false otherwise
     */
    public abstract boolean outputContains(int outputIndex, GXEntity GXEntity);

    /**
     * Get the index of the function linked to one GXEntity or -1 if it's not a function GXEntity
     *
     * @param index  GXEntity output index
     * @param outputGXEntity linked to function GXEntity
     *
     * @return index of the linked to function GXEntity, or -1 if GXEntity is not link on function
     */
    public abstract int getIndexOfInputFromEntityOnOutput(int index, GXEntity outputGXEntity);

    /**
     * Get all the output entities
     *
     * @return output entities keeping separated by output lane
     */
    public abstract Collection<? extends Collection<GXEntity>> getAllOutputEntities();

    /**
     * Get number of function taking variables
     *
     * @return number of function taking variables
     */
    public abstract int getNumberOfVariableInput();

    /**
     * Get number of used function taking variables
     *
     * @return number of inputs taking variables
     */
    public abstract int getNumberOfUsedVariableInput();

    /**
     * Check if an output gives variable
     * @param outputIndex output index
     * @return true if output gives variable, false otherwise
     */
    public abstract boolean isVariableOutput(int outputIndex);

    /**
     * Remove one GXEntity link on output at the given index
     * DO NOT USE THIS ALONE
     * @param outputIndex output entry to remove GXEntity
     * @param GXEntity removed GXEntity
     */
    public abstract void removeLinkOutput(int outputIndex, GXEntity GXEntity);

    /**
     * Modify the information on the linked index of one outputGXEntity
     * @param outputIndex output index
     * @param outputGXEntity output GXEntity
     * @param newInputIndex updated function index
     */
    public abstract void changeIndexOfInputFromEntityOnOutput(int outputIndex, GXEntity outputGXEntity, int newInputIndex);

    /**
     * Add link on output
     * DO NOT USE THIS ALONE
     * @param outputIndex output index
     * @param inputEntityIndex corresponding function index
     * @param GXEntity added GXEntity
     */
    public abstract void addLinkOutput(int outputIndex, int inputEntityIndex, GXEntity GXEntity);

    /**
     * Get the func of the GXEntity
     *
     * @return used func
     */
    public abstract Func getFunc();

    /**
     * Create a link between this GXEntity considered as output and one other function GXEntity
     * @param outputIndex output index
     * @param entityInput other GXEntity function index
     * @param GXEntity function GXEntity
     */
    void linkAsOutput(int outputIndex, int entityInput, GXEntity GXEntity);
    /**
     * Remove link on output at the given output index
     * @param outputIndex output index
     * @param GXEntity removed link on output GXEntity
     */
    void unlinkAsOutput(int outputIndex, GXEntity GXEntity);

    /**
     * Default add funcs and funcs params
     *
     * @param calledFunctions          list of called function
     * @param calledFunctionAddresses list of used called function data
     * @param addressesMap                      map to link GXEntity to corresponding GXEntity output addresses
     */
    void pushEntityCode(ArrayList<Func> calledFunctions,
                        ArrayList<FuncDataAddresses> calledFunctionAddresses,
                        Map<GXEntity, OutputAddresses> addressesMap);

    /**
     * Create outputs and alloc outputs in stack
     * @param currentDataAddress current address on stack of data addresses
     * @return created output addresses
     */
    OutputAddresses createAndAllocOutputs(DataAddress currentDataAddress);

    /**
     * Create funcDataAddresses and link inputs to the corresponding outputs addresses
     * @param addressesMap map GXEntity to outputAddresses
     * @return created and linked func data addresses
     */
    FuncDataAddresses createAndLinkFuncDataAddresses(Map<GXEntity, OutputAddresses> addressesMap);

    /**
     * Get all the output entities
     *
     * @return output entities
     */
    public Collection<GXEntity> getAllOutputEntitiesCollection();

    /**
     * Check if the output at the index is valid
     *
     * @param index output index
     *
     * @return true if the output is valid, false otherwise
     */
    public boolean isValidOutput(int index);

    /**
     * Check if all the outputs of are valid
     *
     * @return true if outputs are valid, false otherwise
     */
    public boolean hasValidOutput();

    /**
     * Get the number of used inputs
     * @return number of used inputs
     */
    public int getNumberOfUsedInputs();

    /**
     * Check if one or more inputs are used
     * @return true if one or more inputs are used, false otherwise
     */
    public boolean hasInputsUsed();
}