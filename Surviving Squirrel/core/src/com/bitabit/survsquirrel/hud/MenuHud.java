package com.bitabit.survsquirrel.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bitabit.survsquirrel.tools.Colores;
import com.bitabit.survsquirrel.tools.EstiloFuente;

public class MenuHud implements HeadsUpDisplay {

	private Stage stage;
	private ScreenViewport vw;
	private Table tabla, contenedor;
	private Label pepitoTag;
	private Label etiqueta2;
	private Table mostrarVida;
	private Label mostrarVidaE;
	private Label.LabelStyle estiloFuente, estiloPeligro, fuenteAdvertencia;



	public MenuHud() {
		System.out.println("Soy el Hud");
		crearFuentes(); // Primero crear las fuentes
		crearActores(); // Después de crear las fuentes
		poblarStage();
		addListeners();
		stage.setDebugAll(true); // Enable debug lines
	}


	@Override
	public void crearActores() {
		vw = new ScreenViewport();
		stage = new Stage(vw);
		tabla = new Table();
		tabla.setFillParent(true);
		contenedor = new Table();
		pepitoTag = new Label("Pepito", estiloFuente);

	}

	@Override
	public void poblarStage() {
		stage.addActor(tabla);
		tabla.add(contenedor).size(1200,680);
		contenedor.add(pepitoTag).expand().top().left();
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
		pepitoTag.setText(txt);
	}

	public void setLabel(String txt, int num) {
		String tempTxt = txt + num;
		pepitoTag.setText(tempTxt);
	}

	public void setLabel(String txt, float numf) {
		String tempTxt = txt + numf;
		pepitoTag.setText(tempTxt);
	}

	public void addListeners() {
		pepitoTag.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Hola");
//				pepitoTag.setColor(Color.BLUE);
			}
			
		}		
				);
		
	}
	
	public Stage getStage() {
		return stage;
	}

}
