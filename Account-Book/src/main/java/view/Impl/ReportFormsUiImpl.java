package view.Impl;

import controller.Impl.UserControllerImpl;
import view.ReportFormsUi;
import controller.ReportFormsController;
import controller.Impl.ReportFormsControllerImpl;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.jfree.chart.ChartPanel;

public class ReportFormsUiImpl implements ReportFormsUi {
    private JPanel contentPanel;
    private ReportFormsController controller;
    private UserControllerImpl userController;

    public ReportFormsUiImpl(JPanel contentPanel,UserControllerImpl userController) {
        this.contentPanel = contentPanel;
        this.userController = userController; // 初始化UserController
        this.controller = new ReportFormsControllerImpl(userController); // 传入UserController
    }

    public void ReportFormsWindow() {
        String path = "Account-Book/src/main/resources/finance_data.csv"; // 或从配置中获取
        List<ChartPanel> panels = controller.generateCharts();

        JPanel mainPanel = new JPanel(new GridLayout(3, 1));
        for (ChartPanel panel : panels) {
            mainPanel.add(panel);
        }

        contentPanel.add(mainPanel, BorderLayout.CENTER);
    }
}
