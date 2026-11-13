document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".car-image-wrap img").forEach((image) => {
    image.addEventListener("error", () => {
      const context = document.querySelector('meta[name="app-context"]')?.content ?? "";
      image.src = `${context}/assets/images/car-placeholder.svg`;
      image.onerror = null;
    }, { once: true });
  });
  const today = new Date();
  today.setMinutes(today.getMinutes() - today.getTimezoneOffset());
  const iso = today.toISOString().slice(0, 10);
  document.querySelectorAll("[data-min-today]").forEach((el) => (el.min = iso));
  document.querySelectorAll(".booking-inline").forEach((form) => {
    const from = form.querySelector("[name=pickupDate]"),
      to = form.querySelector("[name=returnDate]"),
      out = form.querySelector(".estimate"),
      rate = Number(out.dataset.price);
    const update = () => {
      if (from.value) {
      const returnMin = new Date(`${from.value}T00:00:00`);
      returnMin.setDate(returnMin.getDate() + 1);
      to.min = `${returnMin.getFullYear()}-${String(returnMin.getMonth() + 1).padStart(2, "0")}-${String(returnMin.getDate()).padStart(2, "0")}`;
        if (to.value && to.value <= from.value) to.value = "";
      }
      if (from.value && to.value && to.value > from.value) {
        const days = Math.round(
          (new Date(to.value) - new Date(from.value)) / 86400000,
        );
        out.textContent = `${days} day${days === 1 ? "" : "s"} · Estimated total ₹${(days * rate).toLocaleString("en-IN")}`;
      } else out.textContent = "Select dates to estimate total";
    };
    from.addEventListener("change", update);
    to.addEventListener("change", update);
  });
});
