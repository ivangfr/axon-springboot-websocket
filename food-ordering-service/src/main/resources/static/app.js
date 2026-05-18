let stompClient = null;

const foodOrderingServiceApiBaseUrl = "http://localhost:9082/api";

const dataStore = {
  customers: {},
  restaurants: {},
  dishes: {},
};

const wizardState = {
  currentStep: 1,
  customerId: null,
  customerName: null,
  restaurantId: null,
  restaurantName: null,
};

function setWsStatus(connected) {
  if (connected) {
    $("#wsStatusDot").removeClass("bg-red-500").addClass("bg-green-500");
    $("#wsStatusText")
      .text("Connected")
      .removeClass("text-red-400")
      .addClass("text-green-400");
  } else {
    $("#wsStatusDot").removeClass("bg-green-500").addClass("bg-red-500");
    $("#wsStatusText")
      .text("Disconnected")
      .removeClass("text-green-400")
      .addClass("text-red-400");
  }
}

function connectToWebSocket() {
  stompClient = new StompJs.Client({
    webSocketFactory: () => new SockJS("/websocket"),
    debug: () => {},
  });

  stompClient.onConnect = function (frame) {
    console.log("Connected: " + frame);
    setWsStatus(true);

    stompClient.subscribe("/topic/customer/added", function (event) {
      addCustomer(JSON.parse(event.body));
    });

    stompClient.subscribe("/topic/customer/updated", function (event) {
      updateCustomer(JSON.parse(event.body));
    });

    stompClient.subscribe("/topic/customer/deleted", function (event) {
      removeCustomer(JSON.parse(event.body));
    });

    stompClient.subscribe("/topic/restaurant/added", function (event) {
      addRestaurant(JSON.parse(event.body));
    });

    stompClient.subscribe("/topic/restaurant/updated", function (event) {
      updateRestaurant(JSON.parse(event.body));
    });

    stompClient.subscribe("/topic/restaurant/deleted", function (event) {
      removeRestaurant(JSON.parse(event.body));
    });

    stompClient.subscribe("/topic/restaurant/dish/added", function (event) {
      addRestaurantDish(JSON.parse(event.body));
    });

    stompClient.subscribe("/topic/restaurant/dish/updated", function (event) {
      updateRestaurantDish(JSON.parse(event.body));
    });

    stompClient.subscribe("/topic/restaurant/dish/deleted", function (event) {
      removeRestaurantDish(JSON.parse(event.body));
    });

    stompClient.subscribe("/topic/order/created", function (event) {
      addOrder(JSON.parse(event.body));
    });
  };

  stompClient.onStompError = function (frame) {
    console.log("STOMP error: " + frame);
    setWsStatus(false);
  };

  stompClient.onWebSocketClose = function () {
    showModal(
      $("#alertModal"),
      "WebSocket Disconnected",
      "WebSocket is disconnected. Maybe, food-ordering-service is down or restarting",
    );
    setWsStatus(false);
  };

  stompClient.activate();
}

function disconnectWebSocket() {
  if (stompClient !== null) {
    stompClient.deactivate();
  }
  setWsStatus(false);
  console.log("Disconnected");
}

function loadCustomers() {
  $.ajax({
    url: foodOrderingServiceApiBaseUrl.concat("/customers"),
    contentType: "application/json",
    success: function (data) {
      data.forEach((customer) => {
        addCustomer(customer);
      });
    },
  });
}

function loadRestaurants() {
  $.ajax({
    url: foodOrderingServiceApiBaseUrl.concat("/restaurants"),
    contentType: "application/json",
    success: function (data) {
      data.forEach((restaurant) => {
        addRestaurant(restaurant);
        restaurant.dishes
          .map((dish) => {
            return {
              restaurantId: restaurant.id,
              dishId: dish.id,
              dishName: dish.name,
              dishPrice: dish.price,
            };
          })
          .map((dish) => addRestaurantDish(dish));
      });
    },
  });
}

function loadOrders() {
  $.ajax({
    url: foodOrderingServiceApiBaseUrl.concat("/orders"),
    contentType: "application/json",
    success: function (data) {
      data.forEach((order) => {
        addOrder(order);
      });
    },
  });
}

