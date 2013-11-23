/*
 * The MIT License
 *
 * Copyright 2013 Daniel Kec.
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
package cz.syntea.nb.sqlxml.plugin.actions;

import cz.syntea.nb.sqlxml.plugin.output.XDCMOutputTopComponent;
import cz.syntea.nb.sqlxml.plugin.output.XmlUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLXML;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.openide.ErrorManager;
import org.openide.cookies.EditorCookie;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Build",
        id = "cz.syntea.nb.sqlxml.plugin.actions.RunSqlXmlAction"
)
@ActionRegistration(
        iconBase = "cz/syntea/nb/sqlxml/plugin/run.png",
        displayName = "#CTL_RunSqlXmlAction"
)
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-sql/Actions", position = 50),
    @ActionReference(path = "Editors/text/x-sql/Popup", position = 300),
    @ActionReference(path = "Shortcuts", name="D-S-X")
    
})
@Messages("CTL_RunSqlXmlAction=Run SQLXML")
public final class RunSqlXmlAction implements ActionListener {

    private final EditorCookie context;

    public RunSqlXmlAction(EditorCookie context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        //FileUtil.getConfigObject("path/to/print/action/in/layer.xml", Action.class).actionPerformed(ev);
        // SQLEditorSupport sQLEditorSupport = (SQLEditorSupport)context;
        //SQLExecuteCookie sQLEditorSupport = (SQLExecuteCookie)context;
        XDCMOutputTopComponent out;
        out = XDCMOutputTopComponent.getDefault();
        Object invoked;
        try {
            invoked = context.getClass().getDeclaredMethod("getDatabaseConnection").invoke((Object) context);
            if (invoked == null) {
                return;
            }
            DatabaseConnection databaseConnection = (DatabaseConnection) invoked;
            Connection conn = databaseConnection.getJDBCConnection();
            if(conn==null)return;
            ResultSet rs = conn.createStatement().executeQuery(getSql());
            if (rs.next()) {
                SQLXML sqlxml = rs.getSQLXML(1);
                out.printXML(XmlUtils.format(sqlxml.getString()));
                out.open();
                out.requestActive();
            }
            rs.close();
           // JOptionPane.showMessageDialog(null, databaseConnection.getJDBCConnection());
        } catch (Exception ex) {

            ErrorManager.getDefault().notify(ex);
            out.printXML(getSql());
                out.open();
                out.requestActive();
            //Exceptions.printStackTrace(ex);
        }

   //     out.printXML("<a ddd=\""+sQLEditorSupport.getDatabaseConnection().getDisplayName()+"\"/>");
        //out.printXML("<a ddd=\"dfgdfg\"/>");
    }

    private String getSql() {
        try {
            StyledDocument document = context.getDocument();
            return document.getText(0, document.getLength());
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
}
