package xqtr.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Support {
	
	public static void setTimeout(int delay, Runnable runnable) {
		
	    new Thread(() -> {
	        try {
	            Thread.sleep(delay);
	            runnable.run();
	        } catch (Exception e) {
	            System.err.println(e);
	        }
	    }).start();
	}
	
	public static Element parseXML(String path) {
		
		try {
			File inputFile = new File(path);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inputFile);
			Element root = document.getDocumentElement();
			root.normalize();
			return root;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static List<Node> nodeList(NodeList list) {
		
		return IntStream.range(0, list.getLength()).mapToObj(list::item).collect(Collectors.toList());
	}
	
	public static <A, B> List<B> transform(List<A> list, Function<A, B> function) {
		
		return list.stream().map(function).collect(Collectors.toList());	
	}
	
	public static <B> List<B> transform(NodeList list, Function<Node, B> function) {
		
		return Support.transform(nodeList(list), function);
	}
	
	public static Date dateFromString(String string) {
		
		Map<String, String> patterns = new HashMap<String, String>();
		patterns.put("\\d{2}:\\d{2}", "HH:mm");
		patterns.put("\\d{2}:\\d{2}:\\d{2}", "HH:mm:ss");
		patterns.put("\\d{2}:\\d{2}:\\d{2}.\\d{3}", "HH:mm:ss.SSS");
		patterns.put("\\d{4}", "yyyy");
		patterns.put("\\d{4}-\\d{2}", "yyyy-MM");
		patterns.put("\\d{4}-\\d{2}-\\d{2}", "yyyy-MM-dd");
		patterns.put("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}", "yyyy-MM-dd HH:mm");
		patterns.put("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", "yyyy-MM-dd HH:mm:ss");
		patterns.put("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3}", "yyyy-MM-dd HH:mm:ss.SSS");
		
		String format = patterns.get(patterns.keySet().stream().filter(p -> Pattern.matches(p, string)).findFirst().get());
		
		try {
			return (new SimpleDateFormat(format)).parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getFileExtension(File file) {
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		return i != -1 ? fileName.substring(i+1) : "";
	}
	
	public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(.*)" + regex, "$1" + replacement);
    }
}
