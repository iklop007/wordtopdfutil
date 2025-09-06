package org.example.jframe;

import org.example.word.Word2PdfJacobUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class WordToPdfConverter extends JFrame {
    private JTextField wordFilePathField;
    private JTextField pdfFilePathField;
    private JButton browseButton;
    private JButton convertButton;
    private JFileChooser fileChooser;

    public WordToPdfConverter() {
        setTitle("Word转PDF转换器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 200);
        setLocationRelativeTo(null);
        setResizable(false);

        // 初始化组件
        initComponents();

        // 设置布局
        setLayout(new BorderLayout());
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private void initComponents() {
        wordFilePathField = new JTextField();
        wordFilePathField.setToolTipText("只接受.doc或.docx文件");

        pdfFilePathField = new JTextField();
        pdfFilePathField.setEditable(false);
        pdfFilePathField.setBackground(new Color(240, 240, 240));

        browseButton = new JButton("浏览...");
        convertButton = new JButton("转换为PDF");

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Word文档 (*.doc, *.docx)", "doc", "docx"));

        // 添加事件监听器
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(WordToPdfConverter.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    wordFilePathField.setText(selectedFile.getAbsolutePath());

                    // 自动生成PDF文件路径
                    String pdfPath = selectedFile.getAbsolutePath().replaceAll("\\.docx?$", ".pdf");
                    pdfFilePathField.setText(pdfPath);
                }
            }
        });

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wordPath = wordFilePathField.getText();
                String pdfPath = pdfFilePathField.getText();

                if (wordPath.isEmpty()) {
                    JOptionPane.showMessageDialog(WordToPdfConverter.this,
                            "请选择Word文件", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!wordPath.matches(".*\\.docx?$")) {
                    JOptionPane.showMessageDialog(WordToPdfConverter.this,
                            "只支持.doc或.docx文件", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 这里应该是实际的转换代码
                // 由于Word转PDF需要第三方库，这里只模拟转换过程
                try {
                    // 模拟转换过程
                    Word2PdfJacobUtil.documents4jWordToPdf(wordPath, pdfPath);
                    Thread.sleep(2000);

                    JOptionPane.showMessageDialog(WordToPdfConverter.this,
                            "转换完成！\nPDF文件已保存到: " + pdfPath,
                            "成功", JOptionPane.INFORMATION_MESSAGE);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Word文件路径面板
        JPanel wordPanel = new JPanel(new BorderLayout(5, 5));
        wordPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Word文件路径",
                TitledBorder.LEFT,
                TitledBorder.TOP));

        wordPanel.add(wordFilePathField, BorderLayout.CENTER);
        wordPanel.add(browseButton, BorderLayout.EAST);

        // PDF文件路径面板
        JPanel pdfPanel = new JPanel(new BorderLayout(5, 5));
        pdfPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "PDF输出路径",
                TitledBorder.LEFT,
                TitledBorder.TOP));

        pdfPanel.add(pdfFilePathField, BorderLayout.CENTER);

        panel.add(wordPanel);
        panel.add(pdfPanel);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.add(convertButton);
        return panel;
    }
    public static void main(String[] args) {
        // 设置外观为系统默认
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WordToPdfConverter().setVisible(true);
            }
        });
    }
}
