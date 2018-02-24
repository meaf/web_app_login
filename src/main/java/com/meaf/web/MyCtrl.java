package com.meaf.web;

import com.meaf.ResponseUtil;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;

@Path("/")
public class MyCtrl {

    @Inject
    HttpServletRequest req;
    @Inject
    HelloBean bean;


    @GET
    @Path("/")
    public Response test() {
        return Response.status(200).entity(new SimpleDateFormat("hh:mm:ss").format(bean.getDate())).build();
    }

    @GET
    @Path("/login")
    public Response loginlogin() {
        return Response.status(200).build();
//        return ResponseUtil.seeOther("../");
    }

    @GET
    @Path("/getRole/{id}")
    public Response getRole(@PathParam("id") Long id) {
        String r = ResponseUtil.getRole(id);
        System.out.println(r);
        return Response.status(200).build();
    }

}