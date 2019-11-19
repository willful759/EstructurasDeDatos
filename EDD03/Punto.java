/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.epromero.util.LienzoStd;
/**
 *
 * @author IvanPineda
 */
public class Punto implements Comparable<Punto>{
  private final int x;
  private final int y;

  public Punto(int x,int y){
    this.x = x;
    this.y = y;
  }

  public void dibuja(){
    LienzoStd.punto(this.x, this.y);
  }

  public void dibujaHasta(Punto p){
    LienzoStd.linea(this.x, this.y, p.x, p.y);
  }

  public double pendienteHasta(Punto p){
    double val = 0;
    if(this.x == p.x)
      val = Double.POSITIVE_INFINITY;
    else
      val = (double)(p.y - this.y)/(double)(p.x - this.x);
      //val = this.x*p.y - this.y*p.x;

    return val;
   }

  @Override
  public int compareTo(Punto p) {
    int res = 1;

    if(this.y < p.y)
      res = -1;
    else if(this.y == p.y && this.x < p.x )
      res =  -1;
    else if(this.x == p.x && this.y == p.y)
      res = 0;

    return res;
  }

  @Override
  public String toString(){
    return "(" + this.x + "," + this.y + ")";
  }
}
