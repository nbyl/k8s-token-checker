package com.github.nbyl.k8stokenchecker;

public class CheckResult {

    private final boolean success;

    private final String path;

    private final String message;

    public CheckResult(String path) {
        this(true, path, "");
    }

    public CheckResult(boolean success, String path, String message) {
        this.success = success;
        this.path = path;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }
}
