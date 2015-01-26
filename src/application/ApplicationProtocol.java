package application;

import peersim.edsim.*;
import peersim.core.*;
import peersim.config.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.lang.Integer;


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

        //état du noeud dans l'application
        private long state;

        //probabilité d'envoyer un message après reception d'un message INC_STATE
        private double probaMessage;
        
        //probabilité de broadcaster un message après reception d'un message INC_STATE
        private double probaBroadcast;

        //nb de message envoyés
        private int nbSent[];

        //nb de messages recus
        private int nbRcvd[];
        
        //pile de checkpoints
        private Stack<Checkpoint> checkpoints;

        private boolean rollbackMode;

        private int iterCount1;
        
        private int iterCount2;


        public ApplicationProtocol(String prefix)
        { 
                this.prefix = prefix;
                //initialisation des identifiants a partir du fichier de configuration
                this.transportPid = Configuration.getPid(prefix + ".transport");
                this.mypid = Configuration.getPid(prefix + ".myself");
                this.transport = null;
                this.probaMessage = Configuration.getDouble(prefix + ".probaMessage");
                this.probaBroadcast = Configuration.getDouble(prefix + ".probaBroadcast");
                
                this.checkpoints = new Stack<Checkpoint>();
                this.state = 0;
                this.nbSent = new int[Network.size()];
                this.nbRcvd = new int[Network.size()];

                this.rollbackMode = false;
                this.iterCount1 = 0;
                this.iterCount2 = 0;
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
                this.addCheckpoint();
        }

        //broadcast, envoie d'un message à tout le monde
        public void broadcast(Message msg)
        {
                System.out.println("[t=" + CommonState.getTime() +"] " + this + " : Broadcast " + msg.getContent());
                for (int i=0; i<Network.size(); i++)
                {
                        if (i != this.nodeId)
                        {
                                this._send(msg, Network.get(i));
                        }
                }
        }

        //envoi d'un message (l'envoi se fait via la couche transport)
        public void send(Message msg, Node dest)
        {
                System.out.println("[t=" + CommonState.getTime() +"] " + this + " : Send " + msg.getContent());
                this._send(msg, dest);
        }

        private void _send(Message msg, Node dest)
        {
                this.transport.send(getMyNode(), dest, msg, this.mypid);
                this.nbSent[dest.getIndex()]++;
        }


        //affichage a la reception
        private void receive(Message r_msg)
        {
                System.out.println("[t=" + CommonState.getTime() +"] " + this + " : Received " + r_msg.getContent() + " from " + r_msg.getEmitter());
                switch (r_msg.getType())
                {
                        case Message.INC_STATE:
                                if (!rollbackMode)
                                {
                                        this.state++;
                                        this.addIncreaseStateEvent(); 

                                        if (CommonState.r.nextFloat() < probaMessage)
                                        {
                                                Message msg = new Message(Message.APPLICATION, "<fehhfzihfiheiuhfizh>", this.nodeId);
                                                Node dest = Network.get(CommonState.r.nextInt(Network.size()));
                                                this.send(msg, dest);
                                        }

                                        if (CommonState.r.nextFloat() < probaBroadcast)
                                        {
                                                Message msg = new Message(Message.APPLICATION, "<this is a broadcast>", this.nodeId);
                                                this.broadcast(msg);
                                        }
                                }
                                break;

                        case Message.CHECKPOINT:
                                if (!rollbackMode)
                                {
                                        this.addCheckpoint();
                                        this.addCheckpointEvent();
                                }
                                break;

                        case Message.APPLICATION:
                                if (!rollbackMode)
                                {
                                        this.nbRcvd[r_msg.getEmitter()]++;
                                }
                                break;

                        case Message.ROLLBACK:
                                // Si initialisation du rollback
                                if (!this.rollbackMode)
                                {
                                        this.rollbackMode = true;
                                        // on envoie a tous qu'on fait un rollback
                                        this.broadcastRollback();

                                        // Si on est fautif on restaure le dernier checkpoint
                                        if (r_msg.getEmitter() == this.nodeId)
                                        {
                                                this.restoreLastCheckpoint();
                                        }
                                        // Sinon on crée un checkpoint volatile
                                        else
                                        {
                                                this.addCheckpoint();
                                        }
                                }
                                
                                if (r_msg.getEmitter() != this.nodeId) 
                                {
                                        System.out.println("cpt 1 : " + iterCount1 + " cpt2 : "+iterCount2);
                                        this.iterCount2++;
                                        int nbRcvdNeighbour = Integer.parseInt(r_msg.getContent());
                                        //TODO findCorrectCheckpoint : verifier le nombre de message dans le temps
                                        Checkpoint tmp = this.findCheckpoint(nbRcvdNeighbour, r_msg.getEmitter());
                                        this.restoreCheckpoint(tmp);
                                        // Condition fin de boucle 2
                                        if (this.iterCount2 == Network.size() - 1)
                                        {
                                                System.out.println("fin boucle 2");
                                                this.iterCount1++;
                                                this.iterCount2 = 0;
                                                // Condition fin de boucle 1
                                                if (this.iterCount1 == Network.size() - 1)
                                                {
                                                        System.out.println("fin boucle 1");
                                                        this.iterCount1 = 1;
                                                        this.iterCount2 = 0;
                                                        this.rollbackMode = false;
                                                }
                                                else //sinon broadcast du tour de boucle 1 suivant
                                                {
                                                        this.broadcastRollback();
                                                }
                                        }
                                }
                                break;

                        default:
                                break;
                }
        }


        private void broadcastRollback()
        {
                for (int i=0; i< Network.size(); i++)
                {
                        if (i != this.nodeId)
                        {
                                Message msg = new Message(Message.ROLLBACK, ""+nbSent[i], this.nodeId);
                                this.send(msg, Network.get(i));
                        }
                }
        }


        private void addIncreaseStateEvent()
        {
                Message message = new Message(Message.INC_STATE, "<increase state>");
                int delay = CommonState.r.nextInt(11)+10;
                EDSimulator.add(delay, message, this.getMyNode(), this.mypid);
        }

        private void addCheckpointEvent()
        {
                Message message = new Message(Message.CHECKPOINT, "<checkpoint yourself>");
                int delay = CommonState.r.nextInt(31)+45;
                EDSimulator.add(delay, message, this.getMyNode(), this.mypid);
        }

        private void addCheckpoint()
        {
                Checkpoint toAdd = new Checkpoint(this.state, this.nbSent, this.nbRcvd);
                checkpoints.push(toAdd);
                System.out.println("new checkpoint added : " + toAdd);
        }

        private void restoreLastCheckpoint()
        {
                this.restoreCheckpoint(this.checkpoints.pop());
        }

        private void restoreCheckpoint(Checkpoint checkpoint)
        {
                this.state = checkpoint.getState();
                this.nbSent = checkpoint.getNbSent();
                this.nbRcvd = checkpoint.getNbRcvd();
                this.checkpoints.push(checkpoint);
                System.out.println(this.nodeId + " : next ckpt : " + checkpoint);
        }

        private Checkpoint findCheckpoint(int nbRcvdNeighbour, int neighbour)
        {
                Checkpoint tmp;

                        System.out.println(tmp);
                do
                {
                        tmp = this.checkpoints.pop();
                        System.out.println(tmp);
                }
                while(tmp.getNbRcvd(neighbour) <= nbRcvdNeighbour);
                return tmp;
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
