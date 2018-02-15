<%
    String uname = request.getUserPrincipal().getName();
    out.println("<h2>Hey, " + uname + "</h2>");

    if(request.isUserInRole("admin")){
        out.println("<a href='../admin/'>To the admin panel we go!</a>");
    }

    else out.println("mere user");
%>