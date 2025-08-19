package com.app.security;

import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


//@SecurityScheme(
//name = "basicAuth",
//type = SecuritySchemeType.HTTP,
//scheme = "basic"
//)


//JWT bearer token auth
@SecurityScheme
(name = "bearerAuth",
 type = SecuritySchemeType.HTTP,
 scheme = "bearer",
 bearerFormat = "JWT"
)

@OpenAPIDefinition(info = @Info (title = "Accomodation Service Api",version = "v1.0"),security = {
			@SecurityRequirement(name = "bearerAuth")
}
)

@Configuration
public class SwaggerConfig {

}
