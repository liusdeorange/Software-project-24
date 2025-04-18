import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MultiWindowApp {
    private static JFrame mainFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createMainFrame();
        });
    }

    // 创建主界面
    private static void createMainFrame() {
        mainFrame = new JFrame("主界面");
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        String[] btnNames = {"Account Book", "Report Form", "Import", "Settings"};
        for (String name : btnNames) {
            JButton btn = createMenuButton(name);
            btn.addActionListener(e -> {
                mainFrame.setVisible(false);
                createSubFrame(name);
            });
            buttonPanel.add(btn);
        }

        mainFrame.add(buttonPanel);
        mainFrame.setVisible(true);
    }

    // 创建子界面
    private static void createSubFrame(String title) {
        JFrame subFrame = new JFrame(title);
        subFrame.setSize(mainFrame.getSize());
        subFrame.setLocationRelativeTo(mainFrame);
        subFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 返回按钮区域
        JButton backBtn = new JButton("← 返回主界面");
        backBtn.addActionListener(e -> {
            subFrame.dispose();
            mainFrame.setVisible(true);
        });

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.add(backBtn);

        // 内容区域
        JPanel content = new JPanel(new BorderLayout());
        content.add(new JLabel(title + " 界面内容区域", JLabel.CENTER));

        subFrame.add(header, BorderLayout.NORTH);
        subFrame.add(content, BorderLayout.CENTER);

        // 窗口关闭监听
        subFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                mainFrame.setVisible(true);
            }
        });

        subFrame.setVisible(true);
    }

    // 创建统一风格的菜单按钮
    private static JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(180, 60));
        btn.setFont(new Font("微软雅黑", Font.BOLD, 16));
        btn.setBackground(new Color(240, 240, 240));
        btn.setFocusPainted(false);
        return btn;
    }
}
