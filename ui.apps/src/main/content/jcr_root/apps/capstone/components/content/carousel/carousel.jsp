<%@include file="/apps/capstone/components/content/capstonetextimage/imageserver.jsp"%>



                        <li>

                            <a page="en.html#" href="${currentPage.name}.html">
                               <% if(img!=null) img.draw(out); %>
                            </a>
                            <div class="boxright" style="">
                                <div class="boxright-tile">
                                    <div class="marquee-slide-title"><cq:text property="jcr:title" placeholder="" tagName="small" escapeXml="true"/></div>
                                    <div class="marquee-slide-sub-title"><cq:text property="jcr:subtitle" placeholder="" tagName="small" escapeXml="true"/></div>
                                </div>
                                <div class="boxright-content">
                                    <p><cq:text property="jcr:title" placeholder="" tagName="small" escapeXml="true"/></p>

                                        <div class="boxright-content-get"><a id="get" href="<cq:text property="jcr:linkURL" placeholder="" tagName="small" escapeXml="true"/>" x-cq-linkchecker="skip">getstarted</a></div>



                                </div>
                            </div>
                        </li>


