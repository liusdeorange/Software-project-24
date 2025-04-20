package view.Impl;

import controller.Impl.SettingControllerImpl;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SettingUiImpl {

    private final JPanel contentPanel;
    private JTextField pathField;
    private JButton chooseButton;
    private JButton saveButton;

    public SettingUiImpl(JPanel contentPanel) {
        this.contentPanel = contentPanel;
    }

    public void SettingWindow() {
        // 清空原本的内容
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        // 创建主设置面板
        JPanel settingPanel = createMainSettingPanel();

        // 添加到 contentPanel 中
        contentPanel.add(settingPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();

        // 设置按钮事件监听
        setupButtonListeners();
    }

    private JPanel createMainSettingPanel() {
        JPanel settingPanel = new JPanel(new BorderLayout());
        settingPanel.setBorder(BorderFactory.createEmptyBorder(4, 2, 5, 5));

        // 添加文件路径设置组件
        settingPanel.add(createFilePathPanel(), BorderLayout.NORTH);

        // 这里可以添加其他设置面板
        // settingPanel.add(createOtherSettingsPanel(), BorderLayout.CENTER);

        // 添加保存按钮
        settingPanel.add(createSaveButtonPanel(), BorderLayout.SOUTH);

        return settingPanel;
    }

    private JPanel createFilePathPanel() {
        JPanel pathPanel = new JPanel(new BorderLayout());
        pathField = new JTextField(SettingControllerImpl.getFinanceFilePath());
        pathField.setEditable(false);
        chooseButton = new JButton("选择新路径");

        pathPanel.add(new JLabel("当前路径: "), BorderLayout.WEST);
        pathPanel.add(pathField, BorderLayout.CENTER);
        pathPanel.add(chooseButton, BorderLayout.EAST);

        return pathPanel;
    }

    private JPanel createSaveButtonPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("保存设置");
        bottomPanel.add(saveButton);
        return bottomPanel;
    }

    private void setupButtonListeners() {
        // 选择路径按钮逻辑
        chooseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("选择新的 finance_data.csv 文件位置");
            fileChooser.setSelectedFile(new File("finance_data.csv"));

            int result = fileChooser.showSaveDialog(contentPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                pathField.setText(selectedFile.getAbsolutePath());
            }
        });

        // 保存按钮逻辑
        saveButton.addActionListener(e -> {
            String newPath = pathField.getText();
            boolean success = SettingControllerImpl.setFinanceFilePath(newPath);
            if (success) {
                JOptionPane.showMessageDialog(contentPanel, "路径保存成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(contentPanel, "路径保存失败（可能是相同路径或权限问题）", "失败", JOptionPane.ERROR_MESSAGE);
                pathField.setText(SettingControllerImpl.getFinanceFilePath());
            }
        });
    }
}