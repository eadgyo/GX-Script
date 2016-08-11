package org.eadge.gxscript.testing;

/**
 * Created by eadgyo on 11/08/16.
 *
 */
public class PrintTest
{
    public static void printResult(boolean result, String job)
    {
        System.out.println(convertResult(result) + "  " + job);
    }

    public static String convertResult(boolean result)
    {
        if (result)
            return "[  OK  ]";
        else
            return "[ FAIL ]";
    }

}
