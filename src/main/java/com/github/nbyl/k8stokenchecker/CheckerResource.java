package com.github.nbyl.k8stokenchecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@RestController
public class CheckerResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckerResource.class);

    private static final String CA_CRT_PATH = "/var/run/secrets/kubernetes.io/serviceaccount/ca.crt";
    private static final String SERVICE_CA_CRT_PATH = "/var/run/secrets/kubernetes.io/serviceaccount/service-ca.crt";
    private static final String NAMESPACE_PATH = "/var/run/secrets/kubernetes.io/serviceaccount/namespace";


    @RequestMapping("/")
    public CheckResult[] checkToken() {
        CheckResult[] results = {
                checkCAFile(CA_CRT_PATH),
                checkCAFile(SERVICE_CA_CRT_PATH),
                checkKubernetesClient()
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

    private static CheckResult checkKubernetesClient() {
        try {
            new DefaultKubernetesClient(createDefaultConfig());
            return new CheckResult("Kubernetes Client");
        } catch (IOException e) {
            LOGGER.debug("Error checking ca.crt", e);
            return new CheckResult(false, "Kubernetes Client", org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
        }
    }

    private static Config createDefaultConfig() throws IOException {
        Config config = new ConfigBuilder().withNamespace(getNamespace()).build();
        LOGGER.info("Using configuration for kubernetes client: {}", new ObjectMapper().writeValueAsString(config));
        return config;
    }

    private static String getNamespace() throws IOException {
        String namespace = FileUtils.readFileToString(new File(NAMESPACE_PATH), StandardCharsets.UTF_8);
        LOGGER.info("Running in namespace {}", namespace);
        return namespace;
    }
}
