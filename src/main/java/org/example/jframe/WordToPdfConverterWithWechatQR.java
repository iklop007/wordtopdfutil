package org.example.jframe;

import org.example.file.ImageUtils;
import org.example.word.Word2PdfJacobUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class WordToPdfConverterWithWechatQR extends JFrame {
    private JTextField wordFilePathField;
    private JTextField pdfFilePathField;
    private JButton browseButton;
    private JButton convertButton;
    private JButton alipayButton;
    private JButton wechatButton;
    private JFileChooser fileChooser;
    private boolean paymentSuccessful = false;
    private JLabel paymentStatusLabel;
    private BufferedImage wechatQRImage;
    private final double PAYMENT_AMOUNT = 2.00;

    public WordToPdfConverterWithWechatQR() {
        setTitle("Word转PDF转换器 - 付费版 (¥2.00)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        // 加载微信支付图片
        loadWechatQRImage();

        // 初始化组件
        initComponents();

        // 设置布局
        setLayout(new BorderLayout());
        add(createPaymentPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        updateUIState();
    }

    private void loadWechatQRImage() {
        try {
            // 从文件加载微信支付二维码图片
            // 请确保图片文件 f6264a90b41cb60a3b778848792de05.jpg 放在同一目录下
            // ImageIO.read(new File("src\\main\\java\\org\\example\\pic\\f6264a90b41cb60a3b778848792de05.jpg"));
            wechatQRImage = ImageUtils.loadAndResizeImage(new File("src\\main\\java\\org\\example\\pic\\f6264a90b41cb60a3b778848792de05.jpg"), 300, 300, ImageUtils.ScaleMode.FILL);
        } catch (Exception e) {
            System.err.println("无法加载微信支付二维码图片: " + e.getMessage());
            // 创建默认的替代图片
            wechatQRImage = createDefaultQRImage();
        }
    }

    private BufferedImage createDefaultQRImage() {
        BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 绘制白色背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 300, 300);

        // 绘制黑色边框
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 299, 299);

        // 绘制提示文字
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("微软雅黑", Font.BOLD, 16));
        g2d.drawString("微信支付二维码加载失败", 50, 150);
        g2d.drawString("请检查图片文件", 80, 180);

        g2d.dispose();
        return image;
    }

    private void initComponents() {
        wordFilePathField = new JTextField();
        wordFilePathField.setToolTipText("只接受.doc或.docx文件");

        pdfFilePathField = new JTextField();
        pdfFilePathField.setEditable(false);
        pdfFilePathField.setBackground(new Color(240, 240, 240));

        browseButton = new JButton("浏览...");
        convertButton = new JButton("转换为PDF");
        convertButton.setBackground(new Color(0, 128, 0));
        convertButton.setForeground(Color.BLACK);
        convertButton.setFont(new Font("微软雅黑", Font.BOLD, 14));

        alipayButton = new JButton("支付宝支付");
        alipayButton.setBackground(new Color(64, 0, 255));
        alipayButton.setForeground(Color.BLACK);
        alipayButton.setFont(new Font("微软雅黑", Font.BOLD, 14));

        wechatButton = new JButton("微信支付");
        wechatButton.setBackground(new Color(84, 7, 193));
        wechatButton.setForeground(Color.BLACK);
        wechatButton.setFont(new Font("微软雅黑", Font.BOLD, 14));

        paymentStatusLabel = new JLabel("未支付");
        paymentStatusLabel.setForeground(Color.RED);
        paymentStatusLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Word文档 (*.doc, *.docx)", "doc", "docx"));

        // 添加事件监听器
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(WordToPdfConverterWithWechatQR.this);
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
                if (!paymentSuccessful) {
                    JOptionPane.showMessageDialog(WordToPdfConverterWithWechatQR.this,
                            "请先完成支付才能使用转换功能", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String wordPath = wordFilePathField.getText();
                String pdfPath = pdfFilePathField.getText();

                if (wordPath.isEmpty()) {
                    JOptionPane.showMessageDialog(WordToPdfConverterWithWechatQR.this,
                            "请选择Word文件", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!wordPath.matches(".*\\.docx?$")) {
                    JOptionPane.showMessageDialog(WordToPdfConverterWithWechatQR.this,
                            "只支持.doc或.docx文件", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 执行转换
                performConversion(wordPath, pdfPath);
            }
        });

        alipayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAlipayDialog();
            }
        });

        wechatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showWechatDialog();
            }
        });
    }

    private void showWechatDialog() {
        JDialog wechatDialog = new JDialog(this, "微信支付 - 扫码支付", true);
        wechatDialog.setSize(500, 700);
        wechatDialog.setLocationRelativeTo(this);
        wechatDialog.setLayout(new BorderLayout());

        // 标题
        JLabel titleLabel = new JLabel("微信支付 - 扫码支付", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // 金额显示
        JLabel amountLabel = new JLabel("支付金额: ¥" + String.format("%.2f", PAYMENT_AMOUNT), JLabel.CENTER);
        amountLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        amountLabel.setForeground(new Color(255, 0, 0));

        // 二维码图片面板
        JPanel qrCodePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // 绘制二维码图片
                if (wechatQRImage != null) {
                    int x = (getWidth() - wechatQRImage.getWidth()) / 2;
                    int y = 20;
                    g2d.drawImage(wechatQRImage, x, y, null);
                }
            }
        };
        qrCodePanel.setPreferredSize(new Dimension(450, 350));

        // 支付说明
        JTextArea instructionArea = new JTextArea(
                "支付说明:\n" +
                        "1. 请使用微信扫描上方二维码完成支付\n" +
                        "2. 支付金额: ¥2.00 人民币\n" +
                        "3. 收款方: 大橙子（**成）\n" +
                        "4. 支付成功后即可解锁Word转PDF功能\n" +
                        "5. 支付完成后点击'模拟支付成功'按钮"
        );
        instructionArea.setEditable(false);
        instructionArea.setBackground(new Color(240, 240, 240));
        instructionArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        instructionArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton simulatePayButton = new JButton("模拟支付成功");
        simulatePayButton.setBackground(new Color(7, 193, 96));
        simulatePayButton.setForeground(Color.BLACK);
        simulatePayButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        simulatePayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paymentSuccessful = true;
                paymentStatusLabel.setText("已支付");
                paymentStatusLabel.setForeground(new Color(0, 128, 0));
                updateUIState();
                wechatDialog.dispose();
                JOptionPane.showMessageDialog(WordToPdfConverterWithWechatQR.this,
                        "微信支付成功！\n您已支付 ¥2.00\n现在可以使用Word转PDF功能了",
                        "支付成功", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("取消支付");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wechatDialog.dispose();
            }
        });

        buttonPanel.add(simulatePayButton);
        buttonPanel.add(cancelButton);

        // 组装对话框
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(amountLabel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(qrCodePanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(instructionArea, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        wechatDialog.add(northPanel, BorderLayout.NORTH);
        wechatDialog.add(centerPanel, BorderLayout.CENTER);
        wechatDialog.add(southPanel, BorderLayout.SOUTH);

        wechatDialog.setVisible(true);
    }

    private void showAlipayDialog() {
        JDialog alipayDialog = new JDialog(this, "支付宝支付", true);
        alipayDialog.setSize(500, 600);
        alipayDialog.setLocationRelativeTo(this);
        alipayDialog.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("支付宝支付", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));

        JLabel amountLabel = new JLabel("支付金额: ¥" + String.format("%.2f", PAYMENT_AMOUNT), JLabel.CENTER);
        amountLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        amountLabel.setForeground(Color.RED);

        // 支付宝账号信息
        JPanel accountPanel = new JPanel(new GridLayout(2, 1));
        accountPanel.setBorder(BorderFactory.createTitledBorder("支付宝收款信息"));
        accountPanel.add(new JLabel("收款账号: 18618143404", JLabel.CENTER));
        accountPanel.add(new JLabel("收款人: 大橙子", JLabel.CENTER));

        JTextArea instructionArea = new JTextArea(
                "支付宝支付说明:\n" +
                        "1. 请打开支付宝APP\n" +
                        "2. 使用扫一扫功能\n" +
                        "3. 扫描屏幕上的二维码\n" +
                        "4. 支付金额: ¥2.00\n" +
                        "5. 支付成功后点击'模拟支付成功'"
        );
        instructionArea.setEditable(false);
        instructionArea.setBackground(new Color(240, 240, 240));

        JButton simulateButton = new JButton("模拟支付宝支付成功");
        simulateButton.setBackground(new Color(0, 168, 255));
        simulateButton.setForeground(Color.BLACK);
        simulateButton.addActionListener(e -> {
            paymentSuccessful = true;
            paymentStatusLabel.setText("已支付");
            paymentStatusLabel.setForeground(new Color(0, 128, 0));
            updateUIState();
            alipayDialog.dispose();
            JOptionPane.showMessageDialog(this, "支付宝支付成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        });

        alipayDialog.add(titleLabel, BorderLayout.NORTH);
        alipayDialog.add(amountLabel, BorderLayout.CENTER);
        alipayDialog.add(accountPanel, BorderLayout.CENTER);
        alipayDialog.add(instructionArea, BorderLayout.CENTER);
        alipayDialog.add(simulateButton, BorderLayout.SOUTH);

        alipayDialog.setVisible(true);
    }

    private void performConversion(String wordPath, String pdfPath) {
        try {
            convertButton.setEnabled(false);
            convertButton.setText("转换中...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Word2PdfJacobUtil.documents4jWordToPdf(wordPath, pdfPath);
                    Thread.sleep(2000);
                    return null;
                }

                @Override
                protected void done() {
                    convertButton.setEnabled(true);
                    convertButton.setText("转换为PDF");
                    convertButton.setBackground(new Color(76, 175, 80));
                    convertButton.setForeground(Color.ORANGE);
                    JOptionPane.showMessageDialog(WordToPdfConverterWithWechatQR.this,
                            "转换完成！\nPDF文件已保存到: " + pdfPath,
                            "转换成功", JOptionPane.INFORMATION_MESSAGE);
                }
            };
            worker.execute();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(WordToPdfConverterWithWechatQR.this,
                    "转换失败: " + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            convertButton.setEnabled(true);
            convertButton.setText("转换为PDF");
        }
    }

    private void updateUIState() {
        convertButton.setEnabled(paymentSuccessful);
        if (paymentSuccessful) {
            convertButton.setBackground(new Color(76, 175, 80));
            convertButton.setForeground(Color.WHITE);
        } else {
            convertButton.setBackground(Color.LIGHT_GRAY);
            convertButton.setForeground(Color.BLACK);
        }
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 150, 136)),
                "支付方式选择 - 每次转换需支付 ¥2.00",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("微软雅黑", Font.BOLD, 12),
                new Color(0, 150, 136)
        ));

        panel.add(alipayButton);
        panel.add(wechatButton);
        panel.add(new JLabel("支付状态:"));
        panel.add(paymentStatusLabel);

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel wordPanel = new JPanel(new BorderLayout(10, 10));
        wordPanel.setBorder(BorderFactory.createTitledBorder("选择Word文件 (.doc或.docx)"));
        wordPanel.add(wordFilePathField, BorderLayout.CENTER);
        wordPanel.add(browseButton, BorderLayout.EAST);

        JPanel pdfPanel = new JPanel(new BorderLayout(10, 10));
        pdfPanel.setBorder(BorderFactory.createTitledBorder("PDF输出路径"));
        pdfPanel.add(pdfFilePathField, BorderLayout.CENTER);

        panel.add(wordPanel);
        panel.add(pdfPanel);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(convertButton);
        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WordToPdfConverterWithWechatQR().setVisible(true);
            }
        });
    }
}