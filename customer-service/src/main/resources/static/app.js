let stompClient = null;
const customerServiceApiBaseUrl = "http://localhost:9080/api/customers"

function connectToWebSocket() {
    const socket = new SockJS('/websocket')
    stompClient = Stomp.over(socket)

    stompClient.connect({},
        function (frame) {
            console.log('Connected: ' + frame)

            stompClient.subscribe('/topic/customers/added', function (event) {
                addCustomer(JSON.parse(event.body))
            })

            stompClient.subscribe('/topic/customers/updated', function (event) {
                updateCustomer(JSON.parse(event.body))
            })

            stompClient.subscribe('/topic/customers/deleted', function (event) {
                removeCustomer(JSON.parse(event.body).id)
            })
        },
        function() {
            console.log('Unable to connect to Websocket!')
        }
    )
}

function addCustomer(customer) {
    const row =
        '<tr id="'+customer.id+'">'+
            '<td>'+
                '<button class="btnDelete circular small red ui icon button"><i class="icon trash alternate"></i></button>'+
                '<button class="btnEdit circular small orange ui icon button"><i class="icon edit"></i></button>'+
            '</td>'+
            '<td>'+customer.id+'</td>'+
            '<td>'+customer.name+'</td>'+
            '<td>'+customer.address+'</td>'+
        '</tr>'
    $('#customerTable').find('tbody').prepend(row)
}

function updateCustomer(customer) {
    $('#'+customer.id).remove()
    addCustomer(customer)
}

function removeCustomer(id) {
    $('#'+id).remove()
}

function loadCustomers() {
    $.ajax({
        url: customerServiceApiBaseUrl,
        contentType: "application/json",
        success: function(data, textStatus, jqXHR) {
            data.forEach(customer => addCustomer(customer))
        }
    });
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

    $('#btnSave').click(function(event) {
        event.preventDefault();
        const customerData = validateAndGetFormData()
        if (customerData == null) {
            return
        }

        $.ajax({
            type: customerData.id.length === 0 ? "POST" : "PATCH",
            url: customerData.id.length === 0 ? customerServiceApiBaseUrl : customerServiceApiBaseUrl.concat("/", customerData.id),
            contentType: "application/json",
            data: JSON.stringify({name: customerData.name, address: customerData.address}),
            success: function(data, textStatus, jqXHR) {
                resetForm()
            },
            error: function (jqXHR, textStatus, errorThrown) {}
        });
    })

    $('#customerTable').on('click', '.btnDelete', function() {
        const id = $(this).closest('tr').attr('id')
        $.ajax({
            type: "DELETE",
            url: customerServiceApiBaseUrl.concat("/", id),
            success: function(data, textStatus, jqXHR) {},
            error: function (jqXHR, textStatus, errorThrown) {}
        });
    });

    $('#customerTable').on('click', '.btnEdit', function() {
        const id = $(this).closest('tr').attr('id')
        $.ajax({
            url: customerServiceApiBaseUrl.concat("/", id),
            success: function(data, textStatus, jqXHR) {
                fillForm(data)
            },
            error: function (jqXHR, textStatus, errorThrown) {}
        });
    });

    $('#btnClear').click(function() {
        resetForm()
    })
})

connectToWebSocket()