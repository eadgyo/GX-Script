package org.eadge.gxscript.testing.validator;

import java.io.IOException;

/**
 * Created by eadgyo on 13/08/16.
 *
 * Test all
 */
public class TestAll
{
    public static void main(String[] args) throws IOException
    {
        TestValidateHaveInput.main(args);
        System.out.println();

        TestValidateLinks.main(args);
        System.out.println();

        TestValidateNoInterdependency.main(args);
        System.out.println();

        TestValidateImbrication.main(args);
        System.out.println();
    }

}
