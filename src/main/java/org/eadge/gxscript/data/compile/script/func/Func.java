package org.eadge.gxscript.data.compile.script.func;

import org.eadge.gxscript.data.compile.program.Program;

import java.io.Serializable;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Abstract class for funcs
 */
public abstract class Func implements Serializable
{
    /**
     * Run script
     * @param program containing all called funcs, pointer on the current read funcs and memory stack
     */
    public abstract void run(Program program);
}
