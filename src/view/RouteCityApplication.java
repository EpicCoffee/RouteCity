package view;

import controller.Utillity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import model.Constants;
import model.Node;
import model.Session;
import model.viewModel.ViewNode;

public class RouteCityApplication extends JFrame{

	private ArrayList<ViewNode> viewNodes = new ArrayList<>();
	private ArrayList<Line2D> linesBetweenNodes = new ArrayList<>();
	private ArrayList<Line2D> greenLinesBetweenNodes = new ArrayList<>();

	public RouteCityApplication() throws IOException
	{
		initializeMainFrame();
	}

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

		setVisible(true);
	}

	private void initializeButtons()
	{
	}

	private void initializeAllNodes() throws IOException
	{
		for (Node node : Session.getSession().getLoadedNodes())
		{
			ViewNode viewNode = new ViewNode(node.getStreetName(), node.getCoordinates(), node);

			for (Node nod: node.getConnectedNodes())
			{
				linesBetweenNodes.add(new Line2D.Double((node.getCoordinates().getX() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.145, (node.getCoordinates().getY() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.65, (nod.getCoordinates().getX() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.145, (nod.getCoordinates().getY() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.65));
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
						greenLinesBetweenNodes.clear();
						for (int i = 0; i < fastestPath.size() - 1; i++)
						{
							greenLinesBetweenNodes.add(new Line2D.Double((fastestPath.get(i).getCoordinates().getX() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.145, (fastestPath.get(i).getCoordinates().getY() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.65, (fastestPath.get(i +1).getCoordinates().getX() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.145, (fastestPath.get(i +1).getCoordinates().getY() * Constants.nodeViewSize) + Constants.nodeViewSize * 1.65));
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
		Graphics2D g2 = (Graphics2D) g;

		paintLinesBetweenNodes(g2);

	}

	public void paintLinesBetweenNodes(Graphics2D g2) {
		for (Line2D line : linesBetweenNodes)
		{
			g2.draw(line);
		}

		for (Line2D line : greenLinesBetweenNodes)
		{
			g2.setColor(Color.green);
			g2.draw(line);
		}
	}



}
