package org.eadge.gxscript.data.entity.classic.entity.types.collection.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by eadgyo on 10/09/16.
 *
 *
 */
public class ArrayListGXEntity extends ListGXEntity
{
    public ArrayListGXEntity()
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
