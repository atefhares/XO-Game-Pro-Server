/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Esraa
 */
public class FXMLDocumentController implements Initializable {

   //ImageView image = new ImageView("C:\\Users\\Esraa\\Desktop\\on.png");

    @FXML
    private Label label;
    @FXML
    private TableView<Player> table;
    @FXML
    private TableColumn<Player,String> images;
    @FXML
    private TableColumn<Player, String> player_name;
    public ObservableList<Player> list = FXCollections.observableArrayList();

    @FXML
    public void handleBtn1(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("STOPGAME");
    }

    public void handleBtn2(ActionEvent event) {
        label.setText("STARTGAME");
      
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       // images.setCellValueFactory(P;
       //images.setPrefWidth(50);
      
      images.setCellValueFactory(new PropertyValueFactory<>("images"));
        player_name.setCellValueFactory(new PropertyValueFactory<Player, String>("player_name"));
        
               ImageView image1 = new ImageView(new Image(this.getClass().getResourceAsStream("on.png")));
             ImageView image2 = new ImageView(new Image(this.getClass().getResourceAsStream("on.png")));
             ImageView image3 = new ImageView(new Image(this.getClass().getResourceAsStream("on.png")));
             
                ImageView image4 = new ImageView(new Image(this.getClass().getResourceAsStream("off.png")));
             ImageView image5 = new ImageView(new Image(this.getClass().getResourceAsStream("off.png")));
           
          Player p1=  new Player(image1, "esraa");
        Player p2=    new Player( image2,"shimaa");
            Player p3=new Player(image3, "atef");
              Player p4=  new Player(image4, "bassam");
        Player p5=    new Player( image5,"aboseree");
         
            list.add(p1);
            list.add(p2);
            list.add(p3);
              list.add(p4);
            list.add(p5);
          
        table.setItems(list);
    }

}
