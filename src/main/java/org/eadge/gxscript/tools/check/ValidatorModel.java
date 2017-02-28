package org.eadge.gxscript.tools.check;

import org.eadge.gxscript.data.entity.GXEntity;
import org.eadge.gxscript.data.script.RawGXScript;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Validator model
 */
public abstract class ValidatorModel
{
    protected Set<GXEntity> entitiesWithError = new HashSet<>();

    public Collection<GXEntity> getEntitiesWithError()
    {
        return entitiesWithError;
    }

    public boolean validate(RawGXScript rawGXScript)
    {
        entitiesWithError.clear();

        return true;
    }

    public boolean validate(ValidatorModel validatorModel, RawGXScript rawGXScript)
    {
        if (!validatorModel.validate(rawGXScript))
        {
            entitiesWithError.addAll(validatorModel.getEntitiesWithError());
            return false;
        }

        return true;
    }
}
