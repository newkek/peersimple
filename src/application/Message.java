package application;

import peersim.edsim.*;

public class Message
{
        public final static int APPLICATION = 0;
        public final static int INC_STATE = 1;
        public final static int CHECKPOINT = 2;
        public final static int ROLLBACK = 3;
        public final static int HEARTBEAT = 4;
        public final static int AREYOUALIVE = 5;
	public final static int HBCHECK = 6;
	public final static int DIE = 7;
	

        private int type;
        private String content;
        private int emitter;

        public Message(int type, String content, int pemitter)
        {
                this.type = type;
                this.content = content;
                this.emitter = pemitter;
        }

        public Message(int type, String content)
        {
                this.type = type;
                this.content = content;
                this.emitter= -1;
        }

        public String getContent()
        {
                return this.content;
        }

        public int getType()
        {
                return this.type;
        }

        public int getEmitter()
        {
                return this.emitter;
        }
}
