package org.eadge.gxscript.classic.entity.types.collection.common;

import org.eadge.gxscript.data.entity.special.OneInputDefinesOneOutput;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

import java.util.*;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Clone collection
 */
public class CloneCollectionEntity extends OneInputDefinesOneOutput
{
    public CloneCollectionEntity()
    {
        super("Clone collection", Collection.class);
    }

    @Override
    public Func getFunc()
    {
        return new Func()
        {
            private Class defaultClass;

            public Func init(Class defaultClass)
            {
                this.defaultClass = defaultClass;

                return this;
            }

            @Override
            public void run(Program program)
            {

                Object[] objects = program.loadCurrentParametersObjects();

                // Get collection to copy
                Collection collection = (Collection) objects[SOURCE_INPUT_INDEX];

                Object added;

                // Clone collection
                if (this.defaultClass == ArrayList.class)
                {
                    //noinspection unchecked
                    added = new ArrayList(collection);
                }
                else if (this.defaultClass == HashMap.class)
                {
                    //noinspection unchecked
                    added = new HashMap((Map) collection);
                }
                else if (this.defaultClass == HashSet.class)
                {
                    //noinspection unchecked
                    added = new HashSet<>((Set) collection);
                }
                else
                {
                    throw new RuntimeException("No class corresponding");
                }

                program.pushInMemory(added);
            }
        };
    }
}
