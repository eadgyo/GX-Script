package org.eadge.gxscript.tools.compile;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.imbrication.StartImbricationEntity;
import org.eadge.gxscript.data.imbrication.ImbricationNode;
import org.eadge.gxscript.data.imbrication.compile.ImbricationNodeC;
import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.tools.Tools;

import java.util.*;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Compile code in linear code
 */
public class GXCompiler
{
    protected Set<Entity> entitiesWithError = new HashSet<>();

    /**
     * Hold output index of one entity
     */
    private class OutputIndex
    {
        public int index;
    }

    /**
     * Holds all outputs index for one entity
     */
    private class EntityOutputIndices
    {
        public OutputIndex outputsIndices[];
    }

    private class EntityToOutputIndices
    {
        public Map<Entity, EntityOutputIndices> entityToOutputIndices;
    }


    public CompiledGXScript compile(RawGXScript rawGXScript)
    {
        Collection<Entity> entities = rawGXScript.getEntities();

        // Create level 0 of imbrication, it's the root of the tree
        // All entities are in this level
        ImbricationNodeC root = new ImbricationNodeC(entities);

        // Get starting entities
        Collection<Entity> startingEntities = rawGXScript.getStartingEntities();

        // Add them to the to be treated stack
        root.addAllToBeTreated(startingEntities);

        // While all entities have not been processed
        while (root.hasFinishedProcess())
        {
            // While to be treated stack is not empty
            while (root.isToBeTreatedEmpty())
            {
                // Get the first element
                Entity beingTreated = root.popToBeTreated();

                // Get his level of imbrication
                ImbricationNode highestImbricationNode = root.getHighestImbricationNode(beingTreated);

                // If the entity is a start imbrication entity
                if (beingTreated instanceof StartImbricationEntity)
                {
                    // Start a new imbrication node on top of the highest imbrication node
                    highestImbricationNode.startImbricationNode((StartImbricationEntity) beingTreated);
                }
                else
                {
                    // It's a normal entity
                    highestImbricationNode.treatEntity(beingTreated);
                }
            }

            Tools.endImbrication(root);
        }
        return root.compile();
    }
}
