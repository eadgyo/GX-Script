package org.eadge.gxscript.tools.check.validator;

import org.eadge.gxscript.data.entity.GXEntity;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.tools.check.ValidatorModel;

import java.util.*;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Check if there are entities with interdependency. It's game be circle of interdependency or self-script-output.
 * For example, one GXEntity which have his script linked to his output is one case of interdependency.
 *
 */
public class ValidateNoInterdependency extends ValidatorModel
{
    /**
     * Holds GXEntity and his current on lane collection of entities
     */
    private class EntityAndOnLane
    {
        private GXEntity      GXEntity;
        private Set<GXEntity> onLaneEntities;

        public GXEntity getGXEntity()
        {
            return GXEntity;
        }

        EntityAndOnLane(GXEntity GXEntity)
        {
            this.GXEntity = GXEntity;
            this.onLaneEntities = new HashSet<>();
        }

        EntityAndOnLane(GXEntity GXEntity, Set<GXEntity> onLaneEntities)
        {
            this.GXEntity = GXEntity;
            this.onLaneEntities = onLaneEntities;
        }

        /**
         * Check if one GXEntity is already on lane
         * @param GXEntity checked GXEntity
         * @return true if it is already on lane, false otherwise
         */
        boolean isAlreadyOnLane(GXEntity GXEntity)
        {
            return onLaneEntities.contains(GXEntity);
        }

        Set<GXEntity> getOnLaneEntities()
        {
            return onLaneEntities;
        }

        /**
         * Add one GXEntity to the collection of on lanes entities
         * @param GXEntity added GXEntity
         */
        void addEntityToOnLaneEntities(GXEntity GXEntity)
        {
            onLaneEntities.add(GXEntity);
        }
    }

    private boolean areAllTreatedEntities(Collection<GXEntity> entities, Set<GXEntity> treatedEntities)
    {
        for (GXEntity inputGXEntity : entities)
        {
            if (inputGXEntity != null && !treatedEntities.contains(inputGXEntity))
            {
                return false;
            }
        }
        return true;
    }

    private void addNextTreatedElements(GXEntity testedGXEntity,
                                        Set<GXEntity> onLaneEntities,
                                        Stack<EntityAndOnLane> stack,
                                        Set<GXEntity> treatedEntities)
    {
        Collection<GXEntity> inputEntities = testedGXEntity.getAllInputEntities();

        // If all of the blocks script are treated
        if (areAllTreatedEntities(inputEntities, treatedEntities))
        {
            // Add not treated entities to the stack of next treated entities and clone his on lane entities
            EntityAndOnLane insertedEntityAndLane = new EntityAndOnLane(testedGXEntity, new HashSet<>(onLaneEntities));
            stack.add(insertedEntityAndLane);
        }
    }

    @Override
    public boolean validate(RawGXScript rawGXScript)
    {
        super.validate(rawGXScript);

        Collection<GXEntity> entities = rawGXScript.getEntities();

        Set<GXEntity> treatedEntities = new HashSet<>();

        // Get all starting entities
        Collection<GXEntity> startingEntities = rawGXScript.getStartingEntities();

        // Starting entities are added to stack of next treated entities
        // They are linked to a on lane set which contains all entities on lane
        Stack<EntityAndOnLane> stack = new Stack<>();

        for (GXEntity startingGXEntity : startingEntities)
        {
            // Create GXEntity and his on lane entities
            EntityAndOnLane notTreatedElement = new EntityAndOnLane(startingGXEntity);

            // Add it to stack
            stack.add(notTreatedElement);
        }

        // While there are still not treated entities in stack
        while (stack.size() != 0)
        {
            // Remove the first not treated GXEntity
            EntityAndOnLane firstElement = stack.pop();

            // If the GXEntity is already in on his lane set
            if (firstElement.isAlreadyOnLane(firstElement.GXEntity))
            {
                // There is interdependency
                // Add all lane to error
                Set<GXEntity> onLaneEntities = firstElement.getOnLaneEntities();
                for (GXEntity onLaneGXEntity : onLaneEntities)
                {
                    entitiesWithError.add(onLaneGXEntity);
                }
                // Stop process, it's not validated
                return false;
            }
            // Add this element to the on lane set
            firstElement.addEntityToOnLaneEntities(firstElement.GXEntity);

            // Add this first GXEntity to set of treated elements
            treatedEntities.add(firstElement.GXEntity);

            // Add all linked output entities which have all script in treated set, and link to them the set
            Collection<GXEntity> outputEntities = firstElement.GXEntity.getAllOutputEntitiesCollection();

            for (GXEntity outputGXEntity : outputEntities)
            {
                addNextTreatedElements(outputGXEntity, firstElement.getOnLaneEntities(), stack, treatedEntities);
            }
        }

        return treatedEntities.size() == rawGXScript.numberOfEntities();
    }
}
