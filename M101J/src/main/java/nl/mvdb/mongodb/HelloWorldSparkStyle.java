package nl.mvdb.mongodb;

import java.io.StringWriter;
import java.util.HashMap;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class HelloWorldSparkStyle {

	public static void main(String[] args) {
		Configuration conf = new Configuration(new Version("2.3.23"));
		conf.setClassForTemplateLoading(HelloWorldFreemarkerStyle.class, "/");
		
		Spark.get("/", new Route() {
			@Override
			public Object handle(Request arg0, Response arg1) throws Exception {
				HashMap<String, Object> viewMap = new HashMap<String,Object>();
				viewMap.put("name", "Martijn van der Blom");
				
				StringWriter sw = new StringWriter();

				Template helloTemplate = conf.getTemplate("hello.ftl");
				helloTemplate.process(viewMap, sw);
				return sw;
			}
		});
	}

}
