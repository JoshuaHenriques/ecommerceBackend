package org.jayhenri.ecommerce.controller.inventory;

import org.jayhenri.ecommerce.controller.InventoryController;
import org.jayhenri.ecommerce.exception.InvalidItemException;
import org.jayhenri.ecommerce.exception.ItemAlreadyExistsException;
import org.jayhenri.ecommerce.exception.ItemNotFoundException;
import org.jayhenri.ecommerce.model.Inventory;
import org.jayhenri.ecommerce.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * The type Inventory controller uni test.
 */
@ExtendWith(MockitoExtension.class)
class InventoryControllerUniTest {

    @Captor
    private ArgumentCaptor<Inventory> captorInventory;

    @Captor
    private ArgumentCaptor<String> captorString;

    @Mock
    private InventoryService inventoryService;

    private InventoryController testMe;

    private Inventory inventory;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        testMe = new InventoryController(inventoryService);
    }

    /**
     * Update item.
     *
     * @throws ItemAlreadyExistsException the item already exists exception
     * @throws InvalidItemException       the invalid item exception
     * @throws ItemNotFoundException      the item not found exception
     */
    @Test
    void updateItem() throws ItemAlreadyExistsException, InvalidItemException, ItemNotFoundException {
        given(inventoryService.existsByProductName(inventory.getProductName())).willReturn(true);

        assertThat(HttpStatus.OK).isEqualTo(testMe.updateItem(inventory).getStatusCode());

        then(inventoryService).should().update(captorInventory.capture());

        assertThat(captorInventory.getValue()).isEqualTo(inventory);
    }

    /**
     * Update item throws invalid item exception.
     */
    @Test
    void updateItemThrowsInvalidItemException() {
        assertThrows(InvalidItemException.class, () -> {
            testMe.updateItem(null);
        });
    }

    /**
     * Update item throws item not found exception.
     */
    @Test
    void updateItemThrowsItemNotFoundException() {
        given(inventoryService.existsByProductName(inventory.getProductName())).willReturn(false);

        assertThrows(ItemNotFoundException.class, () -> {
            testMe.updateItem(inventory);
        });
    }

    /**
     * Add item.
     *
     * @throws ItemAlreadyExistsException the item already exists exception
     * @throws InvalidItemException       the invalid item exception
     */
    @Test
    void addItem() throws ItemAlreadyExistsException, InvalidItemException {
        ResponseEntity<String> response = testMe.addItem(inventory);

        assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());

        then(inventoryService).should().add(captorInventory.capture());

        assertThat(captorInventory.getValue()).isEqualTo(inventory);

    }

    /**
     * Add item throws invalid item exception.
     */
    @Test
    void addItemThrowsInvalidItemException() {
        assertThrows(InvalidItemException.class, () -> {
            testMe.addItem(null);
        });
    }

    /**
     * Add item throws item already exists exception.
     */
    @Test
    void addItemThrowsItemAlreadyExistsException() {
        given(inventoryService.existsByProductName(inventory.getProductName())).willReturn(true);

        assertThrows(ItemAlreadyExistsException.class, () -> {
            testMe.addItem(inventory);
        });
    }

    /**
     * Gets by product name.
     *
     * @throws ItemNotFoundException the item not found exception
     */
    @Test
    void getByProductName() throws ItemNotFoundException {
        given(inventoryService.existsByProductName("Test")).willReturn(true);
        given(inventoryService.getByProductName("Test")).willReturn(inventory);

        ResponseEntity<Inventory> _inventory = testMe.getByProductName("Test");

        assertThat(inventory).isEqualTo(_inventory.getBody());

        then(inventoryService).should().getByProductName(captorString.capture());

        assertThat(captorString.getValue()).isEqualTo("Test");
    }

    /**
     * Gets by product name throws item not found exception.
     */
    @Test
    void getByProductNameThrowsItemNotFoundException() {
        given(inventoryService.existsByProductName("Test")).willReturn(false);

        assertThrows(ItemNotFoundException.class, () -> {
            testMe.getByProductName("Test");
        });
    }

    /**
     * Remove item.
     *
     * @throws InvalidItemException  the invalid item exception
     * @throws ItemNotFoundException the item not found exception
     */
    @Test
    void removeItem() throws InvalidItemException, ItemNotFoundException {
        given(inventoryService.existsByProductName("Test")).willReturn(true);
        given(inventoryService.getByProductName("Test")).willReturn(inventory);

        assertThat(HttpStatus.OK).isEqualTo(testMe.removeItem("Test").getStatusCode());

        then(inventoryService).should().delete(inventory);
    }

    /**
     * Remove item throws invalid item exception.
     */
    @Test
    void removeItemThrowsInvalidItemException() {
        assertThrows(InvalidItemException.class, () -> {
            testMe.removeItem(null);
        });
    }

    /**
     * Remove item throws item not found exception.
     */
    @Test
    void removeItemThrowsItemNotFoundException() {
        given(inventoryService.existsByProductName("Test")).willReturn(false);

        assertThrows(ItemNotFoundException.class, () -> {
            testMe.removeItem("Test");
        });
    }

    /**
     * Find all.
     */
    @Test
    void findAll() {
        assertThat(testMe.findAll().getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}