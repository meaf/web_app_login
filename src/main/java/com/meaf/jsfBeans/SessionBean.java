package com.meaf.jsfBeans;

import com.meaf.core.dao.service.MiscHelper;
import com.meaf.core.dao.service.SessionManagementHelper;
import com.meaf.core.dao.service.base.Response;
import com.meaf.core.dao.service.users.ProjectUserConnectionService;
import com.meaf.core.dao.service.users.UserProfileService;
import com.meaf.core.dao.service.users.UserService;
import com.meaf.core.entities.*;
import com.meaf.core.meta.EProjectRole;
import com.meaf.core.meta.ESurveyStatus;
import com.meaf.core.meta.EUserRole;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class SessionBean implements Serializable {

    @Inject
    ProjectUserConnectionService projectUserConnectionService;
    @Inject
    UserService userService;
    @Inject
    UserProfileService userProfileService;
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
    private Date birthDate = new Date();
    private Role role;

    private List<EProjectRole> projectRoles = Arrays.asList(EProjectRole.values());
    private EProjectRole projectRole;

    public void createUser() throws Exception {
        userService.addUser(username, password, role);

    }

    public void registerUser() {
        Response response = new Response();
        if (invite == null || invite.isEmpty() || projectUserConnectionService.isInviteValid(invite)) {
            if (!username.isEmpty() && userService.isUsernameTaken(username)) {
                try {
                    role = userService.getRoleByName(EUserRole.user);
                    userService.addUser(username, password, role);

                    User newUser = userService.getUserByName(username);
                    userProfileService.add(new UserProfile(birthDate, fname, lname, pname, phone, email, newUser));

                    userService.connectUserWithInvite(newUser, invite);
                    response.setSeverity(FacesMessage.SEVERITY_INFO);
                    response.setTitle("Успіх");
                    response.setInfo("Тепер увійдіть використовуючи введені данні");
                } catch (Exception ex) {
                    response.setSeverity(FacesMessage.SEVERITY_ERROR);
                    response.setTitle("Невдача");
                    response.setInfo("Перевірте правильність введення даних та спробуйте знову");
                    ex.printStackTrace();
                }
            } else {
                response.setSeverity(FacesMessage.SEVERITY_ERROR);
                response.setTitle("Невдача");
                response.setInfo("Користувач з таким логіном вже існує");
            }
        } else {
            response.setSeverity(FacesMessage.SEVERITY_WARN);
            response.setTitle("Невдача");
            response.setInfo("Запрошення не вірне");
        }
        addToast(response);
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

    public User getCurrentUser() {
        return sessionManagementHelper.getCurrentUser();
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
            password = "";
            Response response = new Response(FacesMessage.SEVERITY_ERROR, "Вхід не здійснено", "Неправильний логін чи пароль");
            addToast(response);
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

    public String getCurrentProjectRole() {
        ProjectUserConnection connection = sessionManagementHelper.getCurrentSessionProjectUserConnection();
        return connection != null
                ? connection.getRole().getLocal().toUpperCase()
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

    public String generateInvite(EProjectRole role) throws Exception {
        do {
            invite = MiscHelper.randomString(8);
        } while (projectUserConnectionService.isInviteCodeTaken(invite));

        saveInvite(role, invite);
        return invite;
    }

    public void saveInvite(EProjectRole role, String invite) throws Exception {
        Response response = projectUserConnectionService.addInvite(role, invite);
        addToast(response);
    }

    private void addToast(Response response) {
        if (response != null)
            FacesContext.getCurrentInstance().addMessage(null, response.generateToast());
    }

    public boolean isOrganizing(Project project) {
        User currentUser = sessionManagementHelper.getCurrentUser();
        return projectUserConnectionService.getUserProjects(currentUser, EProjectRole.ORGANIZER).stream().anyMatch(p -> p.getId() == project.getId());
    }

    public boolean isOrganizingAny() {
        List<ProjectUserConnection> connections = projectUserConnectionService.getUserConnections(sessionManagementHelper.getCurrentUser(), EProjectRole.ORGANIZER);
        return connections.stream().anyMatch(c -> c.getRole() == EProjectRole.ORGANIZER);
    }

    public boolean canUserDelete(String status) {
        return sessionManagementHelper.getCurrentSessionProjectUserConnection().getRole() == EProjectRole.ORGANIZER
                && status.trim().equals(ESurveyStatus.NEW.getLocal());
    }


    /**
     * GET SET section
     */
    public ProjectUserConnectionService getProjectUserConnectionService() {
        return projectUserConnectionService;
    }

    public void setProjectUserConnectionService(ProjectUserConnectionService projectUserConnectionService) {
        this.projectUserConnectionService = projectUserConnectionService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserProfileService getUserProfileService() {
        return userProfileService;
    }

    public void setUserProfileService(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    public SessionManagementHelper getSessionManagementHelper() {
        return sessionManagementHelper;
    }

    public void setSessionManagementHelper(SessionManagementHelper sessionManagementHelper) {
        this.sessionManagementHelper = sessionManagementHelper;
    }

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<EProjectRole> getProjectRoles() {
        return projectRoles;
    }

    public void setProjectRoles(List<EProjectRole> projectRoles) {
        this.projectRoles = projectRoles;
    }

    public EProjectRole getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(EProjectRole projectRole) {
        this.projectRole = projectRole;
    }
}
