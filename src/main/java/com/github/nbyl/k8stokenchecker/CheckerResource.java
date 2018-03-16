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

    private static final String CA_CRT_PATH = "/var/run/secrets/kubernetes.io/serviceaccount/ca.crt";
    private static final String SERVICE_CA_CRT_PATH = "/var/run/secrets/kubernetes.io/serviceaccount/service-ca.crt";


    @RequestMapping("/")
    public CheckResult[] checkToken() {
        CheckResult[] results = {
                checkCAFile(CA_CRT_PATH),
                checkCAFile(SERVICE_CA_CRT_PATH)
        };
        return results;
    }

    private static CheckResult checkCAFile(String path) {
        try {
            try (InputStream pemInputStream = new FileInputStream(path)) {
                CertificateFactory certFactory = CertificateFactory.getInstance("X509");
                X509Certificate cert = (X509Certificate) certFactory.generateCertificate(pemInputStream);
            }

            return new CheckResult(path);
        } catch (CertificateException | IOException e) {
            LOGGER.debug("Error checking ca.crt", e);
            return new CheckResult(false, path, org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
        }
    }
}
