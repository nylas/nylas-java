package com.nylas.examples;

import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import com.nylas.File;
import com.nylas.Files;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;

public class FilesExample {

	public static void main(String[] args) throws Exception {
		Properties props = Examples.loadExampleProperties();
		String accessToken = props.getProperty("access.token");
		
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(accessToken);
		Files files = account.files();
		List<File> allFiles = files.list();
		for (File file : allFiles) {
			System.out.println("File: " + file);
		}
		
		File first = allFiles.get(0);
		byte[] fileBytes = files.downloadBytes(first.getId());
		java.nio.file.Files.write(Paths.get("/tmp/" + first.getFilename()), fileBytes);
		
		File uploaded = files.upload(first.getFilename(), first.getContentType(), fileBytes);
		System.out.println("Uploaded: " + uploaded);
		
		files.delete(uploaded.getId());
		System.out.println("deleted");
	}

}
