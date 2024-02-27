 @Test
    public void testClearMemory() {
        // Arrange
        MemoryDAO memoryDAO = new MemoryDAO(); // Using a stub for testing
        ClearService clearService = new ClearService(memoryDAOStub);

        // Act
        clearService.clearMemory();

        // Assert
        assertEquals(true, memoryDAOStub.isClearMethodCalled(), "MemoryDAO clear method should be called");
    }