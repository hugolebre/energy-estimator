package com.lebre;

import java.io.*;

import org.junit.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    final String[] testString = {"1544206562 TurnOff", "1544206563 Delta +0.5", "1544210163 TurnOff"};
    final String[] testString2 = {"1544206562 TurnOff", "1544206563 Delta +0.5", "1544210163 TurnOff"};

    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);

    }

    private String getOutput() {
        return testOut.toString();
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Test
    public void testCase1() {
        
        for(int i = 0; i < testString.length ; i++)
        {
            provideInput(testString[i]);
            App.main(new String[0]);
            String tmp = getOutput();
            System.out.println("App test: " + tmp);
    }
}

}
