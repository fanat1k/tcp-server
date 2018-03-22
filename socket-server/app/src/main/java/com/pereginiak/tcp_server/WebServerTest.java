package com.pereginiak.tcp_server;

import fi.iki.elonen.NanoHTTPD;

public class WebServerTest extends NanoHTTPD {
    public WebServerTest(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        if (uri.equals("/hello")) {
            String response = "HelloWorld";
            return newFixedLengthResponse(response);
        }
        return  null;
    }
}
