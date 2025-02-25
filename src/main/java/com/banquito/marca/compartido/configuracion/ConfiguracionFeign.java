package com.banquito.marca.compartido.configuracion;

import com.banquito.marca.compartido.excepciones.FeignExcepcion;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionFeign {
    @Bean
    public ErrorDecoder customErrorDecoder() {
        return new CustomErrorDecoder();
    }

    public static class CustomErrorDecoder implements ErrorDecoder {

        private final ErrorDecoder defaultErrorDecoder = new Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            try {
                String errorBody = response.body() != null
                        ? new String(response.body().asInputStream().readAllBytes())
                        : "No error body available";
                return new FeignExcepcion(response.status(), errorBody);
            } catch (Exception e) {
                return defaultErrorDecoder.decode(methodKey, response);
            }
        }
    }
}
