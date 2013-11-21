/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.syntea.nb.sqlxml.plugin.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.cookies.EditorCookie;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;



@Messages("CTL_RunSqlXmlAction=Run SQLXML")
public final class RunSqlXmlAction implements ActionListener,Presenter.Toolbar{

    private final EditorCookie context;

    public RunSqlXmlAction(EditorCookie context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use context
    }

    @Override
    public Component getToolbarPresenter() {
       return new ToolBarPresenter();
    }
}
