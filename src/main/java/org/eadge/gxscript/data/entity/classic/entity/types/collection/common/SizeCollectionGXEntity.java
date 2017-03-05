package org.eadge.gxscript.data.entity.classic.entity.types.collection.common;

import org.eadge.gxscript.data.entity.model.def.DefaultGXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Size collection GXEntity
 */
public class SizeCollectionGXEntity extends DefaultGXEntity
{
    public final static int COLLECTION_INPUT_INDEX = 0;
    public final static int NEXT_INPUT_INDEX = 1;

    public static final int SIZE_OUTPUT_INDEX = 0;
    public static final int CONTINUE_OUTPUT_INDEX = 1;

    public SizeCollectionGXEntity()
    {
        super("Size collection");

        // Add script
        addInputEntry(COLLECTION_INPUT_INDEX, "Collection", Collection.class);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Add outputs
        addOutputEntry(SIZE_OUTPUT_INDEX, "Size", Integer.class);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @Override
    public Func getFunc()
    {
        return new Func()
        {
            @Override
            public void run(Program program)
            {
                Object[] objects = program.loadCurrentParametersObjects();

                // Get collection
                Collection collection = (Collection) objects[COLLECTION_INPUT_INDEX];

                // Push size of collection in memory
                program.pushInMemory(collection.size());
            }
        };
    }
}
