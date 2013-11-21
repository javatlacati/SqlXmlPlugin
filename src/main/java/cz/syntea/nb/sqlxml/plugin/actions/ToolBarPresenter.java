/*
 * The MIT License
 *
 * Copyright 2013 daniel.
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Daniel Kec
 */
public class ToolBarPresenter extends JPanel {


        private JLabel comboLabel;


        public ToolBarPresenter() {
            initComponents();
        }

        public Dimension getMinimumSize() {
            Dimension dim = super.getMinimumSize();
            return new Dimension(0, dim.height);
        }


        private void initComponents() {
            setLayout(new BorderLayout(4, 0));
            setBorder(new EmptyBorder(0, 2, 0, 8));
            setOpaque(false);


            comboLabel = new JLabel();
            comboLabel.setText("Ahoj svete");
            comboLabel.setOpaque(false);
            //comboLabel.setLabelFor(combo);
            add(comboLabel, BorderLayout.WEST);
        }

        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
        }
    }

