package org.eadge.gxscript.data.entity.base;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Variable GXEntity model
 */
public interface VariableGXEntity extends GXEntity
{
    //----------------------------
    //-------- Modified ----------
    //----------------------------

    /**
     * Create GXEntity generating modifications
     *
     * @param outputIndex output modifiable index
     *
     * @return GXEntity modifying variable at outputIndex
     */
    ModifyingGXEntity createModificationEntity(int outputIndex);

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
     * Create modify link between this GXEntity and another GXEntity
     *
     * @param outputIndex       output index
     * @param entityOutputIndex output index of modifying GXEntity
     * @param GXEntity            linked GXEntity
     */
    void linkAsModifiedOutput(int outputIndex, int entityOutputIndex, GXEntity GXEntity);

    /**
     * Get class of variable
     *
     * @return class
     */
    Class getVariableClass();
}
