package Result;

public record JoinGameResult(String message) {
    public JoinGameResult(String message) {
        this.message = message;
    }

    public String toString(){
        return this.message;
    }
}
