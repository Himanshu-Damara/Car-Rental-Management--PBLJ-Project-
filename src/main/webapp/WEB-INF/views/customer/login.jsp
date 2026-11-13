<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<section class="container narrow section-pad">
  <div class="form-panel">
    <div class="eyebrow">
      WELCOME BACK
    </div>
    <h1>
      Sign in to CU Drive
    </h1>
    <c:if test="${not empty message}">
      <div class="alert alert-info">
        <c:out value="${message}"/>
      </div>
    </c:if>
    <form method="post" action="login">
      <input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="next" value="<c:out value='${param.next}'/>"><label>Email address</label><input required type="email" name="email" class="form-control mb-3"><label>Password</label><input required type="password" name="password" class="form-control mb-4"><button class="btn btn-lime w-100">Sign in</button>
    </form>
    <p class="mt-3">
      New here? <a href="register">Create an account</a>
    </p>
    <div class="demo-note">
      Demo customer: demo@demo / demo<br>Admin: admin@admin / admin
    </div>
  </div>
</section>
<%@ include file="/WEB-INF/views/footer.jspf" %>
