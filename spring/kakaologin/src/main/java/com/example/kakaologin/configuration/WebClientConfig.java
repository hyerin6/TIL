package com.example.kakaologin.configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient apiWebClient() {

		final ReactorClientHttpConnector connector = new ReactorClientHttpConnector(
			HttpClient.from(
				TcpClient.create()
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
					.doOnConnected(conn ->
						conn.addHandler(new ReadTimeoutHandler(3000, TimeUnit.MILLISECONDS))
					)
			)
		);

		return WebClient.builder()
			.clientConnector(connector)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}

}
