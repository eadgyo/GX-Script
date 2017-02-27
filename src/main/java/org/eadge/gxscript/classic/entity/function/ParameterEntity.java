package org.eadge.gxscript.classic.entity.function;

import org.eadge.gxscript.data.entity.DefaultEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;

/**
 * Created by eadgyo on 27/02/17.
 *
 * Used to create entity with parameters (as inputs)
 */
public class ParameterEntity extends DefaultEntity
{
    public final static int SOURCE_INPUT_INDEX = 0;

    public final static int CONTINUE_OUTPUT_INDEX = 0;

    public ParameterEntity()
    {
        super("Parameter");

        // Add continue output
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Object.class);
    }


    @Override
    public Func getFunc()
    {
        return new Func()
        {
            @Override
            public void run(Program program)
            {
                // Get the source
                Object[] objects = program.loadCurrentParametersObjects();
                Object  source  = (Boolean) objects[SOURCE_INPUT_INDEX];

                program.pushInMemory(source);
            }
        };
    }

    /**
     * Create the func data addresses used to retrieve input object
     * @return Func data addresses
     */
    public FuncDataAddresses allocParameterAddresses(DataAddress dataAddress)
    {
        // Alloc one address
        dataAddress.alloc();
        return new FuncDataAddresses(1);
    }
}
