package com.anbot.server.maptiler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.anbot.server.maptiler.bean.MapTilerResult;
import com.anbot.server.maptiler.exception.CreateFolderException;
import com.anbot.server.maptiler.exception.ImageWriteException;
import com.anbot.server.maptiler.pgmtool.PGMToPNG;

/**
 * @author suwan
 *
 */
public class MapTiler {
	private static int TILE_SIZE = 256;
	
	public static MapTilerResult main(String[] args){
		if(args!=null && args.length==2)
			return tilerMap(args[0], args[1]);
		else
			return null;
	}
	
	/**
	 * 切图入口函数
	 * @param srcMapPath
	 * @param tileredMapPath
	 * @return
	 */
	public static MapTilerResult tilerMap(String srcMapPath, String tileredMapPath){
        try {
        	String suffix = null;
        	BufferedImage srcImg;
        	if(srcMapPath.indexOf(".") > -1)
                suffix = srcMapPath.substring(srcMapPath.lastIndexOf(".") + 1);
        	else 
        		return null;
            
        	if(suffix.equalsIgnoreCase("pgm"))
        		srcImg = new PGMToPNG().pgmToPng(srcMapPath);
        	else
        		srcImg = ImageIO.read(new File(srcMapPath));
			Vector<Vector<Vector<BufferedImage>>> tileredMaps = new Vector<Vector<Vector<BufferedImage>>>();
			int srcWidth = srcImg.getWidth(); 
	        int srcHeight = srcImg.getHeight(); 

			while(true){
				BufferedImage extendImg = extendImage(srcImg);
				tileredMaps.add(cutMap(extendImg));
				if(extendImg.getWidth() == TILE_SIZE && extendImg.getHeight() == TILE_SIZE)
					break;
				Image image = extendImg.getScaledInstance(extendImg.getWidth() / 2, extendImg.getHeight() / 2, Image.SCALE_SMOOTH);
				srcImg = new BufferedImage(extendImg.getWidth() / 2, extendImg.getHeight() / 2, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = srcImg.createGraphics();
	            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
	            g.dispose();
			}
			storeTileredMaps(tileredMaps, tileredMapPath);
			MapTilerResult result = new MapTilerResult(srcWidth,srcHeight,tileredMaps.size()-1,0);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 创建文件夹
	 * @param folderPath
	 * @return
	 * @throws CreateFolderException
	 */
	private static void createFolder(String folderPath) throws CreateFolderException{
		File dir = new File(folderPath);
		if(dir.exists()) {
			throw new CreateFolderException("目录已存在！");
		} else {
			dir.mkdirs();
		}
	}
	
	/**
	 * 扩展原图成256的整数倍
	 * @param srcImage
	 * @return
	 */
	private static BufferedImage extendImage(BufferedImage srcImage){
		int height = srcImage.getHeight();
	    int width = srcImage.getWidth();
	    int extRows = TILE_SIZE - (height % TILE_SIZE);
	    int extCols = TILE_SIZE - (width % TILE_SIZE);
	    
	    if (extRows == TILE_SIZE) 
	    	extRows = 0; 
	    
	    if (extCols == TILE_SIZE)
	    	extCols = 0; 
	    
	    Graphics2D g = srcImage.createGraphics();
	    BufferedImage extendImage = g.getDeviceConfiguration().createCompatibleImage(width+extCols, height+extRows, Transparency.TRANSLUCENT);
	    g.dispose();
	    g = extendImage.createGraphics();
	    //g.setColor(new Color(205, 205, 205));
	    g.setColor(new Color(0xdc, 0xdc, 0xdc));
	    g.fillRect(0, 0, width+extCols, height+extRows);
	    g.drawImage(srcImage, 0, 0, null);
	    g.dispose();
	    
		return extendImage;
	}
	
	/**
	 * 对图片进行256*256切片
	 * @param srcImage
	 * @return
	 */
	private static Vector<Vector<BufferedImage>> cutMap(BufferedImage srcImage){
		Vector<Vector<BufferedImage>> tileredMap = new Vector<Vector<BufferedImage>>();
		int height = srcImage.getHeight();   // 获取图像像素高度
	    int width = srcImage.getWidth();    // 获取图像像素宽度

	    // 计算切割后子片的像素长宽比
	    int h = height / TILE_SIZE;
	    int w = width / TILE_SIZE;
	    
	    Image image = srcImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    
	    for(int i = 0; i < w; i++) {
	    	Vector<BufferedImage> tilered_map_vec = new Vector<BufferedImage>();
	    	for(int j = 0; j < h; j++) {
	    		ImageFilter cropfilter = new CropImageFilter(TILE_SIZE * i, TILE_SIZE * j, TILE_SIZE, TILE_SIZE);
	    		Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(),cropfilter));
	    		BufferedImage tag = new BufferedImage(TILE_SIZE, TILE_SIZE , BufferedImage.TYPE_INT_RGB);
	    		Graphics2D gs = tag.createGraphics();
	    		gs.drawImage(img, 0, 0, null);
	    		gs.dispose();
	    		tilered_map_vec.add(tag);
	    	}
	    	tileredMap.add(tilered_map_vec);
	    }
	    
		return tileredMap;
	}
	
	/**
	 * 存储切片地图
	 * @param tileredMaps
	 * @param path
	 * @throws CreateFolderException
	 * @throws ImageWriteException
	 */
	private static void storeTileredMaps(Vector<Vector<Vector<BufferedImage>>> tileredMaps, String path) throws CreateFolderException, ImageWriteException{
		createFolder(path);
		int layerSize = tileredMaps.size();
		for(int curLayer = 0; curLayer < layerSize; curLayer++){
			int reverseLayer = layerSize - (curLayer + 1);
			String layerPath = path + "/" + reverseLayer;
			createFolder(layerPath);
			layerPath += "/";
			Vector<Vector<BufferedImage>> pSubMaps = tileredMaps.get(curLayer);
			int colSize = pSubMaps.size();
			for(int curCol = 0; curCol < colSize; curCol++){
				String curPath = layerPath + curCol;
				createFolder(curPath);
				Vector<BufferedImage> maps = pSubMaps.get(curCol);
				int mapSize = maps.size();
				for(int curMap = 0; curMap < mapSize; curMap++){
					try {
						ImageIO.write(maps.get(curMap), "png", new File(curPath + "/" + curMap + ".png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						throw new ImageWriteException("生成图片文件失败！", e);
					}
				}
			}	
		}
	}
}
