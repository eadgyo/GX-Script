package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.imbrication.StartImbricationEntity;
import org.eadge.gxscript.data.imbrication.ImbricationNode;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.tools.Tools;

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
        while (root.hasFinishedProcess())
        {
            // While to be treated stack is not empty
            while (root.isToBeTreatedEmpty())
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
        return true;
    }
}
