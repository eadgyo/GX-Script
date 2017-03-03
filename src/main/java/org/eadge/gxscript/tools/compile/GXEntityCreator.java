package org.eadge.gxscript.tools.compile;

import org.eadge.gxscript.data.entity.DefaultGXEntity;
import org.eadge.gxscript.data.entity.GXEntity;
import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.address.FuncAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.script.address.OutputAddresses;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by eadgyo on 28/02/17.
 *
 * Used to create GXEntity object from scripts
 */
public class GXEntityCreator
{
    /**
     * Create GXEntity from GXScript
     *
     * @param compiledGXScript used RawGXScript
     *
     * @return GXEntity from raw GXScript
     */
    public GXEntity createGXEntity(final CompiledGXScript compiledGXScript)
    {
        // Create the entity
        return new DefaultGXEntity()
        {
            GXEntity init(CompiledGXScript compiledGXScript)
            {
                Class[] classInputs = compiledGXScript.getInputsScriptClasses();
                Class[] classOutputs = compiledGXScript.getOutputsScriptClasses();

                int numberOfScriptOutputs = compiledGXScript.getNumberOfScriptOutputs();


                // Add used inputs
                for (int classIndex = 0; classIndex < classInputs.length; classIndex++)
                {
                    addInputEntry(classInputs[classIndex].getName(), classInputs[classIndex]);
                }

                // Add used outputs
                for (int classIndex = 0; classIndex < classOutputs.length; classIndex++)
                {
                    addOutputEntry(classOutputs[classIndex].getName(), classOutputs[classIndex]);
                }


                return this;
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
                        // Rserve outputs
                        program.reserve(numberOfScriptOutputs);

                        // Save this level
                        program.pushMemoryLevel();

                        // Start script
                        // --> Reserve inputs
                        program.reserve(numberOfScriptInputs);

                        // Compute func address
                        FuncAddress currentFuncAddress = program.getCurrentFuncAddress();
                        program.runFromAndUntil(currentFuncAddress.addOffset(1), currentFuncAddress.addOffset(numberOfFuncs + 1));

                        // Unpush level
                        program.popMemoryLevel();
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
        }.init(compiledGXScript);
    }
}
