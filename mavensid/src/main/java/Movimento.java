public class Movimento {
	double variavel = 10;//quanto maior o valor da variavel mais rapido se vai alertar
	int contador = 0; //contador para nao tar sempre a enviar alertas
	private static final int ESPACAMENTO_ENTRE_ALERTAS=10;
	MongoToMySQL contact;
	
	public Movimento(MongoToMySQL contact) {
		this.contact=contact;
	}
	
		
	public void processar(Double num) {
		if(num >= 1 && contador == 0) {
			if(contact.verRondas()) {
				System.err.println("N�O ESCREVI PORQUE HAVIA RONDA");
				return;
			}
			System.err.println("Alerta Movimentos!!!");
			contact.writeAlertaToMySQL("MOV", num+"", 1+"", "Movimentos a acontecer", 1+"", "");
			contador=ESPACAMENTO_ENTRE_ALERTAS;
		}else {
			if(contador>0)
				contador--;
		}
	}
}
