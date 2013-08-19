package org.bytefire.ld27.core.util;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigHandler extends DefaultHandler {

    private boolean bresolution = false;

    private int width = 1024;
    private boolean bwidth = false;

    private int height = 768;
    private boolean bheight = false;

    @Override
    public void startElement(String uri, String localname, String qName, Attributes attributes){
        if (qName.equalsIgnoreCase("resolution")) bresolution = true;
        if (bresolution && qName.equalsIgnoreCase("width")) bwidth = true;
        if (bresolution && qName.equalsIgnoreCase("height")) bheight = true;
    }

    @Override
    public void endElement(String uri, String localName, String qName){
        if (bresolution && qName.equalsIgnoreCase("resolution")) bresolution = false;
        if (bwidth && qName.equalsIgnoreCase("width")) bwidth = false;
        if (bheight && qName.equalsIgnoreCase("height")) bheight = false;
    }

    @Override
    public void characters(char ch[], int start, int length){
        if (bwidth) width = Integer.valueOf(new String(ch, start, length));
        if (bheight) height = Integer.valueOf(new String(ch, start, length));
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

}
