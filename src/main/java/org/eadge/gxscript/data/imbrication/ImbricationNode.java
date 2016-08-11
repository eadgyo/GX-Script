package org.eadge.gxscript.data.imbrication;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.imbrication.StartImbricationEntity;
import org.eadge.gxscript.tools.Tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eadgyo on 04/08/16.
 *
 * Node used for creating imbrication tree
 */
public class ImbricationNode
{
    /**
     * Parent node of imbricationNode
     */
    protected ImbricationNode parent;

    /**
     * Children of the imbrication node
     */
    protected Set<ImbricationNode> children = new HashSet<>();

    /**
     * Entity starting the imbrication
     */
    protected StartImbricationEntity startImbricationEntity;

    /**
     * Holds index of imbrication output
     */
    protected int imbricationOutputIndex;

    /**
     * All elements in imbrication
     */
    protected Set<Entity> allElements;

    /**
     * Already processed entities at this level
     */
    protected Set<Entity> alreadyTreatedEntities = new HashSet<>();

    /**
     * Entities ready to be processed
     */
    protected Set<Entity> toBeTreatedEntities = new HashSet<>();

    public ImbricationNode(int index, StartImbricationEntity startImbricationEntity, Set<Entity> allElements)
    {
        this.imbricationOutputIndex = index;
        this.startImbricationEntity = startImbricationEntity;
        this.allElements = allElements;
    }

    public ImbricationNode(Set<Entity> allElements)
    {
        this.startImbricationEntity = null;
        this.allElements = allElements;
    }

    public ImbricationNode(Collection<Entity> allElements)
    {
        this.startImbricationEntity = null;
        this.allElements = new HashSet<>(allElements);
    }

    /**
     * Create one level of imbrication, add it to this level and return the created imbrication.
     *
     * @param imbricationIndex imbrication index
     * @param startImbricationEntity entity starting imbrication
     * @param inImbricationEntities  entities imbricated in the startImbricationEntity imbrication
     *
     * @return create imbricationNode
     */
    public ImbricationNode pushImbrication(int imbricationIndex,
                                           StartImbricationEntity startImbricationEntity,
                                           Set<Entity> inImbricationEntities)
    {
        // Create imbrication
        ImbricationNode child = new ImbricationNode(imbricationIndex, this.startImbricationEntity, inImbricationEntities);

        // Push imbrication
        addChild(child);

        return child;
    }

    /**
     * Add all imbricated outputs from the start entity imbrication, which have all inputs treated to the to be treated
     * stack
     *
     * @param index imbrication index
     */
    public void addImbricatedOutputsWithInputsTreated(int index)
    {
        // Check all outputs entities imbricated of starting block, add those who have all entries treated
        Collection<Entity> imbricatedOutputs = startImbricationEntity.getImbricatedOutputs(index);

        for (Entity imbricatedOutput : imbricatedOutputs)
        {
            // If all inputs have benn treated
            if (hasAllInputsTreatedAtLevelOrLower(imbricatedOutput))
            {
                addToBeTreated(imbricatedOutput);
            }
        }
    }

    /**
     * Add all not imbricated outputs from the start entity imbrication, which have all inputs treated to the to be
     * treated stack
     */
    public void addNotImbricatedOutputsWithInputsTreated(StartImbricationEntity startImbricationEntity)
    {
        // Check all outputs entities not imbricated of starting block, add those who have all entries treated
        Collection<Entity> notImbricatedOutputs = startImbricationEntity.getNotImbricatedOutputs();

        for (Entity notImbricatedOutput : notImbricatedOutputs)
        {
            // If all inputs have been treated
            if (hasAllInputsTreatedAtLevelOrLower(notImbricatedOutput))
            {
                addToBeTreated(notImbricatedOutput);
            }
        }
    }

