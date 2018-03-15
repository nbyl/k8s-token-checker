package com.github.nbyl.k8stokenchecker;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckerResource {

    @RequestMapping("/")
    public CheckResult[] checkToken() {
        return new CheckResult[0];
    }
}
