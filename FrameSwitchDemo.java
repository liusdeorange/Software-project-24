import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FrameSwitchDemo {
    public static void main(String[] args) {
        // 使用事件调度线程确保线程安全
        SwingUtilities.invokeLater(() -> {
            // 主窗口设置（Main Frame）
            JFrame mainFrame = new JFrame("界面切换演示");
            mainFrame.setSize(500, 400);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLocationRelativeTo(null); // 窗口居中

            // 创建卡片布局容器（CardLayout Container）
            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);

            // 创建第一个面板（Panel 1）
            JPanel panel1 = new JPanel(new BorderLayout());
            JButton button1 = new JButton("进入界面二 >>");
            panel1.add(new JLabel("欢迎来到主界面！", JLabel.CENTER), BorderLayout.CENTER);
            panel1.add(button1, BorderLayout.SOUTH);

            // 创建第二个面板（Panel 2）
            JPanel panel2 = new JPanel(new BorderLayout());
            JButton button2 = new JButton("<< 返回主界面");
            panel2.add(new JLabel("这里是第二个界面", JLabel.CENTER), BorderLayout.CENTER);
            panel2.add(button2, BorderLayout.SOUTH);

            // 创建第3个面板（Panel 3）
            JPanel panel3 = new JPanel(new BorderLayout());
            JButton button3 = new JButton("进入界面三 >>");
            JButton button4 = new JButton("<< 返回主界面");
            panel3.add(new JLabel("这是第三个界面", JLabel.CENTER), BorderLayout.CENTER);
            panel1.add(button3, BorderLayout.NORTH);
            panel3.add(button4, BorderLayout.SOUTH);

            // 将面板添加到卡片容器（Add panels to card container）
            cardPanel.add(panel1, "PANEL_1");
            cardPanel.add(panel2, "PANEL_2");
            cardPanel.add(panel3, "PANEL_3");

            // 按钮事件绑定（Button event binding）
            button1.addActionListener(e -> cardLayout.show(cardPanel, "PANEL_2"));
            button2.addActionListener(e -> cardLayout.show(cardPanel, "PANEL_1"));
            button3.addActionListener(e -> cardLayout.show(cardPanel, "PANEL_3"));
            button4.addActionListener(e -> cardLayout.show(cardPanel, "PANEL_1"));

            // 将卡片面板添加到主窗口（Add card panel to main frame）
            mainFrame.add(cardPanel);
            mainFrame.setVisible(true);
        });
    }
}
