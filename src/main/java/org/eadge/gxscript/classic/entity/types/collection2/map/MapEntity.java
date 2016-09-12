package org.eadge.gxscript.classic.entity.types.collection2.map;

import org.eadge.gxscript.classic.entity.types.collection2.model.Collection2Entity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

import java.util.Map;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Map entity
 */
public abstract class MapEntity extends Collection2Entity
{
    public MapEntity(String name)
    {
        super(name, Map.class, DEFAULT_CLASS);
    }

    @Override
    public Func getFunc()
    {
        if (isInputUsed(SOURCE_INPUT_INDEX) )
        {
            return new Func()
            {
                @Override
                public void run(Program program)
                {
                    Object[] objects = program.loadCurrentParametersObjects();

                    // Get the source
                    Map sourceMap = (Map) objects[SOURCE_INPUT_INDEX];

                    // Create new map using the source
                    Map map = createMap(sourceMap);

                    // Push into memory created map
                    program.pushInMemory(map);
                }
            };
        }
        else
        {
            return new Func()
            {
                @Override
                public void run(Program program)
                {
                    // Create new map
                    Map map = createMap();

                    // Push into memory created map
                    program.pushInMemory(map);
                }
            };
        }
    }


    public abstract Map createMap();
    public abstract Map createMap(Map map);
}