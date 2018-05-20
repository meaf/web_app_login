package com.meaf.core.dao.service.base;

import javax.faces.application.FacesMessage;

public class Response {
    private FacesMessage.Severity severity;
    private String title;
    private String info;
    private Exception ex;

    public Response() {
    }

    public Response(FacesMessage.Severity severity, String title, String info) {
        this.severity = severity;
        this.title = title;
        this.info = info;
    }

    public FacesMessage generateToast() {
        FacesMessage facesMessage = new FacesMessage();
        facesMessage.setSummary(title);
        facesMessage.setDetail(info + (ex == null ? "" : ex.getLocalizedMessage()));
        facesMessage.setSeverity(severity);
        return facesMessage;
    }

    public FacesMessage.Severity getSeverity() {
        return severity;
    }

    public void setSeverity(FacesMessage.Severity severity) {
        this.severity = severity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }
}
