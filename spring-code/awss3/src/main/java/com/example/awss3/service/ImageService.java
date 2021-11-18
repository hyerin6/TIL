package com.example.awss3.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.awss3.aop.ImageSaveExHandler;
import com.example.awss3.domain.Image;
import com.example.awss3.repository.ImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

	private final ImageRepository imageRepository;

	@ImageSaveExHandler
	@Transactional
	public void addImage(Image image) {
		imageRepository.save(image);
	}

	@Transactional
	public void addImages(List<Image> images) throws Exception {
		imageRepository.saveAll(images);
		throw new Exception("error");
	}

}
