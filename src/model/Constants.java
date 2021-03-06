package model;

public class Constants {
	/**
	 * All our public static final variables.
	 */

	public static final int mainFrameMaxX = 1035;
	public static final  int mainFrameMaxY = 1000;

    public static final String[] streetNames = {"Olskroken", "Centralen", "Brunnsparken", "Hissingen", "Korsvägen", "Frölunda", "Liseberg", "Liné", "Feskekörka", "Lejonklippan"};
    public static final String[] buttonNames= {"New Session","Extra Connection"};

	public static final int maxXCoordinate = 15;
	public static final int maxYCoordinate = 15;

	public static final int nodeViewSize = (int) (mainFrameMaxX * 0.057);

	public static final String activeNodeImagePath = "L:\\RouteCity\\src\\model\\viewModel\\resources\\nodeActive.png";
	public static final String inActiveNodeImagePath = "L:\\RouteCity\\src\\model\\viewModel\\resources\\node.png";
	//public static final String activeNodeImagePath ="C:\\Users\\91matfri\\IdeaProjects\\RouteCity\\src\\model\\viewModel\\resources\\nodeActive.png";
	//public static final String inActiveNodeImagePath = "C:\\Users\\91matfri\\IdeaProjects\\RouteCity\\src\\model\\viewModel\\resources\\node.png";

}
