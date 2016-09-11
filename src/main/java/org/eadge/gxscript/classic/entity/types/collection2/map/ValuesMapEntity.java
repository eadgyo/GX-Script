package org.eadge.gxscript.classic.entity.types.collection2.map;

import org.eadge.gxscript.classic.entity.types.collection.ClassItem;
import org.eadge.gxscript.data.entity.DefaultEntity;
import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

import java.util.Collection;
import java.util.Map;

/**
 * Created by eadgyo on 11/09/16.
 *
 * Get values from map
 */
public class ValuesMapEntity extends DefaultEntity implements ClassItem
{
    public static final int MAP_INPUT_INDEX  = 0;
    public static final int NEXT_INPUT_INDEX = 1;

    public static final int VALUES_OUTPUT_INDEX = 0;
    public static final int CONTINUE_OUTPUT_INDEX = 1;

    private Class itemClass;

    public ValuesMapEntity()
    {
        super("Values from map");

        itemClass = DEFAULT_CLASS;

        // Add inputs
        addInputEntry(MAP_INPUT_INDEX, "Map", Map.class);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Add outputs
        addOutputEntry(VALUES_OUTPUT_INDEX, "Values", Collection.class);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @Override
    public void linkAsInput(int inputIndex, int entityOutput, Entity entity)
    {
        super.linkAsInput(inputIndex, entityOutput, entity);

        if (inputIndex == MAP_INPUT_INDEX)
        {
            // Get output class
            itemClass = ((ClassItem) getInputEntity(MAP_INPUT_INDEX)).getItemClass();
        }
    }

    @Override
    public void unlinkAsInput(int inputIndex)
    {
        super.unlinkAsInput(inputIndex);

        if (inputIndex == MAP_INPUT_INDEX)
        {
            // Change output class
            itemClass = DEFAULT_CLASS;
        }
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

                // Get map
                Map map = (Map) objects[MAP_INPUT_INDEX];

                // Get values
                Collection values = map.values();

                // Push values in memoru
                program.pushInMemory(values);
            }
        };
    }

    @Override
    public Class getItemClass()
    {
        return itemClass;
    }
}
