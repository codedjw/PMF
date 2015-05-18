package org.pmf.graph.socialnetwork;

public class SocialNetworkFactory {
	
	private SocialNetworkFactory() {
		
	}
	
	public static SocialNetwork newSocialNetwork(String label) {
		return new SocialNetworkImpl(label);
	}

}
