package edu.upenn.cis.cis455.m1.server.interfaces;

import edu.upenn.cis.cis455.m1.server.HttpIoHandler;
import org.apache.logging.log4j.Level;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpHandlerTest {

    @Before
    public void setUp() {
        org.apache.logging.log4j.core.config.Configurator.setLevel("edu.upenn.cis.cis455", Level.DEBUG);
    }

    @Test
    public void testforgetHeaders()
    {
        Map<String,String> testHeaders = new HashMap<>();
        testHeaders.put("Connection","close");
        Assert.assertTrue(HttpIoHandler.getHeadersString(testHeaders).equalsIgnoreCase("Connection:close\r\n"));
    }

    }

