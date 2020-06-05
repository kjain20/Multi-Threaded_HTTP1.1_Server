package edu.upenn.cis.cis455.handlers;

import edu.upenn.cis.cis455.m2.server.interfaces.Request;
import edu.upenn.cis.cis455.m2.server.interfaces.Response;

public class PostHandler implements Route{
    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.body("<HTML><BODY>Hello World-"+request.body()+"</BODY></HTML>");
        return null;
    }
}