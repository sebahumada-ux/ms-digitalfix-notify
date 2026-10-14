package ms_digitalfix_notify.service;

import ms_digitalfix_notify.dto.WorkOrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(NotificationService.class);

    public void procesarEmail(WorkOrderEvent evento) {

        validarEvento(evento);

        log.info(
                "EMAIL procesado -> Orden={}, Estado={}",
                evento.workOrderId(),
                evento.estado()
        );
    }

    public void procesarDespacho(WorkOrderEvent evento) {

        validarEvento(evento);

        log.info(
                "DESPACHO procesado -> Orden={}, Estado={}",
                evento.workOrderId(),
                evento.estado()
        );
    }

    public void procesarReporte(WorkOrderEvent evento) {

        validarEvento(evento);

        log.info(
                "REPORTE procesado -> Orden={}, Estado={}",
                evento.workOrderId(),
                evento.estado()
        );
    }

    private void validarEvento(WorkOrderEvent evento) {

        if (evento == null) {
            throw new IllegalArgumentException(
                    "El evento no puede ser null"
            );
        }

        if (evento.workOrderId() == null) {
            throw new IllegalArgumentException(
                    "El workOrderId es obligatorio"
            );
        }

        if (evento.tipo() == null || evento.tipo().isBlank()) {
            throw new IllegalArgumentException(
                    "El tipo del evento es obligatorio"
            );
        }

        if (evento.estado() == null || evento.estado().isBlank()) {
            throw new IllegalArgumentException(
                    "El estado del evento es obligatorio"
            );
        }
    }
}