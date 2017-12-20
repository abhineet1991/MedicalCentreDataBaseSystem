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
		
		
		ServerReplica rep2 = new ServerReplica(2);
		Thread thread2 = new Thread(rep2);
		thread2.start();
		
		ServerReplica rep3 = new ServerReplica(3);
		Thread thread3 = new Thread(rep3);
		thread3.start();
	}
}
