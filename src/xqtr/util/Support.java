package xqtr.util;

import java.io.File;
import java.util.List;
import java.util.function.Function;
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
}
