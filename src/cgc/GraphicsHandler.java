package cgc;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GraphicsHandler extends Application {

    /**
     * This method creates 5 objects of tree image and put it in
     * the border pane.
     * @param imageName image name which is a string
     * @return  border Pane object with trees
     * @throws FileNotFoundException
     */
    public BorderPane getTreePane(String imageName) throws FileNotFoundException {
        BorderPane treePane = new BorderPane();

        ImageView tree1 = new ImageView(new Image(new FileInputStream(imageName)));
        tree1.setFitHeight(100);
        ImageView tree2 = new ImageView(new Image(new FileInputStream(imageName)));
        tree2.setFitHeight(100);
        ImageView tree3 = new ImageView(new Image(new FileInputStream(imageName)));
        tree3.setFitHeight(100);
        ImageView tree4 = new ImageView(new Image(new FileInputStream(imageName)));
        tree4.setFitHeight(100);
        ImageView tree5 = new ImageView(new Image(new FileInputStream(imageName)));
        tree5.setFitHeight(100);

        treePane.setTop(tree1);
        treePane.setBottom(tree2);
        treePane.setLeft(tree3);
        treePane.setRight(tree4);
        treePane.setCenter(tree5);

        BorderPane.setAlignment(tree1, Pos.CENTER);
        BorderPane.setAlignment(tree2, Pos.CENTER);
        return treePane;

    }

    /**
     * This method display the window of the program
     * @param window window of the program
     * @throws Exception
     */
    public void start(Stage window) throws Exception {


        // Creating Image object for each components in the theme park
        ImageView grassImage = new ImageView(new Image(new FileInputStream("static/img/Grass.jpg")));
        grassImage.setFitWidth(1500);

        ImageView trexImage = new ImageView(new Image(new FileInputStream("static/img/alice.gif")));
        trexImage.setFitHeight(80);
        trexImage.setFitWidth(150);

        ImageView carImage = new ImageView(new Image(new FileInputStream("static/img/sideViewCar.png")));
        carImage.setFitHeight(150);
        carImage.setFitWidth(100);


        ImageView fenceImage = new ImageView(new Image(new FileInputStream("static/img/fence.png")));
        fenceImage.setFitHeight(150);
        fenceImage.setFitWidth(350);
        fenceImage.setRotate(180.0);


        ImageView pathImage = new ImageView(new Image(new FileInputStream("static/img/circularRoad.png")));
        pathImage.setFitHeight(400);
        pathImage.setFitWidth(800);


        ImageView payStationImage = new ImageView(new Image(new FileInputStream("static/img/payStation.png")));
        payStationImage.setFitWidth(150);
        payStationImage.setFitHeight(200);


        // Trees are on the border pane
        // getTreePane method give borderPane with trees
        BorderPane longTreePane = getTreePane("static/img/longTree.png");
        BorderPane shortTreePane = getTreePane("static/img/shortTree.png");


        // All the components are added to the anchor pane. Anchor pane is
        // root of the scene.
        AnchorPane anchorPane = new AnchorPane();


        // Setting the position of nodes in the anchor pane
        AnchorPane.setRightAnchor(payStationImage, 100.0);
        AnchorPane.setBottomAnchor(payStationImage, 100.0);

        AnchorPane.setTopAnchor(longTreePane, 10.0);
        AnchorPane.setRightAnchor(longTreePane, 10.0);

        AnchorPane.setTopAnchor(shortTreePane, 30.0);
        AnchorPane.setLeftAnchor(shortTreePane, 60.0);


        AnchorPane.setLeftAnchor(trexImage, 600.0);

        AnchorPane.setLeftAnchor(pathImage, 300.0);
        AnchorPane.setTopAnchor(pathImage, 200.0);


        AnchorPane.setLeftAnchor(carImage, 630.0);
        AnchorPane.setBottomAnchor(carImage, 100.0);

        AnchorPane.setLeftAnchor(fenceImage, 540.0);


        // Adding all the components to the anchorPane
        anchorPane.getChildren().addAll(grassImage, pathImage, trexImage,
                longTreePane, shortTreePane, fenceImage, carImage, payStationImage);
        //Adding the achnorPane object to the scene
        Scene gardenScene = new Scene(anchorPane, 1320, 700);
        window.setTitle("Cretaceous Garden Controller");
        window.setScene(gardenScene);
        window.show();
    }
}
