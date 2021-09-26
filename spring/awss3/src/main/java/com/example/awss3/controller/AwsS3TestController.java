package com.example.awss3.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.awss3.domain.Image;
import com.example.awss3.s3.S3Uploader;
import com.example.awss3.service.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AwsS3TestController {

	private static final String FAIL = "image upload fail";
	private static final String SUCCESS = "image upload success";

	private final S3Uploader s3Uploader;
	private final ImageService imageService;

	@PostMapping("/upload")
	public String upload(@RequestParam("data") MultipartFile multipartFile) {
		String path = "";

		try {
			path = s3Uploader.upload(multipartFile, "testImage");

			Image image = Image.builder()
				.fileName(multipartFile.getOriginalFilename())
				.filePath(path)
				.build();

			imageService.addImage(image);
		} catch (Exception e) {
			return FAIL;
		}

		return path;
	}

	@PostMapping("/files/upload")
	public String uploadList(@RequestParam("data") List<MultipartFile> files) {
		List<Image> images = new ArrayList<>();

		files.stream()
			.forEach(x -> {
				Image image = Image.builder()
					.fileName(x.getName())
					.build();
				images.add(image);
			});

		try {
			List<String> filePaths = s3Uploader.uploadFiles("test2", files);
			log.info("\n" + filePaths.toString() + "\n");
			imageService.addImages(images);
		} catch (Exception e) {
			return FAIL;
		}

		return SUCCESS;
	}
}
