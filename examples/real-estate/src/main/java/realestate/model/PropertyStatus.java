package realestate.model;

public enum PropertyStatus {
    LISTED, // The property has been listed for sale.
    UNDER_CONTRACT, // A potential buyer has made an offer, which the seller has accepted.
    INSPECTION, // The property is under inspection.
    APPROVED, // The sale has been approved, typically after successful inspection and financing.
    SOLD // The property has been sold.
}
