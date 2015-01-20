package application;

import java.util.List;
import java.lang.Integer;

public class Checkpoint
{
        private long state;
        private int  nbSent[];
        private int  nbRecv[];

        public Checkpoint(long state, int nbSent[], int nbRecv[])
        {
                this.state = state;
                this.nbSent = nbSent;
                this.nbRecv = nbRecv;
        }

        public long getState()
        {
                return this.state;
        }

        public int[] getNbRecv()
        {
                return this.nbRecv;
        }
        
        public int[] getNbSent()
        {
                return this.nbSent;
        }

        public int getNbSent(int i)
        {
                return this.nbSent[i];
        }
        
        public int getNbRecv(int i)
        {
                return this.nbRecv[i];
        }

        public String toString()
        {
                return "state " + this.state + " : nbSent " + this.nbSent + " : nbRecv " + this.nbRecv;
        }
}

