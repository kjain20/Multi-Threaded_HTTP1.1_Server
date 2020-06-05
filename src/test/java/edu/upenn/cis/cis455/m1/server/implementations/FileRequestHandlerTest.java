package edu.upenn.cis.cis455.m1.server.implementations;

import edu.upenn.cis.cis455.m1.server.interfaces.HttpRequestHandler;
import org.apache.logging.log4j.Level;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;


public class FileRequestHandlerTest {
    FileRequestHandler testObject =  new FileRequestHandler(null, null);

    @Before
    public void setUp() {
        org.apache.logging.log4j.core.config.Configurator.setLevel("edu.upenn.cis.cis455", Level.DEBUG);
    }

    @Test
    public void testgetResponseString() throws IOException
    {
//        String static_file_location = "/www";
//        String resourceFile = new File(".").getCanonicalPath() + static_file_location +"/folder/demo.txt";
//        String expectedOutput = "This is the demo file\n";
//        String outputString = testObject.getResponseString(resourceFile);
//        Assert.assertTrue(outputString.equalsIgnoreCase(expectedOutput));
    }

    }
