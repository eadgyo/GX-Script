package org.eadge.gxscript.data.entity.base;

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
 * GXEntity can have one GXEntity per script and multiples entities per output
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
     * Get the class of the script object at the index
     *
     * @param index script index
     *
     * @return class of the script object at the index
     */
    public abstract Class getInputClass(int index);

    /**
     * Get all the classes of the script objects
     *
     * @return classes of the script objects
     */
    public abstract Collection<Class> getAllInputClasses();

    /**
     * Get the number of script objects
     *
     * @return number of script objects
     */
    public abstract int getNumberOfInputs();

    /**
     * Check if one GXEntity is contained at the script
     * @param inputIndex script index
     * @param GXEntity tested GXEntity
     * @return true if GXEntity is contained at script index, false otherwise
     */
    public abstract boolean inputContains(int inputIndex, GXEntity GXEntity);

    /**
     * Get the script GXEntity at the index or null if there are no entities
     *
     * @param index script index
     *
     * @return script GXEntity at the index or null if there are no entities
     */
    public abstract GXEntity getInputEntity(int index);

    /**
     * Check if script entry at index is used
     *
     * @param index script index
     *
     * @return true if the script entry is used, false otherwise
     */
    public abstract boolean isInputUsed(int index);

    /**
     * Get the index of the output linked to one GXEntity or -1 if it's not a output GXEntity
     *
     * @param inputIndex used script index
     *
     * @return index of the linked to output GXEntity, or -1 if GXEntity is not link on output
     */
    public abstract int getIndexOfOutputFromEntityOnInput(int inputIndex);

    /**
     * Get output class from the GXEntity on script
     *
     * @param inputIndex script index
     *
     * @return class linked on from script GXEntity
     */
    public abstract Class getOutputClassFromInputEntity(int inputIndex);

    /**
     * Get all the script entities
     *
     * @return all script entities
     */
    public abstract Collection<GXEntity> getAllInputEntities();

    /**
     * Check if GXEntity have necessary script
     *
     * @return true if GXEntity have enough script, false otherwise
     */
    public abstract boolean hasAllNeededInput();

    /**
     * Check if the script at the index is valid
     *
     * @param inputIndex script index
     *
     * @return true if the script is valid, false otherwise
     */
    public boolean isValidInput(int inputIndex);

    /**
     * Add link on script
     * DO NOT USE THIS ALONE
     * @param inputIndex output index
     * @param GXEntity added GXEntity
     * @param entityOutputIndex corresponding output index
     */
    public abstract void addLinkInput(int inputIndex, int entityOutputIndex, GXEntity GXEntity);

    /**
     * Remove one GXEntity link on script at the given index
     * DO NOT USE THIS ALONE
     * @param inputIndex script entry to remove GXEntity
     */
    public abstract void removeLinkInput(int inputIndex);

    /**
     * Modify the information on the linked index of one inputEntity
     * @param inputIndex script index
     * @param newOutputIndex updated output index
     */
    public abstract void changeIndexOfOutputFromEntityOnInput(int inputIndex, int newOutputIndex);

    /**
     * Create a link between this GXEntity considered as script and one other output GXEntity
     * @param inputIndex script index
     * @param entityOutput other GXEntity output index
     * @param GXEntity output GXEntity
     */
    public abstract void linkAsInput(int inputIndex, int entityOutput, GXEntity GXEntity);


    /**
     * Remove link on script at the given script index
     * @param inputIndex script index
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
     * Check if an script takes variable
     * @param inputIndex script index
     * @return true if script takes variable, false otherwise
     */
    public abstract boolean isVariableInput(int inputIndex);

    /**
     * Check if an script at the given index is necessary
     * @param inputIndex script index
     * @return true if the script at index is needed, false otherwise
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
    public abstract Collection<Class> getOutputClasses();

    /**
     * Get the number of output objects
     *
     * @return number of output objects
     */
    public abstract int getNumberOfOutputs();

    /**
     * Get the number output entities at the index
     *
     * @param outputIndex output index
     *
     * @return number of output entities at the index
     */
    public abstract int getNumberOfOutputEntities(int outputIndex);

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
     * Get the index of the script linked to one GXEntity or -1 if it's not a script GXEntity
     *
     * @param index  GXEntity output index
     * @param outputGXEntity linked to script GXEntity
     *
     * @return index of the linked to script GXEntity, or -1 if GXEntity is not link on script
     */
    public abstract int getIndexOfInputFromEntityOnOutput(int index, GXEntity outputGXEntity);

    /**
     * Get all the output entities
     *
     * @return output entities keeping separated by output lane
     */
    public abstract Collection<? extends Collection<GXEntity>> getAllOutputEntities();

    /**
     * Get number of script taking variables
     *
     * @return number of script taking variables
     */
    public abstract int getNumberOfVariableInput();

    /**
     * Get number of used script taking variables
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
     * @param newInputIndex updated script index
     */
    public abstract void changeIndexOfInputFromEntityOnOutput(int outputIndex, GXEntity outputGXEntity, int newInputIndex);

    /**
     * Add link on output
     * DO NOT USE THIS ALONE
     * @param outputIndex output index
     * @param inputEntityIndex corresponding script index
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
     * Create a link between this GXEntity considered as output and one other script GXEntity
     * @param outputIndex output index
     * @param entityInput other GXEntity script index
     * @param GXEntity script GXEntity
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
     * @param calledFunctions          list of called script
     * @param calledFunctionAddresses list of used called script data
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