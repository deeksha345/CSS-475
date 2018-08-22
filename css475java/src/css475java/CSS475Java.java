package css475java;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import com.sun.javafx.webkit.WebConsoleListener;
import netscape.javascript.JSObject;

/*  
 * TEAM 04 
 * TEAM MEMBERS: DYLAN DESMOND, DEEKSHA SHARMA 
 * 
 * This is a sample Java client-only application that uses a HTML interface to interact
 * with a Microsoft Access database. The database is assumed to be relative to the root directory
 * of the project.
 *  
 * This application uses special JDBC database driver (Ucanaccess) rather than the original jdbc:odbc
 * driver that was removed in JRE 1.8. The libraries are included in the zip of this project.
 * 
 * This application uses a special interface class (JSInterface) to allow JavaScript to call Java code.
 * This interface as been stubbed to the browser / JavaScript object "app". In the JavaScript code, you will
 * see references to calls such as app.getCustomerList() that calls method on this object. This class file is
 * the main file that you will need to modify in order to add database functionality to the HTML interface.
 * 
 * Note that application starts with the "index.html" home page. All pages are relative to this page and
 * navigated to by simple hyperlinks within each webpage.
 * 
 * Note that application is not meant to be a full-fledge application and properly implement all aspects of
 * what might be expected in new application, but rather is meant to show how to make JDBC database calls using
 * SQL. A new application might make use of MVC (Model-View-Controller) architecture.
 * 
 */
public class CSS475Java extends Application {
    private Scene scene;

	@Override
	public void start(Stage stage) throws Exception {
        // create the scene
        stage.setTitle("Web View");
        scene = new Scene(new Browser(),750,500, Color.web("#666970"));
        stage.setScene(scene);
        stage.show();
	}

    public static void main(String[] args){
        launch(args);
    }
}


class Browser extends Region {
	 
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
     
    public Browser() {

    	// load the web page
        String home = CSS475Java.class.getResource("index.html").toExternalForm();
        webEngine.load(home);

        //add the web view to the scene
        getChildren().add(browser);

        // process page loading
        webEngine.getLoadWorker().stateProperty().addListener(
            new ChangeListener<State>() {
                @Override
                public void changed(ObservableValue<? extends State> ov,
                    State oldState, State newState) {
                    if (newState == State.SUCCEEDED) {
                        JSObject win = 
                            (JSObject) webEngine.executeScript("window");
                        win.setMember("app", new JSInterface());
                        }
                    }
                }
        );
        
        WebConsoleListener.setDefaultListener(new WebConsoleListener(){
            @Override
            public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
                System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message);
            }
        });
        
    }
    
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
 
    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }
 
    @Override protected double computePrefWidth(double height) {
        return 750;
    }
 
    @Override protected double computePrefHeight(double width) {
        return 500;
    }
}
