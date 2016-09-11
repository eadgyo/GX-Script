package org.eadge.gxscript.classic.entity.types.collection2.map;

import org.eadge.gxscript.classic.entity.types.collection2.model.Collection2Define2Inputs;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

import java.util.Map;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Put a key and his value in the map
 */
public class PutMapEntity extends Collection2Define2Inputs
{
    public static final int NEXT_INPUT_INDEX = 3;

    public static final int CONTINUE_OUTPUT_INDEX = 0;

    public PutMapEntity()
    {
        super("Put key in map", "Map", Map.class);

        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @Override
    public Func getFunc()
    {
        return new Func()
        {
            @Override
            public void run(Program program)
            {
                Object[] objects = program.loadCurrentParametersObjects();

                // Get the map
                Map map = (Map) objects[COLLECTION2_INPUT_INDEX];

                // Get the key
                Object key = objects[E0_INPUT_INDEX];

                // Get the value
                Object value = objects[E1_INPUT_INDEX];

                // Put key and value in map
                //noinspection unchecked
                map.put(key, value);
            }
        };
    }
}
