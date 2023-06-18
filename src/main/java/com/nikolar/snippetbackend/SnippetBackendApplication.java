package com.nikolar.snippetbackend;

import com.nikolar.gutenbergbooksparser.ArffFileCreationThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SnippetBackendApplication {

	public static void main(String[] args) {
		String outputFileName = "data/writer";
		ArffFileCreationThread trainingThread = new ArffFileCreationThread(outputFileName, false, true);
		ArffFileCreationThread testThread = new ArffFileCreationThread(outputFileName,true,false);
		trainingThread.start();
		testThread.start();
		SpringApplication.run(SnippetBackendApplication.class, args);
	}

}
