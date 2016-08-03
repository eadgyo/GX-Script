package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Validate links
 */
public class ValidatorLinks extends ValidatorModel
{
    @Override
    public boolean validate(Collection<Entity> entities)
    {
        super.validate(entities);

        // For each entities check if their input are good
        for (Entity entity : entities)
        {
            // If output is not valid
            if (!entity.hasValidOutput())
            {
                return false;
            }
        }

        // All links are valid
        return true;
    }
}
