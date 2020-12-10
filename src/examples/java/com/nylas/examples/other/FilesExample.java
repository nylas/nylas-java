package com.nylas.examples.other;

import java.nio.file.Path;

import com.nylas.File;
import com.nylas.FileQuery;
import com.nylas.Files;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RemoteCollection;
import com.nylas.examples.ExampleConf;

public class FilesExample {

	public static void main(String[] args) throws Exception {
		ExampleConf conf = new ExampleConf();
		NylasClient client = new NylasClient();
		NylasAccount account = client.account(conf.get("access.token"));
		Files files = account.files();
		RemoteCollection<File> allFiles = files.list(new FileQuery());
		for (File file : allFiles) {
			System.out.println("File: " + file);
		}
		
		File first = allFiles.iterator().next();
		byte[] fileBytes = files.downloadBytes(first.getId());
		
		Path tmpfile = java.nio.file.Files.createTempFile(first.getFilename(), null);
		java.nio.file.Files.write(tmpfile, fileBytes);
		
		File uploaded = files.upload(first.getFilename(), first.getContentType(), fileBytes);
		System.out.println("Uploaded: " + uploaded);
		
		files.delete(uploaded.getId());
		System.out.println("deleted");
	}

}
