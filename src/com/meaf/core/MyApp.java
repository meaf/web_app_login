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


    @POST
    @Path("/login")
    public Response login(ServletRequest req, ServletResponse res, @FormParam("username") String username, @FormParam("password") String password) {
        LOGGER.log(Level.INFO, "YEAAAAAAAAAAH: ");
        HttpServletRequest request = (HttpServletRequest)req;
        try {
            request.login(username, password);
            LOGGER.log(Level.INFO, "Login Success for: " + username);
        } catch (ServletException e) {
            LOGGER.log(Level.WARNING, "Login Exception: " + e.getMessage());
        }

        return ResponseUtil.seeOther("../");
    }

    @GET
    @Path("/logout")
    public Response logout(ServletRequest req, ServletResponse res) {
        HttpServletRequest request = (HttpServletRequest)req;
        try {
            request.logout();
        } catch (ServletException e) {
            LOGGER.log(Level.WARNING, "Logout Exception: " + e.getMessage());
        }
        return ResponseUtil.seeOther("../");
    }

}