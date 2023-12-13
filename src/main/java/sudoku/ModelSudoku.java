package sudoku;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.event.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ModelSudoku {
  //private VueChoixGrille vue;
  //private VueSudoku vue;
  private Grille grille;

  public Grille getGrille(){
    return grille;
  }
  // public VueSudoku getVue(){
  //   return this.vue;
  // }

  public ModelSudoku(Grille g){
    grille = g;
    //vue = new VueSudoku(this);
  }

}
