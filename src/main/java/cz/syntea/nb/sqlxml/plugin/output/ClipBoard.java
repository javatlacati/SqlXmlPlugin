/*
 *  ClipBoard.java 
 * 
 *  Copyright 2010 Syntea software group a.s.
 * 
 *  This file may be used, copied, modified and distributed only in accordance
 *  with the terms of the limited licence contained in the accompanying
 *  file LICENSE.TXT.
 * 
 *  Tento soubor muze byt pouzit, kopirovan, modifikovan a siren pouze v souladu
 *  s licencnimi podminkami uvedenymi v prilozenem souboru LICENSE.TXT.
 */
package cz.syntea.nb.sqlxml.plugin.output;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExClipboard;

/**
 * Manage to get and set clipboard text content.
 * @author Daniel Kec <Daniel.Kec at syntea.cz>
 * @since 15.2.2010
 * @version 1.0
 */
public class ClipBoard implements ClipboardOwner {

	private Clipboard _clipboard;

	/**
	 * Manage to get and set clipboard text content.
	 */
	public ClipBoard() {
		_clipboard = Lookup.getDefault().lookup(ExClipboard.class);
	}

	/**
	 * If a string is on the system clipboard, this method returns it;
	 * otherwise it returns null.
	 * @return clipboard text insights.
	 */
	public String getClipboardContents() {
		Transferable t =
			Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		try {
			if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String text =
					(String) t.getTransferData(DataFlavor.stringFlavor);
				return text;
			}
		} catch (UnsupportedFlavorException e) {
		} catch (IOException e) {
		}
		return null;
	}

	/**
	 * Set text content of clipboard.
	 * @param content text content
	 */
	public void setClipboardContents(String content) {
		if (_clipboard != null) {
			if (content == null) {
				StatusDisplayer.getDefault().setStatusText("");
				_clipboard.setContents(null, null);
			} else {
				StatusDisplayer.getDefault().setStatusText("Clipboard: " +
					content);
				_clipboard.setContents(new StringSelection(content), null);
			}
		}
	}

	/**
	 * Don't need to know that yet.
	 * @param clipboard
	 * @param contents
	 */
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}
}
