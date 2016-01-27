<%@include file="/libs/foundation/global.jsp" %>
<%@page import="com.test.bundle.Hello" %>


<% Hello he=new Hello();%>
<%=he.getBundleMsg()+"aa" %>

<div id="topright-left">Select language

	<%  String resPath = resource.getPath();  %>

                                            <div class="flag1">
                                                <% if(resPath.startsWith("/content/capstone/en")){ %>
                                               <a href="/content/capstone/fr/${currentPage.name}.html" x-cq-linkchecker="skip">
                                                    <img src="../../dam/capstone/sp.gif" />
                                                </a>
                                                <% } else { %> <img src="../../dam/capstone/sp.gif" /> <% }%>
                                            </div>
                                            <div class="flag2">
                                                <% if(resPath.startsWith("/content/capstone/fr")){ %>
                                               <a href="/content/capstone/en/${currentPage.name}.html" x-cq-linkchecker="skip">
                                                    <img src="../../dam/capstone/en.jpg" />
                                                </a>
                                                <% } else { %> <img src="../../dam/capstone/en.jpg" /> <% }%>


                                            </div>
  </div>