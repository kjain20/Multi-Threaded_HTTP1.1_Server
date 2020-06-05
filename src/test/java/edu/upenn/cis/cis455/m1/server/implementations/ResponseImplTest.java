package edu.upenn.cis.cis455.m1.server.implementations;

import org.apache.logging.log4j.Level;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class ResponseImplTest {

    ResponseImpl response = new ResponseImpl();

    @Before
    public void setUp() {
        org.apache.logging.log4j.core.config.Configurator.setLevel("edu.upenn.cis.cis455", Level.DEBUG);
    }

    @Test
    public void testforgetHeaders()
    {
        Map<String,String> testHeaders = new HashMap<>();
        testHeaders.put("Connection","close");
        response.setHeaders(testHeaders);
        Assert.assertTrue(response.getHeaders().equalsIgnoreCase("Connection:close\r\n"));
    }
}
