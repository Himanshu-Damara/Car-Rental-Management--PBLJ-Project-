<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<section class="container narrow section-pad">
  <div class="form-panel">
    <div class="eyebrow">
      REQUEST COULD NOT BE COMPLETED
    </div>
    <h1>
      Something went wrong.
    </h1>
    <p>
      Please return to the fleet and try again.
    </p>
    <a class="btn btn-lime" href="${pageContext.request.contextPath}/">Back to home</a>
  </div>
</section>
<%@ include file="/WEB-INF/views/footer.jspf" %>
