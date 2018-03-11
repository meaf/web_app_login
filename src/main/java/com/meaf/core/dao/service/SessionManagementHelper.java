package com.meaf.core.dao.service;

import com.meaf.core.dao.service.base.ConfigurationBean;
import com.meaf.core.entities.Project;
import com.meaf.core.entities.ProjectUserConnection;
import com.meaf.core.entities.User;
import com.meaf.core.meta.ProjectConstants;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.meaf.core.meta.ProjectConstants.SESSION_PARAM__CURRENT_USER;
import static com.meaf.core.meta.ProjectConstants.SESSION_PARAM__PROJECT;

@Named
public class SessionManagementHelper {
    @EJB
    private ConfigurationBean configurationBean;
    @Inject
    private HttpServletRequest request;

    public User getCurrentUser() {
        if (request.getRemoteUser() == null)
            return null;
        if (request.getSession().getAttribute(SESSION_PARAM__CURRENT_USER) == null
                || !((User) request.getSession().getAttribute(SESSION_PARAM__CURRENT_USER))
                .getUsername().equals(request.getRemoteUser())) {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            cq.select(root).where(cb.equal(root.get("username"), request.getRemoteUser()));
            User user = getEntityManager().createQuery(cq).getSingleResult();
            request.getSession().setAttribute(SESSION_PARAM__CURRENT_USER, user);
            return user;
        } else
            return (User) request.getSession().getAttribute(SESSION_PARAM__CURRENT_USER);
    }

    public List<Project> getCurrentUserProjects() {
        return getUserProjects(getCurrentUser());
    }

    public List<Project> getUserProjects(User user) {
        List<ProjectUserConnection> projectUserConnections = loadConnections(user);

        List<Project> ps = projectUserConnections.stream()
                .map(ProjectUserConnection::getProject)
                .collect(Collectors.toList());
        return ps;
    }


    /**
     * WRAPPERS
     */
    private EntityManager getEntityManager() {
        return configurationBean.getEntityManager();
    }

    public void logout() throws ServletException {
        request.logout();
    }

    public void login(String uname, String pass) throws ServletException {
        if (getCurrentUser() != null)
            logout();
        request.login(uname, pass);
    }

    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }

    /**
     * SESSION PROJECT OPERATIONS
     */
    public void swichProject(Project project) {
        setCurrentSessionProject(project);
    }

    public Project getCurrentProject() {
        return (Project) request.getSession().getAttribute(SESSION_PARAM__PROJECT);
    }

    /**
     * SESSION CONNECTION OPERATIONS
     */
    private void updateConnections() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ProjectUserConnection> cq = cb.createQuery(ProjectUserConnection.class);
        Root<ProjectUserConnection> root = cq.from(ProjectUserConnection.class);
        cq.select(root).where(cb.and(
                cb.equal(root.get("project").get("id"), getCurrentSessionProject().getId()),
                cb.equal(root.get("user"), getCurrentUser())
        ));
        ProjectUserConnection connection = getEntityManager().createQuery(cq).getSingleResult();
        setCurrentSessionProjectUserConnection(connection);
    }


    /**
     * SESSION OPERATIONS
     */
    public Project getCurrentSessionProject() {
        return (Project) request.getSession()
                .getAttribute(SESSION_PARAM__PROJECT);
    }

    private void setCurrentSessionProject(Project currentProject) {
        request.getSession().setAttribute(
                ProjectConstants.SESSION_PARAM__PROJECT,
                currentProject);

        updateConnections();

    }

    public ProjectUserConnection getCurrentSessionProjectUserConnection() {
        return (ProjectUserConnection) request.getSession()
                .getAttribute(ProjectConstants.SESSION_PARAM__PROJECT_USER_CONNECTION);
    }

    private void setCurrentSessionProjectUserConnection(ProjectUserConnection connection) {
        request.getSession().setAttribute(
                ProjectConstants.SESSION_PARAM__PROJECT_USER_CONNECTION,
                connection);
    }

    private List<ProjectUserConnection> loadConnections(User user) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ProjectUserConnection> cq = cb.createQuery(ProjectUserConnection.class);
        Root<ProjectUserConnection> root = cq.from(ProjectUserConnection.class);
        cq.select(root).where(cb.equal(root.get("user"), user));
        List<ProjectUserConnection> projectUserConnections = getEntityManager().createQuery(cq).getResultList();
        return projectUserConnections;
    }
}
