package controller;

import java.util.concurrent.Semaphore;
import view.principal;

public class ThreadFormula extends Thread {

	private int xEscuderia;
	private Semaphore semaforoLargada;
	private Semaphore semaforoEscuderia;
	public static int carrosForaDaPista = 0;

	public ThreadFormula(int id, Semaphore semaforoLargada, Semaphore semaforoEscuderia) {
		this.xEscuderia = id;
		this.semaforoLargada = semaforoLargada;
		this.semaforoEscuderia = semaforoEscuderia;
	}

	@Override
	public void run() {
		for (int i = 1; i < 3; i++) {
			try {
				semaforoLargada.acquire();
				CarroAndando(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				semaforoLargada.release();
				System.out.println("O carro " + i + " da escuderia " + xEscuderia + " saiu da pista");
				carrosForaDaPista++;
			}
		}
		if (carrosForaDaPista == 14) {
			OrdenaGrid();
		}
	}
	private void CarroAndando(int carro) {
		System.out.println("O carro " + carro + " da escuderia " + xEscuderia + " entrou na pista");
		for (int i = 1; i < 4; i++) {
			int tempoVolta = (int) ((Math.random() * 180) + 60);
			try {
				sleep(tempoVolta * 30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Escuderia: " + xEscuderia + " Carro: " + carro + " Volta: " + i + " Tempo: "
					+ tempoVolta + " segundos");
			try {
				semaforoEscuderia.acquire();
				if (tempoVolta < principal.valorVoltas[(2 * xEscuderia) - carro]
						|| principal.valorVoltas[(2 * xEscuderia) - carro] == 0) {
					principal.valorVoltas[(2 * xEscuderia - 2 + carro) - 1] = tempoVolta;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				semaforoEscuderia.release();
			}
		}
	}
	public void OrdenaGrid() {
		int aux;
		String auxiliar;
		for (int i = 0; i < 13; i++) {
			for (int j = i + 1; j < 14; j++) {
				if (principal.valorVoltas[i] > principal.valorVoltas[j]) {
					aux = principal.valorVoltas[i];
					principal.valorVoltas[i] = principal.valorVoltas[j];
					principal.valorVoltas[j] = aux;
					auxiliar = principal.textoVoltas[i];
					principal.textoVoltas[i] = principal.textoVoltas[j];
					principal.textoVoltas[j] = auxiliar;
				}
			}
		}
		for (int i = 0; i < 14; i++) {
			System.out.println(
					"Posição " + (i + 1) + ": " + principal.textoVoltas[i] + principal.valorVoltas[i] + " segundos");
		}
	}

}