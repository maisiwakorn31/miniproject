package com.lab;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class minesweeper extends Application{

  private Stage stage;
    
    public static void main(String[] args) {
        Application.launch(args);
        
    }
    

    @Override
      public void start(Stage Frame) throws Exception {

        var Scene = new Scene(panel(),900,600);
        Scene.getStylesheets().add(getClass().getResource("/css/frame.css").toExternalForm());
        Frame.setScene(Scene);
        Frame.setTitle("minesweeper");
        Frame.setResizable(false);
        Frame.show();

      }

    public Region panel(){

    var frame = new AnchorPane();


        
    frame.getChildren().add(AddImage("/image/2.png"));
    frame.getChildren().add(AddImage("/image/3.png"));
    frame.getChildren().add(AddImage("/image/4.png"));

    frame.getChildren().add(createLabel("MINIPROJECT ค้าบอ้วน!!", 300, 50, 70.00, 30.00,30));
    frame.getChildren().add(createLabel("9X9 10 ทุ่นระเบิด", 300, 110, 120.00, 170.00,30));
    frame.getChildren().add(createLabel("16X16 40 ทุ่นระเบิด", 300, 110, 100.00, 320.00,30));
    frame.getChildren().add(createLabel("30X16 99 ทุ่นระเบิด", 300, 110, 90.00, 470.00,30));

    frame.getChildren().addAll(createButton("ง่าย",280,60,70.00,130.00,30,easy.class));
    frame.getChildren().addAll(createButton("ปานกลาง",280,60,70.00,270.00,30,medium.class));
    frame.getChildren().addAll(createButton("ยาก",280,60,70.00,420.00,30,hard.class));

    

    return frame;
  }

  private Node createButton(String name,int width,int hight,Double x,Double y,int size , Class<? extends Application> gameClass){
    Font customFont = Font.loadFont(getClass().getResourceAsStream("/font/ZFTERMIN__.ttf"), size);
    var Button = new Button(name);
    Button.setPrefSize(width,hight);
    Button.setOnAction(e -> launchGame(gameClass));
    AnchorPane.setLeftAnchor(Button, x); 
    AnchorPane.setTopAnchor(Button, y);
    Button.setFont(customFont);
    Button.getStyleClass().add("Button-style");
    return Button;
  }

  private Node createLabel(String word,int width,int hight,Double x,Double y,int size){
    Font customFont = Font.loadFont(getClass().getResourceAsStream("/font/ZFTERMIN__.ttf"), size);
    Label label = new Label(word);
    label.setPrefSize(width, hight);
    AnchorPane.setLeftAnchor(label, x);
    AnchorPane.setTopAnchor(label, y);
    label.setFont(customFont);
    label.setStyle("-fx-text-fill: white;");
    //label.getStyleClass().add("label-style");
    return label;
  }

  private Node AddImage(String pathfile){
    Image image = new Image(getClass().getResourceAsStream(pathfile));
    ImageView imageView = new ImageView(image);
    return imageView;
  }

  private void launchGame(Class<? extends Application> gameClass) {
    try {
        
        if (stage != null) {
          stage.close();
      }

        
        Application gameApp = gameClass.getDeclaredConstructor().newInstance();
        Stage newStage = new Stage();
        gameApp.start(newStage);

    } catch (Exception e) {
        e.printStackTrace();
    }
}




}
