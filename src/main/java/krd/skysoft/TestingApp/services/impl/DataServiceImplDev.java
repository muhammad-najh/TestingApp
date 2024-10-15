package krd.skysoft.TestingApp.services.impl;

import krd.skysoft.TestingApp.services.DataService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev") //for production
public class DataServiceImplDev implements DataService {
    @Override
    public String getData() {
      return "Dev Data";
    }
}
