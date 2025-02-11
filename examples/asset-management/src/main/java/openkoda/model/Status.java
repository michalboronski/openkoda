package openkoda.model;

public enum Status {
    in_use("in use"),
    broken("broken"),
    in_repair("in repair"),
    retired("retired"),
    utilized("utilized");

    public final String status;

    Status(String status) {
        this.status = status;
    }
}
