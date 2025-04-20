package view.Impl;

import controller.Impl.AccountBookControllerImpl;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map;

public class AccountBookUiImpl {
    private JPanel contentPanel;
    private JScrollPane resultScrollPane;
    private JPanel resultPanel;
    private AccountBookControllerImpl controller;
    // 添加总额显示面板
    private JPanel totalPanel;
    private JLabel totalLabel;

    // ⚡ 声明为成员变量
    private JFormattedTextField startDateField;
    private JFormattedTextField endDateField;

    public AccountBookUiImpl(JPanel contentPanel) {
        this.contentPanel = contentPanel;
        this.controller = new AccountBookControllerImpl();
        initializeDateFormat();
    }

    public void AccountBookWindow() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel controlPanel = createControlPanel();
        initializeResultPanel();

        contentPanel.add(controlPanel, BorderLayout.NORTH);
        contentPanel.add(resultScrollPane, BorderLayout.CENTER);

        // ⚡ 调整初始化顺序
        autoLoadInitialData();
        contentPanel.revalidate();
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // ⚡ 使用带默认值的创建方法
        startDateField = createDateFieldWithDefault(150,
                controller.getDefaultDateRange()[0]);
        endDateField = createDateFieldWithDefault(150,
                controller.getDefaultDateRange()[1]);

        JButton searchButton = new JButton("Search");

        addComponent(panel, new JLabel("Start Date:"), gbc, 0, 0);
        addComponent(panel, startDateField, gbc, 1, 0);
        addComponent(panel, new JLabel("End Date:"), gbc, 2, 0);
        addComponent(panel, endDateField, gbc, 3, 0);
        addComponent(panel, searchButton, gbc, 4, 0);

        searchButton.addActionListener(e -> handleSearch(
                startDateField.getText().trim(),
                endDateField.getText().trim()
        ));

        totalPanel = new JPanel();
        totalLabel = new JLabel("本月消费总额：--");
        totalPanel.add(totalLabel);
        totalPanel.setBackground(new Color(240, 240, 240));

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(panel, BorderLayout.NORTH);
        containerPanel.add(totalPanel, BorderLayout.CENTER);

