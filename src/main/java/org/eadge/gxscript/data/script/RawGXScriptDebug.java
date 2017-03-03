package org.eadge.gxscript.data.script;

import org.eadge.gxscript.data.entity.base.GXEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eadgyo on 12/08/16.
 *
 * Holds all entities in a GXScript and facilitate access to already created variables
 */
public class RawGXScriptDebug extends RawGXScript
{
    public Map<String, GXEntity> map = new HashMap<>();

    public void addEntity(String name, GXEntity GXEntity)
    {
        addEntity(GXEntity);

        assert (!map.containsKey(name));

        map.put(name, GXEntity);
    }

    public GXEntity getEntity(String name)
    {
        GXEntity GXEntity = map.get(name);

        assert (GXEntity != null);

        return GXEntity;
    }

    public void removeEntity(String name)
    {
        GXEntity GXEntity = getEntity(name);

        assert (map.containsKey(name));

        removeEntity(GXEntity);
    }
}
