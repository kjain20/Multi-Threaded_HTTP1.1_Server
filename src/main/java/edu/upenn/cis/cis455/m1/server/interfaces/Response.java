/**
 * CIS 455/555 route-based HTTP framework
 * <p>
 * Z. Ives, 8/2017
 * <p>
 * Portions excerpted from or inspired by Spark Framework,
 * <p>
 * http://sparkjava.com,
 * <p>
 * with license notice included below.
 */

/*
 * Copyright 2011- Per Wendel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.upenn.cis.cis455.m1.server.interfaces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;


public abstract class Response {
    protected int statusCode = 200;
    protected byte[] body;
    protected String contentType = null;
    public static Logger logger = LogManager.getLogger(Response.class);

    public int status() {
        return statusCode;
    }

    public void status(int statusCode) {
        this.statusCode = statusCode;
    }

    public String body() {
        try {
            return body == null ? "" : new String(body, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public byte[] bodyRaw() {
        return body;
    }

    public void bodyRaw(byte[] b) {
        body = b;
    }

    public void body(String body) {
        logger.info("Setting body of Response");
        this.body = body == null ? null : body.getBytes();
    }

    public String type() {
        logger.info("Get Content Type of Response");
        return contentType;
    }

    public void type(String contentType) {

        this.contentType = contentType;
    }

    public abstract String getHeaders();

    public abstract void setConnectionFalse();

    public abstract void setDate();
}
