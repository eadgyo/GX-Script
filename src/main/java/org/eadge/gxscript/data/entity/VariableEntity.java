package org.eadge.gxscript.data.entity;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Variable entity model
 */
public interface VariableEntity extends Entity
{
    //----------------------------
    //-------- Modified ----------
    //----------------------------

    /**
     * Create entity generating modifications
     *
     * @param outputIndex output modifiable index
     *
     * @return Entity modifying variable at outputIndex
     */
    ModifyingEntity createModificationEntity(int outputIndex);

    /**
     * Check if an output is modifiable
     *
     * @param outputIndex output index
     *
     * @return true if the output is modifiable, false otherwise
     */
    boolean isModifiableOutput(int outputIndex);

    void addModifiableOutputEntry(String outputName, Class cl);

    void addModifiableOutputEntry(int outputIndex, String outputName, Class cl);

    /**
     * Create modify link between this entity and another entity
     *
     * @param outputIndex       output index
     * @param entityOutputIndex output index of modifying entity
     * @param entity            linked entity
     */
    void linkAsModifiedOutput(int outputIndex, int entityOutputIndex, Entity entity);
}
