package com.mut8ed.battlemap.server.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mut8ed.battlemap.shared.MapObjectType;
import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;



/**
 * need a data structure that can track the location of map objects in a 3d matrix.
 * need to be able to find the location of a given object in O(1) time and also
 * find what objects exist at a location given a set of coordinates in O(1) time.
 * @author ryan
 *
 */
public class MapMatrix implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Logger logger = Logger.getLogger(MapMatrix.class);
	
	private int cellCountX;
	private int cellCountY;
	//map<level,list of elem IDs in 2d matrix>
	private Map<Integer,ArrayList<String>[/*x*/][/*y*/]> matrix = new HashMap<Integer,ArrayList<String>[/*x*/][/*y*/]>();

	public MapMatrix(){}
	
	public MapMatrix(int cellCountX, int cellCountY) {
		this.cellCountX = cellCountX;
		this.cellCountY = cellCountY;
	}

	/**
	 * 
	 * @param mow
	 * @return list of elementIds that need to be removed.
	 */
	public List<String> add(MapObjectWrapper mow){

		int x = mow.getX();
		int y = mow.getY();
		int z = mow.getZ();
		int width = mow.getWidth();
		int height = mow.getHeight();

		ArrayList<String>[][] level = getLayerMatrix(z);

		//find and remove objects that conflict in the same map cell
		//ie, remove any existing tile if adding a tile, but dont remove a figure if adding a tile.
		ArrayList<String> toRemove = new ArrayList<String>();
		if (mow.getMapObjectType()==MapObjectType.TILE || mow.getMapObjectType()==MapObjectType.FIGURE){
			
			
			for (int i=x;i<x+width;i++){
				for (int j=y;j<y+height;j++){
					for (String id : level[i][j]){
						MapObjectType type = MapObjectType.valueOf(id.split("-")[0]);
						if (type.equals(mow.getMapObjectType())){
							toRemove.add(id);
						}
					}
					synchronized(level[i][j]){
						level[i][j].removeAll(toRemove);
						level[i][j].add(mow.getMapObject().getElementId());
					}
				}
			}

		}
		return toRemove;

	}
	
	public void clear() {
		matrix.clear();
	}

	@SuppressWarnings("unchecked")
	private ArrayList<String>[][] getLayerMatrix(int z) {
		ArrayList<String>[][] layer = matrix.get(z);
		if (layer==null){
			layer = (ArrayList<String>[][])new ArrayList[cellCountX][cellCountY];
			for (int x=0;x<cellCountX;x++){
				for (int y=0;y<cellCountY;y++){
					layer[x][y] = new ArrayList<String>(); 
				}
			}
			matrix.put(z,layer);		
		}
		return layer;
	}

	public List<String> getObjectsAt(int x, int y, int z){
		return matrix.get(z)[x][y];
	}

	public void remove(MapObjectWrapper mow){
		if (mow==null)return;
		logger.debug("removing "+mow.getElementId()+" from matrix");
		int z = mow.getZ();

		if (mow.getElementId().matches("(TILE|FIGURE).*")){
			int x = mow.getX();
			int y = mow.getY();
			int width = mow.getWidth();
			int height = mow.getHeight();
			for (int i=x;i<x+width;i++){
				for (int j=y;j<y+height;j++){
					ArrayList<String> square = getLayerMatrix(z)[i][j];
					synchronized (square) {
						square.remove(mow.getElementId());	
					}
				}
			}
		}
		
	}

	public List<String> getBlockersAt(int x, int y, int z) {
		return getLayerMatrix(z)[x][y];
	}

	public void showCounts() {
		System.out.println(matrix);
		for (int layer : matrix.keySet()){
			System.out.println("layer "+layer);
			List<String>[][] m = matrix.get(layer);
			for (int i = 0; i < m.length; i++) {
				for (int j = 0; j < m[i].length; j++) {
					int size = m[i][j].size();
					if (size>1)System.out.println(i+"x"+j+":"+size);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "MapMatrix [cellCountX=" + cellCountX + ", cellCountY="
				+ cellCountY + ", matrix=" + matrix + "]";
	}

}
