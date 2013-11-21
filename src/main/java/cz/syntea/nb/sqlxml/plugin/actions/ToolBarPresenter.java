/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

