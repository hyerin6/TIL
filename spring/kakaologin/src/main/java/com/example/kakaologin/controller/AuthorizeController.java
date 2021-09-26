package com.example.kakaologin.controller;

import static com.example.kakaologin.dto.ResponseEntityConstants.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kakaologin.domain.User;
import com.example.kakaologin.service.KakaoAuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthorizeController {

	private final KakaoAuthService kakaoAuthService;

	@GetMapping("/third-parties/kakao/sign-up")
	public ResponseEntity<Void> kakaoSignUp(@RequestParam("code") String code) {
		User user = kakaoAuthService.signUp(code);

		log.info(user.toString());

		return CREATED;
	}

}
