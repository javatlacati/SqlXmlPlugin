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

import static cz.syntea.nb.sqlxml.plugin.output.XDCMOutputTopComponent.PATH_ICON_RUN;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.beans.Transient;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Daniel Kec
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SqlXmlCompletionItem implements CompletionItem {

    @XmlAttribute(name="name")
    private String name;
    @XmlAttribute(name="insertText")
    private String insertText;
    @XmlAttribute()
    private String dbmss;//suported by
    @XmlElement(name="Documentation")
    private String doc;
    @XmlAttribute
    private String parent;
    @XmlElement
    private String url = "http://architects.dzone.com/articles/sqlx-db-straight-xml-and-back";
    @XmlAttribute
    private int caret = 0;
    private static final ImageIcon runIcon = ImageUtilities.loadImageIcon(PATH_ICON_RUN, true);
    private static Color fieldColor = Color.decode("0x0000B2");
    private int caretOffset;
    private int startOffset;

    private SqlXmlCompletionItem(){}
    
    public SqlXmlCompletionItem(int caretOffset) {
        this.caretOffset = caretOffset;
    }

    @Override
    public void defaultAction(JTextComponent jtc) {
       try {
        StyledDocument doc = (StyledDocument) jtc.getDocument();
            //Here we remove the characters starting at the start offset
            //and ending at the point where the caret is currently found:
            doc.remove(startOffset, caretOffset-startOffset);
            doc.insertString(startOffset, insertText, null);
            System.out.println(jtc.getCaretPosition()+" + "+caret+" = "+jtc.getCaretPosition()+caret);
            jtc.setCaretPosition(jtc.getCaretPosition()+caret);
        

        
        Completion.get().hideAll();
    } catch (BadLocationException ex) {
        Exceptions.printStackTrace(ex);
    }
    }
       

    @Override
    public void processKeyEvent(KeyEvent ke) {}

    @Override
    public int getPreferredWidth(Graphics graphics, Font font) {
        return CompletionUtilities.getPreferredWidth(getText(), null, graphics, font);
    }

    @Override
    public void render(Graphics g, Font defaultFont, Color defaultColor,Color backgroundColor, int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(runIcon,
                getText(), 
                dbmss, 
                g,
                defaultFont,
                (selected ? Color.white : fieldColor), 
                width, 
                height, 
                selected);
    }

    @Override
    public CompletionTask createDocumentationTask() {
        return new AsyncCompletionTask(new AsyncCompletionQuery() {
                        @Override
                        protected void query(CompletionResultSet crs, Document document, int i) {
                                crs.setDocumentation(new SqlXmlCompletionDocumentation(doc,url));
                                crs.finish();
                        }
                });
        
    }

    @Override
    public CompletionTask createToolTipTask() {
         return null;
    }

    @Override
    public boolean instantSubstitution(JTextComponent jtc) {
        return false;
    }

    @Override
    public int getSortPriority() {
        return 0;
    }

    @Override
    public CharSequence getSortText() {
       return name;
    }
    /**
     * Returns a text used for finding of a longest common prefix
     * after the <i>TAB</i> gets pressed or when the completion is opened explicitly.
     * <br>
     * The completion infrastructure will evaluate the insert prefixes
     * of all the items present in the visible result and finds the longest
     * common prefix.
     *
     * <p>
     * Generally the returned text does not need to contain all the information
     * that gets inserted when the item is selected.
     * <br>
     * For example in java completion the field name should be returned for fields
     * or a method name for methods (but not parameters)
     * or a non-FQN name for classes.
     *
     * @return non-null character sequence containing the insert prefix.
     *  <br>
     *  Returning an empty string will effectively disable the TAB completion
     *  as the longest common prefix will be empty.
     *
     * @since 1.4
     */
    @Override
    public CharSequence getInsertPrefix() {
         return "insertPrefix";
    }


    public String getText() {
        return name;
    }

    @Transient
    public void setCaretOffset(int caretOffset) {
       this.caretOffset = caretOffset;
    }

    void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

}
