package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.imbrication.StartImbricationEntity;
import org.eadge.gxscript.data.imbrication.ImbricationNode;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.tools.Tools;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by eadgyo on 09/08/16.
 *
 * Validate imbrications
 */
public class ValidateImbrication extends ValidatorModel
{
    @Override
    public boolean validate(RawGXScript rawGXScript)
    {
        super.validate(rawGXScript);

        Collection<Entity> entities = rawGXScript.getEntities();
        Collection<Entity> startingEntities = rawGXScript.getStartingEntities();

        // Create level 0 of imbrication, it's the root of the tree
        // All entities are in this level
        ImbricationNode root = new ImbricationNode(entities);

        // Add them to the to be treated stack
        root.addAllToBeTreated(startingEntities);

        // While all entities have not been processed
        while (!root.hasFinishedProcess())
        {
            // While to be treated stack is not empty
            while (!root.isToBeTreatedEmpty())
            {
                // Get the first element
                Entity beingTreated = root.popToBeTreated();

                // Get his level of imbrication
                ImbricationNode highestImbricationNode = root.getHighestImbricationNode(beingTreated);

                // If entity have in input, entities not in lower or equal imbrication level
                Entity inputNotInLowerOrEqualLevel = highestImbricationNode.getInputNotInLowerOrEqualLevel(beingTreated);
                if (inputNotInLowerOrEqualLevel != null)
                {
                    // Entity can be in a removed, higher or parallels imbricated level
                    // Add being treated entity and entity not found
                    entitiesWithError.add(beingTreated);
                    entitiesWithError.add(inputNotInLowerOrEqualLevel);
                    return false;
                }

                if (hasStartImbricationInputButDifferentLevelOfImbrication(beingTreated, highestImbricationNode))
                {
                    // Entity can be in a removed, higher or parallels imbricated level
                    // Add being treated entity and entity not found
                    entitiesWithError.add(beingTreated);
                    return false;
                }

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

            boolean hasEndedImbrication = Tools.endImbrication(root);

            // Check if no changed
            if (!hasEndedImbrication)
            {
                ArrayList<ImbricationNode> leaves = root.getLeaves();
                for (ImbricationNode leaf : leaves)
                {
                    ImbricationNode parent = leaf.getParent();
                    if (parent != null)
                    {
                        // End this imbrication
                        parent.endImbrication(leaf);

                        Entity maybeInError = parent.popToBeTreated();
                        entitiesWithError.add(maybeInError);
                    }
                }

                return false;
            }
        }
        return true;
    }

    public static boolean hasStartImbricationInputButDifferentLevelOfImbrication(Entity entity, ImbricationNode highestImbricationNode)
    {
        ImbricationNode parent = highestImbricationNode.getParent();
        if (parent != null)
        {
            if (hasStartImbricationInputButDifferentLevelOfImbrication(entity, parent))
                return true;
        }

        StartImbricationEntity startImbricationEntity = highestImbricationNode.getStartImbricationEntity();

        if (startImbricationEntity == null)
            return false;

        int currentImbrication = highestImbricationNode.getImbricationOutputIndex();

        for (int inputIndex = 0; inputIndex < entity.getNumberOfInputs(); inputIndex++)
        {
            Entity inputEntity = entity.getInputEntity(inputIndex);

            // If input is start imbrication entity
            if (inputEntity == startImbricationEntity)
            {
                // Get the index
                int outputIndex = entity.getIndexOfOutputFromEntityOnInput(inputIndex);

                // Get the imbrication index at the output index
                int outputImbrication = startImbricationEntity.getOutputImbrication(outputIndex);

                // Check if the imbrication is the same as the current imbrication node
                if (outputImbrication != currentImbrication)
                {
                    return true;
                }
            }
        }

        return false;
    }
}
