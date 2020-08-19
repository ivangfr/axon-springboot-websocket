const restaurantServiceApiBaseUrl = "http://localhost:9081/api/restaurants"

function connectToWebSocket() {
    const socket = new SockJS('/websocket')
    const stompClient = Stomp.over(socket)

    stompClient.connect({},
        function (frame) {
            console.log('Connected: ' + frame)
            $('.connWebSocket').find('i').removeClass('red').addClass('green')

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
        },
        function() {
            showModal($('.modal.alert'), 'WebSocket Disconnected', 'WebSocket is disconnected. Maybe, restaurant-service is down or restarting')
            $('.connWebSocket').find('i').removeClass('green').addClass('red')
        }
    )
}

function loadRestaurants() {
    $.ajax({
        url: restaurantServiceApiBaseUrl,
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

function addRestaurant(restaurant) {
    const row =
        '<div id="'+restaurant.id+'" class="item">'+
            '<div class="content">'+
                '<div class="ui grid">'+
                    '<div class="six wide column">'+
                        '<button class="btnDelete ui tiny red icon button"><i class="icon trash alternate"></i></button>'+
                        '<button class="btnEdit ui tiny orange icon button"><i class="icon edit"></i></button>'+
                        '<button class="btnAddDish ui tiny teal labeled icon button">'+
                            '<i class="plus icon"></i>Dish'+
                        '</button>'+
                        '<button class="btnOrders ui tiny violet button">Orders</button>'+
                    '</div>'+
                    '<div class="four wide center aligned column">'+
                        '<h3>'+restaurant.name+'</h3>'+
                    '</div>'+
                    '<div class="six wide right aligned column">'+
                        '<div class="ui label">'+restaurant.id+'</div>'+
                    '</div>'+
                    '<div class="sixteen wide column" style="padding-top: 0;margin-bottom: 10px;">'+
                        '<table class="ui striped small compact unstackable table">'+
                            '<tbody></tbody>'+
                        '</table>'+
                    '</div>'+
                '</div>'
            '</div>'+
        '</div>'
    $('#restaurantList').prepend(row)
}

function getRestaurantDishRow(dish) {
    return (
        '<tr id="'+dish.dishId+'">'+
            '<td class="six wide">'+dish.dishId+'</td>'+
            '<td class="name six wide"><strong>'+dish.dishName+'</strong></td>'+
            '<td class="price two wide">'+accounting.formatMoney(dish.dishPrice)+'</td>'+
            '<td class="two right aligned wide">'+
                '<button class="btnDeleteDish mini red ui icon button"><i class="icon trash alternate"></i></button>'+
                '<button class="btnEditDish mini orange ui icon button"><i class="icon edit"></i></button>'+
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
        showModal($('.modal.alert'), 'Missing fields', 'Please inform restaurant Name')
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
        showModal($('.modal.alert'), 'Missing fields', 'Please inform dish Name and Price')
        return null
    }

    return {restaurantId, dishId, dishName, dishPrice}
}

function showRestaurantForm() {
    $('#restaurantForm').css('display', 'block')
}

function hideRestaurantForm() {
    $('#restaurantForm').css('display', 'none')
}

function showRestaurantDishForm() {
    $('#restaurantDishForm').css('display', 'block')
}

function hideRestaurantDishForm() {
    $('#restaurantDishForm').css('display', 'none')
}

function buildRestaurantOrderTable(data) {
    const orders = data.map(order => {
        const items = order.items
            .map(item => item.quantity + "x " + item.dishName + " " + accounting.formatMoney(item.dishPrice) + " (" + accounting.formatMoney(item.quantity*item.dishPrice) + ")")
            .map(description => '<li>' + description + '</li>')
            .join('')
        return (
            '<tr>'+
                '<td>'+order.id+'</td>'+
                '<td>'+moment(order.createdAt).format('YYYY-MM-DD HH:mm:ss')+'</td>'+
                '<td>'+order.status+'</td>'+
                '<td><strong>'+order.customerName+'</strong></td>'+
                '<td><strong>'+order.customerAddress+'</strong></td>'+
                '<td>'+accounting.formatMoney(order.total)+'</td>'+
                '<td><ul class="ui list">'+items+'</ul></td>'+
            '</tr>'
        )
    }).join('')

    return (
        '<table id="orderTable" class="ui seven column striped very compact table">'+
            '<thead>'+
            '<tr>'+
                '<th>ID</th>'+
                '<th>CreatedAt</th>'+
                '<th>Status</th>'+
                '<th>Customer Name</th>'+
                '<th>Customer Address</th>'+
                '<th>Total</th>'+
                '<th>Items</th>'+
            '</tr>'+
            '</thead>'+
            '<tbody>'+orders+'</tbody>'+
        '</table>'
    )
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
            success: function(data, textStatus, jqXHR) {
                resetRestaurantForm()
            },
            error: function (jqXHR, textStatus, errorThrown) {}
        })
    })

    $('#restaurantList').on('click', '.btnDelete', function() {
        const $restaurant = $(this).closest('div.item')
        const id = $restaurant.attr('id')
        const name = $restaurant.find('h3').text()
        showModal($('.modal.confirmation'), 'Delete Restaurant', 'Are you sure you want to delete restaurant "'+name+'"?', function() {
            $.ajax({
                type: "DELETE",
                url: restaurantServiceApiBaseUrl.concat("/", id),
                success: function(data, textStatus, jqXHR) {},
                error: function (jqXHR, textStatus, errorThrown) {}
            })
        })
    })

    $('#restaurantList').on('click', '.btnEdit', function() {
        const id = $(this).closest('div.item').attr('id')
        $.ajax({
            url: restaurantServiceApiBaseUrl.concat("/", id),
            success: function(data, textStatus, jqXHR) {
                fillRestaurantForm(data)
                showRestaurantForm()
                hideRestaurantDishForm()
                resetRestaurantDishForm()
            },
            error: function (jqXHR, textStatus, errorThrown) {}
        })
    })

    $('#restaurantList').on('click', '.btnAddDish', function() {
        const restaurantId = $(this).closest('div.item').attr('id')
        $('#restaurantDishForm input[name="restaurantId"]').val(restaurantId)
        showRestaurantDishForm()
        hideRestaurantForm()
    })

    $('#restaurantList').on('click', '.btnOrders', function() {
        const $restaurant = $(this).closest('div.item')
        const restaurantId = $restaurant.attr('id')
        const restaurantName = $restaurant.find('h3').text()

        $.ajax({
            url: restaurantServiceApiBaseUrl.concat("/", restaurantId, "/orders"),
            success: function(data, textStatus, jqXHR) {
                const $modal = $('.modal.orders')
                $modal.find('.header').text(restaurantName + " orders")
                $modal.find('.content').empty().append(buildRestaurantOrderTable(data))
                $modal.modal('show')
            },
            error: function (jqXHR, textStatus, errorThrown) {}
        })
    })

    $('#restaurantForm button[name="btnCancel"]').click(function() {
        resetRestaurantForm()
    })

    $('#restaurantDishForm button[name="btnSave"').click(function() {
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
            success: function(data, textStatus, jqXHR) {
                showRestaurantForm()
                hideRestaurantDishForm()
                resetRestaurantDishForm()
            },
            error: function (jqXHR, textStatus, errorThrown) {}
        })
    })

    $('#restaurantDishForm button[name="btnCancel"').click(function() {
        showRestaurantForm()
        hideRestaurantDishForm()
        resetRestaurantDishForm()
    })

    $('#restaurantList').on('click', '.btnDeleteDish', function() {
        const $restaurant = $(this).closest('div.item')
        const restaurantId = $restaurant.attr('id')
        const restaurantName = $restaurant.find('h3').text()
        const $dish = $(this).closest('tr')
        const dishId = $dish.attr('id')
        const dishName = $dish.find('td.name').text()

        showModal($('.modal.confirmation'), 'Delete Restaurant Dish', 'Are you sure you want to delete the dish "'+dishName+'" of "'+restaurantName+'"?', function() {
            $.ajax({
                type: "DELETE",
                url: restaurantServiceApiBaseUrl.concat("/", restaurantId, "/dishes/", dishId),
                success: function(data, textStatus, jqXHR) {},
                error: function (jqXHR, textStatus, errorThrown) {}
            })
        })
    })

    $('#restaurantList').on('click', '.btnEditDish', function() {
        const restaurantId = $(this).closest('div.item').attr('id')
        const dishId = $(this).closest('tr').attr('id')
        $.ajax({
            url: restaurantServiceApiBaseUrl.concat("/", restaurantId, "/dishes/", dishId),
            success: function(data, textStatus, jqXHR) {
                fillRestaurantDishForm(data)
                showRestaurantDishForm()
                hideRestaurantForm()
            },
            error: function (jqXHR, textStatus, errorThrown) {}
        })
    })

    $('.connWebSocket').click(function() {
        connectToWebSocket()
    })

})
