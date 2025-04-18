import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainApp {
    private static final Color BUTTON_BG = new Color(245, 245, 245);
    private static final Color CONTENT_BG = new Color(255, 255, 255);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 主窗口配置
            JFrame frame = new JFrame("Finance Manager");
            frame.setSize(1000, 600);
            frame.setMinimumSize(new Dimension(800, 500));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            // 主容器采用BorderLayout
            JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // ================== 左侧按钮面板 ==================
            JPanel buttonPanel = createButtonPanel();
            mainPanel.add(buttonPanel, BorderLayout.WEST);

            // ================== 右侧内容区域 ==================
            CardLayout cardLayout = new CardLayout();
            JPanel contentPanel = new JPanel(cardLayout);
            contentPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

            // 主内容面板（带欢迎信息）
            JPanel mainContent = createMainContent(cardLayout, contentPanel);
            contentPanel.add(mainContent, "MAIN");

            // 四个功能子界面
            String[] panels = {"Account Book", "Report Form", "Import", "Settings"};
            for (String panelName : panels) {
                contentPanel.add(createSubPanel(panelName, cardLayout, contentPanel), panelName);
            }

            // ================== 事件绑定 ==================
            bindButtonActions(buttonPanel, cardLayout, contentPanel);

            mainPanel.add(contentPanel, BorderLayout.CENTER);
            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }

    // 创建左侧按钮面板
    private static JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));
        panel.setBackground(BUTTON_BG);

        String[] btnNames = {"Account Book", "Report Form", "Import", "Settings"};
        for (String name : btnNames) {
            JButton btn = new JButton(name);
            btn.setPreferredSize(new Dimension(120, 40));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setFocusPainted(false);
            panel.add(btn);
        }
        return panel;
    }

    // 创建主内容界面
    private static JPanel createMainContent(CardLayout layout, JPanel parent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CONTENT_BG);

        JLabel welcomeLabel = new JLabel("Financial Management System", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(60, 63, 65));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
    }

    // 创建子界面模板
    private static JPanel createSubPanel(String title, CardLayout layout, JPanel parent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CONTENT_BG);

        // 返回按钮区域
        JButton backButton = new JButton("← Back to Main");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.addActionListener(e -> layout.show(parent, "MAIN"));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(240, 240, 240));
        header.add(backButton);
        panel.add(header, BorderLayout.NORTH);

        // 内容标题
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    // 绑定按钮事件
    // 修改后的绑定按钮事件方法
    private static void bindButtonActions(JPanel buttonPanel, CardLayout layout, JPanel parent) {
        Component[] buttons = buttonPanel.getComponents();
        String[] cardNames = {"Account Book", "Report Form", "Import", "Settings"};

        for (int i = 0; i < buttons.length; i++) {
            final int index = i; // 创建final的临时变量
            ((JButton)buttons[i]).addActionListener(e -> layout.show(parent, cardNames[index]));
        }
    }

}
