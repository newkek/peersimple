package application;

import java.util.List;
import java.lang.Integer;

public class Checkpoint
{
        private long state;
        private int  nbSent[];
        private int  nbRcvd[];

        public Checkpoint(long state, int nbSent[], int nbRcvd[])
        {
                this.state = state;
                this.nbSent = nbSent;
                this.nbRcvd = nbRcvd;
        }

        public Checkpoint()
        {
                this.state = -1;
        }


        public long getState()
        {
                return this.state;
        }

        public int[] getNbRcvd()
        {
                return this.nbRcvd;
        }
        
        public int[] getNbSent()
        {
                return this.nbSent;
        }

        public int getNbSent(int i)
        {
                return this.nbSent[i];
        }
        
        public int getNbRcvd(int i)
        {
                return this.nbRcvd[i];
        }

        public String toString()
        {
                String str = "state " + this.state + " : nbSent [";
                for (int i=0; i<nbSent.length; i++)
                {
                        str += this.nbSent[i]+"";
                        if (i<nbSent.length-1)
                        {
                                str += ",";
                        } 
                }
                str += "] : nbRcvd [";
                for (int i=0; i<nbRcvd.length; i++)
                {
                        str += nbRcvd[i]+"";
                        if (i<nbRcvd.length-1)
                        {
                                str += ",";
                        } 
                }
                return str + "]";
        }
}

