package com.herokuapp;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.herokuapp.file.FileService;
import com.herokuapp.pdfbox.DocumentService;
import com.herokuapp.sanitize.SanitizerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ApplicationConfiguration {

	@Bean
	@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
	FileService fileService(@Value("${root}") String root) {
		String sessionId = UUID.randomUUID().toString();
		log.info("Initiate new session {}", sessionId);
		return new FileService(root, sessionId);
	}

	@Bean
	DocumentService documentService() {
		return new DocumentService();
	}

	@Bean
	SanitizerService sanitizerService() {
		return new SanitizerService();
	}
	
}