package realestate.model;

public enum PropertyEventType {
    VIEWED, // The property has been viewed by a potential buyer.
    OFFER_MADE, // An offer has been made on the property.
    OFFER_ACCEPTED, // An offer on the property has been accepted.
    OFFER_REJECTED, // An offer on the property has been rejected.
    INSPECTION_SCHEDULED, // An inspection of the property has been scheduled.
    INSPECTION_COMPLETED, // An inspection of the property has been completed.
    SOLD // The property has been sold.
}
