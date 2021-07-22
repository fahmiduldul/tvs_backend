package fahmi.authentication.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@NoArgsConstructor
@Component
public class JwtTool {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${jwt.expiration}")
    private String expiration;

    public String encode(String subject){
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(
                        new Date(System.currentTimeMillis() + Long.parseLong(this.expiration)))
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

            // DEPRECATED
            //Jwts.parser()
            //        .setSigningKey(this.secret)
            //        .parseClaimsJws(jwt)
            //        .getBody()
            //        .getSubject();
        } catch (Exception e){
            return false;
        }

        return true;
    }
}
