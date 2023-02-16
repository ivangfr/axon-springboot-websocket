package com.ivanfranchin.customerservice.rest;

import com.ivanfranchin.customerservice.command.AddCustomerCommand;
import com.ivanfranchin.customerservice.command.DeleteCustomerCommand;
import com.ivanfranchin.customerservice.command.UpdateCustomerCommand;
import com.ivanfranchin.customerservice.mapper.CustomerMapper;
import com.ivanfranchin.customerservice.model.Customer;
import com.ivanfranchin.customerservice.model.Order;
import com.ivanfranchin.customerservice.query.GetCustomerOrdersQuery;
import com.ivanfranchin.customerservice.query.GetCustomerQuery;
import com.ivanfranchin.customerservice.query.GetCustomersQuery;
import com.ivanfranchin.customerservice.rest.dto.AddCustomerRequest;
import com.ivanfranchin.customerservice.rest.dto.CustomerOrderResponse;
import com.ivanfranchin.customerservice.rest.dto.CustomerResponse;
import com.ivanfranchin.customerservice.rest.dto.UpdateCustomerRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final CustomerMapper customerMapper;

    @GetMapping
    public CompletableFuture<List<CustomerResponse>> getCustomers() {
        return queryGateway.query(new GetCustomersQuery(), ResponseTypes.multipleInstancesOf(Customer.class))
                .thenApply(customers -> customers.stream().map(customerMapper::toCustomerResponse).toList());
    }

    @GetMapping("/{id}")
    public CompletableFuture<CustomerResponse> getCustomer(@PathVariable String id) {
        return queryGateway.query(new GetCustomerQuery(id), Customer.class).thenApply(customerMapper::toCustomerResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompletableFuture<String> addCustomer(@Valid @RequestBody AddCustomerRequest request) {
        return commandGateway.send(new AddCustomerCommand(UUID.randomUUID().toString(), request.getName(), request.getAddress()));
    }

    @PatchMapping("/{id}")
    public CompletableFuture<String> updateCustomer(@PathVariable String id,
                                                    @Valid @RequestBody UpdateCustomerRequest request) {
        return commandGateway.send(new UpdateCustomerCommand(id, request.getName(), request.getAddress()));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<String> deleteCustomer(@PathVariable String id) {
        return commandGateway.send(new DeleteCustomerCommand(id));
    }

    @GetMapping("/{id}/orders")
    public CompletableFuture<List<CustomerOrderResponse>> getCustomerOrders(@PathVariable String id) {
        return queryGateway.query(new GetCustomerOrdersQuery(id), ResponseTypes.multipleInstancesOf(Order.class))
                .thenApply(orders -> orders.stream().map(customerMapper::toCustomerOrderResponse).toList());
    }
}
