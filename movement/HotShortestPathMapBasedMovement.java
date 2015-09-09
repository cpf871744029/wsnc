/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package movement;

import java.util.ArrayList;
import java.util.List;

import movement.map.DijkstraPathFinder;
import movement.map.MapNode;
import movement.map.PointsOfInterest;
import core.Settings;
import movement.map.SimMap;

/**
 * Map based movement model that uses Dijkstra's algorithm to find shortest
 * paths between two random map nodes and Points Of Interest
 */
public class HotShortestPathMapBasedMovement extends MapBasedMovement implements 
	SwitchableMovement {
	/** the Dijkstra shortest path finder */
	private DijkstraPathFinder pathFinder;

	/** Points Of Interest handler */
	private PointsOfInterest pois;
	
	/**
	 * Creates a new movement model based on a Settings object's settings.
	 * @param settings The Settings object where the settings are read from
	 */
	public HotShortestPathMapBasedMovement(Settings settings) {
		super(settings);
		this.pathFinder = new DijkstraPathFinder(getOkMapNodeTypes());
		this.pois = new PointsOfInterest(getMap(), getOkMapNodeTypes(),
				settings, rng);
	}
	
	/**
	 * Copyconstructor.
	 * @param mbm The ShortestPathMapBasedMovement prototype to base 
	 * the new object to 
	 */
	protected HotShortestPathMapBasedMovement(HotShortestPathMapBasedMovement mbm) {
		super(mbm);
		this.pathFinder = mbm.pathFinder;
		this.pois = mbm.pois;
	}
	
	@Override
	public Path getPath() {
		MapNode to = pois.selectDestination();
		ArrayList<MapNode> hotNodes = new ArrayList<MapNode>();
		hotNodes = getMap().getHotNodes();		
		List<Path> hotPath = new ArrayList<Path>(hotNodes.size());		
		for(int i=0;i<hotNodes.size();i++){
			hotPath.add(new Path(generateSpeed()));
			List<MapNode> nodePath;
			List<MapNode> nodePath1 = pathFinder.getShortestPath(lastMapNode, hotNodes.get(i));
			List<MapNode> nodePath2 = pathFinder.getShortestPath(hotNodes.get(i),to);
			if(nodePath1.size()>=2){
				nodePath1.remove(nodePath1.size()-1);		
			}
			nodePath = nodePath1;	
			nodePath.addAll(nodePath2);
			assert nodePath.size() > 0 : "No path from " + lastMapNode + " to " +
			to + ". The simulation map isn't fully connected";				
			for (MapNode node : nodePath) { // create a Path from the shortest path
				hotPath.get(i).addWaypoint(node.getLocation());
			}
			nodePath = null;
		}
		double minDistence = Double.MAX_VALUE;
		int minIndex = -1;
		for(int i=0;i<hotPath.size();i++){
			if(hotPath.get(i).getDistence()<minDistence){
				minDistence = hotPath.get(i).getDistence();
				minIndex = i;
			}
		}
		lastMapNode = to;
		return hotPath.get(minIndex);
	}	
	
	@Override
	public HotShortestPathMapBasedMovement replicate() {
		return new HotShortestPathMapBasedMovement(this);
	}

}
