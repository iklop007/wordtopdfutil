package org.example.jframe;

import org.example.word.Word2PdfJacobUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class RealWechatPaymentConverter extends JFrame {
    private JTextField wordFilePathField;
    private JTextField pdfFilePathField;
    private JButton browseButton;
    private JButton convertButton;
    private JButton wechatPayButton;
    private JFileChooser fileChooser;
    private boolean paymentSuccessful = false;
    private JLabel paymentStatusLabel;
    private JLabel countdownLabel;
    private Timer paymentCheckTimer;
    private final String PAYMENT_AMOUNT = "2.00";
    private final String MERCHANT_ID = "YOUR_MERCHANT_ID"; // 需要替换为实际商户ID
    private final String APP_ID = "YOUR_APP_ID"; // 需要替换为实际APP_ID

    public RealWechatPaymentConverter() {
        setTitle("Word转PDF转换器 - 微信支付版 (¥2.00)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        // 初始化组件
        initComponents();

        // 设置布局
        setLayout(new BorderLayout());
        add(createPaymentPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        updateUIState();
    }

    private void initComponents() {
        wordFilePathField = new JTextField();
        wordFilePathField.setToolTipText("只接受.doc或.docx文件");

        pdfFilePathField = new JTextField();
        pdfFilePathField.setEditable(false);
        pdfFilePathField.setBackground(new Color(240, 240, 240));

        browseButton = new JButton("浏览...");
        convertButton = new JButton("转换为PDF");

        wechatPayButton = new JButton("微信支付 ¥2.00");
        wechatPayButton.setBackground(new Color(7, 193, 96));
        wechatPayButton.setForeground(Color.WHITE);
        wechatPayButton.setFont(new Font("微软雅黑", Font.BOLD, 14));

        paymentStatusLabel = new JLabel("未支付");
        paymentStatusLabel.setForeground(Color.RED);
        paymentStatusLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));

        countdownLabel = new JLabel("");
        countdownLabel.setForeground(Color.BLUE);
        countdownLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Word文档 (*.doc, *.docx)", "doc", "docx"));

        // 添加事件监听器
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(RealWechatPaymentConverter.this);
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
                    JOptionPane.showMessageDialog(RealWechatPaymentConverter.this,
                            "请先完成微信支付才能使用转换功能", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String wordPath = wordFilePathField.getText();
                String pdfPath = pdfFilePathField.getText();

                if (wordPath.isEmpty()) {
                    JOptionPane.showMessageDialog(RealWechatPaymentConverter.this,
                            "请选择Word文件", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!wordPath.matches(".*\\.docx?$")) {
                    JOptionPane.showMessageDialog(RealWechatPaymentConverter.this,
                            "只支持.doc或.docx文件", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 执行转换
                performConversion(wordPath, pdfPath);
            }
        });

        wechatPayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showWechatPaymentDialog();
            }
        });
    }

    private void showWechatPaymentDialog() {
        JDialog paymentDialog = new JDialog(this, "微信支付", true);
        paymentDialog.setSize(500, 650);
        paymentDialog.setLocationRelativeTo(this);
        paymentDialog.setLayout(new BorderLayout());

        // 标题
        JLabel titleLabel = new JLabel("微信支付 - 扫码支付", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // 金额显示
        JLabel amountLabel = new JLabel("支付金额: ¥" + PAYMENT_AMOUNT, JLabel.CENTER);
        amountLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        amountLabel.setForeground(new Color(255, 0, 0));

        // 二维码图片面板 - 显示附件中的微信支付二维码
        JPanel qrCodePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 绘制支付信息背景
                g2d.setColor(Color.WHITE);
                g2d.fillRect(50, 20, 400, 400);

                // 绘制边框
                g2d.setColor(new Color(7, 193, 96));
                g2d.drawRect(50, 20, 400, 400);

                // 绘制支付信息
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("微软雅黑", Font.BOLD, 16));
                g2d.drawString("推荐使用微信支付", 150, 60);

                g2d.setColor(Color.RED);
                g2d.setFont(new Font("微软雅黑", Font.BOLD, 24));
                g2d.drawString("￥" + PAYMENT_AMOUNT, 200, 100);

                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                g2d.drawString("大橙子（**成）", 180, 130);

                g2d.setColor(new Color(7, 193, 96));
                g2d.setFont(new Font("微软雅黑", Font.BOLD, 18));
                g2d.drawString("微信支付", 190, 160);

                // 绘制模拟二维码
                g2d.setColor(Color.BLACK);
                for (int i = 0; i < 200; i++) {
                    int x = 120 + (int)(Math.random() * 260);
                    int y = 180 + (int)(Math.random() * 160);
                    g2d.fillRect(x, y, 4, 4);
                }

                // 绘制微信logo
                g2d.setColor(new Color(7, 193, 96));
                g2d.fillOval(210, 250, 80, 80);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("微软雅黑", Font.BOLD, 20));
                g2d.drawString("微", 235, 295);
            }
        };
        qrCodePanel.setPreferredSize(new Dimension(500, 450));

        // 支付说明
        JTextArea instructionArea = new JTextArea(
                "支付说明:\n" +
                        "1. 请使用微信扫描上方二维码完成支付\n" +
                        "2. 支付金额: ¥2.00 人民币\n" +
                        "3. 收款方: 大橙子（**成）\n" +
                        "4. 支付成功后系统会自动检测并解锁功能\n" +
                        "5. 支付有效时间: 5分钟\n" +
                        "6. 请勿关闭此窗口，支付完成后会自动更新状态"
        );
        instructionArea.setEditable(false);
        instructionArea.setBackground(new Color(240, 240, 240));
        instructionArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        instructionArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 倒计时标签
        JLabel timeLabel = new JLabel("支付倒计时: 05:00", JLabel.CENTER);
        timeLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        timeLabel.setForeground(Color.RED);

        // 支付状态标签
        JLabel statusLabel = new JLabel("等待支付中...", JLabel.CENTER);
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton checkPaymentButton = new JButton("检查支付状态");
        checkPaymentButton.setBackground(new Color(7, 193, 96));
        checkPaymentButton.setForeground(Color.WHITE);
        checkPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkPaymentStatus(statusLabel, paymentDialog);
            }
        });

        JButton cancelButton = new JButton("取消支付");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (paymentCheckTimer != null) {
                    paymentCheckTimer.cancel();
                }
                paymentDialog.dispose();
            }
        });

        buttonPanel.add(checkPaymentButton);
        buttonPanel.add(cancelButton);

        // 组装对话框
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(amountLabel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(qrCodePanel, BorderLayout.CENTER);
        centerPanel.add(timeLabel, BorderLayout.SOUTH);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(instructionArea, BorderLayout.CENTER);
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        statusPanel.add(buttonPanel, BorderLayout.SOUTH);
        southPanel.add(statusPanel, BorderLayout.SOUTH);

        paymentDialog.add(northPanel, BorderLayout.NORTH);
        paymentDialog.add(centerPanel, BorderLayout.CENTER);
        paymentDialog.add(southPanel, BorderLayout.SOUTH);

        // 启动支付状态检查定时器
        startPaymentStatusCheck(statusLabel, timeLabel, paymentDialog);

        paymentDialog.setVisible(true);
    }

    private void startPaymentStatusCheck(JLabel statusLabel, JLabel timeLabel, JDialog dialog) {
        if (paymentCheckTimer != null) {
            paymentCheckTimer.cancel();
        }

        paymentCheckTimer = new Timer();
        final int[] timeLeft = {300}; // 5分钟倒计时

        paymentCheckTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    timeLeft[0]--;
                    if (timeLeft[0] <= 0) {
                        timeLabel.setText("支付已超时");
                        timeLabel.setForeground(Color.RED);
                        this.cancel();
                        return;
                    }

                    int minutes = timeLeft[0] / 60;
                    int seconds = timeLeft[0] % 60;
                    timeLabel.setText(String.format("支付倒计时: %02d:%02d", minutes, seconds));

                    // 模拟检查支付状态（实际应调用微信支付API）
                    boolean paymentSuccess = simulateCheckPaymentStatus();

                    if (paymentSuccess) {
                        paymentSuccessful = true;
                        statusLabel.setText("支付成功！");
                        statusLabel.setForeground(new Color(0, 128, 0));
                        updateUIState();
                        JOptionPane.showMessageDialog(RealWechatPaymentConverter.this,
                                "微信支付成功！\n您已支付 ¥2.00\n现在可以使用Word转PDF功能了",
                                "支付成功", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        this.cancel();
                    }
                });
            }
        }, 0, 1000); // 每秒检查一次
    }

    private boolean simulateCheckPaymentStatus() {
        // 模拟支付检查 - 10%的概率返回支付成功
        // 实际环境中应该调用微信支付查询API:
        // https://api.mch.weixin.qq.com/pay/orderquery
        return Math.random() < 0.1; // 10%的成功率用于演示
    }


    private void checkPaymentStatus(JLabel statusLabel, JDialog dialog) {
        // 模拟调用微信支付查询API
        statusLabel.setText("正在查询支付状态...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // 模拟网络请求延迟
                Thread.sleep(2000);
                return simulateCheckPaymentStatus();
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        paymentSuccessful = true;
                        statusLabel.setText("支付成功！");
                        statusLabel.setForeground(new Color(0, 128, 0));
                        updateUIState();
                        JOptionPane.showMessageDialog(RealWechatPaymentConverter.this,
                                "微信支付成功！", "支付成功", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                    } else {
                        statusLabel.setText("尚未支付，请完成支付");
                        statusLabel.setForeground(Color.RED);
                    }
                } catch (Exception e) {
                    statusLabel.setText("查询失败: " + e.getMessage());
                    statusLabel.setForeground(Color.RED);
                }
            }
        };
        worker.execute();
    }

    private void performConversion(String wordPath, String pdfPath) {
        try {
            convertButton.setEnabled(false);
            convertButton.setText("转换中...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Thread.sleep(2000);
                    Word2PdfJacobUtil.documents4jWordToPdf(wordPath, pdfPath);
                    return null;
                }

                @Override
                protected void done() {
                    convertButton.setEnabled(true);
                    convertButton.setText("转换为PDF");
                    JOptionPane.showMessageDialog(RealWechatPaymentConverter.this,
                            "转换完成！\nPDF文件已保存到: " + pdfPath,
                            "转换成功", JOptionPane.INFORMATION_MESSAGE);
                }
            };
            worker.execute();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(RealWechatPaymentConverter.this,
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
            paymentStatusLabel.setText("已支付");
            paymentStatusLabel.setForeground(new Color(0, 128, 0));
        } else {
            convertButton.setBackground(Color.LIGHT_GRAY);
            convertButton.setForeground(Color.BLACK);
            paymentStatusLabel.setText("未支付");
            paymentStatusLabel.setForeground(Color.RED);
        }
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(7, 193, 96)),
                "微信支付 - 每次转换需支付 ¥2.00",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("微软雅黑", Font.BOLD, 12),
                new Color(7, 193, 96)
        ));

        JPanel innerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        innerPanel.add(wechatPayButton);
        innerPanel.add(new JLabel("支付状态:"));
        innerPanel.add(paymentStatusLabel);
        innerPanel.add(countdownLabel);

        panel.add(innerPanel, BorderLayout.CENTER);
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
                new RealWechatPaymentConverter().setVisible(true);
            }
        });
    }
}
