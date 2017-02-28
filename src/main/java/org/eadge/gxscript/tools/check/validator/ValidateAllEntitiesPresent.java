package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.GXEntity;
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

        Collection<GXEntity> entities = rawGXScript.getEntities();

        // Check if all entities have necessary script
        loop: for (GXEntity GXEntity : entities)
        {
            Collection<GXEntity> allInputEntities = GXEntity.getAllInputEntities();

            for (GXEntity inputGXEntity : allInputEntities)
            {
                if (inputGXEntity != null && !entities.contains(inputGXEntity))
                {
                    entitiesWithError.add(inputGXEntity);
                    continue loop;
                }
            }

            Collection<GXEntity> allOutputEntitiesCollection = GXEntity.getAllOutputEntitiesCollection();

            for (GXEntity outputGXEntity : allOutputEntitiesCollection)
            {
                if (outputGXEntity != null && !entities.contains(outputGXEntity))
                {
                    entitiesWithError.add(outputGXEntity);
                    continue loop;
                }
            }
        }

        // If there were no errors
        return entitiesWithError.size() == 0;
    }
}
