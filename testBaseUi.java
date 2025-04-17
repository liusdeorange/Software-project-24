package test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class testBaseUi  {
    private JPanel sidebar;
    private JPanel contentPanel;

    public testBaseUi() {
        JFrame frame = new JFrame("侧边栏示例");
        frame.setTitle("侧边栏示例");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        // 创建侧边栏面板
        createSidebar();

        // 创建主内容面板
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        JLabel defaultLabel = new JLabel("默认内容", SwingConstants.CENTER);
        contentPanel.add(defaultLabel, BorderLayout.CENTER);

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void createSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.LIGHT_GRAY);

        // 创建侧边栏按钮
        String[] buttonLabels = {"Account Book", "Report Forms", "Import","Setting"};
        for (String label : buttonLabels) {
            String htmlLabel = label;
            if (label.length() > 20) { // 简单判断，可根据实际情况调整
                int mid = label.length() / 2;
                htmlLabel = label.substring(0, mid) + "<br>" + label.substring(mid);
            }
            JButton button = new JButton("<html>" + htmlLabel + "</html>");
            button.setVerticalTextPosition(SwingConstants.CENTER);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setPreferredSize(new Dimension(100, 50));
            button.addActionListener(new SidebarButtonListener(label));
            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(1)); // 添加按钮间间隔
        }
            sidebar.add(Box.createVerticalGlue()); // 添加垂直胶水填充剩余空间
    }

    private class SidebarButtonListener implements ActionListener {
        private String buttonLabel;

        public SidebarButtonListener(String label) {
            this.buttonLabel = label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // 清空主内容面板
            contentPanel.removeAll();
            // 根据点击的按钮显示不同内容
            JLabel newLabel = new JLabel(buttonLabel + " 内容", SwingConstants.CENTER);
            contentPanel.add(newLabel, BorderLayout.CENTER);
            // 重新绘制主内容面板
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(testBaseUi::new);
    }
}    

