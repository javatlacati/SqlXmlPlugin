/*
 * The MIT License
 *
 * Copyright 2013 Daniel Kec <daniel at kecovi.cz>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package cz.syntea.nb.sqlxml.plugin.completion;

import java.beans.Transient;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.openide.util.Exceptions;


/**
 *
 * @author Daniel Kec
 */
@XmlRootElement(name = "SqlXmlCompletionTokens")
@XmlAccessorType(XmlAccessType.FIELD)
public class SqlXmlCompletionTokens {
    private static SqlXmlCompletionTokens instance;
    @XmlElement(name = "CompletionItem")
    private List<SqlXmlCompletionItem> itemList;

    private SqlXmlCompletionTokens(){}
    public static SqlXmlCompletionTokens getInstance(){
        if(null == instance){
            try {
                instance = unmarshal(new Scanner(SqlXmlCompletionTokens.class.getResourceAsStream("SqlXmlCompletionTokens.xml")).useDelimiter("\\A").next());
            } catch (JAXBException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return instance;
    }

    public List<SqlXmlCompletionItem> getSqlXmlCompletionItems(){
        return itemList;
    }

    /**
     * Unmarshalling itself from XML document by JAXB annotations
     * @param xml xml document serialized as String
     * @return serialized XML document
     * @throws JAXBException
     */
    @Transient
    private static SqlXmlCompletionTokens unmarshal(String xml) throws JAXBException{
        JAXBContext jc = JAXBContext.newInstance(SqlXmlCompletionTokens.class);
        Unmarshaller u = jc.createUnmarshaller();
        return (SqlXmlCompletionTokens) u.unmarshal(new StringReader(xml));
    }
}
