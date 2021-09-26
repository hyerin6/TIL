package com.example.kakaologin.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.kakaologin.domain.User;
import com.example.kakaologin.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public User signUp(User user) {
		return userRepository.save(user);
	}

}
