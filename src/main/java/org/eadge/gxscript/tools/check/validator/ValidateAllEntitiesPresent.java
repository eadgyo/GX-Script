package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.tools.check.ValidatorModel;

import java.util.Collection;

/**
 * Created by eadgyo on 14/08/16.
 *
 * Validate that all linked entities are present in the list of entities in script
 */
public class ValidateAllEntitiesPresent extends ValidatorModel
{
    public boolean validate(RawGXScript rawGXScript)
    {
        super.validate(rawGXScript);

        Collection<Entity> entities = rawGXScript.getEntities();

        // Check if all entities have necessary input
        loop: for (Entity entity : entities)
        {
            Collection<Entity> allInputEntities = entity.getAllInputEntities();

            for (Entity inputEntity : allInputEntities)
            {
                if (inputEntity != null && !entities.contains(inputEntity))
                {
                    entitiesWithError.add(inputEntity);
                    continue loop;
                }
            }

            Collection<Entity> allOutputEntitiesCollection = entity.getAllOutputEntitiesCollection();

            for (Entity outputEntity : allOutputEntitiesCollection)
            {
                if (outputEntity != null && !entities.contains(outputEntity))
                {
                    entitiesWithError.add(outputEntity);
                    continue loop;
                }
            }
        }

        // If there were no errors
        return entitiesWithError.size() == 0;
    }
}