function addOrder(order) {
  const items = order.items
    .map(
      (item) =>
        item.quantity +
        "x " +
        item.dishName +
        " " +
        accounting.formatMoney(item.dishPrice) +
        " (" +
        accounting.formatMoney(item.quantity * item.dishPrice) +
        ")",
    )
    .map((description) => "<li>" + description + "</li>")
    .join("");

  const row =
    '<tr class="border-b border-gray-100">' +
    '<td class="py-2 px-4 text-gray-500">' +
    order.id +
    "</td>" +
    '<td class="py-2 px-4"><strong>' +
    order.customerName +
    "</strong></td>" +
    '<td class="py-2 px-4">' +
    order.customerAddress +
    "</td>" +
    '<td class="py-2 px-4"><strong>' +
    order.restaurantName +
    "</strong></td>" +
    '<td class="py-2 px-4">' +
    order.status +
    "</td>" +
    '<td class="py-2 px-4 text-right font-medium">' +
    accounting.formatMoney(order.total) +
    "</td>" +
    '<td class="py-2 px-4">' +
    dayjs(order.createdAt).format("YYYY-MM-DD HH:mm:ss") +
    "</td>" +
    '<td class="py-2 px-4"><ul class="list-disc list-inside text-xs text-gray-600">' +
    items +
    "</ul></td>" +
    "</tr>";

  $("#orderTable").find("tbody").prepend(row);
}

function addCustomer(customer) {
  dataStore.customers[customer.id] = customer;
  const $menu = $("#customerDropdown").find(".dropdown-menu");
  $menu.append(
    '<div class="dropdown-item px-3 py-2 text-sm text-gray-700 hover:bg-gray-100 cursor-pointer" data-value="' +
      customer.id +
      '">' +
      customer.name +
      "</div>",
  );
}

function updateCustomer(customer) {
  dataStore.customers[customer.id] = customer;
  $("#customerDropdown")
    .find('[data-value="' + customer.id + '"]')
    .text(customer.name);
}

function removeCustomer(customer) {
  delete dataStore.customers[customer.id];
  $("#customerDropdown")
    .find('[data-value="' + customer.id + '"]')
    .remove();
}

function addRestaurant(restaurant) {
  dataStore.restaurants[restaurant.id] = restaurant;
  const card =
    '<div class="restaurant-card px-4 py-3 bg-white border border-gray-200 rounded-lg cursor-pointer hover:border-teal-400 hover:shadow-sm transition-all flex items-center justify-between" data-id="' +
    restaurant.id +
    '">' +
    '<span class="text-sm font-medium text-gray-900">' +
    restaurant.name +
    "</span>" +
    '<svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/></svg>' +
    "</div>";
  $("#restaurantList").append(card);
}

function updateRestaurant(restaurant) {
  dataStore.restaurants[restaurant.id] = restaurant;
  $('.restaurant-card[data-id="' + restaurant.id + '"]')
    .find("span:first")
    .text(restaurant.name);
}

function removeRestaurant(restaurant) {
  delete dataStore.restaurants[restaurant.id];
  $('.restaurant-card[data-id="' + restaurant.id + '"]').remove();
}

function addRestaurantDish(dish) {
  if (!dataStore.dishes[dish.restaurantId]) {
    dataStore.dishes[dish.restaurantId] = [];
  }
  dataStore.dishes[dish.restaurantId].push({
    dishId: dish.dishId,
    dishName: dish.dishName,
    dishPrice: dish.dishPrice,
  });
  if (
    wizardState.currentStep === 3 &&
    wizardState.restaurantId === dish.restaurantId
  ) {
    renderDishRows();
    updateStep3Total();
  }
}

function updateRestaurantDish(dish) {
  const dishList = dataStore.dishes[dish.restaurantId];
  if (dishList) {
    const idx = dishList.findIndex((d) => d.dishId === dish.dishId);
    if (idx >= 0) {
      dishList[idx] = {
        dishId: dish.dishId,
        dishName: dish.dishName,
        dishPrice: dish.dishPrice,
      };
    }
  }
  if (
    wizardState.currentStep === 3 &&
    wizardState.restaurantId === dish.restaurantId
  ) {
    renderDishRows();
    updateStep3Total();
  }
}

function removeRestaurantDish(dish) {
  const dishList = dataStore.dishes[dish.restaurantId];
  if (dishList) {
    dataStore.dishes[dish.restaurantId] = dishList.filter(
      (d) => d.dishId !== dish.dishId,
    );
  }
  if (
    wizardState.currentStep === 3 &&
    wizardState.restaurantId === dish.restaurantId
  ) {
    renderDishRows();
    updateStep3Total();
  }
}

