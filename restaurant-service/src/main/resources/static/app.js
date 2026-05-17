let stompClient = null

const restaurantServiceApiBaseUrl = "http://localhost:9081/api/restaurants"

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
    }

    stompClient.onStompError = function (frame) {
        console.log('STOMP error: ' + frame)
        setWsStatus(false)
    }

    stompClient.onWebSocketClose = function () {
        showModal($('#alertModal'), 'WebSocket Disconnected', 'WebSocket is disconnected. Maybe, restaurant-service is down or restarting')
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

function loadRestaurants() {
    $.ajax({
        url: restaurantServiceApiBaseUrl,
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

function addRestaurant(restaurant) {
    const row =
        '<div id="'+restaurant.id+'" class="restaurant-card bg-white rounded-xl shadow-sm border border-gray-200 p-4">'+
            '<div class="flex items-center justify-between mb-3">'+
                '<div class="flex items-center gap-1">'+
                    '<button class="btnDelete px-2 py-1 bg-red-500 hover:bg-red-600 text-white rounded text-xs transition-colors">'+
                        '<svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/></svg>'+
                    '</button>'+
                    '<button class="btnEdit px-2 py-1 bg-amber-500 hover:bg-amber-600 text-white rounded text-xs transition-colors">'+
                        '<svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/></svg>'+
                    '</button>'+
                    '<button class="btnAddDish px-2 py-1 bg-teal-500 hover:bg-teal-600 text-white rounded text-xs transition-colors">'+
                        '<svg class="w-3.5 h-3.5 inline mr-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/></svg>'+
                        'Dish'+
                    '</button>'+
                    '<button class="btnOrders px-2 py-1 bg-violet-500 hover:bg-violet-600 text-white rounded text-xs transition-colors">Orders</button>'+
                '</div>'+
                '<div class="text-right">'+
                    '<h3 class="text-base font-semibold text-gray-900">'+restaurant.name+'</h3>'+
                    '<span class="text-xs text-gray-500 font-mono">'+restaurant.id+'</span>'+
                '</div>'+
            '</div>'+
            '<div class="overflow-x-auto">'+
                '<table class="w-full text-xs border-collapse">'+
                    '<tbody></tbody>'+
                '</table>'+
            '</div>'+
        '</div>'
    $('#restaurantList').prepend(row)
}

function getRestaurantDishRow(dish) {
    return (
        '<tr id="'+dish.dishId+'" class="border-b border-gray-100">'+
            '<td class="py-1.5 px-2 text-gray-500">'+dish.dishId+'</td>'+
            '<td class="py-1.5 px-2 name"><strong>'+dish.dishName+'</strong></td>'+
            '<td class="py-1.5 px-2 price">'+accounting.formatMoney(dish.dishPrice)+'</td>'+
            '<td class="py-1.5 px-2 text-right">'+
                '<button class="btnDeleteDish px-1.5 py-0.5 bg-red-500 hover:bg-red-600 text-white rounded text-xs transition-colors">'+
                    '<svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/></svg>'+
                '</button>'+
                '<button class="btnEditDish px-1.5 py-0.5 bg-amber-500 hover:bg-amber-600 text-white rounded text-xs transition-colors">'+
                    '<svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/></svg>'+
                '</button>'+
            '</td>'+
        '</tr>'
    )
}

function updateRestaurant(restaurant) {
    $('#'+restaurant.id).find('h3').text(restaurant.name)
}

function removeRestaurant(restaurant) {
    $('#'+restaurant.id).remove()
}

function addRestaurantDish(dish) {
    $('#'+dish.restaurantId).find('tbody').prepend(getRestaurantDishRow(dish))
}

function updateRestaurantDish(dish) {
    const $dish = $('#'+dish.dishId)
    $dish.find('td.name > strong').text(dish.dishName)
    $dish.find('td.price').text(accounting.formatMoney(dish.dishPrice))
}

function removeRestaurantDish(dish) {
    $('#'+dish.dishId).remove()
}

function fillRestaurantForm(data) {
    $('#restaurantForm input[name="id"]').val(data.id)
    $('#restaurantForm input[name="name"]').val(data.name)
}

function fillRestaurantDishForm(data) {
    $('#restaurantDishForm input[name="restaurantId"]').val(data.restaurantId)
    $('#restaurantDishForm input[name="dishId"]').val(data.dishId)
    $('#restaurantDishForm input[name="dishName"]').val(data.dishName)
    $('#restaurantDishForm input[name="dishPrice"]').val(data.dishPrice)
}

function resetRestaurantForm() {
    $('#restaurantForm input[name="id"]').val('')
    $('#restaurantForm input[name="name"]').val('')
}

function resetRestaurantDishForm() {
    $('#restaurantDishForm input[name="restaurantId"]').val('')
    $('#restaurantDishForm input[name="dishId"]').val('')
    $('#restaurantDishForm input[name="dishName"]').val('')
    $('#restaurantDishForm input[name="dishPrice"]').val('')
}

function validateAndGetRestaurantFormData() {
    const id = $('#restaurantForm input[name="id"]').val()
    const name = $('#restaurantForm input[name="name"]').val()

    if (name.trim().length === 0) {
        showModal($('#alertModal'), 'Missing fields', 'Please inform restaurant Name')
        return null
    }

    return {id, name}
}

function validateAndGetRestaurantDishFormData() {
    const restaurantId = $('#restaurantDishForm input[name="restaurantId"]').val()
    const dishId = $('#restaurantDishForm input[name="dishId"]').val()
    const dishName = $('#restaurantDishForm input[name="dishName"]').val()
    const dishPrice = $('#restaurantDishForm input[name="dishPrice"]').val()

    if (dishName.trim().length === 0 || dishPrice <= 0) {
        showModal($('#alertModal'), 'Missing fields', 'Please inform dish Name and Price')
        return null
    }

    return {restaurantId, dishId, dishName, dishPrice}
}

function showRestaurantForm() {
    $('#restaurantForm').removeClass('hidden')
}

function hideRestaurantForm() {
    $('#restaurantForm').addClass('hidden')
}

function showRestaurantDishForm() {
    $('#restaurantDishForm').removeClass('hidden')
}

function hideRestaurantDishForm() {
    $('#restaurantDishForm').addClass('hidden')
}

function buildRestaurantOrderTable(data) {
    const orders = data.map(order => {
        const items = order.items
            .map(item => item.quantity + "x " + item.dishName + " " + accounting.formatMoney(item.dishPrice) + " (" + accounting.formatMoney(item.quantity * item.dishPrice) + ")")
            .map(description => '<li>' + description + '</li>')
            .join('')
        return (
            '<tr class="border-b border-gray-100">'+
                '<td class="py-1.5 px-3 text-gray-500">'+order.id+'</td>'+
                '<td class="py-1.5 px-3">'+dayjs(order.createdAt).format('YYYY-MM-DD HH:mm:ss')+'</td>'+
                '<td class="py-1.5 px-3">'+order.status+'</td>'+
                '<td class="py-1.5 px-3"><strong>'+order.customerName+'</strong></td>'+
                '<td class="py-1.5 px-3">'+order.customerAddress+'</td>'+
                '<td class="py-1.5 px-3 text-right font-medium">'+accounting.formatMoney(order.total)+'</td>'+
                '<td class="py-1.5 px-3"><ul class="list-disc list-inside text-xs text-gray-600">'+items+'</ul></td>'+
            '</tr>'
        )
    }).join('')

    return (
        '<table class="w-full text-sm border-collapse">'+
            '<thead>'+
                '<tr class="bg-gray-50 border-b border-gray-200">'+
                    '<th class="text-left px-3 py-2 text-xs font-semibold text-gray-500 uppercase tracking-wider">ID</th>'+
                    '<th class="text-left px-3 py-2 text-xs font-semibold text-gray-500 uppercase tracking-wider">CreatedAt</th>'+
                    '<th class="text-left px-3 py-2 text-xs font-semibold text-gray-500 uppercase tracking-wider">Status</th>'+
                    '<th class="text-left px-3 py-2 text-xs font-semibold text-gray-500 uppercase tracking-wider">Customer Name</th>'+
                    '<th class="text-left px-3 py-2 text-xs font-semibold text-gray-500 uppercase tracking-wider">Customer Address</th>'+
                    '<th class="text-right px-3 py-2 text-xs font-semibold text-gray-500 uppercase tracking-wider">Total</th>'+
                    '<th class="text-left px-3 py-2 text-xs font-semibold text-gray-500 uppercase tracking-wider">Items</th>'+
                '</tr>'+
            '</thead>'+
            '<tbody class="divide-y divide-gray-100">'+orders+'</tbody>'+
        '</table>'
    )
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

$(function () {
    setWsStatus(false)
    loadRestaurants()

    connectToWebSocket()

    $('#restaurantForm button[name="btnSave"]').click(function(event) {
        event.preventDefault()
        const restaurantData = validateAndGetRestaurantFormData()
        if (restaurantData == null) {
            return
        }

        let type = "POST"
        let url = restaurantServiceApiBaseUrl
        if (restaurantData.id.length > 0) {
            type = "PATCH"
            url = url.concat("/", restaurantData.id)
        }

        $.ajax({
            type,
            url,
            contentType: "application/json",
            data: JSON.stringify({name: restaurantData.name}),
            success: function() {
                resetRestaurantForm()
            }
        })
    })

    $('#restaurantList').on('click', '.btnDelete', function() {
        const $restaurant = $(this).closest('.restaurant-card')
        const id = $restaurant.attr('id')
        const name = $restaurant.find('h3').text()
        showModal($('#confirmationModal'), 'Delete Restaurant', 'Are you sure you want to delete restaurant "'+name+'"?', function() {
            $.ajax({
                type: "DELETE",
                url: restaurantServiceApiBaseUrl.concat("/", id)
            })
        })
    })

    $('#restaurantList').on('click', '.btnEdit', function() {
        const id = $(this).closest('.restaurant-card').attr('id')
        $.ajax({
            url: restaurantServiceApiBaseUrl.concat("/", id),
            success: function(data) {
                fillRestaurantForm(data)
                showRestaurantForm()
                hideRestaurantDishForm()
                resetRestaurantDishForm()
            }
        })
    })

    $('#restaurantList').on('click', '.btnAddDish', function() {
        const restaurantId = $(this).closest('.restaurant-card').attr('id')
        $('#restaurantDishForm input[name="restaurantId"]').val(restaurantId)
        showRestaurantDishForm()
        hideRestaurantForm()
    })

    $('#restaurantList').on('click', '.btnOrders', function() {
        const $restaurant = $(this).closest('.restaurant-card')
        const restaurantId = $restaurant.attr('id')
        const restaurantName = $restaurant.find('h3').text()

        $.ajax({
            url: restaurantServiceApiBaseUrl.concat("/", restaurantId, "/orders"),
            success: function(data) {
                const $modal = $('#ordersModal')
                $modal.find('.modal-header').text(restaurantName + " orders")
                $modal.find('.modal-content').empty().append(buildRestaurantOrderTable(data))
                $modal.removeClass('hidden')
            }
        })
    })

    $('#restaurantForm button[name="btnCancel"]').click(function() {
        resetRestaurantForm()
    })

    $('#restaurantDishForm button[name="btnSave"]').click(function(event) {
        event.preventDefault()
        const restaurantDishData = validateAndGetRestaurantDishFormData()
        if (restaurantDishData == null) {
            return
        }

        let type = "POST"
        let url = restaurantServiceApiBaseUrl.concat("/", restaurantDishData.restaurantId, "/dishes")
        if (restaurantDishData.dishId.length > 0) {
            type = "PATCH"
            url = url.concat("/", restaurantDishData.dishId)
        }

        $.ajax({
            type,
            url,
            contentType: "application/json",
            data: JSON.stringify({
                name: restaurantDishData.dishName,
                price: restaurantDishData.dishPrice
            }),
            success: function() {
                showRestaurantForm()
                hideRestaurantDishForm()
                resetRestaurantDishForm()
            }
        })
    })

    $('#restaurantDishForm button[name="btnCancel"]').click(function() {
        showRestaurantForm()
        hideRestaurantDishForm()
        resetRestaurantDishForm()
    })

    $('#restaurantList').on('click', '.btnDeleteDish', function() {
        const $restaurant = $(this).closest('.restaurant-card')
        const restaurantId = $restaurant.attr('id')
        const restaurantName = $restaurant.find('h3').text()
        const $dish = $(this).closest('tr')
        const dishId = $dish.attr('id')
        const dishName = $dish.find('td.name').text()

        showModal($('#confirmationModal'), 'Delete Restaurant Dish', 'Are you sure you want to delete the dish "'+dishName+'" of "'+restaurantName+'"?', function() {
            $.ajax({
                type: "DELETE",
                url: restaurantServiceApiBaseUrl.concat("/", restaurantId, "/dishes/", dishId)
            })
        })
    })

    $('#restaurantList').on('click', '.btnEditDish', function() {
        const restaurantId = $(this).closest('.restaurant-card').attr('id')
        const dishId = $(this).closest('tr').attr('id')
        $.ajax({
            url: restaurantServiceApiBaseUrl.concat("/", restaurantId, "/dishes/", dishId),
            success: function(data) {
                fillRestaurantDishForm(data)
                showRestaurantDishForm()
                hideRestaurantForm()
            }
        })
    })

    $('.connWebSocket').click(function() {
        if (stompClient !== null) {
            disconnectWebSocket()
        }
        connectToWebSocket()
    })
})
