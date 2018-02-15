package com.meaf.core;

import com.meaf.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationPath("/rest")
public class MyApp extends Application {
    private static Logger LOGGER = Logger.getGlobal();

    @GET
    @Path("/testok")
    public Response test() {
        return Response.status(200).entity("test page ok").build();
    }




}