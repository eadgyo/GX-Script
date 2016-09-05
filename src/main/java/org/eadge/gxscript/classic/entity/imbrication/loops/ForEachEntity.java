package org.eadge.gxscript.classic.entity.imbrication.loops;

import org.eadge.gxscript.classic.entity.types.collection.CollectionEntity;
import org.eadge.gxscript.data.entity.DefaultStartImbricationEntity;
import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.address.FuncAddress;
import org.eadge.gxscript.data.script.address.FuncImbricationDataAddresses;

import java.util.Collection;

/**
 * Created by eadgyo on 02/08/16.
 *
 * For each entity
 */
public class ForEachEntity extends DefaultStartImbricationEntity
{
    public static final int COLLECTION_INPUT_INDEX = 0;
    public static final int NEXT_INPUT_INDEX = 1;

    public static final int DO_OUTPUT_INDEX = 0;
    public static final int ITEM_OUTPUT_INDEX = 1;

    public static final int CONTINUE_OUTPUT_INDEX = 2;


    public ForEachEntity()
    {
        super("ForEach");

        // Input
        // Add collection source
        addInputEntry(COLLECTION_INPUT_INDEX, "Collection", Collection.class);

        // Add next input
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Output
        // Add imbricated entry
        addOutputImbricatedEntry(DO_OUTPUT_INDEX, 0, "Do", Void.class);

        // Add item entry
        addOutputImbricatedEntry(ITEM_OUTPUT_INDEX, 0, "Item", Void.class);

        // Add continue
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @Override
    public void addLinkInput(int inputIndex, int outputEntityIndex, Entity entity)
    {
        super.addLinkInput(inputIndex, outputEntityIndex, entity);

        if (inputIndex == COLLECTION_INPUT_INDEX)
        {
            CollectionEntity collectionEntity = (CollectionEntity) entity;
            setOutputClass(ITEM_OUTPUT_INDEX, collectionEntity.getItemClass());
        }
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

                FuncImbricationDataAddresses parameters = program.getCurrentFuncImbricationParameters();

                // Get start and end of functions
                FuncAddress doAddress = parameters.getImbricationAddress(DO_OUTPUT_INDEX);
                FuncAddress endAddress = parameters.getImbricationAddress(DO_OUTPUT_INDEX + 1);

                // Get the collection of objects
                Collection collection = (Collection) objects[COLLECTION_INPUT_INDEX];

                // Iterate other the collection
                for (Object item : collection)
                {
                    // Save memory state
                    program.pushMemoryLevel();

                    // Push item on the memory
                    program.pushInMemory(item);

                    // Run program
                    program.runFromAndUntil(doAddress, endAddress);

                    // Remove added memory level
                    program.popMemoryLevel();
                }

                // Move the cursor to the end
                program.setNextFuncAddress(endAddress);
            }
        };
    }
}