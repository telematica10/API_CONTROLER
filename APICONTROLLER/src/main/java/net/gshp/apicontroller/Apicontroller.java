package net.gshp.apicontroller;

public class Apicontroller {
	
	private static Apicontroller instance=new Apicontroller();
	
	private static String PATH_FOTO;
	
	private Apicontroller() {
	}
	
	public static Apicontroller getInstance(){
		if(instance==null){
			instance= new Apicontroller();
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
