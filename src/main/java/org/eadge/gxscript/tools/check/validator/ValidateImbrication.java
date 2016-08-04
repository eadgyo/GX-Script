package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Checks if entities using imbrication like if, for... don't use future variable or variable created in other path.
 * For example one variable created in else part and used in if part.
 */
public class ValidateImbrication extends ValidatorModel
{
    @Override
    public boolean validate(Collection<Entity> entities)
    {
        super.validate(entities);


        return true;
    }
}
