package com.github.nbyl.k8stokenchecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@RestController
public class CheckerResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckerResource.class);

    private static final String CA_CRT_PATH = "";

    @RequestMapping("/")
    public CheckResult[] checkToken() {
        CheckResult[] results = {checkCAFile()};
        return results;
    }

    private CheckResult checkCAFile() {
        try {
            try (InputStream pemInputStream = new FileInputStream(CA_CRT_PATH)) {
                CertificateFactory certFactory = CertificateFactory.getInstance("X509");
                X509Certificate cert = (X509Certificate) certFactory.generateCertificate(pemInputStream);
            }

            return new CheckResult(CA_CRT_PATH);
        } catch (CertificateException | IOException e) {
            LOGGER.debug("Error checking ca.crt", e);
            return new CheckResult(false, CA_CRT_PATH, org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
        }
    }
}
