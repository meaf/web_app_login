package com.meaf.jsfBeans;

import com.meaf.core.dao.service.SessionManagementHelper;
import com.meaf.core.dao.service.UserService;
import com.meaf.core.entities.Project;
import com.meaf.core.entities.ProjectUserConnection;
import com.meaf.core.entities.Role;
import com.meaf.core.entities.User;
import com.meaf.core.meta.EProjectRole;
import com.meaf.core.meta.EUserRole;

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
    private String fname;
    private String lname;
    private String pname;
    private String email;
    private String phone;
    private String invite;
    private Role role;

    public void createUser() throws Exception {
        userService.addUser(username, password, role);

    }

    public void registerUser() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        role = userService.getRoleByName(EUserRole.user);
        userService.addUser(username, password, role);
//        User newUser = userService.getUserByName(username);
//
//
//        userService.connectUserToProject();
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
            if (sessionManagementHelper.isUserInRole(EUserRole.admin))
                redirectPage = "/admin/index.xhtml";
            if (sessionManagementHelper.isUserInRole(EUserRole.user))
                redirectPage = "/user/index.xhtml";
            context.getExternalContext().redirect(redirectPage);
        } catch (ServletException se) {
            context.addMessage("loginForm:username", new FacesMessage("Authentication Failed. Check username or password."));
        }
    }

    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            sessionManagementHelper.logout();
            context.getExternalContext().redirect("/index.xhtml");
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }

    public String getProjectRole() {
        ProjectUserConnection connection = sessionManagementHelper.getCurrentSessionProjectUserConnection();
        return connection != null
                ? connection.getRole().name().toUpperCase()
                : null;
    }

    public boolean isUserExpert() {
        return isUserInProjectRole(EProjectRole.EXPERT);
    }

    public boolean isUserOrganizer() {
        return isUserInProjectRole(EProjectRole.ORGANIZER);
    }

    public boolean isUserOrganizerInProject(Project project) {
        ProjectUserConnection connection = sessionManagementHelper.getConnectionBetween(project, sessionManagementHelper.getCurrentUser());
        return connection.getRole() == EProjectRole.ORGANIZER;
    }

    public boolean isUserInProjectRole(EProjectRole role) {
        ProjectUserConnection connection = sessionManagementHelper.getCurrentSessionProjectUserConnection();
        return connection != null
                && role.equals(connection.getRole());
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

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public SessionManagementHelper getSessionManagementHelper() {
        return sessionManagementHelper;
    }

    public void setSessionManagementHelper(SessionManagementHelper sessionManagementHelper) {
        this.sessionManagementHelper = sessionManagementHelper;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInvite() {
        return invite;
    }

    public void setInvite(String invite) {
        this.invite = invite;
    }
}