package com.bitabit.survsquirrel.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public abstract class EstiloFuente {
	public static Label.LabelStyle labelStyle;
	
	public static Label.LabelStyle generarFuente (int tamano, String hex, boolean sombra) {
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Rutas.FUENTE));
	    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
	    
	    parameter.size = tamano;
	    parameter.color = Color.valueOf(hex);
	    parameter.borderWidth = 1;
	    parameter.minFilter = Texture.TextureFilter.Linear;//esto hace que no se vea tan mal cuando se reescala
	    parameter.magFilter = Texture.TextureFilter.Linear;//esto hace que no se vea tan mal cuando se reescala
	    if(sombra) {
	    	parameter.shadowOffsetX = 3;
	    	parameter.shadowOffsetY = 3;
	    	parameter.shadowColor = parameter.color;
	    }
	    
	    BitmapFont font24 = generator.generateFont(parameter); // tamaño de la fuente 24 pixeles
	    generator.dispose();
	 
	    labelStyle = new Label.LabelStyle();
	    labelStyle.font = font24;
	    return labelStyle;
	}
	
}
