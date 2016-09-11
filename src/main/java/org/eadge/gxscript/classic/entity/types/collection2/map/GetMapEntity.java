package org.eadge.gxscript.classic.entity.types.collection2.map;

import org.eadge.gxscript.classic.entity.types.collection2.model.Collection2Define1Input1Output;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

import java.util.Map;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Get key from map entity
 */
public class GetMapEntity extends Collection2Define1Input1Output
{
    public static final int NEXT_INPUT_INDEX = 2;

    public static final int CONTINUE_OUTPUT_INDEX = 1;

    public GetMapEntity()
    {
        super("Get key from map", "Map", Map.class);

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
                Object key = objects[E_INPUT_INDEX];

                // Get the value from the key in map
                Object value = map.get(key);

                // Push value in memory
                program.pushInMemory(value);
            }
        };
    }
}
