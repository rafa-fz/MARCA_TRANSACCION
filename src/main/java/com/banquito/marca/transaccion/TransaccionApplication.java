package com.banquito.marca.transaccion;

import java.math.BigDecimal;
import java.util.Arrays;

import org.bson.types.Decimal128;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.banquito.marca.transaccion.repository")
@EnableFeignClients(basePackages = "com.banquito.marca.transaccion.client")
public class TransaccionApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransaccionApplication.class, args);
	}

	@Bean
	MongoCustomConversions customConverions() {
		Converter<Decimal128, BigDecimal> decimal128ToBigDecimal = new Converter<Decimal128, BigDecimal>() {
			@Override
			public BigDecimal convert(Decimal128 s) {
				return s == null ? null : s.bigDecimalValue();
			}
		};

		Converter<BigDecimal, Decimal128> bigDecimalToDecimal128 = new Converter<BigDecimal, Decimal128>() {
			@Override
			public Decimal128 convert(BigDecimal s) {
				return s == null ? null : new Decimal128(s);
			}
		};

		return new MongoCustomConversions(Arrays.asList(decimal128ToBigDecimal, bigDecimalToDecimal128));
	}

}
