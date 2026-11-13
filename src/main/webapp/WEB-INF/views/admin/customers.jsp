<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<section class="container section-pad">
  <div class="eyebrow">
    CUSTOMER DIRECTORY
  </div>
  <h1>
    Customers
  </h1>
  <div class="table-responsive mt-4">
    <table class="table">
      <thead>
        <tr>
          <th>
            Name
          </th>
          <th>
            Email
          </th>
          <th>
            Phone
          </th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${users}" var="u">
          <tr>
            <td>
              <c:out value="${u.name}"/>
            </td>
            <td>
              <c:out value="${u.email}"/>
            </td>
            <td>
              <c:out value="${u.phone}"/>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</section>
<%@ include file="/WEB-INF/views/footer.jspf" %>
