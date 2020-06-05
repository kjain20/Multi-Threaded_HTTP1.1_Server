package edu.upenn.cis.cis455.m1.server.interfaces;

import edu.upenn.cis.cis455.exceptions.HaltException;

@FunctionalInterface
public interface HttpRequestHandler {
    public void handle(Request request, Response response) throws HaltException;
}
