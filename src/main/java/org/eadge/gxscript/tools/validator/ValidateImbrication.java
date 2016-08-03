package org.eadge.gxscript.tools.validator;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.imbricate.ImbricatedEntity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Checks if entities using imbrication like if, for... don't use future variable or variable created in other path.
 * For example one variable created in else part and used in if part.
 */
public class ValidateImbrication extends ValidatorModel
{
    /**
     * Get all imbricated entities
     * @param entities collection of entities
     * @return all imbricated entities in entities collection
     */
    public Collection<ImbricatedEntity> getAllImbricatedEntities(Collection<Entity> entities)
    {
        ArrayList<ImbricatedEntity> imbricatedEntities = new ArrayList<>();

        for (Entity entity : entities)
        {
            if (entity instanceof ImbricatedEntity)
            {
                imbricatedEntities.add((ImbricatedEntity) entity);
            }
        }

        return imbricatedEntities;
    }

    @Override
    public boolean validate(Collection<Entity> entities)
    {
        super.validate(entities);

        // Get all imbricated entities

        // Create treated set

        // For each imbricated entities

        //

        return true;
    }
}
