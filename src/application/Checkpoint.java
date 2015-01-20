package application;

import java.util.List;
import java.lang.Integer;

public class Checkpoint
{
        private int state;
        private List<Integer> nbSent;
        private List<Integer> nbRecv;

        public Checkpoint(int state, List<Integer> nbSent, List<Integer>nbRecv)
        {
                this.state = state;
                this.nbSent = nbSent;
                this.nbRecv = nbRecv;
        }

        public int getState()
        {
                return this.state;
        }

        public List<Integer> getNbRecv()
        {
                return this.nbRecv;
        }
        
        public List<Integer> getNbSent()
        {
                return this.nbSent;
        }

        public int getNbSent(int i)
        {
                return this.nbSent.get(i);
        }
        
        public int getNbRecv(int i)
        {
                return this.nbRecv.get(i);
        }

        public String toString()
        {
                return "state " + this.state + " : nbSent " + this.nbSent + " : nbRecv " + this.nbRecv;
        }
}

