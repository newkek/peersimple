package application;

import peersim.edsim.*;

public class Message
{
    public final static int APPLICATION = 0;
    public final static int INC_STATE= 1;
    public final static int CHECKPOINT= 2;

    private int type;
    private String content;

    Message(int type, String content)
    {
	this.type = type;
	this.content = content;
    }

    public String getContent()
    {
	return this.content;
    }

    public int getType()
    {
	return this.type;
    }
}
