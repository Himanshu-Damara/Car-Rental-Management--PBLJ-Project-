<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<section class="container narrow section-pad">
  <div class="form-panel">
    <div class="eyebrow">
      START YOUR JOURNEY
    </div>
    <h1>
      Create account
    </h1>
    <c:if test="${not empty message}">
      <div class="alert alert-warning">
        <c:out value="${message}"/>
      </div>
    </c:if>
    <form method="post" action="register">
      <input type="hidden" name="_csrf" value="${csrf}"><label>Full name</label><input required maxlength="100" name="name" class="form-control mb-3"><label>Email address</label><input required type="email" name="email" class="form-control mb-3"><label>Phone number</label><input required maxlength="30" name="phone" class="form-control mb-3"><label>Password (minimum 8 characters)</label><input required minlength="8" type="password" name="password" class="form-control mb-4"><button class="btn btn-lime w-100">Create my account</button>
    </form>
    <p class="mt-3">
      Already a member? <a href="login">Sign in</a>
    </p>
  </div>
</section>
<%@ include file="/WEB-INF/views/footer.jspf" %>
