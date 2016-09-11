package org.eadge.gxscript.classic.entity.types.collection.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by eadgyo on 10/09/16.
 *
 *
 */
public class ArrayListEntity extends ListEntity
{
    public ArrayListEntity()
    {
        super();
    }

    @Override
    public List createList()
    {
        return new ArrayList();
    }

    @Override
    public List createList(Collection collection)
    {
        //noinspection unchecked
        return new ArrayList(collection);
    }
}
