package com.meaf.web;

import com.meaf.ResponseUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class MyCtrl {

    @GET
    @Path("/")
    public Response test() {
        return Response.status(200).entity("test page ok").build();
    }

    @GET
    @Path("/login")
    public Response loginlogin() {
        return ResponseUtil.seeOther("../");
    }

}