package org.example;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        super("Конвертация данных");
        this.setBounds(100, 100, 500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 1));

        JButton select = new JButton("Выбрать файл");
        container.add(select);
        JLabel path = new JLabel("Путь файла: ", SwingConstants.CENTER);
        container.add(path);
        JButton convert = new JButton("Конвертировать данные");
        container.add(convert);
        select.addActionListener(new ButtonSelect(path, convert));

    }
}
