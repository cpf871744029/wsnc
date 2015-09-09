/* 
  * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package routing;

import core.Connection;
import core.Message;
import core.Settings;

/**
 * Epidemic message router with drop-oldest buffer and only single transferring
 * connections at a time.
 */
public class EpidemicRouter extends ActiveRouter {
	
	/**
	 * Constructor. Creates a new message router based on the settings in
	 * the given Settings object.
	 * @param s The settings object
	 */
	public EpidemicRouter(Settings s) {
		super(s);
		//TODO: read&use epidemic router specific settings (if any)
	}
	
	/**
	 * Copy constructor.
	 * @param r The router prototype where setting values are copied from
	 */
	protected EpidemicRouter(EpidemicRouter r) {
		super(r);
		//TODO: copy epidemic settings here (if any)
	}
			
	@Override
	public void update() {

		super.update();
		if (isTransferring() || !canStartTransfer()) {
			return; // transferring, don't try other connections yet
		}
		if(this.getHost().toString().charAt(0)=='F'){
			return;
		}
		// Try first the messages that can be delivered to final recipient
		if (exchangeDeliverableMessages() != null) {
			return; // started a transfer, don't try others (yet)
		}
		if(this.getHost().toString().charAt(0)=='W'){
			return;
		}
		// then try any/all message to any/all connection
		this.tryAllMessagesToAllConnections();
	}
	
	@Override
	protected void transferDone(Connection con) {
		Message m = con.getMessage();
		
		if (m == null) {
			core.Debug.p("Null message for con " + con);
			return;
		}
		
		/* was the message delivered to the final recipient? */
		if ((this.getMessage(m.getId())!=null)&&(m.getTo() == con.getOtherNode(getHost()) || ('W'==con.getOtherNode(getHost()).toString().charAt(0)))) { 
			this.deleteMessage(m.getId(), false);
		}
	}
	@Override
	public EpidemicRouter replicate() {
		return new EpidemicRouter(this);
	}

}