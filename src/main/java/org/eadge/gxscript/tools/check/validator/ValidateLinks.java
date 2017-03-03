package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.base.GXEntity;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.tools.check.ValidatorModel;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Validate links
 */
public class ValidateLinks extends ValidatorModel
{
    @Override
    public boolean validate(RawGXScript rawGXScript)
    {
        super.validate(rawGXScript);

        Collection<GXEntity> entities = rawGXScript.getEntities();

        // For each entities check if their script are good
        for (GXEntity GXEntity : entities)
        {
            // If output is not valid
            if (!GXEntity.hasValidOutput())
            {
                return false;
            }
        }

        // All links are valid
        return true;
    }
}
