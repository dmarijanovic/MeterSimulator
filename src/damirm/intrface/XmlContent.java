package damirm.intrface;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface XmlContent {

    void fillWithContent(Element rootElement, Document document);
    boolean showOutput();
}
