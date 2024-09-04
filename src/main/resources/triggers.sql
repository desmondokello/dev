-- Trigger to update product stock after an order is placed
CREATE TRIGGER update_inventory_after_order
    AFTER INSERT ON OrderItem
    FOR EACH ROW
BEGIN
    UPDATE Inventory
    SET quantity = quantity - NEW.quantity
    WHERE product_id = NEW.product_id;
END;
