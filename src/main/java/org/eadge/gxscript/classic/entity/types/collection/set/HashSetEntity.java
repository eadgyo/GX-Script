package org.eadge.gxscript.classic.entity.types.collection.set;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eadgyo on 10/09/16.
 *
 * Create hash set
 */
public class HashSetEntity extends SetEntity
{
    public HashSetEntity()
    {
        super("HashSet");
    }


    @Override
    public Set createSet()
    {
        return new HashSet();
    }

    @Override
    public Set createSet(Collection collection)
    {
        //noinspection unchecked
        return new HashSet(collection);
    }
}
