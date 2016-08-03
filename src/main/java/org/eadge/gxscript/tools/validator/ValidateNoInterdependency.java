package org.eadge.gxscript.tools.validator;

import org.eadge.gxscript.data.entity.Entity;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Check if there are entities with interdependency. It's game be circle of interdependency or self-input-output.
 * For example, one entity which have his input linked to his output is one case of interdependency.
 *
 */
public class ValidateNoInterdependency implements ValidatorModel
{
    @Override
    public Collection<Entity> getEntitiesWithError()
    {
        return null;
    }

    @Override
    public boolean validate(Collection<Entity> entities)
    {
        // Get all starting entities

        // Starting entities are added to stack of next treated entities
        // There are linked to a on lane set which contains all entities on lane


        // While there are still not treated entities in stack

            // Remove the first not treated entity

            // If the entity is already in on lane set
                // There is interdependency
                // Add all lane to error
                // Stop process, it's not validated

            // Add this element to the on lane set
            // Add this first entity to set of treated elements

            // Add all linked output entities which have all input in treated set, and link to them the set


        return false;
    }
}
