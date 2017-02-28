package org.eadge.gxscript.data.imbrication;

import org.eadge.gxscript.data.entity.GXEntity;
import org.eadge.gxscript.data.entity.StartImbricationGXEntity;
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
     * GXEntity starting the imbrication
     */
    protected StartImbricationGXEntity startImbricationEntity;

    /**
     * Holds index of imbrication output
     */
    protected int imbricationOutputIndex;

    /**
     * All elements in imbrication
     */
    protected Set<GXEntity> allElements;

    /**
     * Already processed entities at this level
     */
    protected Set<GXEntity> alreadyTreatedEntities = new HashSet<>();

    /**
     * Entities ready to be processed
     */
    protected Set<GXEntity> toBeTreatedEntities = new HashSet<>();

    public ImbricationNode(int index, StartImbricationGXEntity startImbricationEntity, Set<GXEntity> allElements)
    {
        this.imbricationOutputIndex = index;
        this.startImbricationEntity = startImbricationEntity;
        this.allElements = allElements;
    }

    public ImbricationNode(Set<GXEntity> allElements)
    {
        this.startImbricationEntity = null;
        this.allElements = allElements;
    }

    public ImbricationNode(Collection<GXEntity> allElements)
    {
        this.startImbricationEntity = null;
        this.allElements = new HashSet<>(allElements);
    }

    /**
     * Create one level of imbrication, add it to this level and return the created imbrication.
     *
     * @param imbricationIndex imbrication index
     * @param startImbricationEntity GXEntity starting imbrication
     * @param inImbricationEntities  entities imbricated in the startImbricationEntity imbrication
     *
     * @return create imbricationNode
     */
    public ImbricationNode pushImbrication(int imbricationIndex,
                                           StartImbricationGXEntity startImbricationEntity,
                                           Set<GXEntity> inImbricationEntities)
    {
        // Create imbrication
        ImbricationNode child = createImbrication(imbricationIndex, startImbricationEntity, inImbricationEntities);

        // Push imbrication
        addChild(child);

        return child;
    }

    protected ImbricationNode createImbrication(int imbricationIndex,
                                                 StartImbricationGXEntity startImbricationEntity,
                                                 Set<GXEntity> inImbricationEntities)
    {
        return new ImbricationNode(imbricationIndex, startImbricationEntity, inImbricationEntities);
    }

    /**
     * Add all imbricated outputs from the start GXEntity imbrication, which have all inputs treated to the to be treated
     * stack
     *
     * @param index imbrication index
     */
    public void addImbricatedOutputsWithInputsTreated(int index)
    {
        // Check all outputs entities imbricated of starting block, add those who have all entries treated
        Collection<GXEntity> imbricatedOutputs = startImbricationEntity.getImbricatedOutputs(index);

        for (GXEntity imbricatedOutput : imbricatedOutputs)
        {
            // If all inputs have benn treated
            if (hasAllInputsTreatedAtLevelOrLower(imbricatedOutput))
            {
                addToBeTreated(imbricatedOutput);
            }
        }
    }

    /**
     * Add all not imbricated outputs from the start GXEntity imbrication, which have all inputs treated to the to be
     * treated stack
     */
    public void addNotImbricatedOutputsWithInputsTreated(StartImbricationGXEntity startImbricationEntity)
    {
        // Check all outputs entities not imbricated of starting block, add those who have all entries treated
        Collection<GXEntity> notImbricatedOutputs = startImbricationEntity.getNotImbricatedOutputs();

        for (GXEntity notImbricatedOutput : notImbricatedOutputs)
        {
            // If all inputs have been treated
            if (hasAllInputsTreatedAtLevelOrLower(notImbricatedOutput))
            {
                addToBeTreated(notImbricatedOutput);
            }
        }
    }

    /**
     * Add all not outputs from the start GXEntity imbrication, which have all inputs treated to the to be
     * treated stack
     *
     * @param GXEntity checked GXEntity
     */
    public void addOutputsWithInputsTreated(GXEntity GXEntity)
    {
        // Check all outputs entities of this block, add those who have all entries treated
        Collection<GXEntity> outputEntities = GXEntity.getAllOutputEntitiesCollection();

        for (GXEntity outputGXEntity : outputEntities)
        {
            // If all inputs have been treated
            if (hasAllInputsTreated(outputGXEntity))
            {
                addToBeTreated(outputGXEntity);
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

        StartImbricationGXEntity startImbricationEntity = child.getStartImbricationEntity();
        
        // If all parallels imbrications have NOT been treated
        if (child.getImbricationOutputIndex() != startImbricationEntity.getNumberOfParallelsImbrications() - 1)
        {
            // Start next parallel imbrication
            startImbricationNode(startImbricationEntity, child.getImbricationOutputIndex() + 1);
        }
        else
        {
            // Add this start imbrication GXEntity as treated
            addAlreadyTreated(startImbricationEntity);

            // Add all NOT imbricated output entities of starting imbrication entiy which have all their
            // function block treated in lower or equal imbrication level
            addNotImbricatedOutputsWithInputsTreated(startImbricationEntity);
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
        removeElementsRecursive(child.getAllElements());
    }

    /**
     * Remove from parents all elements
     *
     * @param allElements removed elements
     */
    private void removeElementsRecursive(Collection<GXEntity> allElements)
    {
        for (GXEntity GXEntity : allElements)
        {
            removeElementRecursive(GXEntity);
        }
    }

    /**
     * Remove from parents one GXEntity
     *
     * @param GXEntity removed GXEntity
     */
    private void removeElementRecursive(GXEntity GXEntity)
    {
        allElements.remove(GXEntity);

        // If the node is not a root node
        if (parent != null)
        {
            // Remove this element from parent
            parent.removeElementRecursive(GXEntity);
        }
    }

    public void treatStartImbricationEntity(StartImbricationGXEntity startImbricationEntity)
    {
        // Add this start imbrication GXEntity as treated
        addAlreadyTreated(startImbricationEntity);

        // Add all NOT imbricated output entities of starting imbrication entiy which have all their
        // function block treated in lower or equal imbrication level
        addNotImbricatedOutputsWithInputsTreated(startImbricationEntity);
    }

    public void treatEntity(GXEntity GXEntity)
    {
        // Add this GXEntity to already treated
        addAlreadyTreated(GXEntity);

        // Add all output entities which have all their function block treated in lower or equal imbrication
        // level
        addOutputsWithInputsTreated(GXEntity);
    }

    // Recursive getter

    /**
     * Get the highest imbrication node for one GXEntity
     *
     * @param GXEntity used GXEntity
     *
     * @return highest imbrication node or null if it does not contain GXEntity
     */
    public ImbricationNode getHighestImbricationNode(GXEntity GXEntity)
    {
        // If GXEntity in this level or higher
        if (isInImbrication(GXEntity))
        {
            for (ImbricationNode child : children)
            {
                // Get highest imbrication node from this child
                ImbricationNode highestImbricationNode = child.getHighestImbricationNode(GXEntity);

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
     * Check if one GXEntity had been treated in actual or lower imbrication
     *
     * @param GXEntity checked GXEntity
     *
     * @return true if it had been treated in actual or lower imbrication
     */
    public boolean wasTreatedLowerRecursive(GXEntity GXEntity)
    {
        if (wasTreated(GXEntity))
        {
            return true;
        }
        else if (parent != null)
        {
            // Check if it was treated in lower imbrication
            return parent.wasTreatedLowerRecursive(GXEntity);
        }
        return false;
    }

    /**
     * Check if one GXEntity has all inputs treated in THIS actual or lower imbrication
     *
     * @param GXEntity GXEntity with checked entries
     *
     * @return true if all inputs had been treated in THIS actual or lower imbrication
     */
    public boolean hasAllInputsTreatedAtLevelOrLower(GXEntity GXEntity)
    {
        // Check for each inputs
        for (int inputIndex = 0; inputIndex < GXEntity.getNumberOfInputs(); inputIndex++)
        {
            // If it's not treated at an actual or lower level
            if (!wasTreatedLowerRecursive(GXEntity.getInputEntity(inputIndex)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if one GXEntity has all inputs treated in HIS actual or lower imbrication
     * HIS mean it will get GXEntity higher imbrication node first
     *
     * @param GXEntity GXEntity with checked entries
     *
     * @return true if all inputs had been treated in HIS actual or lower imbrication
     */
    public boolean hasAllInputsTreated(GXEntity GXEntity)
    {
        ImbricationNode highestImbricationNode = getHighestImbricationNode(GXEntity);
        assert (highestImbricationNode != null);
        return highestImbricationNode.hasAllInputsTreatedAtLevelOrLower(GXEntity);
    }


    // Getter
    public Set<GXEntity> getAllElements()
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
     * Remove the next GXEntity to be treated from to be treated collection
     *
     * @return next GXEntity to be treated
     */
    public GXEntity popToBeTreated()
    {
        if (toBeTreatedEntities.size() != 0)
        {
            // Remove the first element
            GXEntity next = toBeTreatedEntities.iterator().next();
            removeToBeTreated(next);

            return next;
        }
        else
        {
            // Search in children
            for (ImbricationNode child : children)
            {
                GXEntity GXEntity = child.popToBeTreated();

                if (GXEntity != null)
                    return GXEntity;
            }
        }

        return null;
    }

    public void removeToBeTreated(GXEntity GXEntity)
    {
        toBeTreatedEntities.remove(GXEntity);
    }

    public StartImbricationGXEntity getStartImbricationEntity()
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

    public boolean isStartingEntity(GXEntity GXEntity)
    {
        return startImbricationEntity == GXEntity;
    }

    public boolean isInImbrication(GXEntity GXEntity)
    {
        return allElements.contains(GXEntity);
    }

    public boolean wasTreated(GXEntity GXEntity)
    {
        // Check if GXEntity has already been processed Or GXEntity is the start GXEntity, and we know that Start GXEntity
        // has already been processed at this level
        return alreadyTreatedEntities.contains(GXEntity) || startImbricationEntity == GXEntity;
    }

    public boolean isGoingToBeTreated(GXEntity GXEntity)
    {
        return toBeTreatedEntities.contains(GXEntity);
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
        if (toBeTreatedEntities.size() == 0)
        {
            // Search in children
            for (ImbricationNode child : children)
            {
                if (!child.isToBeTreatedEmpty())
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public void addChild(ImbricationNode child)
    {
        this.children.add(child);
        child.setParent(this);
    }

    public void addToBeTreated(GXEntity GXEntity)
    {
        toBeTreatedEntities.add(GXEntity);
    }

    public void addAllToBeTreated(Collection<GXEntity> entities)
    {
        toBeTreatedEntities.addAll(entities);
    }

    public void addAlreadyTreated(GXEntity GXEntity)
    {
        alreadyTreatedEntities.add(GXEntity);
    }

    public void addAllAlreadyTreated(Collection<GXEntity> entities)
    {
        alreadyTreatedEntities.addAll(entities);
    }

    public void removeChild(ImbricationNode child)
    {
        this.children.remove(child);
        child.setParent(null);
    }

    /**
     * Get the first GXEntity not in lower or equal level of imbrication, or null if all of them are in lower or equal
     * level of imbrication
     *
     * @param GXEntity checked inputs GXEntity
     *
     * @return the first GXEntity not in lower or equal level of imbrication, or null if all of them are in lower or equal
     * level of imbrication
     */
    public GXEntity getInputNotInLowerOrEqualLevel(GXEntity GXEntity)
    {
        // Get all function entities
        Collection<GXEntity> allInputEntities = GXEntity.getAllInputEntities();

        // Search for all function entities
        for (GXEntity inputGXEntity : allInputEntities)
        {
            if (!isInLowerOrEqualLevel(GXEntity))
            {
                return inputGXEntity;
            }
        }

        return null;
    }

    /**
     * Check if all inputs of an GXEntity are in his level or in a lower level
     *
     * @param GXEntity checked function GXEntity
     *
     * @return true if they are all in lower or equal level of imbrication, false if they are not all
     */
    public boolean hasAllInputInLowerOrEqualLevel(GXEntity GXEntity)
    {
        // Get all function entities
        Collection<GXEntity> allInputEntities = GXEntity.getAllInputEntities();

        // Search for all function entities
        for (GXEntity inputGXEntity : allInputEntities)
        {
            if (!isInLowerOrEqualLevel(inputGXEntity))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if one GXEntity is in higher imbrication node
     * We don't need to check all highers imbrication nodes, just the first
     * As all parents imbrication nodes have their children.
     *
     * @param GXEntity checked GXEntity
     *
     * @return true if the GXEntity is present in a higher level of imbrication, false otherwise.
     */
    public boolean isInHigherImbricationNode(GXEntity GXEntity)
    {
        for (ImbricationNode child : children)
        {
            if (child.isInImbrication(GXEntity))
                return true;
        }
        return false;
    }

    /**
     * Check if one GXEntity (higheest level of imbrication) is in lower or equal level of imbrication
     *
     * @param GXEntity checked GXEntity
     *
     * @return true if it is in a lower or equal level of imbrication, false otherwise
     */
    public boolean isInLowerOrEqualLevel(GXEntity GXEntity)
    {
        // If the GXEntity is in this level of imbrication
        if (isInImbrication(GXEntity))
        {
            // Return true if this level is his highest level of imbrication, false otherwise
            return !isInHigherImbricationNode(GXEntity);
        }
        else if (parent != null) // Else if this node is not a root
        {
            // Search for lower imbrication node
            return isInLowerOrEqualLevel(GXEntity);
        }
        else
        {
            // We didn't found the GXEntity in a lower or equal level
            return false;
        }
    }

    /**
     * Start a new imbrication from start imbrication GXEntity
     *
     * @param startImbricationEntity GXEntity starting imbrication
     */
    public void startImbricationNode(StartImbricationGXEntity startImbricationEntity)
    {
        startImbricationNode(startImbricationEntity, 0);
    }

    /**
     * Start a new imbrication from start imbrication GXEntity
     *
     * @param startImbricationEntity GXEntity starting imbrication
     * @param index                  imbrication index
     */
    public void startImbricationNode(StartImbricationGXEntity startImbricationEntity, int index)
    {
        // Get all imbricated entities from this start imbrication GXEntity
        Set<GXEntity> inImbricationEntities = Tools.getInImbricationEntities(startImbricationEntity, index);

        // Push one level of imbrication in actual GXEntity's imbrication and add imbricated entities from
        // the startImbricationEntity.
        ImbricationNode pushedImbrication = pushImbrication(index, startImbricationEntity, inImbricationEntities);

        // Add all imbricated output entities which have all their function block treated in lower or equal
        // imbrication level.
        pushedImbrication.addImbricatedOutputsWithInputsTreated(index);
    }

    /**
     * Get all not treated elements
     * @return all not treated elements
     */
    public Collection<? extends GXEntity> getAllNotTreatedElements()
    {
        Set<GXEntity> allNotTreatedElements = new HashSet<>(this.allElements);
        allNotTreatedElements.removeAll(this.alreadyTreatedEntities);

        return allNotTreatedElements;
    }
}
