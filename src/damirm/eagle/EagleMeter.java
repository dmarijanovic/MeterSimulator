package damirm.eagle;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import damirm.Meter;
import damirm.intrface.XmlContent;

public class EagleMeter extends Meter {

    private List<CurrentSummationFragment> pulseList;

    private CurrentSummationFragment currentPulseFragment;

    public EagleMeter() {
        pulseList = new ArrayList<CurrentSummationFragment>();
        currentPulseFragment = new CurrentSummationFragment();
    }

    @Override
    public void makePulse() {
        currentPulseFragment.increment(randInt(10, 20));

        pulseList.add(currentPulseFragment.getCopy());
    }

    @Override
    public void sendToServer() throws Exception {
        String xmlBody = createXML(new XmlContent() {

            @Override
            public boolean showOutput() {
                return true;
            }

            @Override
            public void fillWithContent(Element rootElement, Document document) {
                rootElement.appendChild(currentPulseFragment.getXmlFragment(document));
            }
        });

        postData(xmlBody);
    }

    public void sendToServerHistoryData() throws ParserConfigurationException, TransformerException {
        String xmlBody = createXML(new XmlContent() {

            @Override
            public boolean showOutput() {
                return false;
            }

            @Override
            public void fillWithContent(Element rootElement, Document document) {
                Element historyData = document.createElement("HistoryData");
                for (CurrentSummationFragment currentSummationFragment : pulseList) {
                    historyData.appendChild(currentSummationFragment.getXmlFragment(document));
                }
                rootElement.appendChild(historyData);
            }
        });

        System.out.println("Sending history data " + pulseList.size());
        postData(xmlBody);
    }
    
    private void postData(String xmlBody) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:9000/eagle/");

        try {
            StringEntity entity = new StringEntity(xmlBody);
            entity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(entity);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                proccessResponse(response);
            } else {
                System.out.println("Error sending with status code " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void proccessResponse(CloseableHttpResponse response) throws ParserConfigurationException, IllegalStateException, SAXException, IOException, TransformerException {
        boolean isXml = response.getEntity().getContentType().getValue().contains("text/xml");
        
        // exit if is not xml
        if (!isXml) return;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(response.getEntity().getContent()));

        Node rootNode = doc.getFirstChild();
        boolean isLocalCommand = rootNode.getNodeName().equals("LocalCommand");
        
        // we support only local command
        if (!isLocalCommand) return;
        
        String command = "";
        for (int i = 0; i < rootNode.getChildNodes().getLength(); i++) {
            Node node = rootNode.getChildNodes().item(i);
            if (node.getNodeName().equals("Name")) {
                command = node.getTextContent();
            }
        }

        if (command.equals("get_history_data")) {
            sendToServerHistoryData();
        } else {
            System.out.println("Unsupported command " + command);
        }
    }

    private String createXML(XmlContent xmlContent) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("rainforest");
        rootElement.setAttribute("macId", currentPulseFragment.macId);
        doc.appendChild(rootElement);

        xmlContent.fillWithContent(rootElement, doc);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StringWriter writer = new StringWriter();

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(writer);

        transformer.transform(source, result);

        String output = writer.getBuffer().toString();

        if (xmlContent.showOutput()) {
            System.out.println(output);
        }
        
        return output;
    }    

    @Override
    public int queueSize() {
        return pulseList.size();
    }
}
