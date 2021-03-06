package com.meaf.core.dao.service.users;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.dao.service.base.Response;
import com.meaf.core.dao.service.base.SysPropertiesManager;
import com.meaf.core.entities.Project;
import com.meaf.core.entities.ProjectUserConnection;
import com.meaf.core.entities.SysProperties;
import com.meaf.core.entities.User;
import com.meaf.core.meta.EProjectRole;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class ProjectUserConnectionService extends ABaseService<ProjectUserConnection> {
    @Inject
    SysPropertiesManager sysPropertiesManager;

    @Override
    public List<ProjectUserConnection> getBranched(Long rootNode) throws IllegalAccessException {
        throw new IllegalAccessException("has no root");
    }

    @Override
    public List<ProjectUserConnection> getAll() {
        return getEntityManager()
                .createQuery("select u from ProjectUserConnection u", ProjectUserConnection.class)
                .getResultList();
    }

    @Override
    public ProjectUserConnection getById(Long id) {
        return getEntityManager().find(ProjectUserConnection.class, id);
    }

    @Override
    public void update(ProjectUserConnection con) {
        getEntityManager().persist(con);
    }

    public List<ProjectUserConnection> getUnusedInvites() {
        return getEntityManager()
                .createQuery("select u from ProjectUserConnection u " +
                        "where u.invite is not null " +
                        "and u.user is null", ProjectUserConnection.class)
                .getResultList();
    }

    public boolean isInviteCodeTaken(String inv) {
        return !getEntityManager()
                .createQuery("select u from ProjectUserConnection u where u.invite = :inv", ProjectUserConnection.class)
                .setParameter("inv", inv)
                .getResultList().isEmpty();
    }

    public boolean isInviteValid(String inv) {
        return !getEntityManager()
                .createQuery("select u from ProjectUserConnection u " +
                        "where u.invite = :inv " +
                        "and u.user is null", ProjectUserConnection.class)
                .setParameter("inv", inv)
                .getResultList().isEmpty();
    }

    public Response addInvite(EProjectRole role, String invite) {
        Response response = new Response();
        try {
            ProjectUserConnection con = super.add(new ProjectUserConnection(invite,
                    findSingleByWhereClause(Project.class, "id", 0),
                    role));

            if (role == EProjectRole.EXPERT) {
                SysProperties sysProperty = sysPropertiesManager.getByKey("project_experts");
                Long userNum = Long.parseLong(sysProperty.getValue()) + 1;
                con.setNumber(userNum);
                update(con);
                sysProperty.setValue("" + (userNum));
                getEntityManager().merge(sysProperty);
            }

            response.setSeverity(FacesMessage.SEVERITY_INFO);
            response.setTitle("Успіх");
            response.setInfo("Запрошення створено");
        } catch (Exception ex) {
            response.setEx(ex);
            response.setSeverity(FacesMessage.SEVERITY_ERROR);
            response.setTitle("Невдача");
            response.setInfo("Щось пішло не так...");
            ex.printStackTrace();
        }
        return response;
    }

    public List<Project> getUserProjects(User user, EProjectRole role) {
        return getUserConnections(user, role)
                .stream()
                .map(ProjectUserConnection::getProject)
                .collect(Collectors.toList());
    }

    public List<ProjectUserConnection> getUserConnections(User user, EProjectRole role) {
        return getEntityManager()
                .createQuery("select u from ProjectUserConnection u " +
                        "where u.user = :user " +
                        "and u.role = :role", ProjectUserConnection.class)
                .setParameter("user", user)
                .setParameter("role", role)
                .getResultList();
    }

    public List<ProjectUserConnection> getProjectConnections(Project project, EProjectRole role) {
        return getEntityManager()
                .createQuery("select u from ProjectUserConnection u " +
                        "where u.project = :project " +
                        "and u.role = :role " +
                        "and u.user is not null ", ProjectUserConnection.class)
                .setParameter("project", project)
                .setParameter("role", role)
                .getResultList();
    }

    public ProjectUserConnection getUserProjectConnections(User user, Project project) {
        return getEntityManager()
                .createQuery("select u from ProjectUserConnection u " +
                        "where u.user = :user " +
                        "and u.project = :project", ProjectUserConnection.class)
                .setParameter("user", user)
                .setParameter("project", project)
                .getSingleResult();
    }

}
