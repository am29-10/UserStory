package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ButtonSelect implements ActionListener {
    private File file;
    private final String CSV = "csv";
    private JLabel path;
    private final JButton convert;

    public ButtonSelect(JLabel path, JButton convert) {
        this.path = path;
        this.convert = convert;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.getName().endsWith(CSV)) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return ".csv";
            }
        });
        fileChooser.showOpenDialog(null);
        file = fileChooser.getSelectedFile();
        path.setText("Путь файла: " + file.getPath());
        convert.addActionListener(new ButtonConvert(file));
    }
}
