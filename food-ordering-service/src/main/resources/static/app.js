let stompClient = null

const foodOrderingServiceApiBaseUrl = "http://localhost:9082/api"

function setWsStatus(connected) {
    if (connected) {
        $('#wsStatusDot').removeClass('bg-red-500').addClass('bg-green-500')
        $('#wsStatusText').text('Connected').removeClass('text-red-400').addClass('text-green-400')
    } else {
        $('#wsStatusDot').removeClass('bg-green-500').addClass('bg-red-500')
        $('#wsStatusText').text('Disconnected').removeClass('text-green-400').addClass('text-red-400')
    }
}

function connectToWebSocket() {
    stompClient = new StompJs.Client({
        webSocketFactory: () => new SockJS('/websocket'),
        debug: () => {},
    })

    stompClient.onConnect = function (frame) {
        console.log('Connected: ' + frame)
        setWsStatus(true)

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
    }

    stompClient.onStompError = function (frame) {
        console.log('STOMP error: ' + frame)
        setWsStatus(false)
    }

    stompClient.onWebSocketClose = function () {
        showModal($('#alertModal'), 'WebSocket Disconnected', 'WebSocket is disconnected. Maybe, food-ordering-service is down or restarting')
        setWsStatus(false)
    }

    stompClient.activate()
}

function disconnectWebSocket() {
    if (stompClient !== null) {
        stompClient.deactivate()
    }
    setWsStatus(false)
    console.log('Disconnected')
}

function loadCustomers() {
    $.ajax({
        url: foodOrderingServiceApiBaseUrl.concat("/customers"),
        contentType: "application/json",
        success: function(data) {
            data.forEach(customer => {
                addCustomer(customer)
            })
        }
    })
}

function loadRestaurants() {
    $.ajax({
        url: foodOrderingServiceApiBaseUrl.concat("/restaurants"),
        contentType: "application/json",
        success: function(data) {
            data.forEach(restaurant => {
                addRestaurant(restaurant)
                restaurant.dishes.map(dish => {
                    return {restaurantId: restaurant.id, dishId: dish.id, dishName: dish.name, dishPrice: dish.price}
                })
                .map(dish => addRestaurantDish(dish))
            })
        }
    })
}

function loadOrders() {
    $.ajax({
        url: foodOrderingServiceApiBaseUrl.concat("/orders"),
        contentType: "application/json",
        success: function(data) {
            data.forEach(order => {
                addOrder(order)
            })
        }
    })
}

function addOrder(order) {
    const items = order.items
        .map(item => item.quantity + "x " + item.dishName + " " + accounting.formatMoney(item.dishPrice) + " (" + accounting.formatMoney(item.quantity * item.dishPrice) + ")")
        .map(description => '<li>' + description + '</li>')
        .join('')

    const row =
        '<tr class="border-b border-gray-100">'+
            '<td class="py-2 px-4 text-gray-500">'+order.id+'</td>'+
            '<td class="py-2 px-4"><strong>'+order.customerName+'</strong></td>'+
            '<td class="py-2 px-4">'+order.customerAddress+'</td>'+
            '<td class="py-2 px-4"><strong>'+order.restaurantName+'</strong></td>'+
            '<td class="py-2 px-4">'+order.status+'</td>'+
            '<td class="py-2 px-4 text-right font-medium">'+accounting.formatMoney(order.total)+'</td>'+
            '<td class="py-2 px-4">'+dayjs(order.createdAt).format('YYYY-MM-DD HH:mm:ss')+'</td>'+
            '<td class="py-2 px-4"><ul class="list-disc list-inside text-xs text-gray-600">'+items+'</ul></td>'+
        '</tr>'

    $('#orderTable').find('tbody').prepend(row)
}

function addCustomer(customer) {
    const $menu = $('#customerDropdown').find('.dropdown-menu')
    $menu.append('<div class="dropdown-item px-3 py-2 text-sm text-gray-700 hover:bg-gray-100 cursor-pointer" data-value="'+customer.id+'">'+customer.name+'</div>')
}

