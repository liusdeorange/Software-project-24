import java.io.*;
import java.util.*;

public class Test {

    private static final String OUTPUT_FILE = "finance_data.csv";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // 确保文件存在，并为其添加表头
        createFileIfNotExists();

        // 主菜单
        while (true) {
            System.out.println("请选择操作:");
            System.out.println("1. 手动导入一条数据");
            System.out.println("2. 从CSV文件导入数据");
            System.out.println("3. 退出");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    manualImport();
                    break;
                case "2":
                    fileImport();
                    break;
                case "3":
                    System.out.println("程序退出.");
                    return;
                default:
                    System.out.println("无效输入，请重新选择.");
            }
        }
    }

    // 确保 CSV 文件存在，并在文件第一次创建时写入表头
    private static void createFileIfNotExists() {
        File file = new File(OUTPUT_FILE);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Date,Amount,Category,Description\n");
            } catch (IOException e) {
                System.out.println("无法创建 CSV 文件: " + e.getMessage());
            }
        }
    }

    // 手动导入数据，增加确认步骤
    private static void manualImport() {
        System.out.println("请输入数据:");

        System.out.print("日期 (yyyy-mm-dd): ");
        String date = scanner.nextLine();

        System.out.print("金额: ");
        double amount = Double.parseDouble(scanner.nextLine());

        System.out.print("类别: ");
        String category = scanner.nextLine();

        System.out.print("描述: ");
        String description = scanner.nextLine();

        // 显示输入的数据并要求确认
        System.out.println("\n您输入的数据是:");
        System.out.println("日期: " + date);
        System.out.println("金额: " + amount);
        System.out.println("类别: " + category);
        System.out.println("描述: " + description);

        System.out.print("是否确认导入以上数据？(y/n): ");
        String confirmation = scanner.nextLine();

        if ("y".equalsIgnoreCase(confirmation)) {
            // 将数据追加到 CSV 文件
            appendToCSV(date, amount, category, description);
            System.out.println("数据已成功导入!");
        } else {
            System.out.println("数据未导入.");
        }
    }

    // 从 CSV 文件导入数据
    private static void fileImport() {
        System.out.print("请输入要导入的CSV文件路径: ");
        String filePath = scanner.nextLine();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("文件不存在.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // 跳过文件头
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    String date = fields[0];
                    double amount = Double.parseDouble(fields[1]);
                    String category = fields[2];
                    String description = fields[3];

                    // 将每条数据追加到目标 CSV 文件
                    appendToCSV(date, amount, category, description);
                    System.out.println("导入的数据: " + date + ", " + amount + ", " + category + ", " + description);
                }
            }
            System.out.println("CSV文件中的数据已成功导入!");
        } catch (IOException e) {
            System.out.println("读取文件时发生错误: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("CSV 文件格式错误: 金额字段无法解析为数字.");
        }
    }

    // 将数据追加到 CSV 文件中
    private static void appendToCSV(String date, double amount, String category, String description) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, true))) {
            writer.write(String.format("%s,%.2f,%s,%s\n", date, amount, category, description));
        } catch (IOException e) {
            System.out.println("无法写入 CSV 文件: " + e.getMessage());
        }
    }
}


