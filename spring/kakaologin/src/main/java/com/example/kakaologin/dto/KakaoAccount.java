package com.example.kakaologin.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class KakaoAccount {

	private Profile profile;
	private String email;

	@Getter
	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	public static class Profile {
		private String nickname;
		private String profileImageUrl;
	}

}