package com.meaf;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

public class ResponseUtil {

    public static Response seeOther(String url) {
        try {
            return Response.seeOther(new URI(url)).build();
        } catch (URISyntaxException e) {
            return null;
        }
    }
}