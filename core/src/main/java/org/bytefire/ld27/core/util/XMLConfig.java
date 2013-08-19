package org.bytefire.ld27.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

public class XMLConfig {

    private ConfigHandler handler;

    private int width;
    private int height;

    public XMLConfig(InputStream stream){
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            handler = new ConfigHandler();
            parser.parse(stream, handler);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLConfig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLConfig.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

        width = handler.getWidth();
        height = handler.getHeight();
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
