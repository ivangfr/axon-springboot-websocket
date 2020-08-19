const customerServiceApiBaseUrl = "http://localhost:9080/api/customers"

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

            stompClient.subscribe('/topic/customer/order/created', function (event) {
                addOrder(JSON.parse(event.body))
            })
        },
        function() {
            showModal($('.modal.alert'), 'WebSocket Disconnected', 'WebSocket is disconnected. Maybe, customer-service is down or restarting')
            $('.connWebSocket').find('i').removeClass('green').addClass('red')
        }
    )
}

function loadCustomers() {
    $.ajax({
        url: customerServiceApiBaseUrl,
        contentType: "application/json",
        success: function(data, textStatus, jqXHR) {
            data.forEach(customer => {
                addCustomer(customer)
                $.ajax({
                    url: customerServiceApiBaseUrl.concat("/", customer.id, "/orders"),
                    contentType: "application/json",
                    success: function(data, textStatus, jqXHR) {
                        data.map(order => {
                            let orderCopy = { ... order }
                            orderCopy['customerId'] = customer.id
                            return orderCopy
                        })
                        .forEach(order => addOrder(order))
                    },
                    error: function (jqXHR, textStatus, errorThrown) {}
                })
            })
        },
        error: function (jqXHR, textStatus, errorThrown) {}
    })
}

function addCustomer(customer) {
    const row =
        '<div id="'+customer.id+'" class="item">'+
            '<div class="content">'+
                '<div class="ui grid">'+
                    '<div class="two wide column">'+
                        '<button class="btnDelete tiny red ui icon button"><i class="icon trash alternate"></i></button>'+
                        '<button class="btnEdit tiny orange ui icon button"><i class="icon edit"></i></button>'+
                    '</div>'+
                    '<div class="four wide center aligned column">'+
                        '<h3 class="name">'+customer.name+'</h3>'+
                    '</div>'+
                    '<div class="four wide center aligned column">'+
                        '<p class="address">Address: <strong>'+customer.address+'</strong></p>'+
                    '</div>'+
                    '<div class="six wide right aligned column">'+
                        '<div class="ui label">'+customer.id+'</div>'+
                    '</div>'+
                    '<div class="sixteen wide column" style="padding-top: 0;margin-bottom: 10px;">'+
                        '<table class="ui six column striped small compact unstackable table">'+
                            '<tbody></tbody>'+
                        '</table>'+
                    '</div>'+
                '</div>'
            '</div>'+
        '</div>'
    $('#customerList').prepend(row)
}

function getOrderRow(order) {
    const items = order.items
        .map(item => item.quantity + "x " + item.dishName + " " + accounting.formatMoney(item.dishPrice) + " (" + accounting.formatMoney(item.quantity*item.dishPrice) + ")")
        .map(description => '<li>' + description + '</li>')
        .join('')
    return (
        '<tr>'+
            '<td>'+order.id+'</td>'+
            '<td>'+moment(order.createdAt).format('YYYY-MM-DD HH:mm:ss')+'</td>'+
            '<td>'+order.status+'</td>'+
            '<td><strong>'+order.restaurantName+'</strong></td>'+
            '<td>'+accounting.formatMoney(order.total)+'</td>'+
            '<td><ul class="ui list">'+items+'</ul></td>'+
        '</tr>'
    )
}

function updateCustomer(customer) {
    const $customer = $('#'+customer.id)
    $customer.find('h3.id').text(customer.name)
    $customer.find('p.address > strong').text(customer.address)
}

function removeCustomer(customer) {
    $('#'+customer.id).remove()
}

function addOrder(order) {
    $('#'+order.customerId).find('tbody').prepend(getOrderRow(order))
}

function fillForm(data) {
    $('#customerForm input[name="id"]').val(data.id)
    $('#customerForm input[name="name"]').val(data.name)
    $('#customerForm input[name="address"]').val(data.address)
}

function resetForm() {
    $('#customerForm input[name="id"]').val('')
    $('#customerForm input[name="name"]').val('')
    $('#customerForm input[name="address"]').val('')
}

function validateAndGetFormData() {
    const id = $('#customerForm input[name="id"]').val()
    const name = $('#customerForm input[name="name"]').val()
    const address = $('#customerForm input[name="address"]').val()

    if (name.trim().length === 0 || address.trim().length === 0) {
        showModal($('.modal.alert'), 'Missing fields', 'Please inform customer Name and Address')
        return null
    }

    return {id, name, address}
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

    connectToWebSocket()

    $('#customerForm button[name="btnSave"]').click(function(event) {
        event.preventDefault()
        const customerData = validateAndGetFormData()
        if (customerData == null) {
            return
        }

        let type = "POST"
        let url = customerServiceApiBaseUrl
        if (customerData.id.length > 0) {
            type = "PATCH"
            url = url.concat("/", customerData.id)
        }

        $.ajax({
            type,
            url,
            contentType: "application/json",
            data: JSON.stringify({name: customerData.name, address: customerData.address}),
            success: function(data, textStatus, jqXHR) {
                resetForm()
            },
            error: function (jqXHR, textStatus, errorThrown) {}
        })
    })

    $('#customerList').on('click', '.btnDelete', function() {
        const $customer = $(this).closest('div.item')
        const id = $customer.attr('id')
        const name = $customer.find('h3').text()
        showModal($('.modal.confirmation'), 'Delete Customer', 'Are you sure you want to delete customer "'+name+'"?', function() {
            $.ajax({
                type: "DELETE",
                url: customerServiceApiBaseUrl.concat("/", id),
                success: function(data, textStatus, jqXHR) {},
                error: function (jqXHR, textStatus, errorThrown) {}
            })
        })
    })

    $('#customerList').on('click', '.btnEdit', function() {
        const id = $(this).closest('div.item').attr('id')
        $.ajax({
            url: customerServiceApiBaseUrl.concat("/", id),
            success: function(data, textStatus, jqXHR) {
                fillForm(data)
            },
            error: function (jqXHR, textStatus, errorThrown) {}
        })
    })

    $('#customerForm button[name="btnCancel"]').click(function() {
        resetForm()
    })

    $('.connWebSocket').click(function() {
        connectToWebSocket()
    })
})
