package com.meaf.core.dao.service;

import com.meaf.core.dao.service.base.ABaseService;
import com.meaf.core.entities.UserProfile;

import javax.inject.Named;
import java.util.List;

@Named
public class UserProfileService extends ABaseService<UserProfile> {
    @Override
    public List<UserProfile> getBranched(Long rootNode) throws IllegalAccessException {
        throw new IllegalAccessException("has no root");
    }

    @Override
    public List<UserProfile> getAll() {
        return getEntityManager().createQuery("select u from UserProfile u", UserProfile.class).getResultList();
    }

    @Override
    public UserProfile getById(Long id) {
        return getEntityManager().find(UserProfile.class, id);
    }

    @Override
    public void update(UserProfile user) {
        UserProfile olUser = getById(user.getId());
        throw new UnsupportedOperationException("uns");
//        olUser.setPassword(user.getPassword());
//        olUser.setUsername(user.getUsername());
    }

}