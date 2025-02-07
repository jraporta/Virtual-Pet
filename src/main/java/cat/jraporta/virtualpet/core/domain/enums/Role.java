package cat.jraporta.virtualpet.core.domain.enums;

public enum Role {
    USER,
    ADMIN;

    public String getRoleName() {
        return "ROLE_" + this.name();
    }
}
