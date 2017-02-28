package org.eadge.gxscript.classic.entity.types.collection.list;

import org.eadge.gxscript.classic.entity.types.collection.CollectionGXEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

import java.util.Collection;
import java.util.List;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Create one list
 */
public abstract class ListGXEntity extends CollectionGXEntity
{
    public ListGXEntity()
    {
        super("List", List.class, DEFAULT_CLASS);
    }

    @Override
    public Func getFunc()
    {
        // If the source function entry is used
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
                    @SuppressWarnings("unchecked") List list = createList(collection);

                    // Push into memory created collection
                    program.pushInMemory(list);
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
                    List list = createList();

                    // Push into memory created collection
                    program.pushInMemory(list);
                }
            };
        }
    }

    public abstract List createList();
    public abstract List createList(Collection collection);
}
