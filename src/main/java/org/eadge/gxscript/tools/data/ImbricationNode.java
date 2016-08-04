package org.eadge.gxscript.tools.data;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.imbricate.ImbricatedEntity;

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
    private ImbricationNode parent;

    /**
     * Children of the imbrication node
     */
    private Set<ImbricationNode> children = new HashSet<>();

    /**
     * Entity starting the imbrication
     */
    private ImbricatedEntity startEntity;

    /**
     * All elements in imbrication
     */
    private Set<Entity> allElements;

    /**
     * Already processed entities at this level
     */
    private Set<Entity> alreadyTreatedEntities = new HashSet<>();
    ;

    /**
     * Entities ready to be processed
     */
    private Set<Entity> toBeTreatedEntities = new HashSet<>();
    ;

    public ImbricationNode(ImbricatedEntity startEntity, Set<Entity> allElements)
    {
        this.startEntity = startEntity;
        this.allElements = allElements;
    }

    public ImbricationNode(Set<Entity> allElements)
    {
        this.startEntity = null;
        this.allElements = allElements;
    }

    /**
     * Create one level of imbrication, add it to this level and return the created imbrication.
     *
     * @param startEntity     entity starting imbrication
     * @param isInImbrication list of elements in imbrication
     *
     * @return create imbricationNode
     */
    public ImbricationNode pushImbrication(ImbricatedEntity startEntity, Set<Entity> isInImbrication)
    {
        // Create imbrication
        ImbricationNode child = new ImbricationNode(startEntity, isInImbrication);

        // Push imbrication
        addChild(child);

        // Check all outputs entities imbricated of starting block, add those who have all entries treated
        Collection<Entity> imbricatedOutputs = startEntity.getImbricatedOutputs();

        for (Entity imbricatedOutput : imbricatedOutputs)
        {
            // If all inputs have benn treated
            if (child.hasAllInputsTreatedAtLevelOrLower(imbricatedOutput))
            {
                child.addToBeTreated(imbricatedOutput);
            }
        }

        return child;
    }

    /**
     * Pour down contents of the actual imbrication node to the lower one
     */
    public void endImbrication()
    {
        assert (parent != null);

        // Add starting imbrication entity to the already treated of lower imbrication
        parent.addAlreadyTreated(startEntity);

        // Remove this imbrication to the lower one
        parent.removeChild(this);

        // Check all outputs entities not imbricated of starting block, add those who have all entries treated
        Collection<Entity> notImbricatedOutputs = startEntity.getNotImbricatedOutputs();

        for (Entity notImbricatedOutput : notImbricatedOutputs)
        {
            // If all inputs have been treated
            if (parent.hasAllInputsTreatedAtLevelOrLower(notImbricatedOutput))
            {
                parent.addToBeTreated(notImbricatedOutput);
            }
        }

        // Add treated all imbricated elements in the parent list of elements
        parent.addAllAlreadyTreated(getAllElements());
    }

    public void treatEntity(Entity entity)
    {
        // Add this entity to already treated
        addAlreadyTreated(entity);

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
        for (int inputIndex = 0; inputIndex < entity.getNumberOfInput(); inputIndex++)
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

    public Entity getStartEntity()
    {
        return startEntity;
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
        return startEntity == entity;
    }

    public boolean isInImbrication(Entity entity)
    {
        return allElements.contains(entity);
    }

    public boolean wasTreated(Entity entity)
    {
        // Check if Entity has already been processed Or entity is the start entity, and we know that Start entity
        // has already been processed at this level
        return alreadyTreatedEntities.contains(entity) || startEntity == entity;
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
}
