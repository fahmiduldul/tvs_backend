package tvs.apigateway.configuration;

import java.util.List;

@org.springframework.context.annotation.Configuration
public class Configuration {
    public static final List<String> publicPath = List.of(
            "/login",
            "/user"
    );
}
