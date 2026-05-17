let stompClient = null

const customerServiceApiBaseUrl = "http://localhost:9080/api/customers"

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

        stompClient.subscribe('/topic/customer/order/created', function (event) {
            addOrder(JSON.parse(event.body))
        })
    }

    stompClient.onStompError = function (frame) {
        console.log('STOMP error: ' + frame)
        setWsStatus(false)
    }

    stompClient.onWebSocketClose = function () {
        showModal($('#alertModal'), 'WebSocket Disconnected', 'WebSocket is disconnected. Maybe, customer-service is down or restarting')
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
        url: customerServiceApiBaseUrl,
        contentType: "application/json",
        success: function(data) {
            data.forEach(customer => {
                addCustomer(customer)
                $.ajax({
                    url: customerServiceApiBaseUrl.concat("/", customer.id, "/orders"),
                    contentType: "application/json",
                    success: function(data) {
                        data.map(order => {
                            let orderCopy = { ...order }
                            orderCopy['customerId'] = customer.id
                            return orderCopy
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
        '<div id="'+customer.id+'" class="customer-card bg-white rounded-xl shadow-sm border border-gray-200 p-4">'+
            '<div class="flex items-start justify-between mb-2">'+
                '<div class="flex items-center gap-1">'+
                    '<button class="btnDelete px-2 py-1 bg-red-500 hover:bg-red-600 text-white rounded text-xs transition-colors">'+
                        '<svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/></svg>'+
                    '</button>'+
                    '<button class="btnEdit px-2 py-1 bg-amber-500 hover:bg-amber-600 text-white rounded text-xs transition-colors">'+
                        '<svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/></svg>'+
                    '</button>'+
                '</div>'+
                '<div class="text-right">'+
                    '<h3 class="name text-base font-semibold text-gray-900">'+customer.name+'</h3>'+
                    '<span class="text-xs text-gray-500 font-mono">'+customer.id+'</span>'+
                '</div>'+
            '</div>'+
            '<p class="address text-sm text-gray-600 mb-3">Address: <strong>'+customer.address+'</strong></p>'+
            '<div class="overflow-x-auto">'+
                '<table class="w-full text-xs border-collapse">'+
                    '<tbody></tbody>'+
                '</table>'+
            '</div>'+
        '</div>'
    $('#customerList').prepend(row)
}

function getOrderRow(order) {
    const items = order.items
        .map(item => item.quantity + "x " + item.dishName + " " + accounting.formatMoney(item.dishPrice) + " (" + accounting.formatMoney(item.quantity * item.dishPrice) + ")")
        .map(description => '<li>' + description + '</li>')
        .join('')
    return (
        '<tr class="border-b border-gray-100">'+
            '<td class="py-1.5 px-2 text-gray-500">'+order.id+'</td>'+
            '<td class="py-1.5 px-2">'+dayjs(order.createdAt).format('YYYY-MM-DD HH:mm:ss')+'</td>'+
            '<td class="py-1.5 px-2">'+order.status+'</td>'+
            '<td class="py-1.5 px-2"><strong>'+order.restaurantName+'</strong></td>'+
            '<td class="py-1.5 px-2 text-right font-medium">'+accounting.formatMoney(order.total)+'</td>'+
            '<td class="py-1.5 px-2"><ul class="list-disc list-inside text-xs text-gray-600">'+items+'</ul></td>'+
        '</tr>'
    )
}

function updateCustomer(customer) {
    const $customer = $('#'+customer.id)
    $customer.find('h3.name').text(customer.name)
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
        showModal($('#alertModal'), 'Missing fields', 'Please inform customer Name and Address')
        return null
    }

    return {id, name, address}
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
            success: function() {
                resetForm()
            }
        })
    })

    $('#customerList').on('click', '.btnDelete', function() {
        const $customer = $(this).closest('.customer-card')
        const id = $customer.attr('id')
        const name = $customer.find('h3').text()
        showModal($('#confirmationModal'), 'Delete Customer', 'Are you sure you want to delete customer "'+name+'"?', function() {
            $.ajax({
                type: "DELETE",
                url: customerServiceApiBaseUrl.concat("/", id)
            })
        })
    })

    $('#customerList').on('click', '.btnEdit', function() {
        const id = $(this).closest('.customer-card').attr('id')
        $.ajax({
            url: customerServiceApiBaseUrl.concat("/", id),
            success: function(data) {
                fillForm(data)
            }
        })
    })

    $('#customerForm button[name="btnCancel"]').click(function() {
        resetForm()
    })

    $('.connWebSocket').click(function() {
        if (stompClient !== null) {
            disconnectWebSocket()
        }
        connectToWebSocket()
    })
})
