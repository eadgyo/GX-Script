package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.tools.Tools;

import java.util.*;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Check if there are entities with interdependency. It's game be circle of interdependency or self-input-output.
 * For example, one entity which have his input linked to his output is one case of interdependency.
 *
 */
public class ValidateNoInterdependency extends ValidatorModel
{
    /**
     * Holds entity and his current on lane collection of entities
     */
    private class EntityAndOnLane
    {
        private Entity      entity;
        private Set<Entity> onLaneEntities;

        public Entity getEntity()
        {
            return entity;
        }

        EntityAndOnLane(Entity entity)
        {
            this.entity = entity;
            this.onLaneEntities = new HashSet<>();
        }

        EntityAndOnLane(Entity entity, Set<Entity> onLaneEntities)
        {
            this.entity = entity;
            this.onLaneEntities = onLaneEntities;
        }

        /**
         * Check if one entity is already on lane
         * @param entity checked entity
         * @return true if it is already on lane, false otherwise
         */
        boolean isAlreadyOnLane(Entity entity)
        {
            return onLaneEntities.contains(entity);
        }

        Set<Entity> getOnLaneEntities()
        {
            return onLaneEntities;
        }

        /**
         * Add one entity to the collection of on lanes entities
         * @param entity added entity
         */
        void addEntityToOnLaneEntities(Entity entity)
        {
            onLaneEntities.add(entity);
        }
    }

    private boolean areAllTraitedEntities(Collection<Entity> entities, Set<Entity> treatedEntities)
    {
        for (Entity inputEntity : entities)
        {
            if (!treatedEntities.contains(inputEntity))
            {
                return false;
            }
        }
        return true;
    }

    private void addNextTraitedElements(Entity testedEntity,
                                        Set<Entity> onLaneEntities,
                                        Deque<EntityAndOnLane> stack,
                                        Set<Entity> treatedEntities)
    {
        Collection<Entity> inputEntities = testedEntity.getAllInputEntities();

        // If all of the blocks input are treated
        if (areAllTraitedEntities(inputEntities, treatedEntities))
        {
            // Add not treated entities to the stack of next treated entities and clone his on lane entities
            EntityAndOnLane insertedEntityAndLane = new EntityAndOnLane(testedEntity, new HashSet<>(onLaneEntities));
            stack.add(insertedEntityAndLane);
        }
    }

    @Override
    public boolean validate(RawGXScript rawGXScript)
    {
        super.validate(rawGXScript);

        Collection<Entity> entities = rawGXScript.getEntities();

        Set<Entity> treatedEntities = new HashSet<>();

        // Get all starting entities
        Collection<Entity> startingEntities = Tools.getStartingEntities(entities);

        // Starting entities are added to stack of next treated entities
        // They are linked to a on lane set which contains all entities on lane
        Deque<EntityAndOnLane> stack = new ArrayDeque<>();

        for (Entity startingEntity : startingEntities)
        {
            // Create entity and his on lane entities
            EntityAndOnLane notTreatedElement = new EntityAndOnLane(startingEntity);
        }

        // While there are still not treated entities in stack
        while (stack.size() != 0)
        {
            // Remove the first not treated entity
            EntityAndOnLane firstElement = stack.pop();

            // If the entity is already in on his lane set
            if (firstElement.isAlreadyOnLane(firstElement.entity))
            {
                // There is interdependency
                // Add all lane to error
                Set<Entity> onLaneEntities = firstElement.getOnLaneEntities();
                for (Entity onLaneEntity : onLaneEntities)
                {
                    entitiesWithError.add(onLaneEntity);
                }
                // Stop process, it's not validated
                return false;
            }
            // Add this element to the on lane set
            firstElement.addEntityToOnLaneEntities(firstElement.entity);

            // Add this first entity to set of treated elements
            treatedEntities.add(firstElement.entity);

            // Add all linked output entities which have all input in treated set, and link to them the set
            Collection<Entity> outputEntities = firstElement.entity.getAllOutputEntitiesCollection();

            for (Entity outputEntity : outputEntities)
            {
                addNextTraitedElements(outputEntity, firstElement.getOnLaneEntities(), stack, treatedEntities);
            }
        }

        return true;
    }
}
