package com.example.kakaologin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class KakaoTokenResponse {

	private String tokenType;
	private String accessToken;
	private Integer expiresIn;
	private String refreshToken;
	private Integer refreshTokenExpiresIn;
	private String scope;

	@JsonCreator
	public KakaoTokenResponse(@JsonProperty("token_type") String tokenType,
		@JsonProperty("access_token") String accessToken,
		@JsonProperty("expires_in") Integer expiresIn,
		@JsonProperty("refresh_token") String refreshToken,
		@JsonProperty("refresh_token_expires_in") Integer refreshTokenExpiresIn,
		@JsonProperty("scope") String scope
	) {
		this.tokenType = tokenType;
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.refreshToken = refreshToken;
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
		this.scope = scope;
	}

}
