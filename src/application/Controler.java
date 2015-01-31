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
public class Controler implements peersim.core.Control {
    
    private int applicationPid;

    private boolean test=false;

    public Controler(String prefix) {
	//recuperation du pid de la couche applicative
	this.applicationPid = Configuration.getPid(prefix + ".applicationProtocolPid");

    }

    public boolean execute() {
		int nodeNb, node, temp;		
		Random rand = new Random();
		Node dest;
	
		//recuperation de la taille du reseau
		nodeNb = Network.size();

		//System.out.println("coucou from Controler");
		//creation du message
/*		if(test==false){
			if(rand.nextInt(100)<probaFaute){
				int delay = 0;
				if(rand.nextInt(100)<probaPas){
					delay = 1;
				}
				test=true;
				node = rand.nextInt(nodeNb);
				System.out.println("kill de "+node);
				Message message = new Message(Message.KILL,"A+ SOUS L'BUS",-1);
				EDSimulator.add(delay, message, Network.get(node), helloWorldPid);
				//Network.get(node).setFailState(Fallible.DEAD);
			}else{
				//System.out.println("pas de kill");
			}
		}
*/		
		if(CommonState.getTime() == 20){	
			//EDSimulator.add(500, Message(Message.DIE, "<A+ sous l'bus>", -1), applicationPid);
		}
		
		
		return false;
    }
}
