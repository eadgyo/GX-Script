package org.eadge.gxscript.data.entity.model.def;

import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.entity.model.base.ModifyingGXEntity;
import org.eadge.gxscript.data.entity.model.base.VariableGXEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Variable GXEntity model
 * Variable have script and output as GXEntity, but also have modified lane
 */
public abstract class DefaultVariableGXEntity extends DefaultGXEntity implements VariableGXEntity
{
    /**
     * Holds output modifiable
     */
    private Set<Integer> indicesModifiableOutputs = new HashSet<>();

    public DefaultVariableGXEntity(String name)
    {
        super(name);
    }

    //----------------------------
    //-------- Modified ----------
    //----------------------------

    /**
     * Create GXEntity generating modifications
     * @param outputIndex output modifiable index
     * @return GXEntity modifying variable at outputIndex
     */
    public ModifyingGXEntity createModificationEntity(int outputIndex)
    {
        // Get output class
        Class outputClass = getOutputClass(outputIndex);

        // Create a default outputClass en return a default modifying GXEntity
        return new DefaultModifyingGXEntity("Modify " + outputClass.toString(), outputClass);
    }

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
    public void addOutputEntry(int outputIndex, String outputName, Class cl)
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
    public void removeOutputEntry(int outputIndex)
    {
        indicesModifiableOutputs.remove(outputIndex);
        super.removeOutputEntry(outputIndex);
    }

    /**
     * Create modify link between this GXEntity and another GXEntity
     * @param outputIndex output index
     * @param entityOutputIndex output index of modifying GXEntity
     * @param GXEntity linked GXEntity
     */
    public void linkAsModifiedOutput(int outputIndex, int entityOutputIndex, GXEntity GXEntity)
    {
        // Create modification GXEntity
        ModifyingGXEntity modificationEntity = createModificationEntity(outputIndex);

        assert (modificationEntity != null);

        // Link as output this GXEntity
        modificationEntity.linkAsInput(0, outputIndex, this);

        // Link as output modifying GXEntity
        modificationEntity.linkAsInput(1, entityOutputIndex, GXEntity);
    }

    @Override
    public Class getVariableClass()
    {
        return getOutputClass(0);
    }
}
