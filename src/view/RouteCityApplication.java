package view;

import controller.OnActionListner;
import controller.Utillity;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

import model.Constants;
import model.Node;
import model.Session;
import model.viewModel.ViewNode;

public class RouteCityApplication extends JFrame{

	private ArrayList<ViewNode> viewNodes = new ArrayList<>();

	private HashMap<Line2D, Float> linesBetweenNodesAndValue = new HashMap<>();
	private HashMap<Line2D, Float> greenLinesBetweenNodesAndValue = new HashMap<>();
	private JButton buttons[];

	public RouteCityApplication() throws IOException
	{
		initializeMainFrame();
	}
	/**
	 * starts up out view and inialize all the objects.
	 */
	private void initializeMainFrame() throws IOException
	{
		setTitle("Route City");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Constants.mainFrameMaxX, Constants.mainFrameMaxY);
		setLayout(null);
		setResizable(false);

		setLocationRelativeTo(null);
		initializeAllNodes();
		initializeButtons();
		initializeButtonsActionListners();

		setVisible(true);
	}
	/**
	 * creates all the buttons.
	 */
	private void initializeButtons() throws IOException {
		buttons = new JButton[2];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton(Constants.buttonNames[i]);
			buttons[i].setBounds(0,70*i,135,30);
			add(buttons[i]);
		}
	}
	/**
	 * adds actionListeners to the buttons using lamda expression.
	 */
	private void initializeButtonsActionListners(){
		buttons[0].addActionListener(actionEvent -> {
			try {
				OnActionListner.resetAllNodes();
				repaint();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		buttons[1].addActionListener(actionEvent -> {
			try {
				OnActionListner.AddExtraConnectionAtRandomNode();
				repaint();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}
	/**
	 * clears all of the data strukcturs and removes ViewNodes from the frame.
	 */
	public void resetView() {
		for (ViewNode node : viewNodes) {
			remove(node);
		}
		linesBetweenNodesAndValue.clear();
		greenLinesBetweenNodesAndValue.clear();
		viewNodes.clear();
	}
	/**
	 * 
	 */
	public void initializeAllNodes() throws IOException
	{
		for (Node node : Session.getSession().getLoadedNodes())
		{
			ViewNode viewNode = new ViewNode(node.getStreetName(), node.getCoordinates(), node);

			for (Node nod: node.getConnectedNodes())
			{
				linesBetweenNodesAndValue.put(new Line2D.Double((node.getCoordinates().getX() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.145, (node.getCoordinates().getY() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.65, (nod.getCoordinates().getX() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.145, (nod.getCoordinates().getY() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.65), Utillity.getFlightPathDistanceTest(node.getCoordinates(), nod.getCoordinates()));
			}


			viewNode.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					if (e.getButton() == 1)
					{
						System.out.println("You clicked node " + node.getStreetName());
						Session.getSession().setSelectedNode(node);
						viewNode.switchImage();

						for (ViewNode viewNode : viewNodes)
						{
							if (Session.getSession().getSelectedStartNode() == null || Session.getSession().getSelectedEndNode() == null)
							{
								break;
							}
							if (!Session.getSession().getSelectedStartNode().equals(viewNode.getNode()) && !Session.getSession().getSelectedEndNode().equals(viewNode.getNode()) && viewNode.isActive())
							{
								viewNode.switchImage();
							}
						}
					}
					if (Session.getSession().getSelectedStartNode() != null && Session.getSession().getSelectedEndNode() != null)
					{
						ArrayList<Node> fastestPath = Utillity.djikstrasGetShortestPath(Session.getSession().getSelectedStartNode(), Session.getSession().getSelectedEndNode());
						greenLinesBetweenNodesAndValue.clear();
						for (int i = 0; i < fastestPath.size() - 1; i++)
						{
							greenLinesBetweenNodesAndValue.put(new Line2D.Double((fastestPath.get(i).getCoordinates().getX() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.145, (fastestPath.get(i).getCoordinates().getY() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.65, (fastestPath.get(i +1).getCoordinates().getX() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.145, (fastestPath.get(i +1).getCoordinates().getY() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.65), Utillity.getFlightPathDistanceTest(fastestPath.get(i).getCoordinates(), fastestPath.get(i +1).getCoordinates()));
						}

						repaint();
					}

				}
			});

			viewNodes.add(viewNode);
			add(viewNode);
		}
	}



	public void paint(Graphics g) {
		super.paint(g);


		paintLinesBetweenNodes(g);

	}


	public void paintLinesBetweenNodes(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 12);
		g2.setFont(font);

			linesBetweenNodesAndValue.forEach((k,v)-> {
				g2.draw(k);


				float x = (float) ((k.getX1() + k.getX2()) / 2) ;
				float y = (float) ((k.getY1() +  k.getY2()) / 2) ;

				String vWithTwoDecimals = String.format("%.02f", v);

				g2.drawString(vWithTwoDecimals, x, y);
		});

		greenLinesBetweenNodesAndValue.forEach((k,v)-> {
			g2.setColor(Color.GREEN);
			g2.draw(k);

			g2.setColor(Color.RED);

			float x = (float) ((k.getX1() + k.getX2()) / 2) ;
			float y = (float) ((k.getY1() +  k.getY2()) / 2) ;

			String vWithTwoDecimals = String.format("%.02f", v);
			
			g2.drawString(vWithTwoDecimals, x, y);
		});

	}



}
