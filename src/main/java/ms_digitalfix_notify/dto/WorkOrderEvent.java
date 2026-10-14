package ms_digitalfix_notify.dto;

public record WorkOrderEvent(
        String tipo,
        Long workOrderId,
        String estado,
        String timestamp
) {
}