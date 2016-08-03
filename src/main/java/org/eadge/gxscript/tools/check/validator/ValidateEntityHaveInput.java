package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Check if there are no entities alone
 */
public class ValidateEntityHaveInput extends ValidatorModel
{
    public boolean validate(Collection<Entity> entities)
    {
        super.validate(entities);

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
