package ikrs.yuccasrv;

/**
 * In the first implementation steps I forgot to recognize secure sockets :/
 * 
 * This is just a simple socket wrapper that can containe 'normal' unsecure socket and
 * also secure SSL sockets.
 *
 *
 * @author  Ikaros Kappler
 * @date    2012-07-23
 * @version 1.0.0
 **/

import java.net.Socket;


public class SocketWrapper {


    private Socket socket;


    public SocketWrapper( Socket socket ) {
	this.socket = socket;
    }


    public Socket getSocket() {
	return this.socket;
    }


}