        return containerPanel;
    }

    // ⚡ 增强的日期输入框创建
    private JFormattedTextField createDateFieldWithDefault(int width, String defaultValue) {
        JFormattedTextField field = new JFormattedTextField(
                new SafeDateFormatter(controller.getDateFormat()));

        configureTextField(field, width);
        try {
            field.setValue(controller.getDateFormat().parse(defaultValue));
        } catch (ParseException e) {
            field.setValue(new Date());
        }
        return field;
    }

    private static class SafeDateFormatter extends DateFormatter {
        public SafeDateFormatter(SimpleDateFormat format) {
            super(format);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value == null) {
                return this.getFormat().format(new Date());
            }
            if (!(value instanceof Date)) {
                throw new ParseException("Invalid date type: " +
                        value.getClass().getName(), 0);
            }
            return super.valueToString(value);
        }
    }

    private void autoLoadInitialData() {
        try {
            // ⚡ 空值安全处理
            Date startDate = Optional.ofNullable(controller.getFirstDayOfMonth())
                    .orElse(new Date());
            Date endDate = new Date();

            startDateField.setValue(startDate);
            endDateField.setValue(endDate);

            handleSearch(
                    controller.formatDate(startDate),
                    controller.formatDate(endDate)
            );
        } catch (IllegalArgumentException e) {
            showError("自动加载失败: " + e.getMessage());
        }
    }

    private void handleSearch(String startInput, String endInput) {
        try {
            if (startInput.isEmpty() || endInput.isEmpty()) {
                throw new ParseException("日期不能为空", 0);
            }

            Map<Date, List<AccountBookControllerImpl.Record>> filteredData =
                    controller.searchRecords(startInput, endInput);
            updateResultPanel(filteredData);
        } catch (IOException ex) {
            showError("<html><b>文件读取失败：</b>" + ex.getMessage()
                    + "<br>请检查文件路径：</html>");
        } catch (RuntimeException ex) { // 捕获新增的运行时异常
            showError("<html><b>数据解析错误：</b>"
                    + ex.getCause().getMessage() + "</html>");
        } catch (Exception ex) {
            showError("<html><b>搜索错误：</b>"
                    + ex.getMessage().replace("\n", "<br>") + "</html>");
        }
    }


    // 其余保持不变的方法
    private void initializeResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultScrollPane = new JScrollPane(resultPanel);
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    private void configureTextField(JFormattedTextField field, int width) {
        field.setColumns(10);
        field.setPreferredSize(new Dimension(width, 30));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setToolTipText("<html>格式：<b>YYYY-MM-DD</b><br>示例：2025-04-18</html>");
    }

    private void addComponent(JPanel panel, Component comp,
                              GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(comp, gbc);
    }

    private void updateResultPanel(Map<Date, List<AccountBookControllerImpl.Record>> data) {
        resultPanel.removeAll();
        if (data.isEmpty()) {
            resultPanel.add(new JLabel("未找到相关记录"));
        } else {
            data.forEach((date, records) ->
                    resultPanel.add(createDatePanel(date, records)));
        }
        // 计算总金额
        double total = data.values().stream()
                .flatMap(List::stream)
                .mapToDouble(AccountBookControllerImpl.Record::amount)
                .sum();

        updateTotalDisplay(total);  // 更新总额显示

        resultPanel.revalidate();
        resultScrollPane.repaint();
    }

    // 添加总额更新方法
    private void updateTotalDisplay(double total) {
        String formattedTotal = String.format("¥%.2f", total);
        String htmlText = "<html><b style='color:#D32F2F; font-size:14px;'>"
                + "消费总额：</b><span style='font-size:16px;'>%s</span></html>";
        totalLabel.setText(String.format(htmlText, formattedTotal));

        // 添加动态效果
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(224, 224, 224)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private JPanel createDatePanel(Date date, List<AccountBookControllerImpl.Record> records) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        // ================= 动态尺寸计算 =================
        int rowHeight = 28; // 行高（根据字体大小可调节）
        int headerHeight = 30; // 表头高度
        int maxVisibleRows = 8; // 最大可见行数

        // 动态计算理想高度
        int idealHeight = headerHeight + (Math.min(records.size(), maxVisibleRows) * rowHeight);
        int minHeight = 80; // 最小面板高度

        // ================= 智能表格创建 =================
        JTable table = createSmartTable(records);
        JScrollPane scrollPane = new JScrollPane(table);

        // 滚动条策略配置（根据行数动态调整）
        if (records.size() > maxVisibleRows) {
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            idealHeight = headerHeight + (maxVisibleRows * rowHeight) + 5; // 包含滚动条补偿
        } else {
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }

        // ================= 布局优化 =================
        scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(), Math.max(idealHeight, minHeight)));
        panel.add(scrollPane, BorderLayout.CENTER);

        // ================= 智能边框 =================
        String title = String.format("%s (%d 条)", controller.formatDate(date), records.size());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("微软雅黑", Font.BOLD, 12),
                new Color(100, 100, 100)
        ));

        // ================= 总计显示优化 =================
        JLabel totalLabel = createTotalLabel(records);
        panel.add(totalLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JTable createSmartTable(List<AccountBookControllerImpl.Record> records) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"金额 (Amount)", "分类 (Category)", "描述 (Description)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);

        // ================= 列宽自适应配置 =================
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 关闭自动调整
        TableColumnModel colModel = table.getColumnModel();

        // 金额列（固定宽度）
        TableColumn amountCol = colModel.getColumn(0);
        amountCol.setPreferredWidth(90);
        amountCol.setMaxWidth(120);

        // 分类列（弹性宽度）
        TableColumn categoryCol = colModel.getColumn(1);
        categoryCol.setPreferredWidth(120);
        categoryCol.setMinWidth(100);

        // 描述列（最大弹性宽度）
        TableColumn descCol = colModel.getColumn(2);
        descCol.setPreferredWidth(300);
        descCol.setMinWidth(150);

        // ================= 视觉优化 =================
        table.setRowHeight(28);
        table.setShowVerticalLines(true);
        table.setGridColor(new Color(220, 220, 220));
        table.getTableHeader().setBackground(new Color(245, 245, 245));

        DefaultTableCellRenderer wrapRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JTextArea textArea = new JTextArea(value != null ? value.toString() : "");
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true); // 按单词换行
                textArea.setFont(table.getFont());
                textArea.setBorder(UIManager.getBorder("Table.cellBorder"));

                // 动态计算最佳高度
                int width = table.getColumnModel().getColumn(column).getWidth();
                textArea.setSize(new Dimension(width, Integer.MAX_VALUE));
                int preferredHeight = textArea.getPreferredSize().height + 4; // 加边距

                // 设置行高（但不超过120px）
                if (table.getRowHeight(row) != Math.min(preferredHeight, 120)) {
                    table.setRowHeight(row, Math.min(preferredHeight, 120));
                }
                return textArea;
            }
        };
        // 应用渲染器到描述列
        descCol.setCellRenderer(wrapRenderer);

        // ================= 调整自动调整策略 =================
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // 启用自动调整
        table.doLayout(); // 立即应用布局

        // 数据填充
        records.forEach(r -> model.addRow(new Object[]{
                String.format("¥%.2f", r.amount()),
                r.category(),
                r.description()
        }));

        return table;

    }

    private JLabel createTotalLabel(List<AccountBookControllerImpl.Record> records) {
        double total = records.stream()
                .mapToDouble(AccountBookControllerImpl.Record::amount)
                .sum();

        JLabel label = new JLabel("当日总计: " + String.format("¥%.2f", total));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(3, 5, 3, 10)
        ));
        label.setFont(label.getFont().deriveFont(Font.BOLD, 12f));
        return label;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(contentPanel, message,
                "系统提示", JOptionPane.ERROR_MESSAGE);
    }

    private void initializeDateFormat() {
        controller.initializeDateFormat();
    }
}
