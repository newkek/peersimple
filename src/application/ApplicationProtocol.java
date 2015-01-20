package application;

import peersim.edsim.*;
import peersim.core.*;
import peersim.config.*;
import java.util.Random;

public class ApplicationProtocol implements EDProtocol
{   
        //identifiant de la couche transport
        private int transportPid;
        //objet couche transport
        private MatrixTransport transport;

        //identifiant de la couche courante (la couche applicative)
        private int mypid;
        //prefixe de la couche (nom de la variable de protocole du fichier de config)
        private String prefix;

        //le numero de noeud
        private int nodeId;

        //pour tirer des valeurs aléatoires
        private Random rand;

        //état du noeud dans l'application
        private long state;


        public ApplicationProtocol(String prefix)
        { 
                this.prefix = prefix;
                //initialisation des identifiants a partir du fichier de configuration
                this.transportPid = Configuration.getPid(prefix + ".transport");
                this.mypid = Configuration.getPid(prefix + ".myself");
                this.transport = null;
                this.state = 0;
                this.rand = new Random();
        }


        //methode appelee lorsqu'un message est recu par le protocole HelloWorld du noeud
        public void processEvent( Node node, int pid, Object event )
        {
                this.receive((Message)event);
        }


        //methode necessaire pour la creation du reseau (qui se fait par clonage d'un prototype)
        public Object clone()
        {
                ApplicationProtocol dolly = new ApplicationProtocol(this.prefix);
                return dolly;
        }


        public void init(int nodeId)
        {
                //liaison entre un objet de la couche applicative et un 
                //objet de la couche transport situes sur le meme noeud
                this.nodeId = nodeId;
                this.transport = (MatrixTransport)Network.get(this.nodeId).getProtocol(this.transportPid);

                //création des évênements d'incrémentaion de l'état du noeud et de checkpoint 
                this.addIncreaseStateEvent();
                this.addCheckpointEvent();
        }

        //broadcast, envoie d'un message à tout le monde
        public void broadcast(Message msg)
        {
                System.out.println("[t=" + CommonState.getTime() +"] " + this + " : Broadcast " + msg.getContent());
                for (int i=0; i<Network.size(); i++)
                {
                        this.transport.send(getMyNode(), Network.get(i), msg, this.mypid);
                }
        }

        //envoi d'un message (l'envoi se fait via la couche transport)
        public void send(Message msg, Node dest)
        {
                System.out.println("[t=" + CommonState.getTime() +"] " + this + " : Send " + msg.getContent());
                this.transport.send(getMyNode(), dest, msg, this.mypid);
        }


        //affichage a la reception
        private void receive(Message r_msg)
        {
                System.out.println("[t=" + CommonState.getTime() +"] " + this + " : Received " + r_msg.getContent());
                switch (r_msg.getType())
                {
                        case Message.INC_STATE:
                                
                                this.state++;
                                this.addIncreaseStateEvent(); 
                                
                                if (this.rand.nextFloat() < 0.5)
                                {
                                        Message msg = new Message(Message.APPLICATION, "<fehhfzihfiheiuhfizh>");
                                        Node dest = Network.get(this.rand.nextInt(Network.size()));
                                        this.send(msg, dest);
                                }

                                if (this.rand.nextFloat() < 0.005)
                                {
                                        Message msg = new Message(Message.APPLICATION, "<this is a broadcast>");
                                        this.broadcast(msg);
                                }
                                break;

                        case Message.CHECKPOINT:
                                
                                this.addCheckpointEvent();
                                break;

                        default:
                                break;
                }
        }

        private void addIncreaseStateEvent()
        {
                Message message = new Message(Message.INC_STATE, "<increase state>");
                int delay = this.rand.nextInt(11)+10;
                EDSimulator.add(delay, message, this.getMyNode(), this.mypid);
        }
        
        private void addCheckpointEvent()
        {
                Message message = new Message(Message.CHECKPOINT, "<checkpoint yourself>");
                int delay = this.rand.nextInt(31)+45;
                EDSimulator.add(delay, message, this.getMyNode(), this.mypid);
        }

        //retourne le noeud courant
        private Node getMyNode()
        {
                return Network.get(this.nodeId);
        }


        public String toString()
        {
                return "Node "+ this.nodeId + " : State " + this.state;
        }
}
