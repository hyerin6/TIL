package com.example.awss3.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.awss3.domain.Image;
import com.example.awss3.s3.S3Uploader;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Aspect
@Component
public class ServiceExceptionAspect {

	private final S3Uploader s3Uploader;

	@AfterThrowing("@annotation(com.example.awss3.aop.ImageSaveExHandler)")
	public void imageUploadExceptionHandler(JoinPoint joinPoint) {
		Image image = (Image)joinPoint.getArgs()[0];
		s3Uploader.deleteS3Object(image);
	}

	@AfterThrowing("execution(* addImages*(*))")
	public void imagesUploadExceptionHandler(JoinPoint joinPoint) {

	}

}
