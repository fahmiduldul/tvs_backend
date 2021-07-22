package fahmi.authentication.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@NoArgsConstructor
@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private Environment env;

    private final String secret = this.env.getProperty("jwt.expiration");

    @Autowired
    public JwtUtil(Environment env) {
        this.env = env;
    }

    public String encode(String subject){
        assert this.secret != null;
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(
                        new Date(System.currentTimeMillis() + Long.parseLong(this.secret)))
                .signWith(this.key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validate(String jwt){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(this.key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (Exception e){
            return false;
        }

        return true;
    }
}
