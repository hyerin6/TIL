package com.example.kakaologin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoErrorResponse {
	private int code;
	private String msg;
}