/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

/**
 *
 * @author Esraa
 */
public class Player {
    SimpleStringProperty player_name;
   private ImageView images;


    public Player( ImageView images ,String player_name) {
        this.player_name =  new SimpleStringProperty(player_name);
       // this.player_status = new  SimpleStringProperty (player_status);
      this.images=images;
      
    }

    public String getPlayer_name() {
        return player_name.get();
    }

    
      public ImageView getImages(){
            return images;
    }
    
    
    public void setImages(ImageView images)
    {
      this.images =  images;
    
    }
}