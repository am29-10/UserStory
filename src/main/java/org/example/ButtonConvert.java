package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ButtonConvert implements ActionListener {
    private static final Logger logger = Logger.getLogger(ButtonConvert.class.getName());
    private final File file;
    private final StringBuilder sb = new StringBuilder();
    private final int[] columnNumbers = {25, 24, 23, 22, 21, 20, 19, 18, 17};
    private final String CSV = "csv";
    private final String CHARSET_NAME = "windows-1251";

    public ButtonConvert(File file) {
        this.file = file;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Files.newInputStream(file.toPath()), CHARSET_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] array = line.split(";");
                List<String> list = new LinkedList<>(Arrays.asList(array));
                for (int i : columnNumbers) {
                    list.remove(i);
                }

                for (String str : list) {
                    if (!str.equals(list.get(list.size() - 1))) {
                        sb.append(str).append(";");
                    } else {
                        sb.append(str);
                    }
                }
                sb.append("\n");
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Возникла ошибка при чтении файла");
            throw new RuntimeException("Возникла ошибка при чтении файла: " + ex);
        }

        FileNameExtensionFilter filter = new FileNameExtensionFilter(CSV, CSV);
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(filter);
        int result = jFileChooser.showSaveDialog(null);
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String filename = jFileChooser.getSelectedFile().getPath();
        filename = filename + ".csv";
        jFileChooser.setSelectedFile(new File(filename));

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                Files.newOutputStream(jFileChooser.getSelectedFile().toPath()), CHARSET_NAME))) {
            out.write(String.valueOf(sb));
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Возникла ошибка при записи файла");
            throw new RuntimeException("Возникла ошибка при записи файла: " + ex);
        }

        if (result == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(jFileChooser,
                    "Файл '" + jFileChooser.getSelectedFile() +
                            " сохранен");
        }
    }
}
