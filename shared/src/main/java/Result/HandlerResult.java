package Result;

import spark.Response;

public record HandlerResult(Response code, Object result) {
}
