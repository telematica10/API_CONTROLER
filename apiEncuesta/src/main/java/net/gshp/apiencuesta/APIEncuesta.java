package net.gshp.apiencuesta;

public class APIEncuesta {
	
	private static APIEncuesta instance=new APIEncuesta(); 
	
	private static String PATH_FOTO;
	
	private APIEncuesta() {
	}
	
	public static APIEncuesta getInstance(){
		if(instance==null){
			instance= new APIEncuesta();
		}
		
		return instance;                                                                                                                                                                                                                                                                                                                       
	}

	public static String getPATH_FOTO() {
		return PATH_FOTO;
	}

	public static void setPATH_FOTO(String pATH_FOTO) {
		PATH_FOTO = pATH_FOTO;
	}
	
	
}
