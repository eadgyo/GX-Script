package org.eadge.gxscript.data.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Variable entity model
 * Variable have input and output as entity, but also have modified lane
 */
public abstract class DefaultVariableEntity extends DefaultEntity implements VariableEntity
{
    /**
     * Holds output modifiable
     */
    private Set<Integer> indicesModifiableOutputs = new HashSet<>();

    public DefaultVariableEntity(String name)
    {
        super(name);
    }

    public DefaultVariableEntity()
    {
    }

    //----------------------------
    //-------- Modified ----------
    //----------------------------

    /**
     * Create entity generating modifications
     * @param outputIndex output modifiable index
     * @return Entity modifying variable at outputIndex
     */
    public abstract ModifyingEntity createModificationEntity(int outputIndex);

    /**
     * Check if an output is modifiable
     * @param outputIndex output index
     * @return true if the output is modifiable, false otherwise
     */
    public boolean isModifiableOutput(int outputIndex)
    {
        return indicesModifiableOutputs.contains(outputIndex);
    }

    @Override
    protected void addOffsetOutput(int startOutputIndex, int offset)
    {
        for (int outputIndex = startOutputIndex; outputIndex < getNumberOfOutputs(); outputIndex++)
        {
            int updatedOutput = outputIndex + offset;

            // If the output is a modifiable entry
            if (isModifiableOutput(outputIndex))
            {
                indicesModifiableOutputs.remove(outputIndex);
                indicesModifiableOutputs.add(updatedOutput);
            }
        }

        super.addOffsetOutput(startOutputIndex, offset);
    }

    @Override
    protected void addOutputEntry(int outputIndex, String outputName, Class cl)
    {
        super.addOutputEntry(outputIndex, outputName, cl);

        // If the output is a variable
        if (isVariableOutput(outputIndex))
        {
            // Consider it as a modifiable output
            indicesModifiableOutputs.add(outputIndex);
        }
    }

    public void addModifiableOutputEntry(String outputName, Class cl)
    {
        addModifiableOutputEntry(getNumberOfOutputs(), outputName, cl);
    }

    public void addModifiableOutputEntry(int outputIndex, String outputName, Class cl)
    {
        super.addOutputEntry(outputIndex, outputName, cl);

        indicesModifiableOutputs.add(outputIndex);
    }

    @Override
    protected void removeOutputEntry(int outputIndex)
    {
        indicesModifiableOutputs.remove(outputIndex);
        super.removeOutputEntry(outputIndex);
    }

    /**
     * Create modify link between this entity and another entity
     * @param outputIndex output index
     * @param entityOutputIndex output index of modifying entity
     * @param entity linked entity
     */
    public void linkAsModifiedOutput(int outputIndex, int entityOutputIndex, Entity entity)
    {
        // Create modification entity
        ModifyingEntity modificationEntity = createModificationEntity(outputIndex);

        assert (modificationEntity != null);

        // Link as output this entity
        modificationEntity.linkAsInput(0, outputIndex, this);

        // Link as output modifying entity
        modificationEntity.linkAsInput(1, entityOutputIndex, entity);
    }
}
