import java.util.LinkedList;
import java.util.Scanner;

public class Temperaturas {
	LinkedList<Double> valoresRecebidos = new LinkedList<Double>();
	LinkedList<Double> mediasAnteriores = new LinkedList<Double>();

	double variavel = 10; // quanto maior o valor da variavel mais rapido se vai alertar
	double limiteTempSup = 45;
	double limiteTempInf = 0;
	/** ACABAR ISTO COM UMA FORMULA MANHOSA **/
	int contadorVermelho = 0;
	int contadorAmarelo = 0;
	private static final int ESPACAMENTO_ENTRE_ALERTAS=10;
	MongoToMySQL contact;

	public Temperaturas(MongoToMySQL contact) {
		this.contact = contact;
	}

	private double calcularMediaAnterior() {
		double sum = 0;
		for (int i = 0; i != mediasAnteriores.size(); i++) {
			sum += valoresRecebidos.get(i);
		}
		// System.out.println("media valores recebidos anterior:"
		// +sum/mediasAnteriores.size());
		return sum / mediasAnteriores.size();
	}

	public void processar(double num) {

		double mediaAnterior = calcularMediaAnterior(); // Media que vai entrar
		double media5InstantesAntes = Double.NaN; // � preciso inicializar a NaN para termos uma condi��o em baixo para
													// n�o prever
													// antes de ter 5 elementos na lista

		if (mediasAnteriores.size() == 5) {
			media5InstantesAntes = mediasAnteriores.poll(); // Media que vai sair da lista, isto s� acontece depois de 5
															// ciclos
		}
		/*
		 * Aqui vemos para o quente
		 */
		// System.out.println("vou comparar " + num + " com " + limiteTempSup);
		if (num >= limiteTempSup && contadorVermelho == 0) {
			System.err.println("Alerta HOT HOT HOT!!!");
			contact.writeAlertaToMySQL("TEM", num + "", limiteTempSup + "", "Santar�m", 1 + "", ""); // Este vai ser a
																										// vermelho
			contadorVermelho = ESPACAMENTO_ENTRE_ALERTAS;
		} else {
			if (contadorVermelho > 0) {
				contadorVermelho--;
			}
			if (!Double.isNaN(media5InstantesAntes)) { // prever aumento
				double calc = (mediaAnterior - media5InstantesAntes) * variavel + num;
				if (calc >= limiteTempSup && contadorAmarelo == 0) {
					System.err.println("Alerta Temperatura a aumentar!!!");
					contadorAmarelo = ESPACAMENTO_ENTRE_ALERTAS;
					// TODO Alerta Temp alta
					contact.writeAlertaToMySQL("TEM", num + "", limiteTempSup + "", "Temperatura a aumentar", 0 + "",
							""); // Este vai ser a AMARELO
				} else {
					if (contadorAmarelo > 0) {
						contadorAmarelo--;
					}
				}
			}
		}
		/*
		 * Aqui vemos para o frio
		 */
		if (num <= limiteTempInf && contadorVermelho == 0) {
			System.err.println("Alerta COLD COLD COLD!!!");
			contact.writeAlertaToMySQL("TEM", num + "", limiteTempInf + "", "Fresquinho", 1 + "", ""); // Este vai ser a
																										// vermelho
			contadorVermelho = ESPACAMENTO_ENTRE_ALERTAS;
		} else {
			if(contadorVermelho > 0) {
				contadorVermelho--;
			}
			if (!Double.isNaN(media5InstantesAntes)) {
				double calcneg = (media5InstantesAntes - mediaAnterior) * variavel + num;
				if (calcneg <= limiteTempInf && contadorAmarelo == 0) {
					System.err.println("Alerta Temperatura a diminuir!!!");
					// TODO Alerta Temp baixa
					contact.writeAlertaToMySQL("TEM", num + "", limiteTempInf + "", "Temperatura a diminuir", 0 + "","");// Este vai ser a amarelo
					contadorAmarelo = ESPACAMENTO_ENTRE_ALERTAS;
				} else {
					if (contadorAmarelo > 0) {
						contadorAmarelo--;
					}
				}
			}
		}
		if (valoresRecebidos.size() == 5)
			valoresRecebidos.removeFirst();
		valoresRecebidos.addLast(num);
		if (mediasAnteriores.size() == 5)
			mediasAnteriores.removeFirst();
		mediasAnteriores.addLast(mediaAnterior);

	}

	public void updateLimite(double limit) {
		this.limiteTempSup = limit;
		this.limiteTempInf = limit - limit;
		System.out.println("mudei o limite para " + limiteTempSup);
	}
}
