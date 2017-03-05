package org.eadge.gxscript.tools.check;

import org.eadge.gxscript.data.compile.script.RawGXScript;
import org.eadge.gxscript.tools.check.validator.*;

/**
 * Created by eadgyo on 02/08/16.
 *
 * GX script validator
 */
public class GXValidator extends ValidatorModel
{
    private ValidateAllEntitiesPresent validatorAllEntitiesPresent;
    private ValidatorModel             validatorInputs;
    private ValidatorModel             validatorLinks;
    private ValidatorModel             validatorNoInterdependency;
    private ValidatorModel             validatorImbrications;

    public GXValidator()
    {
        validatorAllEntitiesPresent = new ValidateAllEntitiesPresent();
        validatorInputs = new ValidateEntityHaveInput();
        validatorLinks = new ValidateLinks();
        validatorNoInterdependency = new ValidateNoInterdependency();
        validatorImbrications = new ValidateImbrication();
    }

    @Override
    public boolean validate(RawGXScript gxScript)
    {
        gxScript.updateEntities();

        if (!super.validate(validatorAllEntitiesPresent, gxScript))
            return false;

        if (!super.validate(validatorInputs, gxScript))
            return false;

        if (!super.validate(validatorLinks, gxScript))
            return false;

        if (!super.validate(validatorNoInterdependency, gxScript))
            return false;

        return super.validate(validatorImbrications, gxScript);
    }
}
