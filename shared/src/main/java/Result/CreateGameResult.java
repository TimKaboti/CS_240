package Result;

public record CreateGameResult(Integer gameID, String message) {
    public CreateGameResult(Integer gameID, String message) {
        this.gameID = gameID;
        this.message = message;
    }
    public String toString(){
        return this.message;
    }
}
