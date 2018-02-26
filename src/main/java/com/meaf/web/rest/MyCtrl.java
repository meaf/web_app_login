//package com.meaf.web.rest;
//
//import com.meaf.web.ResponseUtil;
//
//import javax.inject.Inject;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.core.Response;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Path("/")
//public class MyCtrl {
//
//    @Inject
//    ResponseUtil responseUtil;
//
//    @GET
//    @Path("/")
//    public Response test() {
//        return Response.status(200).entity(new SimpleDateFormat("hh:mm:ss").format(new Date())).build();
//    }
//
//    @GET
//    @Path("/getRole/{id}")
//    public Response getRole(@PathParam("id") Long id) {
//        String r = responseUtil.getRole(id);
//        System.out.println(r);
//        return Response.status(200).build();
//    }
//
//}