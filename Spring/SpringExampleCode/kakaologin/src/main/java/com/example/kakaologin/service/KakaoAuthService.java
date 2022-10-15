package com.example.kakaologin.service;

import org.springframework.stereotype.Service;

import com.example.kakaologin.domain.User;
import com.example.kakaologin.dto.KakaoProfileResponse;
import com.example.kakaologin.dto.KakaoTokenResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

	private final AuthApiService authApiService;
	private final UserService userService;

	public User signUp(String code) {

		KakaoTokenResponse kakaoTokenResponse = authApiService.getKakaoAccessToken(code);

		KakaoProfileResponse kakaoProfile = authApiService.getKakaoProfile(kakaoTokenResponse.getAccessToken());

		User user = User.builder()
			.uid(kakaoProfile.getId())
			.email(kakaoProfile.getKakaoAccount().getEmail())
			.name(kakaoProfile.getKakaoAccount().getProfile().getNickname())
			.profile(kakaoProfile.getKakaoAccount().getProfile().getProfileImageUrl())
			.build();

		return userService.signUp(user);
	}

}
