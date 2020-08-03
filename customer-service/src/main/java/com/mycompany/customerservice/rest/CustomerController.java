package com.mycompany.customerservice.rest;

import com.mycompany.customerservice.command.AddCustomerCommand;
import com.mycompany.customerservice.command.DeleteCustomerCommand;
import com.mycompany.customerservice.command.UpdateCustomerCommand;
import com.mycompany.customerservice.mapper.CustomerMapper;
import com.mycompany.customerservice.model.Customer;
import com.mycompany.customerservice.query.GetCustomerQuery;
import com.mycompany.customerservice.query.GetCustomersQuery;
import com.mycompany.customerservice.rest.dto.AddCustomerRequest;
import com.mycompany.customerservice.rest.dto.CustomerDto;
import com.mycompany.customerservice.rest.dto.UpdateCustomerRequest;
import com.mycompany.customerservice.service.CustomerService;
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

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @GetMapping
    public CompletableFuture<List<CustomerDto>> getCustomers() {
        return queryGateway.query(new GetCustomersQuery(), ResponseTypes.multipleInstancesOf(Customer.class))
                .thenApply(customers -> customers.stream()
                        .map(customerMapper::toCustomerDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public CompletableFuture<CustomerDto> getCustomer(@PathVariable String id) {
        return queryGateway.query(new GetCustomerQuery(id), Customer.class).thenApply(customerMapper::toCustomerDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompletableFuture<String> addCustomer(@Valid @RequestBody AddCustomerRequest request) {
        return commandGateway.send(new AddCustomerCommand(UUID.randomUUID().toString(), request.getName(), request.getAddress()));
    }

    @PatchMapping("/{id}")
    public CompletableFuture<String> updateCustomer(@PathVariable String id,
                                                    @Valid @RequestBody UpdateCustomerRequest request) {
        Customer customer = customerService.validateAndGetCustomer(id);
        String name = request.getName() == null ? customer.getName() : request.getName();
        String address = request.getAddress() == null ? customer.getAddress() : request.getAddress();
        return commandGateway.send(new UpdateCustomerCommand(id, name, address));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<String> deleteCustomer(@PathVariable String id) {
        customerService.validateAndGetCustomer(id);
        return commandGateway.send(new DeleteCustomerCommand(id));
    }

}
