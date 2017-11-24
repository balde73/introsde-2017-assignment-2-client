package rest.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrettyPrinter {
	String fullFile;
	String format;
	
	public PrettyPrinter(String format) throws FileNotFoundException {
		this.format = format;
		PrintStream fileStream = new PrintStream("client-server-"+format+".log");
		System.setOut(fileStream);
	}
	
	public void print(String primeStamp, String result, Integer status, String responseString, String format) {
		System.out.println(primeStamp);
		System.out.println("=> Result: "+result);
		System.out.println("=> HTTP Status: "+status);
		try {
			if(format == "xml")
				System.out.println(formatXml(responseString));
			else if(format == "json")
				System.out.println(formatJson(responseString));
			else
				System.out.println(responseString);
		} catch(Exception e) {
			System.out.println(responseString);
		} finally {
			System.out.println("");
			System.out.println("----------------------------------------");
			System.out.println("");
		}
	}
	
	public void printWarning(String line) {
		System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
		System.out.println("WARNING: "+line);
		System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
	}
	
	public void printStep(int num) {
		System.out.println("########################################");
		System.out.println("##           step"+num+"             ##");
		System.out.println("########################################");
		System.out.println("");
		System.out.println("");
		System.out.println("");
	}
	
	private String formatJson(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(jsonString, Object.class);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
	}
	
	public String formatXml(String xml) {
        try {
            final InputSource src = new InputSource(new StringReader(xml));
            final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
            final Boolean keepDeclaration = Boolean.valueOf(xml.startsWith("<?xml"));

            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            final LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
            writer.getDomConfig().setParameter("xml-declaration", keepDeclaration); // Set this to true if the declaration is needed to be outputted.

            return writer.writeToString(document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
