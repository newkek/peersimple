package application;

import java.util.Random;

import peersim.edsim.*;
import peersim.core.*;
import peersim.config.*;

/*
  Module d'initialisation de helloWorld: 
Fonctionnement:
pour chaque noeud, le module fait le lien entre la couche transport et la couche applicative
ensuite, il fait envoyer au noeud 0 un message "Hello" a tous les autres noeuds
*/
public class Controler implements peersim.core.Control
{
        private int applicationPid;
        private float probaCrash;

        public Controler(String prefix)
        {
                //recuperation du pid de la couche applicative
                this.applicationPid = Configuration.getPid(prefix + ".applicationProtocolPid");
                this.probaCrash = (float)Configuration.getDouble(prefix + ".probaCrash");
        }

        public boolean execute()
        {
                int nodeNb;   //recuperation de la taille du reseau
                nodeNb = Network.size();
                
                if (this.probaCrash > CommonState.r.nextFloat())
                {
                        int nodeId = CommonState.r.nextInt(nodeNb);
                        EDSimulator.add(0, new Message(Message.DIE, "<A+ sous l'bus>", -1), Network.get(nodeId), this.applicationPid);
                }
                return false;
        }
>>>>>>> 7b017722347d1fbd30daad06ceed00c902faa23b
}
