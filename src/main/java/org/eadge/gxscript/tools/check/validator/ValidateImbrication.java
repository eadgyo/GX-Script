package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.GXEntity;
import org.eadge.gxscript.data.entity.StartImbricationGXEntity;
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

        Collection<GXEntity> entities         = rawGXScript.getEntities();
        Collection<GXEntity> startingEntities = rawGXScript.getStartingEntities();

        Set<GXEntity> alreadyTreated = new HashSet<>();

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
                GXEntity beingTreated = root.popToBeTreated();
                alreadyTreated.add(beingTreated);

                // Get his level of imbrication
                ImbricationNode highestImbricationNode = root.getHighestImbricationNode(beingTreated);

                // If GXEntity have in function, entities not in lower or equal imbrication level
                GXEntity inputNotInLowerOrEqualLevel = highestImbricationNode.getInputNotInLowerOrEqualLevel
                        (beingTreated);
                if (inputNotInLowerOrEqualLevel != null)
                {
                    // GXEntity can be in a removed, higher or parallels imbricated level
                    // Add being treated GXEntity and GXEntity not found
                    entitiesWithError.add(beingTreated);
                    entitiesWithError.add(inputNotInLowerOrEqualLevel);
                    return false;
                }

                if (isInParallelsImbrications(beingTreated, highestImbricationNode))
                {
                    // GXEntity can be in a removed, higher or parallels imbricated level
                    // Add being treated GXEntity and GXEntity not found
                    entitiesWithError.add(beingTreated);
                    return false;
                }

                // If the GXEntity is a start imbrication GXEntity
                if (beingTreated instanceof StartImbricationGXEntity)
                {
                    // Start a new imbrication node on top of the highest imbrication node
                    highestImbricationNode.startImbricationNode((StartImbricationGXEntity) beingTreated);
                }
                else
                {
                    // It's a normal GXEntity
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
    private void addBlockingEntities(ImbricationNode root, Set<GXEntity> alreadyTreated)
    {
        ArrayList<ImbricationNode> leaves = root.getLeaves();
        for (ImbricationNode leaf : leaves)
        {
            // Get not treated entities
            Collection<? extends GXEntity> allNotTreatedElements = leaf.getAllNotTreatedElements();

            // Check if entities have function in different level of imbrication
            for (GXEntity notTreatedElement : allNotTreatedElements)
            {
                if (hasInputsInParallelsImbrications(notTreatedElement, leaf, alreadyTreated))
                    entitiesWithError.add(notTreatedElement);
            }
        }
    }

    /**
     * Check if GXEntity have in function different output level of imbrication from one start GXEntity imbrication
     *
     * @param GXEntity                 checked GXEntity inputs
     * @param highestImbricationNode his highest imbrication node
     *
     * @return true if GXEntity has in function different output level of imbrication
     */
    private static boolean isInParallelsImbrications(GXEntity GXEntity,
                                                     ImbricationNode highestImbricationNode)
    {
        StartImbricationGXEntity startImbricationEntity = highestImbricationNode.getStartImbricationEntity();

        // It's the root imbrication
        if (startImbricationEntity == null)
            return false;

        // Get the imbrication id to be compared with function imbrication id with same start imbrication GXEntity
        int currentImbrication = highestImbricationNode.getImbricationOutputIndex();

        for (int inputIndex = 0; inputIndex < GXEntity.getNumberOfInputs(); inputIndex++)
        {
            GXEntity inputGXEntity = GXEntity.getInputEntity(inputIndex);

            // If function is start imbrication GXEntity
            if (inputGXEntity == startImbricationEntity)
            {
                // Get the index
                int outputIndex = GXEntity.getIndexOfOutputFromEntityOnInput(inputIndex);

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
            if (isInParallelsImbrications(GXEntity, parent))
                return true;
        }

        return false;
    }

    /**
     * Check if GXEntity has inputs recursively in parallels level of imbrication.
     *
     * @param GXEntity                 checked GXEntity function
     * @param highestImbricationNode GXEntity's highest imbrication node
     * @param safeEntities           entities safe, that doesn't need to be treated
     *
     * @return true if GXEntity has inputs in parallels level of imbrication, false otherwise.
     */
    private static boolean hasInputsInParallelsImbrications(GXEntity GXEntity,
                                                            ImbricationNode highestImbricationNode,
                                                            Set<GXEntity> safeEntities)
    {
        Set<GXEntity>   alreadyTreated      = new HashSet<GXEntity>();
        Stack<GXEntity> toBeTreatedEntities = new Stack<>();

        // Add the GXEntity to check
        toBeTreatedEntities.add(GXEntity);

        // While there are entities to treat
        while (!toBeTreatedEntities.empty())
        {
            GXEntity poppedGXEntity = toBeTreatedEntities.pop();

            // Treat GXEntity
            alreadyTreated.add(poppedGXEntity);

            // Check inputs levels
            if (isInParallelsImbrications(poppedGXEntity, highestImbricationNode))
                return true;

            // Add function entities not treated
            Collection<GXEntity> allInputEntities = poppedGXEntity.getAllInputEntities();
            for (GXEntity inputGXEntity : allInputEntities)
            {
                if (inputGXEntity != null)
                {
                    // If function GXEntity is not safe and it is not already treated
                    if (!safeEntities.contains(inputGXEntity) && !alreadyTreated.contains(inputGXEntity))
                        toBeTreatedEntities.add(inputGXEntity);
                }
            }
        }

        return false;
    }
}
