package Result;

public record LogoutResult(String message) {
    public LogoutResult(String message) {
        this.message = message;
    }

    public String toString(){
        return this.message;
    }
}
