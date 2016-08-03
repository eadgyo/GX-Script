package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.imbricate.ImbricatedEntity;

import java.util.*;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Checks if entities using imbrication like if, for... don't use future variable or variable created in other path.
 * For example one variable created in else part and used in if part.
 */
public class ValidateImbrication extends ValidatorModel
{
    private class ImbricationLevel
    {
        public Entity      startImbrication                   = null;
        public Set<Entity> treatedEntity                      = new HashSet<>();
        public Set<Entity> treatedEntityAfterEndOfImbrication = new HashSet<>();
    }

    private class Imbrications
    {
        private Deque<ImbricationLevel> stackOfLevels = new ArrayDeque<>();

        public void pushLevel()
        {

        }
    }

    public class ImbricationInfo
    {
        public Imbrications     imbrications     = null;
        public ImbricationLevel imbricationLevel = null;
    }

    /**
     * Get all imbricated entities
     *
     * @param entities collection of entities
     *
     * @return all imbricated entities in entities collection
     */
    public Collection<ImbricatedEntity> getAllImbricatedEntities(Collection<Entity> entities)
    {
        ArrayList<ImbricatedEntity> imbricatedEntities = new ArrayList<>();

        for (Entity entity : entities)
        {
            if (entity instanceof ImbricatedEntity)
            {
                imbricatedEntities.add((ImbricatedEntity) entity);
            }
        }

        return imbricatedEntities;
    }

    @Override
    public boolean validate(Collection<Entity> entities)
    {
        super.validate(entities);

        // Get all imbricated entities

        // Create imbrication list with stack of imbrication and nextTreatedAfterEndOfImbrication
        // Create set of old Imbrication entities

        // Create already treated entities set
        // Create treated elements stack

        // Create map of link entity to his imbrication
        // Create set of treated elements after the end of the imbrication

        // While there are elements not treated
        while ()
        {
            // While the stack of treated elements is not empty
            while ()
            {
                // Take the head of the stack
                // Add this entity to already treated

                // If has input or output old imbrication entities
                if ()
                {
                    // Stop process
                    // Add this element to the error entities list
                }

                // Get the higher imbrication of all input entities
                // Check if it's an imbrication entity if it's linked to the right output

                // If it's an imbrication entity
                if ()
                {


                    // If none of the input entities has imbrication
                    if ()
                    {
                        // Create new Imbrication
                    }
                    // Push one more imbrication and add this entity as start of new imbrication


                    // Mark imbrication and level for this entity
                    // Link entity to ImbricationInfo in map
                    // Add this block to the set of treated entities in this imbrication

                    // Add blocks treated after the end of the imbrication
                }
                else
                {
                    // It's a normal block

                    // If there is one input entity imbricated
                    if ()
                    {
                        // Mark imbrication and level for this entity
                        // Link entity to ImbricationInfo in map
                        // Add this block to the set of treated entities in this imbrication

                        // If one of the output entities is a future treated entities of lower or equal level of
                        // imbrication
                        if()
                        {
                            // Stop process
                            // Add two blocks in errors
                        }
                    }
                    else
                    {
                        // It has no imbricated input
                        // Add output entity which all inputs already treated
                    }
                }
            }

            // For all imbrications and all levels
            for()
            {
                for()
                {
                    // Add treatedEntities of ImbricationLevel to old imbrication. Do not add start imbrication.
                    // Add treated after the end of imbrication to global treatedElements
                }
            }
        }

        return true;
    }
}
