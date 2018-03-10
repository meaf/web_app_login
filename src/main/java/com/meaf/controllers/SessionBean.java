package com.meaf.controllers;

import com.meaf.core.dao.service.SessionManagementHelper;
import com.meaf.core.dao.service.UserService;
import com.meaf.core.entities.Project;
import com.meaf.core.entities.Role;
import com.meaf.core.entities.User;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named
@ManagedBean
@RequestScoped
public class SessionBean implements Serializable {


    @Inject
    UserService userService;
    @Inject
    SessionManagementHelper sessionManagementHelper;

    private String username;
    private String password;
    private String passwordConfirm;
    private Role role;

    public void createUser() throws Exception {
        userService.addUser(username, password, role);
    }

    public void registerUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!password.equals(passwordConfirm)) {
            context.addMessage("registerForm:passwordConfirm", new FacesMessage("Passwords don't match."));
        } else {
            role = userService.getRoleByName("user");
            userService.addUser(username, password, role);
        }
    }

    public void loadStages(Project project) throws IOException {
        swichProject(project);
        FacesContext context = FacesContext.getCurrentInstance();

        context.getExternalContext().redirect("stages.xhtml");
    }

    public void swichProject(Project project) {
        sessionManagementHelper.swichProject(project);
    }

    public Project getCurrentProject() {
        return sessionManagementHelper.getCurrentProject();
    }

    public List<Role> getRolesList() {
        return userService.getRolesList();
    }

    public List<User> getUsersList() {
        return userService.getUsersList();
    }

    public void login() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            sessionManagementHelper.login(username, password);
            String redirectPage = "index.xhtml";
            if (sessionManagementHelper.isUserInRole("admin"))
                redirectPage = "admin/users.xhtml";
            if (sessionManagementHelper.isUserInRole("user"))
                redirectPage = "user/index.xhtml";
            context.getExternalContext().redirect(redirectPage);
        } catch (ServletException se) {
            context.addMessage("loginForm:username", new FacesMessage("Authentication Failed. Check username or password."));
        }
    }

    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        try {
            context.getExternalContext().redirect("login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * GET SET section
     */


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}