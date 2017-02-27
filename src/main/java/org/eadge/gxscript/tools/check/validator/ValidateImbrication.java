package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.StartImbricationEntity;
import org.eadge.gxscript.data.imbrication.ImbricationNode;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.tools.Tools;
import org.eadge.gxscript.tools.check.ValidatorModel;

import java.util.*;

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

        Collection<Entity> entities         = rawGXScript.getEntities();
        Collection<Entity> startingEntities = rawGXScript.getStartingEntities();

        Set<Entity> alreadyTreated = new HashSet<>();

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
                alreadyTreated.add(beingTreated);

                // Get his level of imbrication
                ImbricationNode highestImbricationNode = root.getHighestImbricationNode(beingTreated);

                // If entity have in function, entities not in lower or equal imbrication level
                Entity inputNotInLowerOrEqualLevel = highestImbricationNode.getInputNotInLowerOrEqualLevel
                        (beingTreated);
                if (inputNotInLowerOrEqualLevel != null)
                {
                    // Entity can be in a removed, higher or parallels imbricated level
                    // Add being treated entity and entity not found
                    entitiesWithError.add(beingTreated);
                    entitiesWithError.add(inputNotInLowerOrEqualLevel);
                    return false;
                }

                if (isInParallelsImbrications(beingTreated, highestImbricationNode))
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
                // There are entities blocking check imbrication process
                addBlockingEntities(root, alreadyTreated);
                return false;
            }
        }
        return true;
    }

    /**
     * Add entities blocking imbrication checking process. They are in multiples level of imbrication.
     *
     * @param root imbrication root
     */
    private void addBlockingEntities(ImbricationNode root, Set<Entity> alreadyTreated)
    {
        ArrayList<ImbricationNode> leaves = root.getLeaves();
        for (ImbricationNode leaf : leaves)
        {
            // Get not treated entities
            Collection<? extends Entity> allNotTreatedElements = leaf.getAllNotTreatedElements();

            // Check if entities have function in different level of imbrication
            for (Entity notTreatedElement : allNotTreatedElements)
            {
                if (hasInputsInParallelsImbrications(notTreatedElement, leaf, alreadyTreated))
                    entitiesWithError.add(notTreatedElement);
            }
        }
    }

    /**
     * Check if entity have in function different output level of imbrication from one start entity imbrication
     *
     * @param entity                 checked entity inputs
     * @param highestImbricationNode his highest imbrication node
     *
     * @return true if entity has in function different output level of imbrication
     */
    private static boolean isInParallelsImbrications(Entity entity,
                                                     ImbricationNode highestImbricationNode)
    {
        StartImbricationEntity startImbricationEntity = highestImbricationNode.getStartImbricationEntity();

        // It's the root imbrication
        if (startImbricationEntity == null)
            return false;

        // Get the imbrication id to be compared with function imbrication id with same start imbrication entity
        int currentImbrication = highestImbricationNode.getImbricationOutputIndex();

        for (int inputIndex = 0; inputIndex < entity.getNumberOfInputs(); inputIndex++)
        {
            Entity inputEntity = entity.getInputEntity(inputIndex);

            // If function is start imbrication entity
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

        // Check parents
        ImbricationNode parent = highestImbricationNode.getParent();
        if (parent != null)
        {
            if (isInParallelsImbrications(entity, parent))
                return true;
        }

        return false;
    }

    /**
     * Check if entity has inputs recursively in parallels level of imbrication.
     *
     * @param entity                 checked entity function
     * @param highestImbricationNode entity's highest imbrication node
     * @param safeEntities           entities safe, that doesn't need to be treated
     *
     * @return true if entity has inputs in parallels level of imbrication, false otherwise.
     */
    private static boolean hasInputsInParallelsImbrications(Entity entity,
                                                            ImbricationNode highestImbricationNode,
                                                            Set<Entity> safeEntities)
    {
        Set<Entity>   alreadyTreated      = new HashSet<Entity>();
        Stack<Entity> toBeTreatedEntities = new Stack<>();

        // Add the entity to check
        toBeTreatedEntities.add(entity);

        // While there are entities to treat
        while (!toBeTreatedEntities.empty())
        {
            Entity poppedEntity = toBeTreatedEntities.pop();

            // Treat entity
            alreadyTreated.add(poppedEntity);

            // Check inputs levels
            if (isInParallelsImbrications(poppedEntity, highestImbricationNode))
                return true;

            // Add function entities not treated
            Collection<Entity> allInputEntities = poppedEntity.getAllInputEntities();
            for (Entity inputEntity : allInputEntities)
            {
                if (inputEntity != null)
                {
                    // If function entity is not safe and it is not already treated
                    if (!safeEntities.contains(inputEntity) && !alreadyTreated.contains(inputEntity))
                        toBeTreatedEntities.add(inputEntity);
                }
            }
        }

        return false;
    }
}
