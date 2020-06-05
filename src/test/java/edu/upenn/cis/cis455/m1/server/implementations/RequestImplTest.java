package edu.upenn.cis.cis455.m1.server.implementations;

import org.apache.logging.log4j.Level;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RequestImplTest {

    @Before
    public void setUp() {
        org.apache.logging.log4j.core.config.Configurator.setLevel("edu.upenn.cis.cis455", Level.DEBUG);
    }

    RequestImpl testRequest = new RequestImpl();

    @Test
    public void testForPort()
    {
        testRequest.setPort(8088);
        Assert.assertEquals(testRequest.getPort(),8088);
    }

    @Test
    public void testForBody()
    {
        testRequest.setBody("Request Body");
        Assert.assertTrue(testRequest.getBody().equalsIgnoreCase("Request Body"));
    }

    @Test
    public void testForIp()
    {
        testRequest.setIp("http://localhost:8088/");
        Assert.assertTrue(testRequest.getIp().equalsIgnoreCase("http://localhost:8088/"));
    }

}
