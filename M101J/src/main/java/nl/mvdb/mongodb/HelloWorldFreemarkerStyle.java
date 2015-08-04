package nl.mvdb.mongodb;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class HelloWorldFreemarkerStyle {

	public static void main(String[] args) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Configuration conf = new Configuration(new Version("2.3.23"));
		conf.setClassForTemplateLoading(HelloWorldFreemarkerStyle.class, "/");
		HashMap<String, Object> viewMap = new HashMap<String,Object>();
		viewMap.put("name", "Martijn van der Blom");
		
		StringWriter sw = new StringWriter();

		Template helloTemplate = conf.getTemplate("hello.ftl");
		helloTemplate.process(viewMap, sw);
		System.out.println(sw);
	}

}
