<%@include file="/apps/capstone/components/content/capstonetextimage/imageserver.jsp"%>

<div class="mdtitle">
                        <a page="en/about_xumak.html" href="${currentPage.name}.html">
                            <%  if (img!=null) {  img.draw(out);} %><cq:text property="jcr:title" placeholder="" tagName="small" escapeXml="true"/>
                        </a>
                    </div>

                    <h4><p><cq:text property="jcr:subtitle" placeholder="" tagName="small" escapeXml="true"/></p></h4>


                <div class="mdisi">
					<p><cq:text property="jcr:description" placeholder="" tagName="small" escapeXml="true"/></p>
                </div>
