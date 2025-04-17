package view.Impl;


import javafx.application.Application;
import javafx.stage.Stage;
import view.BaseUi;

public class BaseUiImpl extends Application implements BaseUi {
    public void BaseWindow(){

    }

    public void AccountBookWindow(){

    }

    public void ReportFormsWindow(){

    }

    public void ImportWindow(){

    }

    public void SettingWindow(){

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Account Book");
        stage.show();
    }
}
