package tvs.apigateway.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTool {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    public String encode(String subject){
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(
                        new Date(System.currentTimeMillis() + Long.parseLong(this.expiration)))
                .signWith(SignatureAlgorithm.HS512, this.secret)
                .compact();
    }

    public Boolean validate(String jwt){
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (Exception e){
            return false;
        }

        return true;
    }
}
