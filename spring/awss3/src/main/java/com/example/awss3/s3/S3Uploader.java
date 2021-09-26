package com.example.awss3.s3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.example.awss3.configuration.AWSConfig;
import com.example.awss3.domain.Image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

	private final AWSConfig awsConfig;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.s3.path}")
	private String filePath;

	public String upload(MultipartFile multipartFile, String dirName) throws IOException {
		File uploadFile = convert(multipartFile, multipartFile.getOriginalFilename())
			.orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
		return upload(uploadFile, dirName);
	}

	private String upload(File uploadFile, String dirName) {
		String fileName = uploadFile.getName();
		String uploadImageUrl = putS3(uploadFile, fileName);
		removeNewFile(uploadFile);
		return uploadImageUrl;
	}

	private String putS3(File uploadFile, String fileName) {
		AmazonS3 s3Client = awsConfig.AwsS3Client();

		s3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
			CannedAccessControlList.PublicRead));

		return s3Client.getUrl(bucket, fileName).toString();
	}

	public List<String> uploadFiles(String folderName, List<MultipartFile> multipartFiles) {
		List<File> files = new ArrayList<>();

		multipartFiles.stream()
			.forEach(x -> files.add(convert(x, x.getOriginalFilename())
				.orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."))));

		// List<String> filePaths = upload(folderName, files);
		// filePaths.stream().forEach(x -> multipartFiles.get(filePaths.indexOf(x)));

		return upload(folderName, files);
	}

	private List<String> upload(String folderName, List<File> files) {
		AmazonS3 s3Client = awsConfig.AwsS3Client();
		TransferManager xferMgr = TransferManagerBuilder.standard()
			.withS3Client(s3Client)
			.build();

		MultipleFileUpload xfer = xferMgr.uploadFileList(bucket, folderName, new File(filePath), files);

		try {
			xfer.waitForCompletion();
			xfer.waitForException();
		} catch (InterruptedException e) {
			log.error(e.toString());
		} finally {
			files.stream().forEach(x -> x.delete());
			xferMgr.shutdownNow(false);
		}

		List<String> filesUrl = new ArrayList<>();
		files.stream().forEach(x -> filesUrl.add(getUrl(bucket.concat("/" + folderName), x.getName())));

		return filesUrl;
	}

	public String getUrl(String path, String fileName) {
		AmazonS3 s3Client = awsConfig.AwsS3Client();
		return s3Client.getUrl(path, fileName).toString();
	}

	private void removeNewFile(File targetFile) {
		if (targetFile.delete()) {
			log.info("파일이 삭제되었습니다.");
		} else {
			log.info("파일이 삭제되지 못했습니다.");
		}
	}

	private Optional<File> convert(MultipartFile file, String fileName) {
		File convertFile = new File(fileName);

		try {
			if (convertFile.createNewFile()) {
				try (FileOutputStream fos = new FileOutputStream(convertFile)) {
					fos.write(file.getBytes());
				}
				return Optional.of(convertFile);
			}
		} catch (IOException e) {
			log.error(e.toString());
		}

		return Optional.empty();
	}

	public void deleteS3Object(Image image) {
		AmazonS3 s3Client = awsConfig.AwsS3Client();
		s3Client.deleteObject(new DeleteObjectRequest(bucket, image.getFileName()));
	}

	// public void deleteObjects(List<Image> images) {
	// 	AmazonS3 s3Client = awsConfig.AwsS3Client();
	// 	ArrayList<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
	// 	for (int i = 0; i < images.size(); ++i) {
	// 		String keyName = images.get(i).getFilePath();
	// 		keys.add(new DeleteObjectsRequest.KeyVersion(keyName));
	// 	}
	//
	// 	images.stream().forEach(x -> System.out.println(x.getFilePath()));
	//
	// 	DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucket)
	// 		.withKeys(keys)
	// 		.withQuiet(false);
	//
	// 	s3Client.deleteObjects(multiObjectDeleteRequest);
	// }

}
