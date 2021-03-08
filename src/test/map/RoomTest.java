package test.map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.map.Room;

class RoomTest {

	@Test
	void testBufferIntersects() {
		Room r1 = new Room(10, 10, 20, 20);
		Room r2 = new Room(8, 8, 20, 20);
		Room r3 = new Room(35, 35, 10, 10);
		Room r4 = new Room(12, 12, 16, 16);
		
		assertTrue(Room.bufferIntersects(r1, r2, 0));
		assertTrue(Room.bufferIntersects(r1, r2, 5));
		assertTrue(Room.bufferIntersects(r1, r2, 100));
		
		assertTrue(Room.bufferIntersects(r2, r1, 0));
		assertTrue(Room.bufferIntersects(r2, r1, 5));
		assertTrue(Room.bufferIntersects(r2, r1, 100));
		
		assertFalse(Room.bufferIntersects(r1, r3, 0));
		assertTrue(Room.bufferIntersects(r1, r3, 6));
		
		assertFalse(Room.bufferIntersects(r3, r1, 0));
		assertTrue(Room.bufferIntersects(r3, r1, 6));
		
		assertTrue(Room.bufferIntersects(r1, r4, 0));
		assertTrue(Room.bufferIntersects(r1, r4, 8));
		

		assertTrue(Room.bufferIntersects(r4, r1, 0));
		assertTrue(Room.bufferIntersects(r4, r1, 8));
	}

}
