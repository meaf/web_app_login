package com.meaf.core.utils;

import com.meaf.core.entities.*;
import com.mysql.cj.core.exceptions.WrongArgumentException;

public class RelationUtil {
    public <T> Project getProject(T obj) {
        if (obj instanceof Answer)
            return ((Answer) obj).getQuestion().getSurvey().getStage().getRootProject();
        if (obj instanceof Question)
            return ((Question) obj).getSurvey().getStage().getRootProject();
        if (obj instanceof Survey)
            return ((Survey) obj).getStage().getRootProject();
        if (obj instanceof ProjectStage)
            return ((ProjectStage) obj).getRootProject();
        throw new WrongArgumentException("cannot retrieve project for type" + obj.getClass());
    }


}
