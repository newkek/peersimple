package application;

import peersim.config.*;
import peersim.core.*;
import peersim.edsim.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MatrixTransport implements Protocol
{
    // Fichier contenant la matrice de latences
    private final String matrixFileName;
    private static long matrix[][];   
    
    public MatrixTransport(String prefix) {
	System.out.println("Transport Layer Enabled");
	matrixFileName = Configuration.getString(prefix + ".matrix");
	try
        {
	    BufferedReader reader = new BufferedReader(new FileReader(matrixFileName));
	    String line = null;
	    List<String> items = new ArrayList<String>();
	    StringTokenizer split;
	    while ((line = reader.readLine()) != null)
            {
		items.add(line);
	    }
	    int size = items.size();
	    System.out.println("MatrixTransport : " + size + "x" + size + " network detected");
	    matrix = new long[size][size];
	    for (int i=0; i<size; i++)
            {
		split = new StringTokenizer(items.get(i), " ");		
		for(int j=0; j<size; j++) {	
		    matrix[i][j] = Long.parseLong((String)split.nextElement());
		}
	    }
	} catch(IOException e)
        {
            System.out.println("Can't find file " + matrixFileName);
	}
    }
    
    public Object clone()
    {
	return this;
    }
    
    //envoi d'un message: il suffit de l'ajouter a la file d'evenements
    public void send(Node src, Node dest, Object msg, int pid)
    {
	long delay = getLatency(src,dest);
	EDSimulator.add(delay, msg, dest, pid);
    }
    
    
    //latence random entre la borne min et la borne max
    public long getLatency(Node src, Node dest)
    {
	return matrix[(int)src.getID()][(int)dest.getID()];
    }

    public long setLatency(Node src, Node dest, long newLatency)
    {
	long oldLatency = matrix[(int)src.getID()][(int)dest.getID()];
	matrix[(int)src.getID()][(int)dest.getID()] = newLatency;
	return oldLatency;
    }
}

