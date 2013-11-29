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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.api.db.explorer.ConnectionManager;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.openide.cookies.EditorCookie;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.awt.NotificationDisplayer;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Mutex;
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
   // @ActionReference(path = "Loaders/text/x-sql/Actions", position = 50),
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
        //SQLEditorSupport sQLEditorSupport = (SQLEditorSupport)context;
        //SQLExecuteCookie sQLEditorSupport = (SQLExecuteCookie)context;
        
        //System.out.println(""+Lookup.getDefault().lookup(SQLExecuteCookie.class));
        
        XDCMOutputTopComponent out;
        out = XDCMOutputTopComponent.getDefault();
        Object invoked;
        Connection conn = null;
        try {
            invoked = context.getClass().getDeclaredMethod("getDatabaseConnection").invoke((Object) context);
            if (invoked == null) {
                return;
            }
            final DatabaseConnection databaseConnection = (DatabaseConnection) invoked;
            // Reconnect when datasource is not connected
            Mutex.EVENT.readAccess(new Mutex.Action<Void>() {
                    @Override
                    public Void run() {
                        ConnectionManager.getDefault().showConnectionDialog(databaseConnection);
                        return null;
                    }
            });
            
            conn = databaseConnection.getJDBCConnection();
            if(conn==null)return;
            ResultSet rs = conn.createStatement().executeQuery(getSql());
            if (rs.next()&&rs.getMetaData().getColumnCount()!=0) {
                Object o;
                try{
                    o = rs.getObject(1);
                }catch(NullPointerException e){
                    
                    Exception e2 = new JDBCDriverProblemException(e);//Exceptions.attachMessage(e, "There is a problem with jdbc driver!!!");
                    Exceptions.printStackTrace(e2);
                    rs.close();
                    return;
                }
                if(o instanceof SQLXML){
                    SQLXML sqlxml = rs.getSQLXML(1);
                    out.printXML(XmlUtils.format(sqlxml.getString()));
                    out.open();
                    out.requestActive();
                }else if(o instanceof Clob){
                    String result = clobToString((Clob)o);
                    out.printXML(XmlUtils.format(result));
                    out.open();
                    out.requestActive();
                }else if(o instanceof String){                  
                    out.printXML(XmlUtils.format((String)o));
                    out.open();
                    out.requestActive();
                }else{
                    NotificationDisplayer.getDefault().notify(
                        "SqlXml Plugin",
                        ImageUtilities.loadImageIcon("/cz/syntea/nb/sqlxml/plugin/run.png", true), 
                        "Unknow datatype in the first column of the first row.\n"
                      + "Found datatype: "+o.getClass().getName()+",\n"
                      + "Supported datatypes: SQLXML,Clob,String",
                        null);
                }
                
            }
            rs.close();

        } catch (SQLException ex) {
            if(ex.getMessage().contains("Unsupported feature")){
                 NotificationDisplayer.getDefault().notify(
                        "SqlXml Plugin",
                        ImageUtilities.loadImageIcon("/cz/syntea/nb/sqlxml/plugin/run.png", true), 
                        "JDBC Driver says: \"Unsupported feature\", more info in IDE Log.\n"
                      + "If your driver doesn't support SQLXML type, use XMLSERIALIZE",
                        null);
            }
            ex.printStackTrace();
            try {
                // when there is error in the query, lets run standard run statement action 
                // it will show errors by standard means
                context.getClass().getDeclaredMethod("execute").invoke((Object) context);
            } catch (Exception ex1) {
                Exceptions.printStackTrace(ex1);
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private String getSql() {
        try {
            StyledDocument document = context.getDocument();
            String text = document.getText(0, document.getLength()).trim();
            if(text.endsWith(";")){
                text = text.substring(0,text.length()-1);
            }
            
            //It have to be only one query
            if(text.contains(";")){
                int[] pos = calculatePos(text, ";");
                NotificationDisplayer.getDefault().notify(
                        "SqlXml Plugin",
                        ImageUtilities.loadImageIcon("/cz/syntea/nb/sqlxml/plugin/run.png", true), 
                        "Please remove the semicolon, query must have only one statement.\n"
                      + "Semicolon is located on the row= "+pos[0]+", column= "+pos[1],
                        null);
            }
            return text;
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
    
    private int[] calculatePos(String text,String mask){
        int[] vector = new int[2];
        try{
        int index = text.indexOf(mask);
        String beforeText = text.substring(0, index+1);
        String[] beforeLines = beforeText.split("\n");
        vector[0] = beforeLines.length;
        vector[1] = beforeLines[beforeLines.length-1].length();
        }catch(Exception e){
            vector=new int[]{0,0};
        }
        return vector;
    }

        /**
     * Prevede clob na string
     * @param data clob na prevedeni
     * @return vysledny string
     * @throws SQLException
     * @throws IOException 
     */
    public static String clobToString(Clob data) throws SQLException, IOException {
        StringBuilder sb = new StringBuilder();
        Reader reader = data.getCharacterStream();
        BufferedReader br = new BufferedReader(reader);
        String line;
        while (null != (line = br.readLine())) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