function updateCustomer(customer) {
    $('#customerDropdown').find('[data-value="'+customer.id+'"]').text(customer.name)
}

function removeCustomer(customer) {
    $('#customerDropdown').find('[data-value="'+customer.id+'"]').remove()
}

function addRestaurant(restaurant) {
    const row =
        '<div class="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden">'+
            '<div class="accordion-title px-4 py-3 cursor-pointer hover:bg-gray-50 flex items-center justify-between select-none" data-id="'+restaurant.id+'">'+
                '<h3 class="text-base font-semibold text-gray-900">'+restaurant.name+'</h3>'+
                '<svg class="accordion-arrow w-5 h-5 text-gray-400 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/></svg>'+
            '</div>'+
            '<div class="accordion-content hidden px-4 pb-4">'+
                '<table class="w-full text-sm border-collapse">'+
                    '<tbody></tbody>'+
                    '<tfoot>'+
                        '<tr>'+
                            '<th class="w-10"></th>'+
                            '<th class="text-left"></th>'+
                            '<th class="text-left"></th>'+
                            '<th class="total text-right font-semibold text-teal-600"></th>'+
                            '<th class="w-24">'+
                                '<button class="btnOrder w-full px-3 py-1.5 bg-teal-500 hover:bg-teal-600 text-white rounded text-xs font-medium transition-colors">Order</button>'+
                            '</th>'+
                        '</tr>'+
                    '</tfoot>'+
                '</table>'+
            '</div>'+
        '</div>'
    $('#restaurantAccordion').prepend(row)
}

function updateRestaurant(restaurant) {
    $('.accordion-title[data-id="'+restaurant.id+'"]').find('h3').text(restaurant.name)
}

function removeRestaurant(restaurant) {
    $('.accordion-title[data-id="'+restaurant.id+'"]').closest('.overflow-hidden').remove()
}

function getRestaurantDishRow(dish) {
    return (
        '<tr id="'+dish.dishId+'" class="dish border-b border-gray-100">'+
            '<td class="py-1.5 px-2 text-center">'+
                '<input type="checkbox" class="rounded border-gray-300 text-teal-500 focus:ring-teal-500">'+
            '</td>'+
            '<td class="py-1.5 px-2 name">'+dish.dishName+'</td>'+
            '<td class="py-1.5 px-2 price">'+accounting.formatMoney(dish.dishPrice)+'</td>'+
            '<td class="py-1.5 px-2 quantity">'+
                '<input type="number" value="1" min="1" max="10" class="w-16 px-2 py-1 border border-gray-300 rounded text-sm">'+
            '</td>'+
            '<td></td>'+
        '</tr>'
    )
}

function addRestaurantDish(dish) {
    const $content = $('.accordion-title[data-id="'+dish.restaurantId+'"]').next('.accordion-content')
    $content.find('tbody').prepend(getRestaurantDishRow(dish))
}

function updateRestaurantDish(dish) {
    const $dish = $('#'+dish.dishId)
    $dish.find('td.name').text(dish.dishName)
    $dish.find('td.price').text(accounting.formatMoney(dish.dishPrice))
}

function removeRestaurantDish(dish) {
    $('#'+dish.dishId).remove()
}

function getOrderRequest($this) {
    const customerId = $('#customerDropdown').find('.dropdown-value').val()

    const $accordionContent = $this.closest('.accordion-content')
    const $accordionTitle = $accordionContent.prev('.accordion-title')
    const restaurantId = $accordionTitle.data('id')

    const items = []
    $accordionContent.find('tr.dish').each(function() {
        const dishChecked = $(this).find('input[type="checkbox"]').prop('checked')
        if (dishChecked) {
            const dishId = $(this).attr('id')
            const quantity = $(this).find('input[type="number"]').val()
            items.push({dishId, quantity})
        }
    })

    return {customerId, restaurantId, items}
}

