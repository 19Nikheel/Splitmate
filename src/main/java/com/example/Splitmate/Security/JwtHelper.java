package com.example.Splitmate.Security;

import com.example.Splitmate.Config.CustomUserDetails;
import com.example.Splitmate.Entity.AcceptRequests;
import com.example.Splitmate.Entity.MainUser;
import com.example.Splitmate.Repo.AcceptRequestsRepo;
import com.example.Splitmate.Repo.MainuserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.io.Deserializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import org.antlr.v4.runtime.Token;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {

    @Autowired
    private MainuserRepo userrepo;

    @Autowired
    private AcceptRequestsRepo arp;



    //requirement :
    public static final long JWT_TOKEN_VALIDITY =  36* 60 * 60;

    //    public static final long JWT_TOKEN_VALIDITY =  60;
    private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public int getTokenIDFromToken(String token) {

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        Claims body = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return body.get("tokenId",Integer.class);
    }


    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(CustomUserDetails userDetails) {

        if(userDetails.getUsername().startsWith("@#")){
            AcceptRequests art=arp.findByUserId(userDetails.getUsername().substring(2))
                    .orElseThrow(()-> new UsernameNotFoundException("User not found"));
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", art.getRole());
            claims.put("tokenId", art.getTokenID());
            return doGenerateToken(claims, userDetails.getUsername());
        }

        MainUser user = userrepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Map<String, Object> claims = new HashMap<>();

        claims.put("role", user.getRole());
        claims.put("tokenId",-1);
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String generateTokenlimit(CustomUserDetails userDetails) {

        MainUser user = userrepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "otp");
        claims.put("tokenId",-1);

        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 150 * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }


    public String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //validate token
    public Boolean validateToken(String token, CustomUserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        int tokenId = getTokenIDFromToken(token);
        if(tokenId!=-1 && tokenId!=userDetails.getTokenId()){
            return false;
        }

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
