package org.eadge.gxscript.data.entity.model.script;

import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.entity.model.def.DefaultGXEntity;
import org.eadge.gxscript.data.compile.script.CompiledGXScript;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;
import org.eadge.gxscript.data.compile.script.address.FuncAddress;
import org.eadge.gxscript.data.compile.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.compile.script.address.OutputAddresses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by eadgyo on 03/03/17.
 *
 * Script entity to run compiledGXScript
 */
public class ScriptGXEntity extends DefaultGXEntity
{
    private CompiledGXScript compiledGXScript;

    public ScriptGXEntity(CompiledGXScript compiledGXScript)
    {
        this.compiledGXScript = compiledGXScript;

        Class[] classInputs = compiledGXScript.getInputsScriptClasses();
        Class[] classOutputs = compiledGXScript.getOutputsScriptClasses();
        String[] inputsNames = compiledGXScript.getInputsScriptNames();
        String[] outputNames = compiledGXScript.getOutputsScriptNames();

        // Add used inputs
        for (int i = 0; i < inputsNames.length; i++)
        {
            String inputName = inputsNames[i];
            Class classInput = classInputs[i];
            addInputEntry(inputName, classInput);
        }

        // Add used outputs
        for (int i = 0; i < outputNames.length; i++)
        {
            String outputName = outputNames[i];
            Class classInput = classOutputs[i];
            addOutputEntry(outputName, classInput);
        }
    }

    @Override
    public Func getFunc()
    {
        return new Func()
        {
            private int numberOfScriptInputs;
            private int numberOfScriptOutputs;
            private int numberOfFuncs;

            Func init()
            {
                numberOfScriptOutputs = getNumberOfOutputs();
                numberOfScriptInputs = getNumberOfInputs();
                numberOfFuncs = compiledGXScript.getNumberOfFuncs();
                return this;
            }

            @Override
            public void run(Program program)
            {
                Object[] objects = program.loadCurrentParametersObjects();

                // Reserve outputs
                program.reserve(numberOfScriptOutputs);

                // Save this level
                program.pushLevel();

                // Start script
                // --> Copy inputs
                for (Object object : objects)
                {
                    program.pushInMemory(object);
                }

                // Compute func address
                FuncAddress currentFuncAddress = program.getCurrentFuncAddress();
                program.runFromAndUntil(currentFuncAddress, currentFuncAddress.addOffset(numberOfFuncs));

                // Unpush level
                program.popLevel();
                program.setNextFuncAddress(new FuncAddress(numberOfFuncs));
            }
        }.init();
    }

    @Override
    public void pushEntityCode(ArrayList<Func> calledFunctions,
                               ArrayList<FuncDataAddresses> calledFunctionAddresses,
                               Map<GXEntity, OutputAddresses> addressesMap)
    {
        super.pushEntityCode(calledFunctions, calledFunctionAddresses, addressesMap);

        Func[] scriptCalledFunctions = compiledGXScript.getCalledFunctions();
        FuncDataAddresses[] scriptCalledFunctionsAddresses = compiledGXScript.getCalledFunctionsAddresses();

        for (int funcIndex = 0; funcIndex < scriptCalledFunctions.length; funcIndex++)
        {
            Func scriptCalledFunction = scriptCalledFunctions[funcIndex];
            FuncDataAddresses scriptCalledFunctionsAddress = scriptCalledFunctionsAddresses[funcIndex];

            calledFunctions.add(scriptCalledFunction);
            calledFunctionAddresses.add(scriptCalledFunctionsAddress);
        }
    }

    @Override
    public void treatNameAdding(List<String> names)
    {
        names.add(name);

        for (int i = 0; i < compiledGXScript.getNumberOfFuncs(); i++)
        {
            names.add(name);
        }
    }
}
