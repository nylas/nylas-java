package com.nylas.examples.nativeAuth;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class HtmlTemplateResponder {

	private final Configuration cfg;
	
	public HtmlTemplateResponder(String path) {
		cfg = new Configuration(Configuration.VERSION_2_3_29);
		cfg.setClassForTemplateLoading(HtmlTemplateResponder.class, path);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		cfg.setFallbackOnNullLoopVariable(false);
	}
	
	public void respond(String templateName, Object dataModel, HttpServletResponse response) throws IOException {
		try {
			Template template = cfg.getTemplate(templateName);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			template.process(dataModel, response.getWriter());
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}
	
}