    /**
     * Add all not outputs from the start entity imbrication, which have all inputs treated to the to be
     * treated stack
     *
     * @param entity checked entity
     */
    public void addOutputsWithInputsTreated(Entity entity)
    {
        // Check all outputs entities of this block, add those who have all entries treated
        Collection<Entity> outputEntities = entity.getAllOutputEntitiesCollection();

        for (Entity outputEntity : outputEntities)
        {
            // If all inputs have been treated
            if (hasAllInputsTreated(entity))
            {
                addToBeTreated(outputEntity);
            }
        }
    }

    public int getImbricationOutputIndex()
    {
        return imbricationOutputIndex;
    }

    /**
     * End this imbrication level and update to be treated not imbricated entities
     */
    public void endImbrication(ImbricationNode child)
    {
        // Remove imbrication and all elements in imbrications
        removeImbricationAndAllElementsInImbrication(child);

        StartImbricationEntity startImbricationEntity = child.getStartImbricationEntity();
        
        // If all parallels imbrications have been treated
        if (child.getImbricationOutputIndex() == startImbricationEntity.getNumberOfParallelsImbrications() - 1)
        {
            // Add this start imbrication entity as treated
            addAlreadyTreated(startImbricationEntity);

            // Add all NOT imbricated output entities of starting imbrication entiy which have all their
            // input block treated in lower or equal imbrication level
            addNotImbricatedOutputsWithInputsTreated(startImbricationEntity);
        }
        else
        {
            // Start next parallel imbrication
            startImbricationNode(startImbricationEntity, child.getImbricationOutputIndex() + 1);
        }
    }

    /**
     * Remove imbrication and all elements in imbrication
     * @param child removed imbrication
     */
    public void removeImbricationAndAllElementsInImbrication(ImbricationNode child)
    {
        // Remove this imbrication to the lower one
        removeChild(child);

        // All elements are removed from the parents nodes, they doesn't exist after the end of imbrication
        child.removeElementsRecursive(child.getAllElements());
    }

    /**
     * Remove from parents all elements
     *
     * @param allElements removed elements
     */
    private void removeElementsRecursive(Collection<Entity> allElements)
    {
        for (Entity entity : allElements)
        {
            removeElementRecursive(entity);
        }
    }

    /**
     * Remove from parents one entity
     *
     * @param entity removed entity
     */
    private void removeElementRecursive(Entity entity)
    {
        allElements.remove(entity);

        // If the node is not a root node
        if (parent != null)
        {
            // Remove this element from parent
            parent.removeElementRecursive(entity);
        }
    }

    public void treatStartImbricationEntity(StartImbricationEntity entity)
    {
        // Add this start imbrication entity as treated
        addAlreadyTreated(startImbricationEntity);

        // Add all NOT imbricated output entities of starting imbrication entiy which have all their
        // input block treated in lower or equal imbrication level
        addNotImbricatedOutputsWithInputsTreated(startImbricationEntity);
    }

    public void treatEntity(Entity entity)
    {
        // Add this entity to already treated
        addAlreadyTreated(entity);

        // Add all output entities which have all their input block treated in lower or equal imbrication
        // level
        addOutputsWithInputsTreated(entity);
    }

    // Recursive getter

    /**
     * Get the highest imbrication node for one entity
     *
     * @param entity used entity
     *
     * @return highest imbrication node or null if it does not contain entity
     */
    public ImbricationNode getHighestImbricationNode(Entity entity)
    {
        // If entity in this level or higher
        if (isInImbrication(entity))
        {
            for (ImbricationNode child : children)
            {
                // Get highest imbrication node from this child
                ImbricationNode highestImbricationNode = child.getHighestImbricationNode(entity);

                // If we found the highest Imbrication node
                if (highestImbricationNode != null)
                {
                    return highestImbricationNode;
                }
            }
            return this;
        }
        else
        {
            return null;
        }
    }

    /**
     * Check if one entity had been treated in actual or lower imbrication
     *
     * @param entity checked entity
     *
     * @return true if it had been treated in actual or lower imbrication
     */
    public boolean wasTreatedLowerRecursive(Entity entity)
    {
        if (wasTreated(entity))
        {
            return true;
        }
        else if (parent != null)
        {
            // Check if it was treated in lower imbrication
            return parent.wasTreatedLowerRecursive(entity);
        }
        return false;
    }

