<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/header.jspf" %>
<section class="container narrow section-pad">
  <div class="form-panel text-center">
    <div class="success-mark">
      ✓
    </div>
    <div class="eyebrow">
      REQUEST RECEIVED
    </div>
    <h1>
      You're all set.
    </h1>
    <p>
      Your booking <b><c:out value="${booking.id}"/></b> is pending admin approval.
    </p>
    <div class="estimate">
      Total: ₹<c:out value="${booking.totalPrice}"/>
    </div>
    <a class="btn btn-lime mt-3" href="bookings">View my bookings</a>
  </div>
</section>
<%@ include file="/WEB-INF/views/footer.jspf" %>
