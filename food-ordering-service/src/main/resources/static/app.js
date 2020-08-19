const foodOrderingServiceApiBaseUrl = "http://localhost:9082/api"

function connectToWebSocket() {
    const socket = new SockJS('/websocket')
    const stompClient = Stomp.over(socket)

    stompClient.connect({},
        function (frame) {
            console.log('Connected: ' + frame)
            $('.connWebSocket').find('i').removeClass('red').addClass('green')

            stompClient.subscribe('/topic/customer/added', function (event) {
                addCustomer(JSON.parse(event.body))
            })

            stompClient.subscribe('/topic/customer/updated', function (event) {
                updateCustomer(JSON.parse(event.body))
            })

            stompClient.subscribe('/topic/customer/deleted', function (event) {
                removeCustomer(JSON.parse(event.body))
            })

            stompClient.subscribe('/topic/restaurant/added', function (event) {
                addRestaurant(JSON.parse(event.body))
            })

            stompClient.subscribe('/topic/restaurant/updated', function (event) {
                updateRestaurant(JSON.parse(event.body))
            })

            stompClient.subscribe('/topic/restaurant/deleted', function (event) {
                removeRestaurant(JSON.parse(event.body))
            })

            stompClient.subscribe('/topic/restaurant/dish/added', function (event) {
                addRestaurantDish(JSON.parse(event.body))
            })

            stompClient.subscribe('/topic/restaurant/dish/updated', function (event) {
                updateRestaurantDish(JSON.parse(event.body))
            })

            stompClient.subscribe('/topic/restaurant/dish/deleted', function (event) {
                removeRestaurantDish(JSON.parse(event.body))
            })

            stompClient.subscribe('/topic/order/created', function (event) {
                addOrder(JSON.parse(event.body))
            })
        },
        function() {
            showModal($('.modal.alert'), 'WebSocket Disconnected', 'WebSocket is disconnected. Maybe, food-ordering-service is down or restarting')
            $('.connWebSocket').find('i').removeClass('green').addClass('red')
        }
    )
}

function loadCustomers() {
    $.ajax({
        url: foodOrderingServiceApiBaseUrl.concat("/customers"),
        contentType: "application/json",
        success: function(data, textStatus, jqXHR) {
            data.forEach(customer => {
                addCustomer(customer)
            })
        },
        error: function (jqXHR, textStatus, errorThrown) {}
    })
}

function loadRestaurants() {
    $.ajax({
        url: foodOrderingServiceApiBaseUrl.concat("/restaurants"),
        contentType: "application/json",
        success: function(data, textStatus, jqXHR) {
            data.forEach(restaurant => {
                addRestaurant(restaurant)
                restaurant.dishes.map(dish => {
                    return {restaurantId: restaurant.id, dishId: dish.id, dishName: dish.name, dishPrice: dish.price}
                })
                .map(dish => addRestaurantDish(dish))
            })
        },
        error: function (jqXHR, textStatus, errorThrown) {}
    })
}

function loadOrders() {
    $.ajax({
        url: foodOrderingServiceApiBaseUrl.concat("/orders"),
        contentType: "application/json",
        success: function(data, textStatus, jqXHR) {
            data.forEach(order => {
                addOrder(order)
            })
        },
        error: function (jqXHR, textStatus, errorThrown) {}
    })
}

function addOrder(order) {
    const items = order.items
        .map(item => item.quantity + "x " + item.dishName + " " + accounting.formatMoney(item.dishPrice) + " (" + accounting.formatMoney(item.quantity*item.dishPrice) + ")")
        .map(description => '<li>' + description + '</li>')
        .join('')

    const row =
        '<tr>'+
            '<td>'+order.id+'</th>'+
            '<td><strong>'+order.customerName+'</strong></th>'+
            '<td>'+order.customerAddress+'</th>'+
            '<td><strong>'+order.restaurantName+'</strong></th>'+
            '<td>'+order.status+'</th>'+
            '<td>'+accounting.formatMoney(order.total)+'</th>'+
            '<td>'+moment(order.createdAt).format('YYYY-MM-DD HH:mm:ss')+'</th>'+
            '<td><ul class="ui list">'+items+'</ul></td>'+
        '</tr>'

    $('#orderTable').find('tbody').prepend(row)
}

function addCustomer(customer) {
    $('.ui.dropdown').find('div.menu').append('<div class="item" id="'+customer.id+'" data-value="'+customer.id+'">'+customer.name+'</div>')
}

function updateCustomer(customer) {
    $('#'+customer.id).text(customer.name)
}

function removeCustomer(customer) {
    $('#'+customer.id).remove()
}

