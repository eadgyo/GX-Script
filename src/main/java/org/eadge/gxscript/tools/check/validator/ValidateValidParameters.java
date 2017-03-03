package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.InputScriptGXEntity;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.tools.check.ValidatorModel;

import java.util.Collection;

/**
 * Created by eadgyo on 28/02/17.
 *
 * Validate that all parameters entities are connected with valid output
 */
public class ValidateValidParameters extends ValidatorModel
{
    public boolean validate(RawGXScript rawGXScript)
    {
        super.validate(rawGXScript);

        Collection<InputScriptGXEntity> entities = rawGXScript.getScriptInputEntities();

        // Check if all entities have necessary script
        for (InputScriptGXEntity entity : entities)
        {
            if (!entity.isValidParameter())
            {
                entitiesWithError.add(entity);
            }
        }

        // If there were no errors
        return entitiesWithError.size() == 0;
    }
}