function renderDishRows() {
  const dishes = dataStore.dishes[wizardState.restaurantId] || [];
  const $tbody = $("#dishTableBody").empty();
  dishes.forEach((dish) => {
    $tbody.append(
      '<tr id="' +
        dish.dishId +
        '" class="border-b border-gray-100 dish-row">' +
        '<td class="py-2 px-2 text-center">' +
        '<input type="checkbox" class="dish-checkbox rounded border-gray-300 text-teal-500 focus:ring-teal-500">' +
        "</td>" +
        '<td class="py-2 px-2 name text-sm">' +
        dish.dishName +
        "</td>" +
        '<td class="py-2 px-2 price text-sm text-gray-600">' +
        accounting.formatMoney(dish.dishPrice) +
        "</td>" +
        '<td class="py-2 px-2">' +
        '<input type="number" value="1" min="1" max="10" class="dish-qty w-16 px-2 py-1 border border-gray-300 rounded text-sm">' +
        "</td>" +
        '<td class="py-2 px-2"></td>' +
        "</tr>",
    );
  });
}

function updateStep3Total() {
  let total = 0;
  $("#dishTableBody")
    .find(".dish-row")
    .each(function () {
      const $tr = $(this);
      if ($tr.find(".dish-checkbox").prop("checked")) {
        const price = accounting.unformat($tr.find(".price").text());
        const quantity = parseInt($tr.find(".dish-qty").val()) || 0;
        total += price * quantity;
      }
    });
  $("#step3Total").text(total > 0 ? accounting.formatMoney(total) : "");
  updateStep3Next();
}

function updateStep3Next() {
  let hasChecked = false;
  $("#dishTableBody")
    .find(".dish-checkbox")
    .each(function () {
      if ($(this).prop("checked")) hasChecked = true;
    });
  $("#step3Next").prop("disabled", !hasChecked);
}

function getSelectedItemsFromStep3() {
  const items = [];
  $("#dishTableBody")
    .find(".dish-row")
    .each(function () {
      const $tr = $(this);
      if ($tr.find(".dish-checkbox").prop("checked")) {
        const dishId = $tr.attr("id");
        const dishName = $tr.find(".name").text();
        const price = accounting.unformat($tr.find(".price").text());
        const quantity = parseInt($tr.find(".dish-qty").val()) || 1;
        items.push({ dishId, dishName, price, quantity });
      }
    });
  return items;
}

function renderReview() {
  const items = getSelectedItemsFromStep3();
  let total = 0;
  const $list = $("#reviewItems").empty();
  items.forEach((item) => {
    const lineTotal = item.price * item.quantity;
    total += lineTotal;
    $list.append(
      '<li class="text-sm flex justify-between"><span>' +
        item.quantity +
        "x " +
        item.dishName +
        '</span><span class="text-gray-600">' +
        accounting.formatMoney(lineTotal) +
        "</span></li>",
    );
  });
  $("#reviewCustomer").text(wizardState.customerName);
  $("#reviewRestaurant").text(wizardState.restaurantName);
  $("#reviewTotal").text(accounting.formatMoney(total));
}

function goToStep(step) {
  wizardState.currentStep = step;

  $(".step-circle").each(function () {
    const s = $(this).data("step");
    if (s < step) {
      $(this)
        .removeClass("bg-gray-200 text-gray-500")
        .addClass("bg-teal-500 text-white");
      $(this).find(".step-number").addClass("hidden");
      $(this).find(".step-check").removeClass("hidden");
    } else if (s === step) {
      $(this)
        .removeClass("bg-gray-200 text-gray-500")
        .addClass("bg-teal-500 text-white");
      $(this).find(".step-number").removeClass("hidden");
      $(this).find(".step-check").addClass("hidden");
    } else {
      $(this)
        .removeClass("bg-teal-500 text-white")
        .addClass("bg-gray-200 text-gray-500");
      $(this).find(".step-number").removeClass("hidden");
      $(this).find(".step-check").addClass("hidden");
    }
  });

  $(".step-label").each(function () {
    const s = $(this).data("step");
    $(this)
      .toggleClass("text-teal-600", s <= step)
      .toggleClass("text-gray-400", s > step);
  });

  $(".step-line").each(function () {
    const from = $(this).data("from");
    $(this)
      .toggleClass("bg-teal-500", from < step)
      .toggleClass("bg-gray-300", from >= step);
  });

  $(".step-content").addClass("hidden");
  $("#step" + step + "Content").removeClass("hidden");

  if (step === 1) {
    $("#step1Next").prop("disabled", !wizardState.customerId);
  }
  if (step === 2) {
    $("#step2Next").prop("disabled", !wizardState.restaurantId);
  }
  if (step === 3) {
    renderDishRows();
    updateStep3Total();
  }
  if (step === 4) {
    renderReview();
  }
}

function resetWizard() {
  wizardState.customerId = null;
  wizardState.customerName = null;
  wizardState.restaurantId = null;
  wizardState.restaurantName = null;

  $("#customerDropdown")
    .find(".dropdown-text")
    .text("Select Customer")
    .removeClass("text-gray-900")
    .addClass("text-gray-500");
  $("#customerDropdown").find(".dropdown-value").val("");
  $(".restaurant-card")
    .removeClass("border-teal-500 bg-teal-50")
    .addClass("border-gray-200");
  $("#step1Next").prop("disabled", true);
  $("#step2Next").prop("disabled", true);
  $("#step3Next").prop("disabled", true);
  $("#step3Total").text("");
  $("#dishTableBody").empty();

  goToStep(1);
}

