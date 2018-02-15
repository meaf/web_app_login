package com.meaf.web;

import com.meaf.ResponseUtil;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/")
public class MyCtrl {
    private static Logger LOGGER = Logger.getGlobal();

    @GET
    @Path("/")
    public Response test() {
        return Response.status(200).entity("test page ok").build();
    }


    @POST
    @Path("/login")
    public Response login(@FormParam("username") String username, @FormParam("password") String password) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
//            throw new ServletException("");
            request.login(username, password);
//            LOGGER.info("Login Success for: " + username);
        } catch (ServletException e) {
            LOGGER.info("Login Exception: " + e.getMessage());
        }

        return ResponseUtil.seeOther("../");
    }

    @GET
    @Path("/logout")
    public Response logout() {
//        try {
//            request.logout();
//        } catch (ServletException e) {
            LOGGER.info("Logout Exception: ");
//        }

        return ResponseUtil.seeOther("../");
    }

}