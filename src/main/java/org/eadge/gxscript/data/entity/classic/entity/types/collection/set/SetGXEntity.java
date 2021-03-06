package org.eadge.gxscript.data.entity.classic.entity.types.collection.set;

import org.eadge.gxscript.data.entity.classic.entity.types.collection.CollectionGXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;

import java.util.Collection;
import java.util.Set;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Create one set
 */
public abstract class SetGXEntity extends CollectionGXEntity
{
    public SetGXEntity(String name)
    {
        super(name, Set.class, DEFAULT_CLASS);
    }

    @Override
    public Func getFunc()
    {
        // If the source script entry is used
        if (isInputUsed(SOURCE_INPUT_INDEX) )
        {
            // Create list from the collection
            return new Func()
            {
                @Override
                public void run(Program program)
                {
                    Object[] objects = program.loadCurrentParametersObjects();

                    // Get the source collection
                    Collection collection = (Collection) objects[SOURCE_INPUT_INDEX];

                    // Create new list from source collection
                    @SuppressWarnings("unchecked") Set set = createSet(collection);

                    // Push into memory created collection
                    program.pushInMemory(set);
                }
            };
        }
        else
        {
            // Create a new empty list
            return new Func()
            {
                @Override
                public void run(Program program)
                {
                    // Create new list from source collection
                    Set set = createSet();

                    // Push into memory created collection
                    program.pushInMemory(set);
                }
            };
        }
    }

    public abstract Set createSet();
    public abstract Set createSet(Collection collection);
}
