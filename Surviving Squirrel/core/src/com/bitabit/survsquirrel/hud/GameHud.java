package com.bitabit.survsquirrel.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bitabit.survsquirrel.tools.Colores;
import com.bitabit.survsquirrel.tools.EstiloFuente;

public class GameHud implements HeadsUpDisplay{

	private Stage stage;
	private ScreenViewport vw;
	private Table tabla, contenedor;
	private Label hpTag;
	private Label etiqueta2;
	private Table mostrarVida;
	private Label mostrarVidaE;
	private Label.LabelStyle estiloFuente, estiloPeligro, fuenteAdvertencia;
	
	

	public GameHud() {
	    System.out.println("Soy el Hud");
	    crearFuentes(); // Primero crear las fuentes
	    crearActores(); // Después de crear las fuentes
	    poblarStage();
	    stage.setDebugAll(false); // Enable debug lines
	}

	
	@Override
	public void crearActores() {
		vw = new ScreenViewport();
		stage = new Stage(vw);
		tabla = new Table();
		tabla.setFillParent(true);
		contenedor = new Table();
		hpTag = new Label("HP: ", estiloFuente);
		etiqueta2 = new Label("Etiqueta Peligro", estiloPeligro);
		
	}

	@Override
	public void poblarStage() {
		stage.addActor(tabla);
		tabla.add(contenedor).size(1200,680);
		contenedor.add(hpTag).expand().top().left();
	}

	@Override
	public void dibujar() {
		stage.draw();//Dibujar el hud

//		System.out.println("dibujando el hud");
		
	}

	@Override
	public void crearFuentes() {
		estiloFuente = EstiloFuente.generarFuente(40, Colores.AMARILLO, true);	
		estiloPeligro = EstiloFuente.generarFuente(40, Colores.ROJO, false);
		
	}
	
	public void setLabel(String txt) {
		hpTag.setText(txt);
	}
	
	public void setLabel(String txt, int num) {
		String tempTxt = txt + num;
		hpTag.setText(tempTxt);
	}
	
	public void setLabel(String txt, float numf) {
		String tempTxt = txt + numf;
		hpTag.setText(tempTxt);
	}
	
}
