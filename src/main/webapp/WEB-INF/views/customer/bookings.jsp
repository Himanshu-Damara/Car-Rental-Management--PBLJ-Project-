<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<section class="container section-pad">
  <div class="eyebrow">
    YOUR CU DRIVE ACCOUNT
  </div>
  <h1>
    My bookings
  </h1>
  <c:if test="${not empty bookings}">
    <div class="table-responsive mt-4">
      <table class="table align-middle">
        <thead>
          <tr>
            <th>
              Booking
            </th>
            <th>
              Car
            </th>
            <th>
              Dates
            </th>
            <th>
              Total
            </th>
            <th>
              Status
            </th>
            <th>
            </th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${bookings}" var="b">
            <tr>
              <td>
                <c:out value="${b.id}"/>
              </td>
              <td>
                <c:out value="${b.carName}"/>
              </td>
              <td>
                <c:out value="${b.pickupDate}"/> — <c:out value="${b.returnDate}"/>
              </td>
              <td>
                ₹<c:out value="${b.totalPrice}"/>
              </td>
              <td>
                <span class="badge status"><c:out value="${b.status}"/></span>
              </td>
              <td>
                <c:if test="${b.status eq 'PENDING'}">
                  <form method="post" action="cancel">
                    <input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="id" value="<c:out value='${b.id}'/>"><button class="btn btn-sm btn-outline-danger">Cancel</button>
                  </form>
                </c:if>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </c:if>
  <c:if test="${empty bookings}">
    <div class="empty-state mt-4">
      No bookings yet. <a href="cars">Explore the fleet</a>
    </div>
  </c:if>
</section>
<%@ include file="/WEB-INF/views/footer.jspf" %>
