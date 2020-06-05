package edu.upenn.cis.cis455.handlers;

import edu.upenn.cis.cis455.m2.server.interfaces.Request;
import edu.upenn.cis.cis455.m2.server.interfaces.Response;


public class TestHandler implements Route{
    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.cookie("Name", "Karishma");
        response.body("<HTML><BODY>Hello World</BODY></HTML>");
        return response;
    }
}


