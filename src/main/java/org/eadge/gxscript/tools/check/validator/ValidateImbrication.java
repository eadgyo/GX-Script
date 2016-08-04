package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.imbricate.ImbricatedEntity;
import org.eadge.gxscript.tools.check.validator.exception.InTwoParallelsImbricationsException;

import java.util.*;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Checks if entities using imbrication like if, for... don't use future variable or variable created in other path.
 * For example one variable created in else part and used in if part.
 */
public class ValidateImbrication extends ValidatorModel
{
    private class ImbricationNode
    {
        public Set<ImbricationNode> imbricationNodes = new HashSet<>();
        public ImbricationNode parent = null;

        public Entity      startImbrication                   = null;
        public Set<Entity> treatedEntity                      = new HashSet<>();
        public Set<Entity> alreadyTreatedEntities = new HashSet<>();
        public Set<Entity> treatedEntityAfterEndOfImbrication = new HashSet<>();
        public Set<Entity> allImbricatedEntities = new HashSet<>();

        public void pushLevel(ImbricationNode imbricationNode)
        {

        }

        public boolean inImbrication(Entity entity)
        {
            return allImbricatedEntities.contains(entity);
        }

        public boolean hasDoneImbrication()
        {
            return alreadyTreatedEntities.size() == allImbricatedEntities.size();
        }

        public ImbricationNode getHighestImbrication(Entity entity) throws InTwoParallelsImbricationsException
        {
            // If the node contains this entity
            if (allImbricatedEntities.contains(entity))
            {
                ImbricationNode correspondingImbrication = null;
                for (ImbricationNode imbricationNode : imbricationNodes)
                {
                    ImbricationNode highestImbrication = imbricationNode.getHighestImbrication(entity);

                    // If one higher lane contains this entity
                    if (highestImbrication != null)
                    {
                        // If the corresponding node has already been found
                        if (correspondingImbrication != null)
                        {
                            // Entity is in two parallels nodes
                            throw new InTwoParallelsImbricationsException();
                        }

                        correspondingImbrication = highestImbrication;
                    }
                }
                if (correspondingImbrication != null)
                    return correspondingImbrication;
                else
                    return this;
            }
            return null;
        }
    }


    @Override
    public boolean validate(Collection<Entity> entities)
    {
        super.validate(entities);

        // Get all imbricated entities

        // Create imbrication root imbrication tree
        // Create set of old Imbrication entities

        // Create already treated entities set
        // Create treated elements stack


        // While there are elements not treated
        while ()
        {
            // While the stack of treated elements is not empty
            while ()
            {
                // Take the head of the stack
                // Add this entity to already treated

                // If has input or output old imbrication entities
                if ()
                {
                    // Stop process
                    // Add this element to the error entities list
                }

                // Get all imbricationNode of all input entities, check if output is a imbrication output
                // If two inputs have not the same imbrication level, or their imbricationNode are not at least parent
                if()
                {
                    // Stop process
                    // Add these elements to the error entities list
                }

                // If it's an imbrication entity
                if ()
                {
                    // If none of the input entities have imbrication
                    if ()
                    {
                        // Create new Imbrication
                    }
                    // Push one more imbrication and set this entity as start of new imbrication
                    // Add all entities in imbrication in not treated of the imbrication

                    // Add this block to the set of treated entities in this imbrication

                    // Add blocks treated after the end of the imbrication to nextTreatedAfterEndOfImbrication
                }
                else
                {
                    // It's a normal block

                    // If there is one input entity imbricated
                    if ()
                    {
                        // If have different imbricatedlevel
                        if ()
                        {
                            // Stop process
                            // Add two blocks in errors
                        }

                        // Add this block to the set of treated entities in this imbrication

                        // Add output entity which all inputs already treated, in a lowest
                        // If one of the output entities is a future treated entities of lower or equal level of
                        // imbrication
                        if()
                        {
                            // Stop process
                            // Add two blocks in errors
                        }

                    }
                    else
                    {
                        // It has no imbricated input
                        // Add output entity which all inputs already treated
                    }
                }
            }

            // For all done imbricationNode, which there are no more entities not treated
            for()
            {
                for()
                {
                    // Add treatedEntities of ImbricationLevel to old imbrication. Do not add start imbrication.
                    // Add treated after the end of imbrication to global treatedElements
                }
            }
        }

        return true;
    }
}
