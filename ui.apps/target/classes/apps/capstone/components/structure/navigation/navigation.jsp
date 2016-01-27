<%@page session="false"%><%--
  Copyright 1997-2008 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Top Navigation component

  Draws the top navigation

--%><%@include file="/libs/foundation/global.jsp"%><%
%>

<%@ page import="java.util.Iterator,
        com.day.text.Text,
        com.day.cq.wcm.api.PageFilter,
        com.day.cq.wcm.api.Page,
        com.day.cq.commons.Doctype" %><%

    // get starting point of navigation
    long absParent = currentStyle.get("absParent", 2L);
    String navstart = Text.getAbsoluteParent(currentPage.getPath(), (int) absParent);

    //if not deep enough take current node
    if (navstart.equals("")) navstart=currentPage.getPath();

    Resource rootRes = slingRequest.getResourceResolver().getResource(navstart);
    Page rootPage = rootRes == null ? null : rootRes.adaptTo(Page.class);
    String xs = Doctype.isXHTML(request) ? "/" : "";

%>
<div id="menu">

                                <div class="submenu-home">
                                    <a href="${rootPage.name}.html">
                                        HOME
                                    <br />

                                    <h6><%= xssAPI.encodeForHTMLAttr(rootPage.getDescription())==null?xssAPI.encodeForHTMLAttr(rootPage.getTitle()):xssAPI.encodeForHTMLAttr(rootPage.getDescription()) %></h6>

                                    </a>
                                </div>

<%
if (rootPage != null) {
        Iterator<Page> children = rootPage.listChildren(new PageFilter(request));
        while (children.hasNext()) {
            Page child = children.next();
            %>
  <div class="submenu">
                                    <a href="<%= xssAPI.getValidHref(child.getPath()) %>.html">
                                        <%= xssAPI.encodeForHTMLAttr(child.getTitle()) %>
                                    <br />

                                        <h6><%= xssAPI.encodeForHTMLAttr(child.getDescription())==null?xssAPI.encodeForHTMLAttr(child.getTitle()):xssAPI.encodeForHTMLAttr(child.getDescription()) %></h6>

                                    </a>
                                </div>
<%

        }
    }
%>






</div>