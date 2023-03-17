package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainWindow extends JFrame {
    private final JButton select = new JButton("Выбрать файл");
    private final JLabel path = new JLabel("Путь файла: ", SwingConstants.CENTER);
    private final JButton convert = new JButton("Конвертировать данные");
    private JFileChooser fileChooser;
    private File file;
    private final StringBuilder sb = new StringBuilder();

    public MainWindow() {
        super("Конвертация данных");
        this.setBounds(100, 100, 500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 1));

        container.add(select);
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if (f.getName().endsWith("csv")) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        return ".csv";
                    }
                });
                fileChooser.showOpenDialog(select);
                file = fileChooser.getSelectedFile();
                path.setText("Путь файла: " + file.getPath());
            }
        });

        container.add(path);

        container.add(convert);
        convert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), "windows-1251"))) {
                    int[] columnNumbers = {25, 24, 23, 22, 21, 20, 19, 18, 17};
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] array = line.split(";");
                        List<String> list = new LinkedList<>(Arrays.asList(array));
                        System.out.println(list);
                        for (int i : columnNumbers) {
                            list.remove(i);
                        }
                        System.out.println(list);

                        for (String str : list) {
                            if (str != list.get(list.size() - 1)) {
                                sb.append(str).append(";");
                            } else {
                                sb.append(str);
                            }
                        }
                        sb.append("\n");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                FileNameExtensionFilter filter = new FileNameExtensionFilter("csv", "csv");
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(filter);
                int result = jFileChooser.showSaveDialog(convert);
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                String filename = jFileChooser.getSelectedFile().getPath();
                filename = filename + ".csv";
                jFileChooser.setSelectedFile(new File(filename));

                try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(jFileChooser.getSelectedFile()), "windows-1251"))) {
                    out.write(String.valueOf(sb));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                if (result == JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(jFileChooser,
                            "Файл '" + jFileChooser.getSelectedFile() +
                                    " сохранен");
                }
            }
        });
    }
}
