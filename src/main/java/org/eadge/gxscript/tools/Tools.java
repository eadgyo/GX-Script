package org.eadge.gxscript.tools;

import org.eadge.gxscript.data.entity.InputScriptGXEntity;
import org.eadge.gxscript.data.entity.GXEntity;
import org.eadge.gxscript.data.entity.OutputScriptGXEntity;
import org.eadge.gxscript.data.entity.StartImbricationGXEntity;
import org.eadge.gxscript.data.imbrication.ImbricationNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Tools shared with GXEntity processes
 */
public class Tools
{
    /**
     * Get all starting entities from collection of entities. Start entities are those who have no entities linked in
     * script.
     *
     * @param entities collection of entities to be treated
     *
     * @return collection of starting entities
     */
    public static Collection<GXEntity> getStartingEntities(Collection<GXEntity> entities)
    {
        Set<GXEntity> startingEntities = new HashSet<>();

        for (GXEntity GXEntity : entities)
        {
            // If GXEntity has no linked GXEntity block at script
            if (!GXEntity.hasInputsUsed())
            {
                // This GXEntity is a starting GXEntity
                startingEntities.add(GXEntity);
            }
        }

        return startingEntities;
    }

    /**
     * Get all ending entities from collection of entities. Start entities are those who have no entities linked in
     * output.
     *
     * @param entities collection of entities to be treated
     *
     * @return collection of ending entities
     */
    public static Collection<GXEntity> getEndingEntities(Collection<GXEntity> entities)
    {
        Set<GXEntity> endingEntities = new HashSet<>();

        for (GXEntity GXEntity : entities)
        {
            // If GXEntity has no linked GXEntity block at output
            if (GXEntity.getNumberOfOutputs() == 0)
            {
                // This GXEntity is a ending GXEntity
                endingEntities.add(GXEntity);
            }
        }

        return endingEntities;
    }

    /**
     * Get all entities in the imbrication of this start imbrication GXEntity
     * @param startImbricationEntity used start imbrication GXEntity
     * @param index imbrication index
     * @return all imbricated entities
     */
    public static Set<GXEntity> getInImbricationEntities(StartImbricationGXEntity startImbricationEntity, int index)
    {
        // Create set of imbricated entities
        Set<GXEntity> imbricatedEntities = new HashSet<>();

        // For each imbricated output
        Collection<GXEntity> imbricatedOutputs = startImbricationEntity.getImbricatedOutputs(index);
        for (GXEntity imbricatedOutput : imbricatedOutputs)
        {
            // Add their outputs recursively
            addInImbricationEntities(imbricatedOutput, imbricatedEntities);
        }

        return imbricatedEntities;
    }

    private static void addInImbricationEntities(GXEntity GXEntity, Set<GXEntity> imbricatedEntities)
    {
        // Add this GXEntity to all imbricated entities
        imbricatedEntities.add(GXEntity);

        // Get outputs
        Collection<GXEntity> outputEntities = GXEntity.getAllOutputEntitiesCollection();

        // Add all not treated outputs
        for (GXEntity outputGXEntity : outputEntities)
        {
            // If the output is not already treated
            if (!imbricatedEntities.contains(outputGXEntity))
            {
                addInImbricationEntities(outputGXEntity, imbricatedEntities);
            }
        }
    }

    public static boolean endImbrication(ImbricationNode root)
    {
        boolean hasEnded = false;

        // Get all imbrication leaves
        ArrayList<ImbricationNode> leaves = root.getLeaves();

        // For all imbrications leaves
        for (ImbricationNode leave : leaves)
        {
            // If leaf have all entities processed at their level
            if (leave.hasFinishedProcess())
            {
                hasEnded = true;
                ImbricationNode parent = leave.getParent();
                if (parent != null)
                {
                    // End this imbrication
                    parent.endImbrication(leave);
                }
            }
        }
        return hasEnded;
    }

    /**
     * Check if classes are equals or script class derived from output class
     * @param outputClass output class
     * @param inputClass script class
     * @return true if classes are equals or script class derived from output class
     */
    public static boolean isEqualOrDerivedFrom(Class outputClass, Class inputClass)
    {
        //noinspection unchecked
        return inputClass.isAssignableFrom(outputClass) && (outputClass != Void.class || inputClass == Void.class);
    }

    /**
     * Retrieve all input GXScript entities from collection of entities
     * @param entities used collection of entities
     * @return collection of input GXScript entities
     */
    public static Collection<InputScriptGXEntity> getInputGXScriptEntities(Collection<GXEntity> entities)
    {
        ArrayList<InputScriptGXEntity> inputScriptGXEntities = new ArrayList<>();

        for (GXEntity entity : entities)
        {
            if (Tools.isEqualOrDerivedFrom(InputScriptGXEntity.class, entity.getClass()))
            {
                inputScriptGXEntities.add((InputScriptGXEntity) entity);
            }
        }
        return inputScriptGXEntities;
    }

    /**
     * Retrieve all output GXScript entities from collection of entities
     * @param entities used collection of entities
     * @return collection of output GXScript entities
     */
    public static Collection<? extends OutputScriptGXEntity> getOutputGXScriptEntities(Set<GXEntity> entities)
    {
        ArrayList<OutputScriptGXEntity> outputScriptGXEntities = new ArrayList<>();

        for (GXEntity entity : entities)
        {
            if (Tools.isEqualOrDerivedFrom(OutputScriptGXEntity.class, entity.getClass()))
            {
                outputScriptGXEntities.add((OutputScriptGXEntity) entity);
            }
        }
        return outputScriptGXEntities;
    }
}
