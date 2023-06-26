package master.ao.storage.core.domain.enums;

import master.ao.storage.core.domain.services.QuantityUpdated;

public enum MovementType implements QuantityUpdated {

    DONATION {
        public Long quantityUpdated(Long quantity, Long newQuantity) {
            return quantity + newQuantity;
        }
    },
    BUY {
        public Long quantityUpdated(Long quantity, Long newQuantity) {
            return quantity + newQuantity;
        }
    },
    ORDER {
        public Long quantityUpdated(Long quantity, Long newQuantity) {
            return newQuantity;
        }
    },
    REQUEST {
        public Long quantityUpdated(Long quantity, Long newQuantity) {
            return newQuantity;
        }
    },
    DEVOLUTION {
        public Long quantityUpdated(Long quantity, Long newQuantity) {
            return quantity + newQuantity;
        }
    },
    EXTERNAL_OUTPUT {
        public Long quantityUpdated(Long quantity, Long newQuantity) {
            return quantity - newQuantity;
        }
    },
    PATIENT_OUTPUT {
        public Long quantityUpdated(Long quantity, Long newQuantity) {
            return quantity - newQuantity;
        }
    },
    DEFAULT_OUTPUT {
        public Long quantityUpdated(Long quantity, Long newQuantity) {
            return quantity - newQuantity;
        }
    },
    ENTITY_OUTPUT {
        public Long quantityUpdated(Long quantity, Long newQuantity) {
            return quantity - newQuantity;
        }
    },


}
