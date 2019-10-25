package ColaDoble;


import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author IvanPineda
 * @param <T>
 */
public class ColaDoble<T> implements Iterable<T> {

  private class IteradorCola<T> implements Iterator<T>{

    ColaDoble<T> cola;
    NodoCola<T> actual;
    public IteradorCola(ColaDoble<T> cola){
      this.cola = cola;
      this.actual = (NodoCola<T>) this.cola.inicio;
    }

    @Override
    public boolean hasNext() {
      return this.actual != null;
    }

    @Override
    public T next() throws NoSuchElementException{
      T value = null;
      if(hasNext()){
        value = this.actual.item;
        this.actual= this.actual.siguiente;
      }
      return value;
    }

  }

  private class NodoCola<T>{
    T item;
    NodoCola<T> anterior;
    NodoCola<T> siguiente;

    public NodoCola(T item){
      this.item = item;
      this.anterior = null;
      this.siguiente = null;
    }
  }

  private NodoCola<T> inicio;
  private NodoCola<T> fin;
  private int tamanyo;

  public ColaDoble(){
    this.inicio = null;
    this.fin = null;
  }

  public boolean estaVacia(){return this.inicio == this.fin;}
  //yeet
  public int tamanyo(){return this.tamanyo;}

  public void insertaInicio(T item) throws NullPointerException{
    if(item == null){
      throw new NullPointerException("elemento nulo insertado en " + this);
    }
    NodoCola<T> temp = new NodoCola(item);
    if(this.inicio != null){
      this.inicio.anterior = temp;
      temp.siguiente = this.inicio;
    }else{
      this.fin = temp;
    }
    this.tamanyo++;
    this.inicio = temp;
  }

  public void insertaFinal(T item) throws NullPointerException{
    if(item == null){
      throw new NullPointerException("elemento nulo insertado en " + this);
    }
    NodoCola<T> temp = new NodoCola(item);
    if(this.fin != null){
      this.fin.siguiente = temp;
      temp.anterior = this.fin;
    }else{
      this.inicio = temp;
    }
    this.tamanyo++;
    this.fin = temp;
  }

  public T suprimeInicio() throws NoSuchElementException{
    if(this.inicio == null){
      throw new NoSuchElementException("Cola " + this + "vacia");
    }
    T res = this.inicio.item;
    if(this.inicio.siguiente == null){
      this.inicio = null;
    }else{
      this.inicio.siguiente.anterior = null;
      this.inicio = this.inicio.siguiente;
    }
    this.tamanyo--;
    return res;
  }

  public T suprimeFinal() throws NoSuchElementException {
    if(this.fin == null){
      throw new NoSuchElementException("Cola " + this + " vacia");
    }
    T res = this.fin.item;
    if(this.fin.anterior == null){
      this.fin = null;
    }else{
      this.fin.anterior.siguiente = null;
      this.fin = this.fin.anterior;
    }
    this.tamanyo--;
    return res;
  }

  @Override
  public Iterator<T> iterator(){
    return new IteradorCola(this);
  }

  @Override
  public String toString(){
    String str = "(";
    for(T item : this){
      str += item.toString() + ",";
    }
    str = str.replaceAll(",$", "");
    str += ")";
    return str;
  }

  public static void main(String[] args){
    ColaDoble<Integer> queue = new ColaDoble<>();
    System.out.println(queue);
    queue.insertaInicio(1);
    System.out.println(queue);
    queue.insertaFinal(2);
    System.out.println(queue);
    queue.insertaInicio(3);
    System.out.println(queue.tamanyo);
    System.out.println(queue);
    queue.suprimeInicio();
    queue.suprimeFinal();
    System.out.println(queue.tamanyo);
    System.out.println(queue);
  }
}