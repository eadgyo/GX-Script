package org.eadge.gxscript.classic.entity.types.collection.set;

import org.eadge.gxscript.classic.entity.types.collection.CollectionEntity;
import org.eadge.gxscript.data.entity.ModifyingEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

import java.util.Collection;
import java.util.Set;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Create one set
 */
public abstract class SetEntity extends CollectionEntity
{
    public SetEntity(String name, Class defaultClass)
    {
        super(name, defaultClass);
    }

    @Override
    public ModifyingEntity createModificationEntity(int outputIndex)
    {
        return null;
    }

    @Override
    public Func getFunc()
    {
        // If the source input entry is used
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



    public Class getItemClass()
    {
        return itemClass;
    }

    public void setItemClass(Class itemClass)
    {
        this.itemClass = itemClass;
    }

    public abstract Set createSet();
    public abstract Set createSet(Collection collection);
}
