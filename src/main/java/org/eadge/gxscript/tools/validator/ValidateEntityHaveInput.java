package org.eadge.gxscript.tools.validator;

import org.eadge.gxscript.data.entity.Entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Check if there are no entities alone
 */
public class ValidateEntityHaveInput implements ValidatorModel
{
    private Set<Entity> entitiesWithError = new HashSet<>();

    public Collection<Entity> getEntitiesWithError()
    {
        return entitiesWithError;
    }

    public boolean validate(Collection<Entity> entities)
    {
        entitiesWithError.clear();

        // Check if all entities have necessary input
        for (Entity entity : entities)
        {
            if (!entity.hasAllNeededInput())
            {
                entitiesWithError.add(entity);
            }
        }

        // If there were no errors
        return entitiesWithError.size() == 0;
    }
}
