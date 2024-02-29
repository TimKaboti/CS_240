package Result;

public record ClearResult(String message) {
    public ClearResult(String message) {
        this.message = message;
    }

    public String toString(){
        return this.message;
    }
}
