package com.akojimsg.bootify;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

interface CustomerRepository extends CrudRepository<Customer, Integer> {
  Iterable<Customer> findByNameStartsWith(String name);
}

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

}

@Controller
@ResponseBody
class CustomerHttpController {
  private final CustomerRepository repository;
  private final ObservationRegistry registry;

  CustomerHttpController(CustomerRepository repository, ObservationRegistry registry) {
    this.repository = repository;
    this.registry = registry;
  }

  @GetMapping("/customers/{id}")
  Customer byId(@PathVariable Integer id) {
    Customer customer = repository.findById(id).orElse(null);
    Assert.notNull(customer, "Customer with id: %d not found".formatted(id));
    return Observation
        .createNotStarted("by-id", this.registry)
        .observe(() -> customer);
  }

  @GetMapping("/customers/name/{name}")
  Iterable<Customer> byname(@PathVariable String name) {
    Assert.state(Character.isUpperCase(name.charAt(0)), "The name must start with an upper case!");
    //return this.repository.findByNameStartsWith(name);
    return Observation
        .createNotStarted("by-name", this.registry)
        .observe(() -> repository.findByNameStartsWith(name));
  }

  @GetMapping("/customers")
  Iterable<Customer> customers() {
    return this.repository.findAll();
  }
}

record Customer(@Id Integer id, String name) {
}

@ControllerAdvice
class ErrorHandlingControllerAdvice {
  @ExceptionHandler
  ProblemDetail handle(IllegalStateException ise, HttpServletRequest request) {
    request.getHeaderNames().asIterator().forEachRemaining(System.out::println);
    var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
    pd.setDetail(ise.getLocalizedMessage());
    return pd;
  }

  @ExceptionHandler
  ProblemDetail handle(IllegalArgumentException iae) {
    var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
    pd.setDetail(iae.getLocalizedMessage());
    return pd;
  }
}