function addRestaurant(restaurant) {
    const row =
        '<div id="'+restaurant.id+'_title" class="title">'+
            '<h3>'+restaurant.name+'</h3>'+
        '</div>'+
        '<div id="'+restaurant.id+'_content" class="content">'+
            '<table class="ui compact table unstackable">'+
                '<tbody></tbody>'+
                '<tfoot>'+
                    '<tr>'+
                        '<th class="one wide"></th>'+
                        '<th class="five wide"></th>'+
                        '<th class="three wide"></th>'+
                        '<th class="total four wide"></th>'+
                        '<th class="three wide">'+
                            '<button class="btnOrder ui teal fluid small button">Order</button>'+
                        '</th>'+
                    '</tr>'+
                '</tfoot>'+
            '</table>'+
        '</div>'
    $('.ui.accordion').prepend(row)
}

function updateRestaurant(restaurant) {
    $('#'+restaurant.id+'_title').find('h3').text(restaurant.name)
}

function removeRestaurant(restaurant) {
    $('#'+restaurant.id+'_title').remove()
    $('#'+restaurant.id+'_content').remove()
}

function getRestaurantDishRow(dish) {
    return (
        '<tr id="'+dish.dishId+'" class="dish">'+
            '<td class="ui center aligned">'+
                '<input type="checkbox">'+
            '</td>'+
            '<td class="name">'+dish.dishName+'</td>'+
            '<td class="price">'+accounting.formatMoney(dish.dishPrice)+'</td>'+
            '<td class="quantity">'+
                '<div class="ui small input">'+
                    '<input type="number" value="1" min="1" max="10">'+
                '</div>'+
            '</td>'+
            '<td></td>'+
        '</tr>'
    )
}

function addRestaurantDish(dish) {
    $('#'+dish.restaurantId+'_content').find('tbody').prepend(getRestaurantDishRow(dish))
}

function updateRestaurantDish(dish) {
    const $dish = $('#'+dish.dishId);
    $dish.find('td.name').text(dish.dishName)
    $dish.find('td.price').text(accounting.formatMoney(dish.dishPrice))
}

function removeRestaurantDish(dish) {
    $('#'+dish.dishId).remove()
}

function getOrderRequest($this) {
    const customerId = $(".dropdown").dropdown('get value')

    const $restaurantContent = $this.closest('.content')
    const restaurantContentId = $restaurantContent.attr('id')
    const restaurantId = restaurantContentId.substring(0, restaurantContentId.indexOf('_'))

    const items = []
    $restaurantContent.find('tr.dish').each(function(index, tr) {
        const dishChecked = $(tr).find('input[type="checkbox"]').prop('checked')
        if (dishChecked) {
            const dishId = $(tr).attr('id')
            const quantity = $(tr).find('input[type="number"]').val()
            items.push({dishId, quantity})
        }
    });

    return { customerId, restaurantId, items }
}

function validOrderRequest(orderRequest) {
    if (orderRequest.customerId.length === 0) {
        showModal($('.modal.alert'), 'Select a customer', 'Please select a Customer in the dropbox')
        return false
    }
    if (orderRequest.items.length === 0) {
        showModal($('.modal.alert'), 'Choose some dishes', 'Please select some dishes of a restaurant')
        return false
    }
    return true
}

function handlePreviewTotalOrder($this) {
    const $table = $this.closest('table')

    let total = 0
    $table.find('tr.dish').each(function(index, tr) {
        const $tr = $(tr)
        const dishChecked = $tr.find('input[type="checkbox"]').prop('checked')
        if (dishChecked) {
            const price = accounting.unformat($tr.find('td.price').text())
            const quantity = $tr.find('input[type="number"]').val()
            total += price * quantity
        }
    });

    const $total = $table.find('.total')
    if (total > 0) {
        $total.text(accounting.formatMoney(total))
    } else {
        $total.text('')
    }
}

function showModal($modal, header, description, fnApprove) {
    $modal.find('.header').text(header)
    $modal.find('.content').text(description)
    $modal.modal({
        onApprove: function() {
            fnApprove && fnApprove()
        }
    }).modal('show')
}

$(function () {
    loadCustomers()
    loadRestaurants()
    loadOrders()

    connectToWebSocket()

    $('.menu .item').tab()
    $('.ui.dropdown').dropdown()
    $('.ui.accordion').accordion()

    $('.accordion').on('click', '.btnOrder', function() {
        const orderRequest = getOrderRequest($(this))
        if (validOrderRequest(orderRequest)) {
            $.ajax({
                type: 'POST',
                url: foodOrderingServiceApiBaseUrl.concat("/orders"),
                contentType: "application/json",
                data: JSON.stringify(orderRequest),
                success: function(data, textStatus, jqXHR) {
                    showModal($('.modal.alert'), 'Order Submitted', 'Order submitted successfully. The order id is "' + data + '"')
                },
                error: function (jqXHR, textStatus, errorThrown) {}
            })
        }
    })

    $('.accordion').on('change', 'input[type="checkbox"]', function() {
        handlePreviewTotalOrder($(this))
    })

    $('.accordion').on('change', 'input[type="number"]', function() {
        handlePreviewTotalOrder($(this))
    })

    $('.connWebSocket').click(function() {
        connectToWebSocket()
    })
})
