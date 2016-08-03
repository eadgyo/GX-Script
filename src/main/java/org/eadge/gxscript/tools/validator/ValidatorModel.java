package org.eadge.gxscript.tools.validator;

import org.eadge.gxscript.data.entity.Entity;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 */
public interface ValidatorModel
{
    Collection<Entity> getEntitiesWithError();

    boolean validate(Collection<Entity> entities);
}
