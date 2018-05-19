//package com.meaf.web;
//
//import com.meaf.core.dao.service.users.UserService;
//import com.meaf.core.entities.Role;
//
//import javax.inject.Inject;
//import javax.inject.Named;
//import java.util.Optional;
//
//@Named
//public class ResponseUtil {
//
//    @Inject
//    UserService userService;
//
//    public String getRole(long id) {
//        Optional<Role> optRole = userService.getRolesList().stream().filter(r->r.getId() == id).findAny();
//        if(optRole.isPresent())
//            return optRole.get().getRolename();
//        return "role not found";
//    }
//}