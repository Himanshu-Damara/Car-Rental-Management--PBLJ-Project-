<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<section class="container section-pad">
  <div class="eyebrow">
    MANAGEMENT CONSOLE
  </div>
  <h1>
    Admin dashboard
  </h1>
  <div class="row g-3 mt-2">
    <div class="col-6 col-lg-3">
      <div class="stat">
        <span>FLEET</span><strong>${cars.size()}</strong>
      </div>
    </div>
    <div class="col-6 col-lg-3">
      <div class="stat">
        <span>CUSTOMERS</span><strong>${users.size()-1}</strong>
      </div>
    </div>
    <div class="col-6 col-lg-3">
      <div class="stat">
        <span>BOOKINGS</span><strong>${bookings.size()}</strong>
      </div>
    </div>
    <div class="col-6 col-lg-3">
      <div class="stat">
        <span>APPROVED REVENUE</span><strong>₹${revenue}</strong>
      </div>
    </div>
  </div>
  <div class="d-flex justify-content-between align-items-center mt-5">
    <h2>
      Booking requests
    </h2>
    <a class="btn btn-outline-dark" href="cars">Manage fleet</a>
  </div>
  <div class="table-responsive">
    <table class="table">
      <thead>
        <tr>
          <th>
            ID
          </th>
          <th>
            Customer
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
            Status / action
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
              <c:out value="${b.customerName}"/>
            </td>
            <td>
              <c:out value="${b.carName}"/>
            </td>
            <td>
              <c:out value="${b.pickupDate}"/>–<c:out value="${b.returnDate}"/>
            </td>
            <td>
              ₹<c:out value="${b.totalPrice}"/>
            </td>
            <td>
              <c:out value="${b.status}"/>
              <c:if test="${b.status eq 'PENDING'}">
                <form method="post" action="booking/status" class="d-flex gap-1 mt-2">
                  <input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="id" value="<c:out value='${b.id}'/>"><button name="status" value="APPROVED" class="btn btn-sm btn-success">Approve</button><button name="status" value="REJECTED" class="btn btn-sm btn-outline-danger">Reject</button>
                </form>
              </c:if>
              <c:if test="${b.status eq 'APPROVED'}">
                <form method="post" action="booking/status">
                  <input type="hidden" name="_csrf" value="${csrf}"><input type="hidden" name="id" value="<c:out value='${b.id}'/>"><button name="status" value="COMPLETED" class="btn btn-sm btn-outline-primary">Complete</button>
                </form>
              </c:if>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
  <a href="customers" class="text-link">View customer records →</a>
</section>
<%@ include file="/WEB-INF/views/footer.jspf" %>
