package com.anbot.server.maptiler.pgmtool;


import java.io.*;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.*;

/**
 * @author suwan
 *
 */
public class PGMToPNG
{
	int iw, ih;
	boolean rawBits;
    
	/**
	 * pgm图转png图
	 * @param path
	 * @return
	 */
	public BufferedImage pgmToPng(String path){
		int c;
		BufferedImage bufImg = null;
		byte[] bpix = openFile(path);
		int[] pix = new int[iw*ih];
    	for(int i= 0; i<iw*ih; i++)
    	{
    	    if(bpix[i]<0) 
    	    	c = 256+bpix[i];
    	    else          
    	    	c = bpix[i];                	    
    	    pix[i]= (255<<24)|(c<<16)|(c<<8)|c;
    	}
    	
    	ImageProducer ip = new MemoryImageSource(iw, ih, pix, 0, iw);
    	Image iImage = Toolkit.getDefaultToolkit().createImage(ip);
    	bufImg = new BufferedImage(iImage.getWidth(null), iImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufImg.createGraphics();
        g.drawImage(iImage, 0, 0, null);
        g.dispose();
        try {
			ImageIO.write(bufImg, "png", new File(path.substring(0, path.lastIndexOf("."))+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bufImg;
	}
	
    public byte[] openFile(String path) 
    {
		InputStream is = null;
		StreamTokenizer tok = null;
		byte[] pixels = null;
		try{			
		    is = new BufferedInputStream(new FileInputStream(path));
			
		    tok = new StreamTokenizer(is);
			tok.resetSyntax();
			tok.wordChars(33, 255);
			tok.whitespaceChars(0, ' ');
			tok.parseNumbers();
			tok.eolIsSignificant(true);
			tok.commentChar('#');
			
			openHeader(tok);	
		    pixels = new byte[iw*ih];
		    if (rawBits)              //for "P5" 
		        openP5Image(is, iw*ih, pixels);
		    else                      //for "P2"
			    openP2Image(tok, iw*ih, pixels);		    
		}
		catch(IOException e1){}
		return pixels;
	}

	public void openHeader(StreamTokenizer tok) throws IOException 
	{
		String magicNumber = getWord(tok);
		if (magicNumber.equals("P5"))
			rawBits = true;
		else if (!magicNumber.equals("P2"))
			throw new IOException("PGM files must start with \"P2\" or \"P5\"");
		iw = getInt(tok);
		ih = getInt(tok);
		int maxValue = getInt(tok);
		if (iw==-1 || ih==-1 || maxValue==-1)
			throw new IOException("Error opening PGM header..");
		if (maxValue>255)
			throw new IOException("The maximum gray vale is larger than 255.");
	}
	
	public void openP5Image(InputStream is, int size, 
	                        byte[] pixels) throws IOException 
	{
		int count = 0;
		while (count<size && count>=0)
			count = is.read(pixels, count, size-count);
	}
	
	public void openP2Image(StreamTokenizer tok, int size, 
	                        byte[] pixels) throws IOException 
	{
		int i = 0;
		int inc = size/20;
		while (tok.nextToken() != tok.TT_EOF)		
			if (tok.ttype == tok.TT_NUMBER) 
				pixels[i++] = (byte)(((int)tok.nval)&255);				
	}
	
	String getWord(StreamTokenizer tok) throws IOException 
	{
		while (tok.nextToken() != tok.TT_EOF) 
			if (tok.ttype == tok.TT_WORD)
				return tok.sval;
		
		return null;
	}

	int getInt(StreamTokenizer tok) throws IOException 
	{
		while (tok.nextToken() != tok.TT_EOF)		
			if (tok.ttype == tok.TT_NUMBER)
				return (int)tok.nval;
		
		return -1;
	}
	
    public int getWidth()
    {
    	return iw;
    }
    
    public int getHeight()
    {
    	return ih;
    }
}