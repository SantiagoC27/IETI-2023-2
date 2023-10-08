package org.ieti;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "API de Gestión de Usuarios",
                version = "1.0",
                description = "API para gestionar usuarios en una aplicación.",
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "Santiago Cárdenas",
                        email = "santiago.cardenas-a@Mail.escuelaing.edu.co"
                ),
                license = @License(
                        name = "Licencia de Uso",
                        url = "https://www.example.com/licencia"
                )
        )
)
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
