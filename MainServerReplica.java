package com.dist.main;

import com.dist.ServerReplica.ServerReplica;

public class MainServerReplica {

	public static void main(String args[]) {

		/*ServerReplica rep1 = new ServerReplica(1);
		Thread thread1 = new Thread(rep1);
		thread1.start();*/
		ServerReplica rep1 = new ServerReplica(1);
		Thread thread1 = new Thread(rep1);
		thread1.start();
		
		
		
	}
}
