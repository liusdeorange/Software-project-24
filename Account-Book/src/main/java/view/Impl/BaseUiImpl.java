package view.Impl;

import view.BaseUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class BaseUiImpl implements BaseUi {

    private JPanel sidebar;
    private JPanel contentPanel;
    private final Map<String, Runnable> viewMap = new HashMap<>();

    public void BaseWindow() {
        JFrame frame = new JFrame("AccountBook");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // 初始化映射关系
        initViewMap();

        // 创建侧边栏和主内容区
        createSidebar();

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(new JLabel("默认内容", SwingConstants.CENTER), BorderLayout.CENTER);

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /*
    private void createSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.LIGHT_GRAY);

        for (String label : viewMap.keySet()) {
            JButton button = new JButton(label);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setPreferredSize(new Dimension(100, 50));
            button.addActionListener(new SidebarButtonListener(label));
            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(5));
        }

        sidebar.add(Box.createVerticalGlue());
    }
    */

    private void createSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.LIGHT_GRAY);

        // 按钮尺寸统一设置
        Dimension buttonSize = new Dimension(120, 40);

        // 创建侧边栏按钮
        String[] buttonLabels = {"Account Book", "Report Forms", "Import", "Setting"};
        for (String label : buttonLabels) {
            String htmlLabel = label;
            if (label.length() > 20) {
                int mid = label.length() / 2;
                htmlLabel = label.substring(0, mid) + "<br>" + label.substring(mid);
            }

            JButton button = new JButton("<html>" + htmlLabel + "</html>");
            button.setAlignmentX(Component.CENTER_ALIGNMENT);

            // 设置统一的尺寸
            button.setPreferredSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.setMinimumSize(buttonSize);

            // 设置文字居中
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.CENTER);

            // 添加监听器
            button.addActionListener(new SidebarButtonListener(label));

            sidebar.add(button);
        }

        // 添加垂直胶水填充底部空间
        sidebar.add(Box.createVerticalGlue());
    }


    private void initViewMap() {
        viewMap.put("Account Book", this::AccountBookWindow);
        viewMap.put("Report Forms", this::ReportFormsWindow);
        viewMap.put("Import", this::ImportWindow);
        viewMap.put("Setting", this::SettingWindow);
    }

    private class SidebarButtonListener implements ActionListener {
        private final String buttonLabel;

        public SidebarButtonListener(String label) {
            this.buttonLabel = label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            contentPanel.removeAll();

            Runnable viewFunc = viewMap.get(buttonLabel);
            if (viewFunc != null) {
                viewFunc.run();
            } else {
                contentPanel.add(new JLabel("未知选项: " + buttonLabel, SwingConstants.CENTER), BorderLayout.CENTER);
            }

            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    // 以下是各个功能界面方法
    public void AccountBookWindow() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("这里是 Account Book 界面"));
        contentPanel.add(panel, BorderLayout.CENTER);
    }

    public void ReportFormsWindow() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("这里是 Report Forms 界面"));
        contentPanel.add(panel, BorderLayout.CENTER);
    }

    public void ImportWindow() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("这里是 Import 界面"));
        contentPanel.add(panel, BorderLayout.CENTER);
    }

    public void SettingWindow() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("这里是 Setting 界面"));
        contentPanel.add(panel, BorderLayout.CENTER);
    }
}
