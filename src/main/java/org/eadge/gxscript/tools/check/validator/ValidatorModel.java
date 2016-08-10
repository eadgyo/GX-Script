package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.script.RawGXScript;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eadgyo on 03/08/16.
 */
public abstract class ValidatorModel
{
    protected Set<Entity> entitiesWithError = new HashSet<>();

    public Collection<Entity> getEntitiesWithError()
    {
        return entitiesWithError;
    }

    public boolean validate(RawGXScript rawGXScript)
    {
        entitiesWithError.clear();

        return true;
    }
}
