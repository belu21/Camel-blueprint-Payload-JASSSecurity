package tigo.com.bo.proxyondemand.beans;


import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class CamelProcessor {
		private static final Logger log = LoggerFactory.getLogger(CamelProcessor.class);
		
		public String process(Exchange exchange) throws Exception {
			
			try {
				 
	            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
	 
	            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
	 
	            Document document = documentBuilder.newDocument();
	            
	            String msisdn = exchange.getIn().getHeader("msisdn",String.class);
	            String codigo = exchange.getIn().getHeader("codigo",String.class);
	 
	            // ondemand
	            Element root = document.createElement("OndemandResponse");
	            document.appendChild(root);

	 
	            // out
	            Element out = document.createElement("out");
	            out.appendChild(document.createTextNode("msisdn: "+msisdn+ ", codigo: "+codigo));
	            root.appendChild(out);
	 
	            // crea el dom y transforma a xml
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            DOMSource domSource = new DOMSource(document);
	            
	            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	            StringWriter writer = new StringWriter();
	            transformer.transform(domSource, new StreamResult(writer));
	            String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
	            
	            log.info(output);
	            
	            return output;
	            
	 
	        } catch (ParserConfigurationException pce) {
	            pce.printStackTrace();
	            return "Error";
	        } catch (TransformerException tfe) {
	            tfe.printStackTrace();
	            return "Error";
	        }
		}
}
