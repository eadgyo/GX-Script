package org.eadge.gxscript.data.script;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Abstract class for funcs
 */
public abstract class Func
{
    /**
     * Run script
     * @param program containing all called funcs, pointer on the current read funcs and memory stack
     */
    public abstract void run(Program program);
}
