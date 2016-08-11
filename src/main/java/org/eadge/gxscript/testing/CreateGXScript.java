package org.eadge.gxscript.testing;

import org.eadge.gxscript.data.entity.displayer.PrintEntity;
import org.eadge.gxscript.data.entity.imbrication.conditionals.IfEntity;
import org.eadge.gxscript.data.entity.types.number.RealEntity;
import org.eadge.gxscript.data.entity.types.number.comparison.EqualToNumberEntity;
import org.eadge.gxscript.data.script.RawGXScript;

/**
 * Created by eadgyo on 11/08/16.
 *
 */
public class CreateGXScript
{
    public static RawGXScript createScriptIf()
    {
        RawGXScript rawGXScript = new RawGXScript();

        // Create 3 real variables
        RealEntity realEntity1 = new RealEntity(20f);
        RealEntity realEntity2 = new RealEntity(10f);
        RealEntity realEntity3 = new RealEntity();

        // Link realEntity2 on realEntity3 input
        realEntity3.linkAsInput(RealEntity.SET_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity2);

        // Create real comparison
        EqualToNumberEntity equalToNumberEntity = new EqualToNumberEntity();
        equalToNumberEntity.linkAsInput(EqualToNumberEntity.V0_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity1);
        equalToNumberEntity.linkAsInput(EqualToNumberEntity.V1_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity2);

        // Create if entity block control
        IfEntity ifEntity = new IfEntity();
        ifEntity.linkAsInput(IfEntity.TEST_INPUT_INDEX, EqualToNumberEntity.RESULT_OUTPUT_INDEX, equalToNumberEntity);

        // Create 3 prints for each path
        PrintEntity success = new PrintEntity("Success");
        success.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, IfEntity.SUCCESS_OUTPUT_INDEX, ifEntity);

        PrintEntity fail = new PrintEntity("Fail");
        fail.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, IfEntity.FAIL_OUTPUT_INDEX, ifEntity);

        PrintEntity continueP = new PrintEntity("Continue");
        continueP.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, IfEntity.CONTINUE_OUTPUT_INDEX, ifEntity);

        // Add all created entities to raw gx script
        rawGXScript.addEntity(realEntity1);
        rawGXScript.addEntity(realEntity2);
        rawGXScript.addEntity(realEntity3);
        rawGXScript.addEntity(equalToNumberEntity);
        rawGXScript.addEntity(ifEntity);
        rawGXScript.addEntity(success);
        rawGXScript.addEntity(fail);
        rawGXScript.addEntity(continueP);

        return rawGXScript;
    }
}
