package com.akojimsg.bootify;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface CustomerHttpClient {
  @GetExchange("/customers")
  Flux<Customer> all();

  @GetExchange("/customers/{id}")
  Flux<Customer> byName(Integer id);

  @GetExchange("/customers/name/{name}")
  Flux<Customer> byName(String name);

}

@Controller
class CustomerGraphqlController {
  private final CustomerHttpClient cc;

  CustomerGraphqlController(CustomerHttpClient cc) {
    this.cc = cc;
  }

  @BatchMapping
  Map<Customer, Profile> profile (List<Customer> customerList){
    var map = new HashMap<Customer, Profile>();
    for (var c : customerList){
      map.put(c, new Profile(c.id()));
    }
    return map;
  }

//  @SchemaMapping(typeName = "Customer")
//  Profile profile(Customer customer) {
//    System.out.println("customer id " + customer.name());
//    return new Profile(customer.id());
//  }

  //@SchemaMapping(typeName = "Query", field = "customers")
  @QueryMapping
  Flux<Customer> customers(){
    return this.cc.all();
  }
}

//class CustomerHttpClient {
//	private final WebClient http;
//
//  CustomerHttpClient(WebClient http) {
//    this.http = http;
//  }
//}

@SpringBootApplication
public class ClientApp {
  public static void main(String[] args) {
    SpringApplication.run(ClientApp.class, args);
  }

  @Bean
  ApplicationRunner applicationRunner(CustomerHttpClient cc) {
    return args -> cc.all().subscribe(System.out::println);
  }

	@Bean
	CustomerHttpClient client(WebClient.Builder builder){
		var wc = builder.baseUrl("http://localhost:8080/").build();
		var wca = WebClientAdapter.create(wc);
    return HttpServiceProxyFactory
        .builder()
        .exchangeAdapter(wca)
        .build()
        .createClient(CustomerHttpClient.class);
	}
}

record Profile(Integer id){}

record Customer(Integer id, String name) {
}