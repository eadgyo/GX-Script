package org.eadge.gxscript.data.entity.classic.entity.types.collection.common;

import org.eadge.gxscript.data.entity.model.model.OneInputDefinesOneOutput;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;

import java.util.*;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Clone collection
 */
public class CloneCollectionGXEntity extends OneInputDefinesOneOutput
{
    public CloneCollectionGXEntity()
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
