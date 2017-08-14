package org.eadge.gxscript.data.compile.script;

import org.eadge.gxscript.data.compile.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.compile.script.func.Func;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Debug compiled GXScript
 */
public class CompiledGXScript implements Serializable
{
    /**
     * Holds the name of compiledGXScript
     */
    private String name = "";

    /**
     * Holds the group of compiledGXScript
     */
    private String group = "";

    /**
     * Keeps called functions in script
     */
    private Func calledFunctions[];

    /**
     * Keeps parameters used with functions
     */
    private FuncDataAddresses calledFunctionsParameters[];

    /**
     * Keeps classes for script's inputs
     */
    private Class inputsScriptClasses[];

    /**
     * Keeps classes for script's outputs
     */
    private Class outputsScriptClasses[];

    /**
     * Keeps names for script's inputs
     */
    private String inputsScriptNames[];

    /**
     * Keeps names for script's outputs
     */
    private String outputsScriptNames[];

    public CompiledGXScript(Collection<Class> inputsScriptClasses,
                            Collection<Class> outputsScriptClasses,
                            Collection<String> inputsScriptNames,
                            Collection<String> outputsScriptNames,
                            Collection<Func> calledFunctions,
                            Collection<FuncDataAddresses> calledFunctionsParameters)
    {
        assert (calledFunctions.size() == calledFunctionsParameters.size());

        this.inputsScriptClasses = new Class[inputsScriptClasses.size()];
        this.outputsScriptClasses = new Class[outputsScriptClasses.size()];
        this.calledFunctions = new Func[calledFunctions.size()];
        this.calledFunctionsParameters = new FuncDataAddresses[calledFunctions.size()];
        this.inputsScriptNames = new String[inputsScriptNames.size()];
        this.outputsScriptNames = new String[outputsScriptNames.size()];

        inputsScriptClasses.toArray(this.inputsScriptClasses);
        outputsScriptClasses.toArray(this.outputsScriptClasses);
        calledFunctions.toArray(this.calledFunctions);
        calledFunctionsParameters.toArray(this.calledFunctionsParameters);
        inputsScriptNames.toArray(this.inputsScriptNames);
        outputsScriptNames.toArray(this.outputsScriptNames);
    }

    public String[] getInputsScriptNames()
    {
        return inputsScriptNames;
    }

    public String[] getOutputsScriptNames()
    {
        return outputsScriptNames;
    }

    public Class[] getInputsScriptClasses()
    {
        return inputsScriptClasses;
    }

    public Class[] getOutputsScriptClasses()
    {
        return outputsScriptClasses;
    }

    public int getNumberOfScriptInputs()
    {
        return inputsScriptClasses.length;
    }

    public int getNumberOfScriptOutputs()
    {
        return outputsScriptClasses.length;
    }

    public Func[] getCalledFunctions()
    {
        return calledFunctions;
    }

    public FuncDataAddresses[] getCalledFunctionsAddresses()
    {
        return calledFunctionsParameters;
    }

    public int getNumberOfFuncs()
    {
        return calledFunctions.length;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }
}
