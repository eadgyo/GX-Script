package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.tools.check.ValidatorModel;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Check if there are no entities alone
 */
public class ValidateEntityHaveInput extends ValidatorModel
{
    public boolean validate(RawGXScript rawGXScript)
    {
        super.validate(rawGXScript);

        Collection<Entity> entities = rawGXScript.getEntities();

        // Check if all entities have necessary function
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
