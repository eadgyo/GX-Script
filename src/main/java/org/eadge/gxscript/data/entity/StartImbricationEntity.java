package org.eadge.gxscript.data.entity;

import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.script.address.OutputAddresses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Model for imbrication blocks.
 */
public interface StartImbricationEntity extends Entity
{
    /**
     * Get the number of parallels imbrication
     *
     * @return number of parallels imbrication
     */
    int getNumberOfParallelsImbrications();

    /**
     * Get the imbrication of one output or -1 if output is not imbricated
     * @param index output index
     * @return imbrication of output or -1 if output is not imbricated
     */
    int getOutputImbrication(int index);

    /**
     * Check if one output is in imbrication process
     *
     * @param index output imbrication
     *
     * @return true if the index is pointing toward an output in imbrication, false otherwise
     */
    boolean isInImbrication(int index);

    /**
     * Get output entities that are in the imbrication of this entity
     *
     * @param index index of imbrication
     *
     * @return all output entities in imbrication of this entity
     */
    Collection<Entity> getImbricatedOutputs(int index);

    /**
     * Get output entities that are NOT in the imbrication of this entity
     *
     * @return all output entities NOT in imbrication of this entity
     */
    Collection<Entity> getNotImbricatedOutputs();

    /**
     * Create and alloc imbricated outputs
     *
     * @param imbricationOutputIndex index of imbrication
     * @param currentDataAddress current address on stack of data addresses
     *
     * @return created outputs addresses
     */
    OutputAddresses createAndAllocImbricatedOutputs(int imbricationOutputIndex,
                                                                    DataAddress currentDataAddress);

    /**
     * Default add funcs and funcs params
     *
     * @param addressesMap                 map to link entity to corresponding entity output addresses
     * @param imbricatedStartFuncAddresses addresses of imbricated addresses
     * @param calledFunctions              list of called function
     * @param calledFunctionsParameters    list of used called function data
     */
    void pushStartImbricationCode(Map<Entity, OutputAddresses> addressesMap,
                                  FuncAddress[] imbricatedStartFuncAddresses,
                                  ArrayList<Func> calledFunctions,
                                  ArrayList<FuncDataAddresses> calledFunctionsParameters);
}