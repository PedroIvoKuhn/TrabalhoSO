package entities.enums;

public enum RelacaoTempo {
        OITO_HORAS(16000),
        UMA_HORA(2000),
        QUARENTA_MINUTOS(1333),
        TRINTA_MINUTOS(1000),
        VINTE_MINUTOS(667),
        QUINZE_MINUTOS(500),
        DEZ_MINUTOS(333),
        UM_MINUTO(33);
         
        private final int valor;
         
         RelacaoTempo(int valor) {
            this.valor = valor;
        }
        
        public int getValor() {
            return valor;
        }
        
    }
