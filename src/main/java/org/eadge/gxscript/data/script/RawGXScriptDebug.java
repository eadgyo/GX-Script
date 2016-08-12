package org.eadge.gxscript.data.script;

import org.eadge.gxscript.data.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eadgyo on 12/08/16.
 *
 * Holds all entities in a GXScript and facilitate access to already created variables
 */
public class RawGXScriptDebug extends RawGXScript
{
    public Map<String, Entity> map = new HashMap<>();

    public void addEntity(String name, Entity entity)
    {
        addEntity(entity);

        assert (!map.containsKey(name));

        map.put(name, entity);
    }

    public Entity getEntity(String name)
    {
        Entity entity = map.get(name);

        assert (entity != null);

        return entity;
    }

    public void removeEntity(String name)
    {
        Entity entity = getEntity(name);

        assert (map.containsKey(name));

        removeEntity(entity);
    }
}
