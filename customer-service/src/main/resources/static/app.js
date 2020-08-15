let stompClient = null
const customerServiceApiBaseUrl = "http://localhost:9080/api/customers"

function connectToWebSocket() {
    const socket = new SockJS('/websocket')
    stompClient = Stomp.over(socket)

    stompClient.connect({},
        function (frame) {
            console.log('Connected: ' + frame)

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
            console.log('Unable to connect to Websocket!')
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
                        data.map(function(order) {
                            return {
                                customerId: customer.id,
                                orderId: order.id,
                                restaurantName: order.restaurantName,
                                status: order.status,
                                total: order.total,
                                items: order.items
                            }
                        })
                        .forEach(order => addOrder(order))
                    }
                })
            })
        }
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
                        '<h3>'+customer.name+'</h3>'+
                    '</div>'+
                    '<div class="four wide center aligned column">'+
                        '<p>'+customer.address+'</p>'+
                    '</div>'+
                    '<div class="six wide right aligned column">'+
                        '<div class="ui label">'+customer.id+'</div>'+
                    '</div>'+
                    '<div class="sixteen wide column" style="padding-top: 0;margin-bottom: 10px;">'+
                        '<table class="ui striped small compact unstackable table">'+
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
        .map(item => item.quantity + "x " + item.dishName + " $" + item.dishPrice + " ($" + item.quantity*item.dishPrice + ")")
        .map(description => '<div class="item">' + description + '</div>')
        .join('')

    return (
        '<tr id="'+order.orderId+'">'+
            '<td class="six wide">'+order.orderId+'</td>'+
            '<td class="two wide"><strong>'+order.restaurantName+'</strong></td>'+
            '<td class="one wide">'+order.status+'</td>'+
            '<td class="one wide">$'+order.total+'</td>'+
            '<td class="six wide"><div class="ui bulleted list">'+items+'</div></td>'+
        '</tr>'
    )
}

function updateCustomer(customer) {
    $('#'+customer.id).find('h3').text(customer.name)
    $('#'+customer.id).find('p').text(customer.id)
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
        alert("Please inform customer name and address")
        return null
    }

    return {id, name, address}
}

$(function () {
    loadCustomers()

    connectToWebSocket()

    $('#btnSave').click(function(event) {
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
        const id = $(this).closest('div.item').attr('id')
        $.ajax({
            type: "DELETE",
            url: customerServiceApiBaseUrl.concat("/", id),
            success: function(data, textStatus, jqXHR) {},
            error: function (jqXHR, textStatus, errorThrown) {}
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

    $('#btnCancel').click(function() {
        resetForm()
    })
})
