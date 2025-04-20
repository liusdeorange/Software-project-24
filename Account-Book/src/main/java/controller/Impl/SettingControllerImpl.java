package controller.Impl;

import controller.SettingController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class SettingControllerImpl implements SettingController {
    private static final String CONFIG_FILE = "config.properties";
    private static final String FINANCE_FILE_PATH_KEY = "finance_file_path";
    private static final String DEFAULT_DATA_FILE = "finance_data.csv";
    private static String currentFinanceFilePath;

    static {
        initializeApplication(); // 替换原来的loadSettings()
    }

    /**
     * 首次运行时自动初始化所有必要文件
     */
    private static void initializeApplication() {
        // 1. 确保配置目录存在
        new File("config").mkdirs();

        // 2. 初始化配置文件
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            createDefaultConfig(configFile);
        }

        // 3. 加载配置
        loadSettings();

        // 4. 检查数据文件是否存在
        File dataFile = new File(currentFinanceFilePath);
        if (!dataFile.exists()) {
            createDefaultDataFile(dataFile);
        }
    }

    /**
     * 创建默认配置文件
     */
    private static void createDefaultConfig(File configFile) {
        try (OutputStream out = new FileOutputStream(configFile)) {
            Properties props = new Properties();
            // 设置默认数据文件路径（相对路径）
            String defaultDataPath = "data" + File.separator + DEFAULT_DATA_FILE;
            props.setProperty(FINANCE_FILE_PATH_KEY, defaultDataPath);
            props.store(out, "Auto-generated default config");

            // 确保数据目录存在
            new File("data").mkdirs();
        } catch (IOException e) {
            System.err.println("无法创建默认配置文件: " + e.getMessage());
        }
    }

    /**
     * 创建默认数据文件
     */
    private static void createDefaultDataFile(File targetFile) {
        try (InputStream in = SettingControllerImpl.class.getClassLoader()
                .getResourceAsStream(DEFAULT_DATA_FILE)) {

            if (in != null) {
                Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                // 如果资源中没有模板，创建空文件
                targetFile.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("无法创建默认数据文件: " + e.getMessage());
        }
    }

    // 从配置文件中加载设置
    private static void loadSettings() {
        Properties props = new Properties();

        // 首先尝试从外部文件加载（用于保存用户修改）
        File externalConfig = new File(CONFIG_FILE);
        if (externalConfig.exists()) {
            try (FileInputStream in = new FileInputStream(externalConfig)) {
                props.load(in);
                currentFinanceFilePath = props.getProperty(FINANCE_FILE_PATH_KEY);
            } catch (IOException e) {
                System.err.println("加载外部配置时出错: " + e.getMessage());
            }
        }

        // 如果外部配置不存在或没有设置路径，使用默认资源
        if (currentFinanceFilePath == null) {
            try (InputStream in = SettingControllerImpl.class.getClassLoader()
                    .getResourceAsStream("config.properties")) {
                if (in != null) {
                    props.load(in);
                    currentFinanceFilePath = props.getProperty(FINANCE_FILE_PATH_KEY,
                            "finance_data.csv"); // 默认资源路径
                }
            } catch (IOException e) {
                System.err.println("加载内置配置时出错: " + e.getMessage());
            }
        }

        // 如果都没有，使用硬编码默认值
        if (currentFinanceFilePath == null) {
            currentFinanceFilePath = "finance_data.csv";
        }
    }

    // 将设置保存到配置文件
    private static void saveSettings() {
        Properties props = new Properties();
        props.setProperty(FINANCE_FILE_PATH_KEY, currentFinanceFilePath);

        // 确保保存到外部文件
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            props.store(out, "应用程序设置");
        } catch (IOException e) {
            System.err.println("保存设置时出错: " + e.getMessage());
        }
    }

    public static String getFinanceFilePath() {
        return currentFinanceFilePath;
    }

    public static boolean setFinanceFilePath(String newFilePath) {
        try {
            // 获取当前文件（从资源或文件系统）
            InputStream currentInput = null;
            File currentFile = new File(currentFinanceFilePath);
            if (currentFile.exists()) {
                currentInput = new FileInputStream(currentFile);
            } else {
                currentInput = SettingControllerImpl.class.getClassLoader()
                        .getResourceAsStream(currentFinanceFilePath);
            }

            if (currentInput == null) {
                System.err.println("无法找到当前文件: " + currentFinanceFilePath);
                return false;
            }

            // 确保新路径是外部文件路径
            File newFile = new File(newFilePath).getCanonicalFile();
            File parentDir = newFile.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                System.err.println("无法创建目标文件夹：" + parentDir.getAbsolutePath());
                return false;
            }

            try (OutputStream out = new FileOutputStream(newFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = currentInput.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }

                currentFinanceFilePath = newFile.getPath();
                saveSettings();
                return true;
            }
        } catch (IOException e) {
            System.err.println("操作失败: " + e.getMessage());
            return false;
        }
    }
}