    /**
     * Check if one entity has all inputs treated in THIS actual or lower imbrication
     *
     * @param entity entity with checked entries
     *
     * @return true if all inputs had been treated in THIS actual or lower imbrication
     */
    public boolean hasAllInputsTreatedAtLevelOrLower(Entity entity)
    {
        // Check for each inputs
        for (int inputIndex = 0; inputIndex < entity.getNumberOfInputs(); inputIndex++)
        {
            // If it's not treated at an actual or lower level
            if (!wasTreatedLowerRecursive(entity.getInputEntity(inputIndex)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if one entity has all inputs treated in HIS actual or lower imbrication
     * HIS mean it will get entity higher imbrication node first
     *
     * @param entity entity with checked entries
     *
     * @return true if all inputs had been treated in HIS actual or lower imbrication
     */
    public boolean hasAllInputsTreated(Entity entity)
    {
        ImbricationNode highestImbricationNode = getHighestImbricationNode(entity);
        assert (highestImbricationNode != null);
        return highestImbricationNode.hasAllInputsTreatedAtLevelOrLower(entity);
    }


    // Getter
    public Set<Entity> getAllElements()
    {
        return allElements;
    }

    /**
     * Get the list of nodes with no children
     *
     * @return list of nodes with no children
     */
    public ArrayList<ImbricationNode> getLeaves()
    {
        ArrayList<ImbricationNode> leaves = new ArrayList<>();
        retrieveLeaves(leaves);
        return leaves;
    }

    /**
     * Fill the list with the nodes with no children
     *
     * @param leaves filled list with nodes with no children
     */
    public void retrieveLeaves(ArrayList<ImbricationNode> leaves)
    {
        if (isLeaf())
        {
            leaves.add(this);
        }
        else
        {
            // Get leaves from the children
            for (ImbricationNode child : children)
            {
                child.retrieveLeaves(leaves);
            }
        }
    }

    /**
     * Remove the next entity to be treated from to be treated collection
     *
     * @return next entity to be treated
     */
    public Entity popToBeTreated()
    {
        Entity next = toBeTreatedEntities.iterator().next();
        removeToBeTreated(next);

        return next;
    }

    public void removeToBeTreated(Entity entity)
    {
        toBeTreatedEntities.remove(entity);
    }

    public StartImbricationEntity getStartImbricationEntity()
    {
        return startImbricationEntity;
    }

    public ImbricationNode getParent()
    {
        return parent;
    }

    public void setParent(ImbricationNode parent)
    {
        this.parent = parent;
    }

    public Set<ImbricationNode> getChildren()
    {
        return children;
    }

    public boolean isStartingEntity(Entity entity)
    {
        return startImbricationEntity == entity;
    }

    public boolean isInImbrication(Entity entity)
    {
        return allElements.contains(entity);
    }

    public boolean wasTreated(Entity entity)
    {
        // Check if Entity has already been processed Or entity is the start entity, and we know that Start entity
        // has already been processed at this level
        return alreadyTreatedEntities.contains(entity) || startImbricationEntity == entity;
    }

    public boolean isGoingToBeTreated(Entity entity)
    {
        return toBeTreatedEntities.contains(entity);
    }

    public boolean hasChild()
    {
        return children.size() != 0;
    }

    public boolean isLeaf()
    {
        return children.size() == 0;
    }

    // Setter

    /**
     * Check if all blocks are already been treated
     *
     * @return true if blocks have all been treated, false otherwise
     */
    public boolean hasFinishedProcess()
    {
        return alreadyTreatedEntities.size() == allElements.size();
    }

    /**
     * Check if to be treated collection is empty
     *
     * @return true if to be treated collection is empty, false otherwise
     */
    public boolean isToBeTreatedEmpty()
    {
        return toBeTreatedEntities.size() == 0;
    }

    public void addChild(ImbricationNode child)
    {
        this.children.add(child);
        child.setParent(this);
    }

    public void addToBeTreated(Entity entity)
    {
        toBeTreatedEntities.add(entity);
    }

    public void addAllToBeTreated(Collection<Entity> entities)
    {
        toBeTreatedEntities.addAll(entities);
    }

    public void addAlreadyTreated(Entity entity)
    {
        alreadyTreatedEntities.add(entity);
    }

    public void addAllAlreadyTreated(Collection<Entity> entities)
    {
        alreadyTreatedEntities.addAll(entities);
    }

    public void removeChild(ImbricationNode child)
    {
        this.children.remove(child);
        child.setParent(null);
    }


    /**
     * Get the first entity not in lower or equal level of imbrication, or null if all of them are in lower or equal
     * level of imbrication
     *
     * @param entity checked inputs entity
     *
     * @return the first entity not in lower or equal level of imbrication, or null if all of them are in lower or equal
     * level of imbrication
     */
    public Entity getInputNotInLowerOrEqualLevel(Entity entity)
    {
        // Get all input entities
        Collection<Entity> allInputEntities = entity.getAllInputEntities();

        // Search for all input entities
        for (Entity inputEntity : allInputEntities)
        {
            if (!isInLowerOrEqualLevel(entity))
            {
                return inputEntity;
            }
        }

        return null;
    }

    /**
     * Check if all inputs of an entity are in his level or in a lower level
     *
     * @param entity checked input entity
     *
     * @return true if they are all in lower or equal level of imbrication, false if they are not all
     */
    public boolean hasAllInputInLowerOrEqualLevel(Entity entity)
    {
        // Get all input entities
        Collection<Entity> allInputEntities = entity.getAllInputEntities();

        // Search for all input entities
        for (Entity inputEntity : allInputEntities)
        {
            if (!isInLowerOrEqualLevel(inputEntity))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if one entity is in higher imbrication node
     * We don't need to check all highers imbrication nodes, just the first
     * As all parents imbrication nodes have their children.
     *
     * @param entity checked entity
     *
     * @return true if the entity is present in a higher level of imbrication, false otherwise.
     */
    public boolean isInHigherImbricationNode(Entity entity)
    {
        for (ImbricationNode child : children)
        {
            if (child.isInImbrication(entity))
                return true;
        }
        return false;
    }

    /**
     * Check if one entity (higheest level of imbrication) is in lower or equal level of imbrication
     *
     * @param entity checked entity
     *
     * @return true if it is in a lower or equal level of imbrication, false otherwise
     */
    public boolean isInLowerOrEqualLevel(Entity entity)
    {
        // If the entity is in this level of imbrication
        if (isInImbrication(entity))
        {
            // Return true if this level is his highest level of imbrication, false otherwise
            return !isInHigherImbricationNode(entity);
        }
        else if (parent != null) // Else if this node is not a root
        {
            // Search for lower imbrication node
            return isInLowerOrEqualLevel(entity);
        }
        else
        {
            // We didn't found the entity in a lower or equal level
            return false;
        }
    }

    /**
     * Start a new imbrication from start imbrication entity
     *
     * @param startImbricationEntity entity starting imbrication
     */
    public void startImbricationNode(StartImbricationEntity startImbricationEntity)
    {
        startImbricationNode(startImbricationEntity, 0);
    }

    /**
     * Start a new imbrication from start imbrication entity
     *
     * @param startImbricationEntity entity starting imbrication
     * @param index                  imbrication index
     */
    public void startImbricationNode(StartImbricationEntity startImbricationEntity, int index)
    {
        // Get all imbricated entities from this start imbrication entity
        Set<Entity> inImbricationEntities = Tools.getInImbricationEntities(startImbricationEntity, index);

        // Push one level of imbrication in actual entity's imbrication and add imbricated entities from
        // the startImbricationEntity.
        ImbricationNode pushedImbrication = pushImbrication(index, startImbricationEntity, inImbricationEntities);

        // Add all imbricated output entities which have all their input block treated in lower or equal
        // imbrication level.
        pushedImbrication.addImbricatedOutputsWithInputsTreated(index);
    }
}
