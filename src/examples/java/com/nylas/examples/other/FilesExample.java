package com.nylas.examples.other;

import java.nio.file.Paths;
import java.util.List;

import com.nylas.File;
import com.nylas.Files;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.examples.ExampleConf;

public class FilesExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
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
