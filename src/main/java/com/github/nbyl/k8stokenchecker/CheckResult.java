package com.github.nbyl.k8stokenchecker;

public class CheckResult {

    private final String path;

    public CheckResult(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
