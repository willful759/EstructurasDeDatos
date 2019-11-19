/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import edu.epromero.util.In;
import edu.epromero.util.StdOut;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
/**
 *
 * @author IvanPineda
 */

public class Rapido {

  private static class LineTree{

    private class Node{
      double tangent;
      List<Punto> line;
      Node(double tangent,Punto p){
        this.tangent = tangent;
        this.line = new ArrayList<>();
        this.line.add(p);
      }
    }

    private Node head;
    private int height;
    private LineTree left;
    private LineTree right;

    public LineTree(){
      height = 0;
      head = null;
      left = null;
      right = null;
    }
    private void rotateLeft(){
      Node tempNode = head;
      LineTree tempLeft = left;

      head = right.head;

      left = new LineTree();
      left.head = tempNode;
      left.left = tempLeft;
      left.right = right.left;
      right = right.right;

      int rightHeight = (right == null ? -1 : right.height);
      int leftHeight = (left == null ? -1 : left.height);
      height = Integer.max(rightHeight,leftHeight) + 1;
    };

    private void rotateRight(){
      Node tempNode = head;
      LineTree tempRight = right;

      head = left.head;

      right = new LineTree();
      right.head = tempNode;
      right.right = tempRight;
      right.left = left.right;
      left = left.left;

      int rightHeight = (right == null ? -1 : right.height);
      int leftHeight = (left == null ? -1 : left.height);
      height = Integer.max(rightHeight,leftHeight) + 1;
    };

    public void insert(double key, Punto value) {
      if (head == null){
        head = new Node(key,value);
      }else if(head.tangent == key){
        head.line.add(value);
      }else if(key < head.tangent){
        if(left == null){
          left = new LineTree();
        }
        left.insert(key, value);
        if(right != null){
          height = Integer.max(left.height, right.height) + 1;
        }else{
          height++;
        }
      }else{
        if(right == null){
          right = new LineTree();
        }
        right.insert(key, value);
        if(left != null){
          height = Integer.max(left.height, right.height) + 1;
        }else{
          height++;
        }
      }
      int rightHeight = (right == null ? -1 : right.height);
      int leftHeight = (left == null ? -1 : left.height);
      if(leftHeight - rightHeight > 1){
        rotateRight();
      }else if(rightHeight - leftHeight > 1){
        rotateLeft();
      }
    }
    //probly slow as heck but yeah it works
    public void traverse(BiConsumer<Double,List<Punto>> f){
      if(left != null){
        left.traverse(f);
      }
      if(head != null)
        f.accept(head.tangent,head.line);
      if(right != null){
        right.traverse(f);
      }
    }
}

  private static class ResultsTree{

    private class Node{
      double tangent;
      List<List<Punto>> lines;
      Node(double tangent,List<Punto> l){
        this.tangent = tangent;
        this.lines = new ArrayList<>();
        this.lines.add(l);
      }
    }

    private Node head;
    private int height;
    private ResultsTree left;
    private ResultsTree right;

    public ResultsTree(){
      height = 0;
      head = null;
      left = null;
      right = null;
    }

     private void rotateLeft(){
      Node tempNode = head;
      ResultsTree tempLeft = left;

      head = right.head;

      left = new ResultsTree();
      left.head = tempNode;
      left.left = tempLeft;
      left.right = right.left;
      right = right.right;

      int rightHeight = (right == null ? -1 : right.height);
      int leftHeight = (left == null ? -1 : left.height);
      height = Integer.max(rightHeight,leftHeight) + 1;
    };

    private void rotateRight(){
      Node tempNode = head;
      ResultsTree tempRight = right;

      head = left.head;

      right = new ResultsTree();
      right.head = tempNode;
      right.right = tempRight;
      right.left = left.right;
      left = left.left;

      int rightHeight = (right == null ? -1 : right.height);
      int leftHeight = (left == null ? -1 : left.height);
      height = Integer.max(rightHeight,leftHeight) + 1;
    };

