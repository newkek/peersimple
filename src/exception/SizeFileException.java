package exception;


public class SizeFileException extends Exception{ 
  public SizeFileException(){
      System.out.println("The file's size is not high enough for the simulation");
      }  
}
