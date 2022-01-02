package com.alokaparanji.aws.s3.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.alokaparanji.aws.s3.model.Person;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class S3EventHandler implements RequestHandler<S3Event, String> {

	@Override
	public String handleRequest(S3Event s3Event, Context context) {

		s3Event.getRecords().forEach(event -> {
			AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
			S3Object s3Object = s3Client.getObject(
					new GetObjectRequest(event.getS3().getBucket().getName(), event.getS3().getObject().getKey()));
			InputStream objectData = s3Object.getObjectContent();
			ObjectMapper mapper = new ObjectMapper();
			try {
				Arrays.stream(mapper.readValue(objectData, Person[].class)).forEach(System.out::println);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		return "File Processed Successfully";
	}

}
