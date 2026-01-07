# geratokencomquarkus

Projeto exemplo para gerar JWT com Quarkus e SmallRye JWT.

## Passo a passo (do zero)

### 1) Criar o projeto Quarkus

```shell script
mvn io.quarkus.platform:quarkus-maven-plugin:3.30.5:create \
  -DprojectGroupId=br.com.willtokenquarkus \
  -DprojectArtifactId=geratokencomquarkus \
  -Dextensions="smallrye-jwt,smallrye-jwt-build"
cd geratokencomquarkus
```

### 2) Gerar a chave privada (privateKey.pem)

```shell script
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 \
  -out src/main/resources/privateKey.pem
```

### 3) Configurar o caminho da chave

Em `src/main/resources/application.properties`, adicione:

```properties
smallrye.jwt.sign.key.location=privateKey.pem
```

### 4) Criar a classe `Token.java`

Crie o arquivo `src/main/java/br/com/willtokenquarkus/Token.java` com:

```java
package br.com.willtokenquarkus;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;

import java.util.Arrays;
import java.util.HashSet;

public class Token {
    public static void main(String[] args) {
        // Garante que o SmallRye JWT encontre a chave ao rodar como main
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
```

### 5) Gerar o token

```shell script
./mvnw -q -DskipTests package
java -cp target/classes br.com.willtokenquarkus.Token
```

Se preferir, voce tambem pode passar o caminho da chave na linha de comando:

```shell script
java -Dsmallrye.jwt.sign.key.location=privateKey.pem -cp target/classes br.com.willtokenquarkus.Token
```

### 6) Rodar a aplicacao em modo dev (opcional)

```shell script
./mvnw quarkus:dev
```

> **_NOTA:_** A Dev UI fica em <http://localhost:8080/q/dev/>.

## Empacotar e executar

```shell script
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

Para gerar um _uber-jar_:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

## Executavel nativo (opcional)

```shell script
./mvnw package -Dnative
```

Ou com container:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Depois, execute:

```shell script
./target/geratokencomquarkus-1.0-SNAPSHOT-runner
```
