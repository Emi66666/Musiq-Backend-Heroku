package MojKvart.Projekt.rest;

import MojKvart.Projekt.service.impl.AuthenticationBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:4200", "https://progifroggies-moj-kvart.herokuapp.com"})
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    @GetMapping(path = "/basicauth")
    public AuthenticationBean basicauth() {
      return new AuthenticationBean("You are authenticated");
  }
}
