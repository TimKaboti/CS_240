package Result;

public record RegisterResult(String username, String authToken, String message) {

    public RegisterResult(String username, String authToken, String message) {
        this.username = username;
        this.authToken = authToken;
        this.message = message;
    }

    public String toString(){
        return this.message;
    }
}
