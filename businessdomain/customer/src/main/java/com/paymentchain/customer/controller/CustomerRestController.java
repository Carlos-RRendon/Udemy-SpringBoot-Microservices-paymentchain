/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.customer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.repository.CustomerRepository;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/customer")
public class CustomerRestController {
    
    @Autowired
    CustomerRepository customerRepository;

    private final WebClient.Builder webClientBuilder;

    public CustomerRestController(WebClient.Builder webClientBuilder){
        this.webClientBuilder = webClientBuilder;
    }

    //Define timeout
    TcpClient tcpClient = TcpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    @GetMapping("/full")
    public Customer get(@RequestParam String code){
        Customer customer = customerRepository.findByCode(code);
        List<CustomerProduct> products = customer.getProducts();
        products.forEach(dto -> {
            String productName = getProductName(dto.getProductId());
            dto.setProductName(productName);
        });
        customer.setTransactions(getTransactions(customer.getIban()));
        return customer;
    }

    private <T> List<T> getTransactions(String accountIban){
        WebClient client = webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .baseUrl("http://businessdomain-transactions/api/v1/transaction")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://businessdomain-transactions/api/v1/transaction"))
                .build();
        List<Object> block = client.method(HttpMethod.GET).uri(uriBuilder -> uriBuilder
                .path("/transactions")
                .queryParam("ibanAccount",accountIban)
                .build())
                .retrieve().bodyToFlux(Object.class).collectList().block();
        List<T> name = (List<T>) block;
        return name;
    }

    private String getProductName(long id){
        WebClient client = webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .baseUrl("http://businessdomain-product/api/v1/producto")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://businessdomain-product/api/v1/producto"))
                .build();
        JsonNode block = client.method(HttpMethod.GET).uri("/"+id)
                .retrieve().bodyToMono(JsonNode.class).block();
        String name = block.get("name").asText();
        return name;
    }
    
    
    @GetMapping()
    public List<Customer> list() {
        return customerRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Customer get(@PathVariable long id) {
        Customer customer = customerRepository.findById(id).get();
        return  customer;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody Customer input) {
        return null;
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Customer input) {
        input.getProducts().forEach(x -> x.setCustomer(input));
        Customer save = customerRepository.save(input);
        return ResponseEntity.ok(save);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return null;
    }
    
}