function showModal($modal, header, description, fnApprove, fnClose) {
  $modal.removeClass("hidden");
  $modal.find(".modal-header").text(header);
  $modal.find(".modal-content").text(description);
  $modal.data("fn-approve", fnApprove);
  $modal.data("fn-close", fnClose);
}

$(document).on("click", ".modal-yes, .modal-ok", function () {
  const $modal = $(this).closest('[id$="Modal"]');
  const fn = $modal.data("fn-approve");
  if (typeof fn === "function") fn();
  $modal.addClass("hidden");
  const closeFn = $modal.data("fn-close");
  if (typeof closeFn === "function") closeFn();
});

$(document).on("click", ".modal-no, .modal-backdrop", function () {
  $(this).closest('[id$="Modal"]').addClass("hidden");
});

$(document).on("click", ".dropdown-toggle", function (e) {
  e.stopPropagation();
  $(this).siblings(".dropdown-menu").toggleClass("hidden");
});

$(document).on("click", ".dropdown-menu .dropdown-item", function () {
  const val = $(this).data("value");
  const text = $(this).text();
  const $container = $(this).closest(".relative");
  $container
    .find(".dropdown-text")
    .text(text)
    .removeClass("text-gray-500")
    .addClass("text-gray-900");
  $container.find(".dropdown-value").val(val);
  $container.find(".dropdown-menu").addClass("hidden");
  wizardState.customerId = val;
  wizardState.customerName = text;
  $("#step1Next").prop("disabled", false);
});

$(document).click(function () {
  $(".dropdown-menu").addClass("hidden");
});

$(document).on("click", ".restaurant-card", function () {
  $(".restaurant-card")
    .removeClass("border-teal-500 bg-teal-50")
    .addClass("border-gray-200");
  $(this).addClass("border-teal-500 bg-teal-50").removeClass("border-gray-200");
  wizardState.restaurantId = $(this).data("id");
  wizardState.restaurantName = $(this).find("span:first").text();
  $("#step2Next").prop("disabled", false);
});

$(function () {
  setWsStatus(false);
  loadCustomers();
  loadRestaurants();
  loadOrders();

  connectToWebSocket();

  $(document).on("click", ".tab-item", function () {
    $(".tab-item").removeClass(
      "active border-teal-500 text-teal-600 border-transparent text-gray-500",
    );
    $(this).addClass("border-teal-500 text-teal-600");
    $(".tab-panel").addClass("hidden");
    $("#" + $(this).data("tab-panel")).removeClass("hidden");
  });

  $("#step1Next").click(function () {
    goToStep(2);
  });
  $("#step2Back").click(function () {
    goToStep(1);
  });
  $("#step2Next").click(function () {
    goToStep(3);
  });
  $("#step3Back").click(function () {
    goToStep(2);
  });
  $("#step3Next").click(function () {
    goToStep(4);
  });
  $("#step4Back").click(function () {
    goToStep(3);
  });

  $("#dishTableBody").on("change", ".dish-checkbox", function () {
    updateStep3Total();
  });

  $("#dishTableBody").on("change", ".dish-qty", function () {
    updateStep3Total();
  });

  $("#btnPlaceOrder").click(function () {
    const items = getSelectedItemsFromStep3();
    if (items.length === 0) {
      showModal(
        $("#alertModal"),
        "No dishes selected",
        "Please select at least one dish.",
      );
      return;
    }
    if (!wizardState.customerId) {
      showModal(
        $("#alertModal"),
        "No customer selected",
        "Please select a customer.",
      );
      return;
    }

    const orderRequest = {
      customerId: wizardState.customerId,
      restaurantId: wizardState.restaurantId,
      items: items.map((i) => ({ dishId: i.dishId, quantity: i.quantity })),
    };

    $.ajax({
      type: "POST",
      url: foodOrderingServiceApiBaseUrl.concat("/orders"),
      contentType: "application/json",
      data: JSON.stringify(orderRequest),
      success: function (data) {
        showModal(
          $("#alertModal"),
          "Order Submitted",
          'Order submitted successfully. The order id is "' + data + '"',
          null,
          function () {
            resetWizard();
          },
        );
      },
    });
  });

  $(".connWebSocket").click(function () {
    if (stompClient !== null) {
      disconnectWebSocket();
    }
    connectToWebSocket();
  });
});