    public void insert(double key, List<Punto> value) {
      if (head == null){
        head = new Node(key,value);
      }else if(head.tangent == key){
        boolean mustInsert = true;
        for(List<Punto> line : head.lines){
          Punto reference = value.get(0);
          Punto inThisLine = line.get(0);
          int acc = 0;
          if(inThisLine.compareTo(reference) == 0){
            mustInsert = false;
          }
          if(head.tangent == inThisLine.pendienteHasta(reference)){
            mustInsert = false;
          }
        }
        if(mustInsert){
          head.lines.add(value);
        }
      }else if(key < head.tangent){
        if(left == null){
          left = new ResultsTree();
        }
        left.insert(key, value);
        if(right != null){
          height = Integer.max(left.height, right.height) + 1;
        }else{
          height++;
        }
      }else{
        if(right == null){
          right = new ResultsTree();
        }
        right.insert(key, value);
        if(left != null){
          height = Integer.max(left.height, right.height) + 1;
        }else{
          height++;
        }
      }
      int rightHeight = (right == null ? -1 : right.height);
      int leftHeight = (left == null ? -1 : left.height);
      if(leftHeight - rightHeight > 1){
        rotateRight();
      }else if(rightHeight - leftHeight > 1){
        rotateLeft();
      }
    }

    public void traverse(Consumer<List<Punto>> f){
      if(left != null){
        left.traverse(f);
      }
      if(head != null)
        head.lines.forEach((line) -> f.accept(line));
      if(right != null){
        right.traverse(f);
      }
    }
}

  private static void merge(Comparable[] target,Comparable[] leftHalf, Comparable[] rightHalf) {
    int insertLeft = 0;
    int insertRight = 0;
    int insertTarget = 0;

    while(insertLeft < leftHalf.length && insertRight < rightHalf.length){
      if(leftHalf[insertLeft].compareTo(rightHalf[insertRight]) < 0)
          target[insertTarget++] = leftHalf[insertLeft++];
      else
          target[insertTarget++] = rightHalf[insertRight++];
    }

    if(insertLeft == leftHalf.length){
      for(int i = insertRight;i < rightHalf.length;++i){
        target[insertTarget++] = rightHalf[i];
      }
    }else{
      for(int i = insertLeft;i < leftHalf.length;++i){
        target[insertTarget++] = leftHalf[i];
      }
    }
  }

  private static void mergeSort(Comparable[] arr){
    if(arr.length == 0){
      return;
    }
    int size = arr.length;
    int iHalf = (int)(size/2);

    Comparable[] leftHalf = new Comparable[iHalf];
    Comparable[] rightHalf = new Comparable[size - iHalf];

    for(int i = 0; i < size;++i){
        if(i < iHalf){
          leftHalf[i] = arr[i];
        }else{
          rightHalf[i - iHalf] = arr[i];
        }
    }

    if(size == 1){
    }else{
      mergeSort(leftHalf);
      mergeSort(rightHalf);
      merge(arr,leftHalf,rightHalf);
    }
  }
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // TODO code application logic here
    In in = new In(args[0]);
    int iPoints = in.leeInt();
    Punto[] inputs = new Punto[iPoints];
    int lineNum = 0;
    while(lineNum < iPoints){
      inputs[lineNum] =
              new Punto(in.leeInt(),in.leeInt());
      lineNum++;
    }
    mergeSort(inputs);
    LineTree lineBuilder;
    ResultsTree resultsBuilder = new ResultsTree();
    List<List<Punto>> allLines = new ArrayList<>();
    for(int i = 0; i < iPoints;++i){
      lineBuilder = new LineTree();
      for(int j = 0; j < iPoints;++j){
        if(i != j){
          lineBuilder.insert(inputs[i].pendienteHasta(inputs[j]), inputs[j]);
        }
      }
      final Punto lineStart = inputs[i];
      lineBuilder.traverse(
              (key,line) -> {
                if(line.size() >= 3){
                  line.add(0,lineStart);
                  resultsBuilder.insert(key, line);
                }
              });
    }
    resultsBuilder.traverse(
            (line) -> {
              allLines.add(line);
            }
    );
    String resultString = "";
    for(List<Punto> line : allLines){
      resultString = line.stream().map((point) -> point + "->").reduce(resultString, String::concat);
      resultString = resultString.replaceAll("->$","\n");
    }
    StdOut.print(resultString);

  }

}
