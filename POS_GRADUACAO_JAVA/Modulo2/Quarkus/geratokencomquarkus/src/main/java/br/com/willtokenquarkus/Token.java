package br.com.willtokenquarkus;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;

import java.util.Arrays;
import java.util.HashSet;

public class Token {
    public static void main(String[] args) {
        // Ensure SmallRye JWT can find the signing key when running as a plain main()
        System.setProperty("smallrye.jwt.sign.key.location", "privateKey.pem");
        String token = Jwt.issuer("https://example.com/issuer")

                .upn("jdoe@quarkus.io")
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                .claim(Claims.birthdate, "2001-07-13")
                .sign();

        System.out.println("Generated JWT Token: " + token);
        System.exit(0);
    }
}
