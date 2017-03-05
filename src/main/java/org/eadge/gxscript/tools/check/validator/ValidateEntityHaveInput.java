package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.compile.script.RawGXScript;
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

        Collection<GXEntity> entities = rawGXScript.getEntities();

        // Check if all entities have necessary script
        for (GXEntity GXEntity : entities)
        {
            if (!GXEntity.hasAllNeededInput())
            {
                entitiesWithError.add(GXEntity);
            }
        }

        // If there were no errors
        return entitiesWithError.size() == 0;
    }
}
