package Result;

public record LoginResult(String username, String authToken, String message) {
    public LoginResult(String username, String authToken, String message) {
        this.username = username;
        this.authToken = authToken;
        this.message = message;
    }

    public String toString(){
        return this.message;
    }
}