function validOrderRequest(orderRequest) {
    if (!orderRequest.customerId || orderRequest.customerId.length === 0) {
        showModal($('#alertModal'), 'Select a customer', 'Please select a Customer in the dropbox')
        return false
    }
    if (orderRequest.items.length === 0) {
        showModal($('#alertModal'), 'Choose some dishes', 'Please select some dishes of a restaurant')
        return false
    }
    return true
}

function handlePreviewTotalOrder($this) {
    const $table = $this.closest('table')

    let total = 0
    $table.find('tr.dish').each(function() {
        const $tr = $(this)
        const dishChecked = $tr.find('input[type="checkbox"]').prop('checked')
        if (dishChecked) {
            const price = accounting.unformat($tr.find('td.price').text())
            const quantity = $tr.find('input[type="number"]').val()
            total += price * quantity
        }
    })

    const $total = $table.find('.total')
    if (total > 0) {
        $total.text(accounting.formatMoney(total))
    } else {
        $total.text('')
    }
}

function showModal($modal, header, description, fnApprove) {
    $modal.removeClass('hidden')
    $modal.find('.modal-header').text(header)
    $modal.find('.modal-content').text(description)
    $modal.data('fn-approve', fnApprove)
}

$(document).on('click', '.modal-yes, .modal-ok', function() {
    const $modal = $(this).closest('[id$="Modal"]')
    const fn = $modal.data('fn-approve')
    if (typeof fn === 'function') fn()
    $modal.addClass('hidden')
})

$(document).on('click', '.modal-no, .modal-backdrop', function() {
    $(this).closest('[id$="Modal"]').addClass('hidden')
})

$(document).on('click', '.accordion-title', function() {
    const $content = $(this).next('.accordion-content')
    const $arrow = $(this).find('.accordion-arrow')
    $content.toggleClass('hidden')
    $arrow.toggleClass('rotate-180')
})

$(document).on('click', '.dropdown-toggle', function(e) {
    e.stopPropagation()
    $(this).siblings('.dropdown-menu').toggleClass('hidden')
})

$(document).on('click', '.dropdown-menu .dropdown-item', function() {
    const val = $(this).data('value')
    const text = $(this).text()
    const $container = $(this).closest('.relative')
    $container.find('.dropdown-text').text(text).removeClass('text-gray-500').addClass('text-gray-900')
    $container.find('.dropdown-value').val(val)
    $container.find('.dropdown-menu').addClass('hidden')
})

$(document).click(function() {
    $('.dropdown-menu').addClass('hidden')
})

$(function () {
    setWsStatus(false)
    loadCustomers()
    loadRestaurants()
    loadOrders()

    connectToWebSocket()

    $(document).on('click', '.tab-item', function() {
        $('.tab-item').removeClass('active border-teal-500 text-teal-600 border-transparent text-gray-500')
        $(this).addClass('border-teal-500 text-teal-600')
        $('.tab-panel').addClass('hidden')
        $('#' + $(this).data('tab-panel')).removeClass('hidden')
    })

    $('#restaurantAccordion').on('click', '.btnOrder', function() {
        const orderRequest = getOrderRequest($(this))
        if (validOrderRequest(orderRequest)) {
            $.ajax({
                type: 'POST',
                url: foodOrderingServiceApiBaseUrl.concat("/orders"),
                contentType: "application/json",
                data: JSON.stringify(orderRequest),
                success: function(data) {
                    showModal($('#alertModal'), 'Order Submitted', 'Order submitted successfully. The order id is "' + data + '"')
                }
            })
        }
    })

    $('#restaurantAccordion').on('change', 'input[type="checkbox"]', function() {
        handlePreviewTotalOrder($(this))
    })

    $('#restaurantAccordion').on('change', 'input[type="number"]', function() {
        handlePreviewTotalOrder($(this))
    })

    $('.connWebSocket').click(function() {
        if (stompClient !== null) {
            disconnectWebSocket()
        }
        connectToWebSocket()
    })
})
