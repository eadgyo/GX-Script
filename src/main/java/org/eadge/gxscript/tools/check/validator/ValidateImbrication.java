package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Checks if entities using imbrication like if, for... don't use future variable or variable created in other
 * imbrication level. For example one variable created in else part and used in if part.
 */
public class ValidateImbrication extends ValidatorModel
{
    @Override
    public boolean validate(Collection<Entity> entities)
    {
        super.validate(entities);

        // Create level 0 of imbrication, it's the root of the tree
        // All entities are in this level

        // Get starting entities
        // Add them to the to be treated stack

        // While all entities have not been processed
        while ()
        {
            // While to be treated stack is not empty
            while ()
            {
                // Get the first element
                // Get his level of imbrication

                // If entity have in input, entities not in lower or equal imbrication level
                if ()
                {
                    // Entity can be in a removed, higher or parallels imbricated level

                    // Stop the process
                    // Add this entity and the input entity not in lower or equal imbrication
                    return false;
                }

                // If the entity is a start imbrication entity
                if ()
                {
                    // Push one level of imbrication in actual entity's imbrication
                    // Search and add all imbricated entities in new imbrication

                    // Add all imbricated output entities which have all their input block treated in lower or equal
                    // imbrication level
                }
                else
                {
                    // It's a normal entity
                    // Add it to already treated list of his imbrication

                    // Add all output entities which have all their input block treated in lower or equal imbrication
                    // level
                }
            }

            // Get all imbrication leaves

            // For all imbrications leaves
            for ()
            {
                // If leaf have all entities processed at their level
                if ()
                {
                    // Remove already treated elements from elements of the lower ones
                    // Add the starting entity to already treated list of his imbrication

                    // Add all NOT imbricated output entities of starting imbrication entiy which have all their
                    // input block treated in lower or equal imbrication level
                }
            }
        }
        return true;
    }
}
